package org.mtr.font;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.cache.CachedFileResource;

import javax.annotation.Nullable;
import java.nio.file.Path;

public abstract class FontResourceBase extends CachedFileResource {

	@Nullable
	private byte[] data;
	protected final int textureIndex;

	public static final int TEXTURE_CHAR_COUNT = 256;
	public static final int FONT_SIZE = 32;
	private static final int LIFESPAN = 60000;

	protected FontResourceBase(int textureIndex, Path path) {
		super(path, LIFESPAN);
		this.textureIndex = textureIndex;
	}

	@Override
	protected void dataUpdated(@Nullable byte[] data) {
		this.data = data;
	}

	@Nullable
	protected byte[] getData() {
		return data;
	}

	protected static void addToList(ByteArrayList byteArrayList, int glyphWidth, int glyphHeight, int additionalScaleOrXOffset, int yOffset, int advance) {
		byteArrayList.add((byte) glyphWidth);
		byteArrayList.add((byte) glyphHeight);
		byteArrayList.add((byte) additionalScaleOrXOffset);
		byteArrayList.add((byte) yOffset);
		byteArrayList.add((byte) advance);
	}
}
