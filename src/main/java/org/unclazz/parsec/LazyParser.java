package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.ObjectCache;
import org.unclazz.parsec.data.ParserFactory;

final class LazyParser extends Parser{
	private static final ObjectCache<LazyParser> _instanceCache = new ObjectCache<>(100);
	public static String factoryIdentity(ParserFactory factory) {
		final Class<?> clazz = factory.getClass();
		final String clazzName = clazz.getName();
		
		// ファクトリーが合成型である場合はクラス名をそのままIDとする
		// ※この実装は「ファクトリーが合成型である」＝「ファクトリーがラムダ式もしくはメソッド参照で指定されたものである」という前提に基づく。
		// 同一コンテキスト（同一クラス、同一インスタンス、同一メソッド そして 同一呼び出し箇所）において、
		// ラムダ式もしくはメソッド参照で指定されたファクトリーの factory.getClass().getName() は、一意の名前を返す。
		// この性質を前提として利用して合成型の場合は型名をそのままキャッシュのキーとして利用する。
		if (clazz.isSynthetic()) return clazzName;
		
		// それ以外の場合は型名＋IDハッシュをキーとして利用する。
		final int idHash = System.identityHashCode(factory);
		return String.format("%s@%s", clazzName, idHash);
	}
	/**
	 * ファクトリーを引数にとって{@link LazyParser}のインスタンスを返します。
	 * <p>同一のファクトリーで生成した{@link LazyParser}インスタンスがキャッシュに存在する場合、それを返します。</p>
	 * @param factory 
	 * @return
	 */
	public static LazyParser getInstance(ParserFactory factory) {
		final String id = factoryIdentity(factory);
		
		final LazyParser readyMade = _instanceCache.get(id);
		if (readyMade != null) return readyMade;
		
		final LazyParser customMade = new LazyParser(factory);
		_instanceCache.put(id, customMade);
		return customMade;
	}
	
	private final ParserFactory _func;
	private Parser _cache;
	private LazyParser(ParserFactory func){
		ParsecUtility.mustNotBeNull("func", func);
		_func = func;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.create();
		return _cache.doParse(ctx);
	}
}