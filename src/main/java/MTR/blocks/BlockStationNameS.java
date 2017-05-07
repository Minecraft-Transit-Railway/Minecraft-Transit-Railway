package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameS extends BlockStationNameBase {

	private static final String name = "BlockStationNameFAN";

	public BlockStationNameS() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 18;
	}

	public static String getName() {
		return name;
	}
}