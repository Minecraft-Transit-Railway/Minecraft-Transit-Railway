package org.mtr.mod.render;

import org.mtr.core.tools.Utilities;
import org.mtr.core.tools.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.client.*;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	public static final float HALF_PLAYER_WIDTH = 0.3F;
	private static final int CHECK_DOOR_RADIUS = 2;
	private static final int RIDE_STEP_THRESHOLD = 8;

	public static void render(long millisElapsed) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		VehicleRidingMovement.tick();
		final boolean canRide = !clientPlayerEntity.isSpectator();

		ClientData.instance.vehicles.forEach(vehicle -> {
			final ObjectArrayList<PreviousConnectionPositions> previousGangwayPositionsList = new ObjectArrayList<>();
			final ObjectArrayList<PreviousConnectionPositions> previousBarrierPositionsList = new ObjectArrayList<>();
			final PreviousGangwayMovementPositions previousGangwayMovementPositions = new PreviousGangwayMovementPositions();

			vehicle.iterateVehicles((vehicleCar, carNumber, bogiePositionsList) -> CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleCar.getVehicleId(), vehicleResource -> {
				for (int bogieIndex = 0; bogieIndex < bogiePositionsList.size(); bogieIndex++) {
					final RenderVehicleTransformationHelper bogieRenderVehicleTransformationHelper = new RenderVehicleTransformationHelper(bogiePositionsList.get(bogieIndex));
					vehicleResource.iterateBogieModels(bogieIndex, model -> renderModel(bogieRenderVehicleTransformationHelper, storedMatrixTransformations -> model.render(storedMatrixTransformations, vehicle, bogieRenderVehicleTransformationHelper.light, null)));
				}

				final RenderVehicleTransformationHelper renderVehicleTransformationHelper = new RenderVehicleTransformationHelper(bogiePositionsList, vehicleCar.getBogie1Position(), vehicleCar.getBogie2Position(), vehicleCar.getLength());
				final Vector3d playerPosition = renderVehicleTransformationHelper.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
				final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
				final GangwayMovementPositions gangwayMovementPositions1 = new GangwayMovementPositions(renderVehicleTransformationHelper, false);
				final GangwayMovementPositions gangwayMovementPositions2 = new GangwayMovementPositions(renderVehicleTransformationHelper, true);

				renderModel(renderVehicleTransformationHelper, storedMatrixTransformations -> vehicleResource.iterateModels((modelIndex, model) -> {
					final ObjectArrayList<Box> openDoorways = vehicle.persistentVehicleData.getDoorValue() > 0 ? model.doorways.stream().filter(doorway -> canOpenDoors(doorway, renderVehicleTransformationHelper)).collect(Collectors.toCollection(ObjectArrayList::new)) : new ObjectArrayList<>();
					model.render(storedMatrixTransformations, vehicle, renderVehicleTransformationHelper.light, openDoorways);

					if (canRide) {
						model.floors.forEach(floor -> {
							floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
							renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderVehicleTransformationHelper);
							gangwayMovementPositions1.check(floor);
							gangwayMovementPositions2.check(floor);
						});

						openDoorways.forEach(doorway -> {
							floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
							renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderVehicleTransformationHelper);
						});

						VehicleRidingMovement.startRiding(openDoorways, vehicle.getId(), carNumber, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped());
					}

					if (modelIndex >= previousGangwayPositionsList.size()) {
						previousGangwayPositionsList.add(new PreviousConnectionPositions());
					}

					if (modelIndex >= previousBarrierPositionsList.size()) {
						previousBarrierPositionsList.add(new PreviousConnectionPositions());
					}

					renderConnection(
							vehicleResource.hasGangway1(),
							vehicleResource.hasGangway2(),
							true,
							previousGangwayPositionsList.get(modelIndex),
							model.modelProperties.gangwayInnerSideTexture,
							model.modelProperties.gangwayInnerTopTexture,
							model.modelProperties.gangwayInnerBottomTexture,
							model.modelProperties.gangwayOuterSideTexture,
							model.modelProperties.gangwayOuterTopTexture,
							model.modelProperties.gangwayOuterBottomTexture,
							renderVehicleTransformationHelper,
							vehicleCar.getLength(),
							model.modelProperties.getGangwayWidth(),
							model.modelProperties.getGangwayHeight(),
							model.modelProperties.getGangwayYOffset(),
							model.modelProperties.getGangwayZOffset(),
							vehicle.getIsOnRoute()
					);

					renderConnection(
							vehicleResource.hasBarrier1(),
							vehicleResource.hasBarrier2(),
							false,
							previousBarrierPositionsList.get(modelIndex),
							model.modelProperties.barrierInnerSideTexture,
							model.modelProperties.barrierInnerTopTexture,
							model.modelProperties.barrierInnerBottomTexture,
							model.modelProperties.barrierOuterSideTexture,
							model.modelProperties.barrierOuterTopTexture,
							model.modelProperties.barrierOuterBottomTexture,
							renderVehicleTransformationHelper,
							vehicleCar.getLength(),
							model.modelProperties.getBarrierWidth(),
							model.modelProperties.getBarrierHeight(),
							model.modelProperties.getBarrierYOffset(),
							model.modelProperties.getBarrierZOffset(),
							vehicle.getIsOnRoute()
					);
				}));

				if (vehicleResource.hasGangway1()) {
					final Box gangwayConnectionFloor1 = gangwayMovementPositions1.getBox();
					renderFloorOrDoorway(gangwayConnectionFloor1, 0xFF0000FF, playerPosition, renderVehicleTransformationHelper);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor1, true));
				}

				if (vehicleResource.hasGangway2()) {
					final Box gangwayConnectionFloor2 = gangwayMovementPositions2.getBox();
					renderFloorOrDoorway(gangwayConnectionFloor2, 0xFF0000FF, playerPosition, renderVehicleTransformationHelper);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor2, true));
				}

				if (canRide) {
					VehicleRidingMovement.movePlayer(
							millisElapsed, vehicle.getId(), carNumber,
							floorsAndDoorways,
							vehicleResource.hasGangway1() ? previousGangwayMovementPositions.gangwayMovementPositions : null,
							vehicleResource.hasGangway1() ? gangwayMovementPositions1 : null,
							vehicleResource.hasGangway2() ? gangwayMovementPositions2 : null,
							renderVehicleTransformationHelper
					);
				}

				previousGangwayMovementPositions.gangwayMovementPositions = gangwayMovementPositions2;
			}));
		});
	}

	public static boolean boxContains(Box box, double x, double y, double z) {
		return Utilities.isBetween(
				x,
				box.getMinXMapped(),
				box.getMaxXMapped()
		) && Utilities.isBetween(
				y,
				box.getMinYMapped(),
				box.getMaxYMapped(),
				RIDE_STEP_THRESHOLD
		) && Utilities.isBetween(
				z,
				box.getMinZMapped(),
				box.getMaxZMapped()
		);
	}

	private static boolean canOpenDoors(Box doorway, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return false;
		}

		final Vector3d doorwayPosition = renderVehicleTransformationHelper.transformForwards(doorway.getCenter(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);

		for (double checkX = doorwayPosition.getXMapped() - CHECK_DOOR_RADIUS; checkX <= doorwayPosition.getXMapped() + CHECK_DOOR_RADIUS; checkX++) {
			for (double checkY = doorwayPosition.getYMapped() - CHECK_DOOR_RADIUS; checkY <= doorwayPosition.getYMapped() + CHECK_DOOR_RADIUS; checkY++) {
				for (double checkZ = doorwayPosition.getZMapped() - CHECK_DOOR_RADIUS; checkZ <= doorwayPosition.getZMapped() + CHECK_DOOR_RADIUS; checkZ++) {
					final BlockPos checkPos = Init.newBlockPos(checkX, checkY, checkZ);
					if (clientWorld.getBlockState(checkPos).getBlock().data instanceof BlockPlatform) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static void renderModel(RenderVehicleTransformationHelper renderVehicleTransformationHelper, Consumer<StoredMatrixTransformations> render) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(graphicsHolder -> renderVehicleTransformationHelper.transformBackwards(new Object(), (object, pitch) -> {
			graphicsHolder.rotateXRadians((float) (Math.PI + pitch)); // Blockbench exports models upside down
			return new Object();
		}, (object, yaw) -> {
			graphicsHolder.rotateYRadians(-yaw);
			return new Object();
		}, (object, x, y, z) -> {
			graphicsHolder.translate(-x, -y, -z);
			return new Object();
		}));
		render.accept(storedMatrixTransformations);
	}

	private static void renderConnection(
			boolean shouldRender1, boolean shouldRender2, boolean canHaveLight, PreviousConnectionPositions previousConnectionPositions,
			@Nullable Identifier innerSideTexture,
			@Nullable Identifier innerTopTexture,
			@Nullable Identifier innerBottomTexture,
			@Nullable Identifier outerSideTexture,
			@Nullable Identifier outerTopTexture,
			@Nullable Identifier outerBottomTexture,
			RenderVehicleTransformationHelper renderVehicleTransformationHelper,
			double vehicleLength, double width, double height, double yOffset, double zOffset, boolean isOnRoute
	) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final double halfLength = vehicleLength / 2;

		if (shouldRender1 && previousConnectionPositions.isValid()) {
			final Vector position1 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position2 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position3 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position4 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);

			final Vector position5 = previousConnectionPositions.position1;
			final Vector position6 = previousConnectionPositions.position2;
			final Vector position7 = previousConnectionPositions.position3;
			final Vector position8 = previousConnectionPositions.position4;

			final BlockPos blockPosConnection = Init.newBlockPos(position1.x, position1.y + 1, position1.z);
			final int lightConnection = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPosConnection), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPosConnection));

			RenderTrains.scheduleRender(outerSideTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Sides
				IDrawing.drawTexture(
						graphicsHolder,
						position2.x, position2.y, position2.z,
						position7.x, position7.y, position7.z,
						position8.x, position8.y, position8.z,
						position1.x, position1.y, position1.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
				);
				IDrawing.drawTexture(
						graphicsHolder,
						position6.x, position6.y, position6.z,
						position3.x, position3.y, position3.z,
						position4.x, position4.y, position4.z,
						position5.x, position5.y, position5.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
				);
			});

			RenderTrains.scheduleRender(outerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Top
				IDrawing.drawTexture(
						graphicsHolder,
						position3.x, position3.y, position3.z,
						position6.x, position6.y, position6.z,
						position7.x, position7.y, position7.z,
						position2.x, position2.y, position2.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
				);
			});

			RenderTrains.scheduleRender(outerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Bottom
				IDrawing.drawTexture(
						graphicsHolder,
						position1.x, position1.y, position1.z,
						position8.x, position8.y, position8.z,
						position5.x, position5.y, position5.z,
						position4.x, position4.y, position4.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, lightConnection
				);
			});

			RenderTrains.scheduleRender(innerSideTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Sides
				IDrawing.drawTexture(
						graphicsHolder,
						position7.x, position7.y, position7.z,
						position2.x, position2.y, position2.z,
						position1.x, position1.y, position1.z,
						position8.x, position8.y, position8.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
				);
				IDrawing.drawTexture(
						graphicsHolder,
						position3.x, position3.y, position3.z,
						position6.x, position6.y, position6.z,
						position5.x, position5.y, position5.z,
						position4.x, position4.y, position4.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
				);
			});

			RenderTrains.scheduleRender(innerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Top
				IDrawing.drawTexture(
						graphicsHolder,
						position6.x, position6.y, position6.z,
						position3.x, position3.y, position3.z,
						position2.x, position2.y, position2.z,
						position7.x, position7.y, position7.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
				);
			});

			RenderTrains.scheduleRender(innerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Bottom
				IDrawing.drawTexture(
						graphicsHolder,
						position8.x, position8.y, position8.z,
						position1.x, position1.y, position1.z,
						position4.x, position4.y, position4.z,
						position5.x, position5.y, position5.z,
						offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection
				);
			});
		}

		if (shouldRender2) {
			previousConnectionPositions.position1 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position2 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position3 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position4 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
		} else {
			previousConnectionPositions.position1 = null;
			previousConnectionPositions.position2 = null;
			previousConnectionPositions.position3 = null;
			previousConnectionPositions.position4 = null;
		}
	}

	private static void renderFloorOrDoorway(Box floorOrDoorway, int color, Vector3d playerPosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Vector3d corner1 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMinXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner2 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMaxXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner3 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMaxXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner4 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMinXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final int newColor = boxContains(floorOrDoorway,
					playerPosition.getXMapped() - HALF_PLAYER_WIDTH,
					playerPosition.getYMapped(),
					playerPosition.getZMapped() - HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.getXMapped() + HALF_PLAYER_WIDTH,
					playerPosition.getYMapped(),
					playerPosition.getZMapped() - HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.getXMapped() + HALF_PLAYER_WIDTH,
					playerPosition.getYMapped(),
					playerPosition.getZMapped() + HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.getXMapped() - HALF_PLAYER_WIDTH,
					playerPosition.getYMapped(),
					playerPosition.getZMapped() + HALF_PLAYER_WIDTH
			) ? 0xFF00FF00 : color;
			RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> {
				graphicsHolder.drawLineInWorld(
						(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
						(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
						newColor
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
						(float) (corner3.getXMapped() - offset.getXMapped()), (float) (corner3.getYMapped() - offset.getYMapped()), (float) (corner3.getZMapped() - offset.getZMapped()),
						newColor
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner3.getXMapped() - offset.getXMapped()), (float) (corner3.getYMapped() - offset.getYMapped()), (float) (corner3.getZMapped() - offset.getZMapped()),
						(float) (corner4.getXMapped() - offset.getXMapped()), (float) (corner4.getYMapped() - offset.getYMapped()), (float) (corner4.getZMapped() - offset.getZMapped()),
						newColor
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner4.getXMapped() - offset.getXMapped()), (float) (corner4.getYMapped() - offset.getYMapped()), (float) (corner4.getZMapped() - offset.getZMapped()),
						(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
						newColor
				);
			});
		}
	}

	private static class PreviousConnectionPositions {

		private Vector position1;
		private Vector position2;
		private Vector position3;
		private Vector position4;

		private boolean isValid() {
			return position1 != null && position2 != null && position3 != null && position4 != null;
		}
	}

	private static class PreviousGangwayMovementPositions {

		private GangwayMovementPositions gangwayMovementPositions;
	}
}
