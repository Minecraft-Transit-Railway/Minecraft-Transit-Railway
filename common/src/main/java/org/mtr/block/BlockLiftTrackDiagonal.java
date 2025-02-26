package org.mtr.mod.block;

import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLiftTrackDiagonal extends BlockLiftTrackBase implements IBlock {

	public BlockLiftTrackDiagonal() {
		super();
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext context) {
		final Vector3d vector3d = context.getHitPos().rotateY((float) Math.toRadians(getFacing(context).asRotation()));
		return super.getPlacementState2(context).with(new Property<>(HALF.data), MathHelper.fractionalPart(vector3d.getYMapped()) < 0.5 ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER).with(new Property<>(SIDE.data), MathHelper.fractionalPart(vector3d.getXMapped()) < 0.5 ? EnumSide.RIGHT : EnumSide.LEFT);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final boolean isRight = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
		return VoxelShapes.union(
				IBlock.getVoxelShapeByDirection(6, isUpper ? 6 : 0, 0, 10, isUpper ? 16 : 10, 1, IBlock.getStatePropertySafe(state, FACING)),
				IBlock.getVoxelShapeByDirection(isRight ? 6 : 0, 6, 0, isRight ? 16 : 10, 10, 1, IBlock.getStatePropertySafe(state, FACING))
		);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_DIAGONAL.getMutableText().formatted(TextFormatting.GRAY));
	}

	@Override
	public Vector getCenterPoint(BlockPos blockPos, BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		final Direction newFacing = IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
		return new Vector(
				blockPos.getX() + 0.25 * newFacing.getOffsetX(),
				blockPos.getY() + 0.25 * (IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? 1 : -1),
				blockPos.getZ() + 0.25 * newFacing.getOffsetZ()
		);
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		return ObjectArrayList.of(
				IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? Direction.UP : Direction.DOWN,
				IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise()
		);
	}
}
