package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockSettings;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameWallBlack extends BlockStationNameWallBase {

	public BlockStationNameWallBlack(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityWallBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_BLACK.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_BLACK;
		}
	}
}
