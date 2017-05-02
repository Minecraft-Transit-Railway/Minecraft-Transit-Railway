package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameD extends BlockStationNameBase {

	private static final String name = "BlockStationNameSHM";

	protected BlockStationNameD() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 3;
	}

	public static String getName() {
		return name;
	}
}
