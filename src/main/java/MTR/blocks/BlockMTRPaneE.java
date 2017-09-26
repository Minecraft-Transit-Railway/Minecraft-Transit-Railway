package MTR.blocks;

import net.minecraft.block.state.IBlockState;

public class BlockMTRPaneE extends BlockMTRPane {

	private static final String name = "BlockMTRPaneTSH";

	public BlockMTRPaneE() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 4;
	}
}
