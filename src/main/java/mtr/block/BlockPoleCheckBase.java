package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;

import java.util.List;

public abstract class BlockPoleCheckBase extends HorizontalFacingBlock {

	public BlockPoleCheckBase(Settings settings) {
		super(settings);
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
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		final String[] strings = new TranslatableText("tooltip.mtr.pole_placement", getTooltipBlockText()).getString().split("\n");
		for (final String string : strings) {
			tooltip.add(new LiteralText(string).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return getDefaultState().with(FACING, IBlock.getStatePropertySafe(stateBelow, FACING));
	}

	protected abstract boolean isBlock(Block block);

	protected abstract Text getTooltipBlockText();
}
