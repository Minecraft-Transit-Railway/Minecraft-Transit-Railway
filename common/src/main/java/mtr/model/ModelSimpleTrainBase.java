package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class ModelSimpleTrainBase extends ModelTrainBase {

	@Override
	protected final void render(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int car, int totalCars, boolean head1IsFront, boolean renderDetails) {
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;

		for (final int position : getWindowPositions()) {
			renderWindowPositions(matrices, vertices, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head);
		}
		for (final int position : getDoorPositions()) {
			renderDoorPositions(matrices, vertices, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head);
		}

		if (isEnd1Head) {
			renderHeadPosition1(matrices, vertices, renderStage, light, getEndPositions()[0], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, head1IsFront);
		} else {
			renderEndPosition1(matrices, vertices, renderStage, light, getEndPositions()[0], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
		}

		if (isEnd2Head) {
			renderHeadPosition2(matrices, vertices, renderStage, light, getEndPositions()[1], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, !head1IsFront);
		} else {
			renderEndPosition2(matrices, vertices, renderStage, light, getEndPositions()[1], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
		}
	}

	@Override
	protected void renderExtraDetails(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		for (final int position : getDoorPositions()) {
			final ModelDoorOverlay modelDoorOverlay = getModelDoorOverlay();
			if (modelDoorOverlay != null) {
				modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.INTERIOR, lightOnInteriorLevel, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
				modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.EXTERIOR, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
			}
			final ModelDoorOverlayTopBase modelDoorOverlayTop = getModelDoorOverlayTop();
			if (modelDoorOverlayTop != null) {
				modelDoorOverlayTop.render(matrices, vertexConsumers, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}
		}
	}

	protected abstract void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights);

	protected abstract void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights);

	protected abstract void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);

	protected abstract void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);

	protected abstract ModelDoorOverlay getModelDoorOverlay();

	protected abstract ModelDoorOverlayTopBase getModelDoorOverlayTop();

	protected abstract int[] getWindowPositions();

	protected abstract int[] getDoorPositions();

	protected abstract int[] getEndPositions();
}
