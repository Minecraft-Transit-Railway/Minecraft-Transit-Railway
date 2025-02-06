package org.mtr.mod.block;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.*;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketPressLiftButton;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockLiftButtons extends BlockWaterloggable implements DirectionHelper, BlockWithEntity {

	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");

	public BlockLiftButtons() {
		super(Blocks.createDefaultBlockSettings(true));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final ActionResult result = IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
			world.setBlockState(pos, state.with(new Property<>(UNLOCKED.data), unlocked));
			player.sendMessage((unlocked ? TranslationProvider.GUI_MTR_LIFT_BUTTONS_UNLOCKED : TranslationProvider.GUI_MTR_LIFT_BUTTONS_LOCKED).getText(), true);
		});

		if (result == ActionResult.SUCCESS) {
			return ActionResult.SUCCESS;
		} else {
			if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
				return ActionResult.PASS;
			} else {
				final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
				final double hitY = MathHelper.fractionalPart(hit.getPos().getYMapped());

				if (unlocked && hitY < 0.5) {
					// Special case: clientside button press
					if (world.isClient()) {
						final org.mtr.mapping.holder.BlockEntity blockEntity = world.getBlockEntity(pos);
						if (blockEntity != null && blockEntity.data instanceof BlockEntity) {
							// Array order: has down button, has up button
							final boolean[] buttonStates = {false, false};
							((BlockEntity) blockEntity.data).trackPositions.forEach(trackPosition -> BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floor, lift) -> {
							}));

							final LiftDirection liftDirection;
							if (buttonStates[0] && buttonStates[1]) {
								liftDirection = hitY < 0.25 ? LiftDirection.DOWN : LiftDirection.UP;
							} else {
								liftDirection = buttonStates[0] ? LiftDirection.DOWN : LiftDirection.UP;
							}

							final PressLift pressLift = new PressLift();
							((BlockEntity) blockEntity.data).trackPositions.forEach(trackPosition -> pressLift.add(Init.blockPosToPosition(trackPosition), liftDirection));
							InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketPressLiftButton(pressLift));

							return ActionResult.SUCCESS;
						} else {
							return ActionResult.FAIL;
						}
					}

					return ActionResult.SUCCESS;
				} else {
					return ActionResult.FAIL;
				}
			}
		}
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		final Direction facing = itemPlacementContext.getPlayerFacing();
		return super.getPlacementState2(itemPlacementContext).with(new Property<>(FACING.data), facing.data);
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
		super.addBlockProperties(properties);
		properties.add(FACING);
		properties.add(UNLOCKED);
	}

	/**
	 * @param trackPosition the position of the lift floor track
	 * @param buttonStates  an array with at least 2 elements: has down button, has up button
	 * @param callback      a callback for the lift and floor index, only run if the lift floor track exists in the lift
	 */
	public static void hasButtonsClient(BlockPos trackPosition, boolean[] buttonStates, FloorLiftCallback callback) {
		MinecraftClientData.getInstance().lifts.forEach(lift -> {
			final int floorIndex = lift.getFloorIndex(Init.blockPosToPosition(trackPosition));
			if (floorIndex > 0) {
				buttonStates[0] = true;
			}
			if (floorIndex >= 0 && floorIndex < lift.getFloorCount() - 1) {
				buttonStates[1] = true;
			}
			if (floorIndex >= 0) {
				callback.accept(floorIndex, lift);
			}
		});
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

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (isAdd) {
				trackPositions.add(pos);
			} else {
				trackPositions.remove(pos);
			}
			markDirty2();
		}

		public void forEachTrackPosition(Consumer<BlockPos> consumer) {
			trackPositions.forEach(consumer);
		}
	}

	@FunctionalInterface
	public interface FloorLiftCallback {
		void accept(int floor, Lift lift);
	}
}
