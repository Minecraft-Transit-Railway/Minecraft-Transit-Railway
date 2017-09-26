package MTR;

import net.minecraft.item.Item;

public class ItemBase extends Item {

	private static String[] name;

	public ItemBase(String[] name) {
		this(name, name[0]);
	}

	public ItemBase(String[] name, String name2) {
		this.name = name;
		setCreativeTab(MTR.MTRTab);
		setUnlocalizedName(name2);
		setRegistryName(name2);
	}

	public void registerItemModel() {
		for (int i = 0; i < name.length; i++)
			MTR.proxy.registerItemRenderer(this, i, name[i]);
	}
}
