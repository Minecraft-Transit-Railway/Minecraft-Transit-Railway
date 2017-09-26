package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameB extends BlockStationNameBase {

	private static final String name = "BlockStationNameSTW";

	public BlockStationNameB() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 1;
	}
}
