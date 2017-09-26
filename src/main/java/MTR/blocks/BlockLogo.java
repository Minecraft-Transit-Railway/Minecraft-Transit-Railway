package MTR.blocks;

import MTR.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class BlockLogo extends BlockBase {

	private static final String name = "BlockLogo";

	public BlockLogo() {
		super(Material.ROCK, name);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}