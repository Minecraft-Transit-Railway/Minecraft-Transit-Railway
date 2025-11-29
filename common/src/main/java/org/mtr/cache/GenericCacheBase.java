package org.mtr.cache;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Represents an object cache referenced by a key. When not fetched in a while or after a timeout, the object expires.
 *
 * @param <T> the object type
 */
public abstract class GenericCacheBase<T, U, V> {

	private long lastChecked;
	private final V data;
	private final int approximateTimeout;
	private final boolean canExpireWhileFetching;

	public GenericCacheBase(V mapInstance, int approximateTimeout, boolean canExpireWhileFetching) {
		data = mapInstance;
		this.approximateTimeout = approximateTimeout;
		this.canExpireWhileFetching = canExpireWhileFetching;
	}

	public final T get(U key, Supplier<T> createInstance) {
		final long currentTime = System.currentTimeMillis();

		// Every 100 ms, check if the cache has expired data and clear it
		if (currentTime - lastChecked > 100) {
			final ObjectArrayList<U> keysToRemove = new ObjectArrayList<>();
			values(data).forEach(dataHolder -> {
				if (dataHolder.timeout < currentTime) {
					keysToRemove.add(key);
				}
			});
			keysToRemove.forEach(keyToRemove -> remove(data, keyToRemove));
			lastChecked = currentTime;
		}

		// Get cached data
		final DataHolder<T> dataHolder = get(data, key);
		final long newTimeout = currentTime + approximateTimeout + new Random().nextInt(approximateTimeout / 2);
		if (dataHolder == null) {
			final T newData = createInstance.get();
			put(data, key, new DataHolder<>(newTimeout, newData));
			return newData;
		} else {
			if (!canExpireWhileFetching) {
				dataHolder.timeout = newTimeout;
			}
			return dataHolder.data;
		}
	}

	protected abstract DataHolder<T> get(V map, U key);

	protected abstract void put(V map, U key, DataHolder<T> newData);

	protected abstract ObjectCollection<DataHolder<T>> values(V map);

	protected abstract void remove(V map, U key);

	protected static class DataHolder<T> {

		private long timeout;
		private final T data;

		private DataHolder(long timeout, T data) {
			this.timeout = timeout;
			this.data = data;
		}
	}
}
