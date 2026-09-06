package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
//? if >= 26.1 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///? }
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.function.Consumer;

public class BlockLiftTrackHorizontal extends BlockLiftTrackBase {

	public BlockLiftTrackHorizontal(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 10, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
//? if >= 26.1 {
/*	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag options) {
*///? } else {
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipList, TooltipFlag options) {
		final Consumer<Component> tooltip = tooltipList::add;
//? }
		tooltip.accept(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_HORIZONTAL.getMutableText().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
		return ObjectArrayList.of(facing.getClockWise(), facing.getCounterClockWise());
	}
}
