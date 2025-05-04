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
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nullable;

public final class DrivingGuiRenderer {

	@Nullable
	private static VehicleExtension vehicle;

	private static final int PADDING = 4;
	private static final int TOOL_SIZE = 96;
	private static final int SPEEDOMETER_SPAN = 300;
	private static final int SPEEDOMETER_START_ANGLE = -60;
	private static final int SPEEDOMETER_TICK_INTERVAL = 5;

	public static void render(GraphicsHolder graphicsHolder) {
		if (vehicle != null) {
			final VehicleExtraData vehicleExtraData = vehicle.vehicleExtraData;

			// Render P7-B7
			int renderY = PADDING;
			for (int i = Vehicle.MANUAL_NOTCH_BOUND; i >= -Vehicle.MANUAL_NOTCH_BOUND; i--) {
				drawStatusText(graphicsHolder, i < 0 ? "B" + Math.abs(i) : i > 0 ? "P" + i : "N", i == 0 ? -1 : Math.abs(i) * 100 / Vehicle.MANUAL_NOTCH_RATIO, renderY, vehicleExtraData.getManualNotch() == i);
				renderY += IGui.TEXT_HEIGHT + 1;
			}

			// Render door status
			final int doorPercentage = (int) Math.round(vehicle.persistentVehicleData.getDoorValue() * 100);
			renderY += IGui.TEXT_HEIGHT;
			drawStatusText(graphicsHolder, "DC", doorPercentage, renderY, vehicleExtraData.getDoorMultiplier() < 0);
			renderY += IGui.TEXT_HEIGHT + 1;
			drawStatusText(graphicsHolder, "DO", doorPercentage, renderY, vehicleExtraData.getDoorMultiplier() > 0);

			// Render speedometer background
			final Window window = MinecraftClient.getInstance().getWindow();
			final int speedometerX = window.getScaledWidth() - TOOL_SIZE - PADDING;
			final int speedometerY = window.getScaledHeight() - TOOL_SIZE - PADDING;
			graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(new Identifier(Init.MOD_ID, "textures/block/sign/circle.png")));
			IDrawing.drawTexture(graphicsHolder, speedometerX, speedometerY, TOOL_SIZE, TOOL_SIZE, Direction.NORTH, 0);

			final int radius = TOOL_SIZE / 2;
			final float tickX = -radius * 0.95F;
			final int maxManualSpeedKilometersPerHour = (int) Math.round(vehicleExtraData.getMaxManualSpeed() * 3600);
			graphicsHolder.push();
			graphicsHolder.translate(speedometerX + radius, speedometerY + radius, 0);
			graphicsHolder.rotateZDegrees(SPEEDOMETER_START_ANGLE);
			graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));

			// Draw speedometer ticks
			graphicsHolder.push();
			for (int i = 0; i <= maxManualSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL) {
				IDrawing.drawTexture(
						graphicsHolder,
						tickX, -0.5F, 0,
						tickX + radius * (i % 20 == 0 ? 0.2F : i % 10 == 0 ? 0.1F : 0.05F), 0.5F, 0,
						Direction.NORTH,
						vehicle.getSpeedLimitKilometersPerHour() > 0 && i == Math.min(vehicle.getSpeedLimitKilometersPerHour(), maxManualSpeedKilometersPerHour) ? 0xFF00FF00 : IGui.ARGB_WHITE,
						GraphicsHolder.getDefaultLight()
				);
				graphicsHolder.rotateZDegrees((float) SPEEDOMETER_TICK_INTERVAL * SPEEDOMETER_SPAN / maxManualSpeedKilometersPerHour);
			}
			graphicsHolder.pop();

			// Draw speedometer needle
			graphicsHolder.push();
			final double speedKilometersPerHour = vehicle.getAdjustedSpeed() * 3600;
			graphicsHolder.rotateZDegrees((float) speedKilometersPerHour * SPEEDOMETER_SPAN / maxManualSpeedKilometersPerHour);
			IDrawing.drawTexture(
					graphicsHolder,
					-radius * 0.9F, -0.5F, 1,
					0, 0.5F, 1,
					Direction.NORTH,
					0xFFFF0000,
					GraphicsHolder.getDefaultLight()
			);
			graphicsHolder.pop();

			// Draw speedometer labels
			graphicsHolder.push();
			for (int i = 0; i <= maxManualSpeedKilometersPerHour; i += SPEEDOMETER_TICK_INTERVAL * 4) {
				graphicsHolder.push();
				graphicsHolder.translate(-radius * 0.75F + IGui.LINE_HEIGHT / 2F, 0, 0);
				graphicsHolder.rotateZDegrees(60 - (float) i * SPEEDOMETER_SPAN / maxManualSpeedKilometersPerHour);
				graphicsHolder.scale(0.5F, 0.5F, 1);
				drawCenteredText(graphicsHolder, String.valueOf(i));
				graphicsHolder.pop();
				graphicsHolder.rotateZDegrees((float) SPEEDOMETER_TICK_INTERVAL * 4 * SPEEDOMETER_SPAN / maxManualSpeedKilometersPerHour);
			}
			graphicsHolder.pop();

			graphicsHolder.pop();

			// Draw digital speed
			graphicsHolder.push();
			graphicsHolder.translate(speedometerX + radius, speedometerY + TOOL_SIZE * 0.65F - IGui.LINE_HEIGHT / 2F, 0);
			drawCenteredText(graphicsHolder, String.valueOf(Utilities.round(speedKilometersPerHour, 1)));
			graphicsHolder.translate(0, IGui.LINE_HEIGHT, 0);
			drawCenteredText(graphicsHolder, "km/h");
			graphicsHolder.pop();

			// Draw platform stopping indicator
			final DoubleObjectImmutablePair<DoubleDoubleImmutablePair> platformStoppingDetails = vehicle.getPlatformStoppingDetails();
			if (platformStoppingDetails != null) {
				final double platformLength = platformStoppingDetails.right().leftDouble();
				final double vehicleLength = platformStoppingDetails.right().rightDouble();
				final int platformIndicatorX = window.getScaledWidth() - PADDING * 2;
				final int platformIndicatorY = window.getScaledHeight() - TOOL_SIZE * 2 - PADDING * 2;
				final double targetY = vehicleLength / (platformLength + vehicleLength) * (TOOL_SIZE - 1);
				final double positionY = (vehicleLength + platformStoppingDetails.leftDouble()) / (platformLength + vehicleLength) * (TOOL_SIZE - 1);

				graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY, 0, platformIndicatorX + PADDING, platformIndicatorY + TOOL_SIZE, 0, Direction.NORTH, 0xFFAACCFF, 0);
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY + (float) targetY, 0, platformIndicatorX + PADDING, platformIndicatorY + (float) targetY + 1, 0, Direction.NORTH, 0xFF001F4D, 0);
				IDrawing.drawTexture(graphicsHolder, platformIndicatorX, platformIndicatorY + (float) positionY, 0, platformIndicatorX + PADDING, platformIndicatorY + (float) positionY + 1, 0, Direction.NORTH, 0xFFFF0000, 0);

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

	private static void drawStatusText(GraphicsHolder graphicsHolder, String text, int percentage, int textY, boolean isSelected) {
		graphicsHolder.drawText(text + (isSelected && percentage >= 0 ? String.format(" (%s%%)", percentage) : ""), PADDING, textY, isSelected ? IGui.ARGB_WHITE : IGui.ARGB_GRAY, true, GraphicsHolder.getDefaultLight());
	}

	private static void drawCenteredText(GraphicsHolder graphicsHolder, String text) {
		graphicsHolder.drawText(text, -GraphicsHolder.getTextWidth(text) / 2, -IGui.TEXT_HEIGHT / 2, IGui.ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
	}
}
