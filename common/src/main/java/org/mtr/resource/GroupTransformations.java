package org.mtr.resource;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import org.mtr.MTR;
import org.mtr.core.tool.Utilities;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.tool.Drawing;

public final class GroupTransformations {

	private final ObjectArrayList<GroupTransformation> groupTransformationList = new ObjectArrayList<>();

	public GroupTransformations() {
	}

	public GroupTransformations(GroupTransformations groupTransformations, DoubleArrayList origin, DoubleArrayList rotation) {
		groupTransformationList.addAll(groupTransformations.groupTransformationList);
		groupTransformationList.add(new GroupTransformation(
				-Utilities.getElement(origin, 0, 0D).floatValue(),
				-Utilities.getElement(origin, 1, 0D).floatValue(),
				Utilities.getElement(origin, 2, 0D).floatValue(),
				(float) Math.toRadians(-Utilities.getElement(rotation, 0, 0D)),
				(float) Math.toRadians(-Utilities.getElement(rotation, 1, 0D)),
				(float) Math.toRadians(Utilities.getElement(rotation, 2, 0D))
		));
	}

	public ModelPartData create(ModelPartData rootModelPart, int textureX, int textureY) {
		ModelPartData modelPart = rootModelPart;
		float combinedPivotX = 0;
		float combinedPivotY = 0;
		float combinedPivotZ = 0;
		for (final GroupTransformation groupTransformation : groupTransformationList) {
			modelPart = modelPart.addChild(MTR.randomString(), ModelPartBuilder.create().uv(textureX, textureY), ModelTransform.of(
					groupTransformation.pivotX - combinedPivotX,
					groupTransformation.pivotY - combinedPivotY,
					groupTransformation.pivotZ - combinedPivotZ,
					groupTransformation.rotateX,
					groupTransformation.rotateY,
					groupTransformation.rotateZ
			));
			combinedPivotX += groupTransformation.pivotX;
			combinedPivotY += groupTransformation.pivotY;
			combinedPivotZ += groupTransformation.pivotZ;
		}
		return modelPart;
	}

	public void create(StoredMatrixTransformations storedMatrixTransformations) {
		float combinedPivotX = 0;
		float combinedPivotY = 0;
		float combinedPivotZ = 0;
		for (final GroupTransformation groupTransformation : groupTransformationList) {
			float newCombinedPivotX = (groupTransformation.pivotX - combinedPivotX) / 16;
			float newCombinedPivotY = (groupTransformation.pivotY - combinedPivotY) / 16;
			float newCombinedPivotZ = (groupTransformation.pivotZ - combinedPivotZ) / 16;
			storedMatrixTransformations.add(matrixStack -> {
				matrixStack.translate(newCombinedPivotX, newCombinedPivotY, newCombinedPivotZ);
				Drawing.rotateZRadians(matrixStack, groupTransformation.rotateZ);
				Drawing.rotateYRadians(matrixStack, groupTransformation.rotateY);
				Drawing.rotateXRadians(matrixStack, groupTransformation.rotateX);
			});
			combinedPivotX += groupTransformation.pivotX;
			combinedPivotY += groupTransformation.pivotY;
			combinedPivotZ += groupTransformation.pivotZ;
		}
	}

	private record GroupTransformation(float pivotX, float pivotY, float pivotZ, float rotateX, float rotateY, float rotateZ) {
	}
}
