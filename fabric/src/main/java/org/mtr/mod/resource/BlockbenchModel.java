package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.BlockbenchModelSchema;

public final class BlockbenchModel extends BlockbenchModelSchema {

	public BlockbenchModel(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);

		// Combine groups and outliner for the Blockbench 5.0 update
		outliner.forEach(blockbenchOutline -> {
			boolean needsMigration = true;
			for (final BlockbenchOutline group : groups) {
				if (blockbenchOutline.uuidEquals(group)) {
					group.childrenUuid.addAll(blockbenchOutline.childrenUuid);
					group.getChildren().addAll(blockbenchOutline.getChildren());
					needsMigration = false;
				}
			}
			if (needsMigration) {
				groups.add(blockbenchOutline);
			}
		});
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
		return groups;
	}
}
