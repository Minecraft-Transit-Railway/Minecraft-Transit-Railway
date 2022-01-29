package @package@.architectury.extensions;

		import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
		import net.minecraft.nbt.CompoundTag;
		import net.minecraft.world.level.block.state.BlockState;

public interface BlockEntityExtension extends BlockEntityClientSerializable {

	void loadClientData(BlockState pos, CompoundTag tag);

	CompoundTag saveClientData(CompoundTag tag);

	default void syncData() {
		sync();
	}
}
