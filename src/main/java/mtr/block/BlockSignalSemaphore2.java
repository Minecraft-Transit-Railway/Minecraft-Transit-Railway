package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalSemaphore2 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore2(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalSemaphore2();
	}

	public static class TileEntitySignalSemaphore2 extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase {

		public TileEntitySignalSemaphore2() {
			super(MTR.SIGNAL_SEMAPHORE_2);
		}
	}
}
