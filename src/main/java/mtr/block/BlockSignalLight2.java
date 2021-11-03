package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalLight2 extends BlockSignalLightBase {

	public BlockSignalLight2(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight2();
	}

	public static class TileEntitySignalLight2 extends BlockEntity {

		public TileEntitySignalLight2() {
			super(MTR.SIGNAL_LIGHT_2);
		}
	}
}
