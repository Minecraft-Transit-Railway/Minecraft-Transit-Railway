package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneA extends BlockMTRPane {

	private static final String name = "BlockMTRPaneCKT";

	public BlockMTRPaneA() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 0;
	}
}
