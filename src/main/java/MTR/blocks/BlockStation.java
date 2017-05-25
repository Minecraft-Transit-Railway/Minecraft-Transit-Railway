package MTR.blocks;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;

public class BlockStation extends Block {

	private static final String name = "BlockStation";

	public BlockStation() {
		super(Material.glass);
		// GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRTab);
		setBlockUnbreakable();
		setResistance(-1F);
		setUnlocalizedName(name);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	public static String getName() {
		return name;
	}
}
