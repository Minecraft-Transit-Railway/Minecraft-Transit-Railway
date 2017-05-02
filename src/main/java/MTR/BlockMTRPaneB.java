package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneB extends BlockMTRPane {

	private static final String name = "BlockMTRPaneSTW";

	protected BlockMTRPaneB() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 1;
	}

	public String getName() {
		return name;
	}
}
