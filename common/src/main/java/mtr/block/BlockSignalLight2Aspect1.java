package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight2Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect1(Properties settings) {
		super(settings, 2, 14);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight2Aspect1(pos, state);
	}

	public static class TileEntitySignalLight2Aspect1 extends BlockEntityMapper {

		public TileEntitySignalLight2Aspect1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1.get(), pos, state);
		}
	}
}
