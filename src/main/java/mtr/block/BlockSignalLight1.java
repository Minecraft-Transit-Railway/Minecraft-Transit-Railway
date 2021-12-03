package mtr.block;

import mapper.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlockSignalLight1 extends BlockSignalLightBase {

	public BlockSignalLight1(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight1(pos, state);
	}

	public static class TileEntitySignalLight1 extends BlockEntityMapper {

		public TileEntitySignalLight1(BlockPos pos, BlockState state) {
			super(MTR.SIGNAL_LIGHT_1, pos, state);
		}
	}
}
