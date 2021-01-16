package mtr.model;

import mtr.entity.EntityTrainBase;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class ModelTrainBase extends EntityModel<EntityTrainBase> {

	public static int MAX_LIGHT = 0xF00000;

	@Override
	public void setAngles(EntityTrainBase entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public final void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier texture, int light, float doorLeftValue, float doorRightValue, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront) {
		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(texture)), RenderStage.LIGHTS, MAX_LIGHT, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, head1IsFront);
		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(texture)), RenderStage.INTERIOR, MAX_LIGHT, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, head1IsFront);

		for (int position : getDoorPositions()) {
			renderDoorLabels(matrices, vertexConsumers, RenderStage.INTERIOR, MAX_LIGHT, position / 16F, doorLeftValue, doorRightValue);
			renderDoorLabels(matrices, vertexConsumers, RenderStage.EXTERIOR, light, position / 16F, doorLeftValue, doorRightValue);
		}

		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInteriorTranslucent(texture)), RenderStage.INTERIOR_TRANSLUCENT, MAX_LIGHT, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, head1IsFront);
		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), RenderStage.EXTERIOR, light, doorLeftValue, doorRightValue, isEnd1Head, isEnd2Head, head1IsFront);
	}

	private void render(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftValue, float doorRightValue, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront) {
		for (int position : getWindowPositions()) {
			renderWindowPositions(matrices, vertices, renderStage, light, position);
		}
		for (int position : getDoorPositions()) {
			renderDoorPositions(matrices, vertices, renderStage, light, position, doorLeftValue, doorRightValue);
		}

		if (isEnd1Head) {
			renderHeadPosition1(matrices, vertices, renderStage, light, getEndPositions()[0], head1IsFront);
		} else {
			renderEndPosition1(matrices, vertices, renderStage, light, getEndPositions()[0]);
		}

		if (isEnd2Head) {
			renderHeadPosition2(matrices, vertices, renderStage, light, getEndPositions()[1], !head1IsFront);
		} else {
			renderEndPosition2(matrices, vertices, renderStage, light, getEndPositions()[1]);
		}
	}

	protected abstract void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position);

	protected abstract void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, float doorLeftValue, float doorRightValue);

	protected abstract void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean useHeadlights);

	protected abstract void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean useHeadlights);

	protected abstract void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position);

	protected abstract void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position);

	protected abstract void renderDoorLabels(MatrixStack matrices, VertexConsumerProvider vertexConsumers, RenderStage renderStage, int light, float positionScaled, float doorLeftValue, float doorRightValue);

	protected abstract int[] getWindowPositions();

	protected abstract int[] getDoorPositions();

	protected abstract int[] getEndPositions();

	protected static void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	protected static void renderMirror(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		renderOnce(bone, matrices, vertices, light, position);
		renderOnceFlipped(bone, matrices, vertices, light, position);
	}

	protected static void renderOnce(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		bone.setPivot(0, 0, position);
		setRotationAngle(bone, 0, 0, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected static void renderOnceFlipped(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		bone.setPivot(0, 0, position);
		setRotationAngle(bone, 0, (float) Math.PI, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected enum RenderStage {LIGHTS, INTERIOR, INTERIOR_TRANSLUCENT, EXTERIOR}
}
