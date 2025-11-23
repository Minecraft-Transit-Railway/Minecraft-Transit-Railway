package org.mtr.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityExtension extends BlockEntity {

	public BlockEntityExtension(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public final Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public final NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		return createNbt(registries);
	}

	@Override
	protected final void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(nbtCompound, registries);
		readNbt(nbtCompound);
	}

	@Override
	protected final void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(nbtCompound, registries);
		writeNbt(nbtCompound);
	}

	protected void readNbt(NbtCompound nbtCompound) {
	}

	protected void writeNbt(NbtCompound nbtCompound) {
	}
}
