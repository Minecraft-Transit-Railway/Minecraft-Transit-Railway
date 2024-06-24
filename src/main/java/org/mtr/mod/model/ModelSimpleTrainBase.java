package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class ModelSimpleTrainBase<T> extends ModelTrainBase {

	public ModelSimpleTrainBase(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

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
			final ModelDoorOverlay modelDoorOverlay = renderDoorOverlay ? getModelDoorOverlay() : null;
			if (modelDoorOverlay != null) {
				modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.INTERIOR, lightOnInteriorLevel, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
				modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.EXTERIOR, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
			}
			final ModelDoorOverlayTopBase modelDoorOverlayTop = renderDoorOverlay ? getModelDoorOverlayTop() : null;
			if (modelDoorOverlayTop != null) {
				modelDoorOverlayTop.render(matrices, vertexConsumers, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}
		}
	}

	protected void renderFrontDestination(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, float maxWidth, float maxHeight, int colorCjk, int color, float fontSizeRatio, String text, boolean padOneLine, int car, int totalCars) {
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;

		for (int i = 0; i < 2; i++) {
			if (i == 0 && isEnd1Head || i == 1 && isEnd2Head) {
				matrices.pushPose();
				if (i == 1) {
					UtilitiesClient.rotateYDegrees(matrices, 180);
				}
				matrices.translate(x1, y1, z1);
				if (rotationY != 0) {
					UtilitiesClient.rotateYDegrees(matrices, rotationY);
				}
				if (rotationX != 0) {
					UtilitiesClient.rotateXDegrees(matrices, rotationX);
				}
				matrices.translate(x2, y2, z2);
				IDrawing.drawStringWithFont(matrices, font, immediate, text, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, maxWidth, (padOneLine && !text.contains("|") ? IGui.isCjk(text) ? fontSizeRatio / (fontSizeRatio + 1) : 0.5F : 1) * maxHeight, 1, colorCjk, color, fontSizeRatio, false, MAX_LIGHT_GLOWING, null);
				matrices.popPose();
			}
		}
	}

	public abstract T createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay);

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
