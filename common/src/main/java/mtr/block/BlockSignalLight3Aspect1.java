package mtr.block;

import mtr.BlockEntityTypes;
import mtr.client.Config;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalLight3Aspect1 extends BlockSignalLightBase {

	public BlockSignalLight3Aspect1(Properties settings) {
		super(settings, 3, 16);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntitySignalLight3Aspect1(pos, state);
	}

	public static class TileEntitySignalLight3Aspect1 extends BlockEntityMapper {

		public TileEntitySignalLight3Aspect1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1.get(), pos, state);
		}

		@Environment(EnvType.CLIENT)
		public double getViewDistance() {
			return Config.signalMaxDistance();
		}
	}
}
