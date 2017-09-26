package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {

	private static String name;

	public BlockBase(Material material, String name) {
		super(material);
		this.name = name;
		setCreativeTab(MTR.MTRTab);
		setHardness(5F);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

	public void registerItemModel(ItemBlock itemBlock) {
		MTR.proxy.registerItemRenderer(itemBlock, 0, name);
	}
}
