package org.mtr.block;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockLiftTrackHorizontal extends BlockLiftTrackBase {

	public BlockLiftTrackHorizontal(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 10, 1, IBlock.getStatePropertySafe(state, Properties.FACING));
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_HORIZONTAL.getMutableText().formatted(Formatting.GRAY));
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, Properties.FACING);
		return ObjectArrayList.of(facing.rotateYClockwise(), facing.rotateYCounterclockwise());
	}
}
