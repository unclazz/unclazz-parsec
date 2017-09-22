package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.unclazz.parsec.data.Seq;
import org.unclazz.parsec.data.Tuple2;

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
	 * キャプチャ結果に関数を適用するパーサーを返します。
	 * @param func
	 * @return
	 */
	public<U> ValParser<U> map(Function<T, U> func){
		return new MapValParser<>(this, func);
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
	 * @param other 次のトークンを読み取るパーサー
	 * @param<U> 
	 * @return
	 */
	public<U> ValParser<Tuple2<T, U>> then(ValParser<U> other){
		return new Tuple2Parser<>(this, other);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @return
	 */
	public ValParser<Seq<T>> rep(){
		return new RepeatValParser<>(this, 0, -1, -1, null);
	}
	/**
	 * パターンの0回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> rep(Parser sep){
		return new RepeatValParser<>(this, 0, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @return
	 */
	public ValParser<Seq<T>> repMin(int min) {
		return new RepeatValParser<>(this, min, -1, -1, null);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> repMin(int min, Parser sep) {
		return new RepeatValParser<>(this, min, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @return
	 */
	public ValParser<Seq<T>> rep(int min, int max) {
		return new RepeatValParser<>(this, min, max, -1, null);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> rep(int min, int max, Parser sep) {
		return new RepeatValParser<>(this, min, max, -1, sep);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @return
	 */
	public ValParser<Seq<T>> rep(int exactly) {
		return new RepeatValParser<>(this, -1, -1, exactly, null);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> rep(int exactly, Parser sep) {
		return new RepeatValParser<>(this, -1, -1, exactly, sep);
	}
}
