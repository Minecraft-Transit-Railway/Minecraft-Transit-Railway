package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameI extends BlockStationNameBase {

	private static final String name = "BlockStationNameHUH";

	protected BlockStationNameI() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 8;
	}

	public static String getName() {
		return name;
	}
}