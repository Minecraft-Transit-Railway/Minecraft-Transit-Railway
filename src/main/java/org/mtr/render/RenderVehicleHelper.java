package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.mtr.block.BlockPSDAPGDoorBase;
import org.mtr.block.PlatformHelper;
import org.mtr.client.IDrawing;
import org.mtr.core.tool.Utilities;
import org.mtr.registry.Items;

import java.util.function.Consumer;

public class RenderVehicleHelper {

	public static final float HALF_PLAYER_WIDTH = 0.3F;
	private static final int CHECK_DOOR_RADIUS_XZ = 1;
	private static final int CHECK_DOOR_RADIUS_Y = 2;
	private static final double RIDE_STEP_THRESHOLD = 0.75;

	/**
	 * @return Whether the doorway is close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates
	 */
	public static boolean canOpenDoors(AABB doorway, PositionAndRotation positionAndRotation, @Nullable Consumer<BlockPSDAPGDoorBase.BlockEntityBase> doorBlockEntityCallback) {
		final ClientLevel clientWorld = Minecraft.getInstance().level;
		if (clientWorld == null) {
			return false;
		}

		final Vec3 doorwayPosition1 = positionAndRotation.transformForwards(new Vec3(doorway.minX, doorway.maxY, doorway.minZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
		final Vec3 doorwayPosition2 = positionAndRotation.transformForwards(new Vec3(doorway.maxX, doorway.maxY, doorway.minZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
		final Vec3 doorwayPosition3 = positionAndRotation.transformForwards(new Vec3(doorway.maxX, doorway.maxY, doorway.maxZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
		final Vec3 doorwayPosition4 = positionAndRotation.transformForwards(new Vec3(doorway.minX, doorway.maxY, doorway.maxZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
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
					final BlockPos checkPos = BlockPos.containing(checkX, checkY, checkZ);
					final BlockState blockState = clientWorld.getBlockState(checkPos);
					final Block block = blockState.getBlock();
					if (block instanceof PlatformHelper) {
						canOpenDoors = true;
					} else if (block instanceof BlockPSDAPGDoorBase && blockState.getValue(BlockPSDAPGDoorBase.UNLOCKED)) {
						canOpenDoors = true;
						if(doorBlockEntityCallback == null) break;

						final BlockEntity blockEntity = clientWorld.getBlockEntity(checkPos);
						if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase doorBlockEntity) {
							doorBlockEntityCallback.accept(doorBlockEntity);
						}
					}
				}
			}
		}

		return canOpenDoors;
	}

	/**
	 * Attempts to open platform doors nearby the doorway by the given doorValue.
	 * @return Whether the doorway is close to platform blocks, unlocked platform screen doors, or unlocked automatic platform gates
	 */
	public static boolean checkAndOpenNearbyDoors(AABB doorway, PositionAndRotation positionAndRotation, double doorValue) {
		return canOpenDoors(doorway, positionAndRotation, (blockEntity) -> {
			blockEntity.setDoorValue(doorValue);
		});
	}

	public static double getDoorBlockedAmount(AABB doorway, double playerX, double playerY, double playerZ) {
		if (playerX > doorway.minX - HALF_PLAYER_WIDTH && playerX < doorway.maxX + HALF_PLAYER_WIDTH && Utilities.isBetween(playerY, doorway.minY, doorway.maxY) && playerZ > doorway.minZ - HALF_PLAYER_WIDTH && playerZ < doorway.maxZ + HALF_PLAYER_WIDTH) {
			final double halfWidth = (doorway.maxZ - doorway.minZ) / 2;
			final double distance = Math.min(playerZ - HALF_PLAYER_WIDTH - doorway.minZ, doorway.maxZ - HALF_PLAYER_WIDTH - playerZ);
			return Math.clamp((halfWidth - distance) / halfWidth, 0, 1);
		} else {
			return 0;
		}
	}

	public static void renderFloorOrDoorway(AABB floorOrDoorway, int color, Vec3 playerPosition, PositionAndRotation positionAndRotation, boolean useOffset) {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		if (clientPlayerEntity != null && clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Vec3 corner1 = positionAndRotation.transformForwards(new Vec3(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
			final Vec3 corner2 = positionAndRotation.transformForwards(new Vec3(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.minZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
			final Vec3 corner3 = positionAndRotation.transformForwards(new Vec3(floorOrDoorway.maxX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
			final Vec3 corner4 = positionAndRotation.transformForwards(new Vec3(floorOrDoorway.minX, floorOrDoorway.maxY, floorOrDoorway.maxZ), Vec3::xRot, Vec3::yRot, Vec3::zRot, Vec3::add);
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
				drawLine(matrixStack, vertexConsumer, corner1, corner2, useOffset ? offset : Vec3.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner2, corner3, useOffset ? offset : Vec3.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner3, corner4, useOffset ? offset : Vec3.ZERO, newColor);
				drawLine(matrixStack, vertexConsumer, corner4, corner1, useOffset ? offset : Vec3.ZERO, newColor);
			});
		}
	}

	public static boolean boxContains(AABB box, double x, double y, double z) {
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

	private static void drawLine(PoseStack matrixStack, VertexConsumer vertexConsumer, Vec3 corner1, Vec3 corner2, Vec3 offset, int color) {
		IDrawing.drawLineInWorld(
			matrixStack, vertexConsumer,
			(float) (corner1.x - offset.x), (float) (corner1.y - offset.y), (float) (corner1.z - offset.z),
			(float) (corner2.x - offset.x), (float) (corner2.y - offset.y), (float) (corner2.z - offset.z),
			color
		);
	}
}
