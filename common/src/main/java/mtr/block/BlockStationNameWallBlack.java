package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStationNameWallBlack extends BlockStationNameWallBase {

	public BlockStationNameWallBlack(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameWallBlack(pos, state);
	}

	public static class TileEntityStationNameWallBlack extends TileEntityStationNameWallBase {

		public TileEntityStationNameWallBlack(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_BLACK_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_BLACK;
		}
	}
}
