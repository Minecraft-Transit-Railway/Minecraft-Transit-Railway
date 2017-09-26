package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameD extends BlockStationNameBase {

	private static final String name = "BlockStationNameSHM";

	public BlockStationNameD() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 3;
	}
}
