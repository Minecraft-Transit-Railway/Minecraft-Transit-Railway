package org.mtr.block;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketPressLiftButton;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;
import org.mtr.registry.RegistryClient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockLiftButtons extends BlockWaterloggable implements BlockEntityProvider {

	public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");

	public BlockLiftButtons(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		final ActionResult result = IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
			world.setBlockState(pos, state.with(UNLOCKED, unlocked));
			player.sendMessage((unlocked ? TranslationProvider.GUI_MTR_LIFT_BUTTONS_UNLOCKED : TranslationProvider.GUI_MTR_LIFT_BUTTONS_LOCKED).getText(), true);
		});

		if (result == ActionResult.SUCCESS) {
			return ActionResult.SUCCESS;
		} else {
			if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
				return ActionResult.PASS;
			} else {
				final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
				final double hitY = MathHelper.fractionalPart(hit.getPos().y);

				if (unlocked && hitY < 0.5) {
					// Special case: clientside button press
					if (world.isClient()) {
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
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		final Direction facing = itemPlacementContext.getHorizontalPlayerFacing();
		return super.getPlacementState(itemPlacementContext).with(Properties.FACING, facing);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 16, 1, IBlock.getStatePropertySafe(state, Properties.FACING));
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftButtonsBlockEntity(blockPos, blockState);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
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

	public static class LiftButtonsBlockEntity extends BlockEntity {

		private final ObjectOpenHashSet<BlockPos> trackPositions = new ObjectOpenHashSet<>();

		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public LiftButtonsBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_BUTTONS_1.createAndGet(), pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			trackPositions.clear();
			for (final long position : nbt.getLongArray(KEY_TRACK_FLOOR_POS)) {
				trackPositions.add(BlockPos.fromLong(position));
			}
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			final List<Long> trackPositionsList = new ArrayList<>();
			trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
			nbt.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);
		}

		public void registerFloor(BlockPos pos, boolean isAdd) {
			if (isAdd) {
				trackPositions.add(pos);
			} else {
				trackPositions.remove(pos);
			}
			markDirty();
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
