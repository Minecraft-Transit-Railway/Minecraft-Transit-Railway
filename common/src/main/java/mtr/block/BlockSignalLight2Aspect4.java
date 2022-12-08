package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight2Aspect4 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect4(Properties settings) {
		super(settings, 3, 16);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight2Aspect4(pos, state);
	}

	public static class TileEntitySignalLight2Aspect4 extends BlockEntityMapper {

		public TileEntitySignalLight2Aspect4(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4.get(), pos, state);
		}
	}
}
