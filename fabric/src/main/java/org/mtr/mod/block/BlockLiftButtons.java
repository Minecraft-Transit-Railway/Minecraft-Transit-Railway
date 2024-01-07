package org.mtr.mod.block;

import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
import org.mtr.mod.packet.PacketDataBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockLiftButtons extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");

	public BlockLiftButtons() {
		super(BlockHelper.createBlockSettings(true));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final ActionResult result = IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
			world.setBlockState(pos, state.with(new Property<>(UNLOCKED.data), unlocked));
			player.sendMessage(new Text((unlocked ? TextHelper.translatable("gui.mtr.lift_buttons_unlocked") : TextHelper.translatable("gui.mtr.lift_buttons_locked")).data), true);
		});
		if (world.isClient() || result == ActionResult.SUCCESS) {
			return ActionResult.SUCCESS;
		} else {
			if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
				return ActionResult.PASS;
			} else {
				final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
				if (unlocked) {
					final org.mtr.mapping.holder.BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity != null && blockEntity.data instanceof BlockEntity) {
						final LiftDirection liftDirection = MathHelper.fractionalPart(hit.getPos().getYMapped()) < 0.25 ? LiftDirection.DOWN : LiftDirection.UP;
						final PressLift pressLift = new PressLift();
						((BlockEntity) blockEntity.data).trackPositions.forEach(blockPos -> pressLift.add(Init.blockPosToPosition(blockPos), liftDirection));
						PacketDataBase.sendHttpRequest("operation/press-lift", Utilities.getJsonObjectFromData(pressLift), jsonObject -> {
						});
						return ActionResult.SUCCESS;
					} else {
						return ActionResult.FAIL;
					}
				} else {
					return ActionResult.FAIL;
				}
			}
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction facing = ctx.getPlayerFacing();
		return getDefaultState2().with(new Property<>(FACING.data), facing.data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(UNLOCKED);
	}

	public static class BlockEntity extends BlockEntityExtension {

		private final ObjectOpenHashSet<BlockPos> trackPositions = new ObjectOpenHashSet<>();

		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_BUTTONS_1.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			trackPositions.clear();
			for (final long position : compoundTag.getLongArray(KEY_TRACK_FLOOR_POS)) {
				trackPositions.add(BlockPos.fromLong(position));
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			final List<Long> trackPositionsList = new ArrayList<>();
			trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
			compoundTag.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);
		}

		@Override
		public void blockEntityTick() {
			// TODO
		}

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (isAdd) {
				trackPositions.add(pos);
			} else {
				trackPositions.remove(pos);
			}
			markDirty2();
		}
	}
}
