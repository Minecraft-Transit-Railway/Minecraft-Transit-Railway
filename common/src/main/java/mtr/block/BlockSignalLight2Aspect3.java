package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight2Aspect3 extends BlockSignalLightBase {

	public BlockSignalLight2Aspect3(Properties settings) {
		super(settings, 3, 16);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight2Aspect3(pos, state);
	}

	public static class TileEntitySignalLight2Aspect3 extends BlockEntityMapper {

		public TileEntitySignalLight2Aspect3(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3.get(), pos, state);
		}
	}
}
