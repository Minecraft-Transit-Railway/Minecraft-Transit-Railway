package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Position;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemLiftRefresher;
import org.mtr.mod.model.ModelLift1;
import org.mtr.mod.model.ModelSmallCube;
import org.mtr.mod.resource.LiftResource;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RenderLifts implements IGui {

	private static final int LIFT_DISPLAY_COLOR = 0xFFFF0000;
	private static final ModelSmallCube MODEL_SMALL_CUBE = new ModelSmallCube(new Identifier("textures/block/redstone_block.png"));
	private static final float LIFT_DOOR_VALUE = 0.75F;
	private static final float LIFT_FLOOR_PADDING = 0.25F;

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

		final boolean canRide = !clientPlayerEntity.isSpectator();
		final boolean isHoldingRefresher = clientPlayerEntity.isHolding(Items.LIFT_REFRESHER.get());

		MinecraftClientData.getInstance().liftWrapperList.values().forEach(liftWrapper -> {
			final Lift lift = liftWrapper.getLift();

			if (isHoldingRefresher) {
				// Render lift path for debugging
				final LiftFloor[] previousLiftFloor = {null};
				lift.iterateFloors(liftFloor -> {
					final Position position = liftFloor.getPosition();
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(position.getX(), position.getY(), position.getZ());
					MODEL_SMALL_CUBE.render(storedMatrixTransformations, GraphicsHolder.getDefaultLight());

					if (previousLiftFloor[0] != null) {
						final Position position1 = liftFloor.getPosition();
						final Position position2 = previousLiftFloor[0].getPosition();
						MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (graphicsHolder, offset) -> {
							final ObjectArrayList<Vector> trackPositions = ItemLiftRefresher.findPath(new World(clientWorld.data), position1, position2);
							for (int i = 1; i < trackPositions.size(); i++) {
								graphicsHolder.drawLineInWorld(
										(float) (trackPositions.get(i - 1).x - offset.getXMapped() + 0.5),
										(float) (trackPositions.get(i - 1).y - offset.getYMapped() + 0.5),
										(float) (trackPositions.get(i - 1).z - offset.getZMapped() + 0.5),
										(float) (trackPositions.get(i).x - offset.getXMapped() + 0.5),
										(float) (trackPositions.get(i).y - offset.getYMapped() + 0.5),
										(float) (trackPositions.get(i).z - offset.getZMapped() + 0.5),
										ARGB_WHITE
								);
							}
						});
					}

					previousLiftFloor[0] = liftFloor;
				});
			}

			// Calculating vehicle transformations in advance
			final PositionAndRotation absolutePositionAndRotation = getLiftPositionAndRotation(clientWorld, lift);
			cullingTasks.add(occlusionCullingInstance -> {
				final double longestDimension = Math.max(lift.getHeight(), Math.max(lift.getWidth(), lift.getDepth()));
				final boolean shouldRender = occlusionCullingInstance.isAABBVisible(new Vec3d(
						absolutePositionAndRotation.position.x - longestDimension,
						absolutePositionAndRotation.position.y - longestDimension,
						absolutePositionAndRotation.position.z - longestDimension
				), new Vec3d(
						absolutePositionAndRotation.position.x + longestDimension,
						absolutePositionAndRotation.position.y + longestDimension,
						absolutePositionAndRotation.position.z + longestDimension
				), camera);
				return () -> liftWrapper.shouldRender = shouldRender;
			});

			if (liftWrapper.shouldRender) {
				// Riding offset
				final IntObjectImmutablePair<ObjectObjectImmutablePair<Vector3d, Double>> ridingVehicleCarNumberAndOffset = VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(lift.getId());
				final PositionAndRotation ridingCarPositionAndRotation;
				final Vector3d offsetVector;
				final Double offsetRotation;
				if (ridingVehicleCarNumberAndOffset == null) {
					ridingCarPositionAndRotation = null;
					offsetVector = null;
					offsetRotation = null;
				} else {
					ridingCarPositionAndRotation = absolutePositionAndRotation;
					offsetVector = ridingVehicleCarNumberAndOffset.right().left();
					offsetRotation = ridingVehicleCarNumberAndOffset.right().right();
				}

				final PositionAndRotation renderingPositionAndRotation = RenderVehicles.getRenderPositionAndRotation(offsetVector, offsetRotation, ridingCarPositionAndRotation, absolutePositionAndRotation, cameraShakeOffset);

				// A temporary list to store all floors and doorways
				final ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways = new ObjectArrayList<>();
				// Find open doorways (close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates)
				final ObjectArrayList<Box> openDoorways = new ObjectArrayList<>();

				final Box doorway1 = new Box(-LIFT_DOOR_VALUE, 0, -lift.getDepth() / 2 + LIFT_FLOOR_PADDING, LIFT_DOOR_VALUE, 0, -lift.getDepth() / 2);
				final Box doorway2 = new Box(-LIFT_DOOR_VALUE, 0, lift.getDepth() / 2 - LIFT_FLOOR_PADDING, LIFT_DOOR_VALUE, 0, lift.getDepth() / 2);
				final boolean doorway1Open;
				final boolean doorway2Open;
				if (lift.hasCoolDown()) {
					doorway1Open = RenderVehicleHelper.canOpenDoors(doorway1, absolutePositionAndRotation, Math.min(lift.getDoorValue(), LIFT_DOOR_VALUE));
					doorway2Open = lift.getIsDoubleSided() && RenderVehicleHelper.canOpenDoors(doorway2, absolutePositionAndRotation, Math.min(lift.getDoorValue(), LIFT_DOOR_VALUE));
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
					final Vector3d playerPosition = absolutePositionAndRotation.transformBackwards(clientPlayerEntity.getPos(), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
					// Check and mount player
					VehicleRidingMovement.startRiding(openDoorways, 0, 0, lift.getId(), 0, playerPosition.getXMapped(), playerPosition.getYMapped(), playerPosition.getZMapped(), absolutePositionAndRotation.yaw);

					final Box floor = new Box(-lift.getWidth() / 2 + LIFT_FLOOR_PADDING, 0, -lift.getDepth() / 2 + LIFT_FLOOR_PADDING, lift.getWidth() / 2 - LIFT_FLOOR_PADDING, 0, lift.getDepth() / 2 - LIFT_FLOOR_PADDING);
					floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(floor, true));
					RenderVehicleHelper.renderFloorOrDoorway(floor, ARGB_WHITE, playerPosition, renderingPositionAndRotation, offsetVector == null);

					openDoorways.forEach(doorway -> {
						floorsAndDoorways.add(new ObjectBooleanImmutablePair<>(doorway, false));
						RenderVehicleHelper.renderFloorOrDoorway(doorway, 0xFFFF0000, playerPosition, renderingPositionAndRotation, offsetVector == null);
					});
				}

				// Render the lift
				final StoredMatrixTransformations storedMatrixTransformations = RenderVehicles.getStoredMatrixTransformations(offsetVector == null, renderingPositionAndRotation, 0);
				new ModelLift1((int) Math.round(lift.getHeight() * 2), (int) Math.round(lift.getWidth()), (int) Math.round(lift.getDepth()), lift.getIsDoubleSided()).render(
						storedMatrixTransformations,
						null,
						getLiftResource(lift.getStyle()).getTexture(),
						absolutePositionAndRotation.light,
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

				if (canRide) {
					// Main logic for player movement inside the car
					VehicleRidingMovement.movePlayer(
							millisElapsed, lift.getId(), 0,
							floorsAndDoorways,
							null, null, null,
							absolutePositionAndRotation
					);
				}
			}
		});

		if (!OptimizedRenderer.renderingShadows()) {
			MainRenderer.WORKER_THREAD.scheduleLifts(occlusionCullingInstance -> {
				final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
				cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
				minecraftClient.execute(() -> tasks.forEach(Runnable::run));
			});
		}
	}

	public static void renderLiftDisplay(StoredMatrixTransformations storedMatrixTransformations, World world, Lift lift, float width, float height) {
		final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = getLiftDetails(world, lift, Init.positionToBlockPos(lift.getCurrentFloor().getPosition()));
		final LiftDirection liftDirection = liftDetails.left();

		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawStringWithFont(graphicsHolder, liftDetails.right().left(), IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, width, -1, 18 / width, LIFT_DISPLAY_COLOR, false, GraphicsHolder.getDefaultLight(), null);
			graphicsHolder.pop();
		});

		if (liftDirection != LiftDirection.NONE) {
			MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/lift_arrow.png"), false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -width / 6, 0, width / 3, width / 3, 0, liftDirection == LiftDirection.UP ? 0 : 1, 1, liftDirection == LiftDirection.UP ? 1 : 0, Direction.UP, LIFT_DISPLAY_COLOR, GraphicsHolder.getDefaultLight());
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

	public static LiftResource getLiftResource(@Nullable String liftId) {
		final LiftResource liftResource;

		if (liftId == null) {
			liftResource = null;
		} else {
			final LiftResource[] tempLiftResource = {null};
			CustomResourceLoader.getLiftById(liftId, newLiftResource -> tempLiftResource[0] = newLiftResource);
			liftResource = tempLiftResource[0];
		}

		return liftResource == null ? CustomResourceLoader.getLifts().get(0) : liftResource;
	}

	private static PositionAndRotation getLiftPositionAndRotation(ClientWorld clientWorld, Lift lift) {
		final Vector position = lift.getPosition((floorPosition1, floorPosition2) -> ItemLiftRefresher.findPath(new World(clientWorld.data), floorPosition1, floorPosition2));
		return new PositionAndRotation(new Vector(
				position.x + lift.getOffsetX(),
				position.y + lift.getOffsetY(),
				position.z + lift.getOffsetZ()
		), -Math.PI / 2 - lift.getAngle().angleRadians, 0);
	}
}
