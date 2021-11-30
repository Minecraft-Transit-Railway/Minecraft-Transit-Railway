package mapper;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.function.Supplier;

public abstract class PersistentStateMapper extends PersistentState {

	public PersistentStateMapper(String name) {
		super();
	}

	public abstract void readNbt(NbtCompound nbtCompound);

	protected static <T extends PersistentStateMapper> T getInstance(World world, Supplier<T> supplier, String name) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(nbtCompound -> {
				final T railwayData = supplier.get();
				railwayData.readNbt(nbtCompound);
				return railwayData;
			}, supplier, name);
		} else {
			return null;
		}
	}
}
