package org.mtr.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.client.VehicleRidingMovement;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Position;
import org.mtr.core.tool.Vector;
import org.mtr.data.IGui;
import org.mtr.item.ItemLiftRefresher;
import org.mtr.registry.Items;
import org.mtr.resource.LiftResource;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RenderLifts implements IGui {

	private static final int LIFT_DISPLAY_COLOR = 0xFFFF0000;
	private static final float LIFT_DOOR_VALUE = 0.75F;
	private static final float LIFT_FLOOR_PADDING = 0.25F;

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
					MainRenderer.scheduleRender(Identifier.of("textures/block/redstone_block.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						RenderPSDAPGDoor.MODEL_SMALL_CUBE.render(matrixStack, vertexConsumer, DEFAULT_LIGHT, OverlayTexture.DEFAULT_UV);
						matrixStack.pop();
					});

					if (previousLiftFloor[0] != null) {
						final Position position1 = liftFloor.getPosition();
						final Position position2 = previousLiftFloor[0].getPosition();
						MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack, vertexConsumer, offset) -> {
							final ObjectArrayList<Vector> trackPositions = ItemLiftRefresher.findPath(clientWorld, position1, position2);
							for (int i = 1; i < trackPositions.size(); i++) {
								IDrawing.drawLineInWorld(
										matrixStack,
										vertexConsumer,
										(float) (trackPositions.get(i - 1).x() - offset.x + 0.5),
										(float) (trackPositions.get(i - 1).y() - offset.y + 0.5),
										(float) (trackPositions.get(i - 1).z() - offset.z + 0.5),
										(float) (trackPositions.get(i).x() - offset.x + 0.5),
										(float) (trackPositions.get(i).y() - offset.y + 0.5),
										(float) (trackPositions.get(i).z() - offset.z + 0.5),
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
				final boolean shouldRender = occlusionCullingInstance.isAABBVisible(new com.logisticscraft.occlusionculling.util.Vec3d(
						absolutePositionAndRotation.position.x() - longestDimension,
						absolutePositionAndRotation.position.y() - longestDimension,
						absolutePositionAndRotation.position.z() - longestDimension
				), new com.logisticscraft.occlusionculling.util.Vec3d(
						absolutePositionAndRotation.position.x() + longestDimension,
						absolutePositionAndRotation.position.y() + longestDimension,
						absolutePositionAndRotation.position.z() + longestDimension
				), camera);
				return () -> liftWrapper.shouldRender = shouldRender;
			});

			if (liftWrapper.shouldRender) {
				// Riding offset
				final IntObjectImmutablePair<ObjectObjectImmutablePair<Vec3d, Double>> ridingVehicleCarNumberAndOffset = VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(lift.getId());
				final PositionAndRotation ridingCarPositionAndRotation;
				final Vec3d offsetVector;
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
					final Vec3d playerPosition = absolutePositionAndRotation.transformBackwards(clientPlayerEntity.getPos(), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
					// Check and mount player
					VehicleRidingMovement.startRiding(openDoorways, 0, 0, lift.getId(), 0, playerPosition.x, playerPosition.y, playerPosition.z, absolutePositionAndRotation.yaw);

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
//				new ModelLift1((int) Math.round(lift.getHeight() * 2), (int) Math.round(lift.getWidth()), (int) Math.round(lift.getDepth()), lift.getIsDoubleSided()).render(
//						storedMatrixTransformations,
//						null,
//						getLiftResource(lift.getStyle()).getTexture(),
//						absolutePositionAndRotation.light,
//						doorway1Open ? lift.getDoorValue() / LIFT_DOOR_VALUE : 0, doorway2Open ? lift.getDoorValue() / LIFT_DOOR_VALUE : 0, false,
//						0, 1, true, true, false, true, false
//				);

				// Render the display inside the lift
				for (int i = 0; i < (lift.getIsDoubleSided() ? 2 : 1); i++) {
					final boolean shouldRotate = i == 0;
					final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
					storedMatrixTransformationsNew.add(matrixStack -> {
						if (shouldRotate) {
							IDrawing.rotateYDegrees(matrixStack, 180);
						}
						matrixStack.translate(0.875F, -1.5, lift.getDepth() / 2 - 0.25 - SMALL_OFFSET);
					});
					renderLiftDisplay(storedMatrixTransformationsNew, clientWorld, lift, 0.1875F, 0.3125F);
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

		MainRenderer.WORKER_THREAD.scheduleLifts(occlusionCullingInstance -> {
			final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
			cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
			minecraftClient.execute(() -> tasks.forEach(Runnable::run));
		});
	}

	public static void renderLiftDisplay(StoredMatrixTransformations storedMatrixTransformations, World world, Lift lift, float width, float height) {
		final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = getLiftDetails(world, lift, MTR.positionToBlockPos(lift.getCurrentFloor().getPosition()));
		final LiftDirection liftDirection = liftDetails.left();

		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
//			IDrawing.drawStringWithFont(matrixStack, vertexConsumer, liftDetails.right().left(), IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, width, -1, 18 / width, LIFT_DISPLAY_COLOR, false, DEFAULT_LIGHT, null);
			matrixStack.pop();
		});

		if (liftDirection != LiftDirection.NONE) {
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/sign/lift_arrow.png"), false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -width / 6, 0, width / 3, width / 3, 0, liftDirection == LiftDirection.UP ? 0 : 1, 1, liftDirection == LiftDirection.UP ? 1 : 0, Direction.UP, LIFT_DISPLAY_COLOR, DEFAULT_LIGHT);
				matrixStack.pop();
			});
		}
	}

	public static ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> getLiftDetails(World world, Lift lift, BlockPos blockPos) {
		final LiftFloor liftFloor = lift.getCurrentFloor();
		final BlockEntity floorEntity = world.getBlockEntity(blockPos);
		final String floorNumber;
		final String floorDescription;

		if (floorEntity instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity liftTrackFloorBlockEntity) {
			floorNumber = liftTrackFloorBlockEntity.getFloorNumber();
			floorDescription = liftTrackFloorBlockEntity.getFloorDescription();
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
		final Vector position = lift.getPosition((floorPosition1, floorPosition2) -> ItemLiftRefresher.findPath(clientWorld, floorPosition1, floorPosition2));
		return new PositionAndRotation(new Vector(
				position.x() + lift.getOffsetX(),
				position.y() + lift.getOffsetY(),
				position.z() + lift.getOffsetZ()
		), -Math.PI / 2 - lift.getAngle().angleRadians, 0);
	}
}
