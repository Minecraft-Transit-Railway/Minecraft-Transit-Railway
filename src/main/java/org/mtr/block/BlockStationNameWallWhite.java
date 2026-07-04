package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameWallWhite extends BlockStationNameWallBase {

	public BlockStationNameWallWhite(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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
