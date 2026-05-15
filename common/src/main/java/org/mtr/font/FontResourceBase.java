package org.mtr.font;

import org.jspecify.annotations.Nullable;
import org.mtr.cache.CachedFileResource;
import org.mtr.libraries.it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.nio.file.Path;

public abstract class FontResourceBase extends CachedFileResource {

	private byte @Nullable [] data;
	protected final int textureIndex;

	public static final int TEXTURE_CHAR_COUNT = 256;
	public static final int FONT_SIZE = 32;
	private static final int LIFESPAN = 60000;

	protected FontResourceBase(int textureIndex, Path path) {
		super(path, LIFESPAN);
		this.textureIndex = textureIndex;
	}

	@Override
	protected void dataUpdated(byte @Nullable [] data) {
		this.data = data;
	}

	protected byte @Nullable [] getData() {
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
