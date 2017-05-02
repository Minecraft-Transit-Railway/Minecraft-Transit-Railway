package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneC extends BlockMTRPane {

	private static final String name = "BlockMTRPaneCIO";

	protected BlockMTRPaneC() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 2;
	}

	public String getName() {
		return name;
	}
}
