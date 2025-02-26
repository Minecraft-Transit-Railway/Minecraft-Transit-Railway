package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPoleCheckBase extends BlockExtension implements DirectionHelper {

	public BlockPoleCheckBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		BlockState stateBelow = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
		if (isBlock(stateBelow.getBlock())) {
			return placeWithState(stateBelow);
		} else {
			return null;
		}
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		final String[] strings = TranslationProvider.TOOLTIP_MTR_POLE_PLACEMENT.getString(getTooltipBlockText().data).split("\n");
		for (final String string : strings) {
			tooltip.add(TextHelper.literal(string).formatted(TextFormatting.GRAY));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return getDefaultState2().with(new Property<>(FACING.data), IBlock.getStatePropertySafe(stateBelow, FACING).data);
	}

	protected abstract boolean isBlock(Block block);

	protected abstract Text getTooltipBlockText();
}
