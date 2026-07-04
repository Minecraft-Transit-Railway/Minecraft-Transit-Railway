package org.mtr.resource;

import net.minecraft.world.phys.AABB;
import org.mtr.core.tool.ConditionalList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.model.BuiltVehicleModelHolder;

public final class VehicleResourceCache {

	private final ObjectArrayList<BuiltVehicleModelHolder> builtModels;
	public final ObjectArrayList<BuiltVehicleModelHolder> builtBogie1Models;
	public final ObjectArrayList<BuiltVehicleModelHolder> builtBogie2Models;
	public final ObjectImmutableList<AABB> floors;
	public final ObjectImmutableList<AABB> doorways;
	public final ObjectImmutableList<FloorWithNormalizedArea> floorsWithNormalizedArea;

	public VehicleResourceCache(
		ObjectArrayList<BuiltVehicleModelHolder> builtModels,
		ObjectArrayList<BuiltVehicleModelHolder> builtBogie1Models,
		ObjectArrayList<BuiltVehicleModelHolder> builtBogie2Models,
		ObjectImmutableList<AABB> floors,
		ObjectImmutableList<AABB> doorways
	) {
		this.builtModels = builtModels;
		this.builtBogie1Models = builtBogie1Models;
		this.builtBogie2Models = builtBogie2Models;
		this.floors = floors;
		this.doorways = doorways;

		double totalArea = 0;
		final ObjectArrayList<ObjectDoubleImmutablePair<AABB>> floorsWithArea = new ObjectArrayList<>();
		for (final AABB floor : floors) {
			final double area = floor.getXsize() * floor.getZsize();
			totalArea += area;
			floorsWithArea.add(new ObjectDoubleImmutablePair<>(floor, area));
		}

		double totalNormalizedArea = 0;
		final ObjectArrayList<FloorWithNormalizedArea> floorsWithNormalizedArea = new ObjectArrayList<>();
		if (totalArea > 0) {
			for (final ObjectDoubleImmutablePair<AABB> floorWithArea : floorsWithArea) {
				floorsWithNormalizedArea.add(new FloorWithNormalizedArea(floorWithArea.left(), totalNormalizedArea));
				totalNormalizedArea += floorWithArea.rightDouble() / totalArea;
			}
		}
		this.floorsWithNormalizedArea = new ObjectImmutableList<>(floorsWithNormalizedArea);
	}

	public void iterateModels(ModelConsumer modelConsumer) {
		for (int i = 0; i < builtModels.size(); i++) {
			modelConsumer.accept(i, builtModels.get(i));
		}
	}

	public static class FloorWithNormalizedArea implements ConditionalList {

		public final AABB floor;
		private final double normalizedArea;

		private FloorWithNormalizedArea(AABB floor, double normalizedArea) {
			this.floor = floor;
			this.normalizedArea = normalizedArea;
		}

		@Override
		public boolean matchesCondition(double value) {
			return value >= normalizedArea;
		}
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, BuiltVehicleModelHolder builtVehicleModelHolder);
	}
}
