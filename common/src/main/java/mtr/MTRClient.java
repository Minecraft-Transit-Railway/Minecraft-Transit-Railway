package mtr;

import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.BlockTactileMap;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.item.ItemBlockClickingBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import mtr.servlet.Webserver;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;

public class MTRClient implements IPacket {

	private static boolean isReplayMod;
	private static boolean isVivecraft;
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
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_DOOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS_END.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CABLE_CAR_NODE_LOWER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CABLE_CAR_NODE_UPPER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CLOCK.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CIO.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CKT.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_HEO.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_MOS.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_PLAIN.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_SHM.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STAINED.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STW.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_TSH.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_WKS.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LOGO.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.RUBBISH_BIN_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.SIGNAL_LIGHT_1.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_WALL.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_ENTRANCE_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_EXIT_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_MACHINE.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENTRANCE.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_EXIT.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENQUIRY.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_ANNOUNCER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_LOADER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_UNLOADER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_REDSTONE_SENSOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_SCHEDULE_SENSOR.get());

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_PLATFORM.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_SIDING.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_TURN_BACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_CABLE_CAR.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_REMOVER.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_WHITE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_ORANGE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_MAGENTA.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_YELLOW.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIME.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PINK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_CYAN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PURPLE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BROWN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GREEN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_RED.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_WHITE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_ORANGE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_MAGENTA.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_YELLOW.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIME.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PINK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_CYAN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PURPLE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BROWN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GREEN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_RED.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_9.get(), ItemBlockClickingBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.BOAT_NODE_TILE_ENTITY.get(), RenderBoatNode::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY.get(), RenderClock::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_2_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 1));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY.get(), RenderPSDTop::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY.get(), RenderAPGGlass::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_DOOR_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 2));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, false, 0xFF9900, 0x33CC00, 1.25F, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_1.get(), dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2.get(), dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3.get(), dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4.get(), dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_WHITE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_GRAY_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_BLACK_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));

		RegistryClient.registerEntityRenderer(EntityTypes.SEAT.get(), RenderTrains::new);

		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_ANDESITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BEDROCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BIRCH_WOOD.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BONE_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CLAY.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COAL_ORE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COBBLESTONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_POWDER.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DARK_PRISMARINE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DIORITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_GRAVEL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_IRON_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_METAL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PLANKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_ANDESITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_DIORITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_PILLAR.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_PILLAR.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_QUARTZ.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_STONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SNOW_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STAINED_GLASS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_WOOL.get());

		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_ANDESITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BEDROCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BIRCH_WOOD_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BONE_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CLAY_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COAL_ORE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COBBLESTONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DIORITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_GRAVEL_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_IRON_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_METAL_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PLANKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_STONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SNOW_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_WOOL_SLAB.get());

		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get());
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_WALL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLE.get());

		RegistryClient.registerNetworkReceiver(PACKET_VERSION_CHECK, packet -> PacketTrainDataGuiClient.openVersionCheckS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CHUNK_S2C, packet -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_DASHBOARD_SCREEN, packet -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_PIDS_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openArrivalProjectorConfigScreenS2C(Minecraft.getInstance(), packet));
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
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, packet -> ClientData.updateTrainPassengerPosition(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_RAIL_ACTIONS, packet -> ClientData.updateRailActions(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SCHEDULE, packet -> ClientData.updateSchedule(Minecraft.getInstance(), packet));

		MTRClientLifts.init();

		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_ACCELERATE);
		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_BRAKE);
		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_TOGGLE_DOORS);

		BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
			}
		};

		Webserver.init();

		RegistryClient.registerPlayerJoinEvent(player -> {
			Config.refreshProperties();

			isReplayMod = player.getClass().toGenericString().toLowerCase().contains("replaymod");
			try {
				Class.forName("org.vivecraft.main.VivecraftMain");
				isVivecraft = true;
			} catch (Exception ignored) {
				isVivecraft = false;
			}

			System.out.println(isReplayMod ? "Running in Replay Mod mode" : "Not running in Replay Mod mode");
			System.out.println(isVivecraft ? "Vivecraft detected" : "Vivecraft not detected");

			final Minecraft minecraft = Minecraft.getInstance();
			if (!minecraft.hasSingleplayerServer()) {
				Webserver.callback = minecraft::execute;
				Webserver.getWorlds = () -> minecraft.level == null ? new ArrayList<>() : Collections.singletonList(minecraft.level);
				Webserver.getRoutes = railwayData -> ClientData.ROUTES;
				Webserver.getDataCache = railwayData -> ClientData.DATA_CACHE;
				Webserver.start(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("mtr_webserver_port.txt"));
			}
		});
		Registry.registerPlayerQuitEvent(player -> Webserver.stop());
	}

	public static boolean isReplayMod() {
		return isReplayMod;
	}

	public static boolean isVivecraft() {
		return isVivecraft;
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
		ClientData.tick();
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
