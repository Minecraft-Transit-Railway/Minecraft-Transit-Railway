package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight4Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight4Aspect1(Properties settings) {
		super(settings, 3, 16);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight4Aspect1(pos, state);
	}

	public static class TileEntitySignalLight4Aspect1 extends BlockEntityMapper {

		public TileEntitySignalLight4Aspect1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1.get(), pos, state);
		}
	}
}
