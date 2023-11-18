package org.mtr.mod;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.EventRegistryClient;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.block.*;
import org.mtr.mod.client.*;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSType;
import org.mtr.mod.entity.EntityRendering;
import org.mtr.mod.item.ItemBlockClickingBase;
import org.mtr.mod.packet.PacketRequestData;
import org.mtr.mod.render.*;
import org.mtr.mod.sound.LoopingSoundInstance;

import javax.annotation.Nullable;

public final class InitClient {

	private static long lastMillis = 0;
	private static long gameMillis = 0;
	private static float lastPlayedTrainSoundsTick = 0;
	private static BlockPos lastPosition;
	private static Runnable movePlayer;

	public static final int TICKS_PER_SPEED_SOUND = 4;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");
	private static final int SAMPLE_MILLIS = 1000;

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_DOOR);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_GLASS);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_GLASS_END);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CABLE_CAR_NODE_LOWER);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CABLE_CAR_NODE_UPPER);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CLOCK);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_CIO);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_CKT);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_HEO);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_MOS);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_PLAIN);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_SHM);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_STAINED);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_STW);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_TSH);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_WKS);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LOGO);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_INDENTED);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_1_INDENTED);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_2_INDENTED);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_UK_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_UK_1_INDENTED);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_DOOR_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_END_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_DOOR_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_END_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.RUBBISH_BIN_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.STATION_COLOR_STAINED_GLASS);
		RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.STATION_COLOR_STAINED_GLASS_SLAB);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_BLOCK);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_WALL);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_BARRIER_ENTRANCE_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_BARRIER_EXIT_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_MACHINE);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_ENTRANCE);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_EXIT);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_ENQUIRY);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_ANNOUNCER);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_CARGO_LOADER);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_CARGO_UNLOADER);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_REDSTONE_SENSOR);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_SCHEDULE_SENSOR);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_DOOR_EVEN_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_DOOR_ODD_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_EVEN_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_ODD_1);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_EVEN_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_ODD_2);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.ESCALATOR_STEP);
		RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), Blocks.ESCALATOR_SIDE);

		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1, RenderLiftButtons::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_1, dispatcher -> new RenderLiftPanel<>(dispatcher, false, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_1, dispatcher -> new RenderLiftPanel<>(dispatcher, true, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_2, dispatcher -> new RenderLiftPanel<>(dispatcher, false, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_2, dispatcher -> new RenderLiftPanel<>(dispatcher, true, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_DOOR_EVEN_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 3));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.LIFT_DOOR_ODD_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 4));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 1, 15, 16, 14, 14, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, -15, 15, 16, 30, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE, dispatcher -> new RenderPIDS<>(dispatcher, 16, 1, -15, 15, 16, 46, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BOAT_NODE, RenderBoatNode::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.CLOCK, RenderClock::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PSD_DOOR_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PSD_DOOR_2, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 1));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PSD_TOP, RenderPSDTop::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.APG_GLASS, RenderAPGGlass::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.APG_DOOR, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 2));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_1, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.BlockEntity.MAX_ARRIVALS, BlockPIDS1.BlockEntity.LINES_PER_ARRIVAL, 1, 3.25F, 6, 2.5F, 30, true, false, PIDSType.PIDS, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_2, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.BlockEntity.MAX_ARRIVALS, BlockPIDS2.BlockEntity.LINES_PER_ARRIVAL, 1.5F, 7.5F, 6, 6.5F, 29, true, true, PIDSType.PIDS, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_3, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.BlockEntity.MAX_ARRIVALS, BlockPIDS3.BlockEntity.LINES_PER_ARRIVAL, 2.5F, 7.5F, 6, 6.5F, 27, true, false, PIDSType.PIDS, 0xFF9900, 0x33CC00, 1.25F, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_4, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS4.BlockEntity.MAX_ARRIVALS, BlockPIDS4.BlockEntity.LINES_PER_ARRIVAL, 2F, 14F, 15, 28F, 12, false, false, PIDSType.PIDS_VERTICAL, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_SINGLE_ARRIVAL_1, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDSSingleArrival1.BlockEntity.MAX_ARRIVALS, BlockPIDSSingleArrival1.BlockEntity.LINES_PER_ARRIVAL, 2F, 14F, 15, 28F, 12, false, false, PIDSType.PIDS_SINGLE_ARRIVAL, 0xFF9900, 0xFF9900));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD, RenderRailwaySign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT, RenderRouteSign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL, RenderRouteSign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT, RenderRouteSign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL, RenderRouteSign::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, false, 0xFF0000FF));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, false, 0xFF0000FF));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, true, 0xFF00FF00));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, true, 0xFF00FF00));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1, dispatcher -> new RenderSignalLight3Aspect<>(dispatcher, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_2, dispatcher -> new RenderSignalLight3Aspect<>(dispatcher, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1, dispatcher -> new RenderSignalLight4Aspect<>(dispatcher, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2, dispatcher -> new RenderSignalLight4Aspect<>(dispatcher, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1, dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2, dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE, dispatcher -> new RenderStationNameTiled<>(dispatcher, true));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK, RenderStationNameTall::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED, RenderStationNameTall::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL, RenderStationNameTall::new);
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_WHITE, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_GRAY, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_BLACK, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));

		RegistryClient.registerEntityRenderer(EntityTypes.RENDERING, RenderTrains::new);
		RegistryClient.worldRenderingEntity = EntityRendering::new;

		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_100, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_100_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_20, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_20_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_40, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_40_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_60, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_60_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_80, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_80_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_120, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_120_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_160, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_160_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_200, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_200_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_300, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_300_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_PLATFORM, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_SIDING, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_TURN_BACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_CABLE_CAR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_CONNECTOR_RUNWAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.RAIL_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_WHITE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_ORANGE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_MAGENTA, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIGHT_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_YELLOW, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIME, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_PINK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIGHT_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_CYAN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_PURPLE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BROWN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_GREEN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_RED, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BLACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_WHITE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_ORANGE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_MAGENTA, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIGHT_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_YELLOW, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIME, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_PINK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIGHT_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_CYAN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_PURPLE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_BROWN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_GREEN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_RED, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.SIGNAL_REMOVER_BLACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.BRIDGE_CREATOR_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.BRIDGE_CREATOR_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.BRIDGE_CREATOR_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.BRIDGE_CREATOR_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.LIFT_BUTTONS_LINK_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		RegistryClient.registerItemModelPredicate(Items.LIFT_BUTTONS_LINK_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());

		RegistryClient.registerBlockColors((blockState, blockRenderView, blockPos, tintIndex) -> getStationColor(blockPos),
				Blocks.STATION_COLOR_ANDESITE,
				Blocks.STATION_COLOR_BEDROCK,
				Blocks.STATION_COLOR_BIRCH_WOOD,
				Blocks.STATION_COLOR_BONE_BLOCK,
				Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK,
				Blocks.STATION_COLOR_CHISELED_STONE_BRICKS,
				Blocks.STATION_COLOR_CLAY,
				Blocks.STATION_COLOR_COAL_ORE,
				Blocks.STATION_COLOR_COBBLESTONE,
				Blocks.STATION_COLOR_CONCRETE,
				Blocks.STATION_COLOR_CONCRETE_POWDER,
				Blocks.STATION_COLOR_CRACKED_STONE_BRICKS,
				Blocks.STATION_COLOR_DARK_PRISMARINE,
				Blocks.STATION_COLOR_DIORITE,
				Blocks.STATION_COLOR_GRAVEL,
				Blocks.STATION_COLOR_IRON_BLOCK,
				Blocks.STATION_COLOR_METAL,
				Blocks.STATION_COLOR_PLANKS,
				Blocks.STATION_COLOR_POLISHED_ANDESITE,
				Blocks.STATION_COLOR_POLISHED_DIORITE,
				Blocks.STATION_COLOR_PURPUR_BLOCK,
				Blocks.STATION_COLOR_PURPUR_PILLAR,
				Blocks.STATION_COLOR_QUARTZ_BLOCK,
				Blocks.STATION_COLOR_QUARTZ_BRICKS,
				Blocks.STATION_COLOR_QUARTZ_PILLAR,
				Blocks.STATION_COLOR_SMOOTH_QUARTZ,
				Blocks.STATION_COLOR_SMOOTH_STONE,
				Blocks.STATION_COLOR_SNOW_BLOCK,
				Blocks.STATION_COLOR_STAINED_GLASS,
				Blocks.STATION_COLOR_STONE,
				Blocks.STATION_COLOR_STONE_BRICKS,
				Blocks.STATION_COLOR_WOOL,

				Blocks.STATION_COLOR_ANDESITE_SLAB,
				Blocks.STATION_COLOR_BEDROCK_SLAB,
				Blocks.STATION_COLOR_BIRCH_WOOD_SLAB,
				Blocks.STATION_COLOR_BONE_BLOCK_SLAB,
				Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB,
				Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB,
				Blocks.STATION_COLOR_CLAY_SLAB,
				Blocks.STATION_COLOR_COAL_ORE_SLAB,
				Blocks.STATION_COLOR_COBBLESTONE_SLAB,
				Blocks.STATION_COLOR_CONCRETE_SLAB,
				Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB,
				Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB,
				Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB,
				Blocks.STATION_COLOR_DIORITE_SLAB,
				Blocks.STATION_COLOR_GRAVEL_SLAB,
				Blocks.STATION_COLOR_IRON_BLOCK_SLAB,
				Blocks.STATION_COLOR_METAL_SLAB,
				Blocks.STATION_COLOR_PLANKS_SLAB,
				Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB,
				Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB,
				Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB,
				Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB,
				Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB,
				Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB,
				Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB,
				Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB,
				Blocks.STATION_COLOR_SMOOTH_STONE_SLAB,
				Blocks.STATION_COLOR_SNOW_BLOCK_SLAB,
				Blocks.STATION_COLOR_STAINED_GLASS_SLAB,
				Blocks.STATION_COLOR_STONE_SLAB,
				Blocks.STATION_COLOR_STONE_BRICKS_SLAB,
				Blocks.STATION_COLOR_WOOL_SLAB,

				Blocks.STATION_COLOR_POLE,
				Blocks.STATION_NAME_TALL_BLOCK,
				Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED,
				Blocks.STATION_NAME_TALL_WALL
		);

		RegistryClient.setupPackets(new Identifier(Init.MOD_ID, "packet"));

		EventRegistryClient.registerClientJoin(() -> {
			ClientData.instance = new ClientData();
			DynamicTextureCache.instance = new DynamicTextureCache();
			lastMillis = System.currentTimeMillis();
			gameMillis = 0;
			lastPosition = null;
			CustomResourceLoader.reload(); // TODO texture reload event not always working
		});

		EventRegistryClient.registerStartClientTick(() -> {
			incrementGameMillis();
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity != null) {
				final BlockPos blockPos = clientPlayerEntity.getBlockPos();
				if (lastPosition == null || lastPosition.getManhattanDistance(new Vector3i(blockPos.data)) > 8) {
					RegistryClient.sendPacketToServer(new PacketRequestData(false));
					lastPosition = blockPos;
				}
			}
		});

		EventRegistryClient.registerEndClientTick(() -> {
			if (movePlayer != null) {
				movePlayer.run();
				movePlayer = null;
			}
		});

		Patreon.getPatreonList(Config.PATREON_LIST);
		Config.refreshProperties();

		BlockTactileMap.BlockEntity.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.BlockEntity.onUse = blockPos -> {
			final Station station = findStation(blockPos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.getName())));
			}
		};

		// Finish registration
		RegistryClient.init();
	}

	public static int getStationColor(@Nullable BlockPos blockPos) {
		final int defaultColor = 0x7F7F7F;
		if (blockPos == null) {
			return defaultColor;
		} else {
			final Station station = findStation(blockPos);
			return station == null ? defaultColor : station.getColor();
		}
	}

	public static void scheduleMovePlayer(Runnable movePlayer) {
		InitClient.movePlayer = movePlayer;
	}

	public static boolean canPlaySound() {
		if (getGameTick() - lastPlayedTrainSoundsTick >= TICKS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsTick = getGameTick();
		}
		return getGameTick() == lastPlayedTrainSoundsTick && !MinecraftClient.getInstance().isPaused();
	}

	public static Station findStation(BlockPos blockPos) {
		return ClientData.instance.stations.stream().filter(station -> station.inArea(Init.blockPosToPosition(blockPos))).findFirst().orElse(null);
	}

	public static String getShiftText() {
		return MinecraftClient.getInstance().getOptionsMapped().getKeySneakMapped().getBoundKeyLocalizedText().getString();
	}

	public static long serializeExit(String exit) {
		final char[] characters = exit.toCharArray();
		long code = 0;
		for (final char character : characters) {
			code = code << 8;
			code += character;
		}
		return code;
	}

	public static String deserializeExit(long code) {
		StringBuilder exit = new StringBuilder();
		long charCodes = code;
		while (charCodes > 0) {
			exit.insert(0, (char) (charCodes & 0xFF));
			charCodes = charCodes >> 8;
		}
		return exit.toString();
	}

	public static float getGameTick() {
		return gameMillis / 50F;
	}

	public static long getGameMillis() {
		return gameMillis;
	}

	public static void incrementGameMillis() {
		final long currentMillis = System.currentTimeMillis();
		final long millisElapsed = currentMillis - lastMillis;
		lastMillis = currentMillis;
		gameMillis += millisElapsed;
	}

	private static RegistryClient.ModelPredicateProvider checkItemPredicateTag() {
		return (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemBlockClickingBase.TAG_POS) ? 1 : 0;
	}
}
