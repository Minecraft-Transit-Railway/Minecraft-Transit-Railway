package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider {

	public BlockTactileMap(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, 7, 14, facing);
		} else {
			return VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 12, 1, 12), IBlock.getVoxelShapeByDirection(6, 1, 7, 10, 16, 9, facing));
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == MTR.TACTILE_MAP_TILE_ENTITY ? TileEntityTactileMap::tick : null;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTactileMap(pos, state);
	}

	public static class TileEntityTactileMap extends BlockEntity {

		public static BiConsumer<BlockPos, Boolean> callback = null;

		public TileEntityTactileMap(BlockPos pos, BlockState state) {
			super(MTR.TACTILE_MAP_TILE_ENTITY, pos, state);
		}

		@Override
		public void markRemoved() {
			if (world != null && world.isClient && callback != null) {
				callback.accept(pos, true);
			}
			super.markRemoved();
		}

		public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
			if (world != null && world.isClient && callback != null) {
				callback.accept(pos, false);
			}
		}
	}
}

