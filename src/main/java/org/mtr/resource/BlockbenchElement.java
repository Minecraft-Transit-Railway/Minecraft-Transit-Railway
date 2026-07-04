package org.mtr.resource;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.mtr.MTR;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.resource.BlockbenchElementSchema;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.render.StoredMatrixTransformations;

public final class BlockbenchElement extends BlockbenchElementSchema {

	public BlockbenchElement(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getUuid() {
		return uuid;
	}

	public ObjectObjectImmutablePair<AABB, ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> setModelPart(PartDefinition parentModelPart, GroupTransformations groupTransformations) {
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
		final PartDefinition modelPartData = newGroupTransformations.create(parentModelPart, textureX, textureY);

		final float x = -Utilities.getElement(to, 0, 0D).floatValue() - originX;
		final float y = -Utilities.getElement(to, 1, 0D).floatValue() - originY;
		final float z = Utilities.getElement(from, 2, 0D).floatValue() - originZ;
		final int sizeX = (int) Math.round(Utilities.getElement(to, 0, 0D) - Utilities.getElement(from, 0, 0D));
		final int sizeY = (int) Math.round(Utilities.getElement(to, 1, 0D) - Utilities.getElement(from, 1, 0D));
		final int sizeZ = (int) Math.round(Utilities.getElement(to, 2, 0D) - Utilities.getElement(from, 2, 0D));

		modelPartData.addOrReplaceChild(MTR.randomString(), CubeListBuilder.create().texOffs(textureX, textureY).mirror(!shade || mirror_uv).addBox(x, y, z, sizeX, sizeY, sizeZ, new CubeDeformation((float) inflate)), PartPose.ZERO);

		final StoredMatrixTransformations storedMatrixTransformationsDisplay = new StoredMatrixTransformations(0, 0, 0);
		// Write rotation to modelDisplayPart
		newGroupTransformations.create(storedMatrixTransformationsDisplay);
		storedMatrixTransformationsDisplay.add(matrixStack -> matrixStack.translate(x / 16, y / 16, z / 16));

		final Vec3 vector1 = new Vec3(x, y, z).xRot(rotationX).yRot(rotationY).zRot(rotationZ);
		final Vec3 vector2 = new Vec3(x + sizeX, y + sizeY, z + sizeZ).xRot(rotationX).yRot(rotationY).zRot(rotationZ);

		// Normalize dimensions (16 Blockbench units = 1 Minecraft block)
		return new ObjectObjectImmutablePair<>(new AABB(
			-(vector1.x + originX) / 16,
			-(vector1.y + originY) / 16,
			-(vector1.z + originZ) / 16,
			-(vector2.x + originX) / 16,
			-(vector2.y + originY) / 16,
			-(vector2.z + originZ) / 16
		), new ObjectObjectImmutablePair<>(storedMatrixTransformationsDisplay, new IntIntImmutablePair(sizeX, sizeY)));
	}
}
