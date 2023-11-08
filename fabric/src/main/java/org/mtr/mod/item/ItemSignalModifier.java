package org.mtr.mod.item;

import org.mtr.core.data.SignalModification;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketData;

import javax.annotation.Nullable;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final MapColor color;

	public ItemSignalModifier(boolean isConnector, MapColor color, ItemSettings itemSettings) {
		super(true, false, true, isConnector, itemSettings);
		this.color = color;
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		final SignalModification signalModification = new SignalModification(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd), false);
		signalModification.putColorToAdd(color.getColorMapped());
		PacketData.modifySignal(ServerWorld.cast(world), signalModification);
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		final SignalModification signalModification = new SignalModification(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd), false);
		signalModification.putColorToRemove(color.getColorMapped());
		PacketData.modifySignal(ServerWorld.cast(world), signalModification);
	}
}
