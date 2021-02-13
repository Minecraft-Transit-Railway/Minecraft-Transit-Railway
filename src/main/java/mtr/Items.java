package mtr;

import mtr.item.ItemDashboard;
import mtr.item.ItemEscalator;
import mtr.item.ItemPSDAPGBase;
import mtr.item.ItemRailModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public interface Items {

	Item APG_DOOR = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item APG_GLASS = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item APG_GLASS_END = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item BRUSH = new Item(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	Item DASHBOARD = new ItemDashboard(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	Item ESCALATOR = new ItemEscalator(new Item.Settings().group(ItemGroup.TRANSPORTATION));
	Item PSD_DOOR = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD);
	Item PSD_GLASS = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD);
	Item PSD_GLASS_END = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD);
	Item RAIL_CONNECTOR = new ItemRailModifier(true);
	Item RAIL_REMOVER = new ItemRailModifier(false);
}
