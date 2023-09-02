package org.mtr.mod.model;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;

public abstract class ModelSimpleTrainBase<T> extends ModelTrainBase {

	public ModelSimpleTrainBase(int textureWidth, int textureHeight, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(textureWidth, textureHeight, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected final void render(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int car, int totalCars, boolean head1IsFront, boolean renderDetails) {
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;

		for (final int position : getWindowPositions()) {
			renderWindowPositions(graphicsHolder, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head);
		}
		for (final int position : getDoorPositions()) {
			renderDoorPositions(graphicsHolder, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head);
		}

		if (isEnd1Head) {
			renderHeadPosition1(graphicsHolder, renderStage, light, getEndPositions()[0], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, head1IsFront);
		} else {
			renderEndPosition1(graphicsHolder, renderStage, light, getEndPositions()[0], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
		}

		if (isEnd2Head) {
			renderHeadPosition2(graphicsHolder, renderStage, light, getEndPositions()[1], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, !head1IsFront);
		} else {
			renderEndPosition2(graphicsHolder, renderStage, light, getEndPositions()[1], renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
		}
	}

	@Override
	protected void renderExtraDetails(GraphicsHolder graphicsHolder, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		for (final int position : getDoorPositions()) {
			final ModelDoorOverlay modelDoorOverlay = renderDoorOverlay ? getModelDoorOverlay() : null;
			if (modelDoorOverlay != null) {
				modelDoorOverlay.render(graphicsHolder, RenderStage.INTERIOR, lightOnInteriorLevel, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
				modelDoorOverlay.render(graphicsHolder, RenderStage.EXTERIOR, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
			}
			final ModelDoorOverlayTopBase modelDoorOverlayTop = renderDoorOverlay ? getModelDoorOverlayTop() : null;
			if (modelDoorOverlayTop != null) {
				modelDoorOverlayTop.render(graphicsHolder, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}
		}
	}

	protected void renderFrontDestination(GraphicsHolder graphicsHolder, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, float maxWidth, float maxHeight, int colorCjk, int color, float fontSizeRatio, String text, boolean padOneLine, int car, int totalCars) {
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;

		for (int i = 0; i < 2; i++) {
			if (i == 0 && isEnd1Head || i == 1 && isEnd2Head) {
				graphicsHolder.push();
				if (i == 1) {
					graphicsHolder.rotateYDegrees(180);
				}
				graphicsHolder.translate(x1, y1, z1);
				if (rotationY != 0) {
					graphicsHolder.rotateYDegrees(rotationY);
				}
				if (rotationX != 0) {
					graphicsHolder.rotateXDegrees(rotationX);
				}
				graphicsHolder.translate(x2, y2, z2);
				IDrawing.drawStringWithFont(graphicsHolder, text, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, maxWidth, (padOneLine && !text.contains("|") ? IGui.isCjk(text) ? fontSizeRatio / (fontSizeRatio + 1) : 0.5F : 1) * maxHeight, 1, colorCjk, color, fontSizeRatio, false, MAX_LIGHT_GLOWING, null);
				graphicsHolder.pop();
			}
		}
	}

	public abstract T createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay);

	protected abstract void renderWindowPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderDoorPositions(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderHeadPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights);

	protected abstract void renderHeadPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights);

	protected abstract void renderEndPosition1(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);

	protected abstract void renderEndPosition2(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ);

	protected abstract ModelDoorOverlay getModelDoorOverlay();

	protected abstract ModelDoorOverlayTopBase getModelDoorOverlayTop();

	protected abstract int[] getWindowPositions();

	protected abstract int[] getDoorPositions();

	protected abstract int[] getEndPositions();
}
