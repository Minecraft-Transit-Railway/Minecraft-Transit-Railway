package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameWallBlack extends BlockStationNameWallBase {

	public BlockStationNameWallBlack(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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
