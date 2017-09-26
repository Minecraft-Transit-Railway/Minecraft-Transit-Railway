package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameR extends BlockStationNameBase {

	private static final String name = "BlockStationNameTWO";

	public BlockStationNameR() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 17;
	}

	public static String getName() {
		return name;
	}
}