package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mtr.block.BlockPSDAPGDoorBase;
import org.mtr.block.PlatformHelper;
import org.mtr.client.IDrawing;
import org.mtr.client.VehicleRidingMovement;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.registry.Items;

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
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			return false;
		}

		final Vec3d doorwayPosition1 = renderVehicleTransformationHelper.transformForwards(new Vec3d(doorway.minX, doorway.maxY, doorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
		final Vec3d doorwayPosition2 = renderVehicleTransformationHelper.transformForwards(new Vec3d(doorway.maxX, doorway.maxY, doorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
		final Vec3d doorwayPosition3 = renderVehicleTransformationHelper.transformForwards(new Vec3d(doorway.maxX, doorway.maxY, doorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
		final Vec3d doorwayPosition4 = renderVehicleTransformationHelper.transformForwards(new Vec3d(doorway.minX, doorway.maxY, doorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
		final double minX = Math.min(Math.min(doorwayPosition1.x, doorwayPosition2.x), Math.min(doorwayPosition3.x, doorwayPosition4.x));
		final double maxX = Math.max(Math.max(doorwayPosition1.x, doorwayPosition2.x), Math.max(doorwayPosition3.x, doorwayPosition4.x));
		final double minY = Math.min(Math.min(doorwayPosition1.y, doorwayPosition2.y), Math.min(doorwayPosition3.y, doorwayPosition4.y));
		final double maxY = Math.max(Math.max(doorwayPosition1.y, doorwayPosition2.y), Math.max(doorwayPosition3.y, doorwayPosition4.y));
		final double minZ = Math.min(Math.min(doorwayPosition1.z, doorwayPosition2.z), Math.min(doorwayPosition3.z, doorwayPosition4.z));
		final double maxZ = Math.max(Math.max(doorwayPosition1.z, doorwayPosition2.z), Math.max(doorwayPosition3.z, doorwayPosition4.z));
		final int doroRadiusXZ = isLift ? CHECK_DOOR_RADIUS_XZ_LIFT : CHECK_DOOR_RADIUS_XZ_VEHICLE;
		boolean canOpenDoors = false;

		for (double checkX = minX - doroRadiusXZ; checkX <= maxX + doroRadiusXZ; checkX++) {
			for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
				for (double checkZ = minZ - doroRadiusXZ; checkZ <= maxZ + doroRadiusXZ; checkZ++) {
					final BlockPos checkPos = BlockPos.ofFloored(checkX, checkY, checkZ);
					final BlockState blockState = clientWorld.getBlockState(checkPos);
					final Block block = blockState.getBlock();
					if (block instanceof PlatformHelper) {
						canOpenDoors = true;
					} else if (block instanceof BlockPSDAPGDoorBase && blockState.get(BlockPSDAPGDoorBase.UNLOCKED)) {
						canOpenDoors = true;
						final BlockEntity blockEntity = clientWorld.getBlockEntity(checkPos);
						if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase) {
							((BlockPSDAPGDoorBase.BlockEntityBase) blockEntity).open(doorValue);
						}
					}
				}
			}
		}

		return canOpenDoors;
	}

	public static void renderModel(RenderVehicleTransformationHelper renderVehicleTransformationHelper, double oscillationAmount, Consumer<StoredMatrixTransformations> render) {
		final StoredMatrixTransformations storedMatrixTransformations = renderVehicleTransformationHelper.getStoredMatrixTransformations();
		storedMatrixTransformations.add(matrixStack -> {
			renderVehicleTransformationHelper.transformBackwards(new Object(), (object, pitch) -> {
				IDrawing.rotateXRadians(matrixStack, (float) (Math.PI - pitch)); // Blockbench exports models upside down
				return new Object();
			}, (object, yaw) -> {
				IDrawing.rotateYRadians(matrixStack, (float) (Math.PI - yaw));
				return new Object();
			}, (object, x, y, z) -> {
				if (!storedMatrixTransformations.useDefaultOffset) {
					matrixStack.translate(-x, -y, -z);
				}
				return new Object();
			});
			IDrawing.rotateZDegrees(matrixStack, (float) oscillationAmount);
		});

		render.accept(storedMatrixTransformations);
	}

	public static void renderFloorOrDoorway(Box floorOrDoorway, int color, Vec3d playerPosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Vec3d corner1 = renderVehicleTransformationHelper.transformForwards(new Vec3d(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
			final Vec3d corner2 = renderVehicleTransformationHelper.transformForwards(new Vec3d(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
			final Vec3d corner3 = renderVehicleTransformationHelper.transformForwards(new Vec3d(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
			final Vec3d corner4 = renderVehicleTransformationHelper.transformForwards(new Vec3d(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
			final int newColor = boxContains(floorOrDoorway,
					playerPosition.x - HALF_PLAYER_WIDTH,
					playerPosition.y,
					playerPosition.z - HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.x + HALF_PLAYER_WIDTH,
					playerPosition.y,
					playerPosition.z - HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.x + HALF_PLAYER_WIDTH,
					playerPosition.y,
					playerPosition.z + HALF_PLAYER_WIDTH
			) || boxContains(floorOrDoorway,
					playerPosition.x - HALF_PLAYER_WIDTH,
					playerPosition.y,
					playerPosition.z + HALF_PLAYER_WIDTH
			) ? 0xFF00FF00 : color;
			MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack, vertexConsumer, offset) -> renderVehicleTransformationHelper.render(matrixStack, offset, newOffset -> {
				drawLine(matrixStack, vertexConsumer, corner1, corner2, newOffset, newColor);
				drawLine(matrixStack, vertexConsumer, corner2, corner3, newOffset, newColor);
				drawLine(matrixStack, vertexConsumer, corner3, corner4, newOffset, newColor);
				drawLine(matrixStack, vertexConsumer, corner4, corner1, newOffset, newColor);
			}));
		}
	}

	/**
	 * @return an updated list of vehicle car properties with rendering offset information
	 */
	public static <T extends NameColorDataBase> ObjectArrayList<VehicleProperties> getTransformedVehiclePropertiesList(T vehicle, ObjectArrayList<VehicleProperties> vehiclePropertiesList, Vec3d cameraShakeOffset) {
		final IntObjectImmutablePair<ObjectObjectImmutablePair<Vec3d, Double>> ridingVehicleCarNumberAndOffset = VehicleRidingMovement.getRidingVehicleCarNumberAndOffset(vehicle.getId());
		if (ridingVehicleCarNumberAndOffset != null) {
			final VehicleProperties ridingVehicleProperties = vehiclePropertiesList.get(ridingVehicleCarNumberAndOffset.leftInt());
			if (ridingVehicleProperties != null) {
				final Vec3d playerRelativePosition = ridingVehicleCarNumberAndOffset.right().left();
				final Vec3d playerRelativePositionNew = playerRelativePosition == null ? Vec3d.ZERO : playerRelativePosition;
				final double playerYOffset = playerRelativePositionNew.rotateX((float) ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.pitch).y - playerRelativePositionNew.y;
				final Vector cameraShake = new Vector(cameraShakeOffset.x, cameraShakeOffset.y, cameraShakeOffset.z);
				return vehiclePropertiesList.stream().map(vehicleProperties -> {
					final ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList = new ObjectArrayList<>();
					final ObjectArrayList<Vector> averageAbsoluteBogiePositionsList = new ObjectArrayList<>();

					vehicleProperties.bogiePositionsListNormalized.forEach(bogiePositions -> {
						bogiePositionsList.add(new ObjectObjectImmutablePair<>(
								ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.transformBackwards(bogiePositions.left().add(cameraShake), (vector, pitch) -> vector, Vector::rotateY, Vector::add).add(-playerRelativePositionNew.x, -playerRelativePositionNew.y - playerYOffset, -playerRelativePositionNew.z),
								ridingVehicleProperties.renderVehicleTransformationHelperAbsolute.transformBackwards(bogiePositions.right().add(cameraShake), (vector, pitch) -> vector, Vector::rotateY, Vector::add).add(-playerRelativePositionNew.x, -playerRelativePositionNew.y - playerYOffset, -playerRelativePositionNew.z)
						));
						averageAbsoluteBogiePositionsList.add(Vector.getAverage(bogiePositions.left(), bogiePositions.right()).add(cameraShake));
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
				box.minX,
				box.maxX
		) && Utilities.isBetween(
				y,
				box.minY,
				box.maxY,
				RIDE_STEP_THRESHOLD
		) && Utilities.isBetween(
				z,
				box.minZ,
				box.maxZ
		);
	}

	private static void drawLine(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d corner1, Vec3d corner2, Vec3d offset, int color) {
		IDrawing.drawLineInWorld(
				matrixStack, vertexConsumer,
				(float) (corner1.x - offset.x), (float) (corner1.y - offset.y), (float) (corner1.z - offset.z),
				(float) (corner2.x - offset.x), (float) (corner2.y - offset.y), (float) (corner2.z - offset.z),
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
