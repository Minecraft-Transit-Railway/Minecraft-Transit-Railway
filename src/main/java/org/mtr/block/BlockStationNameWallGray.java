package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameWallGray extends BlockStationNameWallBase {

	public BlockStationNameWallGray(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameWallGrayBlockEntity(blockPos, blockState);
	}

	public static class StationNameWallGrayBlockEntity extends BlockEntityWallBase {

		public StationNameWallGrayBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_GRAY.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_LIGHT_GRAY;
		}
	}
}
