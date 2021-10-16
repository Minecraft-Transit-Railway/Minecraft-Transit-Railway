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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider {

	public BlockTactileMap(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient && TileEntityTactileMap.onUse != null) {
			TileEntityTactileMap.onUse.accept(pos);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
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

		public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;
		public static Consumer<BlockPos> onUse = null;

		public TileEntityTactileMap(BlockPos pos, BlockState state) {
			super(MTR.TACTILE_MAP_TILE_ENTITY, pos, state);
		}

		@Override
		public void markRemoved() {
			if (world != null && world.isClient && updateSoundSource != null) {
				updateSoundSource.accept(pos, true);
			}
			super.markRemoved();
		}

		public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
			if (world != null && world.isClient && updateSoundSource != null) {
				updateSoundSource.accept(pos, false);
			}
		}
	}
}

