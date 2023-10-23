package org.mtr.core.data;

import org.mtr.core.generated.BlockbenchResolutionSchema;
import org.mtr.core.serializers.ReaderBase;

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
