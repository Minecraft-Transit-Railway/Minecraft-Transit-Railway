package org.mtr.mod;

import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.PlaceableOnWaterItemExtension;
import org.mtr.mapping.registry.ItemRegistryObject;
import org.mtr.mod.data.RailType;
import org.mtr.mod.item.*;

public final class Items {

	static {
		// Brush and dashboards
		BRUSH = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "brush"), itemSettings -> new Item(new ItemBrush(itemSettings)), CreativeModeTabs.CORE);
		RAILWAY_DASHBOARD = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "dashboard"), itemSettings -> new Item(new ItemDashboard(TransportMode.TRAIN, itemSettings)), CreativeModeTabs.CORE);
		BOAT_DASHBOARD = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "dashboard_2"), itemSettings -> new Item(new ItemDashboard(TransportMode.BOAT, itemSettings)), CreativeModeTabs.CORE);
		CABLE_CAR_DASHBOARD = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "dashboard_3"), itemSettings -> new Item(new ItemDashboard(TransportMode.CABLE_CAR, itemSettings)), CreativeModeTabs.CORE);
		AIRPLANE_DASHBOARD = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "dashboard_4"), itemSettings -> new Item(new ItemDashboard(TransportMode.AIRPLANE, itemSettings)), CreativeModeTabs.CORE);

		// Misc
		DRIVER_KEY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "driver_key"), itemSettings -> new Item(new ItemExtension(itemSettings.maxCount(1))));
		BOAT_NODE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "boat_node"), itemSettings -> new Item(new PlaceableOnWaterItemExtension(Blocks.BOAT_NODE.get(), itemSettings)), CreativeModeTabs.CORE);

		// Doors
		APG_DOOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "apg_door"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		APG_GLASS = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "apg_glass"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		APG_GLASS_END = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "apg_glass_end"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_door"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_GLASS_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_glass"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_GLASS_END_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_glass_end"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_DOOR_2 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_door_2"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_GLASS_2 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_glass_2"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);
		PSD_GLASS_END_2 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "psd_glass_end_2"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings)), CreativeModeTabs.RAILWAY_FACILITIES);

		// Escalators and lifts
		ESCALATOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "escalator"), itemSettings -> new Item(new ItemEscalator(itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_BUTTONS_LINK_CONNECTOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "lift_buttons_link_connector"), itemSettings -> new Item(new ItemLiftButtonsLinkModifier(true, itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_BUTTONS_LINK_REMOVER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "lift_buttons_link_remover"), itemSettings -> new Item(new ItemLiftButtonsLinkModifier(false, itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "lift_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.LIFT_DOOR_1, itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_DOOR_ODD_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "lift_door_odd_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.LIFT_DOOR_ODD_1, itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_REFRESHER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "lift_refresher"), itemSettings -> new Item(new ItemLiftRefresher(itemSettings)), CreativeModeTabs.ESCALATORS_LIFTS);

		// Rail connectors and remover
		RAIL_CONNECTOR_20 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_20"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.WOODEN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_20_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_20_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.WOODEN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_40 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_40"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.STONE, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_40_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_40_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.STONE, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_60 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_60"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.EMERALD, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_60_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_60_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.EMERALD, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_80 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_80"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.IRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_80_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_80_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.IRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_90 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_90"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.SMELTEDIRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_90_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_90_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.SMELTEDIRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_100 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_100"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.SOLIDIRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_100_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_100_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.SOLIDIRON, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_110 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_110"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.SLOWOBSIDIAN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_110_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_110_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.SLOWOBSIDIAN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_120 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_120"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.OBSIDIAN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_120_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_120_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.OBSIDIAN, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_160 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_160"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.BLAZE, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_160_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_160_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.BLAZE, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_200 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_200"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.QUARTZ, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_200_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_200_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.QUARTZ, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_300 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_300"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.DIAMOND, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_300_ONE_WAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_300_one_way"), itemSettings -> new Item(new ItemRailModifier(true, false, true, true, RailType.DIAMOND, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_PLATFORM = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_platform"), itemSettings -> new Item(new ItemRailModifier(true, true, true, false, RailType.PLATFORM, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_SIDING = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_siding"), itemSettings -> new Item(new ItemRailModifier(true, true, true, false, RailType.SIDING, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_TURN_BACK = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_turn_back"), itemSettings -> new Item(new ItemRailModifier(true, false, true, false, RailType.TURN_BACK, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_CABLE_CAR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_cable_car"), itemSettings -> new Item(new ItemRailModifier(false, true, false, true, RailType.CABLE_CAR, itemSettings)), CreativeModeTabs.CORE);
		RAIL_CONNECTOR_RUNWAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_connector_runway"), itemSettings -> new Item(new ItemRailModifier(false, false, true, true, RailType.RUNWAY, itemSettings)), CreativeModeTabs.CORE);
		RAIL_REMOVER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "rail_remover"), itemSettings -> new Item(new ItemRailModifier(itemSettings)), CreativeModeTabs.CORE);

		// Signal connectors
		SIGNAL_CONNECTOR_WHITE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_white"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[0], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_ORANGE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_orange"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[1], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_MAGENTA = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_magenta"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[2], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_LIGHT_BLUE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_light_blue"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[3], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_YELLOW = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_yellow"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[4], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_LIME = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_lime"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[5], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_PINK = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_pink"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[6], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_GRAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_gray"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[7], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_LIGHT_GRAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_light_gray"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[8], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_CYAN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_cyan"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[9], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_PURPLE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_purple"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[10], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_BLUE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_blue"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[11], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_BROWN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_brown"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[12], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_GREEN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_green"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[13], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_RED = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_red"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[14], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_CONNECTOR_BLACK = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_connector_black"), itemSettings -> new Item(new ItemSignalModifier(true, ItemSignalModifier.COLORS[15], itemSettings)), CreativeModeTabs.CORE);

		// Signal removers
		SIGNAL_REMOVER_WHITE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_white"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[0], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_ORANGE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_orange"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[1], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_MAGENTA = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_magenta"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[2], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_LIGHT_BLUE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_light_blue"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[3], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_YELLOW = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_yellow"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[4], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_LIME = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_lime"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[5], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_PINK = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_pink"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[6], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_GRAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_gray"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[7], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_LIGHT_GRAY = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_light_gray"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[8], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_CYAN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_cyan"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[9], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_PURPLE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_purple"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[10], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_BLUE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_blue"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[11], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_BROWN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_brown"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[12], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_GREEN = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_green"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[13], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_RED = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_red"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[14], itemSettings)), CreativeModeTabs.CORE);
		SIGNAL_REMOVER_BLACK = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "signal_remover_black"), itemSettings -> new Item(new ItemSignalModifier(false, ItemSignalModifier.COLORS[15], itemSettings)), CreativeModeTabs.CORE);

		// Building tools
		BRIDGE_CREATOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "bridge_creator_1"), itemSettings -> new Item(new ItemBridgeCreator(1, itemSettings)), CreativeModeTabs.CORE);
		BRIDGE_CREATOR_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "bridge_creator_3"), itemSettings -> new Item(new ItemBridgeCreator(3, itemSettings)), CreativeModeTabs.CORE);
		BRIDGE_CREATOR_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "bridge_creator_5"), itemSettings -> new Item(new ItemBridgeCreator(5, itemSettings)), CreativeModeTabs.CORE);
		BRIDGE_CREATOR_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "bridge_creator_7"), itemSettings -> new Item(new ItemBridgeCreator(7, itemSettings)), CreativeModeTabs.CORE);
		BRIDGE_CREATOR_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "bridge_creator_9"), itemSettings -> new Item(new ItemBridgeCreator(9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_4_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_4_3"), itemSettings -> new Item(new ItemTunnelCreator(4, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_4_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_4_5"), itemSettings -> new Item(new ItemTunnelCreator(4, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_4_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_4_7"), itemSettings -> new Item(new ItemTunnelCreator(4, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_4_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_4_9"), itemSettings -> new Item(new ItemTunnelCreator(4, 9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_5_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_5_3"), itemSettings -> new Item(new ItemTunnelCreator(5, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_5_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_5_5"), itemSettings -> new Item(new ItemTunnelCreator(5, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_5_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_5_7"), itemSettings -> new Item(new ItemTunnelCreator(5, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_5_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_5_9"), itemSettings -> new Item(new ItemTunnelCreator(5, 9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_6_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_6_3"), itemSettings -> new Item(new ItemTunnelCreator(6, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_6_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_6_5"), itemSettings -> new Item(new ItemTunnelCreator(6, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_6_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_6_7"), itemSettings -> new Item(new ItemTunnelCreator(6, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_CREATOR_6_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_creator_6_9"), itemSettings -> new Item(new ItemTunnelCreator(6, 9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_4_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_4_3"), itemSettings -> new Item(new ItemTunnelWallCreator(4, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_4_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_4_5"), itemSettings -> new Item(new ItemTunnelWallCreator(4, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_4_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_4_7"), itemSettings -> new Item(new ItemTunnelWallCreator(4, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_4_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_4_9"), itemSettings -> new Item(new ItemTunnelWallCreator(4, 9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_5_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_5_3"), itemSettings -> new Item(new ItemTunnelWallCreator(5, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_5_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_5_5"), itemSettings -> new Item(new ItemTunnelWallCreator(5, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_5_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_5_7"), itemSettings -> new Item(new ItemTunnelWallCreator(5, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_5_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_5_9"), itemSettings -> new Item(new ItemTunnelWallCreator(5, 9, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_6_3 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_6_3"), itemSettings -> new Item(new ItemTunnelWallCreator(6, 3, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_6_5 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_6_5"), itemSettings -> new Item(new ItemTunnelWallCreator(6, 5, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_6_7 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_6_7"), itemSettings -> new Item(new ItemTunnelWallCreator(6, 7, itemSettings)), CreativeModeTabs.CORE);
		TUNNEL_WALL_CREATOR_6_9 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "tunnel_wall_creator_6_9"), itemSettings -> new Item(new ItemTunnelWallCreator(6, 9, itemSettings)), CreativeModeTabs.CORE);
	}

	public static final ItemRegistryObject BRUSH;
	public static final ItemRegistryObject AIRPLANE_DASHBOARD;
	public static final ItemRegistryObject APG_DOOR;
	public static final ItemRegistryObject APG_GLASS;
	public static final ItemRegistryObject APG_GLASS_END;
	public static final ItemRegistryObject BOAT_DASHBOARD;
	public static final ItemRegistryObject BOAT_NODE;
	public static final ItemRegistryObject BRIDGE_CREATOR_1;
	public static final ItemRegistryObject BRIDGE_CREATOR_3;
	public static final ItemRegistryObject BRIDGE_CREATOR_5;
	public static final ItemRegistryObject BRIDGE_CREATOR_7;
	public static final ItemRegistryObject BRIDGE_CREATOR_9;
	public static final ItemRegistryObject CABLE_CAR_DASHBOARD;
	public static final ItemRegistryObject DRIVER_KEY;
	public static final ItemRegistryObject ESCALATOR;
	public static final ItemRegistryObject LIFT_BUTTONS_LINK_CONNECTOR;
	public static final ItemRegistryObject LIFT_BUTTONS_LINK_REMOVER;
	public static final ItemRegistryObject LIFT_DOOR_1;
	public static final ItemRegistryObject LIFT_DOOR_ODD_1;
	public static final ItemRegistryObject LIFT_REFRESHER;
	public static final ItemRegistryObject PSD_DOOR_1;
	public static final ItemRegistryObject PSD_DOOR_2;
	public static final ItemRegistryObject PSD_GLASS_1;
	public static final ItemRegistryObject PSD_GLASS_2;
	public static final ItemRegistryObject PSD_GLASS_END_1;
	public static final ItemRegistryObject PSD_GLASS_END_2;
	public static final ItemRegistryObject RAIL_CONNECTOR_120;
	public static final ItemRegistryObject RAIL_CONNECTOR_120_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_160;
	public static final ItemRegistryObject RAIL_CONNECTOR_160_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_20;
	public static final ItemRegistryObject RAIL_CONNECTOR_20_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_200;
	public static final ItemRegistryObject RAIL_CONNECTOR_200_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_300;
	public static final ItemRegistryObject RAIL_CONNECTOR_300_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_40;
	public static final ItemRegistryObject RAIL_CONNECTOR_40_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_60;
	public static final ItemRegistryObject RAIL_CONNECTOR_60_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_80;
	public static final ItemRegistryObject RAIL_CONNECTOR_80_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_90;
	public static final ItemRegistryObject RAIL_CONNECTOR_90_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_100;
	public static final ItemRegistryObject RAIL_CONNECTOR_100_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_110;
	public static final ItemRegistryObject RAIL_CONNECTOR_110_ONE_WAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_CABLE_CAR;
	public static final ItemRegistryObject RAIL_CONNECTOR_PLATFORM;
	public static final ItemRegistryObject RAIL_CONNECTOR_RUNWAY;
	public static final ItemRegistryObject RAIL_CONNECTOR_SIDING;
	public static final ItemRegistryObject RAIL_CONNECTOR_TURN_BACK;
	public static final ItemRegistryObject RAIL_REMOVER;
	public static final ItemRegistryObject RAILWAY_DASHBOARD;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_BLACK;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_BLUE;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_BROWN;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_CYAN;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_GRAY;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_GREEN;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_LIGHT_BLUE;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_LIGHT_GRAY;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_LIME;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_MAGENTA;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_ORANGE;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_PINK;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_PURPLE;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_RED;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_WHITE;
	public static final ItemRegistryObject SIGNAL_CONNECTOR_YELLOW;
	public static final ItemRegistryObject SIGNAL_REMOVER_BLACK;
	public static final ItemRegistryObject SIGNAL_REMOVER_BLUE;
	public static final ItemRegistryObject SIGNAL_REMOVER_BROWN;
	public static final ItemRegistryObject SIGNAL_REMOVER_CYAN;
	public static final ItemRegistryObject SIGNAL_REMOVER_GRAY;
	public static final ItemRegistryObject SIGNAL_REMOVER_GREEN;
	public static final ItemRegistryObject SIGNAL_REMOVER_LIGHT_BLUE;
	public static final ItemRegistryObject SIGNAL_REMOVER_LIGHT_GRAY;
	public static final ItemRegistryObject SIGNAL_REMOVER_LIME;
	public static final ItemRegistryObject SIGNAL_REMOVER_MAGENTA;
	public static final ItemRegistryObject SIGNAL_REMOVER_ORANGE;
	public static final ItemRegistryObject SIGNAL_REMOVER_PINK;
	public static final ItemRegistryObject SIGNAL_REMOVER_PURPLE;
	public static final ItemRegistryObject SIGNAL_REMOVER_RED;
	public static final ItemRegistryObject SIGNAL_REMOVER_WHITE;
	public static final ItemRegistryObject SIGNAL_REMOVER_YELLOW;
	public static final ItemRegistryObject TUNNEL_CREATOR_4_3;
	public static final ItemRegistryObject TUNNEL_CREATOR_4_5;
	public static final ItemRegistryObject TUNNEL_CREATOR_4_7;
	public static final ItemRegistryObject TUNNEL_CREATOR_4_9;
	public static final ItemRegistryObject TUNNEL_CREATOR_5_3;
	public static final ItemRegistryObject TUNNEL_CREATOR_5_5;
	public static final ItemRegistryObject TUNNEL_CREATOR_5_7;
	public static final ItemRegistryObject TUNNEL_CREATOR_5_9;
	public static final ItemRegistryObject TUNNEL_CREATOR_6_3;
	public static final ItemRegistryObject TUNNEL_CREATOR_6_5;
	public static final ItemRegistryObject TUNNEL_CREATOR_6_7;
	public static final ItemRegistryObject TUNNEL_CREATOR_6_9;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_4_3;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_4_5;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_4_7;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_4_9;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_5_3;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_5_5;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_5_7;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_5_9;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_6_3;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_6_5;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_6_7;
	public static final ItemRegistryObject TUNNEL_WALL_CREATOR_6_9;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway items");
	}
}
