package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.data.IGui;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements BlockWithEntity {

	public static final int MAX_OPEN_VALUE = 32;

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");
	public static final BooleanProperty TEMP = BooleanProperty.of("temp");

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (IBlock.getSideDirection(state) == direction && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			final BlockState superState = super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
			if (superState.getBlock().equals(Blocks.getAirMapped())) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.offset(IBlock.getSideDirection(state).getOpposite())).getBlock().data instanceof BlockPSDAPGGlassEndBase;
				return superState.with(new Property<>(END.data), end);
			}
		}
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.down();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.up(y));
				if (state.isOf(scanState.getBlock())) {
					lockDoor(world, pos.up(y), scanState, !unlocked);
				}
			}
			player.sendMessage(new Text((!unlocked ? TextHelper.translatable("gui.mtr.psd_apg_door_unlocked") : TextHelper.translatable("gui.mtr.psd_apg_door_locked")).data), true);
		});
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final BlockEntity entity = world.getBlockEntity(pos);
		return entity != null && entity.data instanceof BlockEntityBase && ((BlockEntityBase) entity.data).isOpen() ? VoxelShapes.empty() : super.getCollisionShape2(state, world, pos, context);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(END);
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE);
		properties.add(TEMP);
		properties.add(UNLOCKED);
	}

	private static void lockDoor(World world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final BlockState rightState = world.getBlockState(rightPos);

		if (leftState.isOf(state.getBlock())) {
			final BlockState toggled = leftState.with(new Property<>(UNLOCKED.data), unlocked);
			world.setBlockState(leftPos, toggled);
		}

		if (rightState.isOf(state.getBlock())) {
			final BlockState toggled = rightState.with(new Property<>(UNLOCKED.data), unlocked);
			world.setBlockState(rightPos, toggled);
		}

		world.setBlockState(pos, state.with(new Property<>(UNLOCKED.data), unlocked));
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension implements IGui {

		private int open;
		private float openClient;
		private boolean temp = true;

		private static final String KEY_OPEN = "open";
		private static final String KEY_TEMP = "temp";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			open = compoundTag.getInt(KEY_OPEN);
			temp = compoundTag.getBoolean(KEY_TEMP);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_OPEN, open);
			compoundTag.putBoolean(KEY_TEMP, temp);
			if (temp && getWorld2() != null) {
				getWorld2().setBlockState(getPos2(), getWorld2().getBlockState(getPos2()).with(new Property<>(TEMP.data), false));
				temp = false;
			}
		}

		@Override
		public void blockEntityTick() {
			if (getWorld2() != null && IBlock.getStatePropertySafe(getWorld2(), getPos2(), new Property<>(UNLOCKED.data))) {
				setOpen(0);
			}
		}

		public void setOpen(int open) {
			if (open != this.open) {
				this.open = open;
				markDirty2();
				if (open == 1 && getWorld2() != null) {
					getWorld2().setBlockState(getPos2(), getWorld2().getBlockState(getPos2()).with(new Property<>(TEMP.data), false));
				}
			}
		}

		public float getOpen(float lastFrameDuration) {
			final float change = lastFrameDuration * 0.95F;
			if (Math.abs(open - SMALL_OFFSET_16 * 2 - openClient) < change) {
				openClient = open - SMALL_OFFSET_16 * 2;
			} else if (openClient < open) {
				openClient += change;
			} else {
				openClient -= change;
			}
			return openClient / 32;
		}

		public boolean isOpen() {
			return open > 0;
		}
	}
}