package org.mtr.resource;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.resource.BlockbenchElementSchema;

public final class BlockbenchElement extends BlockbenchElementSchema {

	public BlockbenchElement(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getUuid() {
		return uuid;
	}

	public Box setModelPart(ModelPartData parentModelPart, GroupTransformations groupTransformations, ModelDisplayPart modelDisplayPart) {
		// Add model Y offset when creating the model parts
		final float originX = -Utilities.getElement(origin, 0, 0D).floatValue();
		final float originY = -Utilities.getElement(origin, 1, 0D).floatValue();
		final float originZ = Utilities.getElement(origin, 2, 0D).floatValue();
		final float rotationX = (float) Math.toRadians(-Utilities.getElement(rotation, 0, 0D));
		final float rotationY = (float) Math.toRadians(-Utilities.getElement(rotation, 1, 0D));
		final float rotationZ = (float) Math.toRadians(Utilities.getElement(rotation, 2, 0D));
		final int textureX = Utilities.getElement(uv_offset, 0, 0L).intValue();
		final int textureY = Utilities.getElement(uv_offset, 1, 0L).intValue();

		final GroupTransformations newGroupTransformations = new GroupTransformations(groupTransformations, origin, rotation);
		final ModelPartData modelPartData = newGroupTransformations.create(parentModelPart, textureX, textureY);

		final float x = -Utilities.getElement(to, 0, 0D).floatValue() - originX;
		final float y = -Utilities.getElement(to, 1, 0D).floatValue() - originY;
		final float z = Utilities.getElement(from, 2, 0D).floatValue() - originZ;
		final int sizeX = (int) Math.round(Utilities.getElement(to, 0, 0D) - Utilities.getElement(from, 0, 0D));
		final int sizeY = (int) Math.round(Utilities.getElement(to, 1, 0D) - Utilities.getElement(from, 1, 0D));
		final int sizeZ = (int) Math.round(Utilities.getElement(to, 2, 0D) - Utilities.getElement(from, 2, 0D));

		modelPartData.addChild(MTR.randomString(), ModelPartBuilder.create().uv(textureX, textureY).mirrored(!shade || mirror_uv).cuboid(x, y, z, sizeX, sizeY, sizeZ, new Dilation((float) inflate)), ModelTransform.NONE);

		newGroupTransformations.create(modelDisplayPart.storedMatrixTransformations);
		modelDisplayPart.storedMatrixTransformations.add(matrixStack -> matrixStack.translate(x / 16, y / 16, z / 16));
		modelDisplayPart.width = sizeX;
		modelDisplayPart.height = sizeY;

		final Vec3d vector1 = new Vec3d(x, y, z).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);
		final Vec3d vector2 = new Vec3d(x + sizeX, y + sizeY, z + sizeZ).rotateX(rotationX).rotateY(rotationY).rotateZ(rotationZ);

		// Normalize dimensions (16 Blockbench units = 1 Minecraft block)
		return new Box(
				-(vector1.x + originX) / 16,
				-(vector1.y + originY) / 16,
				-(vector1.z + originZ) / 16,
				-(vector2.x + originX) / 16,
				-(vector2.y + originY) / 16,
				-(vector2.z + originZ) / 16
		);
	}
}
