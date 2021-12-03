package mapper;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityClientSerializableMapper extends BlockEntityMapper implements BlockEntityClientSerializable {

	public BlockEntityClientSerializableMapper(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public final void fromClientTag(NbtCompound nbtCompound) {
		fromTag(world == null ? null : world.getBlockState(pos), nbtCompound);
	}

	@Override
	public final NbtCompound toClientTag(NbtCompound nbtCompound) {
		return writeNbt(nbtCompound);
	}
}
