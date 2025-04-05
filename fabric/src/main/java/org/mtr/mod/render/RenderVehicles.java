package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.Init;
import org.mtr.mod.client.*;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;
import org.mtr.mod.resource.VehicleResource;
import org.mtr.mod.resource.VehicleResourceCache;
import org.mtr.mod.servlet.ResourcePackCreatorOperationServlet;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	public static void render(long millisElapsed, Vector3d cameraShakeOffset) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasks = new ObjectArrayList<>();
		final Vector3d cameraPosition = minecraftClient.getGameRendererMapped().getCamera().getPos();
		final Vec3d camera = new Vec3d(cameraPosition.getXMapped(), cameraPosition.getYMapped(), cameraPosition.getZMapped());

		// When riding a moving vehicle, the client movement is always out of sync with the vehicle rendering. This produces annoying shaking effects.
		// Offsets are used to render the vehicle with respect to the player position rather than the absolute world position, eliminating shaking.

		final boolean canRide = !clientPlayerEntity.isSpectator();

		// Iterate all vehicles that the client knows about
		MinecraftClientData.getInstance().vehicles.forEach(vehicle -> {
			// Used to store gangway or barrier connection positions of the previously iterated car
			final ObjectArrayList<PreviousConnectionPositions> previousGangwayPositionsList = new ObjectArrayList<>();
			final ObjectArrayList<PreviousConnectionPositions> previousBarrierPositionsList = new ObjectArrayList<>();
			final PreviousGangwayMovementPositions previousGangwayMovementPositions = new PreviousGangwayMovementPositions();
			// Calculating vehicle transformations in advance
			final ObjectArrayList<RenderVehicleHelper.VehicleProperties> vehiclePropertiesList = RenderVehicleHelper.getTransformedVehiclePropertiesList(vehicle, vehicle.getVehicleCarsAndPositions().stream()
					.map(vehicleCarAndPosition -> new RenderVehicleHelper.VehicleProperties(vehicleCarAndPosition, !vehicle.getTransportMode().hasPitchAscending && !vehicle.getTransportMode().hasPitchDescending))
					.collect(Collectors.toCollection(ObjectArrayList::new)), cameraShakeOffset);

			// Iterate all cars of a vehicle
			iterateWithIndex(vehiclePropertiesList, (carNumber, vehicleProperties) -> {
				final RenderVehicleTransformationHelper renderVehicleTransformationHelperAbsolute = vehicleProperties.renderVehicleTransformationHelperAbsolute;
				final RenderVehicleTransformationHelper renderVehicleTransformationHelperOffset = vehicleProperties.renderVehicleTransformationHelperOffset;

				cullingTasks.add(occlusionCullingInstance -> {
					final double longestDimension = vehicle.persistentVehicleData.longestDimensions[carNumber];
					final boolean shouldRender = occlusionCullingInstance.isAABBVisible(new Vec3d(
							renderVehicleTransformationHelperAbsolute.pivotPosition.x - longestDimension,
							renderVehicleTransformationHelperAbsolute.pivotPosition.y - 8,
							renderVehicleTransformationHelperAbsolute.pivotPosition.z - longestDimension
					), new Vec3d(
							renderVehicleTransformationHelperAbsolute.pivotPosition.x + longestDimension,
							renderVehicleTransformationHelperAbsolute.pivotPosition.y + 8,
							renderVehicleTransformationHelperAbsolute.pivotPosition.z + longestDimension
					), camera);
					return () -> vehicle.persistentVehicleData.rayTracing[carNumber] = shouldRender;
				});

				if (vehicle.persistentVehicleData.rayTracing[carNumber] || VehicleRidingMovement.isRiding(vehicle.getId())) {
					CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleProperties.vehicleCar.getVehicleId(), vehicleResourceDetails -> {
						final VehicleResource vehicleResource = vehicleResourceDetails.left();
						final boolean fromResourcePackCreator = vehicleResourceDetails.rightBoolean() && !vehicle.getIsOnRoute();
						final int[] scrollingDisplayIndexTracker = {0};

						// Render each bogie of the car
						iterateWithIndex(vehicleProperties.bogiePositionsList, (bogieIndex, bogiePositions) -> {
							final RenderVehicleTransformationHelper renderVehicleTransformationHelperBogie = new RenderVehicleTransformationHelper(bogiePositions, vehicleProperties.averageAbsoluteBogiePositionsList.get(bogieIndex), renderVehicleTransformationHelperOffset);
							if (OptimizedRenderer.hasOptimizedRendering()) {
								RenderVehicleHelper.renderModel(renderVehicleTransformationHelperBogie, 0, storedMatrixTransformations -> vehicleResource.queueBogie(bogieIndex, storedMatrixTransformations, vehicle, renderVehicleTransformationHelperBogie.light));
							} else {
								vehicleResource.iterateBogieModels(bogieIndex, (modelIndex, model) -> RenderVehicleHelper.renderModel(renderVehicleTransformationHelperBogie, 0, storedMatrixTransformations -> model.render(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, renderVehicleTransformationHelperBogie.light, new ObjectArrayList<>(), fromResourcePackCreator)));
							}
						});

						// Player position relative to the car
						final Vector3d playerPosition = renderVehicleTransformationHelperAbsolute.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
						// A temporary list to store all floors and doorways
						final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
						// Extra floors to be used to define where the gangways are
						final GangwayMovementPositions gangwayMovementPositions1 = new GangwayMovementPositions(renderVehicleTransformationHelperAbsolute, false);
						final GangwayMovementPositions gangwayMovementPositions2 = new GangwayMovementPositions(renderVehicleTransformationHelperAbsolute, true);
						// Vehicle resource cache
						final VehicleResourceCache vehicleResourceCache = vehicleResource.getCachedVehicleResource(carNumber, vehicle.vehicleExtraData.immutableVehicleCars.size(), false);
						// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
						final ObjectArrayList<Box> openDoorways;
						if (vehicleResourceCache != null && fromResourcePackCreator) {
							openDoorways = vehicle.persistentVehicleData.checkCanOpenDoors() ? new ObjectArrayList<>(vehicleResourceCache.doorways) : new ObjectArrayList<>();
							vehicle.persistentVehicleData.overrideDoorMultiplier(ResourcePackCreatorOperationServlet.getDoorMultiplier());
						} else if (vehicleResourceCache == null || !vehicle.getTransportMode().continuousMovement && vehicle.isMoving() || !vehicle.persistentVehicleData.checkCanOpenDoors()) {
							openDoorways = new ObjectArrayList<>();
						} else {
							openDoorways = vehicleResourceCache.doorways.stream().filter(doorway -> RenderVehicleHelper.canOpenDoors(doorway, renderVehicleTransformationHelperAbsolute, vehicle.persistentVehicleData.getDoorValue(), false)).collect(Collectors.toCollection(ObjectArrayList::new));
						}
						final double oscillationAmount = vehicle.persistentVehicleData.getOscillation(carNumber).getAmount() * Config.getClient().getVehicleOscillationMultiplier();

						if (canRide) {
							if (vehicleResourceCache != null) {
								vehicleResourceCache.floors.forEach(floor -> {
									floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
									RenderVehicleHelper.renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderVehicleTransformationHelperOffset);
									// Find the floors with the lowest and highest Z values to be used to define where the gangways are
									gangwayMovementPositions1.check(floor);
									gangwayMovementPositions2.check(floor);
								});
							}

							openDoorways.forEach(doorway -> {
								floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
								RenderVehicleHelper.renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderVehicleTransformationHelperOffset);
							});

							// Check and mount player
							VehicleRidingMovement.startRiding(openDoorways, vehicle.vehicleExtraData.getSidingId(), vehicle.getId(), carNumber, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped(), renderVehicleTransformationHelperAbsolute.yaw);
						}

						// Play vehicle sounds
						if (!OptimizedRenderer.renderingShadows()) {
							vehicle.playMotorSound(vehicleResource, carNumber, renderVehicleTransformationHelperAbsolute.pivotPosition);

							// Play door sounds
							if(!openDoorways.isEmpty()) {
								vehicle.playDoorSound(vehicleResource, carNumber, renderVehicleTransformationHelperAbsolute.pivotPosition);
							}
						}

						// Each car can have more than one model defined
						RenderVehicleHelper.renderModel(renderVehicleTransformationHelperOffset, oscillationAmount, storedMatrixTransformations -> {
							if (OptimizedRenderer.hasOptimizedRendering()) {
								vehicleResource.queue(storedMatrixTransformations, vehicle, carNumber, vehicle.vehicleExtraData.immutableVehicleCars.size(), renderVehicleTransformationHelperAbsolute.light, openDoorways);
							}

							vehicleResource.iterateModels(carNumber, vehicle.vehicleExtraData.immutableVehicleCars.size(), (modelIndex, model) -> {
								model.render(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, renderVehicleTransformationHelperAbsolute.light, openDoorways, fromResourcePackCreator);

								while (modelIndex >= previousGangwayPositionsList.size()) {
									previousGangwayPositionsList.add(new PreviousConnectionPositions());
								}

								while (modelIndex >= previousBarrierPositionsList.size()) {
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
										oscillationAmount,
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
										oscillationAmount,
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
					});
				}
			});
		});

		if (!OptimizedRenderer.renderingShadows()) {
			MainRenderer.WORKER_THREAD.scheduleVehicles(occlusionCullingInstance -> {
				final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
				cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
				minecraftClient.execute(() -> tasks.forEach(Runnable::run));
			});
		}
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
			double vehicleLength, double width, double height, double yOffset, double zOffset, double oscillationAmount, boolean isOnRoute
	) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final double halfLength = vehicleLength / 2;
		final double newOscillationAmount = -Math.toRadians(oscillationAmount);

		if (shouldRender1 && previousConnectionPositions.isValid()) {
			final Vector position1 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position2 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position3 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position4 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);

			final Vector position5 = previousConnectionPositions.position1;
			final Vector position6 = previousConnectionPositions.position2;
			final Vector position7 = previousConnectionPositions.position3;
			final Vector position8 = previousConnectionPositions.position4;

			final BlockPos blockPosConnection = Init.newBlockPos(position1.x, position1.y + 1, position1.z);
			final int lightConnection = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPosConnection), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPosConnection));

			MainRenderer.scheduleRender(outerSideTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Sides
				drawTexture(graphicsHolder, position2, position7, position8, position1, newOffset, lightConnection);
				drawTexture(graphicsHolder, position6, position3, position4, position5, newOffset, lightConnection);
			}));

			MainRenderer.scheduleRender(outerTopTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Top
				drawTexture(graphicsHolder, position3, position6, position7, position2, newOffset, lightConnection);
			}));

			MainRenderer.scheduleRender(outerBottomTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Bottom
				drawTexture(graphicsHolder, position1, position8, position5, position4, newOffset, lightConnection);
			}));

			MainRenderer.scheduleRender(innerSideTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Sides
				drawTexture(graphicsHolder, position7, position2, position1, position8, newOffset, canHaveLight && isOnRoute ? GraphicsHolder.getDefaultLight() : lightConnection);
				drawTexture(graphicsHolder, position3, position6, position5, position4, newOffset, canHaveLight && isOnRoute ? GraphicsHolder.getDefaultLight() : lightConnection);
			}));

			MainRenderer.scheduleRender(innerTopTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Top
				drawTexture(graphicsHolder, position6, position3, position2, position7, newOffset, canHaveLight && isOnRoute ? GraphicsHolder.getDefaultLight() : lightConnection);
			}));

			MainRenderer.scheduleRender(innerBottomTexture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				// Bottom
				drawTexture(graphicsHolder, position8, position1, position4, position5, newOffset, canHaveLight && isOnRoute ? GraphicsHolder.getDefaultLight() : lightConnection);
			}));
		}

		if (shouldRender2) {
			previousConnectionPositions.position1 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position2 = renderVehicleTransformationHelper.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position3 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position4 = renderVehicleTransformationHelper.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
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
