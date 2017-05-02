package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameB extends BlockStationNameBase {

	private static final String name = "BlockStationNameSTW";

	protected BlockStationNameB() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 1;
	}

	public static String getName() {
		return name;
	}
}
