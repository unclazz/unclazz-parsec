package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * パーサーを表す抽象クラスです。
 * <p>このパーサーはパース処理にあたってパース対象の値をキャプチャせず、パースの成功・失敗の情報だけを返します。
 * キャプチャが必要な場合は値をキャプチャするパーサーである{@link ValParser}の派生クラスを利用します。
 * {@link ValParser}のインスタンスは{@link #val()}や{@link #map(Function)}や{@link #means(Object)}メソッドから得られます。</p>
 */
public abstract class Parser extends ParserSupport {
	/**
	 * 引数なしのコンストラクタです。
	 * <p>
	 * このコンストラクタで初期化されたインスタンスの{@link #name()}メソッドは
	 * クラス名の末尾の{@code "Parser"}をカットした文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 */
	protected Parser() {}
	/**
	 * パーサーの名前を引数にとるコンストラクタです。
	 * <p>
	 * 初期化されたインスタンスの{@link #name()}メソッドは
	 * このコンストラクタの引数で指定した文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 * @param name パーサーの名前
	 */
	protected Parser(String name) {
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
	protected abstract ResultCore doParse(Context ctx) throws IOException;
	
	/**
	 * パースを行います。
	 * @param text データソースとなるテキスト
	 * @return パース結果オブジェクト
	 */
	public Result parse(String text) {
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
	public Result parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	/**
	 * パースを行います。
	 * @param ctx データソースへのアクセスを提供するコンテキスト
	 * @return パース結果オブジェクト
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public Result parse(Context ctx) throws IOException{
		final TextReader src = ctx.source();
		final CharPosition startedOn = src.position();
		ctx.preParse(name(), paramsString());
		final ResultCore core = doParse(ctx);
		ctx.postParse(core);
		if (core instanceof Result) return (Result) core;
		final CharPosition endedOn = src.position();
		return core.attachPosition(startedOn, endedOn);
	}
	
	/**
	 * パース成功を表すオブジェクトを返します。
	 * @return パース結果オブジェクト
	 */
	protected ResultCore success(){
		return ResultCore.ofSuccess();
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param message 失敗の理由を示すメッセージ
	 * @return パース結果オブジェクト
	 */
	protected ResultCore failure(String message){
		return ResultCore.ofFailure(message);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param format 失敗の理由を示すメッセージのフォーマット
	 * @param args フォーマット引数
	 * @return パース結果オブジェクト
	 */
	protected ResultCore failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	/**
	 * パースした文字列をキャプチャするパーサーを返します。
	 * @return パーサー
	 */
	public ValParser<String> val(){
		return new CaptureParser(this);
	}
	/**
	 * パースした文字列に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。</p>
	 * @param func 値のマッピングを行う関数
	 * @return パーサー
	 * @param <T> 関数によるマッピングの結果型
	 */
	public<T> ValParser<T> map(Function<String, T> func){
		return val().map(func);
	}
	/**
	 * パースした文字列に関数を適用するパーサーを返します。
	 * <p>関数適用時に例外がスローされた場合、例外のメッセージが{@link ValResult#message()}に設定されます。
	 * ただし引数{@code canThrow}に{@code true}が設定されている場合は例外はそのまま再スローされます。</p>
	 * @param func 値のマッピングを行う関数
	 * @param canThrow マッピングを行う関数が例外スローした時それを再スローさせたい場合{@code true}
	 * @return パーサー
	 * @param <T> 関数によるマッピングの結果型
	 */
	public<T> ValParser<T> map(Function<String, T> func, boolean canThrow){
		return val().map(func, canThrow);
	}
	/**
	 * パース結果の値を元に動的にパーサーを構築するパーサーを返します。
	 * @param func パース結果を受け取りパーサーを返す関数
	 * @return パーサー
	 * @param <T> 関数が返すパーサーの読み取り結果型
	 */
	public<T> ValParser<T> flatMap(Function<String, ValParser<T>> func){
		return val().flatMap(func);
	}
	/**
	 * オプションのトークンにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public Parser opt() {
		return new OptParser(this);
	}
	/**
	 * このパーサーのパースが成功すると直近の{@link #or(ValParser)}を起点とするバックトラックが無効になります。
	 * @return パーサー
	 */
	public Parser cut() {
		return new CutParser(this);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other 別のパーサー
	 * @return パーサー
	 */
	public Parser or(Parser other) {
		return new OrParser(this, other);
	}
	/**
	 * このパーサーのパースが成功すればその結果を、さもなくば引数のパーサーの結果を返します。
	 * @param other 別のパーサー
	 * @return パーサー
	 * @param <T> 引数のパーサーの読み取り結果型
	 */
	public<T> ValParser<Optional<T>> or(ValParser<T> other){
		return new OrOptParser<>(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return パーサー
	 */
	public Parser then(Parser other){
		return new ThenParser(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return パーサー
	 * @param <T> 引数のパーサーの読み取り結果型
	 */
	public<T> ValParser<T> then(ValParser<T> other){
		return new ThenTakeRightParser<>(this, other);
	}
	/**
	 * パース成功時に指定した値を返すパーサーを返します。
	 * @param value 任意の値
	 * @return パーサー
	 * @param <T> 引数の値の型
	 */
	public<T> ValParser<T> means(T value){
		return new MeansValParser<>(this, () -> value);
	}
	/**
	 * パース成功時に指定した値を返すパーサーを返します。
	 * @param func 値を供給する関数
	 * @return パーサー
	 * @param <T> 関数が供給する値の型
	 */
	public<T> ValParser<T> means(Supplier<T> func){
		return new MeansValParser<>(this, func);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public RepeatParser rep() {
		return new RepeatParser(this, 0, -1, -1, null);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatParser rep(Parser sep) {
		return new RepeatParser(this, 0, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	public RepeatParser repMin(int min) {
		return new RepeatParser(this, min, -1, -1, null);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatParser repMin(int min, Parser sep) {
		return new RepeatParser(this, min, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param max 繰返しの最大回数
	 * @return パーサー
	 */
	public RepeatParser rep(int min, int max) {
		return new RepeatParser(this, min, max, -1, null);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @param max 繰返しの最大回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatParser rep(int min, int max, Parser sep) {
		return new RepeatParser(this, min, max, -1, sep);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly 繰返しの回数
	 * @return パーサー
	 */
	public RepeatParser rep(int exactly) {
		return new RepeatParser(this, -1, -1, exactly, null);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly 繰返しの回数
	 * @param sep セパレータのパーサー
	 * @return パーサー
	 */
	public RepeatParser rep(int exactly, Parser sep) {
		return new RepeatParser(this, -1, -1, exactly, sep);
	}
}
