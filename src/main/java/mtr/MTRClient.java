package mtr;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
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
import mtr.mixin.ModelPredicateRegisterInvoker;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import mtr.sound.LoopingSoundInstance;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class MTRClient implements ClientModInitializer, IPacket {

	public static boolean isReplayMod;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS_END, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_CIO, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_CKT, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_HEO, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_MOS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_PLAIN, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_SHM, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_STAINED, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_STW, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_TSH, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLASS_FENCE_WKS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LOGO, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_INDENTED, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_NA_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_NA_1_INDENTED, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_NA_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_NA_2_INDENTED, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_UK_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_UK_1_INDENTED, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.RUBBISH_BIN_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SIGNAL_LIGHT_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_COLOR_STAINED_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_WALL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_BARRIER_ENTRANCE_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_BARRIER_EXIT_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_MACHINE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_PROCESSOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_PROCESSOR_ENTRANCE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_PROCESSOR_EXIT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_PROCESSOR_ENQUIRY, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TRAIN_ANNOUNCER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TRAIN_REDSTONE_SENSOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TRAIN_SCHEDULE_SENSOR, RenderLayer.getCutout());


		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_1_WOODEN);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_1_WOODEN_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_2_STONE);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_2_STONE_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_3_IRON);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_3_IRON_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_4_OBSIDIAN);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_5_BLAZE);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_5_BLAZE_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_6_DIAMOND);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_6_DIAMOND_ONE_WAY);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_PLATFORM);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_SIDING);
		registerNodeModifierPredicate(Items.RAIL_CONNECTOR_TURN_BACK);
		registerNodeModifierPredicate(Items.RAIL_REMOVER);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_WHITE);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_ORANGE);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_MAGENTA);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_LIGHT_BLUE);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_YELLOW);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_LIME);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_PINK);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_GRAY);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_LIGHT_GRAY);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_CYAN);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_PURPLE);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_BLUE);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_BROWN);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_GREEN);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_RED);
		registerNodeModifierPredicate(Items.SIGNAL_CONNECTOR_BLACK);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_WHITE);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_ORANGE);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_MAGENTA);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_LIGHT_BLUE);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_YELLOW);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_LIME);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_PINK);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_GRAY);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_LIGHT_GRAY);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_CYAN);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_PURPLE);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_BLUE);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_BROWN);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_GREEN);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_RED);
		registerNodeModifierPredicate(Items.SIGNAL_REMOVER_BLACK);

		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.CLOCK_TILE_ENTITY, RenderClock::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_1_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_2_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_3_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, false, 0xFF9900, 0x33CC00, 1.25F, true));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_2_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_2_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_3_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_3_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_4_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_4_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_5_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_5_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_6_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_6_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_7_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.RAILWAY_SIGN_7_ODD_TILE_ENTITY, RenderRailwaySign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY, RenderRouteSign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ROUTE_SIGN_WALL_METAL_TILE_ENTITY, RenderRouteSign::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_LIGHT_1, dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_LIGHT_2, dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_LIGHT_3, dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_LIGHT_4, dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_SEMAPHORE_1, dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.SIGNAL_SEMAPHORE_2, dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_ENTRANCE_TILE_ENTITY, RenderStationNameEntrance::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_TALL_BLOCK_TILE_ENTITY, RenderStationNameTall::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_TALL_WALL_TILE_ENTITY, RenderStationNameTall::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_WALL_TILE_ENTITY, RenderStationNameWall::new);

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

		ClientPlayNetworking.registerGlobalReceiver(PACKET_CHUNK_S2C, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveChunk(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_DASHBOARD_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openDashboardScreenS2C(minecraftClient));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_PIDS_CONFIG_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_RAILWAY_SIGN_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_TICKET_MACHINE_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_TRAIN_SENSOR_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_ANNOUNCE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.announceS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_GENERATE_PATH, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.generatePathS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_CREATE_RAIL, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.createRailS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_CREATE_SIGNAL, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.createSignalS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_REMOVE_NODE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.removeNodeS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_REMOVE_RAIL, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.removeRailConnectionS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_REMOVE_SIGNALS, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.removeSignalsS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_STATION, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_PLATFORM, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_SIDING, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_ROUTE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_DEPOT, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_STATION, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_PLATFORM, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_SIDING, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_ROUTE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_DEPOT, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraftClient, packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_WRITE_RAILS, (minecraftClient, handler, packet, sender) -> ClientData.writeRails(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_TRAINS, (minecraftClient, handler, packet, sender) -> ClientData.updateTrains(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_TRAINS, (minecraftClient, handler, packet, sender) -> ClientData.deleteTrains(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_TRAIN_RIDING_POSITION, (minecraftClient, handler, packet, sender) -> ClientData.updateTrainRidingPosition(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_SCHEDULE, (minecraftClient, handler, packet, sender) -> ClientData.updateSchedule(minecraftClient, packet));

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

		ClientEntityEvents.ENTITY_LOAD.register((entity, clientWorld) -> {
			if (entity == MinecraftClient.getInstance().player) {
				Config.refreshProperties();
				isReplayMod = entity.getClass().toGenericString().toLowerCase().contains("replaymod");
			}
		});
		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			final MatrixStack matrices = context.matrixStack();
			matrices.push();
			RenderTrains.render(context.world(), matrices, context.consumers(), context.camera());
			matrices.pop();
		});
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new CustomResources());
	}

	private static void registerNodeModifierPredicate(Item item) {
		ModelPredicateRegisterInvoker.invokeRegister(item, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemNodeModifierBase.TAG_POS) ? 1 : 0);
	}

	private static void registerStationColor(Block block) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			final int defaultColor = 0x7F7F7F;
			if (pos != null) {
				return ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
			} else {
				return defaultColor;
			}
		}, block);
	}
}
