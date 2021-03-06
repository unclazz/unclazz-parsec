package org.unclazz.parsec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * {@link Parser}や{@link ValParser}の入力データソースとなるリーダーです。
 */
public final class TextReader extends ResetReader {
	/**
	 * 別のリーダーをラップした新しいインスタンスを返します。
	 * @param reader 別のリーダー
	 * @return 新しいリーダー
	 */
	public static TextReader from(Reader reader) {
		ParsecUtility.mustNotBeNull("reader", reader);
		if (reader instanceof TextReader) return (TextReader) reader;
		return new TextReader(reader);
	}
	/**
	 * 文字列からリーダーを生成して返します。
	 * @param text テキスト
	 * @return 新しいリーダー
	 */
	public static TextReader from(String text) {
		ParsecUtility.mustNotBeNull("text", text);
		return from(new StringReader(text));
	}
	/**
	 * ファイルからリーダーを生成して返します。
	 * @param file ファイル
	 * @param charset キャラクターセット
	 * @return 新しいリーダー
	 * @throws FileNotFoundException ファイルが存在しない場合
	 */
	public static TextReader from(File file, Charset charset) throws FileNotFoundException {
		ParsecUtility.mustNotBeNull("file", file);
		ParsecUtility.mustNotBeNull("charset", charset);
		return from(new FileInputStream(file), charset);
	}
	/**
	 * 入力ストリームからリーダーを生成して返します。
	 * @param stream ストリーム
	 * @param charset キャラクターセット
	 * @return 新しいリーダー
	 */
	public static TextReader from(InputStream stream, Charset charset) {
		ParsecUtility.mustNotBeNull("stream", stream);
		ParsecUtility.mustNotBeNull("charset", charset);
		return from(new BufferedReader(new InputStreamReader(stream, charset)));
	}
	
	private TextReader(Reader reader) {
		super(reader);
	}
	
	/**
	 * コンテキストを初期化して返します。
	 * @return 新しいコンテキスト
	 */
	public Context toContext() {
		return new Context(this);
	}
	/**
	 * コンテキストを初期化して返します。
	 * @param config コンテキストの構成を変更するアクション
	 * @return 新しいコンテキスト
	 */
	public Context toContext(Consumer<ContextConfigurer> config) {
		return toContext().configure(config);
	}
}
