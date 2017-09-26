package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameH extends BlockStationNameBase {

	private static final String name = "BlockStationNameWKS";

	public BlockStationNameH() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 7;
	}
}
