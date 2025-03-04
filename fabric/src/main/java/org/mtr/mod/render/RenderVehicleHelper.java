package org.mtr.mod.render;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.PlatformHelper;
import org.mtr.mod.client.VehicleRidingMovement;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RenderVehicleHelper {

	public static final float HALF_PLAYER_WIDTH = 0.3F;
	private static final int CHECK_DOOR_RADIUS_XZ_VEHICLE = 2;
	private static final int CHECK_DOOR_RADIUS_XZ_LIFT = 1;
	private static final int CHECK_DOOR_RADIUS_Y = 2;
	private static final double RIDE_STEP_THRESHOLD = 0.75;

	/**
	 * @return whether the doorway is close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates
	 */
	public static boolean canOpenDoors(Box doorway, RenderVehicleTransformationHelper renderVehicleTransformationHelper, double doorValue, boolean isLift) {
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
		final int doroRadiusXZ = isLift ? CHECK_DOOR_RADIUS_XZ_LIFT : CHECK_DOOR_RADIUS_XZ_VEHICLE;
		boolean canOpenDoors = false;

		for (double checkX = minX - doroRadiusXZ; checkX <= maxX + doroRadiusXZ; checkX++) {
			for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
				for (double checkZ = minZ - doroRadiusXZ; checkZ <= maxZ + doroRadiusXZ; checkZ++) {
					final BlockPos checkPos = Init.newBlockPos(checkX, checkY, checkZ);
					final BlockState blockState = clientWorld.getBlockState(checkPos);
					final Block block = blockState.getBlock();
					if (block.data instanceof PlatformHelper) {
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

	public static void renderModel(RenderVehicleTransformationHelper renderVehicleTransformationHelper, double oscillationAmount, Consumer<StoredMatrixTransformations> render) {
		final StoredMatrixTransformations storedMatrixTransformations = renderVehicleTransformationHelper.getStoredMatrixTransformations();
		storedMatrixTransformations.add(graphicsHolder -> {
			renderVehicleTransformationHelper.transformBackwards(new Object(), (object, pitch) -> {
				graphicsHolder.rotateXRadians((float) (Math.PI - pitch)); // Blockbench exports models upside down
				return new Object();
			}, (object, yaw) -> {
				graphicsHolder.rotateYRadians((float) (Math.PI - yaw));
				return new Object();
			}, (object, x, y, z) -> {
				if (!storedMatrixTransformations.useDefaultOffset) {
					graphicsHolder.translate(-x, -y, -z);
				}
				return new Object();
			});
			graphicsHolder.rotateZDegrees((float) oscillationAmount);
		});

		render.accept(storedMatrixTransformations);
	}

	public static void renderFloorOrDoorway(Box floorOrDoorway, int color, Vector3d playerPosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
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
			MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (graphicsHolder, offset) -> renderVehicleTransformationHelper.render(graphicsHolder, offset, newOffset -> {
				drawLine(graphicsHolder, corner1, corner2, newOffset, newColor);
				drawLine(graphicsHolder, corner2, corner3, newOffset, newColor);
				drawLine(graphicsHolder, corner3, corner4, newOffset, newColor);
				drawLine(graphicsHolder, corner4, corner1, newOffset, newColor);
			}));
		}
	}

	/**
	 * @return an updated list of vehicle car properties with rendering offset information
	 */
	public static <T extends NameColorDataBase> ObjectArrayList<VehicleProperties> getTransformedVehiclePropertiesList(T vehicle, ObjectArrayList<VehicleProperties> vehiclePropertiesList, Vector3d cameraShakeOffset) {
		final IntObjectImmutablePair<ObjectObjectImmutablePair<Vector3d, Double>> ridingVehicleCarNumberAndOffset = VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(vehicle.getId());
		if (ridingVehicleCarNumberAndOffset != null) {
			final VehicleProperties ridingVehicleProperties = vehiclePropertiesList.get(ridingVehicleCarNumberAndOffset.leftInt());
			if (ridingVehicleProperties != null) {
				final Vector3d playerRelativePosition = ridingVehicleCarNumberAndOffset.right().left();
				final Vector3d playerRelativePositionNew = playerRelativePosition == null ? Vector3d.getZeroMapped() : playerRelativePosition;
				final double playerYOffset = playerRelativePositionNew.rotateX((float) ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.pitch).getYMapped() - playerRelativePositionNew.getYMapped();
				final Vector cameraShake = new Vector(cameraShakeOffset.getXMapped(), cameraShakeOffset.getYMapped(), cameraShakeOffset.getZMapped());
				return vehiclePropertiesList.stream().map(vehicleProperties -> {
					final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList = new ObjectArrayList<>();
					final ObjectArrayList<Vector> averageAbsoluteBogiePositionsList = new ObjectArrayList<>();

					vehicleProperties.bogiePositionsListNormalized.forEach(bogiePositions -> {
						bogiePositionsList.add(new ObjectObjectImmutablePair<>(
								ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.transformBackwards(bogiePositions.left().add(cameraShake), (vector, pitch) -> vector, Vector::rotateY, Vector::add).add(-playerRelativePositionNew.getXMapped(), -playerRelativePositionNew.getYMapped() - playerYOffset, -playerRelativePositionNew.getZMapped()),
								ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.transformBackwards(bogiePositions.right().add(cameraShake), (vector, pitch) -> vector, Vector::rotateY, Vector::add).add(-playerRelativePositionNew.getXMapped(), -playerRelativePositionNew.getYMapped() - playerYOffset, -playerRelativePositionNew.getZMapped())
						));
						averageAbsoluteBogiePositionsList.add(
								ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.transformBackwards(Vector.getAverage(bogiePositions.left(), bogiePositions.right()).add(cameraShake), (vector, pitch) -> vector, Vector::rotateY, Vector::add).add(-playerRelativePositionNew.getXMapped(), -playerRelativePositionNew.getYMapped() - playerYOffset, -playerRelativePositionNew.getZMapped())
						);
					});

					return new VehicleProperties(vehicleProperties.vehicleCar, bogiePositionsList, averageAbsoluteBogiePositionsList, vehicleProperties.renderVehicleTransformationHelperAbsolute, ridingVehicleProperties.renderVehicleTransformationHelperAbsolute, ridingVehicleCarNumberAndOffset.right().right());
				}).collect(Collectors.toCollection(ObjectArrayList::new));
			}
		}

		return vehiclePropertiesList;
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

	private static void drawLine(GraphicsHolder graphicsHolder, Vector3d corner1, Vector3d corner2, Vector3d offset, int color) {
		graphicsHolder.drawLineInWorld(
				(float) (corner1.getXMapped() - offset.getXMapped()), (float) (corner1.getYMapped() - offset.getYMapped()), (float) (corner1.getZMapped() - offset.getZMapped()),
				(float) (corner2.getXMapped() - offset.getXMapped()), (float) (corner2.getYMapped() - offset.getYMapped()), (float) (corner2.getZMapped() - offset.getZMapped()),
				color
		);
	}

	public static class VehicleProperties {

		public final VehicleCar vehicleCar;
		public final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList;
		public final ObjectArrayList<Vector> averageAbsoluteBogiePositionsList;
		public final RenderVehicleTransformationHelper renderVehicleTransformationHelperAbsolute;
		public final RenderVehicleTransformationHelper renderVehicleTransformationHelperOffset;
		private final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsListNormalized;

		public VehicleProperties(ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>> vehicleCarAndPosition, boolean ignorePitch) {
			vehicleCar = vehicleCarAndPosition.left();
			bogiePositionsList = vehicleCarAndPosition.right();

			if (ignorePitch) {
				double y = 0;
				int count = 0;
				for (final ObjectObjectImmutablePair<Vector, Vector> tempBogiePositions : bogiePositionsList) {
					y += tempBogiePositions.left().y + tempBogiePositions.right().y;
					count += 2;
				}
				final double yAverage = y / count;
				bogiePositionsListNormalized = new ObjectArrayList<>();
				bogiePositionsList.forEach(tempBogiePositions -> bogiePositionsListNormalized.add(new ObjectObjectImmutablePair<>(
						new Vector(tempBogiePositions.left().x, yAverage, tempBogiePositions.left().z),
						new Vector(tempBogiePositions.right().x, yAverage, tempBogiePositions.right().z)
				)));
			} else {
				bogiePositionsListNormalized = bogiePositionsList;
			}

			averageAbsoluteBogiePositionsList = new ObjectArrayList<>();
			bogiePositionsList.forEach(bogiePositions -> averageAbsoluteBogiePositionsList.add(Vector.getAverage(bogiePositions.left(), bogiePositions.right())));

			renderVehicleTransformationHelperAbsolute = renderVehicleTransformationHelperOffset = new RenderVehicleTransformationHelper(
					bogiePositionsListNormalized,
					vehicleCar.getBogie1Position(),
					vehicleCar.getBogie2Position(),
					vehicleCar.getLength(),
					false,
					false,
					0,
					0
			);
		}

		private VehicleProperties(VehicleCar vehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList, ObjectArrayList<Vector> averageAbsoluteBogiePositionsList, RenderVehicleTransformationHelper renderVehicleTransformationHelperAbsolute, RenderVehicleTransformationHelper ridingRenderVehicleTransformationHelperAbsolute, @Nullable Double ridingYawDifference) {
			this.vehicleCar = vehicleCar;
			this.bogiePositionsListNormalized = this.bogiePositionsList = bogiePositionsList;
			this.averageAbsoluteBogiePositionsList = averageAbsoluteBogiePositionsList;
			this.renderVehicleTransformationHelperAbsolute = renderVehicleTransformationHelperAbsolute;
			renderVehicleTransformationHelperOffset = new RenderVehicleTransformationHelper(
					bogiePositionsList,
					vehicleCar.getBogie1Position(),
					vehicleCar.getBogie2Position(),
					vehicleCar.getLength(),
					true,
					ridingYawDifference != null,
					ridingYawDifference == null ? ridingRenderVehicleTransformationHelperAbsolute.yaw : ridingYawDifference,
					-ridingRenderVehicleTransformationHelperAbsolute.pitch
			);
		}
	}
}
