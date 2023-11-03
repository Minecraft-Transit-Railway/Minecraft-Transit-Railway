package org.mtr.mod.render;

import org.mtr.core.data.VehicleCar;
import org.mtr.core.tools.Utilities;
import org.mtr.core.tools.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.client.*;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RenderVehicles implements IGui {

	public static final float HALF_PLAYER_WIDTH = 0.3F;
	private static final int CHECK_DOOR_RADIUS_XZ = 1;
	private static final int CHECK_DOOR_RADIUS_Y = 2;
	private static final int RIDE_STEP_THRESHOLD = 8;

	public static void render(long millisElapsed) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		// When riding a moving vehicle, the client movement is always out of sync with the vehicle rendering. This produces annoying shaking effects.
		// Offsets are used to render the vehicle with respect to the player position rather than the absolute world position, eliminating shaking.

		// Tick the riding cool down (dismount player if they are no longer riding a vehicle) and store the player offset cache
		VehicleRidingMovement.tick();
		final boolean canRide = !clientPlayerEntity.isSpectator();

		// Iterate all vehicles that the client knows about
		ClientData.instance.vehicles.forEach(vehicle -> {
			// Used to store gangway or barrier connection positions of the previously iterated car
			final ObjectArrayList<PreviousConnectionPositions> previousGangwayPositionsList = new ObjectArrayList<>();
			final ObjectArrayList<PreviousConnectionPositions> previousBarrierPositionsList = new ObjectArrayList<>();
			final PreviousGangwayMovementPositions previousGangwayMovementPositions = new PreviousGangwayMovementPositions();
			// Calculating vehicle transformations in advance
			final ObjectArrayList<VehicleProperties> vehiclePropertiesList = vehicle.getVehicleCarsAndPositions().stream().map(VehicleProperties::new).collect(Collectors.toCollection(ObjectArrayList::new));
			final Vector3d ridingPerspectiveOffset = getRidingPerspectiveOffset(vehicle, vehiclePropertiesList);

			// Iterate all cars of a vehicle
			iterateWithIndex(vehiclePropertiesList, (carNumber, vehicleProperties) -> CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleProperties.vehicleCar.getVehicleId(), vehicleResource -> {
				final RenderVehicleTransformationHelper renderVehicleTransformationHelper = vehicleProperties.renderVehicleTransformationHelper;
				// Calculate the relative offset of the vehicle car with respect to the player position
				final Vector3d ridingPerspectiveCarOffset = ridingPerspectiveOffset == null ? null : new Vector3d(renderVehicleTransformationHelper.pivotPosition.x, renderVehicleTransformationHelper.pivotPosition.y, renderVehicleTransformationHelper.pivotPosition.z).subtract(ridingPerspectiveOffset);

				// Render each bogie of the car
				for (int bogieIndex = 0; bogieIndex < vehicleProperties.bogiePositionsList.size(); bogieIndex++) {
					final RenderVehicleTransformationHelper bogieRenderVehicleTransformationHelper = new RenderVehicleTransformationHelper(vehicleProperties.bogiePositionsList.get(bogieIndex));
					vehicleResource.iterateBogieModels(bogieIndex, model -> renderModel(bogieRenderVehicleTransformationHelper, ridingPerspectiveCarOffset, storedMatrixTransformations -> model.render(storedMatrixTransformations, vehicle, bogieRenderVehicleTransformationHelper.light, null)));
				}

				// Player position relative to the car
				final Vector3d playerPosition = renderVehicleTransformationHelper.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
				// A temporary list to store all floors and doorways
				final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
				// Extra floors to be used to define where the gangways are
				final GangwayMovementPositions gangwayMovementPositions1 = new GangwayMovementPositions(renderVehicleTransformationHelper, false);
				final GangwayMovementPositions gangwayMovementPositions2 = new GangwayMovementPositions(renderVehicleTransformationHelper, true);
				// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
				final ObjectArrayList<Box> openDoorways = vehicle.isMoving() ? new ObjectArrayList<>() : vehicleResource.doorways.stream().filter(doorway -> canOpenDoors(doorway, renderVehicleTransformationHelper, vehicle.persistentVehicleData.getDoorValue())).collect(Collectors.toCollection(ObjectArrayList::new));

				if (canRide) {
					vehicleResource.floors.forEach(floor -> {
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
						renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderVehicleTransformationHelper, ridingPerspectiveOffset);
						// Find the floors with the lowest and highest Z values to be used to define where the gangways are
						gangwayMovementPositions1.check(floor);
						gangwayMovementPositions2.check(floor);
					});

					openDoorways.forEach(doorway -> {
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
						renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderVehicleTransformationHelper, ridingPerspectiveOffset);
					});

					// Check and mount player
					VehicleRidingMovement.startRiding(openDoorways, vehicle.getId(), carNumber, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped());
				}

				// Each car can have more than one model defined
				renderModel(renderVehicleTransformationHelper, ridingPerspectiveCarOffset, storedMatrixTransformations -> vehicleResource.iterateModels((modelIndex, model) -> {
					model.render(storedMatrixTransformations, vehicle, renderVehicleTransformationHelper.light, openDoorways);

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
							renderVehicleTransformationHelper,
							ridingPerspectiveOffset,
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
							renderVehicleTransformationHelper,
							ridingPerspectiveOffset,
							vehicleProperties.vehicleCar.getLength(),
							model.modelProperties.getBarrierWidth(),
							model.modelProperties.getBarrierHeight(),
							model.modelProperties.getBarrierYOffset(),
							model.modelProperties.getBarrierZOffset(),
							vehicle.getIsOnRoute()
					);
				}));

				// If the vehicle has gangways, add extra floors to define where the gangways are
				if (vehicleResource.hasGangway1()) {
					final Box gangwayConnectionFloor1 = gangwayMovementPositions1.getBox();
					renderFloorOrDoorway(gangwayConnectionFloor1, 0xFF0000FF, playerPosition, renderVehicleTransformationHelper, ridingPerspectiveOffset);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(gangwayConnectionFloor1, true));
				}
				if (vehicleResource.hasGangway2()) {
					final Box gangwayConnectionFloor2 = gangwayMovementPositions2.getBox();
					renderFloorOrDoorway(gangwayConnectionFloor2, 0xFF0000FF, playerPosition, renderVehicleTransformationHelper, ridingPerspectiveOffset);
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

	/**
	 * @return whether the doorway is close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates
	 */
	private static boolean canOpenDoors(Box doorway, RenderVehicleTransformationHelper renderVehicleTransformationHelper, double doorValue) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return false;
		}

		final Vector3d doorwayPosition1 = renderVehicleTransformationHelper.transformForwards(new Vector3d(doorway.getMinXMapped(), doorway.getMaxYMapped(), doorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
		final Vector3d doorwayPosition2 = renderVehicleTransformationHelper.transformForwards(new Vector3d(doorway.getMaxXMapped(), doorway.getMaxYMapped(), doorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
		final Vector3d doorwayPosition3 = renderVehicleTransformationHelper.transformForwards(new Vector3d(doorway.getMaxXMapped(), doorway.getMaxYMapped(), doorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
		final Vector3d doorwayPosition4 = renderVehicleTransformationHelper.transformForwards(new Vector3d(doorway.getMinXMapped(), doorway.getMaxYMapped(), doorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
		final double minX = Math.min(Math.min(doorwayPosition1.getXMapped(), doorwayPosition2.getXMapped()), Math.min(doorwayPosition3.getXMapped(), doorwayPosition4.getXMapped()));
		final double maxX = Math.max(Math.max(doorwayPosition1.getXMapped(), doorwayPosition2.getXMapped()), Math.max(doorwayPosition3.getXMapped(), doorwayPosition4.getXMapped()));
		final double minY = Math.min(Math.min(doorwayPosition1.getYMapped(), doorwayPosition2.getYMapped()), Math.min(doorwayPosition3.getYMapped(), doorwayPosition4.getYMapped()));
		final double maxY = Math.max(Math.max(doorwayPosition1.getYMapped(), doorwayPosition2.getYMapped()), Math.max(doorwayPosition3.getYMapped(), doorwayPosition4.getYMapped()));
		final double minZ = Math.min(Math.min(doorwayPosition1.getZMapped(), doorwayPosition2.getZMapped()), Math.min(doorwayPosition3.getZMapped(), doorwayPosition4.getZMapped()));
		final double maxZ = Math.max(Math.max(doorwayPosition1.getZMapped(), doorwayPosition2.getZMapped()), Math.max(doorwayPosition3.getZMapped(), doorwayPosition4.getZMapped()));
		boolean canOpenDoors = false;

		for (double checkX = minX - CHECK_DOOR_RADIUS_XZ; checkX <= maxX + CHECK_DOOR_RADIUS_XZ; checkX++) {
			for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
				for (double checkZ = minZ - CHECK_DOOR_RADIUS_XZ; checkZ <= maxZ + CHECK_DOOR_RADIUS_XZ; checkZ++) {
					final BlockPos checkPos = Init.newBlockPos(checkX, checkY, checkZ);
					final BlockState blockState = clientWorld.getBlockState(checkPos);
					final Block block = blockState.getBlock();
					if (block.data instanceof BlockPlatform) {
						canOpenDoors = true;
					} else if (block.data instanceof BlockPSDAPGDoorBase && blockState.get(new Property<>(BlockPSDAPGDoorBase.UNLOCKED.data))) {
						canOpenDoors = true;
						final BlockEntity blockEntity = clientWorld.getBlockEntity(checkPos);
						if (blockEntity != null && blockEntity.data instanceof BlockPSDAPGDoorBase.BlockEntityBase) {
							((BlockPSDAPGDoorBase.BlockEntityBase) blockEntity.data).open(doorValue);
						}
					}
				}
			}
		}

		return canOpenDoors;
	}

	private static void renderModel(RenderVehicleTransformationHelper renderVehicleTransformationHelper, @Nullable Vector3d ridingPerspectiveCarOffset, Consumer<StoredMatrixTransformations> render) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(ridingPerspectiveCarOffset == null);
		storedMatrixTransformations.add(graphicsHolder -> renderVehicleTransformationHelper.transformBackwards(new Object(), (object, pitch) -> {
			graphicsHolder.rotateXRadians((float) (Math.PI - pitch)); // Blockbench exports models upside down
			return new Object();
		}, (object, yaw) -> {
			graphicsHolder.rotateYRadians((float) (Math.PI - yaw));
			return new Object();
		}, (object, x, y, z) -> {
			if (ridingPerspectiveCarOffset == null) {
				graphicsHolder.translate(-x, -y, -z);
			} else {
				graphicsHolder.translate(ridingPerspectiveCarOffset.getXMapped(), ridingPerspectiveCarOffset.getYMapped(), ridingPerspectiveCarOffset.getZMapped());
			}
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
			@Nullable Vector3d ridingPerspectiveOffset,
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
				drawTexture(graphicsHolder, position2, position7, position8, position1, ridingPerspectiveOffset, offset, lightConnection);
				drawTexture(graphicsHolder, position6, position3, position4, position5, ridingPerspectiveOffset, offset, lightConnection);
			});

			RenderTrains.scheduleRender(outerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Top
				drawTexture(graphicsHolder, position3, position6, position7, position2, ridingPerspectiveOffset, offset, lightConnection);
			});

			RenderTrains.scheduleRender(outerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Bottom
				drawTexture(graphicsHolder, position1, position8, position5, position4, ridingPerspectiveOffset, offset, lightConnection);
			});

			RenderTrains.scheduleRender(innerSideTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Sides
				drawTexture(graphicsHolder, position7, position2, position1, position8, ridingPerspectiveOffset, offset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
				drawTexture(graphicsHolder, position3, position6, position5, position4, ridingPerspectiveOffset, offset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
			});

			RenderTrains.scheduleRender(innerTopTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Top
				drawTexture(graphicsHolder, position6, position3, position2, position7, ridingPerspectiveOffset, offset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
			});

			RenderTrains.scheduleRender(innerBottomTexture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				// Bottom
				drawTexture(graphicsHolder, position8, position1, position4, position5, ridingPerspectiveOffset, offset, canHaveLight && isOnRoute ? MAX_LIGHT_GLOWING : lightConnection);
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

	private static void renderFloorOrDoorway(Box floorOrDoorway, int color, Vector3d playerPosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper, @Nullable Vector3d ridingPerspectiveOffset) {
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
				drawLine(graphicsHolder, corner1, corner2, ridingPerspectiveOffset, offset, newColor);
				drawLine(graphicsHolder, corner2, corner3, ridingPerspectiveOffset, offset, newColor);
				drawLine(graphicsHolder, corner3, corner4, ridingPerspectiveOffset, offset, newColor);
				drawLine(graphicsHolder, corner4, corner1, ridingPerspectiveOffset, offset, newColor);
			});
		}
	}

	/**
	 * @return the absolute world position of the player or {@code null} if the player is not riding a vehicle
	 */
	@Nullable
	private static Vector3d getRidingPerspectiveOffset(VehicleExtension vehicle, ObjectArrayList<VehicleProperties> vehiclePropertiesList) {
		if (vehicle.isMoving()) {
			final IntObjectImmutablePair<Vector3d> ridingVehicleCarNumberAndPosition = VehicleRidingMovement.getRidingVehicleCarNumberAndPosition(vehicle.getId());
			if (ridingVehicleCarNumberAndPosition != null) {
				final VehicleProperties vehicleProperties = vehiclePropertiesList.get(ridingVehicleCarNumberAndPosition.leftInt());
				final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
				if (vehicleProperties != null && clientPlayerEntity != null) {
					return vehicleProperties.renderVehicleTransformationHelper.transformForwards(ridingVehicleCarNumberAndPosition.right().add(0, clientPlayerEntity.getStandingEyeHeight(), 0), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
				}
			}
		}

		return null;
	}

	private static void drawTexture(GraphicsHolder graphicsHolder, Vector position1, Vector position2, Vector position3, Vector position4, @Nullable Vector3d ridingPerspectiveOffset, Vector3d defaultOffset, int light) {
		final double offsetX = ridingPerspectiveOffset == null ? 0 : ridingPerspectiveOffset.getXMapped();
		final double offsetY = ridingPerspectiveOffset == null ? 0 : ridingPerspectiveOffset.getYMapped();
		final double offsetZ = ridingPerspectiveOffset == null ? 0 : ridingPerspectiveOffset.getZMapped();
		IDrawing.drawTexture(
				graphicsHolder,
				position1.x - offsetX, position1.y - offsetY, position1.z - offsetZ,
				position2.x - offsetX, position2.y - offsetY, position2.z - offsetZ,
				position3.x - offsetX, position3.y - offsetY, position3.z - offsetZ,
				position4.x - offsetX, position4.y - offsetY, position4.z - offsetZ,
				ridingPerspectiveOffset == null ? defaultOffset : Vector3d.getZeroMapped(), 0, 0, 1, 1, Direction.UP, ARGB_WHITE, light
		);
	}

	private static void drawLine(GraphicsHolder graphicsHolder, Vector3d corner1, Vector3d corner2, @Nullable Vector3d ridingPerspectiveOffset, Vector3d defaultOffset, int color) {
		final Vector3d offset = ridingPerspectiveOffset == null ? defaultOffset : ridingPerspectiveOffset;
		graphicsHolder.drawLineInWorld(
				(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
				(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
				color
		);
	}

	private static <T> void iterateWithIndex(ObjectArrayList<T> list, IndexedConsumer<T> indexedConsumer) {
		for (int i = 0; i < list.size(); i++) {
			indexedConsumer.accept(i, list.get(i));
		}
	}

	private static class VehicleProperties {

		private final VehicleCar vehicleCar;
		private final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList;
		private final RenderVehicleTransformationHelper renderVehicleTransformationHelper;

		private VehicleProperties(ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>> vehicleCarAndPosition) {
			vehicleCar = vehicleCarAndPosition.left();
			bogiePositionsList = vehicleCarAndPosition.right();
			renderVehicleTransformationHelper = new RenderVehicleTransformationHelper(bogiePositionsList, vehicleCar.getBogie1Position(), vehicleCar.getBogie2Position(), vehicleCar.getLength());
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
