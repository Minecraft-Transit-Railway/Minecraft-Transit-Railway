package mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockPoleCheckBase extends HorizontalDirectionalBlock {

	public BlockPoleCheckBase(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState stateBelow = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
		if (isBlock(stateBelow.getBlock())) {
			return placeWithState(stateBelow);
		} else {
			return null;
		}
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final String[] strings = new TranslatableComponent("tooltip.mtr.pole_placement", getTooltipBlockText()).getString().split("\n");
		for (final String string : strings) {
			tooltip.add(new TextComponent(string).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return defaultBlockState().setValue(FACING, IBlock.getStatePropertySafe(stateBelow, FACING));
	}

	protected abstract boolean isBlock(Block block);

	protected abstract Component getTooltipBlockText();
}
