package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalSemaphore1 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore1(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalSemaphore1(pos, state);
	}

	public static class TileEntitySignalSemaphore1 extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase {

		public TileEntitySignalSemaphore1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), pos, state);
		}
	}
}
