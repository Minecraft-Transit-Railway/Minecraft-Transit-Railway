package mtr;

import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.BlockTactileMap;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.item.ItemNodeModifierBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

public class MTRClient implements IPacket {

	private static boolean isReplayMod;
	private static float gameTick = 0;
	private static float lastPlayedTrainSoundsTick = 0;

	private static int tick;
	private static long startSampleMillis;
	private static float startSampleGameTick;
	private static float gameTickTest;
	private static int skipTicks;
	private static int lastSkipTicks;

	public static final int TICKS_PER_SPEED_SOUND = 4;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");
	private static final int SAMPLE_MILLIS = 1000;

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_DOOR);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS_END);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CLOCK);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CIO);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CKT);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_HEO);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_MOS);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_PLAIN);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_SHM);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STAINED);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STW);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_TSH);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_WKS);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LOGO);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_INDENTED);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1_INDENTED);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2_INDENTED);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1_INDENTED);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_2);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_2);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_2);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.RAIL_NODE);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.RUBBISH_BIN_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.SIGNAL_LIGHT_1);
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_WALL);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_ENTRANCE_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_EXIT_1);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_MACHINE);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENTRANCE);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_EXIT);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENQUIRY);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_ANNOUNCER);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_LOADER);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_UNLOADER);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_REDSTONE_SENSOR);
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_SCHEDULE_SENSOR);

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_PLATFORM, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_SIDING, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_TURN_BACK, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_REMOVER, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_WHITE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_ORANGE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_MAGENTA, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_YELLOW, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIME, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PINK, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_CYAN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PURPLE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BROWN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GREEN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_RED, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLACK, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_WHITE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_ORANGE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_MAGENTA, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_YELLOW, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIME, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PINK, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_CYAN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PURPLE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BROWN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GREEN, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_RED, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLACK, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_9, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_3, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_5, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_7, ItemNodeModifierBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_9, ItemNodeModifierBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.BOAT_NODE_TILE_ENTITY, RenderBoatNode::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY, RenderClock::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, false, 0xFF9900, 0x33CC00, 1.25F, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY, RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY, RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_1, dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2, dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3, dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4, dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1, dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2, dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY, RenderStationNameEntrance::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY, RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY, RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY, RenderStationNameWall::new);

		RegistryClient.registerEntityRenderer(EntityTypes.SEAT, RenderTrains::new);

		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_ANDESITE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BEDROCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BIRCH_WOOD);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BONE_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CLAY);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COAL_ORE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COBBLESTONE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_POWDER);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DARK_PRISMARINE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DIORITE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_GRAVEL);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_IRON_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_METAL);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PLANKS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_ANDESITE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_DIORITE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_PILLAR);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BRICKS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_PILLAR);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_QUARTZ);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_STONE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SNOW_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STAINED_GLASS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_BRICKS);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_WOOL);
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_BLOCK);
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_WALL);
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLE);

		RegistryClient.registerNetworkReceiver(PACKET_CHUNK_S2C, packet -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_DASHBOARD_SCREEN, packet -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_PIDS_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TICKET_MACHINE_SCREEN, packet -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TRAIN_SENSOR_SCREEN, packet -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, packet -> PacketTrainDataGuiClient.openResourcePackCreatorScreen(Minecraft.getInstance()));
		RegistryClient.registerNetworkReceiver(PACKET_ANNOUNCE, packet -> PacketTrainDataGuiClient.announceS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_GENERATE_PATH, packet -> PacketTrainDataGuiClient.generatePathS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_RAIL, packet -> PacketTrainDataGuiClient.createRailS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_SIGNAL, packet -> PacketTrainDataGuiClient.createSignalS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_NODE, packet -> PacketTrainDataGuiClient.removeNodeS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_RAIL, packet -> PacketTrainDataGuiClient.removeRailConnectionS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_SIGNALS, packet -> PacketTrainDataGuiClient.removeSignalsS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_WRITE_RAILS, packet -> ClientData.writeRails(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAINS, packet -> ClientData.updateTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_TRAINS, packet -> ClientData.deleteTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGERS, packet -> ClientData.updateTrainPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_RAIL_ACTIONS, packet -> ClientData.updateRailActions(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SCHEDULE, packet -> ClientData.updateSchedule(Minecraft.getInstance(), packet));

		BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
			}
		};

		Config.getPatreonList();
		Config.refreshProperties();

		RegistryClient.registerPlayerJoinEvent(player -> {
			Config.refreshProperties();
			isReplayMod = player.getClass().toGenericString().toLowerCase().contains("replaymod");
			System.out.println(isReplayMod ? "Running in Replay Mod mode" : "Not running in Replay Mod mode");
		});
	}

	public static boolean isReplayMod() {
		return isReplayMod;
	}

	public static float getGameTick() {
		return gameTick;
	}

	public static void incrementGameTick() {
		final float lastFrameDuration = getLastFrameDuration();
		gameTickTest += lastFrameDuration;
		if (isReplayMod || tick == 0) {
			gameTick += lastFrameDuration;
		}
		tick++;
		if (tick >= skipTicks) {
			tick = 0;
		}

		final long millis = System.currentTimeMillis();
		if (millis - startSampleMillis >= SAMPLE_MILLIS) {
			skipTicks = Math.round((gameTickTest - startSampleGameTick) * 50 / (millis - startSampleMillis));
			startSampleMillis = millis;
			startSampleGameTick = gameTickTest;
			if (skipTicks != lastSkipTicks) {
				System.out.println("Tick skip updated to " + skipTicks);
			}
			lastSkipTicks = skipTicks;
		}
	}

	public static float getLastFrameDuration() {
		return MTRClient.isReplayMod ? 20F / 60 : Minecraft.getInstance().getDeltaFrameTime();
	}

	public static boolean canPlaySound() {
		if (gameTick - lastPlayedTrainSoundsTick >= TICKS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsTick = gameTick;
		}
		return gameTick == lastPlayedTrainSoundsTick && !Minecraft.getInstance().isPaused();
	}

	@FunctionalInterface
	public interface RegisterItemModelPredicate {
		void accept(String id, Item item, String tag);
	}
}
