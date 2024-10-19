package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.Init;
import org.mtr.mod.generated.resource.BlockbenchOutlineSchema;

import javax.annotation.Nullable;

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

	public GroupTransformations add(GroupTransformations groupTransformations, @Nullable BlockbenchOutline previousBlockbenchOutline, String id) {
		if (previousBlockbenchOutline == null && (Math.abs(Utilities.getElement(rotation, 0, 0D)) > 1 || Math.abs(Utilities.getElement(rotation, 1, 0D)) > 1 || Math.abs(Utilities.getElement(rotation, 2, 0D)) > 1)) {
			Init.LOGGER.warn("[{}] Skipping Blockbench outline rotation for element \"{}\"! {}", id, name, rotation);
		}
		return new GroupTransformations(groupTransformations, previousBlockbenchOutline == null ? origin : DoubleArrayList.of(
				Utilities.getElement(origin, 0, 0D) - Utilities.getElement(previousBlockbenchOutline.origin, 0, 0D),
				Utilities.getElement(origin, 1, 0D) - Utilities.getElement(previousBlockbenchOutline.origin, 1, 0D),
				Utilities.getElement(origin, 2, 0D) - Utilities.getElement(previousBlockbenchOutline.origin, 2, 0D)
		), previousBlockbenchOutline == null ? DoubleArrayList.of(0, 0, 0) : rotation);
	}
}
