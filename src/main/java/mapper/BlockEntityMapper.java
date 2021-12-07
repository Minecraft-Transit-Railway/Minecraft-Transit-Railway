package mapper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityMapper extends BlockEntity {

	public BlockEntityMapper(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type);
	}

	public BlockEntityMapper(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void fromTag(BlockState state, NbtCompound tag) {
		super.fromTag(state, tag);
		readNbt(tag);
	}

	public void readNbt(NbtCompound tag) {
	}
}
