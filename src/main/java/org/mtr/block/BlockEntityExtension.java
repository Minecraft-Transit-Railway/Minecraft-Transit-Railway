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
//? if >= 26.1 {
/*import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///? }

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

//? if >= 26.1 {
	/*// Reading and writing go through a view rather than through the tag itself from 26.1. The whole
	// view is taken as one compound so that the block entities below, which name their own keys and
	// number in the dozens, are left alone.
	//
	// The codec has to be a map codec: a map codec reads and writes at the level it is given, so the
	// keys stay where they have always been, at the top of the block entity's own data. Anything that
	// nested them under a key of their own would compile just as well and would find nothing in a
	// world that already exists.
	//
	// Writing merges rather than replaces, so what the superclass has already written survives.
	private static final MapCodec<CompoundTag> NBT_CODEC = MapCodec.assumeMapUnsafe(CompoundTag.CODEC);

	@Override
	protected final void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);
		readNbt(valueInput.read(NBT_CODEC).orElseGet(CompoundTag::new));
	}

	@Override
	protected final void saveAdditional(ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		final CompoundTag nbtCompound = new CompoundTag();
		writeNbt(nbtCompound);
		valueOutput.store(NBT_CODEC, nbtCompound);
	}
*///? } else {
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
//? }

	protected void readNbt(CompoundTag nbtCompound) {
	}

	protected void writeNbt(CompoundTag nbtCompound) {
	}
}
