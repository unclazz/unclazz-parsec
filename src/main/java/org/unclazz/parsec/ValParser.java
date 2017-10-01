package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.unclazz.parsec.util.Tuple2;

/**
 * パーサーを表す抽象クラスです。
 * <p>このパーサーはパースの成功・失敗の情報とともにキャプチャした値も返します。</p>
 * @param <T> 読み取り結果型
 */
public abstract class ValParser<T> extends ParserSupport {
	/**
	 * 引数なしのコンストラクタです。
	 * <p>
	 * このコンストラクタで初期化されたインスタンスの{@link #name()}メソッドは
	 * クラス名の末尾の{@code "Parser"}をカットした文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 */
	protected ValParser() { }
	/**
	 * パーサーの名前を引数にとるコンストラクタです。
	 * <p>
	 * 初期化されたインスタンスの{@link #name()}メソッドは
	 * このコンストラクタの引数で指定した文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 * @param name パーサーの名前
	 */
	protected ValParser(String name) {
		super(name);
	}
	
	/**
	 * パースを行います。
	 * <p>このメソッドのコードは原則として例外をスローしてはいけません。
	 * パース過程において発生した何かしらの異常な状態はすべて{@link #failure(String)}とその多重定義を用いて通知される必要があります。
	 * throwsステートメントで宣言されている{@link IOException}は、
	 * 文字通りデータソースからの文字の読み取り中にデータソースから例外が当該の例外がスローされた場合を示すものです。</p>
	 * @param ctx コンテキスト
	 * @return パース結果オブジェクト
	 * @throws IOException データソースから例外がスローされた場合
	 */
	protected abstract ValResultCore<T> doParse(Context ctx) throws IOException;
	
	/**
	 * パースを行います。
	 * @param text データソースとなるテキスト
	 * @return パース結果オブジェクト
	 */
	public ValResult<T> parse(String text) {
		try {
			return parse(TextReader.from(text));
		} catch (final IOException ex) {
			throw new RuntimeException("unexpected error has occurred.", ex);
		}
	}
	/**
	 * パースを行います。
	 * @param reader データソースとなるリーダー
	 * @return パース結果オブジェクト
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public ValResult<T> parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	/**
	 * パースを行います。
	 * @param ctx データソースへのアクセスを提供するコンテキスト
	 * @return パース結果オブジェクト
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public ValResult<T> parse(Context ctx) throws IOException{
		final TextReader src = ctx.source();
		final CharPosition startedOn = src.position();
		ctx.preParse(name());
		final ValResultCore<T> core = doParse(ctx);
		ctx.postParse(core);
		if (core instanceof ValResult) return (ValResult<T>) core;
		final CharPosition endedOn = src.position();
		return core.attachPosition(startedOn, endedOn);
	}
	/**
	 * パース成功を表すオブジェクトを返します。
	 * @param capture キャプチャした値
	 * @return パース結果オブジェクト
	 */
	protected ValResultCore<T> success(T capture){
		return ValResultCore.ofSuccess(capture);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param message 失敗の理由を示すメッセージ
	 * @return パース結果オブジェクト
	 */
	protected ValResultCore<T> failure(String message){
		return ValResultCore.ofFailure(message);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param format 失敗の理由を示すメッセージのフォーマット
	 * @param args フォーマット引数
	 * @return パース結果オブジェクト
	 */
	protected ValResultCore<T> failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	/**
	 * パーサーのキャプチャ内容を破棄します。
	 * @return キャプチャを行わないパーサー
	 */
	public Parser unval() {
		return new UncaptureParser<>(this);
	}
	/**
	 * このパーサーのパースが成功すると直近の{@link #or(ValParser)}を起点とするバックトラックが無効になります。
	 * @return パーサー
	 */
	public ValParser<T> cut(){
		return new CutValParser<>(this);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other 別のパーサー
	 * @return パーサー
	 */
	public ValParser<T> or(ValParser<T> other){
		return new OrValParser<>(this, other);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other 別のパーサー
	 * @return パーサー
	 */
	public ValParser<Optional<T>> or(Parser other){
		return new OptOrParser<>(this, other);
	}
	/**
	 * キャプチャ結果に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。</p>
	 * @param func 値のマッピングを行う関数
	 * @return パーサー
	 * @param <U> 関数によるマッピングの結果型
	 */
	public<U> ValParser<U> map(Function<T, U> func){
		return new MapValParser<>(this, func, false);
	}
	/**
	 * パースした文字列に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。
	 * ただし引数{@code canThrow}に{@code true}が設定されている場合は例外はそのまま再スローされます。</p>
	 * @param func 値のマッピングを行う関数
	 * @param canThrow マッピングを行う関数が例外スローした時それを再スローさせたい場合{@code true}
	 * @return パーサー
	 * @param <U> 関数によるマッピングの結果型
	 */
	public<U> ValParser<U> map(Function<T, U> func, boolean canThrow){
		return new MapValParser<>(this, func, canThrow);
	}
	/**
	 * パース結果の値を元に動的にパーサーを構築するパーサーを返します。
	 * @param func パース結果を受け取りパーサーを返す関数
	 * @return パーサー
	 * @param <U> 関数が返すパーサーの読み取り結果型
	 */
	public<U> ValParser<U> flatMap(Function<T, ValParser<U>> func){
		return new FlatMapValParser<>(this, func);
	}
	/**
	 * オプションのトークンにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public ValParser<Optional<T>> opt() {
		return new OptValParser<T>(this);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return パーサー
	 */
	public ValParser<T> then(Parser other){
		return new ThenTakeLeftParser<>(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @param <U> 引数のパーサーの読み取り結果型
	 * @return パーサー
	 */
	public<U> ValParser<Tuple2<T, U>> then(ValParser<U> other){
		return new Tuple2Parser<>(this, other);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(){
		return new RepeatValParser<>(this, 0, -1, -1, null);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(Parser sep){
		return new RepeatValParser<>(this, 0, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	public RepeatValParser<T> repMin(int min) {
		return new RepeatValParser<>(this, min, -1, -1, null);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatValParser<T> repMin(int min, Parser sep) {
		return new RepeatValParser<>(this, min, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param max 繰返しの最大回数
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(int min, int max) {
		return new RepeatValParser<>(this, min, max, -1, null);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param max 繰返しの最大回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(int min, int max, Parser sep) {
		return new RepeatValParser<>(this, min, max, -1, sep);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly 繰返しの回数
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(int exactly) {
		return new RepeatValParser<>(this, -1, -1, exactly, null);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly 繰返しの回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatValParser<T> rep(int exactly, Parser sep) {
		return new RepeatValParser<>(this, -1, -1, exactly, sep);
	}
}
