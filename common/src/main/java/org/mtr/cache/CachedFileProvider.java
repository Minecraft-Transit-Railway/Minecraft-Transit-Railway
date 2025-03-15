package org.mtr.cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.MTR;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * A provider for a number of {@link CachedFileResource} objects.
 *
 * @param <T> the {@link CachedFileResource} implementation
 */
public abstract class CachedFileProvider<T extends CachedFileResource> {

	private final Path cacheDirectory;
	private final Long2ObjectAVLTreeMap<T> dataMap = new Long2ObjectAVLTreeMap<>();

	public CachedFileProvider(Path cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
		try {
			Files.createDirectories(cacheDirectory);
		} catch (IOException e) {
			MTR.LOGGER.error("", e);
		}
	}

	public final void tick() {
		final LongArrayList keysToRemove = new LongArrayList();
		dataMap.forEach((key, data) -> {
			if (data.canBeRemoved()) {
				keysToRemove.add(key.longValue());
			}
		});
		keysToRemove.forEach(dataMap::remove);
	}

	@Nullable
	protected final byte[] get(long key, Function<Path, T> createInstance) {
		return dataMap.computeIfAbsent(key, newKey -> createInstance.apply(cacheDirectory)).get();
	}
}
