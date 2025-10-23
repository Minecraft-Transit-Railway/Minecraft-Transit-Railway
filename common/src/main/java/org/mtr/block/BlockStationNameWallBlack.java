package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameWallBlack extends BlockStationNameWallBase {

	public BlockStationNameWallBlack(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameWallBlackBlockEntity(blockPos, blockState);
	}

	public static class StationNameWallBlackBlockEntity extends BlockEntityWallBase {

		public StationNameWallBlackBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_BLACK.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_BLACK;
		}
	}
}
