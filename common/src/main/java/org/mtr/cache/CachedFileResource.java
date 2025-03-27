package org.mtr.cache;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.MTR;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * Represents a resource that is cached in the filesystem. The data is represented as a {@code byte} array.
 * <br/>
 * The resource also has a lifespan. After the timeout, the resource is regenerated when next requested and rewritten to the filesystem.
 */
public abstract class CachedFileResource {

	@Nullable
	private byte[] data;
	private long regenerateTimeout;
	private long fetchTimeout;

	protected final Path path;
	private final int lifespan;

	private static long nextGenerationTime;

	protected CachedFileResource(Path path, int lifespan) {
		this.path = path;
		this.lifespan = lifespan;
		final long currentTime = System.currentTimeMillis();
		fetchTimeout = currentTime + lifespan + getRandomTimeoutOffset();

		if (Files.exists(path)) {
			try {
				data = Files.readAllBytes(path);
				dataUpdated(data);
				regenerateTimeout = currentTime + lifespan + getRandomTimeoutOffset();
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}
	}

	/**
	 * Generate the data as necessary. If generation fails or is queued, the data can still be null.
	 */
	public final void get() {
		final long currentTime = System.currentTimeMillis();
		fetchTimeout = currentTime + lifespan + getRandomTimeoutOffset();
		if (currentTime > regenerateTimeout && currentTime > nextGenerationTime) {
			generateAndWrite(data);
			nextGenerationTime = currentTime + 100;
		}
	}

	/**
	 * @return if this {@link CachedFileResource} hasn't been fetched in a while and can be destroyed
	 */
	public final boolean canBeRemoved() {
		return System.currentTimeMillis() > fetchTimeout;
	}

	/**
	 * Generates the main data. If the previous value is returned, nothing will be changed and nothing will be written to file.
	 *
	 * @param oldData the data that was there previously or {@code null} if nothing was previously set
	 * @return new data or the same object if nothing needs to be changed or {@code null} if the file should be deleted
	 */
	@Nullable
	protected abstract byte[] generate(@Nullable byte[] oldData);

	/**
	 * Called whenever the data has been updated (loaded from a file or regenerated).
	 *
	 * @param data the new data
	 */
	protected abstract void dataUpdated(@Nullable byte[] data);

	private void generateAndWrite(@Nullable byte[] oldData) {
		final byte[] newData = generate(oldData);

		if (newData != oldData) {
			data = newData;
			dataUpdated(data);
			try {
				if (data == null) {
					Files.deleteIfExists(path);
				} else {
					Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				}
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}

		regenerateTimeout = System.currentTimeMillis() + lifespan + getRandomTimeoutOffset();
	}

	/**
	 * Helper method to get an {@code int} value from four {@code byte} values from the array.
	 *
	 * @param data  the {@code byte} array
	 * @param index the starting index of the four values to read
	 * @return the converted {@code int} value
	 */
	public static int getInt(byte[] data, int index) {
		return new Color(data[index + 1] & 0xFF, data[index + 2] & 0xFF, data[index + 3] & 0xFF, data[index] & 0xFF).getRGB();
	}

	/**
	 * Helper method to write an {@code int} value to four {@code byte} values in a {@link ByteArrayList}.
	 *
	 * @param byteArrayList the list to write to
	 * @param value         the {@code int} value
	 */
	protected static void writeInt(ByteArrayList byteArrayList, int value) {
		final Color color = new Color(value, true);
		byteArrayList.add((byte) color.getAlpha());
		byteArrayList.add((byte) color.getRed());
		byteArrayList.add((byte) color.getGreen());
		byteArrayList.add((byte) color.getBlue());
	}

	/**
	 * Helper method to write an image pixel array to colour and repeated pixel count.
	 *
	 * @param pixels   image pixel array
	 * @param callback a {@link BiConsumer} for the colour and repeated pixel count
	 */
	protected static void writeImage(int[] pixels, BiConsumer<Integer, Byte> callback) {
		int currentColor = 0;
		int count = 0;
		for (int i = 0; i <= pixels.length; i++) {
			final int color = i == pixels.length ? currentColor + 1 : pixels[i];
			if (color != currentColor && count > 0 || count > 0xFF) {
				callback.accept(currentColor, (byte) (count - 1));
				count = 0;
			}
			currentColor = color;
			count++;
		}
	}

	private static int getRandomTimeoutOffset() {
		return new Random().nextInt(500);
	}
}
