package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneH extends BlockMTRPane {

	private static final String name = "BlockMTRPaneWKS";

	protected BlockMTRPaneH() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 7;
	}

	public String getName() {
		return name;
	}
}
