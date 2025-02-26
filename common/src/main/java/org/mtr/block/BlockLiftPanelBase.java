package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.Items;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockLiftPanelBase extends BlockExtension implements IBlock, DirectionHelper, TripleHorizontalBlock, BlockWithEntity {

	private final boolean isOdd;
	private final boolean isFlat;

	public BlockLiftPanelBase(boolean isOdd, boolean isFlat) {
		super(org.mtr.mod.Blocks.createDefaultBlockSettings(true, blockState -> 5));
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (isOdd) {
			return TripleHorizontalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(new Block(this)), super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos));
		} else {
			if (IBlock.getSideDirection(state) == direction && !neighborState.isOf(new Block(this))) {
				return Blocks.getAirMapped().getDefaultState();
			} else {
				return state;
			}
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing();
		if (isOdd) {
			return TripleHorizontalBlock.getPlacementState(ctx, getDefaultState2());
		} else {
			return IBlock.isReplaceable(ctx, direction.rotateYClockwise(), 2) ? getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(SIDE.data), EnumSide.LEFT) : null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, isFlat ? 1 : 4, Direction.convert(state.get(new Property<>(FACING.data))));
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);

			if (isOdd) {
				TripleHorizontalBlock.onPlaced(world, pos, state, getDefaultState2());
			} else {
				world.setBlockState(pos.offset(direction.rotateYClockwise()), getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(SIDE.data), EnumSide.RIGHT), 3);
			}

			world.updateNeighbors(pos, Blocks.getAirMapped());
			state.updateNeighbors(new WorldAccess(world.data), pos, 3);
		}
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (isOdd) {
			TripleHorizontalBlock.onBreak(world, pos, state, player);
		} else {
			if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
				IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
			}
		}
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient()) {
			return ActionResult.SUCCESS;
		} else {
			return player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get()) ? ActionResult.PASS : ActionResult.FAIL;
		}
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add((isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN).getMutableText().formatted(TextFormatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntityExtension {

		private BlockPos trackPosition = null;
		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isOdd) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			final long data = compoundTag.getLong(KEY_TRACK_FLOOR_POS);
			trackPosition = data == 0 ? null : BlockPos.fromLong(data);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLong(KEY_TRACK_FLOOR_POS, trackPosition == null ? 0 : trackPosition.asLong());
		}

		public void registerFloor(World world, BlockPos pos, boolean isAdd) {
			if (IBlock.getStatePropertySafe(world, getPos2(), SIDE) == EnumSide.RIGHT) {
				final BlockEntity blockEntity = world.getBlockEntity(getPos2().offset(IBlock.getStatePropertySafe(world, getPos2(), FACING).rotateYCounterclockwise()));
				if (blockEntity != null && blockEntity.data instanceof BlockLiftPanelBase.BlockEntityBase) {
					((BlockEntityBase) blockEntity.data).registerFloor(world, pos, isAdd);
				}
			} else {
				if (isAdd) {
					trackPosition = pos;
				} else {
					trackPosition = null;
				}
				markDirty2();
			}
		}

		@Nullable
		public BlockPos getTrackPosition() {
			return trackPosition;
		}
	}
}
