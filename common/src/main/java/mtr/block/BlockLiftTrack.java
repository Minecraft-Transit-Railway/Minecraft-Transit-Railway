package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLiftTrack extends BlockDirectionalMapper {

	public BlockLiftTrack() {
		super(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction facing;
		final Direction oppositeFace = ctx.getClickedFace().getOpposite();
		if (oppositeFace.getStepY() == 0) {
			facing = oppositeFace;
		} else {
			final BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(oppositeFace));
			if (state.getBlock() instanceof BlockLiftTrack) {
				facing = IBlock.getStatePropertySafe(state, FACING);
			} else {
				facing = ctx.getHorizontalDirection();
			}
		}
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
