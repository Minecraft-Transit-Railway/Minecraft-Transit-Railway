package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockLiftTrackFloor extends BlockLiftTrack implements EntityBlockMapper {

	public BlockLiftTrackFloor() {
		super();
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftTrackFloor(pos, state);
	}

	public static class TileEntityLiftTrackFloor extends BlockEntityClientSerializableMapper {

		private String floorNumber = "1";
		private boolean shouldDing;

		private static final String KEY_FLOOR_NUMBER = "floor_number";
		private static final String KEY_SHOULD_DING = "should_ding";

		public TileEntityLiftTrackFloor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_TRACK_FLOOR_1_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			floorNumber = compoundTag.getString(KEY_FLOOR_NUMBER);
			shouldDing = compoundTag.getBoolean(KEY_SHOULD_DING);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_FLOOR_NUMBER, floorNumber);
			compoundTag.putBoolean(KEY_SHOULD_DING, shouldDing);
		}

		public void setData(String floorNumber, boolean shouldDing) {
			this.floorNumber = floorNumber;
			this.shouldDing = shouldDing;
			setChanged();
			syncData();
		}

		public String getFloorNumber() {
			return floorNumber;
		}

		public boolean getShouldDing() {
			return shouldDing;
		}

		public void scanFloors(Map<Integer, String> floors) {
			floors.clear();
			scanFloors(floors, false);
			scanFloors(floors, true);
		}

		private void scanFloors(Map<Integer, String> floors, boolean upwards) {
			BlockPos checkPos = worldPosition.above(upwards ? 1 : -1);
			while (level != null && level.getBlockState(checkPos).getBlock() instanceof BlockLiftTrackFloor) {
				final BlockEntity blockEntity = level.getBlockEntity(checkPos);
				if (blockEntity instanceof TileEntityLiftTrackFloor) {
					floors.put(checkPos.getY(), ((TileEntityLiftTrackFloor) blockEntity).floorNumber);
				}
				checkPos = checkPos.above(upwards ? 1 : -1);
			}
		}
	}
}
