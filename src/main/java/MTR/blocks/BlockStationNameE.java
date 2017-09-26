package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameE extends BlockStationNameBase {

	private static final String name = "BlockStationNameTSH";

	public BlockStationNameE() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 4;
	}
}
