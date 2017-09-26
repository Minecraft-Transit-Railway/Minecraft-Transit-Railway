package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameG extends BlockStationNameBase {

	private static final String name = "BlockStationNameMOS";

	public BlockStationNameG() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 6;
	}
}
