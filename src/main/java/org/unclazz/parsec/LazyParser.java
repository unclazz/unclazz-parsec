package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.ObjectCache;
import org.unclazz.parsec.data.ParserFactory;

final class LazyParser extends Parser{
	private static final ObjectCache<LazyParser> _instanceCache = new ObjectCache<>(100);
	public static String factoryIdentity(Object o) {
		final Class<?> clazz = o.getClass();
		final String clazzName = clazz.getName();
		if (clazz.isSynthetic()) return clazzName;
		
		final int idHash = System.identityHashCode(o);
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