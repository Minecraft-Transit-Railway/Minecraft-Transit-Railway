package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneF extends BlockMTRPane {

	private static final String name = "BlockMTRPaneHEO";

	public BlockMTRPaneF() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 5;
	}
}
