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

public final class TextReader extends ResettableReader {
	public static TextReader from(String text) {
		return new TextReader(new StringReader(text));
	}
	public static TextReader from(File file, Charset charset) throws FileNotFoundException {
		return from(new FileInputStream(file), charset);
	}
	public static TextReader from(InputStream stream, Charset charset) {
		return new TextReader(new BufferedReader(new InputStreamReader(stream, charset)));
	}
	
	private TextReader(Reader reader) {
		super(reader);
	}
	
	public Context toContext() {
		return new Context(this);
	}
	public Context toContext(Consumer<ContextConfigurer> config) {
		return toContext().configure(config);
	}
}
