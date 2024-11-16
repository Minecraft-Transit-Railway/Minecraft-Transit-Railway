package org.mtr.mod.client;

import org.apache.commons.lang3.StringUtils;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.KeyBindings;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateVehicleRidingEntities;
import org.mtr.mod.render.RenderVehicleHelper;
import org.mtr.mod.render.RenderVehicleTransformationHelper;
import org.mtr.mod.screen.LiftSelectionScreen;

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
	private static Vector3d ridingPositionCacheOld;
	private static Vector3d ridingPositionCache;
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
				final Screen currentScreen = minecraftClient.getCurrentScreenMapped();
				if (MinecraftClientData.getLift(ridingVehicleId) != null && (currentScreen == null || !(currentScreen.data instanceof LiftSelectionScreen))) {
					minecraftClient.openScreen(new Screen(new LiftSelectionScreen(ridingVehicleId)));
				}
			}

			final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
			if (clientPlayerEntity != null && clientPlayerEntity.isSneaking()) {
				shiftHoldingTicks += minecraftClient.getLastFrameDuration();
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
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		if (isRiding(vehicleId) && ridingVehicleCarNumber == carNumber) {
			ridingVehicleCooldown = 0;
			final double entityYawOld = EntityHelper.getYaw(new Entity(clientPlayerEntity.data));
			final float speedMultiplier = millisElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER * (clientPlayerEntity.isSprinting() ? 2 : 1);
			// Calculate the relative motion inside vehicle (+Z towards back of vehicle, +/-X towards the left and right of the vehicle)
			final Vector3d movement = renderVehicleTransformationHelper.transformBackwards(new Vector3d(
					Math.abs(clientPlayerEntity.getSidewaysSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getSidewaysSpeedMapped()) : 0,
					0,
					Math.abs(clientPlayerEntity.getForwardSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getForwardSpeedMapped()) : 0
			), (vector, pitch) -> vector, (vector, yaw) -> vector.rotateY((float) (yaw - Math.toRadians(entityYawOld))), (vector, x, y, z) -> vector);
			final double movementX = movement.getXMapped();
			final double movementZ = movement.getZMapped();

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
						final Vector3d position1Min = previousCarGangwayMovementPositions.getMinWorldPosition();
						final Vector3d position1Max = previousCarGangwayMovementPositions.getMaxWorldPosition();
						final Vector3d position2Min = thisCarGangwayMovementPositions1.getMinWorldPosition();
						final Vector3d position2Max = thisCarGangwayMovementPositions1.getMaxWorldPosition();
						final double positionX = getFromScale(
								getFromScale(position1Min.getXMapped(), position1Max.getXMapped(), ridingVehicleX),
								getFromScale(position2Min.getXMapped(), position2Max.getXMapped(), ridingVehicleX),
								ridingVehicleZ
						);
						final double positionY = getFromScale(
								getFromScale(position1Min.getYMapped(), position1Max.getYMapped(), ridingVehicleX),
								getFromScale(position2Min.getYMapped(), position2Max.getYMapped(), ridingVehicleX),
								ridingVehicleZ
						);
						final double positionZ = getFromScale(
								getFromScale(position1Min.getZMapped(), position1Max.getZMapped(), ridingVehicleX),
								getFromScale(position2Min.getZMapped(), position2Max.getZMapped(), ridingVehicleX),
								ridingVehicleZ
						);

						// ridingPositionCache should always store the relative position of the player with respect to the riding car, even when the player is on a gangway
						ridingPositionCache = renderVehicleTransformationHelper.transformBackwards(new Vector3d(positionX, positionY, positionZ), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
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
					final ObjectArrayList<Vector3d> offsets = new ObjectArrayList<>();
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
						for (final Vector3d offset : offsets) {
							if (Math.abs(offset.getXMapped()) > Math.abs(clampX)) {
								clampX = offset.getXMapped();
							}
							maxY = Math.max(maxY, offset.getYMapped());
							if (Math.abs(offset.getZMapped()) > Math.abs(clampZ)) {
								clampZ = offset.getZMapped();
							}
						}
						ridingVehicleX += movementX + clampX;
						ridingVehicleY = maxY;
						ridingVehicleZ += movementZ + clampZ;
					}

					ridingPositionCache = new Vector3d(ridingVehicleX, ridingVehicleY, ridingVehicleZ);
					final Vector3d newPlayerPosition = renderVehicleTransformationHelper.transformForwards(ridingPositionCache, Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
					movePlayer(newPlayerPosition.getXMapped(), newPlayerPosition.getYMapped(), newPlayerPosition.getZMapped());
					EntityHelper.setYaw(new Entity(clientPlayerEntity.data), (float) (Math.toDegrees(previousVehicleYaw - renderVehicleTransformationHelper.yaw) + entityYawOld));
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
	public static IntObjectImmutablePair<ObjectObjectImmutablePair<Vector3d, Double>> getRidingVehicleCarNumberAndOffset(long vehicleId) {
		return isRiding(vehicleId) ? new IntObjectImmutablePair<>(ridingVehicleCarNumberCacheOld, new ObjectObjectImmutablePair<>(ridingPositionCacheOld, ridingYawDifferenceOld)) : null;
	}

	public static boolean isRiding(long vehicleId) {
		return vehicleId == ridingVehicleId;
	}

	public static boolean showShiftProgressBar() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (shiftHoldingTicks > 0 && clientPlayerEntity != null) {
			final int progressFilled = MathHelper.clamp((int) (shiftHoldingTicks * DISMOUNT_PROGRESS_BAR_LENGTH / SHIFT_ACTIVATE_TICKS), 0, DISMOUNT_PROGRESS_BAR_LENGTH);
			final String progressBar = String.format("§6%s§7%s", StringUtils.repeat('|', progressFilled), StringUtils.repeat('|', DISMOUNT_PROGRESS_BAR_LENGTH - progressFilled));
			clientPlayerEntity.sendMessage(TranslationProvider.GUI_MTR_DISMOUNT_HOLD.getText(InitClient.getShiftText(), progressBar), true);
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
				.max(Comparator.comparingDouble(floorOrDoorway -> floorOrDoorway.left().getMaxYMapped()))
				.orElse(floorsOrDoorways.stream().filter(floorOrDoorway -> Math.abs(floorOrDoorway.left().getMaxYMapped() - ridingVehicleY) <= 1).min(Comparator.comparingDouble(floorOrDoorway -> {
					final Box box = floorOrDoorway.left();
					final double minX = box.getMinXMapped();
					final double maxX = box.getMaxXMapped();
					final double minZ = box.getMinZMapped();
					final double maxZ = box.getMaxZMapped();
					return (Utilities.isBetween(x, minX, maxX) ? 0 : Math.min(Math.abs(minX - x), Math.abs(maxX - x))) + (Utilities.isBetween(z, minZ, maxZ) ? 0 : Math.min(Math.abs(minZ - z), Math.abs(maxZ - z)));
				})).orElse(null));
	}

	private static void clampPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsAndDoorways, double x, double z, ObjectArrayList<Vector3d> offsets) {
		final ObjectBooleanImmutablePair<Box> floorOrDoorway = bestPosition(floorsAndDoorways, x, ridingVehicleY, z);

		if (floorOrDoorway != null) {
			if (floorOrDoorway.rightBoolean()) {
				// If the intersecting or closest floor or doorway is a floor, then force the player to be in bounds
				offsets.add(new Vector3d(
						Utilities.clamp(x, floorOrDoorway.left().getMinXMapped(), floorOrDoorway.left().getMaxXMapped()) - x,
						floorOrDoorway.left().getMaxYMapped(),
						Utilities.clamp(z, floorOrDoorway.left().getMinZMapped(), floorOrDoorway.left().getMaxZMapped()) - z
				));
			} else if (RenderVehicleHelper.boxContains(floorOrDoorway.left(), x, ridingVehicleY, z)) {
				// If the intersecting or closest floor or doorway is a doorway, then don't force the player to be in bounds
				// Dismount if the player is not intersecting the doorway
				offsets.add(new Vector3d(0, floorOrDoorway.left().getMaxYMapped(), 0));
			}
		}
	}

	/**
	 * Moves the client player to absolute world coordinates right now and also at the end of the client tick.
	 * (If the player is not moved at the end of the client tick, there will be a rubber banding animation which will look weird when moving inside a vehicle.)
	 */
	private static void movePlayer(double x, double y, double z) {
		if (InitClient.getGameTick() > 40) {
			final Runnable runnable = () -> {
				final MinecraftClient minecraftClient = MinecraftClient.getInstance();
				final ClientWorld clientWorld = minecraftClient.getWorldMapped();
				final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
				if (clientPlayerEntity != null && clientWorld != null) {
					clientPlayerEntity.setFallDistanceMapped(0);
					clientPlayerEntity.setVelocity(0, 0, 0);
					clientPlayerEntity.setMovementSpeed(0);
					clientPlayerEntity.updatePosition(x, y, z);
				}
			};

			runnable.run();
			InitClient.scheduleMovePlayer(runnable);
		}
	}

	private static void sendUpdate(boolean dismount) {
		if (ridingVehicleId != 0) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(PacketUpdateVehicleRidingEntities.create(ridingSidingId, ridingVehicleId, dismount ? -1 : ridingVehicleCarNumber, ridingVehicleX, ridingVehicleY, ridingVehicleZ, isOnGangway));
			sendPositionUpdateTime = 0;
		}
	}

	private static double getFromScale(double min, double max, double percentage) {
		return (max - min) * percentage + min;
	}
}
