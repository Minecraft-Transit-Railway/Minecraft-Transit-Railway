package org.mtr.mod.resource;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.ModelPartExtension;

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

	public ModelPartExtension create(ModelPartExtension rootModelPart, float modelYOffset) {
		ModelPartExtension modelPart = rootModelPart;
		float combinedPivotX = 0;
		float combinedPivotY = 0;
		float combinedPivotZ = 0;
		float offset = modelYOffset * 16;
		for (final GroupTransformation groupTransformation : groupTransformationList) {
			modelPart.setPivot(groupTransformation.pivotX - combinedPivotX, groupTransformation.pivotY - combinedPivotY - offset, groupTransformation.pivotZ - combinedPivotZ);
			combinedPivotX += groupTransformation.pivotX;
			combinedPivotY += groupTransformation.pivotY;
			combinedPivotZ += groupTransformation.pivotZ;
			modelPart.setRotation(groupTransformation.rotateX, groupTransformation.rotateY, groupTransformation.rotateZ);
			modelPart = modelPart.addChild();
			offset = 0;
		}
		return modelPart;
	}

	private static class GroupTransformation {

		private final float pivotX;
		private final float pivotY;
		private final float pivotZ;
		private final float rotateX;
		private final float rotateY;
		private final float rotateZ;

		private GroupTransformation(float pivotX, float pivotY, float pivotZ, float rotateX, float rotateY, float rotateZ) {
			this.pivotX = pivotX;
			this.pivotY = pivotY;
			this.pivotZ = pivotZ;
			this.rotateX = rotateX;
			this.rotateY = rotateY;
			this.rotateZ = rotateZ;
		}
	}
}
