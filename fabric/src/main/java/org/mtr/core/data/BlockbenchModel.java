package org.mtr.core.data;

import org.mtr.core.generated.BlockbenchModelSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class BlockbenchModel extends BlockbenchModelSchema {

	public BlockbenchModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public int getTextureWidth() {
		return resolution.getTextureWidth();
	}

	public int getTextureHeight() {
		return resolution.getTextureHeight();
	}

	public ObjectArrayList<BlockbenchElement> getElements() {
		return elements;
	}

	public ObjectArrayList<BlockbenchOutline> getOutlines() {
		return outliner;
	}
}
