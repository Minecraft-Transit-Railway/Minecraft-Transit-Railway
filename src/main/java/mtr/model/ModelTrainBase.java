package mtr.model;

import mtr.entity.EntityTrainBase;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ModelTrainBase extends EntityModel<EntityTrainBase> {

	protected void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	@Override
	public void setAngles(EntityTrainBase entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, float doorLeftValue, float doorRightValue, boolean isEnd1Head, boolean isEnd2Head) {
		for (int position : getWindowPositions()) {
			renderWindowPositions(matrices, vertices, light, position);
		}
		for (int position : getDoorPositions()) {
			renderDoorPositions(matrices, vertices, light, position, doorLeftValue, doorRightValue);
		}

		if (isEnd1Head) {
			renderHeadPosition1(matrices, vertices, light, getEndPositions()[0]);
		} else {
			renderEndPosition1(matrices, vertices, light, getEndPositions()[0]);
		}

		if (isEnd2Head) {
			renderHeadPosition2(matrices, vertices, light, getEndPositions()[1]);
		} else {
			renderEndPosition2(matrices, vertices, light, getEndPositions()[1]);
		}
	}

	protected abstract void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, int light, int position);

	protected abstract void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, int light, int position, float doorLeftValue, float doorRightValue);

	protected abstract void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, int light, int position);

	protected abstract void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, int light, int position);

	protected abstract void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, int light, int position);

	protected abstract void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, int light, int position);

	protected abstract int[] getWindowPositions();

	protected abstract int[] getDoorPositions();

	protected abstract int[] getEndPositions();
}
