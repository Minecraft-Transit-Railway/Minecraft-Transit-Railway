package mtr;

import mtr.item.ItemEscalator;
import mtr.item.ItemPSDAPGBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Items {

	public static final Item APG_DOOR = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.APG);
	public static final Item APG_GLASS = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.APG);
	public static final Item APG_GLASS_END = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.APG);
	public static final Item BRUSH = new Item(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final Item ESCALATOR = new ItemEscalator(new Item.Settings().group(ItemGroup.REDSTONE));
	public static final Item PSD_DOOR = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD);
	public static final Item PSD_GLASS = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD);
	public static final Item PSD_GLASS_END = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD);
}
