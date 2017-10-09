package org.unclazz.parsec.wiki;

import org.junit.Test;
import org.unclazz.parsec.Context;
import org.unclazz.parsec.Parser;
import org.unclazz.parsec.Result;
import org.unclazz.parsec.ResultCore;
import org.unclazz.parsec.TextReader;
import org.unclazz.parsec.ValParser;
import org.unclazz.parsec.ValResult;
import org.unclazz.parsec.ValResultCore;
import org.unclazz.parsec.util.Tuple2;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Optional;

public class Home {
	@Test
	public void list1_intro() {
		
		// "hello"という文字列にマッチするパーサー
		class HelloParser extends ValParser<String> {
			private final ValParser<String> _hello;
			HelloParser() {
				// - keyword(...)はValParser<T>が提供するprotectedなファクトリーメソッド。
				//   その名の通りあるキーワードにマッチするParserの派生型を生成する。
				// - val()はParser派生型をValParser<T>派生型に変換する。
				// - then(...)はレシーバのパーサーと引数のパーサーを連結してシーケンスを構成する。
				// - eof()もValParser<T>が提供するprotectedなファクトリーメソッド。入力データソースの末尾にのみマッチする。
				//   こうしてあえてEOFまで読み取ることを指定しない場合、パーサーは入力データソースにおいて
				//   条件に適合する部分まで読み進めて、それ以降の「まだパースしていない残りの部分」について関知せず結果を返す。
				//   例えばこの例では"hello"という文字列だけでなく"hello "や"hellox"といった文字列でもパース成功となる。
				_hello = keyword("hello").val().then(eof());
			}
			@Override
			protected ValResultCore<String> doParse(Context ctx) throws IOException {
				// - パーサーを実装するにはValParser<T>やParserで宣言された
				//   抽象メソッドdoParse(...)を実装する必要がある。
				// - この例では親クラスが宣言するメソッドにより組み立てたパーサーを内部的に利用しているが、
				//   完全にカスタムメイドのパース処理の実装を行うことも可能。
				// - Contextは入力データソースのTextReaderへのアクセスやデバッグログの出力機能などを提供するオブジェクト。
				//   この例ではそのまま内部パーサーの引数として引き渡している。
				return _hello.parse(ctx);
			}
		}
		
		// パーサーをインスタンス化
		final HelloParser hello = new HelloParser();
		
		// 正常系
		// - HelloParserはValParser<T>の派生型なのでパース結果としてValResult<T>を返す。
		// - "hello"という文字列のパースは成功し、キャプチャ結果を文字列として取得できる。
		final ValResult<String> result1 = hello.parse("hello");
		assertThat(result1.isSuccessful(), is(true));
		assertThat(result1.value(), is("hello"));

		// 異常系1
		// - "hallo"という文字列のパースは失敗し、期待した文字が現れなかった旨のメッセージを取得できる。
		final ValResult<String> result2 = hello.parse("hallo");
		assertThat(result2.isSuccessful(), is(false));
		assertThat(result2.message(), is("'e'(101) expected but 'a'(97) found."));
		
		// 異常系2
		// - "hello "という文字列のパースは失敗し、EOFに到達しなかった旨のメッセージが取得できる。
		final ValResult<String> result3 = hello.parse("hello ");
		assertThat(result3.isSuccessful(), is(false));
		assertThat(result3.message(), is("EOF expected but ' '(32) found."));
	}
	
	@Test
	public void list2_thenMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final Parser hello = keyword("hello");
				final Parser world = keyword("world");
				
				// パターンA： Parser & Parser -> Parser
				// 値を返さないパーサー同士の連結結果は。同じく値を返さないパーサーとなる。
				final Parser helloWorld = hello.then(world);
				assertTrue(helloWorld.parse("helloworld ").isSuccessful());
				assertFalse(helloWorld.parse("hello world").isSuccessful());

				// パターンB： ValParser<String> & Parser -> ValParser<String>
				// 値を返すパーサー（左辺）と値を返さないパーサー（右辺）の連結結果は、
				// 値（左辺のパーサーが読み取ったもの）を返すパーサーとなる。
				final ValParser<String> helloWorld2 = hello.val().then(world);
				assertThat(helloWorld2.parse("hello world").value(), is(nullValue()));
				assertThat(helloWorld2.parse("helloworld ").value(), is("hello"));

				// パターンC： Parser & ValParser<String> -> ValParser<String>
				// 値を返さないパーサー（左辺）と値を返すパーサー（右辺）の連結結果は、
				// 値（右辺のパーサーが読み取ったもの）を返すパーサーとなる。
				final ValParser<String> helloWorld3 = hello.then(world.val());
				assertThat(helloWorld3.parse("hello world").value(), is(nullValue()));
				assertThat(helloWorld3.parse("helloworld ").value(), is("world"));

				// パターンD： ValParser<String> & ValParser<String> -> ValParser<Tuple2<String,String>>
				// 値を返すパーサー（左辺）と値を返すパーサー（右辺）の連結結果は、
				// 値（双方のパーサーが読み取ったものを要素とするタプル）を返すパーサーとなる。
				final ValParser<Tuple2<String, String>> helloWorld4 = hello.val().then(world.val());
				assertThat(helloWorld4.parse("hello world").value(), is(nullValue()));
				assertThat(helloWorld4.parse("helloworld ").value().item1(), is("hello"));

				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list3_repMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final Parser a = exact('a');
				final Parser comma = exact(',');
				
				// パターンA： 最小最大を指定した繰返し
				final Parser min2Max3 = a.rep(2, 3);
				assertFalse(min2Max3.parse("a___").isSuccessful());
				assertTrue(min2Max3.parse("aa__").isSuccessful());
				assertTrue(min2Max3.parse("aaaa").isSuccessful());// 3文字だけ読み取る
				
				// パターンB： 厳密な回数を指定した繰返し
				final Parser exactly2 = a.rep(2);
				assertFalse(exactly2.parse("a___").isSuccessful());
				assertTrue(exactly2.parse("aa__").isSuccessful());
				assertTrue(exactly2.parse("aaaa").isSuccessful());// 2文字だけ読み取る
				
				// パターンC： 0回以上の繰返し
				final Parser gteZero = a.rep();
				assertTrue(gteZero.parse("____").isSuccessful());
				assertTrue(gteZero.parse("aaaa").isSuccessful());// 最後まで読み取る
				
				// パターンD： n回以上の繰返し
				final Parser gteOne = a.repMin(1);
				assertFalse(gteOne.parse("____").isSuccessful());
				assertTrue(gteOne.parse("aaaa").isSuccessful());// 最後まで読み取る
				
				// パターンE： セパレータを伴う繰返し
				// 最小最大指定、厳密な回数指定、そして0回以上とn回以上の繰返し
				// いずれのrep(...)メソッドもセパレータを指定するオーバーロードを持つ。
				final Parser sepComma = a.rep(comma);
				assertTrue(sepComma.parse("a,a,a,b").isSuccessful());
				
				// パターンF： リダクションを伴う繰返し
				// ValParser.rep(...)が返すパーサーはリダクションをサポートしている。
				// reduce(...)メソッドには1～3つの関数型インターフェースの引数をとるオーバーロードが存在する。
				final ValParser<String> reduction = a.val().rep()
						.reduce(StringBuilder::new, // Supplier<T1>
								StringBuilder::append, // BiFunction<T1, T2, T1>
								StringBuilder::toString); // Function<T1, T3>
				assertThat(reduction.parse("aaa_").value(), is("aaa"));
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list4_optMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				Parser a = exact('a');
				
				// ValParser<T>.opt()が返すパーサーはValParser<Optional<T>>となる。
				// （対照的にParser.opt()が返すパーサーはParserとなる。）
				ValParser<Optional<String>> bOpt = exact('b').val().opt();
				ValParser<Optional<String>> a_bOpt = a.then(bOpt);
				
				// 存在すればその値をラップしたOptionalが返される。
				ValResult<Optional<String>> result1 = a_bOpt.parse("abc");
				assertTrue(result1.isSuccessful());
				assertThat(result1.value().orElse("x"), is("b"));
				
				// 存在しなければ空のOptionalが返される。
				ValResult<Optional<String>> result2 = a_bOpt.parse("acd");
				assertTrue(result2.isSuccessful());
				assertThat(result2.value().orElse("x"), is("x"));
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list5_orMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final Parser a = exact('a');
				final Parser b = exact('b');
				final Parser cd = charIn("cd");
				final Parser abcd = a.or(b.or(cd));
				
				assertTrue(abcd.parse("a___").isSuccessful());
				assertTrue(abcd.parse("d___").isSuccessful());
				assertFalse(abcd.parse("e___").isSuccessful());
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list6_bofAndEofFactory() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final Parser a = exact('a');
				final Parser b = exact('b');
				final Parser x = a.or(bof()).then(b).rep().then(eof());
				
				// 'a'もしくはBOFで始まり、その後baba...と繰り返してEOFに至る文字列にのみマッチする
				assertTrue(x.parse("abab").isSuccessful());
				assertTrue(x.parse("babab").isSuccessful());
				assertFalse(x.parse("abb").isSuccessful());
				
				// パース開始時の文字位置が'a'でもBOFでもない場合は結果NG
				final TextReader tr = TextReader.from("ababab");
				tr.read(); // 文字位置が1前進し、残りの文字列は"babab"
				assertFalse(x.parse(tr).isSuccessful());
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list7_lookaheadFactory() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final Parser p1 = keyword("hello").then(keyword("world"));
				final Parser p2 = keyword("hello").then(lookahead(keyword("world")));
				
				final Result p1Result1 = p1.parse("helloworld_");
				final Result p2Result1 = p2.parse("helloworld_");
				final Result p2Result2 = p2.parse("hello world_");
				
				// パース成功となる文字列は単なるthen(...)の場合と同じ
				assertTrue(p1Result1.isSuccessful());
				assertTrue(p2Result1.isSuccessful());
				assertFalse(p2Result2.isSuccessful());
				
				// しかしlookahead(...)の場合パースした後の文字位置が異なってくる
				assertThat(p1Result1.end().index(), is(10)); // 文字位置は'_'の位置
				assertThat(p2Result1.end().index(), is(5)); // 文字位置は'w'の位置
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list8_mapMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				final ValParser<String> binary = charIn("01").repMin(1).val();
				final ValParser<Integer> binaryNum = binary.map(a -> Integer.parseInt(a, 2));
				
				assertThat(binary.parse("1100").value(), is("1100"));
				assertThat(binaryNum.parse("1100").value(), is(12));
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list9_flatMapMethod() {
		class AssertParser extends Parser {
			final Parser lt = exact('<');
			final Parser gt = exact('>');
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				ValParser<String> leftTag = lt.then(except('>').repMin(1).val()).then(gt);
				ValParser<String> tag = leftTag.flatMap(this::rightTag);
				
				assertThat(tag.parse("<a></a>").value(), is("a"));
				assertThat(tag.parse("<a></b>").value(), is(nullValue()));
				
				return success();
			}
			ValParser<String> rightTag(String a) {
				return lt.then(exact('/').then(keyword(a).val())).then(gt);
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list10_cutMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				// ケース1： もっともプリミティブなOR演算
				// → バックトラックが効いてしまい、事実ではあるが不親切なエラーメッセージになってしまう。
				Parser _true1 = keyword("true");
				Parser _false = keyword("false");
				Parser _trueOrFalse1 = _true1.or(_false);
				assertThat(_trueOrFalse1.parse("troo").message(), 
						is("'f'(102) expected but 't'(116) found."));//事実だが不親切なメッセージ
				
				// ケース2： 明示的にcut()を使用してOR演算
				// → 所定の位置までパース成功すると以後バックトラックが無効化されるので、より親切なエラーメッセージになる。
				Parser _true2 = exact('t').cut().then(keyword("rue"));
				Parser _trueOrFalse2 = _true2.or(_false);
				assertThat(_trueOrFalse2.parse("troo").message(), 
						is("'u'(117) expected but 'o'(111) found."));//より親切なメッセージ
				
				// ケース3： keyword(...)の第2引数でcutIndexを指定してOR演算
				// → keyword(...)パーサーの場合だけ使用できるショートカット。ケース2と同じ効果。
				Parser _true3 = keyword("true", 1); // cutIndexを引数で指定
				Parser _trueOrFalse3 = _true3.or(_false);
				assertThat(_trueOrFalse3.parse("troo").message(), 
						is("'u'(117) expected but 'o'(111) found."));

				// ケース4： keywordIn(...)は自動的にcutIndexを算出する
				// → この例の場合1文字目（'t'もしくは'f'）がパース成功となった時点で他の候補に有効なものがなくなる。
				//    このため1文字目のパース成功とともに、自動的にバックトラックが無効化される。
				Parser _trueOrFalse4 = keywordIn("true", "false");
				assertThat(_trueOrFalse4.parse("troo").message(), 
						is("'u'(117) expected but 'o'(111) found."));
				assertThat(_trueOrFalse4.parse("folse").message(), 
						is("'a'(97) expected but 'o'(111) found."));
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
	
	@Test
	public void list11_meansMethod() {
		class AssertParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				ValResult<String> r = keyword("true").means("false").parse("true__");
				
				assertThat(r.isSuccessful(), is(true));
				assertThat(r.value(), is("false"));//キャプチャ値はあくまでも引数で指定された値
				assertThat(r.end().index(), is(4));//文字位置はあくまでも元のパース結果に基づく
				
				return success();
			}
		}
		assertTrue(new AssertParser().parse("check!").isSuccessful());
	}
}