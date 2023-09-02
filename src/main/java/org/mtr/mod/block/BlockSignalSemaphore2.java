package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalSemaphore2 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore2(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalSemaphore2(pos, state);
	}

	public static class TileEntitySignalSemaphore2 extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase {

		public TileEntitySignalSemaphore2(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_2.get(), pos, state);
		}
	}
}
