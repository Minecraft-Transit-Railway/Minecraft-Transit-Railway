package mtr.block;

import minecraftmappings.BlockEntityMapper;
import mtr.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight3 extends BlockSignalLightBase {

	public BlockSignalLight3(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight3(pos, state);
	}

	public static class TileEntitySignalLight3 extends BlockEntityMapper {

		public TileEntitySignalLight3(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_3, pos, state);
		}
	}
}
