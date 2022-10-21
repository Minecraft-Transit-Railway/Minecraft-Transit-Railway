package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.ArrayList;
import java.util.List;

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
	protected void renderExtraDetails(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int car, int totalCars, int stopIndex, List<Long> routeIds) {
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

		if (routeIds != null) {
			final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			if (!RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> renderTextDisplays(matrices, Minecraft.getInstance().font, immediate, thisRoute, nextRoute, thisStation, nextStation, lastStation, thisRoute == null ? null : thisRoute.getDestination(currentStationIndex), car, totalCars))) {
				renderTextDisplays(matrices, Minecraft.getInstance().font, immediate, null, null, null, null, null, null, car, totalCars);
			}
			immediate.endBatch();
		}
	}

	protected void renderTextDisplays(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars) {
	}

	protected void renderFrontDestination(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, float maxWidth, float maxHeight1, float maxHeight2, int color1, int color2, boolean spaceCjk, Station station, String customDestination, int car, int totalCars) {
		renderFrontDestination(matrices, font, immediate, x, y, z, x1, y1, z1, x2, y2, z2, rotationX, rotationY, HorizontalAlignment.CENTER, HorizontalAlignment.CENTER, maxWidth, maxHeight1, maxHeight2, color1, color2, spaceCjk, station, customDestination, car, totalCars);
	}

	protected void renderFrontDestination(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, HorizontalAlignment horizontalAlignment1, HorizontalAlignment horizontalAlignment2, float maxWidth, float maxHeight1, float maxHeight2, int color1, int color2, boolean spaceCjk, Station station, String customDestination, int car, int totalCars) {
		final String[] stationSplit = (customDestination == null ? station == null ? defaultDestinationString() : station.name : customDestination).split("\\|");
		final List<String> stationNameCjk = new ArrayList<>();
		final List<String> stationName = new ArrayList<>();
		for (final String stationNamePart : stationSplit) {
			if (IGui.isCjk(stationNamePart)) {
				if (spaceCjk && stationNamePart.length() == 2) {
					stationNameCjk.add(stationNamePart.charAt(0) + " " + stationNamePart.charAt(1));
				} else {
					stationNameCjk.add(stationNamePart);
				}
			} else {
				stationName.add(stationNamePart);
			}
		}
		if (!stationNameCjk.isEmpty()) {
			renderFrontDestination(matrices, font, immediate, x, y, z, x1, y1, z1, rotationX, rotationY, horizontalAlignment1, maxWidth, maxHeight1, color1, String.join("|", stationNameCjk), car, totalCars);
		}
		if (!stationName.isEmpty()) {
			renderFrontDestination(matrices, font, immediate, x, y, z, x2, y2, z2, rotationX, rotationY, horizontalAlignment2, maxWidth, maxHeight2, color2, String.join("|", stationName), car, totalCars);
		}
	}

	protected void renderFrontDestination(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, float maxWidth, float maxHeight, int color, Station station, int car, int totalCars) {
		renderFrontDestination(matrices, font, immediate, x1, y1, z1, x2, y2, z2, rotationX, rotationY, HorizontalAlignment.CENTER, maxWidth, maxHeight, color, (station == null ? defaultDestinationString() : station.name), car, totalCars);
	}

	protected String defaultDestinationString() {
		return "";
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

	private void renderFrontDestination(PoseStack matrices, Font font, MultiBufferSource.BufferSource immediate, float x1, float y1, float z1, float x2, float y2, float z2, float rotationX, float rotationY, HorizontalAlignment horizontalAlignment, float maxWidth, float maxHeight, int color, String text, int car, int totalCars) {
		final boolean isEnd1Head = car == 0;
		final boolean isEnd2Head = car == totalCars - 1;

		for (int i = 0; i < 2; i++) {
			if (i == 0 && isEnd1Head || i == 1 && isEnd2Head) {
				matrices.pushPose();
				if (i == 1) {
					matrices.mulPose(Vector3f.YP.rotationDegrees(180));
				}
				matrices.translate(x1, y1, z1);
				if (rotationY != 0) {
					matrices.mulPose(Vector3f.YP.rotationDegrees(rotationY));
				}
				if (rotationX != 0) {
					matrices.mulPose(Vector3f.XP.rotationDegrees(rotationX));
				}
				matrices.translate(x2, y2, z2);
				IDrawing.drawStringWithFont(matrices, font, immediate, text.toUpperCase(), horizontalAlignment, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, maxWidth, maxHeight, 1, color, false, MAX_LIGHT_GLOWING, null);
				matrices.popPose();
			}
		}
	}
}
