package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameWallGray extends BlockStationNameWallBase {

	public BlockStationNameWallGray(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameWallGrayBlockEntity(blockPos, blockState);
	}

	public static class StationNameWallGrayBlockEntity extends BlockEntityWallBase {

		public StationNameWallGrayBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_GRAY.createAndGet(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_LIGHT_GRAY;
		}
	}
}
