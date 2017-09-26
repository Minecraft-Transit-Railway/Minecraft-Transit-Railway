package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameQ extends BlockStationNameBase {

	private static final String name = "BlockStationNameTAP";

	public BlockStationNameQ() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 16;
	}

	public static String getName() {
		return name;
	}
}