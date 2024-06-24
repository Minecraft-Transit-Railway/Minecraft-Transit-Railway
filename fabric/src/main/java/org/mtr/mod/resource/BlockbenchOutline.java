package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.generated.resource.BlockbenchOutlineSchema;

public final class BlockbenchOutline extends BlockbenchOutlineSchema {

	public final ObjectArrayList<String> childrenUuid = new ObjectArrayList<>();
	public final boolean isValid;

	public BlockbenchOutline(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		isValid = !childrenUuid.isEmpty() || !children.isEmpty();
	}

	@Override
	public void updateData(ReaderBase readerBase) {
		super.updateData(readerBase);
		readerBase.iterateStringArray("children", childrenUuid::clear, childrenUuid::add);
	}

	public ObjectArrayList<BlockbenchOutline> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}
}
