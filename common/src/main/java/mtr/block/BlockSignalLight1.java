package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight1 extends BlockSignalLightBase {

	public BlockSignalLight1(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight1(pos, state);
	}

	public static class TileEntitySignalLight1 extends BlockEntityMapper {

		public TileEntitySignalLight1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_1.get(), pos, state);
		}
	}
}
