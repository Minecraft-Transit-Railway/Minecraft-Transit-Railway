package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.core.tool.Vector;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public class BlockLiftTrackDiagonal extends BlockLiftTrackBase implements IBlock {

	public BlockLiftTrackDiagonal(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		final Vec3 vector3d = context.getClickLocation().yRot((float) Math.toRadians(getFacing(context).toYRot()));
		return super.getStateForPlacement(context).setValue(HALF, Mth.frac(vector3d.y) < 0.5 ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER).setValue(SIDE, Mth.frac(vector3d.x) < 0.5 ? EnumSide.RIGHT : EnumSide.LEFT);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final boolean isRight = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
		return Shapes.or(
			IBlock.getVoxelShapeByDirection(6, isUpper ? 6 : 0, 0, 10, isUpper ? 16 : 10, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING)),
			IBlock.getVoxelShapeByDirection(isRight ? 6 : 0, 6, 0, isRight ? 16 : 10, 10, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING))
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_DIAGONAL.getMutableText().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Vector getCenterPoint(BlockPos blockPos, BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
		final Direction newFacing = IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.getClockWise() : facing.getCounterClockWise();
		return new Vector(
			blockPos.getX() + 0.25 * newFacing.getStepX(),
			blockPos.getY() + 0.25 * (IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? 1 : -1),
			blockPos.getZ() + 0.25 * newFacing.getStepZ()
		);
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
		return ObjectArrayList.of(
			IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER ? Direction.UP : Direction.DOWN,
			IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT ? facing.getClockWise() : facing.getCounterClockWise()
		);
	}
}
