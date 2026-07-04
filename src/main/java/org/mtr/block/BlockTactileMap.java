package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.registry.BlockEntityTypes;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements EntityBlock {

	public BlockTactileMap(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide() && TactileMapBlockEntity.onUse != null) {
			TactileMapBlockEntity.onUse.accept(pos);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, 7, 14, facing);
		} else {
			return Shapes.or(Block.box(4, 0, 4, 12, 1, 12), IBlock.getVoxelShapeByDirection(6, 1, 7, 10, 16, 9, facing));
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TactileMapBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return type == BlockEntityTypes.TACTILE_MAP.get() && world.isClientSide && TactileMapBlockEntity.updateSoundSource != null ? (world1, pos, state1, blockEntity) -> TactileMapBlockEntity.updateSoundSource.accept(pos, false) : null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
	}

	public static class TactileMapBlockEntity extends BlockEntityExtension {

		@Nullable
		public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;
		@Nullable
		public static Consumer<BlockPos> onUse = null;

		public TactileMapBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TACTILE_MAP.get(), pos, state);
		}

		@Override
		public void setRemoved() {
			if (getLevel() != null && getLevel().isClientSide() && updateSoundSource != null) {
				updateSoundSource.accept(getBlockPos(), true);
			}
			super.setRemoved();
		}
	}
}

