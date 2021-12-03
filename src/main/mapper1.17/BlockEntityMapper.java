package mapper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityMapper extends BlockEntity {

	public BlockEntityMapper(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public final void readNbt(NbtCompound nbtCompound) {
		super.readNbt(nbtCompound);
		readNbtCompound(nbtCompound);
	}

	@Override
	public final NbtCompound writeNbt(NbtCompound nbtCompound) {
		super.writeNbt(nbtCompound);
		writeNbtCompound(nbtCompound);
		return nbtCompound;
	}

	public void readNbtCompound(NbtCompound nbtCompound) {
	}

	public void writeNbtCompound(NbtCompound nbtCompound) {
	}
}
