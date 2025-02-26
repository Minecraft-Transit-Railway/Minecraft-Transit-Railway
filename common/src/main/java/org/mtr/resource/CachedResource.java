package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class CachedResource<T> {

	@Nullable
	private T data;
	private long expiry;

	private final Supplier<T> dataSupplier;
	private final long lifespan;

	private static boolean canFetchCache;
	private static final ObjectArrayList<CachedResource<?>> CACHED_RESOURCES = new ObjectArrayList<>();

	public CachedResource(final Supplier<T> dataSupplier, final long lifespan) {
		this.dataSupplier = dataSupplier;
		this.lifespan = lifespan;
		CACHED_RESOURCES.add(this);
	}

	@Nullable
	public T getData(boolean force) {
		if (force || canFetchCache) {
			final long currentMillis = System.currentTimeMillis();
			if (data == null || currentMillis > expiry) {
				data = dataSupplier.get();
				canFetchCache = false;
			}
			expiry = currentMillis + lifespan;
		}
		return data;
	}

	public static void tick() {
		canFetchCache = true;
		final long currentMillis = System.currentTimeMillis();
		CACHED_RESOURCES.forEach(cachedResource -> {
			if (currentMillis > cachedResource.expiry) {
				cachedResource.data = null;
			}
		});
	}
}
