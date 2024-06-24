package mtr.block;

import mtr.BlockEntityTypes;
import mtr.data.RailwayData;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockLiftTrackFloor extends BlockLiftTrack implements EntityBlockMapper {

	public BlockLiftTrackFloor() {
		super();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof TileEntityLiftTrackFloor) {
				((TileEntityLiftTrackFloor) entity).syncData();
				PacketTrainDataGuiServer.openLiftTrackFloorScreenS2C((ServerPlayer) player, pos);
			}
		});
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftTrackFloor(pos, state);
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.removeLiftFloorTrack(pos);
				PacketTrainDataGuiServer.removeLiftFloorTrackS2C(world, pos);
			}
		}
	}

	public static class TileEntityLiftTrackFloor extends BlockEntityClientSerializableMapper {

		private String floorNumber = "1";
		private String floorDescription = "";
		private boolean shouldDing;

		private static final String KEY_FLOOR_NUMBER = "floor_number";
		private static final String KEY_FLOOR_DESCRIPTION = "floor_description";
		private static final String KEY_SHOULD_DING = "should_ding";

		public TileEntityLiftTrackFloor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_TRACK_FLOOR_1_TILE_ENTITY.get(), pos, state);
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
			setChanged();
			syncData();
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
