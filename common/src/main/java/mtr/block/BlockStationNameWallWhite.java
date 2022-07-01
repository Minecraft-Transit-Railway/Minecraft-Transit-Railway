package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStationNameWallWhite extends BlockStationNameWallBase {

	public BlockStationNameWallWhite(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameWallWhite(pos, state);
	}

	public static class TileEntityStationNameWallWhite extends TileEntityStationNameWallBase {

		public TileEntityStationNameWallWhite(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_WHITE_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public int getColor(BlockState state) {
			return ARGB_WHITE;
		}
	}
}
