package org.mtr.mod.render;

import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.data.IGui;
import org.mtr.mod.model.ModelLift1;
import org.mtr.mod.model.ModelSmallCube;

public class RenderLifts implements IGui {

	private static final int LIFT_DISPLAY_COLOR = 0xFFFF0000;
	private static final Identifier LIFT_TEXTURE = new Identifier(Init.MOD_ID, "textures/vehicle/lift_1.png");
	private static final ModelSmallCube MODEL_SMALL_CUBE = new ModelSmallCube(new Identifier("textures/block/redstone_block.png"));
	private static final float LIFT_DOOR_VALUE = 0.75F;
	private static final float LIFT_FLOOR_PADDING = 0.25F;

	public static void render(long millisElapsed) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final boolean canRide = !clientPlayerEntity.isSpectator();
		final boolean isHoldingRefresher = clientPlayerEntity.isHolding(Items.LIFT_REFRESHER.get());

		ClientData.getInstance().lifts.forEach(lift -> {
			if (isHoldingRefresher) {
				// Render lift path for debugging
				final LiftFloor[] previousLiftFloor = {null};
				lift.iterateFloors(liftFloor -> {
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(true);
					storedMatrixTransformations.add(graphicsHolder -> {
						final Position position = liftFloor.getPosition();
						graphicsHolder.translate(position.getX(), position.getY(), position.getZ());
					});
					MODEL_SMALL_CUBE.render(storedMatrixTransformations, MAX_LIGHT_GLOWING);

					if (previousLiftFloor[0] != null) {
						final Position position1 = liftFloor.getPosition();
						final Position position2 = previousLiftFloor[0].getPosition();
						RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> graphicsHolder.drawLineInWorld(
								(float) (position1.getX() - offset.getXMapped() + 0.5),
								(float) (position1.getY() - offset.getYMapped() + 0.5),
								(float) (position1.getZ() - offset.getZMapped() + 0.5),
								(float) (position2.getX() - offset.getXMapped() + 0.5),
								(float) (position2.getY() - offset.getYMapped() + 0.5),
								(float) (position2.getZ() - offset.getZMapped() + 0.5),
								ARGB_WHITE
						));
					}

					previousLiftFloor[0] = liftFloor;
				});
			}

			// Calculating vehicle transformations in advance
			final RenderVehicleHelper.VehicleProperties vehicleProperties = RenderVehicleHelper.getTransformedVehiclePropertiesList(lift, ObjectArrayList.of(new RenderVehicleHelper.VehicleProperties(new ObjectObjectImmutablePair<>(
					new VehicleCar("", lift.getDepth(), lift.getWidth(), 0, 0, 0, 0),
					ObjectArrayList.of(getVirtualBogiePositions(lift))
			)))).get(0);
			final RenderVehicleTransformationHelper renderVehicleTransformationHelperAbsolute = vehicleProperties.renderVehicleTransformationHelperAbsolute;
			final RenderVehicleTransformationHelper renderVehicleTransformationHelperOffset = vehicleProperties.renderVehicleTransformationHelperOffset;

			// A temporary list to store all floors and doorways
			final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
			// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
			final ObjectArrayList<Box> openDoorways = new ObjectArrayList<>();

			final Box doorway1 = new Box(-LIFT_DOOR_VALUE, 0, -lift.getDepth() / 2 + LIFT_FLOOR_PADDING, LIFT_DOOR_VALUE, 0, -lift.getDepth() / 2);
			final Box doorway2 = new Box(-LIFT_DOOR_VALUE, 0, lift.getDepth() / 2 - LIFT_FLOOR_PADDING, LIFT_DOOR_VALUE, 0, lift.getDepth() / 2);
			final boolean doorway1Open;
			final boolean doorway2Open;
			if (lift.hasCoolDown()) {
				doorway1Open = RenderVehicleHelper.canOpenDoors(doorway1, renderVehicleTransformationHelperAbsolute, Math.min(lift.getDoorValue(), LIFT_DOOR_VALUE) / 2);
				doorway2Open = lift.getIsDoubleSided() && RenderVehicleHelper.canOpenDoors(doorway2, renderVehicleTransformationHelperAbsolute, Math.min(lift.getDoorValue(), LIFT_DOOR_VALUE) / 2);
				if (doorway1Open) {
					openDoorways.add(doorway1);
				}
				if (doorway2Open) {
					openDoorways.add(doorway2);
				}
			} else {
				doorway1Open = false;
				doorway2Open = false;
			}

			if (canRide) {
				// Player position relative to the car
				final Vector3d playerPosition = renderVehicleTransformationHelperAbsolute.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
				// Check and mount player
				VehicleRidingMovement.startRiding(openDoorways, 0, lift.getId(), 0, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped(), renderVehicleTransformationHelperAbsolute.yaw);

				final Box floor = new Box(-lift.getWidth() / 2 + LIFT_FLOOR_PADDING, 0, -lift.getDepth() / 2 + LIFT_FLOOR_PADDING, lift.getWidth() / 2 - LIFT_FLOOR_PADDING, 0, lift.getDepth() / 2 - LIFT_FLOOR_PADDING);
				floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
				RenderVehicleHelper.renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderVehicleTransformationHelperOffset);

				openDoorways.forEach(doorway -> {
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
					RenderVehicleHelper.renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderVehicleTransformationHelperOffset);
				});
			}

			// Render the lift
			RenderVehicleHelper.renderModel(renderVehicleTransformationHelperOffset, storedMatrixTransformations -> {
				new ModelLift1((int) Math.round(lift.getHeight() * 2), (int) Math.round(lift.getWidth()), (int) Math.round(lift.getDepth()), lift.getIsDoubleSided()).render(
						storedMatrixTransformations,
						null,
						LIFT_TEXTURE,
						renderVehicleTransformationHelperOffset.light,
						doorway1Open ? lift.getDoorValue() / LIFT_DOOR_VALUE : 0, doorway2Open ? lift.getDoorValue() / LIFT_DOOR_VALUE : 0, false,
						0, 1, true, true, false, true, false
				);

				// Render the display inside the lift
				for (int i = 0; i < (lift.getIsDoubleSided() ? 2 : 1); i++) {
					final boolean shouldRotate = i == 0;
					final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
					storedMatrixTransformationsNew.add(graphicsHolder -> {
						if (shouldRotate) {
							graphicsHolder.rotateYDegrees(180);
						}
						graphicsHolder.translate(0.875F, -1.5, lift.getDepth() / 2 - 0.25 - SMALL_OFFSET);
					});
					renderLiftDisplay(storedMatrixTransformationsNew, new World(clientWorld.data), lift, 0.1875F, 0.3125F);
				}
			});

			if (canRide) {
				// Main logic for player movement inside the car
				VehicleRidingMovement.movePlayer(
						millisElapsed, lift.getId(), 0,
						floorsAndDoorways,
						null, null, null,
						renderVehicleTransformationHelperAbsolute
				);
			}
		});
	}

	public static void renderLiftDisplay(StoredMatrixTransformations storedMatrixTransformations, World world, Lift lift, float width, float height) {
		final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = getLiftDetails(world, lift, Init.positionToBlockPos(lift.getCurrentFloor().getPosition()));
		final LiftDirection liftDirection = liftDetails.left();

		RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawStringWithFont(graphicsHolder, liftDetails.right().left(), IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, width, -1, 18 / width, LIFT_DISPLAY_COLOR, false, MAX_LIGHT_GLOWING, null);
			graphicsHolder.pop();
		});

		if (liftDirection != LiftDirection.NONE) {
			RenderTrains.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/lift_arrow.png"), false, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -width / 6, 0, width / 3, width / 3, 0, liftDirection == LiftDirection.UP ? 0 : 1, 1, liftDirection == LiftDirection.UP ? 1 : 0, Direction.UP, LIFT_DISPLAY_COLOR, MAX_LIGHT_GLOWING);
				graphicsHolder.pop();
			});
		}
	}

	public static ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> getLiftDetails(World world, Lift lift, BlockPos blockPos) {
		final LiftFloor liftFloor = lift.getCurrentFloor();
		final BlockEntity floorEntity = world.getBlockEntity(blockPos);
		final String floorNumber;
		final String floorDescription;

		if (floorEntity != null && floorEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			floorNumber = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorNumber();
			floorDescription = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorDescription();
		} else {
			floorNumber = liftFloor.getNumber();
			floorDescription = liftFloor.getDescription();
		}

		return new ObjectObjectImmutablePair<>(lift.getDirection(), new ObjectObjectImmutablePair<>(floorNumber, floorDescription));
	}

	private static ObjectObjectImmutablePair<Vector, Vector> getVirtualBogiePositions(Lift lift) {
		final Vector position = lift.getPosition();
		final Angle angle = lift.getAngle();
		final double x = position.x + lift.getOffsetX();
		final double y = position.y + lift.getOffsetY();
		final double z = position.z + lift.getOffsetZ();
		return new ObjectObjectImmutablePair<>(
				new Vector(x + angle.cos, y, z + angle.sin),
				new Vector(x - angle.cos, y, z - angle.sin)
		);
	}
}
