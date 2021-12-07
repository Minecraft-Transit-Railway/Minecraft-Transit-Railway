package mapper;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.function.Supplier;

public abstract class PersistentStateMapper extends PersistentState {

	public PersistentStateMapper(String name) {
		super(name);
	}

	@Override
	public void fromTag(NbtCompound tag) {
		readNbt(tag);
	}

	public abstract void readNbt(NbtCompound nbtCompound);

	protected static <T extends PersistentStateMapper> T getInstance(World world, Supplier<T> supplier, String name) {
		return world instanceof ServerWorld ? ((ServerWorld) world).getPersistentStateManager().getOrCreate(supplier, name) : null;
	}
}
