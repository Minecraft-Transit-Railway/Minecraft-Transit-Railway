package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight3Aspect2 extends BlockSignalLightBase {

	public BlockSignalLight3Aspect2(Properties settings) {
		super(settings, 3, 16);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight3Aspect2(pos, state);
	}

	public static class TileEntitySignalLight3Aspect2 extends BlockEntityMapper {

		public TileEntitySignalLight3Aspect2(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_2.get(), pos, state);
		}
	}
}
