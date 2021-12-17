package mtr;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import mapper.UtilitiesClient;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.ColorHandlers;
import me.shedaniel.architectury.registry.RenderTypes;
import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.BlockTactileMap;
import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.item.ItemNodeModifierBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MTRClient implements IPacket {

	public static boolean isReplayMod;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");

	public static void init() {
		RenderTypes.register(RenderType.cutout(), Blocks.APG_DOOR);
		RenderTypes.register(RenderType.cutout(), Blocks.APG_GLASS);
		RenderTypes.register(RenderType.cutout(), Blocks.APG_GLASS_END);
		RenderTypes.register(RenderType.cutout(), Blocks.CLOCK);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_CIO);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_CKT);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_HEO);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_MOS);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_PLAIN);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_SHM);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_STAINED);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_STW);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_TSH);
		RenderTypes.register(RenderType.translucent(), Blocks.GLASS_FENCE_WKS);
		RenderTypes.register(RenderType.cutout(), Blocks.LOGO);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_INDENTED);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_NA_1);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_NA_1_INDENTED);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_NA_2);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_NA_2_INDENTED);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_UK_1);
		RenderTypes.register(RenderType.cutout(), Blocks.PLATFORM_UK_1_INDENTED);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_DOOR_1);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_GLASS_1);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_GLASS_END_1);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_DOOR_2);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_GLASS_2);
		RenderTypes.register(RenderType.cutout(), Blocks.PSD_GLASS_END_2);
		RenderTypes.register(RenderType.cutout(), Blocks.RAIL);
		RenderTypes.register(RenderType.cutout(), Blocks.RUBBISH_BIN_1);
		RenderTypes.register(RenderType.cutout(), Blocks.SIGNAL_LIGHT_1);
		RenderTypes.register(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS);
		RenderTypes.register(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK);
		RenderTypes.register(RenderType.cutout(), Blocks.STATION_NAME_TALL_WALL);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_BARRIER_ENTRANCE_1);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_BARRIER_EXIT_1);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_MACHINE);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_PROCESSOR);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENTRANCE);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_PROCESSOR_EXIT);
		RenderTypes.register(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENQUIRY);
		RenderTypes.register(RenderType.cutout(), Blocks.TRAIN_ANNOUNCER);
		RenderTypes.register(RenderType.cutout(), Blocks.TRAIN_CARGO_LOADER);
		RenderTypes.register(RenderType.cutout(), Blocks.TRAIN_CARGO_UNLOADER);
		RenderTypes.register(RenderType.cutout(), Blocks.TRAIN_REDSTONE_SENSOR);
		RenderTypes.register(RenderType.cutout(), Blocks.TRAIN_SCHEDULE_SENSOR);

		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_1_WOODEN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_1_WOODEN_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_2_STONE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_2_STONE_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_3_IRON, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_3_IRON_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_4_OBSIDIAN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_5_BLAZE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_5_BLAZE_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_6_DIAMOND, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_6_DIAMOND_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_PLATFORM, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_SIDING, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_TURN_BACK, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_REMOVER, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_WHITE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_ORANGE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_MAGENTA, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_YELLOW, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIME, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PINK, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GRAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_CYAN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PURPLE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLUE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BROWN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GREEN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_RED, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLACK, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_WHITE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_ORANGE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_MAGENTA, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_YELLOW, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIME, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PINK, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GRAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_CYAN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PURPLE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLUE, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BROWN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GREEN, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_RED, ItemNodeModifierBase.TAG_POS);
		UtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLACK, ItemNodeModifierBase.TAG_POS);

		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900, 0xFF9900));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900, 0xFF9900));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900, 0xFF9900));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY, RenderClock::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900, 0xFF9900));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900, 0xFF9900));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, false, 0xFF9900, 0x33CC00, 1.25F, true));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY, RenderRailwaySign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY, RenderRouteSign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY, RenderRouteSign::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_1, dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2, dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3, dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4, dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1, dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2, dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY, RenderStationNameEntrance::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY, RenderStationNameTall::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY, RenderStationNameTall::new);
		UtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY, RenderStationNameWall::new);

		registerStationColor(Blocks.STATION_COLOR_ANDESITE);
		registerStationColor(Blocks.STATION_COLOR_BEDROCK);
		registerStationColor(Blocks.STATION_COLOR_BIRCH_WOOD);
		registerStationColor(Blocks.STATION_COLOR_BONE_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS);
		registerStationColor(Blocks.STATION_COLOR_CLAY);
		registerStationColor(Blocks.STATION_COLOR_COAL_ORE);
		registerStationColor(Blocks.STATION_COLOR_COBBLESTONE);
		registerStationColor(Blocks.STATION_COLOR_CONCRETE);
		registerStationColor(Blocks.STATION_COLOR_CONCRETE_POWDER);
		registerStationColor(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS);
		registerStationColor(Blocks.STATION_COLOR_DARK_PRISMARINE);
		registerStationColor(Blocks.STATION_COLOR_DIORITE);
		registerStationColor(Blocks.STATION_COLOR_GRAVEL);
		registerStationColor(Blocks.STATION_COLOR_IRON_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_METAL);
		registerStationColor(Blocks.STATION_COLOR_PLANKS);
		registerStationColor(Blocks.STATION_COLOR_POLISHED_ANDESITE);
		registerStationColor(Blocks.STATION_COLOR_POLISHED_DIORITE);
		registerStationColor(Blocks.STATION_COLOR_PURPUR_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_PURPUR_PILLAR);
		registerStationColor(Blocks.STATION_COLOR_QUARTZ_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_QUARTZ_BRICKS);
		registerStationColor(Blocks.STATION_COLOR_QUARTZ_PILLAR);
		registerStationColor(Blocks.STATION_COLOR_SMOOTH_QUARTZ);
		registerStationColor(Blocks.STATION_COLOR_SMOOTH_STONE);
		registerStationColor(Blocks.STATION_COLOR_SNOW_BLOCK);
		registerStationColor(Blocks.STATION_COLOR_STAINED_GLASS);
		registerStationColor(Blocks.STATION_COLOR_STONE);
		registerStationColor(Blocks.STATION_COLOR_STONE_BRICKS);
		registerStationColor(Blocks.STATION_COLOR_WOOL);
		registerStationColor(Blocks.STATION_NAME_TALL_BLOCK);
		registerStationColor(Blocks.STATION_NAME_TALL_WALL);
		registerStationColor(Blocks.STATION_COLOR_POLE);

		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_CHUNK_S2C, (packet, context) -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_DASHBOARD_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance()));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_PIDS_CONFIG_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_RAILWAY_SIGN_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_TICKET_MACHINE_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_TRAIN_SENSOR_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openResourcePackCreatorScreen(Minecraft.getInstance()));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_ANNOUNCE, (packet, context) -> PacketTrainDataGuiClient.announceS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_GENERATE_PATH, (packet, context) -> PacketTrainDataGuiClient.generatePathS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_CREATE_RAIL, (packet, context) -> PacketTrainDataGuiClient.createRailS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_CREATE_SIGNAL, (packet, context) -> PacketTrainDataGuiClient.createSignalS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_REMOVE_NODE, (packet, context) -> PacketTrainDataGuiClient.removeNodeS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_REMOVE_RAIL, (packet, context) -> PacketTrainDataGuiClient.removeRailConnectionS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_REMOVE_SIGNALS, (packet, context) -> PacketTrainDataGuiClient.removeSignalsS2C(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_STATION, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, false));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_PLATFORM, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_SIDING, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_ROUTE, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_DEPOT, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_STATION, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, true));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_PLATFORM, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_SIDING, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_ROUTE, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_DEPOT, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_WRITE_RAILS, (packet, context) -> ClientData.writeRails(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_TRAINS, (packet, context) -> ClientData.updateTrains(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_DELETE_TRAINS, (packet, context) -> ClientData.deleteTrains(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_TRAIN_RIDING_POSITION, (packet, context) -> ClientData.updateTrainRidingPosition(Minecraft.getInstance(), packet));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_UPDATE_SCHEDULE, (packet, context) -> ClientData.updateSchedule(Minecraft.getInstance(), packet));

		BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, pos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
			}
		};

		Config.getPatreonList();
		Config.refreshProperties();
		CrowdinTranslate.downloadTranslations("minecraft-transit-railway", MTR.MOD_ID);

		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
			Config.refreshProperties();
			isReplayMod = player.getClass().toGenericString().toLowerCase().contains("replaymod");
		});
		TextureStitchEvent.POST.register(textureAtlas -> {
			CustomResources.reload(Minecraft.getInstance().getResourceManager());
		});
	}

	private static void registerStationColor(Block block) {
		ColorHandlers.registerBlockColors(new StationColor(), block);
	}

	private static class StationColor implements BlockColor {

		@Override
		public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
			final int defaultColor = 0x7F7F7F;
			if (pos != null) {
				return ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
			} else {
				return defaultColor;
			}
		}
	}
}
