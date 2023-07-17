package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.MTR;
import mtr.data.LiftInstructions;
import mtr.mappings.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockLiftButtons extends BlockDirectionalMapper implements EntityBlockMapper {

	public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

	public BlockLiftButtons() {
		super(Properties.of().requiresCorrectToolForDrops().strength(2));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
		final InteractionResult result = IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
			world.setBlockAndUpdate(pos, state.setValue(UNLOCKED, unlocked));
			player.displayClientMessage(unlocked ? Text.translatable("gui.mtr.lift_buttons_unlocked") : Text.translatable("gui.mtr.lift_buttons_locked"), true);
		});
		if (world.isClientSide || result == InteractionResult.SUCCESS) {
			return InteractionResult.SUCCESS;
		} else {
			if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
				return InteractionResult.PASS;
			} else {
				final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
				if (unlocked) {
					final double y = hit.getLocation().y;
					LiftInstructions.addInstruction(world, pos, y - Math.floor(y) > 0.25);
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.FAIL;
				}
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction facing = ctx.getHorizontalDirection();
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftButtons(pos, state);
	}

	@Override
	public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
		TileEntityLiftButtons.tick(world, pos, blockEntity);
	}

	@Override
	public BlockEntityType<? extends BlockEntityMapper> getType() {
		return BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, UNLOCKED);
	}

	public static class TileEntityLiftButtons extends BlockEntityClientSerializableMapper implements TickableMapper {

		private final Set<BlockPos> trackPositions = new HashSet<>();

		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";
		private static final int UPDATE_INTERVAL = 60;

		public TileEntityLiftButtons(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			trackPositions.clear();
			for (final long position : compoundTag.getLongArray(KEY_TRACK_FLOOR_POS)) {
				trackPositions.add(BlockPos.of(position));
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			final List<Long> trackPositionsList = new ArrayList<>();
			trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
			compoundTag.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);
		}

		@Override
		public void tick() {
			tick(level, worldPosition, this);
		}

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (isAdd) {
				trackPositions.add(pos);
			} else {
				trackPositions.remove(pos);
			}
			setChanged();
			syncData();
		}

		public void forEachTrackPosition(Level world, BiConsumer<BlockPos, BlockLiftTrackFloor.TileEntityLiftTrackFloor> callback) {
			final Set<BlockPos> trackPositionsToRemove = new HashSet<>();
			trackPositions.forEach(trackPosition -> {
				final BlockEntity blockEntity = world.getBlockEntity(trackPosition);
				if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
					if (callback != null) {
						callback.accept(trackPosition, (BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity);
					}
				} else {
					trackPositionsToRemove.add(trackPosition);
				}
			});
			trackPositionsToRemove.forEach(trackPositions::remove);
		}

		public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
			if (world != null && world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16, entity -> true) != null && blockEntity instanceof TileEntityLiftButtons && !world.isClientSide && MTR.isGameTickInterval(UPDATE_INTERVAL, (int) pos.asLong())) {
				((TileEntityLiftButtons) blockEntity).forEachTrackPosition(world, null);
				blockEntity.setChanged();
				((TileEntityLiftButtons) blockEntity).syncData();
			}
		}
	}
}
