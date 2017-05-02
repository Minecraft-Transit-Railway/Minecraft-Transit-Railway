package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameP extends BlockStationNameBase {

	private static final String name = "BlockStationNameUNI";

	protected BlockStationNameP() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 15;
	}

	public static String getName() {
		return name;
	}
}