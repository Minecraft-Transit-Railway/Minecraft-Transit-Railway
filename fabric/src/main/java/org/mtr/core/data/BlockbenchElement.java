package org.mtr.core.data;

import org.mtr.core.generated.BlockbenchElementSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.core.tools.Utilities;
import org.mtr.mapping.mapper.ModelPartExtension;

public final class BlockbenchElement extends BlockbenchElementSchema {

	public BlockbenchElement(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getUuid() {
		return uuid;
	}

	public void setModelPart(ModelPartExtension modelPart) {
		final float originX = Utilities.getElement(origin, 0, 0D).floatValue();
		final float originY = Utilities.getElement(origin, 1, 0D).floatValue();
		final float originZ = Utilities.getElement(origin, 2, 0D).floatValue();
		modelPart.setPivot(-originX, -originY, originZ);
		modelPart.setRotation(
				(float) Math.toRadians(-Utilities.getElement(rotation, 0, 0D)),
				(float) Math.toRadians(-Utilities.getElement(rotation, 1, 0D)),
				(float) Math.toRadians(Utilities.getElement(rotation, 2, 0D))
		);
		modelPart.setTextureUVOffset(
				Utilities.getElement(uv_offset, 0, 0L).intValue(),
				Utilities.getElement(uv_offset, 1, 0L).intValue()
		);
		modelPart.addCuboid(
				originX - Utilities.getElement(to, 0, 0D).floatValue(),
				originY - Utilities.getElement(to, 1, 0D).floatValue(),
				Utilities.getElement(from, 2, 0D).floatValue() - originZ,
				Math.round(Utilities.getElement(to, 0, 0D).floatValue() - Utilities.getElement(from, 0, 0D).floatValue()),
				Math.round(Utilities.getElement(to, 1, 0D).floatValue() - Utilities.getElement(from, 1, 0D).floatValue()),
				Math.round(Utilities.getElement(to, 2, 0D).floatValue() - Utilities.getElement(from, 2, 0D).floatValue()),
				(float) inflate,
				!shade
		);
	}
}
