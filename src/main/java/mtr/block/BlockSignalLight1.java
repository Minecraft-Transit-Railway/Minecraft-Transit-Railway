package mtr.block;

import mtr.MTR;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockSignalLight1 extends BlockSignalLightBase {

	public BlockSignalLight1(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntitySignalLight1();
	}

	public static class TileEntitySignalLight1 extends BlockEntity {

		public TileEntitySignalLight1() {
			super(MTR.SIGNAL_LIGHT_1);
		}
	}
}
