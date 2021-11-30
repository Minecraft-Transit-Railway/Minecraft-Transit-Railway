package mtr.block;

import mapper.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlockSignalLight2 extends BlockSignalLightBase {

	public BlockSignalLight2(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight2(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight2(pos, state);
	}

	public static class TileEntitySignalLight2 extends BlockEntityMapper {

		public TileEntitySignalLight2(BlockPos pos, BlockState state) {
			super(MTR.SIGNAL_LIGHT_2, pos, state);
		}
	}
}
