package org.mtr.mod.render;

import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleObjectImmutablePair;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Window;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.RailType;
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nullable;

public final class DrivingGuiRenderer {

	@Nullable
	private static VehicleExtension vehicle;

	private static final int EDGE_PADDING = 16;
	private static final int PADDING = 4;
	private static final int TOOL_SIZE = 96;
	private static final int PLATFORM_BAR_SIZE = 6;
	private static final int SMALL_LINE_SPACING = 6;

	private static final int SPEEDOMETER_CIRCLE_INTERVAL = 3;
	private static final double SPEEDOMETER_CIRCLE_EDGE_LENGTH = Math.tan(Math.toRadians(SPEEDOMETER_CIRCLE_INTERVAL) / 2) * TOOL_SIZE;
	private static final int SPEEDOMETER_SPAN = 300;
	private static final int SPEEDOMETER_START_ANGLE = -60;
	private static final int SPEEDOMETER_TICK_INTERVAL = 5;

	private static final int ATS_SLICES = 64;
	private static final float ATS_INTERVAL = 360F / ATS_SLICES;
	private static final int ATS_RADIUS_1 = 9;
	private static final int ATS_RADIUS_2 = 8;
	private static final int ATS_RADIUS_3 = 5;
	private static final int ATS_RADIUS_4 = 1;
	private static final double ATS_CIRCLE_EDGE_HALF_LENGTH_1 = Math.tan(Math.toRadians(ATS_INTERVAL) / 2) * ATS_RADIUS_1;
	private static final double ATS_CIRCLE_EDGE_HALF_LENGTH_2 = Math.tan(Math.toRadians(ATS_INTERVAL) / 2) * ATS_RADIUS_2;
	private static final double ATS_CIRCLE_EDGE_HALF_LENGTH_3 = Math.tan(Math.toRadians(ATS_INTERVAL) / 2) * ATS_RADIUS_3;
	private static final double ATS_CIRCLE_EDGE_HALF_LENGTH_4 = Math.tan(Math.toRadians(ATS_INTERVAL) / 2) * ATS_RADIUS_4;

	private static final int BLUE_COLOR = 0xFFAACCFF;
	private static final int ORANGE_COLOR = 0xFFFF9900;

	public static void render(GraphicsHolder graphicsHolder) {
		if (vehicle != null) {
			final VehicleExtraData vehicleExtraData = vehicle.vehicleExtraData;
			final Window window = MinecraftClient.getInstance().getWindow();
			final int speedometerX = window.getScaledWidth() - TOOL_SIZE - EDGE_PADDING;
			final int speedometerY = window.getScaledHeight() - TOOL_SIZE - EDGE_PADDING;
			final int radius = TOOL_SIZE / 2;

			graphicsHolder.push();
			graphicsHolder.translate(speedometerX + radius, speedometerY + radius, 0);
			graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));

			// Render speedometer background
			graphicsHolder.push();
			for (int i = 0; i < 180; i += SPEEDOMETER_CIRCLE_INTERVAL) {
				IDrawing.drawTexture(
						graphicsHolder,
						-radius, -(float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2, -2,
						radius, (float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2, -2,
						Direction.NORTH, 0xFF666666, GraphicsHolder.getDefaultLight()
				);
				IDrawing.drawTexture(
						graphicsHolder,
						-radius + 1, -(float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2, -1,
						radius - 1, (float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2, -1,
						Direction.NORTH, 0xFF111111, GraphicsHolder.getDefaultLight()
				);
				graphicsHolder.rotateZDegrees(SPEEDOMETER_CIRCLE_INTERVAL);
			}
			graphicsHolder.pop();

			// Draw ATS
			graphicsHolder.push();
			graphicsHolder.translate(0, radius - 2 - ATS_RADIUS_1, 0);
			graphicsHolder.rotateZDegrees(ATS_INTERVAL * 2.5F);
			for (float i = 0; i < ATS_SLICES; i++) {
				IDrawing.drawTexture(
						graphicsHolder,
						-ATS_RADIUS_1, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_1, -1,
						ATS_RADIUS_1, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_1, -1,
						Direction.NORTH, 0xFF1A1A1A, GraphicsHolder.getDefaultLight()
				);
				if (i % 8 < 4) {
					if (vehicle.isVehiclePastSafeStoppingDistance()) {
						IDrawing.drawTexture(
								graphicsHolder,
								-ATS_RADIUS_2, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_2, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_3, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_3, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								0, 0, 1, 1,
								Direction.NORTH, ORANGE_COLOR, GraphicsHolder.getDefaultLight()
						);
						IDrawing.drawTexture(
								graphicsHolder,
								-ATS_RADIUS_3, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_3, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_4, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								-ATS_RADIUS_4, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								0, 0, 1, 1,
								Direction.NORTH, IGui.ARGB_WHITE, GraphicsHolder.getDefaultLight()
						);
					} else {
						IDrawing.drawTexture(
								graphicsHolder,
								-ATS_RADIUS_2, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_2, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_4, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								-ATS_RADIUS_4, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								0, 0, 1, 1,
								Direction.NORTH, 0xFF222222, GraphicsHolder.getDefaultLight()
						);
					}
				}
				graphicsHolder.rotateZDegrees(ATS_INTERVAL);
			}
			graphicsHolder.pop();

			// Draw speedometer ticks
			graphicsHolder.push();
			final int maxSpeedKilometersPerHour = vehicleExtraData.getIsManualAllowed() ? (int) Math.round(vehicleExtraData.getMaxManualSpeed() * 3600) : RailType.DIAMOND.speedLimit;
			graphicsHolder.rotateZDegrees(SPEEDOMETER_START_ANGLE);
			for (int i = 0; i <= maxSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL) {
				IDrawing.drawTexture(
						graphicsHolder,
						-radius + 2, -0.5F, 0,
						-radius + (i % 20 == 0 ? 10 : i % 10 == 0 ? 8 : 4), 0.5F, 0,
						Direction.NORTH,
						vehicle.getSpeedLimitKilometersPerHour() > 0 && i == Math.min(vehicle.getSpeedLimitKilometersPerHour(), maxSpeedKilometersPerHour) ? 0xFF00FF00 : IGui.ARGB_WHITE,
						GraphicsHolder.getDefaultLight()
				);
				graphicsHolder.rotateZDegrees((float) SPEEDOMETER_TICK_INTERVAL * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			}
			graphicsHolder.pop();

			// Draw speedometer needle
			graphicsHolder.push();
			final double speedKilometersPerHour = vehicle.getSpeed() * 3600;
			graphicsHolder.rotateZDegrees(SPEEDOMETER_START_ANGLE + (float) speedKilometersPerHour * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			IDrawing.drawTexture(
					graphicsHolder,
					-radius + 4, -0.5F, 1,
					0, 0.5F, 1,
					Direction.NORTH,
					0xFFFF0000,
					GraphicsHolder.getDefaultLight()
			);
			graphicsHolder.pop();

			// Draw speedometer labels
			graphicsHolder.push();
			graphicsHolder.rotateZDegrees(SPEEDOMETER_START_ANGLE);
			for (int i = 0; i <= maxSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL * 4) {
				graphicsHolder.push();
				graphicsHolder.translate(-radius + 10 + IGui.LINE_HEIGHT / 2F, 0, 0);
				graphicsHolder.rotateZDegrees(-SPEEDOMETER_START_ANGLE - (float) i * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
				graphicsHolder.scale(0.5F, 0.5F, 1);
				drawCenteredText(graphicsHolder, String.valueOf(i), IGui.ARGB_WHITE);
				graphicsHolder.pop();
				graphicsHolder.rotateZDegrees((float) SPEEDOMETER_TICK_INTERVAL * 4 * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			}
			graphicsHolder.pop();

			// Draw power level
			graphicsHolder.push();
			graphicsHolder.translate(-radius * 0.3F, -TOOL_SIZE * 0.05F - SMALL_LINE_SPACING, 0);
			final int notch = vehicleExtraData.getPowerLevel();
			final int notchColor = notch < -Vehicle.MAX_POWER_LEVEL ? 0xFFFF0000 : notch < 0 ? ORANGE_COLOR : notch > 0 ? BLUE_COLOR : IGui.ARGB_WHITE;
			drawCenteredText(graphicsHolder, notch < -Vehicle.MAX_POWER_LEVEL ? "E" : notch < 0 ? "B" + -notch : notch > 0 ? "P" + notch : "N", notchColor);
			if (notch != 0 && notch >= -Vehicle.MAX_POWER_LEVEL) {
				graphicsHolder.translate(0, SMALL_LINE_SPACING, 0);
				graphicsHolder.scale(0.5F, 0.5F, 1);
				drawCenteredText(graphicsHolder, String.format("(%s%%)", Math.abs(notch) * 100 / Vehicle.POWER_LEVEL_RATIO), notchColor);
			}
			graphicsHolder.pop();

			// Draw ATO status
			graphicsHolder.push();
			graphicsHolder.translate(0, -TOOL_SIZE * 0.15F - SMALL_LINE_SPACING, 0);
			drawCenteredText(graphicsHolder, "ATO", vehicleExtraData.getIsCurrentlyManual() ? 0xFF222222 : 0xFF00FF00);
			graphicsHolder.pop();

			// Draw door status
			graphicsHolder.push();
			graphicsHolder.translate(radius * 0.3F, -TOOL_SIZE * 0.05F - SMALL_LINE_SPACING, 0);
			drawCenteredText(graphicsHolder, vehicleExtraData.getDoorMultiplier() > 0 ? "DO" : "DC", IGui.ARGB_WHITE);
			graphicsHolder.translate(0, SMALL_LINE_SPACING, 0);
			graphicsHolder.scale(0.5F, 0.5F, 1);
			drawCenteredText(graphicsHolder, String.format("(%s%%)", (int) Math.round(vehicle.persistentVehicleData.getDoorValue() * 100)), IGui.ARGB_WHITE);
			graphicsHolder.pop();

			// Draw digital speed
			graphicsHolder.push();
			graphicsHolder.translate(0, TOOL_SIZE * 0.1F, 0);
			final int speedColor = vehicle.getSpeedLimitKilometersPerHour() > 0 && speedKilometersPerHour > vehicle.getSpeedLimitKilometersPerHour() ? ORANGE_COLOR : IGui.ARGB_WHITE;
			drawCenteredText(graphicsHolder, String.valueOf(Utilities.round(speedKilometersPerHour, 1)), speedColor);
			graphicsHolder.translate(0, SMALL_LINE_SPACING, 0);
			graphicsHolder.scale(0.5F, 0.5F, 1);
			drawCenteredText(graphicsHolder, "km/h", speedColor);
			graphicsHolder.pop();

			graphicsHolder.pop();

			// Draw platform stopping indicator
			final DoubleObjectImmutablePair<DoubleDoubleImmutablePair> platformStoppingDetails = vehicle.getPlatformStoppingDetails();
			if (platformStoppingDetails != null) {
				final double platformLength = platformStoppingDetails.right().leftDouble();
				final double vehicleLength = platformStoppingDetails.right().rightDouble();
				final int platformIndicatorX = window.getScaledWidth() - EDGE_PADDING - PLATFORM_BAR_SIZE;
				final int platformIndicatorY = window.getScaledHeight() - TOOL_SIZE * 2 - EDGE_PADDING - PADDING;
				final double targetY = vehicleLength / (platformLength + vehicleLength) * (TOOL_SIZE - 1);
				final double positionY = (vehicleLength + platformStoppingDetails.leftDouble()) / (platformLength + vehicleLength) * (TOOL_SIZE - 1);

				graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY, 0, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + TOOL_SIZE, 0, Direction.NORTH, BLUE_COLOR, 0);
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY + (float) targetY, 0, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + (float) targetY + 1, 0, Direction.NORTH, 0xFF001F4D, 0);
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY + (float) positionY, 0, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + (float) positionY + 1, 0, Direction.NORTH, 0xFFFF0000, 0);

				final String text = Utilities.round(platformStoppingDetails.leftDouble(), 1) + " m";
				final int textWidth = GraphicsHolder.getTextWidth(text);
				graphicsHolder.push();
				graphicsHolder.translate(platformIndicatorX - PADDING / 2F, platformIndicatorY + positionY + 0.5, 0);
				graphicsHolder.scale(0.5F, 0.5F, 1);
				graphicsHolder.drawText(text, -textWidth, -IGui.TEXT_HEIGHT / 2, IGui.ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
				graphicsHolder.pop();
			}
		}

		vehicle = null;
	}

	public static void setVehicle(VehicleExtension vehicle) {
		DrivingGuiRenderer.vehicle = vehicle;
	}

	private static void drawCenteredText(GraphicsHolder graphicsHolder, String text, int color) {
		graphicsHolder.drawText(text, -GraphicsHolder.getTextWidth(text) / 2, -IGui.TEXT_HEIGHT / 2, color, false, GraphicsHolder.getDefaultLight());
	}
}
