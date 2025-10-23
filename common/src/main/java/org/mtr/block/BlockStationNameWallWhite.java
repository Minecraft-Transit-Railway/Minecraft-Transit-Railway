package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameWallWhite extends BlockStationNameWallBase {

	public BlockStationNameWallWhite(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameWallWhiteBlockEntity(blockPos, blockState);
	}

	public static class StationNameWallWhiteBlockEntity extends BlockEntityWallBase {

		public StationNameWallWhiteBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_WHITE.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_WHITE;
		}
	}
}
