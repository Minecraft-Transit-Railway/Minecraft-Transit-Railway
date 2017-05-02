package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameT extends BlockStationNameBase {

	private static final String name = "BlockStationNameSHS";

	protected BlockStationNameT() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 19;
	}

	public static String getName() {
		return name;
	}
}