package org.unclazz.parsec.data;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ObjectCache<T> extends LinkedHashMap<String, T> {
	private static final long serialVersionUID = -1192308201920272827L;
	private final int _maxSize;
    
    public ObjectCache(int maxSize) {
    	if (maxSize < 1) throw new IllegalArgumentException("\"maxSize\" must be greater than 0.");
    	_maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
        return size() > _maxSize;
    }
}