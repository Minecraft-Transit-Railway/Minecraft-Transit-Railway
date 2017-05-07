package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameJ extends BlockStationNameBase {

	private static final String name = "BlockStationNameMKK";

	public BlockStationNameJ() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 9;
	}

	public static String getName() {
		return name;
	}
}