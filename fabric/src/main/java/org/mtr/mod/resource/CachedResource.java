package org.mtr.mod.resource;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class CachedResource<T> {

	@Nullable
	private T data;
	private long expiry;

	private final Supplier<T> dataSupplier;
	private final long lifespan;

	private static boolean canFetchCache;

	public CachedResource(final Supplier<T> dataSupplier, final long lifespan) {
		this.dataSupplier = dataSupplier;
		this.lifespan = lifespan;
	}

	@Nullable
	public T getData(boolean force) {
		if (force || canFetchCache) {
			final long currentMillis = System.currentTimeMillis();
			if (currentMillis > expiry) {
				data = dataSupplier.get();
				canFetchCache = false;
			}
			expiry = currentMillis + lifespan;
		}
		return data;
	}

	public static void tick() {
		canFetchCache = true;
	}
}
