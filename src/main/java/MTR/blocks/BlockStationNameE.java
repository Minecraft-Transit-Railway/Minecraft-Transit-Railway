package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameE extends BlockStationNameBase {

	private static final String name = "BlockStationNameTSH";

	public BlockStationNameE() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 4;
	}

	public static String getName() {
		return name;
	}
}
