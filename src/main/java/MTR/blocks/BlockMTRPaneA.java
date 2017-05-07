package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneA extends BlockMTRPane {

	private static final String name = "BlockMTRPaneCKT";

	public BlockMTRPaneA() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 0;
	}

	public String getName() {
		return name;
	}
}
