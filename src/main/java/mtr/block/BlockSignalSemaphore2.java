package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlockSignalSemaphore2 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore2(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalSemaphore2(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalSemaphore2(pos, state);
	}

	public static class TileEntitySignalSemaphore2 extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase {

		public TileEntitySignalSemaphore2(BlockPos pos, BlockState state) {
			super(MTR.SIGNAL_SEMAPHORE_2, pos, state);
		}
	}
}
