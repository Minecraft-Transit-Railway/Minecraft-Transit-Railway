package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.generated.resource.BlockbenchElementSchema;

public final class BlockbenchElement extends BlockbenchElementSchema {

	public BlockbenchElement(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getUuid() {
		return uuid;
	}

	public Box setModelPart(ModelPartExtension parentModelPart, GroupTransformations groupTransformations, ModelDisplayPart modelDisplayPart, float modelYOffset) {
		// Add model Y offset when creating the model parts
		final float originX = -Utilities.getElement(origin, 0, 0D).floatValue();
		final float originY = -Utilities.getElement(origin, 1, 0D).floatValue() - modelYOffset * 16;
		final float originZ = Utilities.getElement(origin, 2, 0D).floatValue();
		final float rotationX = (float) Math.toRadians(-Utilities.getElement(rotation, 0, 0D));
		final float rotationY = (float) Math.toRadians(-Utilities.getElement(rotation, 1, 0D));
		final float rotationZ = (float) Math.toRadians(Utilities.getElement(rotation, 2, 0D));

		final GroupTransformations newGroupTransformations = new GroupTransformations(groupTransformations, origin, rotation);
		final ModelPartExtension modelPart = newGroupTransformations.create(parentModelPart, modelYOffset);
		modelPart.setTextureUVOffset(Utilities.getElement(uv_offset, 0, 0L).intValue(), Utilities.getElement(uv_offset, 1, 0L).intValue());

		final float x = -Utilities.getElement(to, 0, 0D).floatValue() - originX;
		final float y = -Utilities.getElement(to, 1, 0D).floatValue() - originY - modelYOffset * 16;
		final float z = Utilities.getElement(from, 2, 0D).floatValue() - originZ;
		final int sizeX = (int) Math.round(Utilities.getElement(to, 0, 0D) - Utilities.getElement(from, 0, 0D));
		final int sizeY = (int) Math.round(Utilities.getElement(to, 1, 0D) - Utilities.getElement(from, 1, 0D));
		final int sizeZ = (int) Math.round(Utilities.getElement(to, 2, 0D) - Utilities.getElement(from, 2, 0D));

		modelPart.addCuboid(x, y, z, sizeX, sizeY, sizeZ, (float) inflate, !shade || mirror_uv);

		newGroupTransformations.create(modelDisplayPart.storedMatrixTransformations, modelYOffset);
		modelDisplayPart.storedMatrixTransformations.add(graphicsHolder -> graphicsHolder.translate(x / 16, y / 16, z / 16));
		modelDisplayPart.width = sizeX;
		modelDisplayPart.height = sizeY;

		final Vector3d vector1 = new Vector3d(x, y, z).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);
		final Vector3d vector2 = new Vector3d(x + sizeX, y + sizeY, z + sizeZ).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);

		// Normalize dimensions (16 Blockbench units = 1 Minecraft block)
		return new Box(
				-(vector1.getXMapped() + originX) / 16,
				-(vector1.getYMapped() + originY) / 16,
				-(vector1.getZMapped() + originZ) / 16,
				-(vector2.getXMapped() + originX) / 16,
				-(vector2.getYMapped() + originY) / 16,
				-(vector2.getZMapped() + originZ) / 16
		);
	}
}
