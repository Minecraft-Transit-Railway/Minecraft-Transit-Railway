package org.mtr.cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public final class GenericLongCache<T> extends GenericCacheBase<T, Long, Long2ObjectOpenHashMap<GenericCacheBase.DataHolder<T>>> {

	public GenericLongCache(int approximateTimeout, boolean canExpireWhileFetching) {
		super(new Long2ObjectOpenHashMap<>(), approximateTimeout, canExpireWhileFetching);
	}

	@Override
	protected DataHolder<T> get(Long2ObjectOpenHashMap<DataHolder<T>> map, Long key) {
		return map.get((long) key);
	}

	@Override
	protected void put(Long2ObjectOpenHashMap<DataHolder<T>> map, Long key, DataHolder<T> newData) {
		map.put((long) key, newData);
	}

	@Override
	protected ObjectCollection<DataHolder<T>> values(Long2ObjectOpenHashMap<DataHolder<T>> map) {
		return map.values();
	}

	@Override
	protected void remove(Long2ObjectOpenHashMap<DataHolder<T>> map, Long key) {
		map.remove((long) key);
	}
}
