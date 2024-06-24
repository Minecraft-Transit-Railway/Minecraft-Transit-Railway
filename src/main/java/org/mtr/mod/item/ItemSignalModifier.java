package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.core.tools.Angle;
import org.mtr.core.tools.DyeColor;
import org.mtr.mapping.holder.*;

import javax.annotation.Nullable;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final DyeColor color;

	public ItemSignalModifier(boolean isConnector, DyeColor color, ItemSettings itemSettings) {
		super(true, false, true, isConnector, itemSettings);
		this.color = color;
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		// TODO
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		// TODO
	}
}
