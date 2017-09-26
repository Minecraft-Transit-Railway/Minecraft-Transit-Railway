package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneG extends BlockMTRPane {

	private static final String name = "BlockMTRPaneMOS";

	public BlockMTRPaneG() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 6;
	}
}
