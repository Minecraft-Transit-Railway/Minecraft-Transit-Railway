package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalLight4 extends BlockSignalLightBase {

	public BlockSignalLight4(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight4();
	}

	public static class TileEntitySignalLight4 extends BlockEntity {

		public TileEntitySignalLight4() {
			super(MTR.SIGNAL_LIGHT_4);
		}
	}
}
