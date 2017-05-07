package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameK extends BlockStationNameBase {

	private static final String name = "BlockStationNameKOT";

	public BlockStationNameK() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 10;
	}

	public static String getName() {
		return name;
	}
}