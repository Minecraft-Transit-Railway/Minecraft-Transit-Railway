package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public abstract class BlockPoleCheckBase extends Block {

	public BlockPoleCheckBase(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState stateBelow = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
		if (isBlock(stateBelow.getBlock())) {
			return placeWithState(stateBelow);
		} else {
			return null;
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		final String[] strings = TranslationProvider.TOOLTIP_MTR_POLE_PLACEMENT.getString(getTooltipBlockText()).split("\n");
		for (final String string : strings) {
			tooltip.add(Text.literal(string).formatted(Formatting.GRAY));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return getDefaultState().with(Properties.HORIZONTAL_FACING, IBlock.getStatePropertySafe(stateBelow, Properties.HORIZONTAL_FACING));
	}

	protected abstract boolean isBlock(Block block);

	protected abstract Text getTooltipBlockText();
}
