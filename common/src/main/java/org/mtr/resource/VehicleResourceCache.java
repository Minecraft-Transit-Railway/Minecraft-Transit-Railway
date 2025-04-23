package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.util.math.Box;
import org.mtr.model.BuiltVehicleModelHolder;

public record VehicleResourceCache(
		ObjectArrayList<BuiltVehicleModelHolder> builtModels,
		ObjectArrayList<BuiltVehicleModelHolder> builtBogie1Models,
		ObjectArrayList<BuiltVehicleModelHolder> builtBogie2Models,
		ObjectImmutableList<Box> floors,
		ObjectImmutableList<Box> doorways
) {

	public void iterateModels(ModelConsumer modelConsumer) {
		for (int i = 0; i < builtModels.size(); i++) {
			modelConsumer.accept(i, builtModels.get(i));
		}
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, BuiltVehicleModelHolder builtVehicleModelHolder);
	}
}
