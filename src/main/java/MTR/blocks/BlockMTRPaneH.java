package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneH extends BlockMTRPane {

	private static final String name = "BlockMTRPaneWKS";

	public BlockMTRPaneH() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 7;
	}
}
