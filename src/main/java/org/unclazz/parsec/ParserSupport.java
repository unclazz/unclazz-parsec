package org.unclazz.parsec;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * {@link Parser}および{@link ValParser}クラスとその派生クラスのため
 * 各種のユーティリティとファクトリーメソッドを提供します。
 */
abstract class ParserSupport{
	private static final Pattern _classSuffix = Pattern.compile("Parser$");
	private final String _name;
	
	/**
	 * 引数なしのコンストラクタです。
	 * <p>
	 * このコンストラクタで初期化されたインスタンスの{@link #name()}メソッドは
	 * クラス名の末尾の{@code "Parser"}をカットした文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 */
	protected ParserSupport() {
		this(null);
	}
	/**
	 * パーサーの名前を引数にとるコンストラクタです。
	 * <p>
	 * 初期化されたインスタンスの{@link #name()}メソッドは
	 * このコンストラクタの引数で指定した文字列を返します。
	 * この文字列はデバッグのためのログ出力のときなどに利用されます。
	 * </p>
	 */
	protected ParserSupport(final String name) {
		if (name == null) {
			_name = _classSuffix.matcher(this.getClass().getName()).replaceAll("");
		}else {
			_name = name;
		}
	}
	
	/**
	 * パーサーの名前です。
	 * @return
	 */
	protected String name() {
		return _name;
	}
	
	/**
	 * 引数で指定した文字にマッチするパーサーを返します。
	 * @param ch 文字
	 * @return
	 */
	protected Parser exact(char ch){
		return new ExactCharParser(ch);
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return
	 */
	protected Parser charIn(String chs){
		return new CharClassParser(CharClass.anyOf(chs.toCharArray()));
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return
	 */
	protected Parser charIn(char...chs){
		return new CharClassParser(CharClass.anyOf(chs));
	}
	/**
	 * 引数で指定した文字クラスにマッチするパーサーを返します。
	 * @param chs 文字クラス
	 * @return
	 */
	protected Parser charIn(CharClass clazz){
		return new CharClassParser(clazz);
	}
	/**
	 * 引数で指定した文字の範囲にマッチするパーサーを返します。
	 * @param start 範囲の開始
	 * @param end 範囲の終了
	 * @return
	 */
	protected Parser charBetween(char start, char end){
		return new CharClassParser(CharClass.between(start, end));
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keyword キーワード
	 * @return
	 */
	protected Parser keyword(String keyword) {
		return new KeywordParser(keyword);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param value 値
	 * @return
	 */
	protected<T> ValParser<T> product(T value) {
		return product(() -> value);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param func ファクトリー
	 * @return
	 */
	protected<T> ValParser<T> product(Supplier<T> func) {
		return new ProductParser<>(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * @param func ファクトリー
	 * @return
	 */
	protected Parser lazy(Supplier<Parser> func) {
		return new LazyParser(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * @param func ファクトリー
	 * @return
	 */
	protected<T> ValParser<T> lazyVal(Supplier<ValParser<T>> func) {
		return new LazyValParser<>(func);
	}
}