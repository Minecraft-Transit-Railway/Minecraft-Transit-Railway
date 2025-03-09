package org.mtr.client;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.StringUtils;
import org.mtr.MTRClient;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateVehicleRidingEntities;
import org.mtr.registry.KeyBindings;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderVehicleHelper;
import org.mtr.render.RenderVehicleTransformationHelper;
import org.mtr.screen.LiftSelectionScreen;

import javax.annotation.Nullable;
import java.util.Comparator;

public class VehicleRidingMovement {

	private static long ridingSidingId;
	private static long ridingVehicleId;
	private static int ridingVehicleCarNumber;
	private static double ridingVehicleX;
	private static double ridingVehicleY;
	private static double ridingVehicleZ;
	private static boolean isOnGangway;
	private static int ridingVehicleCooldown;
	private static float shiftHoldingTicks;

	private static int ridingVehicleCarNumberCacheOld;
	private static Vec3d ridingPositionCacheOld;
	private static Vec3d ridingPositionCache;
	private static Double ridingYawDifferenceOld;
	private static Double ridingYawDifference;
	private static double previousVehicleYaw;

	// Cool down for sending player position to simulator
	private static long sendPositionUpdateTime;

	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.005F;
	private static final int RIDING_COOLDOWN = 5;
	private static final int SEND_UPDATE_FREQUENCY = 1000;
	private static final int SHIFT_ACTIVATE_TICKS = 30;
	private static final int DISMOUNT_PROGRESS_BAR_LENGTH = 30;

	public static void tick() {
		if (ridingVehicleCooldown < RIDING_COOLDOWN && shiftHoldingTicks < SHIFT_ACTIVATE_TICKS) {
			ridingVehicleCooldown++;
		} else {
			// If no vehicles are updating the player's position, dismount the player
			sendUpdate(true);
			ridingSidingId = 0;
			ridingVehicleId = 0;
		}

		if (ridingPositionCache != null) {
			ridingVehicleCarNumberCacheOld = ridingVehicleCarNumber;
			ridingPositionCacheOld = ridingPositionCache;
			ridingYawDifferenceOld = ridingYawDifference;
		}

		if (sendPositionUpdateTime > 0 && sendPositionUpdateTime <= System.currentTimeMillis()) {
			sendUpdate(false);
		}

		if (ridingVehicleId == 0) {
			shiftHoldingTicks = 0;
		} else {
			final MinecraftClient minecraftClient = MinecraftClient.getInstance();

			if (KeyBindings.LIFT_MENU.isPressed()) {
				final Screen currentScreen = minecraftClient.currentScreen;
				if (MinecraftClientData.getLift(ridingVehicleId) != null && !(currentScreen instanceof LiftSelectionScreen)) {
					minecraftClient.setScreen(new LiftSelectionScreen(ridingVehicleId));
				}
			}

			final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
			if (clientPlayerEntity != null && clientPlayerEntity.isSneaking()) {
				shiftHoldingTicks += minecraftClient.getRenderTickCounter().getLastFrameDuration();
			} else {
				shiftHoldingTicks = 0;
			}
		}
	}

	/**
	 * Iterate through all open doorways and see if the player is intersecting any of them. If so, start riding the vehicle.
	 */
	public static void startRiding(ObjectArrayList<Box> openDoorways, long sidingId, long vehicleId, int carNumber, double x, double y, double z, double yaw) {
		if (ridingVehicleId == 0 || isRiding(vehicleId)) {
			for (final Box doorway : openDoorways) {
				if (RenderVehicleHelper.boxContains(doorway, x, y, z)) {
					ridingSidingId = sidingId;
					ridingVehicleId = vehicleId;
					ridingVehicleCarNumber = carNumber;
					ridingVehicleX = x;
					ridingVehicleY = y;
					ridingVehicleZ = z;
					isOnGangway = false;
					ridingPositionCacheOld = null;
					ridingPositionCache = null;
					ridingYawDifferenceOld = null;
					ridingYawDifference = null;
					previousVehicleYaw = yaw;
					if (ridingVehicleId == 0) {
						sendUpdate(false);
					}
				}
			}
		}
	}

	public static void movePlayer(
			long millisElapsed, long vehicleId, int carNumber,
			ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways,
			@Nullable GangwayMovementPositions previousCarGangwayMovementPositions,
			@Nullable GangwayMovementPositions thisCarGangwayMovementPositions1,
			@Nullable GangwayMovementPositions thisCarGangwayMovementPositions2,
			RenderVehicleTransformationHelper renderVehicleTransformationHelper
	) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (clientPlayerEntity == null) {
			return;
		}

		if (isRiding(vehicleId) && ridingVehicleCarNumber == carNumber) {
			ridingVehicleCooldown = 0;
			final double entityYawOld = clientPlayerEntity.getYaw();
			final float speedMultiplier = millisElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER * (clientPlayerEntity.isSprinting() ? 2 : 1);
			// Calculate the relative motion inside vehicle (+Z towards back of vehicle, +/-X towards the left and right of the vehicle)
			final Vec3d movement = renderVehicleTransformationHelper.transformBackwards(new Vec3d(
					Math.abs(clientPlayerEntity.sidewaysSpeed) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.sidewaysSpeed) : 0,
					0,
					Math.abs(clientPlayerEntity.forwardSpeed) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.forwardSpeed) : 0
			), (vector, pitch) -> vector, (vector, yaw) -> vector.rotateY((float) (yaw - Math.toRadians(entityYawOld))), (vector, x, y, z) -> vector);
			final double movementX = movement.x;
			final double movementZ = movement.z;

			if (sendPositionUpdateTime == 0 && (movementX != 0 || movementZ != 0)) {
				sendPositionUpdateTime = System.currentTimeMillis() + SEND_UPDATE_FREQUENCY;
			}

			if (isOnGangway) {
				// If the player is currently standing on a gangway, ridingVehicleX and Z will indicate percentages along the gangway (rather than a relative coordinate inside the vehicle car)
				if (thisCarGangwayMovementPositions1 == null || previousCarGangwayMovementPositions == null) {
					// Dismount player
					sendUpdate(true);
					ridingSidingId = 0;
					ridingVehicleId = 0;
				} else {
					if (ridingVehicleZ + movementZ > 1) {
						// If player has left the gangway (in the +Z direction), convert back to non-gangway positioning for ridingVehicleX and Z
						isOnGangway = false;
						ridingVehicleX = thisCarGangwayMovementPositions1.getX(ridingVehicleX);
						ridingVehicleZ = thisCarGangwayMovementPositions1.getZ() + ridingVehicleZ + movementZ - 1;
						ridingPositionCache = null;
					} else if (ridingVehicleZ + movementZ < 0) {
						// If player has left the gangway (in the -Z direction) and consequently moved to the previous car, convert back to non-gangway positioning for ridingVehicleX and Z
						isOnGangway = false;
						ridingVehicleCarNumber--;
						ridingVehicleX = previousCarGangwayMovementPositions.getX(ridingVehicleX);
						ridingVehicleZ = previousCarGangwayMovementPositions.getZ() + ridingVehicleZ + movementZ;
						ridingPositionCache = null;
					} else {
						// Gangway positioning logic
						ridingVehicleX = Utilities.clamp(ridingVehicleX + movementX, 0, 1);
						ridingVehicleZ += movementZ;
						final Vec3d position1Min = previousCarGangwayMovementPositions.getMinWorldPosition();
						final Vec3d position1Max = previousCarGangwayMovementPositions.getMaxWorldPosition();
						final Vec3d position2Min = thisCarGangwayMovementPositions1.getMinWorldPosition();
						final Vec3d position2Max = thisCarGangwayMovementPositions1.getMaxWorldPosition();
						final double positionX = getFromScale(
								getFromScale(position1Min.x, position1Max.x, ridingVehicleX),
								getFromScale(position2Min.x, position2Max.x, ridingVehicleX),
								ridingVehicleZ
						);
						final double positionY = getFromScale(
								getFromScale(position1Min.y, position1Max.y, ridingVehicleX),
								getFromScale(position2Min.y, position2Max.y, ridingVehicleX),
								ridingVehicleZ
						);
						final double positionZ = getFromScale(
								getFromScale(position1Min.z, position1Max.z, ridingVehicleX),
								getFromScale(position2Min.z, position2Max.z, ridingVehicleX),
								ridingVehicleZ
						);

						// ridingPositionCache should always store the relative position of the player with respect to the riding car, even when the player is on a gangway
						ridingPositionCache = renderVehicleTransformationHelper.transformBackwards(new Vec3d(positionX, positionY, positionZ), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
						movePlayer(positionX, positionY, positionZ);
					}
				}
			} else {
				if (thisCarGangwayMovementPositions1 != null && thisCarGangwayMovementPositions1.getPercentageZ(ridingVehicleZ + movementZ) < 1) {
					// If player has entered the gangway (in the -Z direction), convert to gangway positioning for ridingVehicleX and Z
					isOnGangway = true;
					ridingVehicleX = thisCarGangwayMovementPositions1.getPercentageX(ridingVehicleX + movementX);
					ridingVehicleZ = thisCarGangwayMovementPositions1.getPercentageZ(ridingVehicleZ + movementZ);
					ridingPositionCache = null;
				} else if (thisCarGangwayMovementPositions2 != null && thisCarGangwayMovementPositions2.getPercentageZ(ridingVehicleZ + movementZ) > 0) {
					// If player has entered the gangway (in the +Z direction) and consequently moved to the next car, convert to gangway positioning for ridingVehicleX and Z
					isOnGangway = true;
					ridingVehicleCarNumber++;
					ridingVehicleX = thisCarGangwayMovementPositions2.getPercentageX(ridingVehicleX + movementX);
					ridingVehicleZ = thisCarGangwayMovementPositions2.getPercentageZ(ridingVehicleZ + movementZ);
					ridingPositionCache = null;
				} else {
					// Calculate and store all the offsets that should be applied to the player to keep them in bounds of the floors
					final ObjectArrayList<Vec3d> offsets = new ObjectArrayList<>();
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX - RenderVehicleHelper.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ - RenderVehicleHelper.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX + RenderVehicleHelper.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ - RenderVehicleHelper.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX + RenderVehicleHelper.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ + RenderVehicleHelper.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX - RenderVehicleHelper.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ + RenderVehicleHelper.HALF_PLAYER_WIDTH, offsets);

					if (offsets.isEmpty()) {
						// Player is not standing on any floor, dismount player
						sendUpdate(true);
						ridingSidingId = 0;
						ridingVehicleId = 0;
					} else {
						// Find the highest amounts to clamp the player movement in both the X and Z direction and apply the clamps
						double clampX = 0;
						double maxY = -Double.MAX_VALUE;
						double clampZ = 0;
						for (final Vec3d offset : offsets) {
							if (Math.abs(offset.x) > Math.abs(clampX)) {
								clampX = offset.x;
							}
							maxY = Math.max(maxY, offset.y);
							if (Math.abs(offset.z) > Math.abs(clampZ)) {
								clampZ = offset.z;
							}
						}
						ridingVehicleX += movementX + clampX;
						ridingVehicleY = maxY;
						ridingVehicleZ += movementZ + clampZ;
					}

					ridingPositionCache = new Vec3d(ridingVehicleX, ridingVehicleY, ridingVehicleZ);
					final Vec3d newPlayerPosition = renderVehicleTransformationHelper.transformForwards(ridingPositionCache, Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
					movePlayer(newPlayerPosition.x, newPlayerPosition.y, newPlayerPosition.z);
					clientPlayerEntity.setYaw((float) (Math.toDegrees(previousVehicleYaw - renderVehicleTransformationHelper.yaw) + entityYawOld));
				}
			}

			ridingYawDifference = Math.abs(renderVehicleTransformationHelper.yaw - previousVehicleYaw) > 0.001 ? renderVehicleTransformationHelper.yaw + Math.toRadians(entityYawOld) : null;
			previousVehicleYaw = renderVehicleTransformationHelper.yaw;
		}
	}

	/**
	 * @return {@code null} if the player is not riding a vehicle or an {@link IntObjectImmutablePair} of the car number the player is currently riding in and the relative position and yaw of the player with respect to the center of the car they are currently riding in.
	 */
	@Nullable
	public static IntObjectImmutablePair<ObjectObjectImmutablePair<Vec3d, Double>> getRidingVehicleCarNumberAndOffset(long vehicleId) {
		return isRiding(vehicleId) ? new IntObjectImmutablePair<>(ridingVehicleCarNumberCacheOld, new ObjectObjectImmutablePair<>(ridingPositionCacheOld, ridingYawDifferenceOld)) : null;
	}

	public static boolean isRiding(long vehicleId) {
		return vehicleId == ridingVehicleId;
	}

	public static boolean showShiftProgressBar() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;

		if (shiftHoldingTicks > 0 && clientPlayerEntity != null) {
			final int progressFilled = MathHelper.clamp((int) (shiftHoldingTicks * DISMOUNT_PROGRESS_BAR_LENGTH / SHIFT_ACTIVATE_TICKS), 0, DISMOUNT_PROGRESS_BAR_LENGTH);
			final String progressBar = String.format("§6%s§7%s", StringUtils.repeat('|', progressFilled), StringUtils.repeat('|', DISMOUNT_PROGRESS_BAR_LENGTH - progressFilled));
			clientPlayerEntity.sendMessage(TranslationProvider.GUI_MTR_DISMOUNT_HOLD.getText(MTRClient.getShiftText(), progressBar), true);
			return false;
		} else {
			return true;
		}
	}

	public static void writeVehicleId(LongAVLTreeSet keepVehicleIds) {
		if (ridingVehicleId != 0) {
			keepVehicleIds.add(ridingVehicleId);
		}
	}

	/**
	 * Find an intersecting floor or doorway from the player position.
	 * If there are multiple intersecting floors or doorways, get the one with the highest Y level.
	 * If there are no intersecting floors or doorways, find the closest floor or doorway instead.
	 */
	@Nullable
	private static ObjectBooleanImmutablePair<Box> bestPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsOrDoorways, double x, double y, double z) {
		return floorsOrDoorways.stream()
				.filter(floorOrDoorway -> RenderVehicleHelper.boxContains(floorOrDoorway.left(), x, y, z))
				.max(Comparator.comparingDouble(floorOrDoorway -> floorOrDoorway.left().maxY))
				.orElse(floorsOrDoorways.stream().filter(floorOrDoorway -> Math.abs(floorOrDoorway.left().maxY - ridingVehicleY) <= 1).min(Comparator.comparingDouble(floorOrDoorway -> {
					final Box box = floorOrDoorway.left();
					final double minX = box.minX;
					final double maxX = box.maxX;
					final double minZ = box.minZ;
					final double maxZ = box.maxZ;
					return (Utilities.isBetween(x, minX, maxX) ? 0 : Math.min(Math.abs(minX - x), Math.abs(maxX - x))) + (Utilities.isBetween(z, minZ, maxZ) ? 0 : Math.min(Math.abs(minZ - z), Math.abs(maxZ - z)));
				})).orElse(null));
	}

	private static void clampPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways, double x, double z, ObjectArrayList<Vec3d> offsets) {
		final ObjectBooleanImmutablePair<Box> floorOrDoorway = bestPosition(floorsAndDoorways, x, ridingVehicleY, z);

		if (floorOrDoorway != null) {
			if (floorOrDoorway.rightBoolean()) {
				// If the intersecting or closest floor or doorway is a floor, then force the player to be in bounds
				offsets.add(new Vec3d(
						Utilities.clamp(x, floorOrDoorway.left().minX, floorOrDoorway.left().maxX) - x,
						floorOrDoorway.left().maxY,
						Utilities.clamp(z, floorOrDoorway.left().minZ, floorOrDoorway.left().maxZ) - z
				));
			} else if (RenderVehicleHelper.boxContains(floorOrDoorway.left(), x, ridingVehicleY, z)) {
				// If the intersecting or closest floor or doorway is a doorway, then don't force the player to be in bounds
				// Dismount if the player is not intersecting the doorway
				offsets.add(new Vec3d(0, floorOrDoorway.left().maxY, 0));
			}
		}
	}

	/**
	 * Moves the client player to absolute world coordinates right now and also at the end of the client tick.
	 * (If the player is not moved at the end of the client tick, there will be a rubber banding animation which will look weird when moving inside a vehicle.)
	 */
	private static void movePlayer(double x, double y, double z) {
		if (MTRClient.getGameTick() > 40) {
			final Runnable runnable = () -> {
				final MinecraftClient minecraftClient = MinecraftClient.getInstance();
				final ClientWorld clientWorld = minecraftClient.world;
				final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
				if (clientPlayerEntity != null && clientWorld != null) {
					clientPlayerEntity.fallDistance = 0;
					clientPlayerEntity.setVelocity(0, 0, 0);
					clientPlayerEntity.setMovementSpeed(0);
					clientPlayerEntity.updatePosition(x, y, z);
				}
			};

			runnable.run();
			MTRClient.scheduleMovePlayer(runnable);
		}
	}

	private static void sendUpdate(boolean dismount) {
		if (ridingVehicleId != 0) {
			RegistryClient.sendPacketToServer(PacketUpdateVehicleRidingEntities.create(ridingSidingId, ridingVehicleId, dismount ? -1 : ridingVehicleCarNumber, ridingVehicleX, ridingVehicleY, ridingVehicleZ, isOnGangway));
			sendPositionUpdateTime = 0;
		}
	}

	private static double getFromScale(double min, double max, double percentage) {
		return (max - min) * percentage + min;
	}
}
