package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameM extends BlockStationNameBase {

	private static final String name = "BlockStationNameSHT";

	public BlockStationNameM() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 12;
	}

	public static String getName() {
		return name;
	}
}