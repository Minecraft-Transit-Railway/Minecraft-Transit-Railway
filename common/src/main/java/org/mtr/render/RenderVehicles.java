package org.mtr.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.mtr.client.*;
import org.mtr.config.Config;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.data.IGui;
import org.mtr.item.ItemDriverKey;
import org.mtr.resource.VehicleResource;
import org.mtr.resource.VehicleResourceCache;
import org.mtr.servlet.ResourcePackCreatorOperationServlet;
import org.mtr.tool.Interpolation;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	public static final ObjectArrayList<RidingPlayerInterpolation> RIDING_PLAYER_INTERPOLATIONS = new ObjectArrayList<>();

	public static void render(long millisElapsed, Vec3d cameraShakeOffset) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasks = new ObjectArrayList<>();
		final Vec3d cameraPosition = minecraftClient.gameRenderer.getCamera().getPos();
		final com.logisticscraft.occlusionculling.util.Vec3d camera = new com.logisticscraft.occlusionculling.util.Vec3d(cameraPosition.x, cameraPosition.y, cameraPosition.z);

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
			final ObjectArrayList<ObjectObjectImmutablePair<VehicleCar, ObjectObjectImmutablePair<ObjectArrayList<PositionAndRotation>, PositionAndRotation>>> vehiclePropertiesList = vehicle.getSmoothedVehicleCarsAndPositions(millisElapsed)
					.stream()
					.map(vehicleCarAndPosition -> {
						final ObjectArrayList<PositionAndRotation> bogiePositions = vehicleCarAndPosition.right()
								.stream()
								.map(bogiePositionPair -> new PositionAndRotation(bogiePositionPair.left(), bogiePositionPair.right(), true))
								.collect(Collectors.toCollection(ObjectArrayList::new));
						return new ObjectObjectImmutablePair<>(vehicleCarAndPosition.left(), new ObjectObjectImmutablePair<>(bogiePositions, new PositionAndRotation(bogiePositions, vehicleCarAndPosition.left(), vehicle.getTransportMode().hasPitchAscending || vehicle.getTransportMode().hasPitchDescending)));
					})
					.collect(Collectors.toCollection(ObjectArrayList::new));

			// Riding offset
			final IntObjectImmutablePair<ObjectObjectImmutablePair<Vec3d, Double>> ridingVehicleCarNumberAndOffset = VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(vehicle.getId());
			final int ridingCarNumber;
			final PositionAndRotation ridingCarPositionAndRotation;
			final Vec3d offsetVector;
			final Double offsetRotation;
			if (ridingVehicleCarNumberAndOffset == null) {
				ridingCarNumber = -1;
				ridingCarPositionAndRotation = null;
				offsetVector = null;
				offsetRotation = null;
			} else {
				ridingCarNumber = ridingVehicleCarNumberAndOffset.leftInt();
				ridingCarPositionAndRotation = vehiclePropertiesList.get(ridingCarNumber).right().right();
				offsetVector = ridingVehicleCarNumberAndOffset.right().left();
				final Double tempOffsetRotation = ridingVehicleCarNumberAndOffset.right().right();
				offsetRotation = tempOffsetRotation == null ? null : tempOffsetRotation + (Math.abs(Utilities.circularDifference(Math.round(clientPlayerEntity.getYaw()), Math.round(minecraftClient.gameRenderer.getCamera().getYaw()), 360)) > 90 ? Math.PI : 0);
			}

			// Iterate all cars of a vehicle
			iterateWithIndex(vehiclePropertiesList, (carNumber, vehicleCarDetails) -> {
				CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleCarDetails.left().getVehicleId(), vehicleResourceDetails -> {
					final VehicleResource vehicleResource = vehicleResourceDetails.left();
					final boolean fromResourcePackCreator = vehicleResourceDetails.rightBoolean() && !vehicle.getIsOnRoute();
					final int[] scrollingDisplayIndexTracker = {0};

					// Riding offset
					final PositionAndRotation absoluteVehicleCarPositionAndRotation = vehicleCarDetails.right().right();
					final PositionAndRotation vehicleCarRenderingPositionAndRotation = getRenderPositionAndRotation(offsetVector, offsetRotation, ridingCarPositionAndRotation, absoluteVehicleCarPositionAndRotation, cameraShakeOffset);

					// Render each bogie of the car
					iterateWithIndex(vehicleCarDetails.right().left(), (bogieIndex, absoluteBogiePositionAndRotation) -> {
						final PositionAndRotation bogieRenderingPositionAndRotation = getRenderPositionAndRotation(offsetVector, offsetRotation, ridingCarPositionAndRotation, absoluteBogiePositionAndRotation, cameraShakeOffset);
						final StoredMatrixTransformations storedMatrixTransformations = getStoredMatrixTransformations(offsetVector == null, bogieRenderingPositionAndRotation, 0);
						storedMatrixTransformations.add(matrixStack -> IDrawing.rotateXDegrees(matrixStack, 180));
						vehicleResource.queueBogie(bogieIndex, storedMatrixTransformations, vehicle, absoluteVehicleCarPositionAndRotation.light);
					});

					// Player position relative to the car
					final Vec3d playerPosition = absoluteVehicleCarPositionAndRotation.transformBackwards(clientPlayerEntity.getPos(), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
					// A temporary list to store all floors and doorways for player movement
					final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
					// Extra floors to be used to define where the gangways are
					final GangwayMovementPositions gangwayMovementPositions1 = new GangwayMovementPositions(absoluteVehicleCarPositionAndRotation, false);
					final GangwayMovementPositions gangwayMovementPositions2 = new GangwayMovementPositions(absoluteVehicleCarPositionAndRotation, true);
					// Vehicle resource cache
					final VehicleResourceCache vehicleResourceCache = vehicleResource.getCachedVehicleResource(carNumber, vehicle.vehicleExtraData.immutableVehicleCars.size());
					// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
					final ObjectArrayList<Box> openDoorways;
					if (vehicleResourceCache != null && fromResourcePackCreator) {
						openDoorways = vehicle.persistentVehicleData.checkCanOpenDoors() ? new ObjectArrayList<>(vehicleResourceCache.doorways()) : new ObjectArrayList<>();
						vehicle.persistentVehicleData.overrideDoorMultiplier(ResourcePackCreatorOperationServlet.getDoorMultiplier());
					} else if (vehicleResourceCache == null || !vehicle.getTransportMode().continuousMovement && vehicle.isMoving() || !vehicle.persistentVehicleData.checkCanOpenDoors()) {
						openDoorways = new ObjectArrayList<>();
					} else {
						openDoorways = vehicleResourceCache.doorways().stream().filter(doorway -> RenderVehicleHelper.canOpenDoors(doorway, absoluteVehicleCarPositionAndRotation, vehicle.persistentVehicleData.getDoorValue())).collect(Collectors.toCollection(ObjectArrayList::new));
					}
					final double oscillationAmount = vehicle.persistentVehicleData.getOscillation(carNumber).getAmount() * Config.getClient().getVehicleOscillationMultiplier();

					if (canRide) {
						final ObjectArrayList<Box> openFloorsAndDoorways = new ObjectArrayList<>();

						if (vehicleResourceCache != null) {
							vehicleResourceCache.floors().forEach(floor -> {
								floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
								if (!VehicleRidingMovement.isRiding(vehicle.getId())) {
									final ItemDriverKey driverKey = VehicleRidingMovement.getValidHoldingKey(vehicle.vehicleExtraData.getDepotId());
									if (driverKey != null && (driverKey.canBoardAnyVehicle || vehicle.vehicleExtraData.getIsManualAllowed())) {
										openFloorsAndDoorways.add(floor);
									}
								}
								RenderVehicleHelper.renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, vehicleCarRenderingPositionAndRotation, offsetVector == null);
								// Find the floors with the lowest and highest Z values to be used to define where the gangways are
								gangwayMovementPositions1.check(floor);
								gangwayMovementPositions2.check(floor);
							});
						}

						openDoorways.forEach(doorway -> {
							floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
							openFloorsAndDoorways.add(doorway);
							RenderVehicleHelper.renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, vehicleCarRenderingPositionAndRotation, offsetVector == null);
						});

						// Check and mount player
						VehicleRidingMovement.startRiding(openFloorsAndDoorways, vehicle.vehicleExtraData.getDepotId(), vehicle.vehicleExtraData.getSidingId(), vehicle.getId(), carNumber, playerPosition.x, playerPosition.y, playerPosition.z, absoluteVehicleCarPositionAndRotation.yaw);
					}

					// Play vehicle sounds
					vehicle.playMotorSound(vehicleResource, carNumber, absoluteVehicleCarPositionAndRotation.position);

					// Play door sound
					if (!openDoorways.isEmpty()) {
						vehicle.playDoorSound(vehicleResource, carNumber, absoluteVehicleCarPositionAndRotation.position);
					}

					// Each car can have more than one model defined
					if (vehicleResourceCache != null) {
						final StoredMatrixTransformations storedMatrixTransformations = getStoredMatrixTransformations(offsetVector == null, vehicleCarRenderingPositionAndRotation, oscillationAmount);
						storedMatrixTransformations.add(matrixStack -> IDrawing.rotateXDegrees(matrixStack, 180));
						vehicleResourceCache.iterateModels((modelIndex, model) -> {
							model.render(storedMatrixTransformations, vehicle, carNumber, scrollingDisplayIndexTracker, absoluteVehicleCarPositionAndRotation.light, openDoorways, fromResourcePackCreator);

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
									vehicleCarRenderingPositionAndRotation,
									offsetVector == null,
									vehicleCarDetails.left().getLength(),
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
									vehicleCarRenderingPositionAndRotation,
									offsetVector == null,
									vehicleCarDetails.left().getLength(),
									model.modelProperties.getBarrierWidth(),
									model.modelProperties.getBarrierHeight(),
									model.modelProperties.getBarrierYOffset(),
									model.modelProperties.getBarrierZOffset(),
									oscillationAmount,
									vehicle.getIsOnRoute()
							);
						});
					}

					// If the vehicle has gangways, add extra floors to define where the gangways are
					final Box gangwayConnectionFloor1 = gangwayMovementPositions1.getBox();
					if (vehicleResource.hasGangway1()) {
						RenderVehicleHelper.renderFloorOrDoorway(gangwayConnectionFloor1, 0xFF0000FF, playerPosition, vehicleCarRenderingPositionAndRotation, offsetVector == null);
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor1, true));
					}
					final Box gangwayConnectionFloor2 = gangwayMovementPositions2.getBox();
					if (vehicleResource.hasGangway2()) {
						RenderVehicleHelper.renderFloorOrDoorway(gangwayConnectionFloor2, 0xFF0000FF, playerPosition, vehicleCarRenderingPositionAndRotation, offsetVector == null);
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor2, true));
					}

					// Render the current riding player
					if (ridingCarNumber == carNumber && offsetVector != null && minecraftClient.gameRenderer.getCamera().isThirdPerson()) {
						renderPlayer(clientPlayerEntity, -1, 0, 0, offsetVector, offsetVector, offsetRotation, absoluteVehicleCarPositionAndRotation, absoluteVehicleCarPositionAndRotation, cameraShakeOffset);
					}

					// Render other players on this vehicle
					vehicle.vehicleExtraData.iterateRidingEntities(vehicleRidingEntity -> {
						final boolean isOnBackGangway = vehicleRidingEntity.getIsOnGangway() && vehicleRidingEntity.getZ() < 0.5;

						if (vehicleRidingEntity.getRidingCar() - (isOnBackGangway ? 1 : 0) == carNumber && !vehicleRidingEntity.uuid.equals(clientPlayerEntity.getUuid())) {
							final PlayerEntity ridingPlayer = clientWorld.getPlayerByUuid(vehicleRidingEntity.uuid);

							if (ridingPlayer != null) {
								double playerRidingX;
								double playerRidingY;
								double playerRidingZ;

								if (vehicleRidingEntity.getIsOnGangway()) {
									if (isOnBackGangway) {
										playerRidingX = getPositionFromPercentage(vehicleRidingEntity.getX(), gangwayConnectionFloor2.minX + RenderVehicleHelper.HALF_PLAYER_WIDTH, gangwayConnectionFloor2.maxX - RenderVehicleHelper.HALF_PLAYER_WIDTH);
										playerRidingY = getPositionFromPercentage(vehicleRidingEntity.getY(), gangwayConnectionFloor2.minY, gangwayConnectionFloor2.maxY);
										playerRidingZ = getPositionFromPercentage(vehicleRidingEntity.getZ(), gangwayConnectionFloor2.minZ + (gangwayConnectionFloor2.maxZ - gangwayConnectionFloor2.minZ) / 2, gangwayConnectionFloor2.maxZ);
									} else {
										playerRidingX = getPositionFromPercentage(vehicleRidingEntity.getX(), gangwayConnectionFloor1.minX + RenderVehicleHelper.HALF_PLAYER_WIDTH, gangwayConnectionFloor1.maxX - RenderVehicleHelper.HALF_PLAYER_WIDTH);
										playerRidingY = getPositionFromPercentage(vehicleRidingEntity.getY(), gangwayConnectionFloor1.minY, gangwayConnectionFloor1.maxY);
										playerRidingZ = getPositionFromPercentage(vehicleRidingEntity.getZ(), gangwayConnectionFloor1.minZ, gangwayConnectionFloor1.maxZ - (gangwayConnectionFloor1.maxZ - gangwayConnectionFloor1.minZ) / 2);
									}
								} else {
									playerRidingX = vehicleRidingEntity.getX();
									playerRidingY = vehicleRidingEntity.getY();
									playerRidingZ = vehicleRidingEntity.getZ();
								}

								renderPlayer(ridingPlayer, carNumber, gangwayConnectionFloor1.minZ, gangwayConnectionFloor2.maxZ, new Vec3d(playerRidingX, playerRidingY, playerRidingZ), offsetVector, offsetRotation, absoluteVehicleCarPositionAndRotation, ridingCarPositionAndRotation, cameraShakeOffset);
							}
						}
					});

					if (canRide) {
						// Main logic for player movement inside the car
						VehicleRidingMovement.movePlayer(
								millisElapsed, vehicle.getId(), carNumber,
								floorsAndDoorways,
								vehicleResource.hasGangway1() ? previousGangwayMovementPositions.gangwayMovementPositions : null,
								vehicleResource.hasGangway1() ? gangwayMovementPositions1 : null,
								vehicleResource.hasGangway2() ? gangwayMovementPositions2 : null,
								absoluteVehicleCarPositionAndRotation
						);
					}

					previousGangwayMovementPositions.gangwayMovementPositions = gangwayMovementPositions2;
				});
			});
		});
	}

	/**
	 * Gets a new {@link PositionAndRotation} object, either of the absolute position or relative to the player, depending on if the player is currently riding the vehicle.
	 *
	 * @param offsetVector                 if riding, the riding position relative to the centre of the riding car
	 * @param offsetRotation               if riding, the riding rotation relative to the angle of the riding car
	 * @param ridingCarPositionAndRotation the {@link PositionAndRotation} of the car being ridden in
	 * @param renderingPositionAndRotation the {@link PositionAndRotation} of the current object to be rendered
	 * @param cameraShakeOffset            additional camera shake
	 * @return the adjusted {@link PositionAndRotation} object
	 */
	public static PositionAndRotation getRenderPositionAndRotation(@Nullable Vec3d offsetVector, @Nullable Double offsetRotation, @Nullable PositionAndRotation ridingCarPositionAndRotation, PositionAndRotation renderingPositionAndRotation, Vec3d cameraShakeOffset) {
		if (offsetVector == null || ridingCarPositionAndRotation == null) {
			// Normal absolute rendering
			return renderingPositionAndRotation;
		} else if (offsetRotation == null) {
			// Offset rendering
			return new PositionAndRotation(new Vector(-offsetVector.x, -offsetVector.y, -offsetVector.z).rotateX(ridingCarPositionAndRotation.pitch).rotateY(ridingCarPositionAndRotation.yaw).add(
					cameraShakeOffset.x + renderingPositionAndRotation.position.x - ridingCarPositionAndRotation.position.x,
					cameraShakeOffset.y + renderingPositionAndRotation.position.y - ridingCarPositionAndRotation.position.y,
					cameraShakeOffset.z + renderingPositionAndRotation.position.z - ridingCarPositionAndRotation.position.z
			), renderingPositionAndRotation.yaw, renderingPositionAndRotation.pitch);
		} else {
			// Offset rendering with rotation
			final double ridingRotation = offsetRotation - ridingCarPositionAndRotation.yaw - Math.toRadians(MinecraftClient.getInstance().gameRenderer.getCamera().getYaw());
			return new PositionAndRotation(new Vector(-offsetVector.x, -offsetVector.y, -offsetVector.z).rotateX(ridingCarPositionAndRotation.pitch).rotateY(ridingCarPositionAndRotation.yaw).add(
					renderingPositionAndRotation.position.x - ridingCarPositionAndRotation.position.x,
					renderingPositionAndRotation.position.y - ridingCarPositionAndRotation.position.y,
					renderingPositionAndRotation.position.z - ridingCarPositionAndRotation.position.z
			).rotateY(ridingRotation).add(
					cameraShakeOffset.x,
					cameraShakeOffset.y,
					cameraShakeOffset.z
			), renderingPositionAndRotation.yaw + ridingRotation, renderingPositionAndRotation.pitch);
		}
	}

	/**
	 * Gets the {@link StoredMatrixTransformations} after any offsets are applied.
	 *
	 * @param useOffset                    {@code true} if the vehicle is not being ridden in
	 * @param renderingPositionAndRotation the {@link PositionAndRotation} of the current object to be rendered
	 * @param oscillationAmount            any oscillation to be applied
	 * @return the adjusted {@link StoredMatrixTransformations} object
	 */
	public static StoredMatrixTransformations getStoredMatrixTransformations(boolean useOffset, PositionAndRotation renderingPositionAndRotation, double oscillationAmount) {
		final StoredMatrixTransformations storedMatrixTransformations;

		if (useOffset) {
			storedMatrixTransformations = new StoredMatrixTransformations(renderingPositionAndRotation.position.x, renderingPositionAndRotation.position.y, renderingPositionAndRotation.position.z);
		} else {
			storedMatrixTransformations = new StoredMatrixTransformations();
			storedMatrixTransformations.add(graphicsHolder -> graphicsHolder.translate(renderingPositionAndRotation.position.x, renderingPositionAndRotation.position.y, renderingPositionAndRotation.position.z));
		}

		storedMatrixTransformations.add(matrixStack -> {
			IDrawing.rotateYRadians(matrixStack, (float) (renderingPositionAndRotation.yaw + Math.PI));
			IDrawing.rotateXRadians(matrixStack, (float) (renderingPositionAndRotation.pitch + Math.PI));
			IDrawing.rotateZDegrees(matrixStack, (float) oscillationAmount);
		});

		return storedMatrixTransformations;
	}

	/**
	 * Renders a player riding the vehicle.
	 *
	 * @param playerEntity                 the entity to be rendered
	 * @param ridingCar                    the car the entity is riding in (don't interpolate if the riding car number has changed) or -1 to disable interpolation altogether
	 * @param minZ                         the min Z value of the car, used for when the entity just came from a gangway
	 * @param maxZ                         the max Z value of the car, used for when the entity just came from a gangway
	 * @param playerOffsetVector           the player offset from the centre of the player's riding car
	 * @param offsetVector                 if riding, the riding position relative to the centre of the riding car
	 * @param offsetRotation               if riding, the riding rotation relative to the angle of the riding car
	 * @param playerCarPositionAndRotation the {@link PositionAndRotation} of the car being ridden by the entity being rendered
	 * @param ridingCarPositionAndRotation the {@link PositionAndRotation} of the car being ridden in
	 * @param cameraShakeOffset            additional camera shake
	 */
	public static void renderPlayer(PlayerEntity playerEntity, int ridingCar, double minZ, double maxZ, Vec3d playerOffsetVector, @Nullable Vec3d offsetVector, @Nullable Double offsetRotation, PositionAndRotation playerCarPositionAndRotation, @Nullable PositionAndRotation ridingCarPositionAndRotation, Vec3d cameraShakeOffset) {
		Vector interpolatedPosition = null;

		for (final RidingPlayerInterpolation ridingPlayerInterpolation : RIDING_PLAYER_INTERPOLATIONS) {
			if (ridingPlayerInterpolation.uuid.equals(playerEntity.getUuid())) {
				// Interpolate movement
				if (ridingCar >= 0 && ridingPlayerInterpolation.ridingCar == ridingCar) {
					ridingPlayerInterpolation.interpolationX.setValue(playerOffsetVector.x, ridingPlayerInterpolation.previousX != playerOffsetVector.x);
					ridingPlayerInterpolation.interpolationY.setValue(playerOffsetVector.y, ridingPlayerInterpolation.previousY != playerOffsetVector.y);
					ridingPlayerInterpolation.interpolationZ.setValue(playerOffsetVector.z, ridingPlayerInterpolation.previousZ != playerOffsetVector.z);
				} else {
					ridingPlayerInterpolation.interpolationX.setValueDirect(playerOffsetVector.x);
					ridingPlayerInterpolation.interpolationY.setValueDirect(playerOffsetVector.y);
					if (ridingCar >= 0) {
						// Entity came from a gangway
						ridingPlayerInterpolation.interpolationZ.setValueDirect(ridingCar > ridingPlayerInterpolation.ridingCar ? minZ : maxZ);
						ridingPlayerInterpolation.interpolationZ.setValue(playerOffsetVector.z, true);
					} else {
						ridingPlayerInterpolation.interpolationZ.setValueDirect(playerOffsetVector.z);
					}
				}
				ridingPlayerInterpolation.ridingCar = ridingCar;
				ridingPlayerInterpolation.previousX = playerOffsetVector.x;
				ridingPlayerInterpolation.previousY = playerOffsetVector.y;
				ridingPlayerInterpolation.previousZ = playerOffsetVector.z;
				interpolatedPosition = new Vector(ridingPlayerInterpolation.interpolationX.getValue(), ridingPlayerInterpolation.interpolationY.getValue(), ridingPlayerInterpolation.interpolationZ.getValue());
				break;
			}
		}

		if (interpolatedPosition == null) {
			// If the player is not found in RIDING_PLAYER_INTERPOLATIONS
			interpolatedPosition = new Vector(playerOffsetVector.x, playerOffsetVector.y, playerOffsetVector.z);
			final RidingPlayerInterpolation ridingPlayerInterpolation = new RidingPlayerInterpolation(playerEntity.getUuid());
			RIDING_PLAYER_INTERPOLATIONS.add(ridingPlayerInterpolation);
			ridingPlayerInterpolation.interpolationX.setValueDirect(playerOffsetVector.x);
			ridingPlayerInterpolation.interpolationY.setValueDirect(playerOffsetVector.y);
			ridingPlayerInterpolation.interpolationZ.setValueDirect(playerOffsetVector.z);
			ridingPlayerInterpolation.ridingCar = ridingCar;
			ridingPlayerInterpolation.previousX = playerOffsetVector.x;
			ridingPlayerInterpolation.previousY = playerOffsetVector.y;
			ridingPlayerInterpolation.previousZ = playerOffsetVector.z;
		}

		// Transform position
		final PositionAndRotation playerRenderingPositionAndRotation = getRenderPositionAndRotation(
				offsetVector, offsetRotation,
				ridingCarPositionAndRotation,
				new PositionAndRotation(playerCarPositionAndRotation.position.add(interpolatedPosition.rotateX(playerCarPositionAndRotation.pitch).rotateY(playerCarPositionAndRotation.yaw)), 0, 0),
				cameraShakeOffset
		);
		final StoredMatrixTransformations storedMatrixTransformations = getStoredMatrixTransformations(offsetVector == null, playerRenderingPositionAndRotation, 0);

		// Render player
		MainRenderer.scheduleRender(QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.rotateXDegrees(matrixStack, 180);
			IDrawing.rotateYDegrees(matrixStack, 180);
			final MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getEntityRenderDispatcher().render(playerEntity, 0, 0, 0, 0, matrixStack, minecraftClient.getBufferBuilders().getEntityVertexConsumers(), IGui.DEFAULT_LIGHT);
			matrixStack.pop();
		});
	}

	private static void renderConnection(
			boolean shouldRender1, boolean shouldRender2, boolean canHaveLight, PreviousConnectionPositions previousConnectionPositions,
			@Nullable Identifier innerSideTexture,
			@Nullable Identifier innerTopTexture,
			@Nullable Identifier innerBottomTexture,
			@Nullable Identifier outerSideTexture,
			@Nullable Identifier outerTopTexture,
			@Nullable Identifier outerBottomTexture,
			PositionAndRotation positionAndRotation, boolean useOffset,
			double vehicleLength, double width, double height, double yOffset, double zOffset, double oscillationAmount, boolean isOnRoute
	) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			return;
		}

		final double halfLength = vehicleLength / 2;
		final double newOscillationAmount = -Math.toRadians(oscillationAmount);

		if (shouldRender1 && previousConnectionPositions.isValid()) {
			final Vector position1 = positionAndRotation.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position2 = positionAndRotation.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position3 = positionAndRotation.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			final Vector position4 = positionAndRotation.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);

			final Vector position5 = previousConnectionPositions.position1;
			final Vector position6 = previousConnectionPositions.position2;
			final Vector position7 = previousConnectionPositions.position3;
			final Vector position8 = previousConnectionPositions.position4;

			final BlockPos blockPosConnection = BlockPos.ofFloored(position1.x, position1.y + 1, position1.z);
			final int lightConnection = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.BLOCK, blockPosConnection), clientWorld.getLightLevel(LightType.SKY, blockPosConnection));

			MainRenderer.scheduleRender(outerSideTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Sides
				drawTexture(matrixStack, vertexConsumer, position2, position7, position8, position1, useOffset ? offset : Vec3d.ZERO, lightConnection);
				drawTexture(matrixStack, vertexConsumer, position6, position3, position4, position5, useOffset ? offset : Vec3d.ZERO, lightConnection);
			});

			MainRenderer.scheduleRender(outerTopTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Top
				drawTexture(matrixStack, vertexConsumer, position3, position6, position7, position2, useOffset ? offset : Vec3d.ZERO, lightConnection);
			});

			MainRenderer.scheduleRender(outerBottomTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Bottom
				drawTexture(matrixStack, vertexConsumer, position1, position8, position5, position4, useOffset ? offset : Vec3d.ZERO, lightConnection);
			});

			MainRenderer.scheduleRender(innerSideTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Sides
				drawTexture(matrixStack, vertexConsumer, position7, position2, position1, position8, useOffset ? offset : Vec3d.ZERO, canHaveLight && isOnRoute ? DEFAULT_LIGHT : lightConnection);
				drawTexture(matrixStack, vertexConsumer, position3, position6, position5, position4, useOffset ? offset : Vec3d.ZERO, canHaveLight && isOnRoute ? DEFAULT_LIGHT : lightConnection);
			});

			MainRenderer.scheduleRender(innerTopTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Top
				drawTexture(matrixStack, vertexConsumer, position6, position3, position2, position7, useOffset ? offset : Vec3d.ZERO, canHaveLight && isOnRoute ? DEFAULT_LIGHT : lightConnection);
			});

			MainRenderer.scheduleRender(innerBottomTexture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				// Bottom
				drawTexture(matrixStack, vertexConsumer, position8, position1, position4, position5, useOffset ? offset : Vec3d.ZERO, canHaveLight && isOnRoute ? DEFAULT_LIGHT : lightConnection);
			});
		}

		if (shouldRender2) {
			previousConnectionPositions.position1 = positionAndRotation.transformForwards(new Vector(width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position2 = positionAndRotation.transformForwards(new Vector(width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position3 = positionAndRotation.transformForwards(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position4 = positionAndRotation.transformForwards(new Vector(-width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength).rotateZ(newOscillationAmount), Vector::rotateX, Vector::rotateY, Vector::add);
		} else {
			previousConnectionPositions.position1 = null;
			previousConnectionPositions.position2 = null;
			previousConnectionPositions.position3 = null;
			previousConnectionPositions.position4 = null;
		}
	}

	private static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vector position1, Vector position2, Vector position3, Vector position4, Vec3d offset, int light) {
		IDrawing.drawTexture(
				matrixStack, vertexConsumer,
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

	private static double getPositionFromPercentage(double percentage, double min, double max) {
		return min + Math.max(0, max - min) * percentage;
	}

	public static class RidingPlayerInterpolation {

		private int ridingCar;
		private double previousX;
		private double previousY;
		private double previousZ;

		public final UUID uuid;
		private final Interpolation interpolationX;
		private final Interpolation interpolationY;
		private final Interpolation interpolationZ;

		public RidingPlayerInterpolation(UUID uuid) {
			this.uuid = uuid;
			interpolationX = new Interpolation(VehicleRidingMovement.SEND_UPDATE_FREQUENCY);
			interpolationY = new Interpolation(VehicleRidingMovement.SEND_UPDATE_FREQUENCY);
			interpolationZ = new Interpolation(VehicleRidingMovement.SEND_UPDATE_FREQUENCY);
		}
	}

	public static class PreviousConnectionPositions {

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
