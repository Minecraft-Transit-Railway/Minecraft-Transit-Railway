package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneD extends BlockMTRPane {

	private static final String name = "BlockMTRPaneSHM";

	public BlockMTRPaneD() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 3;
	}

	public String getName() {
		return name;
	}
}
