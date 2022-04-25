package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight4 extends BlockSignalLightBase {

	public BlockSignalLight4(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight4(pos, state);
	}

	public static class TileEntitySignalLight4 extends BlockEntityMapper {

		public TileEntitySignalLight4(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4.get(), pos, state);
		}
	}
}
