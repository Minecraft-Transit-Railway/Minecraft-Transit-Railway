package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneD extends BlockMTRPane {

	private static final String name = "BlockMTRPaneSHM";

	public BlockMTRPaneD() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 3;
	}
}
