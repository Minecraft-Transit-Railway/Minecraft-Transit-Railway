package org.mtr.mod.render;

import org.mtr.core.data.VehicleCar;
import org.mtr.core.tools.Utilities;
import org.mtr.core.tools.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	private static long ridingVehicleId;
	private static double ridingVehicleX;
	private static double ridingVehicleY;
	private static double ridingVehicleZ;
	private static int ridingVehicleCarNumber;
	private static int ridingVehicleCoolDown;

	private static final int CHECK_DOOR_RADIUS = 2;
	private static final int RIDE_STEP_THRESHOLD = 8;
	private static final int RIDING_COOL_DOWN = 5;
	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.005F;
	private static final float HALF_PLAYER_WIDTH = 0.3F;

	public static void render(long millisElapsed) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		if (ridingVehicleCoolDown < RIDING_COOL_DOWN) {
			ridingVehicleCoolDown++;
		} else {
			ridingVehicleId = 0;
		}

		final boolean canRide = !clientPlayerEntity.isSpectator();

		ClientData.instance.vehicles.forEach(vehicle -> {
			final ObjectImmutableList<VehicleCar> vehicleCars = vehicle.vehicleExtraData.getVehicleCars(vehicle.getReversed());
			final ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>> positions = vehicle.getPositions();
			final int totalCars = Math.min(vehicleCars.size(), positions.size());
			final PreviousConnectionPositions previousGangwayConnectionPositions = new PreviousConnectionPositions();
			final PreviousConnectionPositions previousBarrierConnectionPositions = new PreviousConnectionPositions();

			for (int i = 0; i < totalCars; i++) {
				final VehicleCar vehicleCar = vehicleCars.get(i);
				final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList = positions.get(i);
				final RenderVehicleTransformationHelper renderVehicleTransformationHelper = new RenderVehicleTransformationHelper(bogiePositionsList, vehicleCar.getBogie1Position(), vehicleCar.getBogie2Position(), vehicleCar.getLength(), vehicle.getReversed());
				final int carNumber = vehicle.getReversed() ? totalCars - i - 1 : i;
				final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
				final Vector3d playerPosition = renderVehicleTransformationHelper.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);

				CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleCar.getVehicleId(), vehicleResource -> {
					for (int j = 0; j < bogiePositionsList.size(); j++) {
						final RenderVehicleTransformationHelper bogieRenderVehicleTransformationHelper = new RenderVehicleTransformationHelper(bogiePositionsList.get(j), vehicle.getReversed());
						vehicleResource.iterateBogieModels(j, model -> renderModel(bogieRenderVehicleTransformationHelper, storedMatrixTransformations -> model.render(storedMatrixTransformations, vehicle, bogieRenderVehicleTransformationHelper.light, null)));
					}

					renderModel(renderVehicleTransformationHelper, storedMatrixTransformations -> vehicleResource.iterateModels(model -> {
						final ObjectArrayList<Box> openDoorways = vehicle.persistentVehicleData.getDoorValue() > 0 ? model.doorways.stream()
								.filter(doorway -> canOpenDoors(doorway, renderVehicleTransformationHelper))
								.collect(Collectors.toCollection(ObjectArrayList::new)) : new ObjectArrayList<>();
						model.render(storedMatrixTransformations, vehicle, renderVehicleTransformationHelper.light, openDoorways);

						if (canRide) {
							model.floors.forEach(floor -> {
								floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
								renderFloorOrDoorway(floor, true, playerPosition, renderVehicleTransformationHelper);
							});

							openDoorways.forEach(doorway -> {
								floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
								renderFloorOrDoorway(doorway, false, playerPosition, renderVehicleTransformationHelper);
							});

							if (ridingVehicleId == 0 || ridingVehicleId == vehicle.getId()) {
								for (final Box doorway : openDoorways) {
									if (boxContains(doorway, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped())) {
										ridingVehicleX = playerPosition.getXMapped();
										ridingVehicleY = playerPosition.getYMapped();
										ridingVehicleZ = playerPosition.getZMapped();
										ridingVehicleCarNumber = carNumber;
										ridingVehicleId = vehicle.getId();
										break;
									}
								}
							}
						}

						renderConnection(
								model.modelProperties.hasGangway, true, previousGangwayConnectionPositions,
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
								model.modelProperties.hasBarrier, false, previousBarrierConnectionPositions,
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
				});

				if (canRide && ridingVehicleId == vehicle.getId() && ridingVehicleCarNumber == carNumber) {
					ridingVehicleCoolDown = 0;
					clientPlayerEntity.setFallDistanceMapped(0);
					clientPlayerEntity.setVelocity(0, 0, 0);
					clientPlayerEntity.setMovementSpeed(0);

					final float speedMultiplier = millisElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER * (clientPlayerEntity.isSprinting() ? 2 : 1);
					final Vector3d movement = renderVehicleTransformationHelper.transformBackwards(new Vector3d(
							Math.abs(clientPlayerEntity.getSidewaysSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getSidewaysSpeedMapped()) : 0,
							0,
							Math.abs(clientPlayerEntity.getForwardSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getForwardSpeedMapped()) : 0
					), (vector, pitch) -> vector, (vector, yaw) -> vector.rotateY((float) (yaw - Math.toRadians(EntityHelper.getYaw(new Entity(clientPlayerEntity.data))))), (vector, x, y, z) -> vector);
					final ObjectArrayList<Vector3d> offsets = new ObjectArrayList<>();

					clampPosition(floorsAndDoorways, ridingVehicleX + movement.getXMapped() - HALF_PLAYER_WIDTH, ridingVehicleZ + movement.getZMapped() - HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movement.getXMapped() + HALF_PLAYER_WIDTH, ridingVehicleZ + movement.getZMapped() - HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movement.getXMapped() + HALF_PLAYER_WIDTH, ridingVehicleZ + movement.getZMapped() + HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movement.getXMapped() - HALF_PLAYER_WIDTH, ridingVehicleZ + movement.getZMapped() + HALF_PLAYER_WIDTH, offsets);

					if (offsets.isEmpty()) {
						ridingVehicleId = 0;
					} else {
						double clampX = 0;
						double maxY = -Double.MAX_VALUE;
						double clampZ = 0;
						for (final Vector3d offset : offsets) {
							if (Math.abs(offset.getXMapped()) > Math.abs(clampX)) {
								clampX = offset.getXMapped();
							}
							maxY = Math.max(maxY, offset.getYMapped());
							if (Math.abs(offset.getZMapped()) > Math.abs(clampZ)) {
								clampZ = offset.getZMapped();
							}
						}
						ridingVehicleX += movement.getXMapped() + clampX;
						ridingVehicleY = maxY;
						ridingVehicleZ += movement.getZMapped() + clampZ;
					}

					if (InitClient.getGameTick() > 40) {
						final Vector3d newPlayerPosition = renderVehicleTransformationHelper.transformForwards(new Vector3d(ridingVehicleX, ridingVehicleY, ridingVehicleZ), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
						clientPlayerEntity.updatePosition(newPlayerPosition.getXMapped(), newPlayerPosition.getYMapped(), newPlayerPosition.getZMapped());
						InitClient.scheduleMovePlayer(() -> clientPlayerEntity.updatePosition(newPlayerPosition.getXMapped(), newPlayerPosition.getYMapped(), newPlayerPosition.getZMapped()));
					}
				}
			}
		});
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
			boolean shouldRender, boolean canHaveLight, PreviousConnectionPositions previousConnectionPositions,
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

		if (shouldRender) {
			final double halfLength = vehicleLength / 2;
			if (previousConnectionPositions.isValid()) {
				final Vector position1 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(-width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
				final Vector position2 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
				final Vector position3 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(width / 2, height + yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
				final Vector position4 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(width / 2, yOffset + SMALL_OFFSET, zOffset - halfLength), Vector::rotateX, Vector::rotateY, Vector::add);

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

			previousConnectionPositions.position1 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position2 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position3 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(-width / 2, height + yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
			previousConnectionPositions.position4 = renderVehicleTransformationHelper.transformForwardsRaw(new Vector(-width / 2, yOffset + SMALL_OFFSET, -zOffset + halfLength), Vector::rotateX, Vector::rotateY, Vector::add);
		} else {
			previousConnectionPositions.position1 = null;
			previousConnectionPositions.position2 = null;
			previousConnectionPositions.position3 = null;
			previousConnectionPositions.position4 = null;
		}
	}

	private static void renderFloorOrDoorway(Box floorOrDoorway, boolean isFloor, Vector3d playerPosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Vector3d corner1 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMinXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner2 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMaxXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner3 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMaxXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final Vector3d corner4 = renderVehicleTransformationHelper.transformForwards(new Vector3d(floorOrDoorway.getMinXMapped(), floorOrDoorway.getMaxYMapped(), floorOrDoorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final int color = boxContains(floorOrDoorway,
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
			) ? 0xFF00FF00 : isFloor ? ARGB_WHITE : 0xFFFF0000;
			RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> {
				graphicsHolder.drawLineInWorld(
						(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
						(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
						color
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
						(float) (corner3.getXMapped() - offset.getXMapped()), (float) (corner3.getYMapped() - offset.getYMapped()), (float) (corner3.getZMapped() - offset.getZMapped()),
						color
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner3.getXMapped() - offset.getXMapped()), (float) (corner3.getYMapped() - offset.getYMapped()), (float) (corner3.getZMapped() - offset.getZMapped()),
						(float) (corner4.getXMapped() - offset.getXMapped()), (float) (corner4.getYMapped() - offset.getYMapped()), (float) (corner4.getZMapped() - offset.getZMapped()),
						color
				);
				graphicsHolder.drawLineInWorld(
						(float) (corner4.getXMapped() - offset.getXMapped()), (float) (corner4.getYMapped() - offset.getYMapped()), (float) (corner4.getZMapped() - offset.getZMapped()),
						(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
						color
				);
			});
		}
	}

	/**
	 * Find an intersecting floor or doorway from the player position.
	 * If there are multiple intersecting floors or doorways, get the one with the highest Y level.
	 * If there are no intersecting floors or doorways, find the closest floor or doorway instead.
	 */
	@Nullable
	private static ObjectBooleanImmutablePair<Box> bestPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsOrDoorways, double x, double y, double z) {
		return floorsOrDoorways.stream().filter(floorOrDoorway -> boxContains(floorOrDoorway.left(), x, y, z)).max(Comparator.comparingDouble(floorOrDoorway -> floorOrDoorway.left().getMaxYMapped())).orElse(floorsOrDoorways.stream().min(Comparator.comparingDouble(floorOrDoorway -> Math.min(
				Math.abs(floorOrDoorway.left().getMinXMapped() - x),
				Math.abs(floorOrDoorway.left().getMaxXMapped() - x)
		) + Math.min(
				Math.abs(floorOrDoorway.left().getMinYMapped() - y),
				Math.abs(floorOrDoorway.left().getMaxYMapped() - y)
		) + Math.min(
				Math.abs(floorOrDoorway.left().getMinZMapped() - z),
				Math.abs(floorOrDoorway.left().getMaxZMapped() - z)
		))).orElse(null));
	}

	private static boolean boxContains(Box box, double x, double y, double z) {
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

	private static void clampPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways, double x, double z, ObjectArrayList<Vector3d> offsets) {
		final ObjectBooleanImmutablePair<Box> floorOrDoorway = bestPosition(floorsAndDoorways, x, ridingVehicleY, z);

		if (floorOrDoorway != null) {
			if (floorOrDoorway.rightBoolean()) {
				// If the intersecting or closest floor or doorway is a floor, then force the player to be in bounds
				offsets.add(new Vector3d(
						Utilities.clamp(x, floorOrDoorway.left().getMinXMapped(), floorOrDoorway.left().getMaxXMapped()) - x,
						floorOrDoorway.left().getMaxYMapped(),
						Utilities.clamp(z, floorOrDoorway.left().getMinZMapped(), floorOrDoorway.left().getMaxZMapped()) - z
				));
			} else if (boxContains(floorOrDoorway.left(), x, ridingVehicleY, z)) {
				// If the intersecting or closest floor or doorway is a doorway, then don't force the player to be in bounds
				// Dismount if the player is not intersecting the doorway
				offsets.add(new Vector3d(0, floorOrDoorway.left().getMaxYMapped(), 0));
			}
		}
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
