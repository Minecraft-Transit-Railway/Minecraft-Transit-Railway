package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameL extends BlockStationNameBase {

	private static final String name = "BlockStationNameTAW";

	protected BlockStationNameL() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 11;
	}

	public static String getName() {
		return name;
	}
}