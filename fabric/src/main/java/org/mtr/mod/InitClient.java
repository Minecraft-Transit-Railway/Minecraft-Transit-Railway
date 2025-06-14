package org.mtr.mod;

import org.mtr.core.data.Depot;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Position;
import org.mtr.core.data.Station;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.servlet.WebServlet;
import org.mtr.core.servlet.Webserver;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.MultipartConfigElement;
import org.mtr.libraries.org.eclipse.jetty.servlet.ServletHolder;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.block.BlockStationNameTallBase;
import org.mtr.mod.block.BlockStationNameTallStanding;
import org.mtr.mod.block.BlockTactileMap;
import org.mtr.mod.block.BlockTrainAnnouncer;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;
import org.mtr.mod.entity.EntityRendering;
import org.mtr.mod.generated.WebserverResources;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.item.ItemBlockClickingBase;
import org.mtr.mod.item.ItemDriverKey;
import org.mtr.mod.packet.PacketRequestData;
import org.mtr.mod.render.*;
import org.mtr.mod.resource.CachedResource;
import org.mtr.mod.screen.BetaWarningScreen;
import org.mtr.mod.servlet.ClientServlet;
import org.mtr.mod.servlet.ResourcePackCreatorOperationServlet;
import org.mtr.mod.servlet.ResourcePackCreatorUploadServlet;
import org.mtr.mod.sound.LoopingSoundInstance;
import org.mtr.mod.sound.ScheduledSound;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

public final class InitClient {

	private static Webserver webserver;
	private static int serverPort;
	private static long lastMillis = 0;
	private static long gameMillis = 0;
	private static long lastPlayedTrainSoundsMillis = 0;
	private static long lastUpdatePacketMillis = 0;
	private static Runnable movePlayer;
	private static ClientWorld lastClientWorld;

	public static final RegistryClient REGISTRY_CLIENT = new RegistryClient(Init.REGISTRY);
	public static final int MILLIS_PER_SPEED_SOUND = 200;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");

	static {
		Init.createWebserverSetup(InitClient::setupWebserver);
	}

	public static void init() {
		KeyBindings.init();
		Init.writeFromClient();

		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_DOOR);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_GLASS);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.APG_GLASS_END);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CABLE_CAR_NODE_LOWER);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CABLE_CAR_NODE_UPPER);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CLOCK);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_CIO);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_CKT);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_HEO);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_MOS);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_PLAIN);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_SHM);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_STAINED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_STW);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_TSH);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.GLASS_FENCE_WKS);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LOGO);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_INDENTED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_SLAB);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_1_INDENTED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_1_SLAB);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_2_INDENTED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_NA_2_SLAB);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_UK_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_UK_1_INDENTED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PLATFORM_UK_1_SLAB);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_DOOR_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_END_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_DOOR_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.PSD_GLASS_END_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.RUBBISH_BIN_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.STATION_COLOR_STAINED_GLASS);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getTranslucent(), Blocks.STATION_COLOR_STAINED_GLASS_SLAB);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_BLOCK);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_WALL);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.STATION_NAME_TALL_STANDING);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_BARRIER_ENTRANCE_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_BARRIER_EXIT_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_MACHINE);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_ENTRANCE);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_EXIT);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TICKET_PROCESSOR_ENQUIRY);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_ANNOUNCER);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_CARGO_LOADER);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_CARGO_UNLOADER);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_REDSTONE_SENSOR);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.TRAIN_SCHEDULE_SENSOR);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_DOOR_EVEN_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_DOOR_ODD_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_EVEN_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_ODD_1);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_EVEN_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.LIFT_PANEL_ODD_2);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.ESCALATOR_STEP);
		REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.ESCALATOR_SIDE);

		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1, RenderLiftButtons::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_1, dispatcher -> new RenderLiftPanel<>(dispatcher, false, false));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_1, dispatcher -> new RenderLiftPanel<>(dispatcher, true, false));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_2, dispatcher -> new RenderLiftPanel<>(dispatcher, false, true));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_2, dispatcher -> new RenderLiftPanel<>(dispatcher, true, true));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_DOOR_EVEN_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 3));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.LIFT_DOOR_ODD_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 4));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL, dispatcher -> new RenderPIDS<>(dispatcher, 1, 15, 16, 14, 14, false, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM, dispatcher -> new RenderPIDS<>(dispatcher, -15, 15, 16, 30, 46, false, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE, dispatcher -> new RenderPIDS<>(dispatcher, -15, 15, 16, 46, 46, false, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.CLOCK, RenderClock::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.DRIVER_KEY_DISPENSER, RenderDriverKeyDispenser::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PSD_DOOR_1, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PSD_DOOR_2, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PSD_TOP, RenderPSDTop::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.APG_GLASS, RenderAPGGlass::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.APG_DOOR, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 2));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PIDS_HORIZONTAL_1, dispatcher -> new RenderPIDS<>(dispatcher, 1, 3.25F, 6, 2.5F, 30, true, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PIDS_HORIZONTAL_2, dispatcher -> new RenderPIDS<>(dispatcher, 1.5F, 7.5F, 6, 6.5F, 29, true, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PIDS_HORIZONTAL_3, dispatcher -> new RenderPIDS<>(dispatcher, 2.5F, 7.5F, 6, 6.5F, 27, true, 1.25F));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PIDS_VERTICAL_1, dispatcher -> new RenderPIDS<>(dispatcher, 2F, 14F, 15, 28F, 12, false, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.PIDS_VERTICAL_SINGLE_ARRIVAL_1, dispatcher -> new RenderPIDS<>(dispatcher, 2F, 14F, 15, 28F, 12, false, 1));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD, RenderRailwaySign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT, RenderRouteSign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL, RenderRouteSign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT, RenderRouteSign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL, RenderRouteSign::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, 0xFF0000FF));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, 0xFF0000FF));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, 0xFF00FF00));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4, dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, 0xFF00FF00));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1, RenderSignalLight3Aspect::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_2, RenderSignalLight3Aspect::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1, RenderSignalLight4Aspect::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2, RenderSignalLight4Aspect::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1, RenderSignalSemaphore::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2, RenderSignalSemaphore::new);
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE, dispatcher -> new RenderStationNameTiled<>(dispatcher, true));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK, dispatcher -> new RenderStationNameTall<>(dispatcher, BlockStationNameTallBase.WIDTH, BlockStationNameTallBase.HEIGHT, 0));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED, dispatcher -> new RenderStationNameTall<>(dispatcher, BlockStationNameTallBase.WIDTH, BlockStationNameTallBase.HEIGHT, 0));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL, dispatcher -> new RenderStationNameTall<>(dispatcher, BlockStationNameTallBase.WIDTH, BlockStationNameTallBase.HEIGHT, 0));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_STANDING, dispatcher -> new RenderStationNameTall<>(dispatcher, BlockStationNameTallStanding.WIDTH, BlockStationNameTallStanding.HEIGHT, BlockStationNameTallStanding.OFFSET_Y));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_WHITE, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_GRAY, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_BLACK, dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		REGISTRY_CLIENT.registerBlockEntityRenderer(BlockEntityTypes.EYE_CANDY, RenderEyeCandy::new);

		REGISTRY_CLIENT.registerEntityRenderer(EntityTypes.RENDERING, MainRenderer::new);

		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_20, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_20_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_40, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_40_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_60, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_60_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_80, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_80_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_100, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_100_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_120, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_120_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_140, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_140_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_160, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_160_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_200, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_200_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_300, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_300_ONE_WAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_PLATFORM, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_SIDING, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_TURN_BACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_CABLE_CAR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_CONNECTOR_RUNWAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.RAIL_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_WHITE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_ORANGE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_MAGENTA, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIGHT_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_YELLOW, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIME, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_PINK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_LIGHT_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_CYAN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_PURPLE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BROWN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_GREEN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_RED, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_CONNECTOR_BLACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_WHITE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_ORANGE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_MAGENTA, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIGHT_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_YELLOW, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIME, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_PINK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_LIGHT_GRAY, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_CYAN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_PURPLE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_BLUE, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_BROWN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_GREEN, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_RED, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.SIGNAL_REMOVER_BLACK, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.BRIDGE_CREATOR_1, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.BRIDGE_CREATOR_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.BRIDGE_CREATOR_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.BRIDGE_CREATOR_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.BRIDGE_CREATOR_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_4_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_5_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_CREATOR_6_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_4_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_5_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_3, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_5, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_7, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.TUNNEL_WALL_CREATOR_6_9, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.LIFT_BUTTONS_LINK_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
		REGISTRY_CLIENT.registerItemModelPredicate(Items.LIFT_BUTTONS_LINK_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());

		REGISTRY_CLIENT.registerBlockColors((blockState, blockRenderView, blockPos, tintIndex) -> getStationColor(blockPos),
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
				Blocks.STATION_COLOR_MOSAIC_TILE,
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
				Blocks.STATION_COLOR_MOSAIC_TILE_SLAB,
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
				Blocks.STATION_NAME_TALL_WALL,
				Blocks.STATION_NAME_TALL_STANDING
		);

		REGISTRY_CLIENT.registerItemColors((itemStack, tintIndex) -> {
			final Item item = itemStack.getItem();
			return item.data instanceof ItemDriverKey ? ((ItemDriverKey) item.data).color : IGui.RGB_WHITE;
		}, Items.BASIC_DRIVER_KEY, Items.ADVANCED_DRIVER_KEY, Items.GUARD_KEY);

		REGISTRY_CLIENT.setupPackets(new Identifier(Init.MOD_ID, "packet"));

		REGISTRY_CLIENT.eventRegistryClient.registerClientJoin(() -> {
			MinecraftClientData.reset();
			DynamicTextureCache.instance = new DynamicTextureCache();
			lastMillis = System.currentTimeMillis();
			gameMillis = 0;
			lastUpdatePacketMillis = 0;
			DynamicTextureCache.instance.reload();

			// Clientside webserver for locally hosting the online system map
			// Only start clientside webserver if not in singleplayer
			if (Init.getServerPort() == 0) {
				serverPort = Init.findFreePort(0);
				webserver = new Webserver(serverPort);
				webserver.addServlet(new ServletHolder(new ClientServlet()), "/");
				setupWebserver(webserver);
				webserver.start();
			} else {
				serverPort = Math.max(Init.getServerPort(), 0);
			}
			if (serverPort > 0) {
				Init.LOGGER.info("Open the Transport System Map at http://localhost:{}", serverPort);
				Init.LOGGER.info("Open the Resource Pack Creator at http://localhost:{}/creator/", serverPort);
			} else {
				Init.LOGGER.info("Transport System Map and Resource Pack Creator disabled");
			}
		});

		REGISTRY_CLIENT.eventRegistryClient.registerClientDisconnect(() -> {
			if (webserver != null) {
				webserver.stop();
				webserver = null;
			}
			serverPort = 0;
		});

		REGISTRY_CLIENT.eventRegistryClient.registerStartClientTick(() -> {
			final long currentMillis = System.currentTimeMillis();
			final long millisElapsed = currentMillis - lastMillis;
			lastMillis = currentMillis;
			gameMillis += millisElapsed;
			CachedResource.tick();
			BetaWarningScreen.handle();

			final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
			if (clientWorld != null) {
				final boolean[] shouldCreateEntity = {true};
				MinecraftClientHelper.getEntities(entity -> {
					if (entity.data instanceof EntityRendering) {
						shouldCreateEntity[0] = false;
						((EntityRendering) entity.data).update();
					}
				});
				if (shouldCreateEntity[0]) {
					MinecraftClientHelper.addEntity(new EntityRendering(new World(clientWorld.data)));
				}

				// If world or dimension changed, reset the data
				if (lastClientWorld == null || !lastClientWorld.equals(clientWorld)) {
					lastClientWorld = clientWorld;
					MinecraftClientData.reset();
				}
			}

			BlockTrainAnnouncer.processQueue();
			ResourcePackCreatorOperationServlet.tick(millisElapsed);

			// If player is moving, send a request every 0.5 seconds to the server to fetch any new nearby data
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity != null && lastUpdatePacketMillis > 0 && getGameMillis() > lastUpdatePacketMillis) {
				final DataRequest dataRequest = new DataRequest(clientPlayerEntity.getUuid(), Init.blockPosToPosition(MinecraftClient.getInstance().getGameRendererMapped().getCamera().getBlockPos()), MinecraftClientHelper.getRenderDistance() * 16L);
				dataRequest.writeExistingIds(MinecraftClientData.getInstance());
				InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketRequestData(dataRequest));
				lastUpdatePacketMillis = 0;
			}
		});

		REGISTRY_CLIENT.eventRegistryClient.registerEndClientTick(() -> {
			if (movePlayer != null) {
				movePlayer.run();
				movePlayer = null;
			}
			ScheduledSound.playScheduledSounds();
		});

		REGISTRY_CLIENT.eventRegistryClient.registerChunkLoad((clientWorld, worldChunk) -> {
			if (lastUpdatePacketMillis == 0) {
				lastUpdatePacketMillis = getGameMillis() + 500;
			}
		});

		REGISTRY_CLIENT.eventRegistryClient.registerResourceReloadEvent(CustomResourceLoader::reload);

		REGISTRY_CLIENT.eventRegistryClient.registerGuiRendering(DrivingGuiRenderer::render);

		Config.init(MinecraftClient.getInstance().getRunDirectoryMapped());

		BlockTactileMap.BlockEntity.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.BlockEntity.onUse = blockPos -> {
			final Station station = findStation(blockPos);
			if (station != null) {
				final String text = IGui.formatStationName(IGui.insertTranslation(TranslationProvider.GUI_MTR_WELCOME_STATION_CJK, TranslationProvider.GUI_MTR_WELCOME_STATION, 1, IGui.textOrUntitled(station.getName())));
				IDrawing.narrateOrAnnounce(text, ObjectArrayList.of(TextHelper.literal(text)));
			}
		};

		// Finish registration
		REGISTRY_CLIENT.init();
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
		if (getGameMillis() - lastPlayedTrainSoundsMillis >= MILLIS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsMillis = getGameMillis();
		}
		return getGameMillis() == lastPlayedTrainSoundsMillis && !MinecraftClient.getInstance().isPaused();
	}

	public static Station findStation(BlockPos blockPos) {
		return MinecraftClientData.getInstance().stations.stream().filter(station -> station.inArea(Init.blockPosToPosition(blockPos))).findFirst().orElse(null);
	}

	public static void findClosePlatform(BlockPos blockPos, int radius, Consumer<Platform> consumer) {
		final Position position = Init.blockPosToPosition(blockPos);
		MinecraftClientData.getInstance().platforms.stream().filter(platform -> platform.closeTo(Init.blockPosToPosition(blockPos), radius)).min(Comparator.comparingDouble(platform -> platform.getApproximateClosestDistance(position, MinecraftClientData.getInstance()))).ifPresent(consumer);
	}

	@Nullable
	public static Depot findDepot(BlockPos blockPos) {
		final Position position = Init.blockPosToPosition(blockPos);
		for (final Depot depot : MinecraftClientData.getInstance().depots) {
			if (depot.inArea(position)) {
				return depot;
			}
		}
		return null;
	}

	public static void transformToFacePlayer(GraphicsHolder graphicsHolder, double x, double y, double z) {
		final Vector3d cameraPosition = MinecraftClient.getInstance().getGameRendererMapped().getCamera().getPos();
		final double differenceX = cameraPosition.getXMapped() - x;
		final double differenceY = cameraPosition.getYMapped() - y;
		final double differenceZ = cameraPosition.getZMapped() - z;
		graphicsHolder.rotateYRadians((float) (Math.atan2(differenceX, differenceZ) + Math.PI));
		graphicsHolder.rotateXRadians((float) Math.atan2(differenceY, Math.sqrt(differenceZ * differenceZ + differenceX * differenceX)));
	}

	public static String getShiftText() {
		return MinecraftClient.getInstance().getOptionsMapped().getKeySneakMapped().getBoundKeyLocalizedText().getString();
	}

	public static String getRightClickText() {
		return MinecraftClient.getInstance().getOptionsMapped().getKeyUseMapped().getBoundKeyLocalizedText().getString();
	}

	public static float getGameTick() {
		return gameMillis / 50F;
	}

	public static long getGameMillis() {
		return gameMillis;
	}

	/**
	 * @return the port of the clientside webserver (multiplayer) or the webserver started by Transport Simulation Core (singleplayer).
	 * <br>{@code 0} means the webserver is not running
	 */
	public static int getServerPort() {
		return serverPort;
	}

	private static RegistryClient.ModelPredicateProvider checkItemPredicateTag() {
		return (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemBlockClickingBase.TAG_POS) ? 1 : 0;
	}

	private static void setupWebserver(Webserver webserver) {
		webserver.addServlet(new ServletHolder(new ResourcePackCretorWebServlet(WebserverResources::get, "/creator/")), "/creator/*");
		webserver.addServlet(new ServletHolder(new ResourcePackCreatorOperationServlet()), "/mtr/api/creator/operation/*");
		final ServletHolder resourcePackCreatorUploadServletHolder = new ServletHolder(new ResourcePackCreatorUploadServlet());
		resourcePackCreatorUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement((String) null));
		webserver.addServlet(resourcePackCreatorUploadServletHolder, "/mtr/api/creator/upload/*");
	}

	private static class ResourcePackCretorWebServlet extends WebServlet {

		public ResourcePackCretorWebServlet(Function<String, String> contentProvider, String expectedPath) {
			super(contentProvider, expectedPath);
		}
	}
}
