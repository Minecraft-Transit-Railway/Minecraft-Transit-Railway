package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalLight3 extends BlockSignalLightBase {

	public BlockSignalLight3(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight3();
	}

	public static class TileEntitySignalLight3 extends BlockEntity {

		public TileEntitySignalLight3() {
			super(MTR.SIGNAL_LIGHT_3);
		}
	}
}
