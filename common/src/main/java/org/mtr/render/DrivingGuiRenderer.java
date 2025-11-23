package org.mtr.render;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.doubles.DoubleObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.mtr.client.VehicleRidingMovement;
import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.data.RailType;
import org.mtr.data.VehicleExtension;
import org.mtr.tool.Drawing;

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

	public static void render(DrawContext context) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (vehicle != null && (minecraftClient.currentScreen == null || minecraftClient.currentScreen.getTitle().getString().contains("chat_screen.title")) && VehicleRidingMovement.getValidHoldingKey(vehicle.vehicleExtraData.getDepotId()) != null) {
			final VehicleExtraData vehicleExtraData = vehicle.vehicleExtraData;
			final Window window = minecraftClient.getWindow();
			final int speedometerX = window.getScaledWidth() - TOOL_SIZE - EDGE_PADDING;
			final int speedometerY = window.getScaledHeight() - TOOL_SIZE - EDGE_PADDING;
			final int radius = TOOL_SIZE / 2;

			final MatrixStack matrixStack = context.getMatrices();
			matrixStack.push();
			matrixStack.translate(speedometerX + radius, speedometerY + radius, 0);
			final Drawing drawing = new Drawing(matrixStack, RenderLayer.getGui());

			// Render speedometer background
			matrixStack.push();
			for (int i = 0; i < 180; i += SPEEDOMETER_CIRCLE_INTERVAL) {
				drawing.setVertices(
						-radius, -(float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2,
						radius, (float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2
				).setColor(0xFF666666).draw();
				drawing.setVertices(
						-radius + 1, -(float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2,
						radius - 1, (float) SPEEDOMETER_CIRCLE_EDGE_LENGTH / 2
				).setColor(0xFF111111).draw();
				Drawing.rotateZDegrees(matrixStack, SPEEDOMETER_CIRCLE_INTERVAL);
			}
			matrixStack.pop();

			// Draw ATS background
			matrixStack.push();
			matrixStack.translate(0, radius - 2 - ATS_RADIUS_1, 0);
			Drawing.rotateZDegrees(matrixStack, ATS_INTERVAL * 2.5F);
			for (float i = 0; i < ATS_SLICES; i++) {
				drawing.setVertices(
						-ATS_RADIUS_1, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_1,
						ATS_RADIUS_1, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_1
				).setColor(0xFF1A1A1A).draw();
				Drawing.rotateZDegrees(matrixStack, ATS_INTERVAL);
			}

			// Draw ATS petals
			for (float i = 0; i < ATS_SLICES; i++) {
				if (i % 8 < 4) {
					if (vehicle.isVehiclePastSafeStoppingDistance()) {
						drawing.setVertices(
								-ATS_RADIUS_2, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_2, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_3, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_3, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0
						).setColor(ORANGE_COLOR).draw();
						drawing.setVertices(
								-ATS_RADIUS_3, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_3, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_3, 0,
								-ATS_RADIUS_4, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								-ATS_RADIUS_4, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0
						).setColor(IGui.ARGB_WHITE).draw();
					} else {
						drawing.setVertices(
								-ATS_RADIUS_2, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_2, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_2, 0,
								-ATS_RADIUS_4, (float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0,
								-ATS_RADIUS_4, -(float) ATS_CIRCLE_EDGE_HALF_LENGTH_4, 0
						).setColor(0xFF222222).draw();
					}
				}
				Drawing.rotateZDegrees(matrixStack, ATS_INTERVAL);
			}
			matrixStack.pop();

			// Draw speedometer ticks
			matrixStack.push();
			final int maxSpeedKilometersPerHour = vehicleExtraData.getIsManualAllowed() ? (int) Math.round(vehicleExtraData.getMaxManualSpeed() * 3600) : RailType.DIAMOND.speedLimit;
			Drawing.rotateZDegrees(matrixStack, SPEEDOMETER_START_ANGLE);
			for (int i = 0; i <= maxSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL) {
				drawing.setVertices(
						-radius + 2, -0.5F,
						-radius + (i % 20 == 0 ? 10 : i % 10 == 0 ? 8 : 4), 0.5F
				).setColor(vehicle.getSpeedLimitKilometersPerHour() > 0 && i == Math.min(vehicle.getSpeedLimitKilometersPerHour(), maxSpeedKilometersPerHour) ? 0xFF00FF00 : IGui.ARGB_WHITE);
				Drawing.rotateZDegrees(matrixStack, (float) SPEEDOMETER_TICK_INTERVAL * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			}
			matrixStack.pop();

			// Draw speedometer labels
			matrixStack.push();
			Drawing.rotateZDegrees(matrixStack, SPEEDOMETER_START_ANGLE);
			for (int i = 0; i <= maxSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL * 4) {
				matrixStack.push();
				matrixStack.translate(-radius + 10 + IGui.LINE_HEIGHT / 2F, 0, 0);
				Drawing.rotateZDegrees(matrixStack, -SPEEDOMETER_START_ANGLE - (float) i * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
				matrixStack.scale(0.5F, 0.5F, 1);
				drawCenteredText(context, String.valueOf(i), IGui.ARGB_WHITE);
				matrixStack.pop();
				Drawing.rotateZDegrees(matrixStack, (float) SPEEDOMETER_TICK_INTERVAL * 4 * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			}
			matrixStack.pop();

			// Draw power level
			matrixStack.push();
			matrixStack.translate(-radius * 0.3F, -TOOL_SIZE * 0.05F - SMALL_LINE_SPACING, 0);
			final int notch = vehicleExtraData.getPowerLevel();
			final int notchColor = notch < -Vehicle.MAX_POWER_LEVEL ? 0xFFFF0000 : notch < 0 ? ORANGE_COLOR : notch > 0 ? BLUE_COLOR : IGui.ARGB_WHITE;
			drawCenteredText(context, notch < -Vehicle.MAX_POWER_LEVEL ? "E" : notch < 0 ? "B" + -notch : notch > 0 ? "P" + notch : "N", notchColor);
			if (notch != 0 && notch >= -Vehicle.MAX_POWER_LEVEL) {
				matrixStack.translate(0, SMALL_LINE_SPACING, 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				drawCenteredText(context, String.format("(%s%%)", Math.abs(notch) * 100 / Vehicle.POWER_LEVEL_RATIO), notchColor);
			}
			matrixStack.pop();

			// Draw ATO status
			matrixStack.push();
			matrixStack.translate(0, -TOOL_SIZE * 0.15F - SMALL_LINE_SPACING, 0);
			drawCenteredText(context, "ATO", vehicleExtraData.getIsCurrentlyManual() ? 0xFF222222 : 0xFF00FF00);
			matrixStack.pop();

			// Draw door status
			matrixStack.push();
			matrixStack.translate(radius * 0.3F, -TOOL_SIZE * 0.05F - SMALL_LINE_SPACING, 0);
			drawCenteredText(context, vehicleExtraData.getDoorMultiplier() > 0 ? "DO" : "DC", IGui.ARGB_WHITE);
			matrixStack.translate(0, SMALL_LINE_SPACING, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			drawCenteredText(context, String.format("(%s%%)", (int) Math.round(vehicle.persistentVehicleData.getDoorValue() * 100)), IGui.ARGB_WHITE);
			matrixStack.pop();

			// Draw digital speed
			matrixStack.push();
			matrixStack.translate(0, TOOL_SIZE * 0.1F, 0);
			final double speedKilometersPerHour = vehicle.getSpeed() * 3600;
			final int speedColor = vehicle.getSpeedLimitKilometersPerHour() > 0 && speedKilometersPerHour > vehicle.getSpeedLimitKilometersPerHour() ? ORANGE_COLOR : IGui.ARGB_WHITE;
			drawCenteredText(context, String.valueOf(Utilities.round(speedKilometersPerHour, 1)), speedColor);
			matrixStack.translate(0, SMALL_LINE_SPACING, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			drawCenteredText(context, "km/h", speedColor);
			matrixStack.pop();

			// Draw speedometer needle
			matrixStack.push();
			Drawing.rotateZDegrees(matrixStack, SPEEDOMETER_START_ANGLE + (float) speedKilometersPerHour * SPEEDOMETER_SPAN / maxSpeedKilometersPerHour);
			drawing.setVertices(
					-radius + 4, -0.5F,
					0, 0.5F
			).setColor(0xFFFF0000);
			matrixStack.pop();

			matrixStack.pop();

			// Draw platform stopping indicator
			final DoubleObjectImmutablePair<DoubleDoubleImmutablePair> platformStoppingDetails = vehicle.getPlatformStoppingDetails();
			if (platformStoppingDetails != null) {
				final double platformLength = platformStoppingDetails.right().leftDouble();
				final double vehicleLength = platformStoppingDetails.right().rightDouble();
				final int platformIndicatorX = window.getScaledWidth() - EDGE_PADDING - PLATFORM_BAR_SIZE;
				final int platformIndicatorY = window.getScaledHeight() - TOOL_SIZE * 2 - EDGE_PADDING - PADDING;
				final double targetY = vehicleLength / (platformLength + vehicleLength) * (TOOL_SIZE - 1);
				final double positionY = (vehicleLength + platformStoppingDetails.leftDouble()) / (platformLength + vehicleLength) * (TOOL_SIZE - 1);
				final TextRenderer textRenderer = minecraftClient.textRenderer;

				drawing.setVertices(platformIndicatorX, platformIndicatorY, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + TOOL_SIZE).setColor(BLUE_COLOR);
				drawing.setVertices(platformIndicatorX, platformIndicatorY + (float) targetY, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + (float) targetY + 1).setColor(0xFF001F4D);
				drawing.setVertices(platformIndicatorX, platformIndicatorY + (float) positionY, platformIndicatorX + PLATFORM_BAR_SIZE, platformIndicatorY + (float) positionY + 1).setColor(0xFFFF0000);

				final String text = Utilities.round(platformStoppingDetails.leftDouble(), 1) + " m";
				final int textWidth = textRenderer.getWidth(text);
				matrixStack.push();
				matrixStack.translate(platformIndicatorX - PADDING / 2F, platformIndicatorY + positionY + 0.5, 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				context.drawText(textRenderer, text, -textWidth, -IGui.TEXT_HEIGHT / 2, IGui.ARGB_WHITE, true);
				matrixStack.pop();
			}
		}

		vehicle = null;
	}

	public static void setVehicle(VehicleExtension vehicle) {
		DrivingGuiRenderer.vehicle = vehicle;
	}

	private static void drawCenteredText(DrawContext context, String text, int color) {
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, text, -textRenderer.getWidth(text) / 2, -IGui.TEXT_HEIGHT / 2, color, false);
	}
}
