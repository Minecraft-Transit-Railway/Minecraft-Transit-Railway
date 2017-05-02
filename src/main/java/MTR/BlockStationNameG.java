package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameG extends BlockStationNameBase {

	private static final String name = "BlockStationNameMOS";

	protected BlockStationNameG() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 6;
	}

	public static String getName() {
		return name;
	}
}
