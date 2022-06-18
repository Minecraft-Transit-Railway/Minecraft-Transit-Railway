package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockPoleCheckBase extends BlockDirectionalMapper {

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
		final String[] strings = Text.translatable("tooltip.mtr.pole_placement", getTooltipBlockText()).getString().split("\n");
		for (final String string : strings) {
			tooltip.add(Text.literal(string).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return defaultBlockState().setValue(FACING, IBlock.getStatePropertySafe(stateBelow, FACING));
	}

	protected abstract boolean isBlock(Block block);

	protected abstract Component getTooltipBlockText();
}
