package mtr;

import mtr.data.RailType;
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
	Item PSD_DOOR_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_GLASS_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_GLASS_END_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_DOOR_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item PSD_GLASS_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item PSD_GLASS_END_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item RAIL_CONNECTOR_1_WOODEN = new ItemRailModifier(true, RailType.WOODEN);
	Item RAIL_CONNECTOR_2_STONE = new ItemRailModifier(true, RailType.STONE);
	Item RAIL_CONNECTOR_3_IRON = new ItemRailModifier(true, RailType.IRON);
	Item RAIL_CONNECTOR_4_OBSIDIAN = new ItemRailModifier(true, RailType.OBSIDIAN);
	Item RAIL_CONNECTOR_5_BLAZE = new ItemRailModifier(true, RailType.BLAZE);
	Item RAIL_CONNECTOR_6_DIAMOND = new ItemRailModifier(true, RailType.DIAMOND);
	Item RAIL_CONNECTOR_DEPOT = new ItemRailModifier(true, RailType.DEPOT);
	Item RAIL_CONNECTOR_PLATFORM = new ItemRailModifier(true, RailType.PLATFORM);
	Item RAIL_REMOVER = new ItemRailModifier(false, null);
}
