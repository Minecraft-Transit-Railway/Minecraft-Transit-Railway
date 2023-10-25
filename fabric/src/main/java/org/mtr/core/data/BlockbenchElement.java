package org.mtr.core.data;

import org.mtr.core.generated.BlockbenchElementSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.core.tools.Utilities;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.ModelPartExtension;

public final class BlockbenchElement extends BlockbenchElementSchema {

	public BlockbenchElement(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getUuid() {
		return uuid;
	}

	public Box setModelPart(ModelPartExtension modelPart) {
		final float originX = -Utilities.getElement(origin, 0, 0D).floatValue();
		final float originY = -Utilities.getElement(origin, 1, 0D).floatValue();
		final float originZ = Utilities.getElement(origin, 2, 0D).floatValue();
		final float rotationX = (float) Math.toRadians(-Utilities.getElement(rotation, 0, 0D));
		final float rotationY = (float) Math.toRadians(-Utilities.getElement(rotation, 1, 0D));
		final float rotationZ = (float) Math.toRadians(Utilities.getElement(rotation, 2, 0D));

		modelPart.setPivot(originX, originY, originZ);
		modelPart.setRotation(rotationX, rotationY, rotationZ);
		modelPart.setTextureUVOffset(Utilities.getElement(uv_offset, 0, 0L).intValue(), Utilities.getElement(uv_offset, 1, 0L).intValue());

		final float x = -Utilities.getElement(to, 0, 0D).floatValue() - originX;
		final float y = -Utilities.getElement(to, 1, 0D).floatValue() - originY;
		final float z = Utilities.getElement(from, 2, 0D).floatValue() - originZ;
		final int sizeX = (int) Math.round(Utilities.getElement(to, 0, 0D) - Utilities.getElement(from, 0, 0D));
		final int sizeY = (int) Math.round(Utilities.getElement(to, 1, 0D) - Utilities.getElement(from, 1, 0D));
		final int sizeZ = (int) Math.round(Utilities.getElement(to, 2, 0D) - Utilities.getElement(from, 2, 0D));

		modelPart.addCuboid(x, y, z, sizeX, sizeY, sizeZ, (float) inflate, !shade);

		final Vector3d vector1 = new Vector3d(x, y, z).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);
		final Vector3d vector2 = new Vector3d(x + sizeX, y + sizeY, z + sizeZ).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);

		return new Box(
				vector1.getXMapped() + originX,
				vector1.getYMapped() + originY,
				vector1.getZMapped() + originZ,
				vector2.getXMapped() + originX,
				vector2.getYMapped() + originY,
				vector2.getZMapped() + originZ
		);
	}
}
