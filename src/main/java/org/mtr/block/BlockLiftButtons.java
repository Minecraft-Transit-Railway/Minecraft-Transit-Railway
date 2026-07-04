package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.packet.PacketPressLiftButton;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;
import org.mtr.registry.RegistryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockLiftButtons extends BlockWaterloggable implements EntityBlock {

	public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

	public BlockLiftButtons(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		final InteractionResult result = IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
			world.setBlockAndUpdate(pos, state.setValue(UNLOCKED, unlocked));
			player.displayClientMessage((unlocked ? TranslationProvider.GUI_MTR_LIFT_BUTTONS_UNLOCKED : TranslationProvider.GUI_MTR_LIFT_BUTTONS_LOCKED).getText(), true);
		});

		if (result == InteractionResult.SUCCESS) {
			return InteractionResult.SUCCESS;
		} else {
			if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
				return InteractionResult.PASS;
			} else {
				final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
				final double hitY = Mth.frac(hit.getLocation().y);

				if (unlocked && hitY < 0.5) {
					// Special case: clientside button press
					if (world.isClientSide()) {
						final BlockEntity blockEntity = world.getBlockEntity(pos);
						if (blockEntity instanceof LiftButtonsBlockEntity) {
							// Array order: has down button, has up button
							final boolean[] buttonStates = {false, false};
							((LiftButtonsBlockEntity) blockEntity).trackPositions.forEach(trackPosition -> BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floor, lift) -> {
							}));

							final LiftDirection liftDirection;
							if (buttonStates[0] && buttonStates[1]) {
								liftDirection = hitY < 0.25 ? LiftDirection.DOWN : LiftDirection.UP;
							} else {
								liftDirection = buttonStates[0] ? LiftDirection.DOWN : LiftDirection.UP;
							}

							final PressLift pressLift = new PressLift();
							((LiftButtonsBlockEntity) blockEntity).trackPositions.forEach(trackPosition -> pressLift.add(MTR.blockPosToPosition(trackPosition), liftDirection));
							RegistryClient.sendPacketToServer(new PacketPressLiftButton(pressLift));

							return InteractionResult.SUCCESS;
						} else {
							return InteractionResult.FAIL;
						}
					}

					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.FAIL;
				}
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
		final Direction facing = itemPlacementContext.getHorizontalDirection();
		return super.getStateForPlacement(itemPlacementContext).setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 16, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftButtonsBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(UNLOCKED);
	}

	/**
	 * @param trackPosition the position of the lift floor track
	 * @param buttonStates  an array with at least 2 elements: has down button, has up button
	 * @param callback      a callback for the lift and floor index, only run if the lift floor track exists in the lift
	 */
	public static void hasButtonsClient(BlockPos trackPosition, boolean[] buttonStates, FloorLiftCallback callback) {
		MinecraftClientData.getInstance().lifts.forEach(lift -> {
			final int floorIndex = lift.getFloorIndex(MTR.blockPosToPosition(trackPosition));
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

	public static class LiftButtonsBlockEntity extends BlockEntityExtension {

		private final ObjectOpenHashSet<BlockPos> trackPositions = new ObjectOpenHashSet<>();

		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public LiftButtonsBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_BUTTONS_1.get(), pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			trackPositions.clear();
			for (final long position : nbtCompound.getLongArray(KEY_TRACK_FLOOR_POS)) {
				trackPositions.add(BlockPos.of(position));
			}
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			final List<Long> trackPositionsList = new ArrayList<>();
			trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
			nbtCompound.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);
		}

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (isAdd) {
				trackPositions.add(pos);
			} else {
				trackPositions.remove(pos);
			}
			setChanged();
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
