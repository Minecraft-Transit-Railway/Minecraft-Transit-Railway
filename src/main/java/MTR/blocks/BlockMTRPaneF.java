package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneF extends BlockMTRPane {

	private static final String name = "BlockMTRPaneHEO";

	public BlockMTRPaneF() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 5;
	}

	public String getName() {
		return name;
	}
}
