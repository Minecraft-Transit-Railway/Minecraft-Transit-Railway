package mtr.block;

import minecraftmappings.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlockSignalLight4 extends BlockSignalLightBase {

	public BlockSignalLight4(Settings settings) {
		super(settings);
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
