package org.mtr.mod.client;

import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.PositionAndRotation;
import org.mtr.mod.render.RenderVehicleHelper;
import org.mtr.mod.resource.VehicleResourceCache;

public final class VehicleWeatherCover {

	private static final double WEATHER_COVER_CHECK_PADDING = 2;
	private static final ObjectArrayList<CachedWeatherCover> cachedWeatherCovers = new ObjectArrayList<>();
	private static long cachedGameTick = Long.MIN_VALUE;

	public static boolean hasWeatherCoverAt(double x, double y, double z) {
		refreshCache();

		for (int i = 0; i < cachedWeatherCovers.size(); i++) {
			if (cachedWeatherCovers.get(i).hasWeatherCoverAt(x, y, z)) {
				return true;
			}
		}

		return false;
	}

	private static void refreshCache() {
		final long gameTick = InitClient.getGameMillis() / 50;
		if (cachedGameTick == gameTick) {
			return;
		}

		cachedGameTick = gameTick;
		cachedWeatherCovers.clear();

		for (final VehicleExtension vehicle : MinecraftClientData.getInstance().vehicles) {
			final ObjectArrayList<ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>>> vehicleCarsAndPositions = vehicle.getSmoothedVehicleCarsAndPositions(0);
			for (int carNumber = 0; carNumber < vehicleCarsAndPositions.size(); carNumber++) {
				addCachedWeatherCover(vehicle, vehicleCarsAndPositions.get(carNumber), carNumber);
			}
		}
	}

	private static void addCachedWeatherCover(VehicleExtension vehicle, ObjectObjectImmutablePair<VehicleCar, ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>>> vehicleCarAndPosition, int carNumber) {
		final VehicleCar vehicleCar = vehicleCarAndPosition.left();
		final VehicleResourceCache[] vehicleResourceCache = {null};
		CustomResourceLoader.getVehicleById(vehicle.getTransportMode(), vehicleCar.getVehicleId(), vehicleResourceDetails -> vehicleResourceCache[0] = vehicleResourceDetails.left().getCachedVehicleResource(carNumber, vehicle.vehicleExtraData.immutableVehicleCars.size(), false));
		if (vehicleResourceCache[0] == null || vehicleResourceCache[0].weatherCoverBoxes.isEmpty()) {
			return;
		}

		final ObjectArrayList<PositionAndRotation> bogiePositions = new ObjectArrayList<>();
		vehicleCarAndPosition.right().forEach(bogiePositionPair -> bogiePositions.add(new PositionAndRotation(bogiePositionPair.left(), bogiePositionPair.right(), true)));
		final PositionAndRotation positionAndRotation = new PositionAndRotation(bogiePositions, vehicleCar, vehicle.getTransportMode().hasPitchAscending || vehicle.getTransportMode().hasPitchDescending);
		final double longestDimension = vehicle.persistentVehicleData.longestDimensions[carNumber] + WEATHER_COVER_CHECK_PADDING;
		cachedWeatherCovers.add(new CachedWeatherCover(positionAndRotation, longestDimension, vehicleResourceCache[0].weatherCoverBoxes));
	}

	private static final class CachedWeatherCover {

		private final PositionAndRotation positionAndRotation;
		private final double longestDimension;
		private final ObjectImmutableList<Box> weatherCoverBoxes;

		private CachedWeatherCover(PositionAndRotation positionAndRotation, double longestDimension, ObjectImmutableList<Box> weatherCoverBoxes) {
			this.positionAndRotation = positionAndRotation;
			this.longestDimension = longestDimension;
			this.weatherCoverBoxes = weatherCoverBoxes;
		}

		private boolean hasWeatherCoverAt(double x, double y, double z) {
			if (Math.abs(x - positionAndRotation.position.x) > longestDimension || Math.abs(y - positionAndRotation.position.y) > longestDimension || Math.abs(z - positionAndRotation.position.z) > longestDimension) {
				return false;
			}

			final Vector3d relativePosition = positionAndRotation.transformBackwards(new Vector3d(x, y, z), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
			final double relativeX = relativePosition.getXMapped();
			final double relativeY = relativePosition.getYMapped();
			final double relativeZ = relativePosition.getZMapped();
			for (int i = 0; i < weatherCoverBoxes.size(); i++) {
				if (RenderVehicleHelper.isBelowWeatherCover(weatherCoverBoxes.get(i), relativeX, relativeY, relativeZ)) {
					return true;
				}
			}

			return false;
		}
	}

	private VehicleWeatherCover() {
	}
}
