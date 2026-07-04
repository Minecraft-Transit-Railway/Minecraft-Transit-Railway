package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityExtension extends BlockEntity {

	public BlockEntityExtension(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public final Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public final CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}

	@Override
	protected final void loadAdditional(CompoundTag nbtCompound, HolderLookup.Provider registries) {
		super.loadAdditional(nbtCompound, registries);
		readNbt(nbtCompound);
	}

	@Override
	protected final void saveAdditional(CompoundTag nbtCompound, HolderLookup.Provider registries) {
		super.saveAdditional(nbtCompound, registries);
		writeNbt(nbtCompound);
	}

	protected void readNbt(CompoundTag nbtCompound) {
	}

	protected void writeNbt(CompoundTag nbtCompound) {
	}
}
