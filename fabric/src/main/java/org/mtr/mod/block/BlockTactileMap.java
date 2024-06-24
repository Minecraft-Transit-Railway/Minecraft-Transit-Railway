package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements BlockWithEntity {

	public BlockTactileMap(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient() && BlockEntity.onUse != null) {
			BlockEntity.onUse.accept(pos);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, 7, 14, facing);
		} else {
			return VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 12, 1, 12), IBlock.getVoxelShapeByDirection(6, 1, 7, 10, 16, 9, facing));
		}
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
	}

	public static class BlockEntity extends BlockEntityExtension {

		public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;
		public static Consumer<BlockPos> onUse = null;

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TACTILE_MAP.get(), pos, state);
		}

		@Override
		public void blockEntityTick() {
			if (getWorld2() != null && getWorld2().isClient() && updateSoundSource != null) {
				updateSoundSource.accept(getPos2(), false);
			}
		}

		@Override
		public void markRemoved2() {
			if (getWorld2() != null && getWorld2().isClient() && updateSoundSource != null) {
				updateSoundSource.accept(getPos2(), true);
			}
			super.markRemoved2();
		}
	}
}

