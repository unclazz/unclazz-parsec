package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Function;

/**
 * パーサーを表す抽象クラスです。
 * <p>このパーサーはパース処理にあたってパース対象の値をキャプチャせず、パースの成功・失敗の情報だけを返します。
 * キャプチャが必要な場合は値をキャプチャするパーサーである{@link ValParser}の派生クラスを利用します。
 * {@link ValParser}のインスタンスは{@link Parser#val()}や{@link Parser#map(Function)}メソッドから得られます。</p>
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
	 * @return パース結果（{@code null}ではない）
	 * @throws IOException データソースから例外がスローされた場合
	 */
	protected abstract ResultCore doParse(Context ctx) throws IOException;
	
	/**
	 * パースを行います。
	 * @param text データソースとなるテキスト
	 * @return 
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
	 * @return
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public Result parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	/**
	 * パースを行います。
	 * @param ctx データソースへのアクセスを提供するコンテキスト
	 * @return
	 * @throws IOException データソースから例外がスローされた場合
	 */
	public Result parse(Context ctx) throws IOException{
		final TextReader src = ctx.source();
		final CharPosition startedOn = src.position();
		ctx.preParse(name());
		final ResultCore core = doParse(ctx);
		ctx.postParse(core);
		if (core instanceof Result) return (Result) core;
		final CharPosition endedOn = src.position();
		return core.attachPosition(startedOn, endedOn);
	}
	
	/**
	 * パース成功を表すオブジェクトを返します。
	 * @return
	 */
	protected ResultCore success(){
		return ResultCore.ofSuccess();
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param message 失敗の理由を示すメッセージ
	 * @return
	 */
	protected ResultCore failure(String message){
		return ResultCore.ofFailure(message);
	}
	/**
	 * パース失敗を表すオブジェクトを返します。
	 * @param format 失敗の理由を示すメッセージのフォーマット
	 * @param args フォーマット引数
	 * @return
	 */
	protected ResultCore failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	/**
	 * パースした文字列をキャプチャするパーサーを返します。
	 * @return
	 */
	public ValParser<String> val(){
		return new CaptureParser(this);
	}
	/**
	 * パースした文字列に関数を適用するパーサーを返します。
	 * @param func
	 * @return
	 */
	public<T> ValParser<T> map(Function<String, T> func){
		return val().map(func);
	}
	/**
	 * オプションのトークンにマッチするパーサーを返します。
	 * @return
	 */
	public Parser orNot() {
		return new OptParser(this);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return
	 */
	public Parser then(Parser other){
		return new ThenParser(this, other);
	}
	/**
	 * シーケンスを読み取るパーサーを返します。
	 * @param other 次のトークンを読み取るパーサー
	 * @return
	 */
	public<T> ValParser<T> then(ValParser<T> other){
		return new ThenTakeRightParser<>(this, other);
	}
}
