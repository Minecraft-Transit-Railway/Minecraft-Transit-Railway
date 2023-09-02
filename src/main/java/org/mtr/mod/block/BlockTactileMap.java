package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.TickableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockTactileMap extends BlockDirectionalDoubleBlockBase implements EntityBlockMapper {

	public BlockTactileMap(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (world.isClientSide && TileEntityTactileMap.onUse != null) {
			TileEntityTactileMap.onUse.accept(pos);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 2, 16, 7, 14, facing);
		} else {
			return Shapes.or(Block.box(4, 0, 4, 12, 1, 12), IBlock.getVoxelShapeByDirection(6, 1, 7, 10, 16, 9, facing));
		}
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTactileMap(pos, state);
	}

	@Override
	public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
		TileEntityTactileMap.tick(world, pos);
	}

	@Override
	public BlockEntityType<? extends BlockEntityMapper> getType() {
		return BlockEntityTypes.TACTILE_MAP_TILE_ENTITY.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}

	public static class TileEntityTactileMap extends BlockEntityMapper implements TickableMapper {

		public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;
		public static Consumer<BlockPos> onUse = null;

		public TileEntityTactileMap(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TACTILE_MAP_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void tick() {
			tick(level, worldPosition);
		}

		@Override
		public void setRemoved() {
			if (level != null && level.isClientSide && updateSoundSource != null) {
				updateSoundSource.accept(worldPosition, true);
			}
			super.setRemoved();
		}

		public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos) {
			if (world != null && world.isClientSide && updateSoundSource != null) {
				updateSoundSource.accept(pos, false);
			}
		}
	}
}

