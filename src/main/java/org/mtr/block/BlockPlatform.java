package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public class BlockPlatform extends Block implements PlatformHelper {

	private final boolean isIndented;

	public BlockPlatform(BlockBehaviour.Properties blockSettings, boolean isIndented) {
		super(blockSettings);
		this.isIndented = isIndented;
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		return PlatformHelper.getActualState(world, pos, state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (isIndented) {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			return Shapes.or(IBlock.getVoxelShapeByDirection(0, 0, 6, 16, 13, 16, facing), Block.box(0, 13, 0, 16, 16, 16));
		} else {
			return super.getShape(state, world, pos, context);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(DOOR_TYPE);
		builder.add(SIDE);
	}
}
