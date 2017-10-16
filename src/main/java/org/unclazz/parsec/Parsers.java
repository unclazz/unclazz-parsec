package org.unclazz.parsec;

import java.util.function.Supplier;

import org.unclazz.parsec.util.ParserFactory;
import org.unclazz.parsec.util.ValParserFactory;

/**
 * {@link Parser}および{@link ValParser}のためのユーティリティです。
 */
public final class Parsers {
	private Parsers() {}
	
	/**
	 * EOFにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public static  Parser eof() {
		return new EofParser();
	}
	/**
	 * BOFにマッチするパーサーを返します。
	 * @return パーサー
	 */
	public static  Parser bof() {
		return new BofParser();
	}
	/**
	 * 引数で指定した文字にマッチするパーサーを返します。
	 * @param ch 文字
	 * @return パーサー
	 */
	public static  Parser exact(char ch){
		return new ExactCharParser(ch);
	}
	/**
	 * 引数で指定した文字以外にマッチするパーサーを返します。
	 * @param ch 文字
	 * @return パーサー
	 */
	public static Parser except(char ch){
		return charIn(CharClass.not(CharClass.exact(ch)));
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static  Parser charIn(String chs){
		return new CharClassParser(CharClass.anyOf(chs.toCharArray()));
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static  Parser charIn(char...chs){
		return new CharClassParser(CharClass.anyOf(chs));
	}
	/**
	 * 引数で指定した文字クラスにマッチするパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	public static  Parser charIn(CharClass clazz){
		return new CharClassParser(clazz);
	}
	/**
	 * 引数で指定した文字集合の補集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static Parser charNotIn(String chs){
		return charNotIn(chs.toCharArray());
	}
	/**
	 * 引数で指定した文字集合の補集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static Parser charNotIn(char...chs){
		return new CharClassParser(CharClass.not(CharClass.anyOf(chs)));
	}
	/**
	 * 引数で指定した文字クラスの補集合にマッチするパーサーを返します。
	 * @param clazz 文字集合
	 * @return パーサー
	 */
	public static Parser charNotIn(CharClass clazz){
		return new CharClassParser(CharClass.not(clazz));
	}
	/**
	 * 引数で指定した文字の範囲にマッチするパーサーを返します。
	 * @param start 範囲の開始
	 * @param end 範囲の終了
	 * @return パーサー
	 */
	public static  Parser charBetween(char start, char end){
		return new CharClassParser(CharClass.between(start, end));
	}
	/**
	 * 引数で指定した文字の範囲の外側にマッチするパーサーを返します。
	 * @param start 範囲の開始
	 * @param end 範囲の終了
	 * @return パーサー
	 */
	public static Parser charNotBetween(char start, char end){
		return new CharClassParser(CharClass.not(CharClass.between(start, end)));
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static  Parser charsWhileIn(String chs) {
		return new CharsWhileInParser(CharClass.anyOf(chs.toCharArray()), 0);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @param min 文字数の最小値
	 * @return パーサー
	 */
	public static  Parser charsWhileIn(String chs, int min) {
		return new CharsWhileInParser(CharClass.anyOf(chs.toCharArray()), min);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	public static  Parser charsWhileIn(CharClass clazz) {
		return new CharsWhileInParser(clazz, 0);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @param min 文字数の最小値
	 * @return パーサー
	 */
	public static  Parser charsWhileIn(CharClass clazz, int min) {
		return new CharsWhileInParser(clazz, min);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	public static Parser charsWhileNotIn(String chs) {
		return new CharsWhileInParser(CharClass.not(CharClass.anyOf(chs.toCharArray())), 0);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @param min 文字数の最小値
	 * @return パーサー
	 */
	public static Parser charsWhileNotIn(String chs, int min) {
		return new CharsWhileInParser(CharClass.not(CharClass.anyOf(chs.toCharArray())), min);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	public static Parser charsWhileNotIn(CharClass clazz) {
		return new CharsWhileInParser(CharClass.not(clazz), 0);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @param min 文字数の最小値
	 * @return パーサー
	 */
	public static Parser charsWhileNotIn(CharClass clazz, int min) {
		return new CharsWhileInParser(CharClass.not(clazz), min);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keyword キーワード
	 * @return パーサー
	 */
	public static  Parser keyword(String keyword) {
		return new KeywordParser(keyword);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keyword キーワード
	 * @param cutIndex この添字より前までパース成功したら以降バックトラックは無効
	 * @return パーサー
	 */
	public static  Parser keyword(String keyword, int cutIndex) {
		return new KeywordParser(keyword, cutIndex);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keywords 複数のキーワード
	 * @return パーサー
	 */
	public static  Parser keywordIn(String...keywords) {
		return new KeywordInParser(keywords);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param value 任意の値
	 * @return パーサー
	 * @param <T> 任意の値の型
	 */
	public static <T> ValParser<T> produce(T value) {
		return produce(() -> value);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param func 任意の値を供給する関数
	 * @return パーサー
	 * @param <T> 関数により供給されるパーサーの読み取り結果型
	 */
	public static <T> ValParser<T> produce(Supplier<T> func) {
		return new ProduceParser<>(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * <p>同一のファクトリーにより生成されたインスタンスが存在する場合はそのインスタンスが返されます。</p>
	 * @param func パーサーのファクトリー関数
	 * @return パーサー
	 */
	public static  Parser lazy(ParserFactory func) {
		return LazyParser.getInstance(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * <p>同一のファクトリーにより生成されたインスタンスが存在する場合はそのインスタンスが返されます。</p>
	 * @param func パーサーのファクトリー関数
	 * @return パーサー
	 * @param <T> ファクトリー関数により生成されるパーサーの読み取り結果型
	 */
	public static <T> ValParser<T> lazy(ValParserFactory<T> func) {
		return LazyValParser.getInstance(func);
	}
	/**
	 * 肯定先読みを行うパーサーを返します。
	 * @param original 先読みに使用するパーサー
	 * @return パーサー
	 */
	public static  Parser lookahead(Parser original) {
		return new LookaheadParser(original);
	}
	/**
	 * 肯定先読みを行うパーサーを返します。
	 * @param original 先読みに使用するパーサー
	 * @return パーサー
	 * @param <T> 元のパーサーの読み取り結果型
	 */
	public static<T> Parser lookahead(ValParser<T> original) {
		return new LookaheadParser(original.unval());
	}
	/**
	 * パーサーの成否を反転させるパーサーを返します。
	 * @param original 元のパーサー
	 * @return パーサー
	 */
	public static  Parser not(Parser original) {
		return new NotParser(original);
	}
	/**
	 * パーサーの成否を反転させるパーサーを返します。
	 * @param original 元のパーサー
	 * @return パーサー
	 * @param <T> 元のパーサーの読み取り結果型
	 */
	public static<T>  Parser not(ValParser<T> original) {
		return new NotParser(original.unval());
	}
	/**
	 * 0個以上の空白にマッチするパーサーを返します。
	 * @return パーサー
	 */
	public static Parser space() {
		return new SpaceParser(0);
	}
	/**
	 * {@code min}個以上の空白にマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	public static Parser space(int min) {
		return new SpaceParser(min);
	}
	/**
	 * 引数で指定したトークンのパーサーが成功するまで入力データソースを読み進めます。
	 * @param token 読み進めるのをやめる条件となるトークンを読み取るパーサー
	 * @return パーサー
	 */
	public static Parser skipTo(Parser token) {
		return new SkipToParser(token);
	}
	/**
	 * 引数で指定したトークンのパーサーが成功するまで入力データソースを読み進めます。
	 * @param token 読み進めるのをやめる条件となるトークンを読み取るパーサー
	 * @param <T> 任意の型
	 * @return パーサー
	 */
	public static <T> ValParser<T> skipTo(ValParser<T> token) {
		return new SkipToValParser<>(token);
	}
}
