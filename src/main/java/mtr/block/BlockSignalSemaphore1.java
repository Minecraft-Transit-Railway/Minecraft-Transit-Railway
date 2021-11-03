package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalSemaphore1 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore1(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalSemaphore1();
	}

	public static class TileEntitySignalSemaphore1 extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase {

		public TileEntitySignalSemaphore1() {
			super(MTR.SIGNAL_SEMAPHORE_1);
		}
	}
}
