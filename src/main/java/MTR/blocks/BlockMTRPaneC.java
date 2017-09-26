package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneC extends BlockMTRPane {

	private static final String name = "BlockMTRPaneCIO";

	public BlockMTRPaneC() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 2;
	}
}
