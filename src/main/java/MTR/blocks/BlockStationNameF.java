package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameF extends BlockStationNameBase {

	private static final String name = "BlockStationNameHEO";

	public BlockStationNameF() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 5;
	}

	public static String getName() {
		return name;
	}
}
