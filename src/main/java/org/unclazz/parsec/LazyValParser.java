package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.ObjectCache;
import org.unclazz.parsec.data.ValParserFactory;

final class LazyValParser<T> extends ValParser<T>{
	private static final ObjectCache<ValParser<?>> _instanceCache = new ObjectCache<>(100);
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
	@SuppressWarnings("unchecked")
	public static<T> ValParser<T> getInstance(ValParserFactory<T> factory) {
		final String id = factoryIdentity(factory);
		
		final ValParser<?> readyMade = _instanceCache.get(id);
		if (readyMade != null) return (ValParser<T>) readyMade;
		
		final ValParser<T> customMade = new LazyValParser<T>(factory);
		_instanceCache.put(id, customMade);
		return customMade;
	}
	
	private final ValParserFactory<T> _func;
	private ValParser<T> _cache;
	private LazyValParser(ValParserFactory<T> func){
		ParsecUtility.mustNotBeNull("func", func);
		_func = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.create();
		return _cache.doParse(ctx);
	}
}