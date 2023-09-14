package org.mtr.mod.data;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.render.TrainRendererBase;

import java.util.UUID;
import java.util.function.Consumer;

public class VehicleRiding {

	private float clientPrevYaw;
	private float oldPercentageX;
	private float oldPercentageZ;
	private double lastSentX;
	private double lastSentY;
	private double lastSentZ;
	private float lastSentTicks;
	private int interval;
	private int previousInterval;

	private final DoubleArrayList offset = new DoubleArrayList();
	private final Object2FloatAVLTreeMap<UUID> percentagesX = new Object2FloatAVLTreeMap<>();
	private final Object2FloatAVLTreeMap<UUID> percentagesZ = new Object2FloatAVLTreeMap<>();
	private final Object2FloatAVLTreeMap<UUID> newPercentagesX = new Object2FloatAVLTreeMap<>();
	private final Object2FloatAVLTreeMap<UUID> newPercentagesZ = new Object2FloatAVLTreeMap<>();
	private final Object2ObjectAVLTreeMap<UUID, Vector3d> riderPositions = new Object2ObjectAVLTreeMap<>();
	private final ObjectArrayList<String> ridingEntities;

	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.25F;
	private static final int VEHICLE_PERCENTAGE_UPDATE_INTERVAL = 20;
	private static final boolean DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS = false;

	public VehicleRiding(ObjectArrayList<String> ridingEntities) {
		this.ridingEntities = ridingEntities;
	}

	public Vector3d renderPlayerAndGetOffset() {
		if (DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS) {
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity != null && ridingEntities.contains(clientPlayerEntity.getUuid().toString())) {
				return new Vector3d(0, Integer.MIN_VALUE, 0);
			}
		}

		final boolean noOffset = offset.isEmpty();
		riderPositions.forEach((uuid, position) -> {
			if (noOffset) {
				TrainRendererBase.renderRidingPlayer(getViewOffset(), uuid, position);
			} else {
				TrainRendererBase.renderRidingPlayer(getViewOffset(), uuid, position.subtract(offset.getDouble(0), offset.getDouble(1), offset.getDouble(2)));
			}
		});

		if (noOffset) {
			return Vector3d.getZeroMapped();
		} else {
			return new Vector3d(offset.getDouble(0), offset.getDouble(1), offset.getDouble(2));
		}
	}

	public void movePlayer(Consumer<UUID> ridingEntityCallback) {
		offset.clear();
		riderPositions.clear();

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		ridingEntities.forEach(id -> {
			final UUID uuid = UUID.fromString(id);
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
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		final boolean isClientPlayer = uuid.equals(clientPlayerEntity.getUuid());
		final double percentageX = getValueFromPercentage(percentagesX.getFloat(uuid), width);
		final float riderOffsetNew = doorLeftOpen && percentageX < 0 || doorRightOpen && percentageX > 1 ? riderOffsetDismounting : riderOffset;
		final Vector3d playerOffset = new Vector3d(percentageX, riderOffsetNew, getValueFromPercentage(MathHelper.fractionalPart(percentagesZ.getFloat(uuid)), length)).rotateX((pitch < 0 ? hasPitchAscending : hasPitchDescending) ? pitch : 0).rotateY(yaw);
		ClientData.updatePlayerRidingOffset(uuid);
		riderPositions.put(uuid, playerOffset.add(x, y, z));

		if (isClientPlayer) {
			final double moveX = x + playerOffset.getXMapped();
			final double moveY = y + playerOffset.getYMapped();
			final double moveZ = z + playerOffset.getZMapped();

			clientPlayerEntity.setFallDistanceMapped(0);
			clientPlayerEntity.setVelocity(0, 0, 0);
			clientPlayerEntity.setMovementSpeed(0);
			if (InitClient.getGameTick() > 40) {
				clientPlayerEntity.updatePosition(moveX, moveY, moveZ);
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
					final Entity castedEntity = new Entity(clientPlayerEntity.data);
					EntityHelper.setYaw(castedEntity, EntityHelper.getYaw(castedEntity) + angleDifference);
				}
				offset.add(x);
				offset.add(y);
				offset.add(z);
				offset.add(playerOffset.getXMapped());
				offset.add(playerOffset.getYMapped() + clientPlayerEntity.getStandingEyeHeight());
				offset.add(playerOffset.getZMapped());
			}

			clientPrevYaw = yaw;
		}
	}

	public void moveSelf(long id, UUID uuid, double length, int width, float yaw, int percentageOffset, int maxPercentage, boolean doorLeftOpen, boolean doorRightOpen, boolean noGangwayConnection, float ticksElapsed) {
		final float speedMultiplier = ticksElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER;
		final float newPercentageX;
		final float newPercentageZ;
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		if (uuid.equals(clientPlayerEntity.getUuid())) {
			final Vector3d movement = new Vector3d(Math.abs(clientPlayerEntity.getSidewaysSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getSidewaysSpeedMapped()) : 0, 0, Math.abs(clientPlayerEntity.getForwardSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getForwardSpeedMapped()) : 0).rotateY((float) -Math.toRadians(EntityHelper.getYaw(new Entity(clientPlayerEntity.data))) - yaw);
			final float tempPercentageX = percentagesX.getFloat(uuid) + (float) movement.getXMapped() / width;
			final float tempPercentageZ = percentagesZ.getFloat(uuid) + (length == 0 ? 0 : (float) movement.getZMapped() / (float) length);
			newPercentageX = MathHelper.clamp(tempPercentageX, doorLeftOpen ? -3 : 0, doorRightOpen ? 4 : 1);
			newPercentageZ = MathHelper.clamp(tempPercentageZ, (noGangwayConnection ? percentageOffset + 0.05F : 0) + 0.01F, (noGangwayConnection ? percentageOffset + 0.95F : maxPercentage) - 0.01F);

			if (previousInterval != interval && (newPercentageX != oldPercentageX || newPercentageZ != oldPercentageZ)) {
				// TODO send position update
				oldPercentageX = newPercentageX;
				oldPercentageZ = newPercentageZ;
			}
		} else {
			final double distanceX = getValueFromPercentage(newPercentagesX.getFloat(uuid), width) - getValueFromPercentage(percentagesX.getFloat(uuid), width);
			final double distanceZ = getValueFromPercentage(newPercentagesZ.getFloat(uuid), length) - getValueFromPercentage(percentagesZ.getFloat(uuid), length);
			final double manhattanDistance = Math.abs(distanceX + distanceZ);
			if (manhattanDistance == 0 || distanceX * distanceX + distanceZ * distanceZ < speedMultiplier * speedMultiplier) {
				newPercentageX = newPercentagesX.getFloat(uuid);
				newPercentageZ = newPercentagesZ.getFloat(uuid);
			} else {
				newPercentageX = percentagesX.getFloat(uuid) + (float) (distanceX / manhattanDistance * speedMultiplier / width);
				newPercentageZ = percentagesZ.getFloat(uuid) + (float) (length == 0 ? 0 : distanceZ / manhattanDistance * speedMultiplier / length);
			}
		}

		percentagesX.put(uuid, newPercentageX);
		percentagesZ.put(uuid, newPercentageZ);
	}

	public void begin() {
		interval = (int) Math.floor(InitClient.getGameTick() / VEHICLE_PERCENTAGE_UPDATE_INTERVAL);
	}

	public void end() {
		previousInterval = interval;
	}

	public void startRiding(UUID uuid, float percentageX, float percentageZ) {
		ridingEntities.add(uuid.toString());
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
		return percentagesZ.getFloat(uuid);
	}

	public Vector3d getVehicleOffset() {
		return offset.isEmpty() ? null : new Vector3d(offset.getDouble(0), offset.getDouble(1), offset.getDouble(2));
	}

	public Vector3d getViewOffset() {
		return offset.isEmpty() ? null : new Vector3d(offset.getDouble(3), offset.getDouble(4), offset.getDouble(5));
	}

	private static double getValueFromPercentage(double percentage, double total) {
		return (percentage - 0.5) * total;
	}
}
