package org.mtr.font;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.mtr.cache.CachedFileResource;

import java.nio.file.Path;

public abstract class FontResourceBase extends CachedFileResource {

	protected final int textureIndex;

	private static final int LIFESPAN = 6000;

	protected FontResourceBase(int textureIndex, Path path) {
		super(path, LIFESPAN);
		this.textureIndex = textureIndex;
	}

	protected static void addToList(ByteArrayList byteArrayList, int glyphWidth, int glyphHeight, int additionalScaleOrXOffset, int yOffset, int advance) {
		byteArrayList.add((byte) glyphWidth);
		byteArrayList.add((byte) glyphHeight);
		byteArrayList.add((byte) additionalScaleOrXOffset);
		byteArrayList.add((byte) yOffset);
		byteArrayList.add((byte) advance);
	}
}
