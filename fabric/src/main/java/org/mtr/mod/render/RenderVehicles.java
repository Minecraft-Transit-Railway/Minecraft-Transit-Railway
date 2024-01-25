package org.mtr.mod.render;

import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.Init;
import org.mtr.mod.client.*;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	public static void render(long millisElapsed) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		// When riding a moving vehicle, the client movement is always out of sync with the vehicle rendering. This produces annoying shaking effects.
		// Offsets are used to render the vehicle with respect to the player position rather than the absolute world position, eliminating shaking.

		final boolean canRide = !clientPlayerEntity.isSpectator();

		// Iterate all vehicles that the client knows about
		ClientData.getInstance().vehicles.forEach(vehicle -> {
			// Used to store gangway or barrier connection positions of the previously iterated car
			final ObjectArrayList<PreviousConnectionPositions> previousGangwayPositionsList = new ObjectArrayList<>();
			final ObjectArrayList<PreviousConnectionPositions> previousBarrierPositionsList = new ObjectArrayList<>();
			final PreviousGangwayMovementPositions previousGangwayMovementPositions = new PreviousGangwayMovementPositions();
			// Calculating vehicle transformations in advance
			final ObjectArrayList<RenderVehicleHelper.VehicleProperties> vehiclePropertiesList = RenderVehicleHelper.getTransformedVehiclePropertiesList(vehicle, vehicle.getVehicleCarsAndPositions().stream().map(RenderVehicleHelper.VehicleProperties::new).collect(Collectors.toCollection(ObjectArrayList::new)));

			// Iterate all cars of a vehicle
			iterateWithIndex(vehiclePropertiesList, (carNumber, vehicleProperties) -> CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleProperties.vehicleCar.getVehicleId(), vehicleResource -> {
				final RenderVehicleTransformationHelper renderVehicleTransformationHelperAbsolute = vehicleProperties.renderVehicleTransformationHelperAbsolute;
				final RenderVehicleTransformationHelper renderVehicleTransformationHelperOffset = vehicleProperties.renderVehicleTransformationHelperOffset;

				// Render each bogie of the car
				iterateWithIndex(vehicleProperties.bogiePositionsList, (bogieIndex, bogiePositions) -> {
					final RenderVehicleTransformationHelper renderVehicleTransformationHelperBogie = new RenderVehicleTransformationHelper(bogiePositions, renderVehicleTransformationHelperOffset);
					if (useOptimizedRendering()) {
						RenderVehicleHelper.renderModel(renderVehicleTransformationHelperBogie, storedMatrixTransformations -> vehicleResource.queueBogie(bogieIndex, storedMatrixTransformations, vehicle, renderVehicleTransformationHelperBogie.light));
					} else {
						vehicleResource.iterateBogieModels(bogieIndex, model -> RenderVehicleHelper.renderModel(renderVehicleTransformationHelperBogie, storedMatrixTransformations -> model.render(storedMatrixTransformations, vehicle, renderVehicleTransformationHelperBogie.light, new ObjectArrayList<>())));
					}
				});

				// Player position relative to the car
				final Vector3d playerPosition = renderVehicleTransformationHelperAbsolute.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
				// A temporary list to store all floors and doorways
				final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
				// Extra floors to be used to define where the gangways are
				final GangwayMovementPositions gangwayMovementPositions1 = new GangwayMovementPositions(renderVehicleTransformationHelperAbsolute, false);
				final GangwayMovementPositions gangwayMovementPositions2 = new GangwayMovementPositions(renderVehicleTransformationHelperAbsolute, true);
				// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
				final ObjectArrayList<Box> openDoorways = vehicle.isMoving() || !vehicle.persistentVehicleData.checkCanOpenDoors() ? new ObjectArrayList<>() : vehicleResource.doorways.stream().filter(doorway -> RenderVehicleHelper.canOpenDoors(doorway, renderVehicleTransformationHelperAbsolute, vehicle.persistentVehicleData.getDoorValue())).collect(Collectors.toCollection(ObjectArrayList::new));

				if (canRide) {
					vehicleResource.floors.forEach(floor -> {
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
						RenderVehicleHelper.renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderVehicleTransformationHelperOffset);
						// Find the floors with the lowest and highest Z values to be used to define where the gangways are
						gangwayMovementPositions1.check(floor);
						gangwayMovementPositions2.check(floor);
					});

					openDoorways.forEach(doorway -> {
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
						RenderVehicleHelper.renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderVehicleTransformationHelperOffset);
					});

					// Check and mount player
					VehicleRidingMovement.startRiding(openDoorways, vehicle.vehicleExtraData.getSidingId(), vehicle.getId(), carNumber, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped(), renderVehicleTransformationHelperAbsolute.yaw);
				}

				// Each car can have more than one model defined
				RenderVehicleHelper.renderModel(renderVehicleTransformationHelperOffset, storedMatrixTransformations -> {
					if (useOptimizedRendering()) {
						vehicleResource.queue(storedMatrixTransformations, vehicle, renderVehicleTransformationHelperAbsolute.light, openDoorways);
					}

					vehicleResource.iterateModels((modelIndex, model) -> {
						model.render(storedMatrixTransformations, vehicle, renderVehicleTransformationHelperAbsolute.light, openDoorways);

						if (modelIndex >= previousGangwayPositionsList.size()) {
							previousGangwayPositionsList.add(new PreviousConnectionPositions());
						}

						if (modelIndex >= previousBarrierPositionsList.size()) {
							previousBarrierPositionsList.add(new PreviousConnectionPositions());
						}

						// Render gangway
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
								renderVehicleTransformationHelperOffset,
								vehicleProperties.vehicleCar.getLength(),
								model.modelProperties.getGangwayWidth(),
								model.modelProperties.getGangwayHeight(),
								model.modelProperties.getGangwayYOffset(),
								model.modelProperties.getGangwayZOffset(),
								vehicle.getIsOnRoute()
						);

						// Render barrier
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
								renderVehicleTransformationHelperOffset,
								vehicleProperties.vehicleCar.getLength(),
								model.modelProperties.getBarrierWidth(),
								model.modelProperties.getBarrierHeight(),
								model.modelProperties.getBarrierYOffset(),
								model.modelProperties.getBarrierZOffset(),
								vehicle.getIsOnRoute()
						);
					});
				});

				// If the vehicle has gangways, add extra floors to define where the gangways are
				if (vehicleResource.hasGangway1()) {
					final Box gangwayConnectionFloor1 = gangwayMovementPositions1.getBox();
					RenderVehicleHelper.renderFloorOrDoorway(gangwayConnectionFloor1, 0xFF0000FF, playerPosition, renderVehicleTransformationHelperOffset);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor1, true));
				}
				if (vehicleResource.hasGangway2()) {
					final Box gangwayConnectionFloor2 = gangwayMovementPositions2.getBox();
					RenderVehicleHelper.renderFloorOrDoorway(gangwayConnectionFloor2, 0xFF0000FF, playerPosition, renderVehicleTransformationHelperOffset);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor2, true));
				}

				if (canRide) {
					// Main logic for player movement inside the car
					VehicleRidingMovement.movePlayer(
							millisElapsed, vehicle.getId(), carNumber,
							floorsAndDoorways,
							vehicleResource.hasGangway1() ? previousGangwayMovementPositions.gangwayMovementPositions : null,
							vehicleResource.hasGangway1() ? gangwayMovementPositions1 : null,
							vehicleResource.hasGangway2() ? gangwayMovementPositions2 : null,
							renderVehicleTransformationHelperAbsolute
					);
				}

				previousGangwayMovementPositions.gangwayMovementPositions = gangwayMovementPositions2;
			}));
		});
	}

	public static boolean useOptimizedRendering() {
		return Config.useDynamicFPS() && OptimizedRenderer.hasOptimizedRendering();
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

			RenderTrains.scheduleRender(outerSideTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Sides
				drawTexture(graphicsHolder, position2, position7, position8, position1, newOffset, lightConnection);
				drawTexture(graphicsHolder, position6, position3, position4, position5, newOffset, lightConnection);
			}));

			RenderTrains.scheduleRender(outerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Top
				drawTexture(graphicsHolder, position3, position6, position7, position2, newOffset, lightConnection);
			}));

			RenderTrains.scheduleRender(outerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Bottom
				drawTexture(graphicsHolder, position1, position8, position5, position4, newOffset, lightConnection);
			}));

			RenderTrains.scheduleRender(innerSideTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Sides
				drawTexture(graphicsHolder, position7, position2, position1, position8, newOffset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
				drawTexture(graphicsHolder, position3, position6, position5, position4, newOffset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
			}));

			RenderTrains.scheduleRender(innerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Top
				drawTexture(graphicsHolder, position6, position3, position2, position7, newOffset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
			}));

			RenderTrains.scheduleRender(innerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Bottom
				drawTexture(graphicsHolder, position8, position1, position4, position5, newOffset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
			}));
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

	private static void drawTexture(GraphicsHolder graphicsHolder, Vector position1, Vector position2, Vector position3, Vector position4, Vector3d offset, int light) {
		IDrawing.drawTexture(
				graphicsHolder,
				position1.x, position1.y, position1.z,
				position2.x, position2.y, position2.z,
				position3.x, position3.y, position3.z,
				position4.x, position4.y, position4.z,
				offset, 0, 0, 1, 1, Direction.UP, ARGB_WHITE, light
		);
	}

	private static <T> void iterateWithIndex(ObjectArrayList<T> list, IndexedConsumer<T> indexedConsumer) {
		for (int i = 0; i < list.size(); i++) {
			indexedConsumer.accept(i, list.get(i));
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

	@FunctionalInterface
	private interface IndexedConsumer<T> {
		void accept(int index, T object);
	}
}
