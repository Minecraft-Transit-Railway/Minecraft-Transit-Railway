package org.mtr.block;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.core.tool.Vector;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockLiftTrackDiagonal extends BlockLiftTrackBase implements IBlock {

	public BlockLiftTrackDiagonal(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		final Vec3d vector3d = context.getHitPos().rotateY((float) Math.toRadians(getFacing(context).getPositiveHorizontalDegrees()));
		return super.getPlacementState(context).with(HALF, MathHelper.fractionalPart(vector3d.y) < 0.5 ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER).with(SIDE, MathHelper.fractionalPart(vector3d.x) < 0.5 ? EnumSide.RIGHT : EnumSide.LEFT);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final boolean isRight = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
		return VoxelShapes.union(
				IBlock.getVoxelShapeByDirection(6, isUpper ? 6 : 0, 0, 10, isUpper ? 16 : 10, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING)),
				IBlock.getVoxelShapeByDirection(isRight ? 6 : 0, 6, 0, isRight ? 16 : 10, 10, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_DIAGONAL.getMutableText().formatted(Formatting.GRAY));
	}

	@Override
	public Vector getCenterPoint(BlockPos blockPos, BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, Properties.HORIZONTAL_FACING);
		final Direction newFacing = IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
		return new Vector(
				blockPos.getX() + 0.25 * newFacing.getOffsetX(),
				blockPos.getY() + 0.25 * (IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? 1 : -1),
				blockPos.getZ() + 0.25 * newFacing.getOffsetZ()
		);
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, Properties.HORIZONTAL_FACING);
		return ObjectArrayList.of(
				IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? Direction.UP : Direction.DOWN,
				IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise()
		);
	}
}
