package org.mtr.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.tool.CullingHelper;
import org.mtr.tool.Drawing;

import java.util.UUID;

public class RenderPassengers {

	private static final int MIN_MOVE_TIME = 10 * Utilities.MILLIS_PER_SECOND;
	private static final int MAX_MOVE_TIME = 40 * Utilities.MILLIS_PER_SECOND;

	private static final float MIN_WALKING_SPEED = 1F / Utilities.MILLIS_PER_SECOND; // 1 m/s
	private static final float MAX_WALKING_SPEED = 4F / Utilities.MILLIS_PER_SECOND; // 4 m/s

	private static final int RESOLUTION = 1000000;
	private static final long GOLDEN_RATIO_FRACTION = 0x9E3779B97F4A7C15L;

	public static void render() {
		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		final ObjectArrayList<Passenger> passengers = minecraftClientData.vehicleIdToPassengers.get(0);

		if (passengers != null) {
			passengers.forEach(passenger -> {
				final ObjectArrayList<PassengerDirection> directions = passenger.getDirections();
				final long passengerId = passenger.getId();

				if (directions.isEmpty()) {
					final SimpleAreaBase area;
					if (passenger.getEndLandmarkId() == 0) {
						area = minecraftClientData.homeIdMap.get(passenger.getHomeId());
					} else {
						area = minecraftClientData.landmarkIdMap.get(passenger.getEndLandmarkId());
					}

					if (area != null) {
						render(passenger, getIdlePosition(area, passengerId));
					}
				} else {
					final PassengerDirection direction = directions.getFirst();
					final Platform platform1 = minecraftClientData.platformIdMap.get(direction.getStartPlatformId());
					final Platform platform2 = minecraftClientData.platformIdMap.get(direction.getEndPlatformId());

					final PositionAndYaw positionAndYaw1;
					if (platform1 == null) {
						final Home home = minecraftClientData.homeIdMap.get(passenger.getHomeId());
						positionAndYaw1 = home == null ? null : getIdlePosition(home, passengerId);
					} else {
						positionAndYaw1 = getIdlePosition(platform1, passengerId);
					}

					final PositionAndYaw positionAndYaw2;
					if (platform2 == null) {
						final Landmark landmark = minecraftClientData.landmarkIdMap.get(passenger.getEndLandmarkId());
						positionAndYaw2 = landmark == null ? null : getIdlePosition(landmark, passengerId);
					} else {
						positionAndYaw2 = getIdlePosition(platform2, passengerId);
					}

					if (positionAndYaw1 != null && positionAndYaw2 != null) {
						final double progress = System.currentTimeMillis() - direction.getStartTime();

						final double differenceX = positionAndYaw2.x - positionAndYaw1.x;
						final double differenceY = positionAndYaw2.y - positionAndYaw1.y;
						final double differenceZ = positionAndYaw2.z - positionAndYaw1.z;
						final double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY + differenceZ * differenceZ);

						final double walkingSpeed = Math.max(getRandomValue(passengerId, MIN_WALKING_SPEED, MAX_WALKING_SPEED), distance / (direction.getEndTime() - direction.getStartTime()));
						final double walkingTime = distance / walkingSpeed;
						final double walkingTimeChange = walkingTime == 0 ? 1 : Math.min(1, progress / walkingTime);
						final double x = positionAndYaw1.x + walkingTimeChange * differenceX;
						final double y = positionAndYaw1.y + walkingTimeChange * differenceY;
						final double z = positionAndYaw1.z + walkingTimeChange * differenceZ;
						final double yaw = Math.atan2(differenceZ, differenceX);

						render(passenger, new PositionAndYaw(x, y, z, yaw));
					}
				}
			});
		}
	}

	private static PositionAndYaw getIdlePosition(SimpleAreaBase area, long passengerId) {
		final ClientLevel clientWorld = Minecraft.getInstance().level;

		if (clientWorld == null) {
			return new PositionAndYaw(0, 0, 0, 0);
		} else {
			final double walkingSpeed = getRandomValue(passengerId, MIN_WALKING_SPEED, MAX_WALKING_SPEED) / 2; // half walking speed when idling
			final double longestWalkingTime = (Math.abs(area.getMaxX() - area.getMinX()) + Math.abs(area.getMaxZ() - area.getMinZ())) / walkingSpeed;
			final long changePositionInterval = (long) Math.ceil(getRandomValue(passengerId, MIN_MOVE_TIME + longestWalkingTime, MAX_MOVE_TIME + longestWalkingTime));

			final long millis = System.currentTimeMillis();
			final long position2Snapshot = millis / changePositionInterval;
			final long position1Snapshot = position2Snapshot - 1;
			final long position3Snapshot = position1Snapshot + 1;
			final long progress = millis - position2Snapshot * changePositionInterval;

			final double x1 = getRandomValue(position1Snapshot * 3, area.getMinX(), area.getMaxX());
			final double x2 = getRandomValue(position2Snapshot * 3, area.getMinX(), area.getMaxX());
			final double x3 = getRandomValue(position3Snapshot * 3, area.getMinX(), area.getMaxX());
			final double differenceX = x2 - x1;

			final double z1 = getRandomValue(position1Snapshot * 5 + 1, area.getMinZ(), area.getMaxZ());
			final double z2 = getRandomValue(position2Snapshot * 5 + 1, area.getMinZ(), area.getMaxZ());
			final double z3 = getRandomValue(position3Snapshot * 5 + 1, area.getMinZ(), area.getMaxZ());
			final double differenceZ = z2 - z1;

			final double walkingTime = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ) / walkingSpeed;
			final double walkingTimeChange = walkingTime == 0 ? 1 : Math.min(1, progress / walkingTime);
			final double x = x1 + walkingTimeChange * differenceX;
			final double z = z1 + walkingTimeChange * differenceZ;

			final double yaw1 = Math.atan2(differenceZ, differenceX);
			final double yaw2 = Math.atan2(z3 - z2, x3 - x2);
			final double yaw = yaw1 + Utilities.clampSafe(progress - walkingTime, 0, 1000) * Utilities.circularDifference(yaw2, yaw1, Math.PI * 2);

			for (long y = area.getMinY(); y <= area.getMaxY(); y++) {
				final BlockPos checkPos1 = BlockPos.containing(x, y - 1, z);
				final BlockPos checkPos2 = BlockPos.containing(x, y, z);
				final BlockPos checkPos3 = BlockPos.containing(x, y + 1, z);

				if (!clientWorld.getBlockState(checkPos1).isAir() && clientWorld.getBlockState(checkPos2).isAir() && clientWorld.getBlockState(checkPos3).isAir()) {
					return new PositionAndYaw(x, y, z, yaw);
				}
			}

			return new PositionAndYaw(x, clientWorld.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) Math.floor(x), (int) Math.floor(z)), z, yaw);
		}
	}

	private static PositionAndYaw getIdlePosition(Platform platform, long passengerId) {
		final ClientLevel clientWorld = Minecraft.getInstance().level;

		if (clientWorld == null) {
			return new PositionAndYaw(0, 0, 0, 0);
		} else {
			final Rail rail = platform.rail;

			if (rail == null) {
				return new PositionAndYaw(0, 0, 0, 0);
			} else {
				// TODO use platform block positions
				final Vector position = rail.railMath.getPosition(getRandomValue(passengerId, 0, rail.railMath.getLength()), false);
				return new PositionAndYaw(position.x(), position.y(), position.z(), 0);
			}
		}
	}

	/**
	 * @return a deterministic pseudo-random value based on a seed
	 */
	private static double getRandomValue(long seed, double min, double max) {
		return min + (Math.floorMod(seed * GOLDEN_RATIO_FRACTION, RESOLUTION)) * (max - min) / RESOLUTION;
	}

	private static void render(Passenger passenger, PositionAndYaw positionAndYaw) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		final ClientLevel clientWorld = minecraftClient.level;

		if (clientWorld != null && CullingHelper.getDistanceFromCamera(positionAndYaw.x, positionAndYaw.y, positionAndYaw.z) <= minecraftClient.levelRenderer.getLastViewDistance() * 8) {
			MainRenderer.scheduleRender(QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				matrixStack.pushPose();
				matrixStack.translate(positionAndYaw.x - offset.x, positionAndYaw.y - offset.y, positionAndYaw.z - offset.z);
				Drawing.rotateYRadians(matrixStack, (float) (Math.PI / 2 - positionAndYaw.yaw));
				final RemotePlayer remotePlayer = new RemotePlayer(clientWorld, new GameProfile(new UUID(passenger.getId(), 0), passenger.getName()));
				final BlockPos blockPos = BlockPos.containing(positionAndYaw.x, positionAndYaw.y, positionAndYaw.z);
				final int light = LightTexture.pack(clientWorld.getBrightness(LightLayer.BLOCK, blockPos), clientWorld.getBrightness(LightLayer.SKY, blockPos));
//? if >= 1.21.4 {
				minecraftClient.getEntityRenderDispatcher().render(remotePlayer, 0, 0, 0, 0, matrixStack, minecraftClient.renderBuffers().bufferSource(), light);
//? } else {
				/*minecraftClient.getEntityRenderDispatcher().render(remotePlayer, 0, 0, 0, 0, 0, matrixStack, minecraftClient.renderBuffers().bufferSource(), light);
//
*///? }
				matrixStack.popPose();
			});
		}
	}

	private record PositionAndYaw(double x, double y, double z, double yaw) {
	}
}
