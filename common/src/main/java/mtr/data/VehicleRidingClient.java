package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.MTRClient;
import mtr.RegistryClient;
import mtr.client.ClientData;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.TrainRendererBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;

public class VehicleRidingClient {

	private float clientPrevYaw;
	private float oldPercentageX;
	private float oldPercentageZ;
	private double lastSentX;
	private double lastSentY;
	private double lastSentZ;
	private float lastSentTicks;
	private int interval;
	private int previousInterval;

	private final List<Double> offset = new ArrayList<>();
	private final Map<UUID, Float> percentagesX = new HashMap<>();
	private final Map<UUID, Float> percentagesZ = new HashMap<>();
	private final Map<UUID, Float> newPercentagesX = new HashMap<>();
	private final Map<UUID, Float> newPercentagesZ = new HashMap<>();
	private final Map<UUID, Vec3> riderPositions = new HashMap<>();
	private final Set<UUID> ridingEntities;
	private final ResourceLocation packetId;

	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.25F;
	private static final int VEHICLE_PERCENTAGE_UPDATE_INTERVAL = 20;
	private static final boolean DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS = false;

	public VehicleRidingClient(Set<UUID> ridingEntities, ResourceLocation packetId) {
		this.ridingEntities = ridingEntities;
		this.packetId = packetId;
	}

	public Vec3 renderPlayerAndGetOffset() {
		if (DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS) {
			final Player player = Minecraft.getInstance().player;
			if (player != null && ridingEntities.contains(player.getUUID())) {
				return new Vec3(0, Integer.MIN_VALUE, 0);
			}
		}

		final boolean noOffset = offset.isEmpty();
		riderPositions.forEach((uuid, position) -> {
			if (noOffset) {
				TrainRendererBase.renderRidingPlayer(getViewOffset(), uuid, position);
			} else {
				TrainRendererBase.renderRidingPlayer(getViewOffset(), uuid, position.subtract(offset.get(0), offset.get(1), offset.get(2)));
			}
		});

		if (noOffset) {
			return Vec3.ZERO;
		} else {
			return new Vec3(offset.get(0), offset.get(1), offset.get(2));
		}
	}

	public void movePlayer(Consumer<UUID> ridingEntityCallback) {
		offset.clear();
		riderPositions.clear();

		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		ridingEntities.forEach(uuid -> {
			if (!percentagesX.containsKey(uuid) || !newPercentagesX.containsKey(uuid)) {
				percentagesX.put(uuid, 0.5F);
				newPercentagesX.put(uuid, 0.5F);
			}
			if (!percentagesZ.containsKey(uuid) || !newPercentagesZ.containsKey(uuid)) {
				percentagesZ.put(uuid, 0.5F);
				newPercentagesZ.put(uuid, 0.5F);
			}

			ridingEntityCallback.accept(uuid);
		});
	}

	public void setOffsets(UUID uuid, double x, double y, double z, float yaw, float pitch, double length, int width, boolean doorLeftOpen, boolean doorRightOpen, boolean hasPitchAscending, boolean hasPitchDescending, float riderOffset, float riderOffsetDismounting, boolean shouldSetOffset, boolean shouldSetYaw, Runnable clientPlayerCallback) {
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		final boolean isClientPlayer = uuid.equals(clientPlayer.getUUID());
		final double percentageX = getValueFromPercentage(percentagesX.get(uuid), width);
		final float riderOffsetNew = doorLeftOpen && percentageX < 0 || doorRightOpen && percentageX > 1 ? riderOffsetDismounting : riderOffset;
		final Vec3 playerOffset = new Vec3(percentageX, riderOffsetNew, getValueFromPercentage(Mth.frac(percentagesZ.get(uuid)), length)).xRot((pitch < 0 ? hasPitchAscending : hasPitchDescending) ? pitch : 0).yRot(yaw);
		ClientData.updatePlayerRidingOffset(uuid);
		riderPositions.put(uuid, playerOffset.add(x, y, z));

		if (isClientPlayer) {
			final double moveX = x + playerOffset.x;
			final double moveY = y + playerOffset.y;
			final double moveZ = z + playerOffset.z;
			final boolean movePlayer;

			if (MTRClient.isVivecraft()) {
				final Entity vehicle = clientPlayer.getVehicle();
				if (vehicle instanceof EntitySeat) {
					((EntitySeat) vehicle).setPosByTrain(moveX, moveY, moveZ);
					movePlayer = false;
				} else {
					movePlayer = true;
				}

				final float tempPercentageX = percentagesX.get(uuid);
				final boolean doorOpen = doorLeftOpen && tempPercentageX < 0 || doorRightOpen && tempPercentageX > 1;
				final boolean movedFar = Math.abs(lastSentX - moveX) > 2 || Math.abs(lastSentY - moveY) > 2 || Math.abs(lastSentZ - moveZ) > 2;

				if (doorOpen || MTRClient.getGameTick() - lastSentTicks > 60 && movedFar) {
					PacketTrainDataGuiClient.sendUpdateEntitySeatPassengerPosition(moveX, moveY, moveZ);
					lastSentX = moveX;
					lastSentY = moveY;
					lastSentZ = moveZ;
					lastSentTicks = MTRClient.getGameTick();
				}
			} else {
				movePlayer = true;
			}

			if (movePlayer) {
				clientPlayer.fallDistance = 0;
				clientPlayer.setDeltaMovement(0, 0, 0);
				clientPlayer.setSpeed(0);
				if (MTRClient.getGameTick() > 40) {
					clientPlayer.absMoveTo(moveX, moveY, moveZ);
				}
			}

			clientPlayerCallback.run();

			if (shouldSetOffset) {
				if (shouldSetYaw) {
					float angleDifference = (float) Math.toDegrees(clientPrevYaw - yaw);
					if (angleDifference > 180) {
						angleDifference -= 360;
					} else if (angleDifference < -180) {
						angleDifference += 360;
					}
					Utilities.incrementYaw(clientPlayer, angleDifference);
				}
				offset.add(x);
				offset.add(y);
				offset.add(z);
				offset.add(playerOffset.x);
				offset.add(playerOffset.y + (MTRClient.isVivecraft() ? 0 : clientPlayer.getEyeHeight()));
				offset.add(playerOffset.z);
			}

			clientPrevYaw = yaw;
		}
	}

	public void moveSelf(long id, UUID uuid, double length, int width, float yaw, int percentageOffset, int maxPercentage, boolean doorLeftOpen, boolean doorRightOpen, boolean noGangwayConnection, float ticksElapsed) {
		final float speedMultiplier = ticksElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER;
		final float newPercentageX;
		final float newPercentageZ;
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;

		if (clientPlayer == null) {
			return;
		}

		if (uuid.equals(clientPlayer.getUUID())) {
			final Vec3 movement = new Vec3(Math.abs(clientPlayer.xxa) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.xxa) : 0, 0, Math.abs(clientPlayer.zza) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.zza) : 0).yRot((float) -Math.toRadians(Utilities.getYaw(clientPlayer)) - yaw);
			final float tempPercentageX = percentagesX.get(uuid) + (float) movement.x / width;
			final float tempPercentageZ = percentagesZ.get(uuid) + (length == 0 ? 0 : (float) movement.z / (float) length);
			newPercentageX = Mth.clamp(tempPercentageX, doorLeftOpen ? -3 : 0, doorRightOpen ? 4 : 1);
			newPercentageZ = Mth.clamp(tempPercentageZ, (noGangwayConnection ? percentageOffset + 0.05F : 0) + 0.01F, (noGangwayConnection ? percentageOffset + 0.95F : maxPercentage) - 0.01F);

			if (previousInterval != interval && (newPercentageX != oldPercentageX || newPercentageZ != oldPercentageZ)) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeLong(id);
				packet.writeFloat(newPercentageX);
				packet.writeFloat(newPercentageZ);
				packet.writeUUID(uuid);
				RegistryClient.sendToServer(packetId, packet);
				oldPercentageX = newPercentageX;
				oldPercentageZ = newPercentageZ;
			}
		} else {
			final double distanceX = getValueFromPercentage(newPercentagesX.get(uuid), width) - getValueFromPercentage(percentagesX.get(uuid), width);
			final double distanceZ = getValueFromPercentage(newPercentagesZ.get(uuid), length) - getValueFromPercentage(percentagesZ.get(uuid), length);
			final double manhattanDistance = Math.abs(distanceX + distanceZ);
			if (manhattanDistance == 0 || distanceX * distanceX + distanceZ * distanceZ < speedMultiplier * speedMultiplier) {
				newPercentageX = newPercentagesX.get(uuid);
				newPercentageZ = newPercentagesZ.get(uuid);
			} else {
				newPercentageX = percentagesX.get(uuid) + (float) (distanceX / manhattanDistance * speedMultiplier / width);
				newPercentageZ = percentagesZ.get(uuid) + (float) (length == 0 ? 0 : distanceZ / manhattanDistance * speedMultiplier / length);
			}
		}

		percentagesX.put(uuid, newPercentageX);
		percentagesZ.put(uuid, newPercentageZ);
	}

	public void begin() {
		interval = (int) Math.floor(MTRClient.getGameTick() / VEHICLE_PERCENTAGE_UPDATE_INTERVAL);
	}

	public void end() {
		previousInterval = interval;
	}

	public void startRiding(UUID uuid, float percentageX, float percentageZ) {
		ridingEntities.add(uuid);
		percentagesX.put(uuid, percentageX);
		percentagesZ.put(uuid, percentageZ);
		newPercentagesX.put(uuid, percentageX);
		newPercentagesZ.put(uuid, percentageZ);
	}

	public void updateRiderPercentages(UUID uuid, float percentageX, float percentageZ) {
		newPercentagesX.put(uuid, percentageX);
		newPercentagesZ.put(uuid, percentageZ);
	}

	public float getPercentageZ(UUID uuid) {
		return percentagesZ.get(uuid);
	}

	public Vec3 getVehicleOffset() {
		return offset.isEmpty() ? null : new Vec3(offset.get(0), offset.get(1), offset.get(2));
	}

	public Vec3 getViewOffset() {
		return offset.isEmpty() ? null : new Vec3(offset.get(3), offset.get(4), offset.get(5));
	}

	private static double getValueFromPercentage(double percentage, double total) {
		return (percentage - 0.5) * total;
	}
}
