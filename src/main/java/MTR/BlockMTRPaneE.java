package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRPaneE extends BlockMTRPane {

	private static final String name = "BlockMTRPaneTSH";

	protected BlockMTRPaneE() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 4;
	}

	public String getName() {
		return name;
	}
}
