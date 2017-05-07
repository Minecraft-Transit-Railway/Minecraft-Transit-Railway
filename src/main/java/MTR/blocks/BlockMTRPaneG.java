package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneG extends BlockMTRPane {

	private static final String name = "BlockMTRPaneMOS";

	public BlockMTRPaneG() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 6;
	}

	public String getName() {
		return name;
	}
}
