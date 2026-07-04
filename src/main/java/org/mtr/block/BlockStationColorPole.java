package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public class BlockStationColorPole extends Block {

	private final boolean showTooltip;

	public BlockStationColorPole(BlockBehaviour.Properties settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getStationPoleShape();
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		if (showTooltip) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR.getMutableText().withStyle(ChatFormatting.GRAY));
		}
	}

	public static VoxelShape getStationPoleShape() {
		return Block.box(6, 0, 6, 10, 16, 10);
	}
}
