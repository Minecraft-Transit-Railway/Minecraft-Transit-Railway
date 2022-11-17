package mtr.block;

import mtr.Items;
import mtr.MTR;
import mtr.mappings.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public abstract class BlockLiftPanelBase extends BlockDirectionalMapper implements EntityBlockMapper, ITripleBlock {

	private final boolean isOdd;
	private final boolean isFlat;

	@Deprecated
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	@Deprecated
	public static final BooleanProperty TEMP = BooleanProperty.create("temp");

	public BlockLiftPanelBase(boolean isOdd, boolean isFlat) {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5));
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public StateDefinition<Block, BlockState> getStateDefinition() {
		return super.getStateDefinition();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (isOdd) {
			return ITripleBlock.updateShape(state, direction, newState.is(this), () -> super.updateShape(state, direction, newState, world, pos, posFrom));
		} else {
			if (IBlock.getSideDirection(state) == direction && !newState.is(this)) {
				return Blocks.AIR.defaultBlockState();
			} else {
				return state;
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection();
		if (isOdd) {
			return IBlock.isReplaceable(ctx, direction.getClockWise(), 3) ? defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.LEFT).setValue(ODD, false) : null;
		} else {
			if (isFlat) {
				return IBlock.isReplaceable(ctx, direction.getClockWise(), 2) ? defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.LEFT) : null;
			} else {
				return IBlock.isReplaceable(ctx, direction.getClockWise(), 2) ? defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.LEFT).setValue(TEMP, false) : null;
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, isFlat ? 1 : 4, state.getValue(FACING));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!world.isClientSide) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);

			if (isOdd) {
				world.setBlock(pos.relative(direction.getClockWise()), defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.RIGHT).setValue(ODD, true), 3);
				world.setBlock(pos.relative(direction.getClockWise(), 2), defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.RIGHT).setValue(ODD, false), 3);
				world.updateNeighborsAt(pos.relative(direction.getClockWise()), Blocks.AIR);
				state.updateNeighbourShapes(world, pos.relative(direction.getClockWise()), 3);
			} else {
				if (isFlat) {
					world.setBlock(pos.relative(direction.getClockWise()), defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.RIGHT), 3);
				} else {
					world.setBlock(pos.relative(direction.getClockWise()), defaultBlockState().setValue(FACING, direction).setValue(SIDE, EnumSide.RIGHT).setValue(TEMP, false), 3);
				}
			}

			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (isOdd) {
			ITripleBlock.playerWillDestroy(world, pos, state, player, false);
		} else {
			if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
				IBlock.onBreakCreative(world, player, pos.relative(IBlock.getSideDirection(state)));
			}
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			return player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get()) ? InteractionResult.PASS : InteractionResult.FAIL;
		}
	}

	@Override
	public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
		TileEntityLiftPanel1Base.tick(world, pos, blockEntity);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(Text.translatable("tooltip.mtr.railway_sign_" + (isOdd ? "odd" : "even")).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}

	public abstract static class TileEntityLiftPanel1Base extends BlockEntityClientSerializableMapper implements TickableMapper {

		private BlockPos trackPosition = null;
		// TODO temp code start
		protected boolean converted = false;
		// TODO temp code end
		private final boolean isOdd;
		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";
		private static final String KEY_CONVERTED = "converted";
		private static final int UPDATE_INTERVAL = 60;

		public TileEntityLiftPanel1Base(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isOdd) {
			super(type, pos, state);
			this.isOdd = isOdd;
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			final long data = compoundTag.getLong(KEY_TRACK_FLOOR_POS);
			trackPosition = data == 0 ? null : BlockPos.of(data);
			converted = compoundTag.getBoolean(KEY_CONVERTED);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLong(KEY_TRACK_FLOOR_POS, trackPosition == null ? 0 : trackPosition.asLong());
			compoundTag.putBoolean(KEY_CONVERTED, converted);
		}

		@Override
		public void tick() {
			tick(level, worldPosition, this);
		}

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (level == null) {
				return;
			}

			setFloor(isAdd ? pos : null);
			final BlockState state = level.getBlockState(getBlockPos());
			final BlockEntity blockEntity = level.getBlockEntity(isOdd && IBlock.getStatePropertySafe(state, ODD) ? getBlockPos() : getBlockPos().relative(IBlock.getSideDirection(state)));
			if (blockEntity instanceof TileEntityLiftPanel1Base) {
				((TileEntityLiftPanel1Base) blockEntity).setFloor(isAdd ? pos : null);
			}
		}

		public BlockPos getTrackPosition(Level world) {
			if (trackPosition != null && !(world.getBlockEntity(trackPosition) instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor)) {
				trackPosition = null;
			}
			return trackPosition;
		}

		private void setFloor(BlockPos pos) {
			trackPosition = pos;
			setChanged();
			syncData();
		}

		// TODO temp code start
		protected void convert() {
			if (!converted) {
				converted = true;
				setChanged();
				syncData();
			}
		}
		// TODO temp code end

		public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
			if (world != null && world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16, entity -> true) != null && blockEntity instanceof TileEntityLiftPanel1Base && !world.isClientSide && MTR.isGameTickInterval(UPDATE_INTERVAL, (int) pos.asLong())) {
				((TileEntityLiftPanel1Base) blockEntity).getTrackPosition(world);
				blockEntity.setChanged();
				((TileEntityLiftPanel1Base) blockEntity).syncData();
			}
			if (world != null && !world.isClientSide && blockEntity instanceof TileEntityLiftPanel1Base) {
				((TileEntityLiftPanel1Base) blockEntity).convert();
			}
		}
	}
}
