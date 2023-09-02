package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStationNameWallGray extends BlockStationNameWallBase {

	public BlockStationNameWallGray(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameWallGray(pos, state);
	}

	public static class TileEntityStationNameWallGray extends TileEntityStationNameWallBase {

		public TileEntityStationNameWallGray(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_GRAY_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_LIGHT_GRAY;
		}
	}
}
