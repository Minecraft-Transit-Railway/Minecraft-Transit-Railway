package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider {

	public BlockTactileMap(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient() && TactileMapBlockEntity.onUse != null) {
			TactileMapBlockEntity.onUse.accept(pos);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, 7, 14, facing);
		} else {
			return VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 12, 1, 12), IBlock.getVoxelShapeByDirection(6, 1, 7, 10, 16, 9, facing));
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TactileMapBlockEntity(blockPos, blockState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BlockEntityTypes.TACTILE_MAP.get() && world.isClient && TactileMapBlockEntity.updateSoundSource != null ? (world1, pos, state1, blockEntity) -> TactileMapBlockEntity.updateSoundSource.accept(pos, false) : null;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
	}

	public static class TactileMapBlockEntity extends BlockEntity {

		public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;
		public static Consumer<BlockPos> onUse = null;

		public TactileMapBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TACTILE_MAP.get(), pos, state);
		}

		@Override
		public void markRemoved() {
			if (getWorld() != null && getWorld().isClient() && updateSoundSource != null) {
				updateSoundSource.accept(getPos(), true);
			}
			super.markRemoved();
		}
	}
}

