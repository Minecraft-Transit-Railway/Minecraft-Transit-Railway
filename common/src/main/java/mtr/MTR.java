package mtr;

import mtr.data.Depot;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.BlockEntityMapper;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.servlet.Webserver;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

public class MTR implements IPacket {

	private static int gameTick = 0;

	public static final String MOD_ID = "mtr";

	public static void init(
			BiConsumer<String, RegistryObject<Item>> registerItem,
			BiConsumer<String, RegistryObject<Block>> registerBlock,
			RegisterBlockItem registerBlockItem,
			RegisterBlockItem registerEnchantedBlockItem,
			BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
			BiConsumer<String, RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType,
			BiConsumer<String, SoundEvent> registerSoundEvent
	) {
		registerItem.accept("brush", Items.BRUSH);
		registerItem.accept("escalator", Items.ESCALATOR);
		registerItem.accept("lift_buttons_link_connector", Items.LIFT_BUTTONS_LINK_CONNECTOR);
		registerItem.accept("lift_buttons_link_remover", Items.LIFT_BUTTONS_LINK_REMOVER);
		registerItem.accept("lift_door_1", Items.LIFT_DOOR_1);
		registerItem.accept("lift_door_odd_1", Items.LIFT_DOOR_ODD_1);
		registerItem.accept("lift_refresher", Items.LIFT_REFRESHER);

		registerBlock.accept("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock.accept("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlockItem.accept("lift_buttons_1", Blocks.LIFT_BUTTONS_1, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_panel_even_1", Blocks.LIFT_PANEL_EVEN_1, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_panel_odd_1", Blocks.LIFT_PANEL_ODD_1, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_panel_even_2", Blocks.LIFT_PANEL_EVEN_2, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_panel_odd_2", Blocks.LIFT_PANEL_ODD_2, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlock.accept("lift_door_1", Blocks.LIFT_DOOR_EVEN_1);
		registerBlock.accept("lift_door_odd_1", Blocks.LIFT_DOOR_ODD_1);
		registerBlockItem.accept("lift_track_1", Blocks.LIFT_TRACK_1, CreativeModeTabs.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_track_floor_1", Blocks.LIFT_TRACK_FLOOR_1, CreativeModeTabs.ESCALATORS_LIFTS);

		registerBlockEntityType.accept("lift_buttons_1", BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_panel_even_1", BlockEntityTypes.LIFT_PANEL_EVEN_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_panel_odd_1", BlockEntityTypes.LIFT_PANEL_ODD_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_panel_even_2", BlockEntityTypes.LIFT_PANEL_EVEN_2_TILE_ENTITY);
		registerBlockEntityType.accept("lift_panel_odd_2", BlockEntityTypes.LIFT_PANEL_ODD_2_TILE_ENTITY);
		registerBlockEntityType.accept("lift_track_floor_1", BlockEntityTypes.LIFT_TRACK_FLOOR_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_door_1", BlockEntityTypes.LIFT_DOOR_EVEN_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_door_odd_1", BlockEntityTypes.LIFT_DOOR_ODD_1_TILE_ENTITY);

		registerEntityType.accept("seat", EntityTypes.SEAT);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_2_2.key, EntityTypes.LiftType.SIZE_2_2.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_2.key, EntityTypes.LiftType.SIZE_3_2.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_3.key, EntityTypes.LiftType.SIZE_3_3.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_3.key, EntityTypes.LiftType.SIZE_4_3.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_4.key, EntityTypes.LiftType.SIZE_4_4.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.registryObject);

		if (!Keys.LIFTS_ONLY) {
			registerItem.accept("apg_door", Items.APG_DOOR);
			registerItem.accept("apg_glass", Items.APG_GLASS);
			registerItem.accept("apg_glass_end", Items.APG_GLASS_END);
			registerItem.accept("dashboard", Items.RAILWAY_DASHBOARD);
			registerItem.accept("dashboard_2", Items.BOAT_DASHBOARD);
			registerItem.accept("dashboard_3", Items.CABLE_CAR_DASHBOARD);
			registerItem.accept("dashboard_4", Items.AIRPLANE_DASHBOARD);
			registerItem.accept("driver_key", Items.DRIVER_KEY);
			registerItem.accept("psd_door", Items.PSD_DOOR_1);
			registerItem.accept("psd_glass", Items.PSD_GLASS_1);
			registerItem.accept("psd_glass_end", Items.PSD_GLASS_END_1);
			registerItem.accept("psd_door_2", Items.PSD_DOOR_2);
			registerItem.accept("psd_glass_2", Items.PSD_GLASS_2);
			registerItem.accept("psd_glass_end_2", Items.PSD_GLASS_END_2);
			registerItem.accept("rail_connector_20", Items.RAIL_CONNECTOR_20);
			registerItem.accept("rail_connector_20_one_way", Items.RAIL_CONNECTOR_20_ONE_WAY);
			registerItem.accept("rail_connector_40", Items.RAIL_CONNECTOR_40);
			registerItem.accept("rail_connector_40_one_way", Items.RAIL_CONNECTOR_40_ONE_WAY);
			registerItem.accept("rail_connector_60", Items.RAIL_CONNECTOR_60);
			registerItem.accept("rail_connector_60_one_way", Items.RAIL_CONNECTOR_60_ONE_WAY);
			registerItem.accept("rail_connector_80", Items.RAIL_CONNECTOR_80);
			registerItem.accept("rail_connector_80_one_way", Items.RAIL_CONNECTOR_80_ONE_WAY);
			registerItem.accept("rail_connector_120", Items.RAIL_CONNECTOR_120);
			registerItem.accept("rail_connector_120_one_way", Items.RAIL_CONNECTOR_120_ONE_WAY);
			registerItem.accept("rail_connector_160", Items.RAIL_CONNECTOR_160);
			registerItem.accept("rail_connector_160_one_way", Items.RAIL_CONNECTOR_160_ONE_WAY);
			registerItem.accept("rail_connector_200", Items.RAIL_CONNECTOR_200);
			registerItem.accept("rail_connector_200_one_way", Items.RAIL_CONNECTOR_200_ONE_WAY);
			registerItem.accept("rail_connector_300", Items.RAIL_CONNECTOR_300);
			registerItem.accept("rail_connector_300_one_way", Items.RAIL_CONNECTOR_300_ONE_WAY);
			registerItem.accept("rail_connector_platform", Items.RAIL_CONNECTOR_PLATFORM);
			registerItem.accept("rail_connector_siding", Items.RAIL_CONNECTOR_SIDING);
			registerItem.accept("rail_connector_turn_back", Items.RAIL_CONNECTOR_TURN_BACK);
			registerItem.accept("rail_connector_cable_car", Items.RAIL_CONNECTOR_CABLE_CAR);
			registerItem.accept("rail_connector_runway", Items.RAIL_CONNECTOR_RUNWAY);
			registerItem.accept("rail_remover", Items.RAIL_REMOVER);
			registerItem.accept("resource_pack_creator", Items.RESOURCE_PACK_CREATOR);
			registerItem.accept("signal_connector_white", Items.SIGNAL_CONNECTOR_WHITE);
			registerItem.accept("signal_connector_orange", Items.SIGNAL_CONNECTOR_ORANGE);
			registerItem.accept("signal_connector_magenta", Items.SIGNAL_CONNECTOR_MAGENTA);
			registerItem.accept("signal_connector_light_blue", Items.SIGNAL_CONNECTOR_LIGHT_BLUE);
			registerItem.accept("signal_connector_yellow", Items.SIGNAL_CONNECTOR_YELLOW);
			registerItem.accept("signal_connector_lime", Items.SIGNAL_CONNECTOR_LIME);
			registerItem.accept("signal_connector_pink", Items.SIGNAL_CONNECTOR_PINK);
			registerItem.accept("signal_connector_gray", Items.SIGNAL_CONNECTOR_GRAY);
			registerItem.accept("signal_connector_light_gray", Items.SIGNAL_CONNECTOR_LIGHT_GRAY);
			registerItem.accept("signal_connector_cyan", Items.SIGNAL_CONNECTOR_CYAN);
			registerItem.accept("signal_connector_purple", Items.SIGNAL_CONNECTOR_PURPLE);
			registerItem.accept("signal_connector_blue", Items.SIGNAL_CONNECTOR_BLUE);
			registerItem.accept("signal_connector_brown", Items.SIGNAL_CONNECTOR_BROWN);
			registerItem.accept("signal_connector_green", Items.SIGNAL_CONNECTOR_GREEN);
			registerItem.accept("signal_connector_red", Items.SIGNAL_CONNECTOR_RED);
			registerItem.accept("signal_connector_black", Items.SIGNAL_CONNECTOR_BLACK);
			registerItem.accept("signal_remover_white", Items.SIGNAL_REMOVER_WHITE);
			registerItem.accept("signal_remover_orange", Items.SIGNAL_REMOVER_ORANGE);
			registerItem.accept("signal_remover_magenta", Items.SIGNAL_REMOVER_MAGENTA);
			registerItem.accept("signal_remover_light_blue", Items.SIGNAL_REMOVER_LIGHT_BLUE);
			registerItem.accept("signal_remover_yellow", Items.SIGNAL_REMOVER_YELLOW);
			registerItem.accept("signal_remover_lime", Items.SIGNAL_REMOVER_LIME);
			registerItem.accept("signal_remover_pink", Items.SIGNAL_REMOVER_PINK);
			registerItem.accept("signal_remover_gray", Items.SIGNAL_REMOVER_GRAY);
			registerItem.accept("signal_remover_light_gray", Items.SIGNAL_REMOVER_LIGHT_GRAY);
			registerItem.accept("signal_remover_cyan", Items.SIGNAL_REMOVER_CYAN);
			registerItem.accept("signal_remover_purple", Items.SIGNAL_REMOVER_PURPLE);
			registerItem.accept("signal_remover_blue", Items.SIGNAL_REMOVER_BLUE);
			registerItem.accept("signal_remover_brown", Items.SIGNAL_REMOVER_BROWN);
			registerItem.accept("signal_remover_green", Items.SIGNAL_REMOVER_GREEN);
			registerItem.accept("signal_remover_red", Items.SIGNAL_REMOVER_RED);
			registerItem.accept("signal_remover_black", Items.SIGNAL_REMOVER_BLACK);
			registerItem.accept("bridge_creator_3", Items.BRIDGE_CREATOR_3);
			registerItem.accept("bridge_creator_5", Items.BRIDGE_CREATOR_5);
			registerItem.accept("bridge_creator_7", Items.BRIDGE_CREATOR_7);
			registerItem.accept("bridge_creator_9", Items.BRIDGE_CREATOR_9);
			registerItem.accept("tunnel_creator_4_3", Items.TUNNEL_CREATOR_4_3);
			registerItem.accept("tunnel_creator_4_5", Items.TUNNEL_CREATOR_4_5);
			registerItem.accept("tunnel_creator_4_7", Items.TUNNEL_CREATOR_4_7);
			registerItem.accept("tunnel_creator_4_9", Items.TUNNEL_CREATOR_4_9);
			registerItem.accept("tunnel_creator_5_3", Items.TUNNEL_CREATOR_5_3);
			registerItem.accept("tunnel_creator_5_5", Items.TUNNEL_CREATOR_5_5);
			registerItem.accept("tunnel_creator_5_7", Items.TUNNEL_CREATOR_5_7);
			registerItem.accept("tunnel_creator_5_9", Items.TUNNEL_CREATOR_5_9);
			registerItem.accept("tunnel_creator_6_3", Items.TUNNEL_CREATOR_6_3);
			registerItem.accept("tunnel_creator_6_5", Items.TUNNEL_CREATOR_6_5);
			registerItem.accept("tunnel_creator_6_7", Items.TUNNEL_CREATOR_6_7);
			registerItem.accept("tunnel_creator_6_9", Items.TUNNEL_CREATOR_6_9);
			registerItem.accept("tunnel_wall_creator_4_3", Items.TUNNEL_WALL_CREATOR_4_3);
			registerItem.accept("tunnel_wall_creator_4_5", Items.TUNNEL_WALL_CREATOR_4_5);
			registerItem.accept("tunnel_wall_creator_4_7", Items.TUNNEL_WALL_CREATOR_4_7);
			registerItem.accept("tunnel_wall_creator_4_9", Items.TUNNEL_WALL_CREATOR_4_9);
			registerItem.accept("tunnel_wall_creator_5_3", Items.TUNNEL_WALL_CREATOR_5_3);
			registerItem.accept("tunnel_wall_creator_5_5", Items.TUNNEL_WALL_CREATOR_5_5);
			registerItem.accept("tunnel_wall_creator_5_7", Items.TUNNEL_WALL_CREATOR_5_7);
			registerItem.accept("tunnel_wall_creator_5_9", Items.TUNNEL_WALL_CREATOR_5_9);
			registerItem.accept("tunnel_wall_creator_6_3", Items.TUNNEL_WALL_CREATOR_6_3);
			registerItem.accept("tunnel_wall_creator_6_5", Items.TUNNEL_WALL_CREATOR_6_5);
			registerItem.accept("tunnel_wall_creator_6_7", Items.TUNNEL_WALL_CREATOR_6_7);
			registerItem.accept("tunnel_wall_creator_6_9", Items.TUNNEL_WALL_CREATOR_6_9);
			registerItem.accept("boat_node", Items.BOAT_NODE);

			registerBlockItem.accept("rail", Blocks.RAIL_NODE, CreativeModeTabs.CORE);
			registerBlock.accept("boat_node", Blocks.BOAT_NODE);
			registerBlockItem.accept("cable_car_node_lower", Blocks.CABLE_CAR_NODE_LOWER, CreativeModeTabs.CORE);
			registerBlockItem.accept("cable_car_node_upper", Blocks.CABLE_CAR_NODE_UPPER, CreativeModeTabs.CORE);
			registerBlockItem.accept("cable_car_node_station", Blocks.CABLE_CAR_NODE_STATION, CreativeModeTabs.CORE);
			registerBlockItem.accept("airplane_node", Blocks.AIRPLANE_NODE, CreativeModeTabs.CORE);
			registerBlock.accept("apg_door", Blocks.APG_DOOR);
			registerBlock.accept("apg_glass", Blocks.APG_GLASS);
			registerBlock.accept("apg_glass_end", Blocks.APG_GLASS_END);
			registerBlockItem.accept("arrival_projector_1_small", Blocks.ARRIVAL_PROJECTOR_1_SMALL, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("arrival_projector_1_medium", Blocks.ARRIVAL_PROJECTOR_1_MEDIUM, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("arrival_projector_1_large", Blocks.ARRIVAL_PROJECTOR_1_LARGE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ceiling", Blocks.CEILING, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ceiling_light", Blocks.CEILING_LIGHT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ceiling_no_light", Blocks.CEILING_NO_LIGHT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("clock", Blocks.CLOCK, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("clock_pole", Blocks.CLOCK_POLE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_cio", Blocks.GLASS_FENCE_CIO, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_ckt", Blocks.GLASS_FENCE_CKT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_heo", Blocks.GLASS_FENCE_HEO, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_mos", Blocks.GLASS_FENCE_MOS, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_plain", Blocks.GLASS_FENCE_PLAIN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_shm", Blocks.GLASS_FENCE_SHM, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_stained", Blocks.GLASS_FENCE_STAINED, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_stw", Blocks.GLASS_FENCE_STW, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_tsh", Blocks.GLASS_FENCE_TSH, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("glass_fence_wks", Blocks.GLASS_FENCE_WKS, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("logo", Blocks.LOGO, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("marble_blue", Blocks.MARBLE_BLUE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("marble_sandy", Blocks.MARBLE_SANDY, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("marble_blue_slab", Blocks.MARBLE_BLUE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("marble_sandy_slab", Blocks.MARBLE_SANDY_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("metal", Blocks.METAL, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("metal_slab", Blocks.METAL_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("pids_1", Blocks.PIDS_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("pids_2", Blocks.PIDS_2, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("pids_3", Blocks.PIDS_3, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("pids_4", Blocks.PIDS_4, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("pids_single_arrival_1", Blocks.PIDS_SINGLE_ARRIVAL_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("pids_pole", Blocks.PIDS_POLE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("platform", Blocks.PLATFORM, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_indented", Blocks.PLATFORM_INDENTED, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_na_1", Blocks.PLATFORM_NA_1, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_na_1_indented", Blocks.PLATFORM_NA_1_INDENTED, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_na_2", Blocks.PLATFORM_NA_2, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_na_2_indented", Blocks.PLATFORM_NA_2_INDENTED, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_uk_1", Blocks.PLATFORM_UK_1, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockItem.accept("platform_uk_1_indented", Blocks.PLATFORM_UK_1_INDENTED, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlock.accept("psd_door", Blocks.PSD_DOOR_1);
			registerBlock.accept("psd_glass", Blocks.PSD_GLASS_1);
			registerBlock.accept("psd_glass_end", Blocks.PSD_GLASS_END_1);
			registerBlock.accept("psd_door_2", Blocks.PSD_DOOR_2);
			registerBlock.accept("psd_glass_2", Blocks.PSD_GLASS_2);
			registerBlock.accept("psd_glass_end_2", Blocks.PSD_GLASS_END_2);
			registerBlock.accept("psd_top", Blocks.PSD_TOP);
			registerBlockItem.accept("railway_sign_2_even", Blocks.RAILWAY_SIGN_2_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_2_odd", Blocks.RAILWAY_SIGN_2_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_3_even", Blocks.RAILWAY_SIGN_3_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_3_odd", Blocks.RAILWAY_SIGN_3_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_4_even", Blocks.RAILWAY_SIGN_4_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_4_odd", Blocks.RAILWAY_SIGN_4_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_5_even", Blocks.RAILWAY_SIGN_5_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_5_odd", Blocks.RAILWAY_SIGN_5_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_6_even", Blocks.RAILWAY_SIGN_6_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_6_odd", Blocks.RAILWAY_SIGN_6_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_7_even", Blocks.RAILWAY_SIGN_7_EVEN, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("railway_sign_7_odd", Blocks.RAILWAY_SIGN_7_ODD, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlock.accept("railway_sign_middle", Blocks.RAILWAY_SIGN_MIDDLE);
			registerBlockItem.accept("railway_sign_pole", Blocks.RAILWAY_SIGN_POLE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("route_sign_standing_light", Blocks.ROUTE_SIGN_STANDING_LIGHT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("route_sign_standing_metal", Blocks.ROUTE_SIGN_STANDING_METAL, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("route_sign_wall_light", Blocks.ROUTE_SIGN_WALL_LIGHT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("route_sign_wall_metal", Blocks.ROUTE_SIGN_WALL_METAL, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("rubbish_bin_1", Blocks.RUBBISH_BIN_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_1", Blocks.SIGNAL_LIGHT_2_ASPECT_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_2", Blocks.SIGNAL_LIGHT_2_ASPECT_2, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_3", Blocks.SIGNAL_LIGHT_2_ASPECT_3, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_4", Blocks.SIGNAL_LIGHT_2_ASPECT_4, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_3_aspect_1", Blocks.SIGNAL_LIGHT_3_ASPECT_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_3_aspect_2", Blocks.SIGNAL_LIGHT_3_ASPECT_2, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_4_aspect_1", Blocks.SIGNAL_LIGHT_4_ASPECT_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_light_4_aspect_2", Blocks.SIGNAL_LIGHT_4_ASPECT_2, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_semaphore_1", Blocks.SIGNAL_SEMAPHORE_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_semaphore_2", Blocks.SIGNAL_SEMAPHORE_2, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("signal_pole", Blocks.SIGNAL_POLE, CreativeModeTabs.RAILWAY_FACILITIES);

			registerEnchantedBlockItem.accept("station_color_andesite", Blocks.STATION_COLOR_ANDESITE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_bedrock", Blocks.STATION_COLOR_BEDROCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_birch_wood", Blocks.STATION_COLOR_BIRCH_WOOD, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_bone_block", Blocks.STATION_COLOR_BONE_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_chiseled_quartz_block", Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_chiseled_stone_bricks", Blocks.STATION_COLOR_CHISELED_STONE_BRICKS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_clay", Blocks.STATION_COLOR_CLAY, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_coal_ore", Blocks.STATION_COLOR_COAL_ORE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_cobblestone", Blocks.STATION_COLOR_COBBLESTONE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_concrete", Blocks.STATION_COLOR_CONCRETE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_concrete_powder", Blocks.STATION_COLOR_CONCRETE_POWDER, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_cracked_stone_bricks", Blocks.STATION_COLOR_CRACKED_STONE_BRICKS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_dark_prismarine", Blocks.STATION_COLOR_DARK_PRISMARINE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_diorite", Blocks.STATION_COLOR_DIORITE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_gravel", Blocks.STATION_COLOR_GRAVEL, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_iron_block", Blocks.STATION_COLOR_IRON_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_metal", Blocks.STATION_COLOR_METAL, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_planks", Blocks.STATION_COLOR_PLANKS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_polished_andesite", Blocks.STATION_COLOR_POLISHED_ANDESITE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_polished_diorite", Blocks.STATION_COLOR_POLISHED_DIORITE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_purpur_block", Blocks.STATION_COLOR_PURPUR_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_purpur_pillar", Blocks.STATION_COLOR_PURPUR_PILLAR, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_block", Blocks.STATION_COLOR_QUARTZ_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_bricks", Blocks.STATION_COLOR_QUARTZ_BRICKS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_pillar", Blocks.STATION_COLOR_QUARTZ_PILLAR, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_smooth_quartz", Blocks.STATION_COLOR_SMOOTH_QUARTZ, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_smooth_stone", Blocks.STATION_COLOR_SMOOTH_STONE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_snow_block", Blocks.STATION_COLOR_SNOW_BLOCK, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stained_glass", Blocks.STATION_COLOR_STAINED_GLASS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stone", Blocks.STATION_COLOR_STONE, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stone_bricks", Blocks.STATION_COLOR_STONE_BRICKS, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_wool", Blocks.STATION_COLOR_WOOL, CreativeModeTabs.STATION_BUILDING_BLOCKS);

			registerEnchantedBlockItem.accept("station_color_andesite_slab", Blocks.STATION_COLOR_ANDESITE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_bedrock_slab", Blocks.STATION_COLOR_BEDROCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_birch_wood_slab", Blocks.STATION_COLOR_BIRCH_WOOD_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_bone_block_slab", Blocks.STATION_COLOR_BONE_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_chiseled_quartz_block_slab", Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_chiseled_stone_bricks_slab", Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_clay_slab", Blocks.STATION_COLOR_CLAY_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_coal_ore_slab", Blocks.STATION_COLOR_COAL_ORE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_cobblestone_slab", Blocks.STATION_COLOR_COBBLESTONE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_concrete_slab", Blocks.STATION_COLOR_CONCRETE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_concrete_powder_slab", Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_cracked_stone_bricks_slab", Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_dark_prismarine_slab", Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_diorite_slab", Blocks.STATION_COLOR_DIORITE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_gravel_slab", Blocks.STATION_COLOR_GRAVEL_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_iron_block_slab", Blocks.STATION_COLOR_IRON_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_metal_slab", Blocks.STATION_COLOR_METAL_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_planks_slab", Blocks.STATION_COLOR_PLANKS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_polished_andesite_slab", Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_polished_diorite_slab", Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_purpur_block_slab", Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_purpur_pillar_slab", Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_block_slab", Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_bricks_slab", Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_quartz_pillar_slab", Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_smooth_quartz_slab", Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_smooth_stone_slab", Blocks.STATION_COLOR_SMOOTH_STONE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_snow_block_slab", Blocks.STATION_COLOR_SNOW_BLOCK_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stained_glass_slab", Blocks.STATION_COLOR_STAINED_GLASS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stone_slab", Blocks.STATION_COLOR_STONE_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_stone_bricks_slab", Blocks.STATION_COLOR_STONE_BRICKS_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerEnchantedBlockItem.accept("station_color_wool_slab", Blocks.STATION_COLOR_WOOL_SLAB, CreativeModeTabs.STATION_BUILDING_BLOCKS);

			registerBlockItem.accept("station_name_entrance", Blocks.STATION_NAME_ENTRANCE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_tall_block", Blocks.STATION_NAME_TALL_BLOCK, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_tall_block_double_sided", Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_tall_wall", Blocks.STATION_NAME_TALL_WALL, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_wall", Blocks.STATION_NAME_WALL_WHITE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_wall_gray", Blocks.STATION_NAME_WALL_GRAY, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_name_wall_black", Blocks.STATION_NAME_WALL_BLACK, CreativeModeTabs.RAILWAY_FACILITIES);
			registerEnchantedBlockItem.accept("station_pole", Blocks.STATION_COLOR_POLE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("tactile_map", Blocks.TACTILE_MAP, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_barrier_entrance_1", Blocks.TICKET_BARRIER_ENTRANCE_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_barrier_exit_1", Blocks.TICKET_BARRIER_EXIT_1, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_machine", Blocks.TICKET_MACHINE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_processor", Blocks.TICKET_PROCESSOR, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_processor_entrance", Blocks.TICKET_PROCESSOR_ENTRANCE, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_processor_exit", Blocks.TICKET_PROCESSOR_EXIT, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("ticket_processor_enquiry", Blocks.TICKET_PROCESSOR_ENQUIRY, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("train_announcer", Blocks.TRAIN_ANNOUNCER, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("train_cargo_loader", Blocks.TRAIN_CARGO_LOADER, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("train_cargo_unloader", Blocks.TRAIN_CARGO_UNLOADER, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("train_sensor", Blocks.TRAIN_REDSTONE_SENSOR, CreativeModeTabs.RAILWAY_FACILITIES);
			registerBlockItem.accept("train_schedule_sensor", Blocks.TRAIN_SCHEDULE_SENSOR, CreativeModeTabs.RAILWAY_FACILITIES);

			registerBlockEntityType.accept("arrival_projector_1_small", BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY);
			registerBlockEntityType.accept("arrival_projector_1_medium", BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY);
			registerBlockEntityType.accept("arrival_projector_1_large", BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY);
			registerBlockEntityType.accept("boat_node", BlockEntityTypes.BOAT_NODE_TILE_ENTITY);
			registerBlockEntityType.accept("clock", BlockEntityTypes.CLOCK_TILE_ENTITY);
			registerBlockEntityType.accept("psd_door_1", BlockEntityTypes.PSD_DOOR_1_TILE_ENTITY);
			registerBlockEntityType.accept("psd_door_2", BlockEntityTypes.PSD_DOOR_2_TILE_ENTITY);
			registerBlockEntityType.accept("psd_top", BlockEntityTypes.PSD_TOP_TILE_ENTITY);
			registerBlockEntityType.accept("apg_glass", BlockEntityTypes.APG_GLASS_TILE_ENTITY);
			registerBlockEntityType.accept("apg_door", BlockEntityTypes.APG_DOOR_TILE_ENTITY);
			registerBlockEntityType.accept("pids_1", BlockEntityTypes.PIDS_1_TILE_ENTITY);
			registerBlockEntityType.accept("pids_2", BlockEntityTypes.PIDS_2_TILE_ENTITY);
			registerBlockEntityType.accept("pids_3", BlockEntityTypes.PIDS_3_TILE_ENTITY);
			registerBlockEntityType.accept("pids_4", BlockEntityTypes.PIDS_4_TILE_ENTITY);
			registerBlockEntityType.accept("pids_single_arrival_1", BlockEntityTypes.PIDS_SINGLE_ARRIVAL_1_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_2_even", BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_2_odd", BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_3_even", BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_3_odd", BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_4_even", BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_4_odd", BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_5_even", BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_5_odd", BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_6_even", BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_6_odd", BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_7_even", BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY);
			registerBlockEntityType.accept("railway_sign_7_odd", BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY);
			registerBlockEntityType.accept("route_sign_standing_light", BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY);
			registerBlockEntityType.accept("route_sign_standing_metal", BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY);
			registerBlockEntityType.accept("route_sign_wall_light", BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY);
			registerBlockEntityType.accept("route_sign_wall_metal", BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY);
			registerBlockEntityType.accept("signal_light_1", BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1);
			registerBlockEntityType.accept("signal_light_2", BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2);
			registerBlockEntityType.accept("signal_light_3", BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3);
			registerBlockEntityType.accept("signal_light_4", BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4);
			registerBlockEntityType.accept("signal_light_3_aspect_1", BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1);
			registerBlockEntityType.accept("signal_light_3_aspect_2", BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_2);
			registerBlockEntityType.accept("signal_light_4_aspect_1", BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1);
			registerBlockEntityType.accept("signal_light_4_aspect_2", BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2);
			registerBlockEntityType.accept("signal_semaphore_1", BlockEntityTypes.SIGNAL_SEMAPHORE_1);
			registerBlockEntityType.accept("signal_semaphore_2", BlockEntityTypes.SIGNAL_SEMAPHORE_2);
			registerBlockEntityType.accept("station_name_entrance", BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_wall", BlockEntityTypes.STATION_NAME_WALL_WHITE_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_wall_gray", BlockEntityTypes.STATION_NAME_WALL_GRAY_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_wall_black", BlockEntityTypes.STATION_NAME_WALL_BLACK_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_tall_block", BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_tall_block_double_sided", BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED_TILE_ENTITY);
			registerBlockEntityType.accept("station_name_tall_wall", BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY);
			registerBlockEntityType.accept("tactile_map", BlockEntityTypes.TACTILE_MAP_TILE_ENTITY);
			registerBlockEntityType.accept("train_announcer", BlockEntityTypes.TRAIN_ANNOUNCER_TILE_ENTITY);
			registerBlockEntityType.accept("train_cargo_loader", BlockEntityTypes.TRAIN_CARGO_LOADER_TILE_ENTITY);
			registerBlockEntityType.accept("train_cargo_unloader", BlockEntityTypes.TRAIN_CARGO_UNLOADER_TILE_ENTITY);
			registerBlockEntityType.accept("train_redstone_sensor", BlockEntityTypes.TRAIN_REDSTONE_SENSOR_TILE_ENTITY);
			registerBlockEntityType.accept("train_schedule_sensor", BlockEntityTypes.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY);

			registerSoundEvent.accept("ticket_barrier", SoundEvents.TICKET_BARRIER);
			registerSoundEvent.accept("ticket_barrier_concessionary", SoundEvents.TICKET_BARRIER_CONCESSIONARY);
			registerSoundEvent.accept("ticket_processor_entry", SoundEvents.TICKET_PROCESSOR_ENTRY);
			registerSoundEvent.accept("ticket_processor_entry_concessionary", SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY);
			registerSoundEvent.accept("ticket_processor_exit", SoundEvents.TICKET_PROCESSOR_EXIT);
			registerSoundEvent.accept("ticket_processor_exit_concessionary", SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY);
			registerSoundEvent.accept("ticket_processor_fail", SoundEvents.TICKET_PROCESSOR_FAIL);
		}

		Registry.registerNetworkReceiver(PACKET_GENERATE_PATH, PacketTrainDataGuiServer::generatePathC2S);
		Registry.registerNetworkReceiver(PACKET_CLEAR_TRAINS, PacketTrainDataGuiServer::clearTrainsC2S);
		Registry.registerNetworkReceiver(PACKET_SIGN_TYPES, PacketTrainDataGuiServer::receiveSignIdsC2S);
		Registry.registerNetworkReceiver(PACKET_ADD_BALANCE, PacketTrainDataGuiServer::receiveAddBalanceC2S);
		Registry.registerNetworkReceiver(PACKET_PIDS_UPDATE, PacketTrainDataGuiServer::receivePIDSMessageC2S);
		Registry.registerNetworkReceiver(PACKET_ARRIVAL_PROJECTOR_UPDATE, PacketTrainDataGuiServer::receiveArrivalProjectorMessageC2S);
		Registry.registerNetworkReceiver(PACKET_USE_TIME_AND_WIND_SYNC, PacketTrainDataGuiServer::receiveUseTimeAndWindSyncC2S);
		Registry.registerNetworkReceiver(PACKET_UPDATE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, (id, transportMode) -> new Station(id), false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, Route::new, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, Depot::new, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_LIFT, railwayData -> railwayData.lifts, railwayData -> railwayData.dataCache.liftsServerIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_TRACK_FLOOR, PacketTrainDataGuiServer::receiveLiftTrackFloorC2S);
		Registry.registerNetworkReceiver(PACKET_DELETE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_UPDATE_TRAIN_SENSOR, PacketTrainDataGuiServer::receiveTrainSensorC2S);
		Registry.registerNetworkReceiver(PACKET_REMOVE_RAIL_ACTION, PacketTrainDataGuiServer::receiveRemoveRailAction);
		Registry.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, PacketTrainDataGuiServer::receiveUpdateTrainPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, PacketTrainDataGuiServer::receiveUpdateLiftPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_UPDATE_ENTITY_SEAT_POSITION, PacketTrainDataGuiServer::receiveUpdateEntitySeatPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_DRIVE_TRAIN, PacketTrainDataGuiServer::receiveDriveTrainC2S);
		Registry.registerNetworkReceiver(PACKET_PRESS_LIFT_BUTTON, PacketTrainDataGuiServer::receivePressLiftButtonC2S);

		Registry.registerTickEvent(minecraftServer -> {
			minecraftServer.getAllLevels().forEach(serverWorld -> {
				final RailwayData railwayData = RailwayData.getInstance(serverWorld);
				if (railwayData != null) {
					railwayData.simulateTrains();
				}
			});

			gameTick++;

			if (Keys.TEST_SERVER && gameTick > 200) {
				System.out.printf("Terminating test server for %s%n", Registry.isFabric() ? "Fabric" : "Forge");
				Runtime.getRuntime().halt(0);
			}
		});
		Registry.registerPlayerJoinEvent(player -> {
			PacketTrainDataGuiServer.versionCheckS2C(player);
			final RailwayData railwayData = RailwayData.getInstance(player.level());
			if (railwayData != null) {
				railwayData.onPlayerJoin(player);
			}
		});
		Registry.registerPlayerQuitEvent(player -> {
			final RailwayData railwayData = RailwayData.getInstance(player.level());
			if (railwayData != null) {
				railwayData.disconnectPlayer(player);
			}
		});

		if (!Keys.LIFTS_ONLY) {
			Webserver.init();
			Registry.registerServerStartingEvent(minecraftServer -> {
				Webserver.callback = minecraftServer::execute;
				Webserver.getWorlds = () -> {
					final List<Level> worlds = new ArrayList<>();
					minecraftServer.getAllLevels().forEach(worlds::add);
					return worlds;
				};
				Webserver.getRoutes = railwayData -> railwayData == null ? new HashSet<>() : railwayData.routes;
				Webserver.getDataCache = railwayData -> railwayData == null ? null : railwayData.dataCache;
				Webserver.start(minecraftServer.getServerDirectory().toPath().resolve("config").resolve("mtr_webserver_port.txt"));
			});
			Registry.registerServerStoppingEvent(minecraftServer -> Webserver.stop());
		}
	}

	public static boolean isGameTickInterval(int interval) {
		return gameTick % interval == 0;
	}

	public static boolean isGameTickInterval(int interval, int offset) {
		return (gameTick + offset) % interval == 0;
	}

	@FunctionalInterface
	public interface RegisterBlockItem {
		void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
	}
}
