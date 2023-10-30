package org.mtr.mod.client;

import org.mtr.core.tools.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.render.RenderVehicleTransformationHelper;
import org.mtr.mod.render.RenderVehicles;

import javax.annotation.Nullable;
import java.util.Comparator;

public class VehicleRidingMovement {

	private static long ridingVehicleId;
	private static int ridingVehicleCarNumber;
	private static double ridingVehicleX;
	private static double ridingVehicleY;
	private static double ridingVehicleZ;
	private static boolean isOnGangway;
	private static int ridingVehicleCoolDown;

	private static int ridingVehicleCarNumberCacheOld;
	private static Vector3d ridingPositionCacheOld;
	private static Vector3d ridingPositionCache;

	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.005F;
	private static final int RIDING_COOL_DOWN = 5;

	public static void tick() {
		if (ridingVehicleCoolDown < RIDING_COOL_DOWN) {
			ridingVehicleCoolDown++;
		} else {
			// If no vehicles are updating the player's position, dismount the player
			ridingVehicleId = 0;
		}
		ridingVehicleCarNumberCacheOld = ridingVehicleCarNumber;
		ridingPositionCacheOld = ridingPositionCache;
	}

	/**
	 * Iterate through all open doorways and see if the player is intersecting any of them. If so, start riding the vehicle.
	 */
	public static void startRiding(ObjectArrayList<Box> openDoorways, long vehicleId, int carNumber, double x, double y, double z) {
		if (ridingVehicleId == 0 || ridingVehicleId == vehicleId) {
			for (final Box doorway : openDoorways) {
				if (RenderVehicles.boxContains(doorway, x, y, z)) {
					ridingVehicleId = vehicleId;
					ridingVehicleCarNumber = carNumber;
					ridingVehicleX = x;
					ridingVehicleY = y;
					ridingVehicleZ = z;
					isOnGangway = false;
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

		if (ridingVehicleId == vehicleId && ridingVehicleCarNumber == carNumber) {
			ridingVehicleCoolDown = 0;
			final float speedMultiplier = millisElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER * (clientPlayerEntity.isSprinting() ? 2 : 1);
			// Calculate the relative motion inside vehicle (+Z towards back of vehicle, +/-X towards the left and right of the vehicle)
			final Vector3d movement = renderVehicleTransformationHelper.transformBackwards(new Vector3d(
					Math.abs(clientPlayerEntity.getSidewaysSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getSidewaysSpeedMapped()) : 0,
					0,
					Math.abs(clientPlayerEntity.getForwardSpeedMapped()) > 0.5 ? Math.copySign(speedMultiplier, clientPlayerEntity.getForwardSpeedMapped()) : 0
			), (vector, pitch) -> vector, (vector, yaw) -> vector.rotateY((float) (yaw - Math.toRadians(EntityHelper.getYaw(new Entity(clientPlayerEntity.data))))), (vector, x, y, z) -> vector);
			final double movementX = movement.getXMapped();
			final double movementZ = movement.getZMapped();

			if (isOnGangway) {
				// If the player is currently standing on a gangway, ridingVehicleX and Z will indicate percentages along the gangway (rather than a relative coordinate inside the vehicle car)
				if (thisCarGangwayMovementPositions1 == null || previousCarGangwayMovementPositions == null) {
					// Dismount player
					ridingVehicleId = 0;
				} else {
					if (ridingVehicleZ + movementZ > 1) {
						// If player has left the gangway (in the +Z direction), convert back to non-gangway positioning for ridingVehicleX and Z
						isOnGangway = false;
						ridingVehicleX = thisCarGangwayMovementPositions1.getX(ridingVehicleX);
						ridingVehicleZ = thisCarGangwayMovementPositions1.getZ() + ridingVehicleZ + movementZ - 1;
					} else if (ridingVehicleZ + movementZ < 0) {
						// If player has left the gangway (in the -Z direction) and consequently moved to the previous car, convert back to non-gangway positioning for ridingVehicleX and Z
						isOnGangway = false;
						ridingVehicleCarNumber--;
						ridingVehicleX = previousCarGangwayMovementPositions.getX(ridingVehicleX);
						ridingVehicleZ = previousCarGangwayMovementPositions.getZ() + ridingVehicleZ + movementZ;
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
				} else if (thisCarGangwayMovementPositions2 != null && thisCarGangwayMovementPositions2.getPercentageZ(ridingVehicleZ + movementZ) > 0) {
					// If player has entered the gangway (in the +Z direction) and consequently moved to the next car, convert to gangway positioning for ridingVehicleX and Z
					isOnGangway = true;
					ridingVehicleCarNumber++;
					ridingVehicleX = thisCarGangwayMovementPositions2.getPercentageX(ridingVehicleX + movementX);
					ridingVehicleZ = thisCarGangwayMovementPositions2.getPercentageZ(ridingVehicleZ + movementZ);
				} else {
					// Calculate and store all the offsets that should be applied to the player to keep them in bounds of the floors
					final ObjectArrayList<Vector3d> offsets = new ObjectArrayList<>();
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX - RenderVehicles.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ - RenderVehicles.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX + RenderVehicles.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ - RenderVehicles.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX + RenderVehicles.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ + RenderVehicles.HALF_PLAYER_WIDTH, offsets);
					clampPosition(floorsAndDoorways, ridingVehicleX + movementX - RenderVehicles.HALF_PLAYER_WIDTH, ridingVehicleZ + movementZ + RenderVehicles.HALF_PLAYER_WIDTH, offsets);

					if (offsets.isEmpty()) {
						// Player is not standing on any floor, dismount player
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
				}
			}
		}
	}

	/**
	 * @return {@code null} if the player is not riding a vehicle or an {@link IntObjectImmutablePair} of the car number the player is currently riding in and the relative position of the player with respect to the center of the car they are currently riding in.
	 */
	public static IntObjectImmutablePair<Vector3d> getRidingVehicleCarNumberAndPosition(long vehicleId) {
		return vehicleId == ridingVehicleId ? new IntObjectImmutablePair<>(ridingVehicleCarNumberCacheOld, ridingPositionCacheOld) : null;
	}

	/**
	 * Find an intersecting floor or doorway from the player position.
	 * If there are multiple intersecting floors or doorways, get the one with the highest Y level.
	 * If there are no intersecting floors or doorways, find the closest floor or doorway instead.
	 */
	@Nullable
	private static ObjectBooleanImmutablePair<Box> bestPosition(ObjectArrayList<ObjectBooleanImmutablePair<Box>> floorsOrDoorways, double x, double y, double z) {
		return floorsOrDoorways.stream().filter(floorOrDoorway -> RenderVehicles.boxContains(floorOrDoorway.left(), x, y, z)).max(Comparator.comparingDouble(floorOrDoorway -> floorOrDoorway.left().getMaxYMapped())).orElse(floorsOrDoorways.stream().min(Comparator.comparingDouble(floorOrDoorway -> Math.min(
				Math.abs(floorOrDoorway.left().getMinXMapped() - x),
				Math.abs(floorOrDoorway.left().getMaxXMapped() - x)
		) + Math.min(
				Math.abs(floorOrDoorway.left().getMinYMapped() - y),
				Math.abs(floorOrDoorway.left().getMaxYMapped() - y)
		) + Math.min(
				Math.abs(floorOrDoorway.left().getMinZMapped() - z),
				Math.abs(floorOrDoorway.left().getMaxZMapped() - z)
		))).orElse(null));
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
			} else if (RenderVehicles.boxContains(floorOrDoorway.left(), x, ridingVehicleY, z)) {
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
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity == null) {
				return;
			}

			final Runnable runnable = () -> {
				clientPlayerEntity.setFallDistanceMapped(0);
				clientPlayerEntity.setVelocity(0, 0, 0);
				clientPlayerEntity.setMovementSpeed(0);
				clientPlayerEntity.updatePosition(x, y, z);
			};

			runnable.run();
			InitClient.scheduleMovePlayer(runnable);
		}
	}

	private static double getFromScale(double min, double max, double percentage) {
		return (max - min) * percentage + min;
	}
}
