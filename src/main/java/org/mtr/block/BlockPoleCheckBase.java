package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public abstract class BlockPoleCheckBase extends Block {

	public BlockPoleCheckBase(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Nullable
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
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		final String[] strings = TranslationProvider.TOOLTIP_MTR_POLE_PLACEMENT.getString(getTooltipBlockText()).split("\n");
		for (final String string : strings) {
			tooltip.add(Component.literal(string).withStyle(ChatFormatting.GRAY));
		}
	}

	protected BlockState placeWithState(BlockState stateBelow) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, IBlock.getStatePropertySafe(stateBelow, BlockStateProperties.HORIZONTAL_FACING));
	}

	protected abstract boolean isBlock(BlockBehaviour block);

	protected abstract Component getTooltipBlockText();
}
