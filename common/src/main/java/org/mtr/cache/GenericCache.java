package org.mtr.cache;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Represents an object cache referenced by a key. When not fetched in a while, the object expires.
 *
 * @param <T> the object type
 */
public final class GenericCache<T> {

	private long lastChecked;
	private final Object2ObjectOpenHashMap<String, DataHolder<T>> data = new Object2ObjectOpenHashMap<>();
	private final int approximateTimeout;

	public GenericCache(int approximateTimeout) {
		this.approximateTimeout = approximateTimeout;
	}

	public T get(String key, Supplier<T> createInstance) {
		final long currentTime = System.currentTimeMillis();

		// Every 100 ms, check if the cache has expired data and clear it
		if (currentTime - lastChecked > 100) {
			final ObjectArrayList<String> keysToRemove = new ObjectArrayList<>();
			data.values().forEach(dataHolder -> {
				if (dataHolder.timeout < currentTime) {
					keysToRemove.add(key);
				}
			});
			keysToRemove.forEach(data::remove);
			lastChecked = currentTime;
		}

		// Get cached data
		final DataHolder<T> dataHolder = data.get(key);
		final long newTimeout = currentTime + approximateTimeout + new Random().nextInt(approximateTimeout / 2);
		if (dataHolder == null) {
			final T newData = createInstance.get();
			data.put(key, new DataHolder<>(newTimeout, newData));
			return newData;
		} else {
			dataHolder.timeout = newTimeout;
			return dataHolder.data;
		}
	}

	private static class DataHolder<T> {

		private long timeout;
		private final T data;

		private DataHolder(long timeout, T data) {
			this.timeout = timeout;
			this.data = data;
		}
	}
}
