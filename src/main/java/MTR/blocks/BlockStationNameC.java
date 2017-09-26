package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameC extends BlockStationNameBase {

	private static final String name = "BlockStationNameCIO";

	public BlockStationNameC() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 2;
	}
}
