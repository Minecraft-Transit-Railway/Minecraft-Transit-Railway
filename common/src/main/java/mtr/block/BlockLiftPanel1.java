package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.MTR;
import mtr.mappings.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLiftPanel1 extends BlockDirectionalMapper implements EntityBlockMapper {

	public static final BooleanProperty LEFT = BooleanProperty.create("left");

	public BlockLiftPanel1() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (getOtherDirection(state) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection().getOpposite();
		return IBlock.isReplaceable(ctx, direction.getCounterClockWise(), 2) ? defaultBlockState().setValue(FACING, direction).setValue(LEFT, true) : null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 12, 16, 16, 16, state.getValue(FACING));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!world.isClientSide) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlock(pos.relative(direction.getCounterClockWise()), defaultBlockState().setValue(FACING, direction).setValue(LEFT, false), 3);
			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!IBlock.getStatePropertySafe(state, LEFT)) {
			IBlock.onBreakCreative(world, player, pos.relative(IBlock.getStatePropertySafe(state, FACING).getClockWise()));
		}
		super.playerWillDestroy(world, pos, state, player);
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
		TileEntityLiftPanel.tick(world, pos, blockEntity);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LEFT);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftPanel(pos, state);
	}

	private static Direction getOtherDirection(BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getStatePropertySafe(state, LEFT) ? facing.getCounterClockWise() : facing.getClockWise();
	}

	public static class TileEntityLiftPanel extends BlockEntityClientSerializableMapper implements TickableMapper {

		private BlockPos trackPosition = null;
		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";
		private static final int UPDATE_INTERVAL = 60;

		public TileEntityLiftPanel(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_1_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			final long data = compoundTag.getLong(KEY_TRACK_FLOOR_POS);
			trackPosition = data == 0 ? null : BlockPos.of(data);
			super.readCompoundTag(compoundTag);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLong(KEY_TRACK_FLOOR_POS, trackPosition == null ? 0 : trackPosition.asLong());
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
			final BlockEntity blockEntity = level.getBlockEntity(getBlockPos().relative(getOtherDirection(level.getBlockState(getBlockPos()))));
			if (blockEntity instanceof TileEntityLiftPanel) {
				((TileEntityLiftPanel) blockEntity).setFloor(isAdd ? pos : null);
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

		public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
			if (world != null && world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16, entity -> true) != null && blockEntity instanceof TileEntityLiftPanel && !world.isClientSide && MTR.isGameTickInterval(UPDATE_INTERVAL, (int) pos.asLong())) {
				((TileEntityLiftPanel) blockEntity).getTrackPosition(world);
				blockEntity.setChanged();
				((TileEntityLiftPanel) blockEntity).syncData();
			}
		}
	}
}
