package org.mtr.cache;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public final class GenericStringCache<T> extends GenericCacheBase<T, String, Object2ObjectOpenHashMap<String, GenericCacheBase.DataHolder<T>>> {

	public GenericStringCache(int approximateTimeout, boolean canExpireWhileFetching) {
		super(new Object2ObjectOpenHashMap<>(), approximateTimeout, canExpireWhileFetching);
	}

	@Override
	protected GenericCacheBase.DataHolder<T> get(Object2ObjectOpenHashMap<String, DataHolder<T>> map, String key) {
		return map.get(key);
	}

	@Override
	protected void put(Object2ObjectOpenHashMap<String, DataHolder<T>> map, String key, GenericCacheBase.DataHolder<T> newData) {
		map.put(key, newData);
	}

	@Override
	protected ObjectCollection<GenericCacheBase.DataHolder<T>> values(Object2ObjectOpenHashMap<String, DataHolder<T>> map) {
		return map.values();
	}

	@Override
	protected void remove(Object2ObjectOpenHashMap<String, DataHolder<T>> map, String key) {
		map.remove(key);
	}
}
