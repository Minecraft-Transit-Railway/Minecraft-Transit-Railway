package mapper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityClientSerializableMapper extends BlockEntityMapper {

	public BlockEntityClientSerializableMapper(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void sync() {
		if (world instanceof ServerWorld) {
			((ServerWorld) world).getChunkManager().markForUpdate(pos);
		}
	}

	@Override
	public final BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public final NbtCompound toInitialChunkDataNbt() {
		final NbtCompound nbtCompound = super.toInitialChunkDataNbt();
		writeNbtCompound(nbtCompound);
		return nbtCompound;
	}
}
