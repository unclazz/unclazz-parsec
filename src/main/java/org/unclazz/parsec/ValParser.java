package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.unclazz.parsec.data.ParserFactory;
import org.unclazz.parsec.data.Tuple2;
import org.unclazz.parsec.data.ValParserFactory;

/**
 * パーサーを表す抽象クラスです。
 * <p>このパーサーはパースの成功・失敗の情報とともにキャプチャした値も返します。</p>
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
	 * @return パース結果（{@code null}ではない）
	 * @throws IOException データソースから例外がスローされた場合
	 */
	protected abstract ValResultCore<T> doParse(Context ctx) throws IOException;
	
	/**
	 * パースを行います。
	 * @param text データソースとなるテキスト
	 * @return 
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
	 * @return
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public ValResult<T> parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	/**
	 * パースを行います。
	 * @param ctx データソースへのアクセスを提供するコンテキスト
	 * @return
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
	 * @return
	 */
	protected ValResultCore<T> success(T capture){
		return ValResultCore.ofSuccess(capture);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param message 失敗の理由を示すメッセージ
	 * @return
	 */
	protected ValResultCore<T> failure(String message){
		return ValResultCore.ofFailure(message);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param format 失敗の理由を示すメッセージのフォーマット
	 * @param args フォーマット引数
	 * @return
	 */
	protected ValResultCore<T> failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	/**
	 * パーサーのキャプチャ内容を破棄します。
	 * @return
	 */
	public Parser unval() {
		return new UncaptureParser<>(this);
	}
	/**
	 * このパーサーのパースが成功すると直近の{@link #or(ValParser)}を起点とするバックトラックが無効になります。
	 * @return
	 */
	public ValParser<T> cut(){
		return new CutValParser<>(this);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other
	 * @return
	 */
	public ValParser<T> or(ValParser<T> other){
		return new OrValParser<>(this, other);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param func
	 * @return
	 */
	public ValParser<T> or(ValParserFactory<T> func){
		return new OrValParser<>(this, lazy(func));
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other
	 * @return
	 */
	public Parser or(Parser other){
		return new OrParser(this.unval(), other);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param func
	 * @return
	 */
	public Parser or(ParserFactory func){
		return new OrParser(this.unval(), lazy(func));
	}
	/**
	 * キャプチャ結果に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。</p>
	 * @param func
	 * @return
	 */
	public<U> ValParser<U> map(Function<T, U> func){
		return new MapValParser<>(this, func, false);
	}
	/**
	 * キャプチャ結果に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。
	 * ただし引数{@code canThrow}に{@code true}が設定されている場合は例外はそのまま再スローされます。</p>
	 * @param func
	 * @param canThrow
	 * @return
	 */
	public<U> ValParser<U> map(Function<T, U> func, boolean canThrow){
		return new MapValParser<>(this, func, canThrow);
	}
	/**
	 * パース結果の値を元に動的にパーサーを構築するパーサーを返します。
	 * @param func
	 * @return
	 */
	public<U> ValParser<U> flatMap(Function<T, ValParser<U>> func){
		return new FlatMapValParser<>(this, func);
	}
	/**
	 * オプションのトークンにマッチするパーサーを返します。
	 * @return
	 */
	public ValParser<Optional<T>> opt() {
		return new OptValParser<T>(this);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return
	 */
	public ValParser<T> then(Parser other){
		return new ThenTakeLeftParser<>(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param func 次のトークンを読み取るパーサーのファクトリー
	 * @return
	 */
	public ValParser<T> then(ParserFactory func){
		return new ThenTakeLeftParser<>(this, lazy(func));
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @param<U> 
	 * @return
	 */
	public<U> ValParser<Tuple2<T, U>> then(ValParser<U> other){
		return new Tuple2Parser<>(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param func 次のトークンを読み取るパーサーのファクトリー
	 * @param<U> 
	 * @return
	 */
	public<U> ValParser<Tuple2<T, U>> then(ValParserFactory<U> func){
		return new Tuple2Parser<>(this, lazy(func));
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @return
	 */
	public RepeatValParser<T> rep(){
		return new RepeatValParser<>(this, 0, -1, -1, null);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param sep
	 * @return
	 */
	public RepeatValParser<T> rep(Parser sep){
		return new RepeatValParser<>(this, 0, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @return
	 */
	public RepeatValParser<T> repMin(int min) {
		return new RepeatValParser<>(this, min, -1, -1, null);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param sep
	 * @return
	 */
	public RepeatValParser<T> repMin(int min, Parser sep) {
		return new RepeatValParser<>(this, min, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @return
	 */
	public RepeatValParser<T> rep(int min, int max) {
		return new RepeatValParser<>(this, min, max, -1, null);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @param sep
	 * @return
	 */
	public RepeatValParser<T> rep(int min, int max, Parser sep) {
		return new RepeatValParser<>(this, min, max, -1, sep);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @return
	 */
	public RepeatValParser<T> rep(int exactly) {
		return new RepeatValParser<>(this, -1, -1, exactly, null);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @param sep
	 * @return
	 */
	public RepeatValParser<T> rep(int exactly, Parser sep) {
		return new RepeatValParser<>(this, -1, -1, exactly, sep);
	}
}
