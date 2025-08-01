package org.mtr.registry;

import net.minecraft.item.Item;
import net.minecraft.item.PlaceableOnWaterItem;
import org.mtr.MTR;
import org.mtr.core.data.TransportMode;
import org.mtr.data.RailType;
import org.mtr.item.*;

public final class Items {

	static {
		// Brush and dashboards
		BRUSH = Registry.registerItem("brush", ItemBrush::new, ItemGroups.CORE);
		RAILWAY_DASHBOARD = Registry.registerItem("dashboard", itemSettings -> new ItemDashboard(TransportMode.TRAIN, itemSettings), ItemGroups.CORE);
		BOAT_DASHBOARD = Registry.registerItem("dashboard_2", itemSettings -> new ItemDashboard(TransportMode.BOAT, itemSettings), ItemGroups.CORE);
		CABLE_CAR_DASHBOARD = Registry.registerItem("dashboard_3", itemSettings -> new ItemDashboard(TransportMode.CABLE_CAR, itemSettings), ItemGroups.CORE);
		AIRPLANE_DASHBOARD = Registry.registerItem("dashboard_4", itemSettings -> new ItemDashboard(TransportMode.AIRPLANE, itemSettings), ItemGroups.CORE);

		// Misc
		BASIC_DRIVER_KEY = Registry.registerItem("basic_driver_key", itemSettings -> new ItemDepotDriverKey(itemSettings.maxCount(1), true, false, false, 0xB6B6B6), null);
		ADVANCED_DRIVER_KEY = Registry.registerItem("advanced_driver_key", itemSettings -> new ItemDepotDriverKey(itemSettings.maxCount(1), true, true, false, 0xFFB6B6), null);
		GUARD_KEY = Registry.registerItem("guard_key", itemSettings -> new ItemDepotDriverKey(itemSettings.maxCount(1), false, true, false, 0xB6FFB6), null);
		CREATIVE_DRIVER_KEY = Registry.registerItem("creative_driver_key", itemSettings -> new ItemCreativeDriverKey(itemSettings.maxCount(1)), ItemGroups.CORE);
		BOAT_NODE = Registry.registerItem("boat_node", itemSettings -> new PlaceableOnWaterItem(Blocks.BOAT_NODE.get(), itemSettings), ItemGroups.CORE);

		// Doors
		APG_DOOR = Registry.registerItem("apg_door", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		APG_GLASS = Registry.registerItem("apg_glass", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		APG_GLASS_END = Registry.registerItem("apg_glass_end", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.APG, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_DOOR_1 = Registry.registerItem("psd_door", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_GLASS_1 = Registry.registerItem("psd_glass", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_GLASS_END_1 = Registry.registerItem("psd_glass_end", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_1, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_DOOR_2 = Registry.registerItem("psd_door_2", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_GLASS_2 = Registry.registerItem("psd_glass_2", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings), ItemGroups.RAILWAY_FACILITIES);
		PSD_GLASS_END_2 = Registry.registerItem("psd_glass_end_2", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_GLASS_END, ItemPSDAPGBase.EnumPSDAPGType.PSD_2, itemSettings), ItemGroups.RAILWAY_FACILITIES);

		// Escalators and lifts
		ESCALATOR = Registry.registerItem("escalator", ItemEscalator::new, ItemGroups.ESCALATORS_LIFTS);
		LIFT_BUTTONS_LINK_CONNECTOR = Registry.registerItem("lift_buttons_link_connector", itemSettings -> new ItemLiftButtonsLinkModifier(true, itemSettings), ItemGroups.ESCALATORS_LIFTS);
		LIFT_BUTTONS_LINK_REMOVER = Registry.registerItem("lift_buttons_link_remover", itemSettings -> new ItemLiftButtonsLinkModifier(false, itemSettings), ItemGroups.ESCALATORS_LIFTS);
		LIFT_DOOR_1 = Registry.registerItem("lift_door_1", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.LIFT_DOOR_1, itemSettings), ItemGroups.ESCALATORS_LIFTS);
		LIFT_DOOR_ODD_1 = Registry.registerItem("lift_door_odd_1", itemSettings -> new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.LIFT_DOOR_ODD_1, itemSettings), ItemGroups.ESCALATORS_LIFTS);
		LIFT_REFRESHER = Registry.registerItem("lift_refresher", ItemLiftRefresher::new, ItemGroups.ESCALATORS_LIFTS);

		// Rail connectors and remover
		RAIL_CONNECTOR_20 = Registry.registerItem("rail_connector_20", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.WOODEN, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_20_ONE_WAY = Registry.registerItem("rail_connector_20_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.WOODEN, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_40 = Registry.registerItem("rail_connector_40", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.STONE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_40_ONE_WAY = Registry.registerItem("rail_connector_40_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.STONE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_60 = Registry.registerItem("rail_connector_60", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.EMERALD, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_60_ONE_WAY = Registry.registerItem("rail_connector_60_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.EMERALD, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_80 = Registry.registerItem("rail_connector_80", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.IRON, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_80_ONE_WAY = Registry.registerItem("rail_connector_80_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.IRON, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_100 = Registry.registerItem("rail_connector_100", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.BRICKS, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_100_ONE_WAY = Registry.registerItem("rail_connector_100_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.BRICKS, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_120 = Registry.registerItem("rail_connector_120", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.OBSIDIAN, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_120_ONE_WAY = Registry.registerItem("rail_connector_120_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.OBSIDIAN, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_140 = Registry.registerItem("rail_connector_140", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.PRISMARINE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_140_ONE_WAY = Registry.registerItem("rail_connector_140_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.PRISMARINE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_160 = Registry.registerItem("rail_connector_160", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.BLAZE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_160_ONE_WAY = Registry.registerItem("rail_connector_160_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.BLAZE, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_200 = Registry.registerItem("rail_connector_200", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.QUARTZ, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_200_ONE_WAY = Registry.registerItem("rail_connector_200_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.QUARTZ, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_300 = Registry.registerItem("rail_connector_300", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.DIAMOND, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_300_ONE_WAY = Registry.registerItem("rail_connector_300_one_way", itemSettings -> new ItemRailModifier(true, false, true, true, RailType.DIAMOND, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_PLATFORM = Registry.registerItem("rail_connector_platform", itemSettings -> new ItemRailModifier(true, true, true, false, RailType.PLATFORM, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_SIDING = Registry.registerItem("rail_connector_siding", itemSettings -> new ItemRailModifier(true, true, true, false, RailType.SIDING, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_TURN_BACK = Registry.registerItem("rail_connector_turn_back", itemSettings -> new ItemRailModifier(true, false, true, false, RailType.TURN_BACK, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_CABLE_CAR = Registry.registerItem("rail_connector_cable_car", itemSettings -> new ItemRailModifier(false, true, false, true, RailType.CABLE_CAR, itemSettings), ItemGroups.CORE);
		RAIL_CONNECTOR_RUNWAY = Registry.registerItem("rail_connector_runway", itemSettings -> new ItemRailModifier(false, false, true, true, RailType.RUNWAY, itemSettings), ItemGroups.CORE);
		RAIL_REMOVER = Registry.registerItem("rail_remover", ItemRailModifier::new, ItemGroups.CORE);

		// Signal connectors
		SIGNAL_CONNECTOR_WHITE = Registry.registerItem("signal_connector_white", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[0], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_ORANGE = Registry.registerItem("signal_connector_orange", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[1], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_MAGENTA = Registry.registerItem("signal_connector_magenta", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[2], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_LIGHT_BLUE = Registry.registerItem("signal_connector_light_blue", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[3], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_YELLOW = Registry.registerItem("signal_connector_yellow", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[4], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_LIME = Registry.registerItem("signal_connector_lime", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[5], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_PINK = Registry.registerItem("signal_connector_pink", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[6], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_GRAY = Registry.registerItem("signal_connector_gray", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[7], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_LIGHT_GRAY = Registry.registerItem("signal_connector_light_gray", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[8], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_CYAN = Registry.registerItem("signal_connector_cyan", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[9], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_PURPLE = Registry.registerItem("signal_connector_purple", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[10], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_BLUE = Registry.registerItem("signal_connector_blue", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[11], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_BROWN = Registry.registerItem("signal_connector_brown", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[12], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_GREEN = Registry.registerItem("signal_connector_green", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[13], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_RED = Registry.registerItem("signal_connector_red", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[14], itemSettings), ItemGroups.CORE);
		SIGNAL_CONNECTOR_BLACK = Registry.registerItem("signal_connector_black", itemSettings -> new ItemSignalModifier(true, ItemSignalModifier.COLORS[15], itemSettings), ItemGroups.CORE);

		// Signal removers
		SIGNAL_REMOVER_WHITE = Registry.registerItem("signal_remover_white", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[0], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_ORANGE = Registry.registerItem("signal_remover_orange", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[1], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_MAGENTA = Registry.registerItem("signal_remover_magenta", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[2], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_LIGHT_BLUE = Registry.registerItem("signal_remover_light_blue", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[3], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_YELLOW = Registry.registerItem("signal_remover_yellow", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[4], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_LIME = Registry.registerItem("signal_remover_lime", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[5], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_PINK = Registry.registerItem("signal_remover_pink", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[6], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_GRAY = Registry.registerItem("signal_remover_gray", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[7], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_LIGHT_GRAY = Registry.registerItem("signal_remover_light_gray", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[8], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_CYAN = Registry.registerItem("signal_remover_cyan", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[9], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_PURPLE = Registry.registerItem("signal_remover_purple", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[10], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_BLUE = Registry.registerItem("signal_remover_blue", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[11], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_BROWN = Registry.registerItem("signal_remover_brown", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[12], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_GREEN = Registry.registerItem("signal_remover_green", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[13], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_RED = Registry.registerItem("signal_remover_red", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[14], itemSettings), ItemGroups.CORE);
		SIGNAL_REMOVER_BLACK = Registry.registerItem("signal_remover_black", itemSettings -> new ItemSignalModifier(false, ItemSignalModifier.COLORS[15], itemSettings), ItemGroups.CORE);

		// Building tools
		BRIDGE_CREATOR_1 = Registry.registerItem("bridge_creator_1", itemSettings -> new ItemBridgeCreator(1, itemSettings), ItemGroups.CORE);
		BRIDGE_CREATOR_3 = Registry.registerItem("bridge_creator_3", itemSettings -> new ItemBridgeCreator(3, itemSettings), ItemGroups.CORE);
		BRIDGE_CREATOR_5 = Registry.registerItem("bridge_creator_5", itemSettings -> new ItemBridgeCreator(5, itemSettings), ItemGroups.CORE);
		BRIDGE_CREATOR_7 = Registry.registerItem("bridge_creator_7", itemSettings -> new ItemBridgeCreator(7, itemSettings), ItemGroups.CORE);
		BRIDGE_CREATOR_9 = Registry.registerItem("bridge_creator_9", itemSettings -> new ItemBridgeCreator(9, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_4_3 = Registry.registerItem("tunnel_creator_4_3", itemSettings -> new ItemTunnelCreator(4, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_4_5 = Registry.registerItem("tunnel_creator_4_5", itemSettings -> new ItemTunnelCreator(4, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_4_7 = Registry.registerItem("tunnel_creator_4_7", itemSettings -> new ItemTunnelCreator(4, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_4_9 = Registry.registerItem("tunnel_creator_4_9", itemSettings -> new ItemTunnelCreator(4, 9, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_5_3 = Registry.registerItem("tunnel_creator_5_3", itemSettings -> new ItemTunnelCreator(5, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_5_5 = Registry.registerItem("tunnel_creator_5_5", itemSettings -> new ItemTunnelCreator(5, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_5_7 = Registry.registerItem("tunnel_creator_5_7", itemSettings -> new ItemTunnelCreator(5, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_5_9 = Registry.registerItem("tunnel_creator_5_9", itemSettings -> new ItemTunnelCreator(5, 9, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_6_3 = Registry.registerItem("tunnel_creator_6_3", itemSettings -> new ItemTunnelCreator(6, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_6_5 = Registry.registerItem("tunnel_creator_6_5", itemSettings -> new ItemTunnelCreator(6, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_6_7 = Registry.registerItem("tunnel_creator_6_7", itemSettings -> new ItemTunnelCreator(6, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_CREATOR_6_9 = Registry.registerItem("tunnel_creator_6_9", itemSettings -> new ItemTunnelCreator(6, 9, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_4_3 = Registry.registerItem("tunnel_wall_creator_4_3", itemSettings -> new ItemTunnelWallCreator(4, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_4_5 = Registry.registerItem("tunnel_wall_creator_4_5", itemSettings -> new ItemTunnelWallCreator(4, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_4_7 = Registry.registerItem("tunnel_wall_creator_4_7", itemSettings -> new ItemTunnelWallCreator(4, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_4_9 = Registry.registerItem("tunnel_wall_creator_4_9", itemSettings -> new ItemTunnelWallCreator(4, 9, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_5_3 = Registry.registerItem("tunnel_wall_creator_5_3", itemSettings -> new ItemTunnelWallCreator(5, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_5_5 = Registry.registerItem("tunnel_wall_creator_5_5", itemSettings -> new ItemTunnelWallCreator(5, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_5_7 = Registry.registerItem("tunnel_wall_creator_5_7", itemSettings -> new ItemTunnelWallCreator(5, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_5_9 = Registry.registerItem("tunnel_wall_creator_5_9", itemSettings -> new ItemTunnelWallCreator(5, 9, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_6_3 = Registry.registerItem("tunnel_wall_creator_6_3", itemSettings -> new ItemTunnelWallCreator(6, 3, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_6_5 = Registry.registerItem("tunnel_wall_creator_6_5", itemSettings -> new ItemTunnelWallCreator(6, 5, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_6_7 = Registry.registerItem("tunnel_wall_creator_6_7", itemSettings -> new ItemTunnelWallCreator(6, 7, itemSettings), ItemGroups.CORE);
		TUNNEL_WALL_CREATOR_6_9 = Registry.registerItem("tunnel_wall_creator_6_9", itemSettings -> new ItemTunnelWallCreator(6, 9, itemSettings), ItemGroups.CORE);
	}

	public static final ObjectHolder<Item> BRUSH;
	public static final ObjectHolder<Item> AIRPLANE_DASHBOARD;
	public static final ObjectHolder<Item> APG_DOOR;
	public static final ObjectHolder<Item> APG_GLASS;
	public static final ObjectHolder<Item> APG_GLASS_END;
	public static final ObjectHolder<Item> BOAT_DASHBOARD;
	public static final ObjectHolder<Item> BOAT_NODE;
	public static final ObjectHolder<Item> BRIDGE_CREATOR_1;
	public static final ObjectHolder<Item> BRIDGE_CREATOR_3;
	public static final ObjectHolder<Item> BRIDGE_CREATOR_5;
	public static final ObjectHolder<Item> BRIDGE_CREATOR_7;
	public static final ObjectHolder<Item> BRIDGE_CREATOR_9;
	public static final ObjectHolder<Item> CABLE_CAR_DASHBOARD;
	public static final ObjectHolder<Item> GUARD_KEY;
	public static final ObjectHolder<Item> BASIC_DRIVER_KEY;
	public static final ObjectHolder<Item> ADVANCED_DRIVER_KEY;
	public static final ObjectHolder<Item> CREATIVE_DRIVER_KEY;
	public static final ObjectHolder<Item> ESCALATOR;
	public static final ObjectHolder<Item> LIFT_BUTTONS_LINK_CONNECTOR;
	public static final ObjectHolder<Item> LIFT_BUTTONS_LINK_REMOVER;
	public static final ObjectHolder<Item> LIFT_DOOR_1;
	public static final ObjectHolder<Item> LIFT_DOOR_ODD_1;
	public static final ObjectHolder<Item> LIFT_REFRESHER;
	public static final ObjectHolder<Item> PSD_DOOR_1;
	public static final ObjectHolder<Item> PSD_DOOR_2;
	public static final ObjectHolder<Item> PSD_GLASS_1;
	public static final ObjectHolder<Item> PSD_GLASS_2;
	public static final ObjectHolder<Item> PSD_GLASS_END_1;
	public static final ObjectHolder<Item> PSD_GLASS_END_2;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_100;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_100_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_120;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_120_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_140;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_140_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_160;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_160_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_20;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_20_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_200;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_200_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_300;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_300_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_40;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_40_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_60;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_60_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_80;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_80_ONE_WAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_CABLE_CAR;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_PLATFORM;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_RUNWAY;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_SIDING;
	public static final ObjectHolder<Item> RAIL_CONNECTOR_TURN_BACK;
	public static final ObjectHolder<Item> RAIL_REMOVER;
	public static final ObjectHolder<Item> RAILWAY_DASHBOARD;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_BLACK;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_BLUE;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_BROWN;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_CYAN;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_GRAY;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_GREEN;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_LIGHT_BLUE;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_LIGHT_GRAY;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_LIME;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_MAGENTA;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_ORANGE;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_PINK;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_PURPLE;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_RED;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_WHITE;
	public static final ObjectHolder<Item> SIGNAL_CONNECTOR_YELLOW;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_BLACK;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_BLUE;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_BROWN;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_CYAN;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_GRAY;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_GREEN;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_LIGHT_BLUE;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_LIGHT_GRAY;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_LIME;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_MAGENTA;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_ORANGE;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_PINK;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_PURPLE;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_RED;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_WHITE;
	public static final ObjectHolder<Item> SIGNAL_REMOVER_YELLOW;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_4_3;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_4_5;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_4_7;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_4_9;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_5_3;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_5_5;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_5_7;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_5_9;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_6_3;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_6_5;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_6_7;
	public static final ObjectHolder<Item> TUNNEL_CREATOR_6_9;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_4_3;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_4_5;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_4_7;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_4_9;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_5_3;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_5_5;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_5_7;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_5_9;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_6_3;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_6_5;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_6_7;
	public static final ObjectHolder<Item> TUNNEL_WALL_CREATOR_6_9;

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway items");
	}
}
