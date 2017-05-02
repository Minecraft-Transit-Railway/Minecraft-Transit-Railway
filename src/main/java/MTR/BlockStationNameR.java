package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameR extends BlockStationNameBase {

	private static final String name = "BlockStationNameTWO";

	protected BlockStationNameR() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 17;
	}

	public static String getName() {
		return name;
	}
}