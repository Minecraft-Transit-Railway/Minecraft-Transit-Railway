package org.mtr.render;

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
import org.mtr.core.tool.Utilities;
import org.mtr.registry.Items;

public class RenderVehicleHelper {

	public static final float HALF_PLAYER_WIDTH = 0.3F;
	private static final int CHECK_DOOR_RADIUS_XZ = 1;
	private static final int CHECK_DOOR_RADIUS_Y = 2;
	private static final double RIDE_STEP_THRESHOLD = 0.75;

	/**
	 * @return whether the doorway is close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates
	 */
	public static boolean canOpenDoors(Box doorway, PositionAndRotation positionAndRotation, double doorValue) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			return false;
		}

		final Vec3d doorwayPosition1 = positionAndRotation.transformForwards(new Vec3d(doorway.minX, doorway.maxY, doorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
		final Vec3d doorwayPosition2 = positionAndRotation.transformForwards(new Vec3d(doorway.maxX, doorway.maxY, doorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
		final Vec3d doorwayPosition3 = positionAndRotation.transformForwards(new Vec3d(doorway.maxX, doorway.maxY, doorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
		final Vec3d doorwayPosition4 = positionAndRotation.transformForwards(new Vec3d(doorway.minX, doorway.maxY, doorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
		final double minX = Math.min(Math.min(doorwayPosition1.x, doorwayPosition2.x), Math.min(doorwayPosition3.x, doorwayPosition4.x));
		final double maxX = Math.max(Math.max(doorwayPosition1.x, doorwayPosition2.x), Math.max(doorwayPosition3.x, doorwayPosition4.x));
		final double minY = Math.min(Math.min(doorwayPosition1.y, doorwayPosition2.y), Math.min(doorwayPosition3.y, doorwayPosition4.y));
		final double maxY = Math.max(Math.max(doorwayPosition1.y, doorwayPosition2.y), Math.max(doorwayPosition3.y, doorwayPosition4.y));
		final double minZ = Math.min(Math.min(doorwayPosition1.z, doorwayPosition2.z), Math.min(doorwayPosition3.z, doorwayPosition4.z));
		final double maxZ = Math.max(Math.max(doorwayPosition1.z, doorwayPosition2.z), Math.max(doorwayPosition3.z, doorwayPosition4.z));
		boolean canOpenDoors = false;

		for (double checkX = minX - CHECK_DOOR_RADIUS_XZ; checkX <= maxX + CHECK_DOOR_RADIUS_XZ; checkX++) {
			for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
				for (double checkZ = minZ - CHECK_DOOR_RADIUS_XZ; checkZ <= maxZ + CHECK_DOOR_RADIUS_XZ; checkZ++) {
					final BlockPos checkPos = BlockPos.ofFloored(checkX, checkY, checkZ);
					final BlockState blockState = clientWorld.getBlockState(checkPos);
					final Block block = blockState.getBlock();
					if (block instanceof PlatformHelper) {
						canOpenDoors = true;
					} else if (block instanceof BlockPSDAPGDoorBase && blockState.get(BlockPSDAPGDoorBase.UNLOCKED)) {
						canOpenDoors = true;
						final BlockEntity blockEntity = clientWorld.getBlockEntity(checkPos);
						if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase) {
							((BlockPSDAPGDoorBase.BlockEntityBase) blockEntity).setDoorValue(doorValue);
						}
					}
				}
			}
		}

		return canOpenDoors;
	}

	public static double getDoorBlockedAmount(Box doorway, double playerX, double playerY, double playerZ) {
		if (playerX > doorway.minX - HALF_PLAYER_WIDTH && playerX < doorway.maxX + HALF_PLAYER_WIDTH && Utilities.isBetween(playerY, doorway.minY, doorway.maxY) && playerZ > doorway.minZ - HALF_PLAYER_WIDTH && playerZ < doorway.maxZ + HALF_PLAYER_WIDTH) {
			final double halfWidth = (doorway.maxZ - doorway.minZ) / 2;
			final double distance = Math.min(playerZ - HALF_PLAYER_WIDTH - doorway.minZ, doorway.maxZ - HALF_PLAYER_WIDTH - playerZ);
			return Math.clamp((halfWidth - distance) / halfWidth, 0, 1);
		} else {
			return 0;
		}
	}

	public static void renderFloorOrDoorway(Box floorOrDoorway, int color, Vec3d playerPosition, PositionAndRotation positionAndRotation, boolean useOffset) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Vec3d corner1 = positionAndRotation.transformForwards(new Vec3d(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
			final Vec3d corner2 = positionAndRotation.transformForwards(new Vec3d(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
			final Vec3d corner3 = positionAndRotation.transformForwards(new Vec3d(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
			final Vec3d corner4 = positionAndRotation.transformForwards(new Vec3d(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
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
			MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack, vertexConsumer, offset) -> {
				drawLine(matrixStack, vertexConsumer, corner1, corner2, useOffset ? offset : Vec3d.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner2, corner3, useOffset ? offset : Vec3d.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner3, corner4, useOffset ? offset : Vec3d.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner4, corner1, useOffset ? offset : Vec3d.ZERO, newColor);
			});
		}
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
}
