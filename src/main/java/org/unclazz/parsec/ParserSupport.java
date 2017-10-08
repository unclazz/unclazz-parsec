package org.unclazz.parsec;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.unclazz.parsec.util.ParserFactory;
import org.unclazz.parsec.util.ValParserFactory;

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
	 * @param name パーサーの名前
	 */
	protected ParserSupport(final String name) {
		if (name == null) {
			_name = _classSuffix.matcher(this.getClass().getSimpleName()).replaceAll("");
		}else {
			_name = name;
		}
	}
	
	/**
	 * パーサーの名前です。
	 * @return パーサーの名前
	 */
	protected final String name() {
		return _name;
	}
	
	/* ---------- 以下、ファクトリーメソッド ---------- */
	/**
	 * EOFにマッチするパーサーを返します。
	 * @return パーサー
	 */
	protected final Parser eof() {
		return new EofParser();
	}
	/**
	 * BOFにマッチするパーサーを返します。
	 * @return パーサー
	 */
	protected final Parser bof() {
		return new BofParser();
	}
	/**
	 * 引数で指定した文字にマッチするパーサーを返します。
	 * @param ch 文字
	 * @return パーサー
	 */
	protected final Parser exact(char ch){
		return new ExactCharParser(ch);
	}
	/**
	 * 引数で指定した文字以外にマッチするパーサーを返します。
	 * @param ch 文字
	 * @return パーサー
	 */
	protected final Parser except(char ch){
		return charIn(CharClass.not(CharClass.exact(ch)));
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charIn(String chs){
		return charIn(chs.toCharArray());
	}
	/**
	 * 引数で指定した文字集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charIn(char...chs){
		return new CharClassParser(CharClass.anyOf(chs));
	}
	/**
	 * 引数で指定した文字クラスにマッチするパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	protected final Parser charIn(CharClass clazz){
		return new CharClassParser(clazz);
	}
	/**
	 * 引数で指定した文字集合の補集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charNotIn(String chs){
		return charNotIn(chs.toCharArray());
	}
	/**
	 * 引数で指定した文字集合の補集合にマッチするパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charNotIn(char...chs){
		return new CharClassParser(CharClass.not(CharClass.anyOf(chs)));
	}
	/**
	 * 引数で指定した文字クラスの補集合にマッチするパーサーを返します。
	 * @param clazz 文字集合
	 * @return パーサー
	 */
	protected final Parser charNotIn(CharClass clazz){
		return new CharClassParser(CharClass.not(clazz));
	}
	/**
	 * 引数で指定した文字の範囲にマッチするパーサーを返します。
	 * @param start 範囲の開始
	 * @param end 範囲の終了
	 * @return パーサー
	 */
	protected final Parser charBetween(char start, char end){
		return new CharClassParser(CharClass.between(start, end));
	}
	/**
	 * 引数で指定した文字の範囲の外側にマッチするパーサーを返します。
	 * @param start 範囲の開始
	 * @param end 範囲の終了
	 * @return パーサー
	 */
	protected final Parser charNotBetween(char start, char end){
		return new CharClassParser(CharClass.not(CharClass.between(start, end)));
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charsWhileIn(String chs) {
		return new CharsWhileInParser(CharClass.anyOf(chs.toCharArray()), 0);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	protected final Parser charsWhileIn(String chs, int min) {
		return new CharsWhileInParser(CharClass.anyOf(chs.toCharArray()), min);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	protected final Parser charsWhileIn(CharClass clazz) {
		return new CharsWhileInParser(clazz, 0);
	}
	/**
	 * 文字集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	protected final Parser charsWhileIn(CharClass clazz, int min) {
		return new CharsWhileInParser(clazz, min);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @return パーサー
	 */
	protected final Parser charsWhileNotIn(String chs) {
		return new CharsWhileInParser(CharClass.not(CharClass.anyOf(chs.toCharArray())), 0);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param chs 文字集合
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	protected final Parser charsWhileNotIn(String chs, int min) {
		return new CharsWhileInParser(CharClass.not(CharClass.anyOf(chs.toCharArray())), min);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @return パーサー
	 */
	protected final Parser charsWhileNotIn(CharClass clazz) {
		return new CharsWhileInParser(CharClass.not(clazz), 0);
	}
	/**
	 * 文字集合の補集合に属する文字が続く間パースを続けるパーサーを返します。
	 * @param clazz 文字クラス
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	protected final Parser charsWhileNotIn(CharClass clazz, int min) {
		return new CharsWhileInParser(CharClass.not(clazz), min);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keyword キーワード
	 * @return パーサー
	 */
	protected final Parser keyword(String keyword) {
		return new KeywordParser(keyword);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keyword キーワード
	 * @param cutIndex この添字より前までパース成功したら以降バックトラックは無効
	 * @return パーサー
	 */
	protected final Parser keyword(String keyword, int cutIndex) {
		return new KeywordParser(keyword, cutIndex);
	}
	/**
	 * 引数で指定したキーワードにマッチするパーサーを返します。
	 * @param keywords 複数のキーワード
	 * @return パーサー
	 */
	protected final Parser keywordIn(String...keywords) {
		return new KeywordInParser(keywords);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param value 任意の値
	 * @return パーサー
	 * @param <T> 任意の値の型
	 */
	protected final <T> ValParser<T> produce(T value) {
		return produce(() -> value);
	}
	/**
	 * 文字位置を変化させず指定した値を産生するパーサーを返します。
	 * @param func 任意の値を供給する関数
	 * @return パーサー
	 * @param <T> 関数により供給されるパーサーの読み取り結果型
	 */
	protected final <T> ValParser<T> produce(Supplier<T> func) {
		return new ProduceParser<>(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * <p>同一のファクトリーにより生成されたインスタンスが存在する場合はそのインスタンスが返されます。</p>
	 * @param func パーサーのファクトリー関数
	 * @return パーサー
	 */
	protected final Parser lazy(ParserFactory func) {
		return LazyParser.getInstance(func);
	}
	/**
	 * パーサーが実際に必要になったときにこれを生成してパースを行う遅延初期化パーサーを返します。
	 * <p>同一のファクトリーにより生成されたインスタンスが存在する場合はそのインスタンスが返されます。</p>
	 * @param func パーサーのファクトリー関数
	 * @return パーサー
	 * @param <T> ファクトリー関数により生成されるパーサーの読み取り結果型
	 */
	protected final <T> ValParser<T> lazy(ValParserFactory<T> func) {
		return LazyValParser.getInstance(func);
	}
	/**
	 * 肯定先読みを行うパーサーを返します。
	 * @param original 先読みに使用するパーサー
	 * @return パーサー
	 */
	protected final Parser lookahead(Parser original) {
		return new LookaheadParser(original);
	}
	/**
	 * 肯定先読みを行うパーサーを返します。
	 * @param original 先読みに使用するパーサー
	 * @return パーサー
	 * @param <T> 元のパーサーの読み取り結果型
	 */
	protected final <T> Parser lookahead(ValParser<T> original) {
		return new LookaheadParser(original.unval());
	}
	/**
	 * パーサーの成否を反転させるパーサーを返します。
	 * @param original 元のパーサー
	 * @return パーサー
	 */
	protected final Parser not(Parser original) {
		return new NotParser(original);
	}
	/**
	 * パーサーの成否を反転させるパーサーを返します。
	 * @param original 元のパーサー
	 * @return パーサー
	 * @param <T> 元のパーサーの読み取り結果型
	 */
	protected final <T> Parser not(ValParser<T> original) {
		return new NotParser(original.unval());
	}
	/**
	 * 0個以上の空白にマッチするパーサーを返します。
	 * @return パーサー
	 */
	protected final Parser space() {
		return new SpaceParser(0);
	}
	/**
	 * {@code min}個以上の空白にマッチするパーサーを返します。
	 * @param min 繰返しの最小回数
	 * @return パーサー
	 */
	protected final Parser space(int min) {
		return new SpaceParser(min);
	}
	/* ---------- 以上、ファクトリーメソッド ---------- */
}