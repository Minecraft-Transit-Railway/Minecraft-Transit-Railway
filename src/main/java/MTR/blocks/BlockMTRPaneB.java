package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneB extends BlockMTRPane {

	private static final String name = "BlockMTRPaneSTW";

	public BlockMTRPaneB() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 1;
	}
}
