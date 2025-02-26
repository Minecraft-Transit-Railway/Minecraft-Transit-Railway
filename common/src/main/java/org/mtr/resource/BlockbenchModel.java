package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.BlockbenchModelSchema;

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
