package org.mtr.mod.render;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tools.Utilities;
import org.mtr.core.tools.Vector;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.TrainClientRegistry;
import org.mtr.mod.client.TrainProperties;
import org.mtr.mod.data.IGui;
import org.mtr.mod.model.ModelBogie;
import org.mtr.mod.model.ModelTrainBase;

import java.util.function.Consumer;

public class RenderVehicles implements IGui {

	private static final ModelBogie MODEL_BOGIE = new ModelBogie();
	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public static void render(GraphicsHolder graphicsHolder) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final Vector3d playerOffset = RenderTrains.getPlayerOffset();

		ClientData.instance.vehicles.forEach(vehicle -> {
			final ObjectImmutableList<VehicleCar> vehicleCars = vehicle.vehicleExtraData.getVehicleCars(vehicle.getReversed());
			final ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>> positions = vehicle.getPositions();
			final int totalCars = Math.min(vehicleCars.size(), positions.size());
			final PreviousConnectionPositions previousGangwayConnectionPositions = new PreviousConnectionPositions();
			final PreviousConnectionPositions previousBarrierConnectionPositions = new PreviousConnectionPositions();

			for (int i = 0; i < totalCars; i++) {
				final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositions = positions.get(i);
				final Vector pivotPosition;
				final DoubleDoubleImmutablePair angles;

				if (bogiePositions.size() == 1) {
					final ObjectObjectImmutablePair<Vector, Vector> bogiePosition = bogiePositions.get(0);
					pivotPosition = Vector.getAverage(bogiePosition.left(), bogiePosition.right());
					angles = getAngles(bogiePosition.left(), bogiePosition.right());
				} else if (bogiePositions.size() == 2) {
					final Vector average1 = Vector.getAverage(bogiePositions.get(0).left(), bogiePositions.get(0).right());
					final Vector average2 = Vector.getAverage(bogiePositions.get(1).left(), bogiePositions.get(1).right());
					pivotPosition = Vector.getAverage(average1, average2);
					angles = getAngles(average1, average2);
				} else {
					pivotPosition = new Vector(0, 0, 0);
					angles = new DoubleDoubleImmutablePair(0, 0);
				}

				final BlockPos blockPos = Init.newBlockPos(pivotPosition.x, pivotPosition.y + 1, pivotPosition.z);
				final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));

				bogiePositions.forEach(bogiePosition -> renderModel(bogiePosition.left(), bogiePosition.right(), vehicle.getReversed(), storedMatrixTransformations -> MODEL_BOGIE.render(storedMatrixTransformations, light), playerOffset));

				final VehicleCar vehicleCar = vehicleCars.get(i);
				final TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(vehicleCar.getVehicleId());
				final LegacyVehicleRenderer legacyVehicleRenderer = trainProperties.legacyVehicleRenderer;
				final ModelTrainBase model = legacyVehicleRenderer.model;

				if (model != null) {
					final int carNumber = vehicle.getReversed() ? totalCars - i - 1 : i;
					final double pivotOffset = Utilities.getAverage(vehicleCar.getBogie1Position(), vehicleCar.getBogie2Position());
					final double halfLength = vehicleCar.getLength() / 2;
					final Vector rotationalVector = new Vector(0, 0, 1).rotateX(angles.rightDouble()).rotateY(angles.leftDouble());
					final double pivotOffset1 = pivotOffset - halfLength;
					final double pivotOffset2 = pivotOffset + halfLength;
					final Vector position1 = rotationalVector.multiply(pivotOffset1, pivotOffset1, pivotOffset1).add(pivotPosition);
					final Vector position2 = rotationalVector.multiply(pivotOffset2, pivotOffset2, pivotOffset2).add(pivotPosition);

					renderModel(
							position1,
							position2,
							vehicle.getReversed(),
							storedMatrixTransformations -> model.render(graphicsHolder, storedMatrixTransformations, vehicle, new Identifier(legacyVehicleRenderer.textureId + ".png"), light, 0, 0, false, carNumber, totalCars, !vehicle.getReversed(), vehicle.getIsOnRoute(), false, true, false),
							playerOffset
					);

					renderConnection(
							!legacyVehicleRenderer.gangwayConnectionId.isEmpty(),
							true,
							previousGangwayConnectionPositions,
							legacyVehicleRenderer.gangwayConnectionId + "_connector_",
							position1, position2, angles,
							vehicleCar.getWidth() / 2 - CONNECTION_X_OFFSET, CONNECTION_Z_OFFSET,
							vehicle.getIsOnRoute()
					);
					renderConnection(
							!legacyVehicleRenderer.trainBarrierId.isEmpty(),
							false,
							previousBarrierConnectionPositions,
							legacyVehicleRenderer.trainBarrierId + "_barrier_",
							position1, position2, angles,
							vehicleCar.getWidth() / 2 + CONNECTION_X_OFFSET / 2, CONNECTION_Z_OFFSET * 2,
							vehicle.getIsOnRoute()
					);
				}
			}
		});
	}

	private static void renderModel(Vector position1, Vector position2, boolean reversed, Consumer<StoredMatrixTransformations> render, Vector3d playerOffset) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final DoubleDoubleImmutablePair angles = getAngles(position1, position2);
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(graphicsHolder -> {
			graphicsHolder.translate(
					Utilities.getAverage(position1.x, position2.x) - playerOffset.getXMapped(),
					Utilities.getAverage(position1.y, position2.y) - playerOffset.getYMapped(),
					Utilities.getAverage(position1.z, position2.z) - playerOffset.getZMapped()
			);
			graphicsHolder.rotateYRadians((float) (angles.leftDouble() + (reversed ? 0 : Math.PI)));
			graphicsHolder.rotateXRadians((float) ((reversed ? -1 : 1) * angles.rightDouble() + Math.PI));
			graphicsHolder.translate(0, -1, 0);
		});
		render.accept(storedMatrixTransformations);
	}

	private static void renderConnection(boolean shouldRender, boolean renderInterior, PreviousConnectionPositions previousConnectionPositions, String textureId, Vector endPosition1, Vector endPosition2, DoubleDoubleImmutablePair angles, double offsetX, double offsetZ, boolean isOnRoute) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		if (shouldRender) {
			if (previousConnectionPositions.isValid()) {
				final Vector position1 = new Vector(-offsetX, 1 + SMALL_OFFSET, offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition1);
				final Vector position2 = new Vector(-offsetX, CONNECTION_HEIGHT + 1 + SMALL_OFFSET, offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition1);
				final Vector position3 = new Vector(offsetX, CONNECTION_HEIGHT + 1 + SMALL_OFFSET, offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition1);
				final Vector position4 = new Vector(offsetX, 1 + SMALL_OFFSET, offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition1);

				final Vector position5 = previousConnectionPositions.position1;
				final Vector position6 = previousConnectionPositions.position2;
				final Vector position7 = previousConnectionPositions.position3;
				final Vector position8 = previousConnectionPositions.position4;

				final BlockPos blockPosConnection = Init.newBlockPos(position1.x, position1.y + 1, position1.z);
				final int lightConnection = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPosConnection), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPosConnection));

				RenderTrains.scheduleRender(new Identifier(textureId + "exterior.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, graphicsHolderNew -> {
					final Vector3d playerOffset = RenderTrains.getPlayerOffset();
					// Sides
					IDrawing.drawTexture(
							graphicsHolderNew,
							position2.x, position2.y, position2.z,
							position7.x, position7.y, position7.z,
							position8.x, position8.y, position8.z,
							position1.x, position1.y, position1.z,
							playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
					);
					IDrawing.drawTexture(
							graphicsHolderNew,
							position6.x, position6.y, position6.z,
							position3.x, position3.y, position3.z,
							position4.x, position4.y, position4.z,
							position5.x, position5.y, position5.z,
							playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
					);

					if (renderInterior) {
						// Top
						IDrawing.drawTexture(
								graphicsHolderNew,
								position3.x, position3.y, position3.z,
								position6.x, position6.y, position6.z,
								position7.x, position7.y, position7.z,
								position2.x, position2.y, position2.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
						);
						// Bottom
						IDrawing.drawTexture(
								graphicsHolderNew,
								position1.x, position1.y, position1.z,
								position8.x, position8.y, position8.z,
								position5.x, position5.y, position5.z,
								position4.x, position4.y, position4.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
						);
					} else {
						IDrawing.drawTexture(
								graphicsHolderNew,
								position7.x, position7.y, position7.z,
								position2.x, position2.y, position2.z,
								position1.x, position1.y, position1.z,
								position8.x, position8.y, position8.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
						);
						IDrawing.drawTexture(
								graphicsHolderNew,
								position3.x, position3.y, position3.z,
								position6.x, position6.y, position6.z,
								position5.x, position5.y, position5.z,
								position4.x, position4.y, position4.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
						);
					}
				});

				if (renderInterior) {
					RenderTrains.scheduleRender(new Identifier(textureId + "side.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, graphicsHolderNew -> {
						final Vector3d playerOffset = RenderTrains.getPlayerOffset();
						// Sides
						IDrawing.drawTexture(
								graphicsHolderNew,
								position7.x, position7.y, position7.z,
								position2.x, position2.y, position2.z,
								position1.x, position1.y, position1.z,
								position8.x, position8.y, position8.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
						);
						IDrawing.drawTexture(
								graphicsHolderNew,
								position3.x, position3.y, position3.z,
								position6.x, position6.y, position6.z,
								position5.x, position5.y, position5.z,
								position4.x, position4.y, position4.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
						);
					});

					RenderTrains.scheduleRender(new Identifier(textureId + "roof.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, graphicsHolderNew -> {
						final Vector3d playerOffset = RenderTrains.getPlayerOffset();
						// Top
						IDrawing.drawTexture(
								graphicsHolderNew,
								position6.x, position6.y, position6.z,
								position3.x, position3.y, position3.z,
								position2.x, position2.y, position2.z,
								position7.x, position7.y, position7.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
						);
					});

					RenderTrains.scheduleRender(new Identifier(textureId + "floor.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, graphicsHolderNew -> {
						final Vector3d playerOffset = RenderTrains.getPlayerOffset();
						// Bottom
						IDrawing.drawTexture(
								graphicsHolderNew,
								position8.x, position8.y, position8.z,
								position1.x, position1.y, position1.z,
								position4.x, position4.y, position4.z,
								position5.x, position5.y, position5.z,
								playerOffset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
						);
					});
				}
			}

			previousConnectionPositions.position1 = new Vector(offsetX, 1 + SMALL_OFFSET, -offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition2);
			previousConnectionPositions.position2 = new Vector(offsetX, CONNECTION_HEIGHT + 1 + SMALL_OFFSET, -offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition2);
			previousConnectionPositions.position3 = new Vector(-offsetX, CONNECTION_HEIGHT + 1 + SMALL_OFFSET, -offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition2);
			previousConnectionPositions.position4 = new Vector(-offsetX, 1 + SMALL_OFFSET, -offsetZ).rotateX(angles.rightDouble()).rotateY(angles.leftDouble()).add(endPosition2);
		} else {
			previousConnectionPositions.position1 = null;
			previousConnectionPositions.position2 = null;
			previousConnectionPositions.position3 = null;
			previousConnectionPositions.position4 = null;
		}
	}

	private static DoubleDoubleImmutablePair getAngles(Vector position1, Vector position2) {
		final double x1 = position1.x;
		final double y1 = position1.y;
		final double z1 = position1.z;
		final double x2 = position2.x;
		final double y2 = position2.y;
		final double z2 = position2.z;
		return new DoubleDoubleImmutablePair(Math.atan2(x2 - x1, z2 - z1), Math.atan2(y2 - y1, Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1))));
	}

	private static class PreviousConnectionPositions {

		private Vector position1 = null;
		private Vector position2 = null;
		private Vector position3 = null;
		private Vector position4 = null;

		private boolean isValid() {
			return position1 != null && position2 != null && position3 != null && position4 != null;
		}
	}
}
