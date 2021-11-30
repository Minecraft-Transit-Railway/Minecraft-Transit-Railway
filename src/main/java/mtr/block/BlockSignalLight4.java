package mtr.block;

import mapper.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlockSignalLight4 extends BlockSignalLightBase {

	public BlockSignalLight4(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight4(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight4(pos, state);
	}

	public static class TileEntitySignalLight4 extends BlockEntityMapper {

		public TileEntitySignalLight4(BlockPos pos, BlockState state) {
			super(MTR.SIGNAL_LIGHT_4, pos, state);
		}
	}
}
