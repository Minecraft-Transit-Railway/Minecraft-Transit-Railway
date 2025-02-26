package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Init;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDeleteData;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLiftTrackFloor extends BlockLiftTrackBase implements BlockWithEntity {

	public BlockLiftTrackFloor() {
		super();
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final org.mtr.mapping.holder.BlockEntity entity = world.getBlockEntity(pos);
			if (entity != null && entity.data instanceof BlockEntity) {
				((BlockEntity) entity.data).markDirty2();
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient()) {
			PacketDeleteData.sendDirectlyToServerLiftFloorPosition(ServerWorld.cast(world), Init.blockPosToPosition(pos));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_FLOOR.getMutableText().formatted(TextFormatting.GRAY));
	}

	@Override
	public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
		final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
		return ObjectArrayList.of(Direction.UP, Direction.DOWN, facing.rotateYClockwise(), facing.rotateYCounterclockwise());
	}

	public static class BlockEntity extends BlockEntityExtension {

		private String floorNumber = "1";
		private String floorDescription = "";
		private boolean shouldDing;

		private static final String KEY_FLOOR_NUMBER = "floor_number";
		private static final String KEY_FLOOR_DESCRIPTION = "floor_description";
		private static final String KEY_SHOULD_DING = "should_ding";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_TRACK_FLOOR_1.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			floorNumber = compoundTag.getString(KEY_FLOOR_NUMBER);
			floorDescription = compoundTag.getString(KEY_FLOOR_DESCRIPTION);
			shouldDing = compoundTag.getBoolean(KEY_SHOULD_DING);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_FLOOR_NUMBER, floorNumber);
			compoundTag.putString(KEY_FLOOR_DESCRIPTION, floorDescription);
			compoundTag.putBoolean(KEY_SHOULD_DING, shouldDing);
		}

		public void setData(String floorNumber, String floorDescription, boolean shouldDing) {
			this.floorNumber = floorNumber;
			this.floorDescription = floorDescription;
			this.shouldDing = shouldDing;
			markDirty2();
		}

		public String getFloorNumber() {
			return floorNumber;
		}

		public String getFloorDescription() {
			return floorDescription;
		}

		public boolean getShouldDing() {
			return shouldDing;
		}
	}
}
