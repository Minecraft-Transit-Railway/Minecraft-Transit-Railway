package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLogo extends Block {

	private static final String name = "BlockLogo";

	protected BlockLogo() {
		super(Material.rock);
		GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRtab);
		setHardness(0.5F);
		setUnlocalizedName(name);
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	public static String getName() {
		return name;
	}
}