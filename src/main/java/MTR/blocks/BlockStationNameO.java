package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameO extends BlockStationNameBase {

	private static final String name = "BlockStationNameRAC";

	public BlockStationNameO() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 14;
	}

	public static String getName() {
		return name;
	}
}