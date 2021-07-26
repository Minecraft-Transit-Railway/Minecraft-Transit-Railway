package mtr;

import mtr.data.RailType;
import mtr.item.ItemDashboard;
import mtr.item.ItemEscalator;
import mtr.item.ItemPSDAPGBase;
import mtr.item.ItemRailModifier;
import net.minecraft.item.Item;

public interface Items {

	Item APG_DOOR = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item APG_GLASS = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item APG_GLASS_END = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.APG);
	Item BRUSH = new Item(new Item.Settings().group(ItemGroups.CORE).maxCount(1));
	Item DASHBOARD = new ItemDashboard(new Item.Settings().group(ItemGroups.CORE).maxCount(1));
	Item ESCALATOR = new ItemEscalator(new Item.Settings().group(ItemGroups.RAILWAY_FACILITIES));
	Item PSD_DOOR_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_GLASS_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_GLASS_END_1 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_1);
	Item PSD_DOOR_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item PSD_GLASS_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item PSD_GLASS_END_2 = new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_2);
	Item RAIL_CONNECTOR_1_WOODEN = new ItemRailModifier(false, RailType.WOODEN);
	Item RAIL_CONNECTOR_1_WOODEN_ONE_WAY = new ItemRailModifier(true, RailType.WOODEN);
	Item RAIL_CONNECTOR_2_STONE = new ItemRailModifier(false, RailType.STONE);
	Item RAIL_CONNECTOR_2_STONE_ONE_WAY = new ItemRailModifier(true, RailType.STONE);
	Item RAIL_CONNECTOR_3_IRON = new ItemRailModifier(false, RailType.IRON);
	Item RAIL_CONNECTOR_3_IRON_ONE_WAY = new ItemRailModifier(true, RailType.IRON);
	Item RAIL_CONNECTOR_4_OBSIDIAN = new ItemRailModifier(false, RailType.OBSIDIAN);
	Item RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY = new ItemRailModifier(true, RailType.OBSIDIAN);
	Item RAIL_CONNECTOR_5_BLAZE = new ItemRailModifier(false, RailType.BLAZE);
	Item RAIL_CONNECTOR_5_BLAZE_ONE_WAY = new ItemRailModifier(true, RailType.BLAZE);
	Item RAIL_CONNECTOR_6_DIAMOND = new ItemRailModifier(false, RailType.DIAMOND);
	Item RAIL_CONNECTOR_6_DIAMOND_ONE_WAY = new ItemRailModifier(true, RailType.DIAMOND);
	Item RAIL_CONNECTOR_PLATFORM = new ItemRailModifier(false, RailType.PLATFORM);
	Item RAIL_CONNECTOR_SIDING = new ItemRailModifier(false, RailType.SIDING);
	Item RAIL_CONNECTOR_TURN_BACK = new ItemRailModifier(false, RailType.TURN_BACK);
	Item RAIL_REMOVER = new ItemRailModifier();
}
