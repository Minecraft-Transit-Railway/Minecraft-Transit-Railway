package mtr.block;

import mtr.MTR;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockSignalLight1 extends HorizontalFacingBlock implements BlockEntityProvider {

	public BlockSignalLight1(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(5, 0, 5, 11, 12, 11);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight1();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static class TileEntitySignalLight1 extends BlockEntity {

		public TileEntitySignalLight1() {
			super(MTR.SIGNAL_LIGHT_1);
		}
	}
}
