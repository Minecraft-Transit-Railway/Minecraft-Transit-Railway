package mtr.block;

import mapper.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlockSignalLight3 extends BlockSignalLightBase {

	public BlockSignalLight3(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight3(pos, state);
	}

	public static class TileEntitySignalLight3 extends BlockEntityMapper {

		public TileEntitySignalLight3(BlockPos pos, BlockState state) {
			super(MTR.SIGNAL_LIGHT_3, pos, state);
		}
	}
}
