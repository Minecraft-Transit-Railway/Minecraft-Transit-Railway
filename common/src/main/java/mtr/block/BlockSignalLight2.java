package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight2 extends BlockSignalLightBase {

	public BlockSignalLight2(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight2(pos, state);
	}

	public static class TileEntitySignalLight2 extends BlockEntityMapper {

		public TileEntitySignalLight2(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2.get(), pos, state);
		}
	}
}
