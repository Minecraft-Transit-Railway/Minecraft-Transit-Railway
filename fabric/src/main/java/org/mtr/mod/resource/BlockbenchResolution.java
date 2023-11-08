package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.BlockbenchResolutionSchema;

public final class BlockbenchResolution extends BlockbenchResolutionSchema {

	public BlockbenchResolution(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	int getTextureWidth() {
		return (int) width;
	}

	int getTextureHeight() {
		return (int) height;
	}
}
