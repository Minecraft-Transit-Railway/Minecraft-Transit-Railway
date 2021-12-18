package mtr;

import minecraftmappings.BlockEntityMapper;
import minecraftmappings.DeferredRegisterHolder;
import minecraftmappings.NetworkUtilities;
import minecraftmappings.RegistryUtilities;
import mtr.data.Depot;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.servlet.ArrivalsServletHandler;
import mtr.servlet.DataServletHandler;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class MTR implements IPacket {

	private static int gameTick = 0;

	public static final String MOD_ID = "mtr";

	private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(MOD_ID, Registry.ITEM_REGISTRY);
	private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(MOD_ID, Registry.BLOCK_REGISTRY);
	private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
	private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(MOD_ID, Registry.SOUND_EVENT_REGISTRY);

	public static void init() {
		registerItem("apg_door", Items.APG_DOOR);
		registerItem("apg_glass", Items.APG_GLASS);
		registerItem("apg_glass_end", Items.APG_GLASS_END);
		registerItem("brush", Items.BRUSH);
		registerItem("dashboard", Items.DASHBOARD);
		registerItem("escalator", Items.ESCALATOR);
		registerItem("psd_door", Items.PSD_DOOR_1);
		registerItem("psd_glass", Items.PSD_GLASS_1);
		registerItem("psd_glass_end", Items.PSD_GLASS_END_1);
		registerItem("psd_door_2", Items.PSD_DOOR_2);
		registerItem("psd_glass_2", Items.PSD_GLASS_2);
		registerItem("psd_glass_end_2", Items.PSD_GLASS_END_2);
		registerItem("rail_connector_1_wooden", Items.RAIL_CONNECTOR_1_WOODEN);
		registerItem("rail_connector_1_wooden_one_way", Items.RAIL_CONNECTOR_1_WOODEN_ONE_WAY);
		registerItem("rail_connector_2_stone", Items.RAIL_CONNECTOR_2_STONE);
		registerItem("rail_connector_2_stone_one_way", Items.RAIL_CONNECTOR_2_STONE_ONE_WAY);
		registerItem("rail_connector_3_iron", Items.RAIL_CONNECTOR_3_IRON);
		registerItem("rail_connector_3_iron_one_way", Items.RAIL_CONNECTOR_3_IRON_ONE_WAY);
		registerItem("rail_connector_4_obsidian", Items.RAIL_CONNECTOR_4_OBSIDIAN);
		registerItem("rail_connector_4_obsidian_one_way", Items.RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY);
		registerItem("rail_connector_5_blaze", Items.RAIL_CONNECTOR_5_BLAZE);
		registerItem("rail_connector_5_blaze_one_way", Items.RAIL_CONNECTOR_5_BLAZE_ONE_WAY);
		registerItem("rail_connector_6_diamond", Items.RAIL_CONNECTOR_6_DIAMOND);
		registerItem("rail_connector_6_diamond_one_way", Items.RAIL_CONNECTOR_6_DIAMOND_ONE_WAY);
		registerItem("rail_connector_platform", Items.RAIL_CONNECTOR_PLATFORM);
		registerItem("rail_connector_siding", Items.RAIL_CONNECTOR_SIDING);
		registerItem("rail_connector_turn_back", Items.RAIL_CONNECTOR_TURN_BACK);
		registerItem("rail_remover", Items.RAIL_REMOVER);
		registerItem("resource_pack_creator", Items.RESOURCE_PACK_CREATOR);
		registerItem("signal_connector_white", Items.SIGNAL_CONNECTOR_WHITE);
		registerItem("signal_connector_orange", Items.SIGNAL_CONNECTOR_ORANGE);
		registerItem("signal_connector_magenta", Items.SIGNAL_CONNECTOR_MAGENTA);
		registerItem("signal_connector_light_blue", Items.SIGNAL_CONNECTOR_LIGHT_BLUE);
		registerItem("signal_connector_yellow", Items.SIGNAL_CONNECTOR_YELLOW);
		registerItem("signal_connector_lime", Items.SIGNAL_CONNECTOR_LIME);
		registerItem("signal_connector_pink", Items.SIGNAL_CONNECTOR_PINK);
		registerItem("signal_connector_gray", Items.SIGNAL_CONNECTOR_GRAY);
		registerItem("signal_connector_light_gray", Items.SIGNAL_CONNECTOR_LIGHT_GRAY);
		registerItem("signal_connector_cyan", Items.SIGNAL_CONNECTOR_CYAN);
		registerItem("signal_connector_purple", Items.SIGNAL_CONNECTOR_PURPLE);
		registerItem("signal_connector_blue", Items.SIGNAL_CONNECTOR_BLUE);
		registerItem("signal_connector_brown", Items.SIGNAL_CONNECTOR_BROWN);
		registerItem("signal_connector_green", Items.SIGNAL_CONNECTOR_GREEN);
		registerItem("signal_connector_red", Items.SIGNAL_CONNECTOR_RED);
		registerItem("signal_connector_black", Items.SIGNAL_CONNECTOR_BLACK);
		registerItem("signal_remover_white", Items.SIGNAL_REMOVER_WHITE);
		registerItem("signal_remover_orange", Items.SIGNAL_REMOVER_ORANGE);
		registerItem("signal_remover_magenta", Items.SIGNAL_REMOVER_MAGENTA);
		registerItem("signal_remover_light_blue", Items.SIGNAL_REMOVER_LIGHT_BLUE);
		registerItem("signal_remover_yellow", Items.SIGNAL_REMOVER_YELLOW);
		registerItem("signal_remover_lime", Items.SIGNAL_REMOVER_LIME);
		registerItem("signal_remover_pink", Items.SIGNAL_REMOVER_PINK);
		registerItem("signal_remover_gray", Items.SIGNAL_REMOVER_GRAY);
		registerItem("signal_remover_light_gray", Items.SIGNAL_REMOVER_LIGHT_GRAY);
		registerItem("signal_remover_cyan", Items.SIGNAL_REMOVER_CYAN);
		registerItem("signal_remover_purple", Items.SIGNAL_REMOVER_PURPLE);
		registerItem("signal_remover_blue", Items.SIGNAL_REMOVER_BLUE);
		registerItem("signal_remover_brown", Items.SIGNAL_REMOVER_BROWN);
		registerItem("signal_remover_green", Items.SIGNAL_REMOVER_GREEN);
		registerItem("signal_remover_red", Items.SIGNAL_REMOVER_RED);
		registerItem("signal_remover_black", Items.SIGNAL_REMOVER_BLACK);

		registerBlock("apg_door", Blocks.APG_DOOR);
		registerBlock("apg_glass", Blocks.APG_GLASS);
		registerBlock("apg_glass_end", Blocks.APG_GLASS_END);
		registerBlock("arrival_projector_1_small", Blocks.ARRIVAL_PROJECTOR_1_SMALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("arrival_projector_1_medium", Blocks.ARRIVAL_PROJECTOR_1_MEDIUM, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("arrival_projector_1_large", Blocks.ARRIVAL_PROJECTOR_1_LARGE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ceiling", Blocks.CEILING, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ceiling_light", Blocks.CEILING_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ceiling_no_light", Blocks.CEILING_NO_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("clock", Blocks.CLOCK, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("clock_pole", Blocks.CLOCK_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlock("glass_fence_cio", Blocks.GLASS_FENCE_CIO, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_ckt", Blocks.GLASS_FENCE_CKT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_heo", Blocks.GLASS_FENCE_HEO, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_mos", Blocks.GLASS_FENCE_MOS, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_plain", Blocks.GLASS_FENCE_PLAIN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_shm", Blocks.GLASS_FENCE_SHM, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_stained", Blocks.GLASS_FENCE_STAINED, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_stw", Blocks.GLASS_FENCE_STW, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_tsh", Blocks.GLASS_FENCE_TSH, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_wks", Blocks.GLASS_FENCE_WKS, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("logo", Blocks.LOGO, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("marble_blue", Blocks.MARBLE_BLUE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("marble_sandy", Blocks.MARBLE_SANDY, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("pids_1", Blocks.PIDS_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("pids_2", Blocks.PIDS_2, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("pids_3", Blocks.PIDS_3, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("pids_pole", Blocks.PIDS_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("platform", Blocks.PLATFORM, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_indented", Blocks.PLATFORM_INDENTED, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_1", Blocks.PLATFORM_NA_1, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_1_indented", Blocks.PLATFORM_NA_1_INDENTED, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_2", Blocks.PLATFORM_NA_2, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_2_indented", Blocks.PLATFORM_NA_2_INDENTED, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_uk_1", Blocks.PLATFORM_UK_1, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_uk_1_indented", Blocks.PLATFORM_UK_1_INDENTED, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("psd_door", Blocks.PSD_DOOR_1);
		registerBlock("psd_glass", Blocks.PSD_GLASS_1);
		registerBlock("psd_glass_end", Blocks.PSD_GLASS_END_1);
		registerBlock("psd_door_2", Blocks.PSD_DOOR_2);
		registerBlock("psd_glass_2", Blocks.PSD_GLASS_2);
		registerBlock("psd_glass_end_2", Blocks.PSD_GLASS_END_2);
		registerBlock("psd_top", Blocks.PSD_TOP);
		registerBlock("rail", Blocks.RAIL, ItemGroups.CORE);
		registerBlock("railway_sign_2_even", Blocks.RAILWAY_SIGN_2_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_2_odd", Blocks.RAILWAY_SIGN_2_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_3_even", Blocks.RAILWAY_SIGN_3_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_3_odd", Blocks.RAILWAY_SIGN_3_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_4_even", Blocks.RAILWAY_SIGN_4_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_4_odd", Blocks.RAILWAY_SIGN_4_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_5_even", Blocks.RAILWAY_SIGN_5_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_5_odd", Blocks.RAILWAY_SIGN_5_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_6_even", Blocks.RAILWAY_SIGN_6_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_6_odd", Blocks.RAILWAY_SIGN_6_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_7_even", Blocks.RAILWAY_SIGN_7_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_7_odd", Blocks.RAILWAY_SIGN_7_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_middle", Blocks.RAILWAY_SIGN_MIDDLE);
		registerBlock("railway_sign_pole", Blocks.RAILWAY_SIGN_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_standing_light", Blocks.ROUTE_SIGN_STANDING_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_standing_metal", Blocks.ROUTE_SIGN_STANDING_METAL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_wall_light", Blocks.ROUTE_SIGN_WALL_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_wall_metal", Blocks.ROUTE_SIGN_WALL_METAL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("rubbish_bin_1", Blocks.RUBBISH_BIN_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_light_1", Blocks.SIGNAL_LIGHT_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_light_2", Blocks.SIGNAL_LIGHT_2, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_light_3", Blocks.SIGNAL_LIGHT_3, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_light_4", Blocks.SIGNAL_LIGHT_4, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_semaphore_1", Blocks.SIGNAL_SEMAPHORE_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_semaphore_2", Blocks.SIGNAL_SEMAPHORE_2, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("signal_pole", Blocks.SIGNAL_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_color_andesite", Blocks.STATION_COLOR_ANDESITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_bedrock", Blocks.STATION_COLOR_BEDROCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_birch_wood", Blocks.STATION_COLOR_BIRCH_WOOD, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_bone_block", Blocks.STATION_COLOR_BONE_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_chiseled_quartz_block", Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_chiseled_stone_bricks", Blocks.STATION_COLOR_CHISELED_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_clay", Blocks.STATION_COLOR_CLAY, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_coal_ore", Blocks.STATION_COLOR_COAL_ORE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_cobblestone", Blocks.STATION_COLOR_COBBLESTONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_concrete", Blocks.STATION_COLOR_CONCRETE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_concrete_powder", Blocks.STATION_COLOR_CONCRETE_POWDER, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_cracked_stone_bricks", Blocks.STATION_COLOR_CRACKED_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_dark_prismarine", Blocks.STATION_COLOR_DARK_PRISMARINE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_diorite", Blocks.STATION_COLOR_DIORITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_gravel", Blocks.STATION_COLOR_GRAVEL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_iron_block", Blocks.STATION_COLOR_IRON_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_metal", Blocks.STATION_COLOR_METAL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_planks", Blocks.STATION_COLOR_PLANKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_polished_andesite", Blocks.STATION_COLOR_POLISHED_ANDESITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_polished_diorite", Blocks.STATION_COLOR_POLISHED_DIORITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_purpur_block", Blocks.STATION_COLOR_PURPUR_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_purpur_pillar", Blocks.STATION_COLOR_PURPUR_PILLAR, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_block", Blocks.STATION_COLOR_QUARTZ_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_bricks", Blocks.STATION_COLOR_QUARTZ_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_pillar", Blocks.STATION_COLOR_QUARTZ_PILLAR, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_smooth_quartz", Blocks.STATION_COLOR_SMOOTH_QUARTZ, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_smooth_stone", Blocks.STATION_COLOR_SMOOTH_STONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_snow_block", Blocks.STATION_COLOR_SNOW_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stained_glass", Blocks.STATION_COLOR_STAINED_GLASS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stone", Blocks.STATION_COLOR_STONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stone_bricks", Blocks.STATION_COLOR_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_wool", Blocks.STATION_COLOR_WOOL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_name_entrance", Blocks.STATION_NAME_ENTRANCE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_tall_block", Blocks.STATION_NAME_TALL_BLOCK, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_tall_wall", Blocks.STATION_NAME_TALL_WALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_wall", Blocks.STATION_NAME_WALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_pole", Blocks.STATION_COLOR_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("tactile_map", Blocks.TACTILE_MAP, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_barrier_entrance_1", Blocks.TICKET_BARRIER_ENTRANCE_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_barrier_exit_1", Blocks.TICKET_BARRIER_EXIT_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_machine", Blocks.TICKET_MACHINE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor", Blocks.TICKET_PROCESSOR, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_entrance", Blocks.TICKET_PROCESSOR_ENTRANCE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_exit", Blocks.TICKET_PROCESSOR_EXIT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_enquiry", Blocks.TICKET_PROCESSOR_ENQUIRY, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("train_announcer", Blocks.TRAIN_ANNOUNCER, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("train_cargo_loader", Blocks.TRAIN_CARGO_LOADER, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("train_cargo_unloader", Blocks.TRAIN_CARGO_UNLOADER, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("train_sensor", Blocks.TRAIN_REDSTONE_SENSOR, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("train_schedule_sensor", Blocks.TRAIN_SCHEDULE_SENSOR, ItemGroups.RAILWAY_FACILITIES);

		registerBlockEntityType("arrival_projector_1_small", BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY);
		registerBlockEntityType("arrival_projector_1_medium", BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY);
		registerBlockEntityType("arrival_projector_1_large", BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY);
		registerBlockEntityType("clock", BlockEntityTypes.CLOCK_TILE_ENTITY);
		registerBlockEntityType("psd_top", BlockEntityTypes.PSD_TOP_TILE_ENTITY);
		registerBlockEntityType("apg_glass", BlockEntityTypes.APG_GLASS_TILE_ENTITY);
		registerBlockEntityType("pids_1", BlockEntityTypes.PIDS_1_TILE_ENTITY);
		registerBlockEntityType("pids_2", BlockEntityTypes.PIDS_2_TILE_ENTITY);
		registerBlockEntityType("pids_3", BlockEntityTypes.PIDS_3_TILE_ENTITY);
		registerBlockEntityType("rail", BlockEntityTypes.RAIL_TILE_ENTITY);
		registerBlockEntityType("railway_sign_2_even", BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_2_odd", BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY);
		registerBlockEntityType("railway_sign_3_even", BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_3_odd", BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY);
		registerBlockEntityType("railway_sign_4_even", BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_4_odd", BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY);
		registerBlockEntityType("railway_sign_5_even", BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_5_odd", BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY);
		registerBlockEntityType("railway_sign_6_even", BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_6_odd", BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY);
		registerBlockEntityType("railway_sign_7_even", BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY);
		registerBlockEntityType("railway_sign_7_odd", BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY);
		registerBlockEntityType("route_sign_standing_light", BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY);
		registerBlockEntityType("route_sign_standing_metal", BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY);
		registerBlockEntityType("route_sign_wall_light", BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY);
		registerBlockEntityType("route_sign_wall_metal", BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY);
		registerBlockEntityType("signal_light_1", BlockEntityTypes.SIGNAL_LIGHT_1);
		registerBlockEntityType("signal_light_2", BlockEntityTypes.SIGNAL_LIGHT_2);
		registerBlockEntityType("signal_light_3", BlockEntityTypes.SIGNAL_LIGHT_3);
		registerBlockEntityType("signal_light_4", BlockEntityTypes.SIGNAL_LIGHT_4);
		registerBlockEntityType("signal_semaphore_1", BlockEntityTypes.SIGNAL_SEMAPHORE_1);
		registerBlockEntityType("signal_semaphore_2", BlockEntityTypes.SIGNAL_SEMAPHORE_2);
		registerBlockEntityType("station_name_entrance", BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY);
		registerBlockEntityType("station_name_wall", BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY);
		registerBlockEntityType("station_name_tall_block", BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY);
		registerBlockEntityType("station_name_tall_wall", BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY);
		registerBlockEntityType("tactile_map", BlockEntityTypes.TACTILE_MAP_TILE_ENTITY);
		registerBlockEntityType("train_announcer", BlockEntityTypes.TRAIN_ANNOUNCER_TILE_ENTITY);
		registerBlockEntityType("train_cargo_loader", BlockEntityTypes.TRAIN_CARGO_LOADER_TILE_ENTITY);
		registerBlockEntityType("train_cargo_unloader", BlockEntityTypes.TRAIN_CARGO_UNLOADER_TILE_ENTITY);
		registerBlockEntityType("train_redstone_sensor", BlockEntityTypes.TRAIN_REDSTONE_SENSOR_TILE_ENTITY);
		registerBlockEntityType("train_schedule_sensor", BlockEntityTypes.TRAIN_SCHEDULE_SENSOR_TILE_ENTITY);

		registerSoundEvent("ticket_barrier", SoundEvents.TICKET_BARRIER);
		registerSoundEvent("ticket_barrier_concessionary", SoundEvents.TICKET_BARRIER_CONCESSIONARY);
		registerSoundEvent("ticket_processor_entry", SoundEvents.TICKET_PROCESSOR_ENTRY);
		registerSoundEvent("ticket_processor_entry_concessionary", SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY);
		registerSoundEvent("ticket_processor_exit", SoundEvents.TICKET_PROCESSOR_EXIT);
		registerSoundEvent("ticket_processor_exit_concessionary", SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY);
		registerSoundEvent("ticket_processor_fail", SoundEvents.TICKET_PROCESSOR_FAIL);

		ITEMS.register();
		BLOCKS.register();
		BLOCK_ENTITY_TYPES.register();
		SOUND_EVENTS.register();

		NetworkUtilities.registerReceiverC2S(PACKET_GENERATE_PATH, PacketTrainDataGuiServer::generatePathC2S);
		NetworkUtilities.registerReceiverC2S(PACKET_CLEAR_TRAINS, PacketTrainDataGuiServer::clearTrainsC2S);
		NetworkUtilities.registerReceiverC2S(PACKET_SIGN_TYPES, PacketTrainDataGuiServer::receiveSignIdsC2S);
		NetworkUtilities.registerReceiverC2S(PACKET_ADD_BALANCE, PacketTrainDataGuiServer::receiveAddBalanceC2S);
		NetworkUtilities.registerReceiverC2S(PACKET_PIDS_UPDATE, PacketTrainDataGuiServer::receivePIDSMessageC2S);
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, Station::new, false));
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, false));
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, false));
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, Route::new, false));
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, Depot::new, false));
		NetworkUtilities.registerReceiverC2S(PACKET_DELETE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, null, true));
		NetworkUtilities.registerReceiverC2S(PACKET_DELETE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, true));
		NetworkUtilities.registerReceiverC2S(PACKET_DELETE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, true));
		NetworkUtilities.registerReceiverC2S(PACKET_DELETE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, null, true));
		NetworkUtilities.registerReceiverC2S(PACKET_DELETE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, null, true));
		NetworkUtilities.registerReceiverC2S(PACKET_UPDATE_TRAIN_SENSOR, PacketTrainDataGuiServer::receiveTrainSensorC2S);

		final Server webServer = new Server(new QueuedThreadPool(100, 10, 120));
		final ServerConnector serverConnector = new ServerConnector(webServer);
		webServer.setConnectors(new Connector[]{serverConnector});
		final ServletContextHandler context = new ServletContextHandler();
		webServer.setHandler(context);
		final URL url = MTR.class.getResource("/assets/mtr/website/");
		if (url != null) {
			try {
				context.setBaseResource(Resource.newResource(url.toURI()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final ServletHolder servletHolder = new ServletHolder("default", DefaultServlet.class);
		servletHolder.setInitParameter("dirAllowed", "true");
		servletHolder.setInitParameter("cacheControl", "max-age=0,public");
		context.addServlet(servletHolder, "/");
		context.addServlet(DataServletHandler.class, "/data");
		context.addServlet(ArrivalsServletHandler.class, "/arrivals");

		RegistryUtilities.registerTickEvent(minecraftServer -> {
			minecraftServer.getAllLevels().forEach(serverWorld -> {
				final RailwayData railwayData = RailwayData.getInstance(serverWorld);
				if (railwayData != null) {
					railwayData.simulateTrains();
				}
			});
			gameTick++;
		});
		RegistryUtilities.registerPlayerChangeDimensionEvent(MTR::broadcastRailwayData);
		RegistryUtilities.registerPlayerJoinEvent(MTR::broadcastRailwayData);
		RegistryUtilities.registerPlayerQuitEvent(player -> {
			final RailwayData railwayData = RailwayData.getInstance(player.getLevel());
			if (railwayData != null) {
				railwayData.disconnectPlayer(player);
			}
		});
		RegistryUtilities.registerServerStartingEvent(server -> {
			int port = 8888;
			final Path path = server.getServerDirectory().toPath().resolve("config").resolve("mtr_webserver_port.txt");
			try {
				port = Mth.clamp(Integer.parseInt(String.join("", Files.readAllLines(path)).replaceAll("[^0-9]", "")), 1025, 65535);
			} catch (Exception ignored) {
				try {
					Files.write(path, Collections.singleton(String.valueOf(port)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			serverConnector.setPort(port);
			DataServletHandler.SERVER = server;
			ArrivalsServletHandler.SERVER = server;
			try {
				webServer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		RegistryUtilities.registerServerStoppingEvent(server -> {
			try {
				webServer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static boolean isGameTickInterval(int interval) {
		return gameTick % interval == 0;
	}

	private static void registerItem(String path, Item item) {
		ITEMS.register(path, () -> item);
	}

	private static void registerBlock(String path, Block block) {
		BLOCKS.register(path, () -> block);
	}

	private static void registerBlock(String path, Block block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		ITEMS.register(path, () -> new BlockItem(block, new Item.Properties().tab(itemGroup)));
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, BlockEntityType<T> blockEntityType) {
		BLOCK_ENTITY_TYPES.register(path, () -> blockEntityType);
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		SOUND_EVENTS.register(path, () -> soundEvent);
	}

	private static void broadcastRailwayData(ServerPlayer player) {
		final RailwayData railwayData = RailwayData.getInstance(player.getLevel());
		if (railwayData != null) {
			railwayData.broadcastToPlayer(player);
		}
	}
}