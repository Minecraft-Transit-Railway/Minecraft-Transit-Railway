package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameU extends BlockStationNameBase {

	private static final String name = "BlockStationNameLOW";

	public BlockStationNameU() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 20;
	}

	public static String getName() {
		return name;
	}
}