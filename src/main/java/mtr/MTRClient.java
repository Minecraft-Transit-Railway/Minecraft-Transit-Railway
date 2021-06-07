package mtr;

import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.gui.ClientData;
import mtr.item.ItemRailModifier;
import mtr.mixin.ModelPredicateRegisterInvoker;
import mtr.model.APGDoorModel;
import mtr.model.PSDDoorModel;
import mtr.model.PSDTopModel;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class MTRClient implements ClientModInitializer, IPacket {

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
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END_2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_COLOR_STAINED_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_WALL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_BARRIER_ENTRANCE_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_BARRIER_EXIT_1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_MACHINE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TICKET_PROCESSOR, RenderLayer.getCutout());

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> new ModelProvider());

		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_1_WOODEN, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_2_STONE, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_3_IRON, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_4_OBSIDIAN, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_5_BLAZE, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_6_DIAMOND, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_SIDING, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_CONNECTOR_PLATFORM, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);
		ModelPredicateRegisterInvoker.invokeRegister(Items.RAIL_REMOVER, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemRailModifier.TAG_POS) ? 1 : 0);

		EntityRendererRegistry.INSTANCE.register(MTR.SEAT, (dispatcher, context) -> new RenderSeat(dispatcher));

		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.CLOCK_TILE_ENTITY, RenderClock::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_1_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 1, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_2_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 3, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900));
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PIDS_3_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 2, 1.5F, 8.0F, 6, 7.5F, 29, true, true, false, 0xFF5E69, 1.45F, 0x599307,true));
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
		registerStationColor(Blocks.STATION_POLE);

		ClientPlayNetworking.registerGlobalReceiver(PACKET_CHUNK_S2C, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveChunk(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_DASHBOARD_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openDashboardScreenS2C(minecraftClient));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_RAILWAY_SIGN_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_OPEN_TICKET_MACHINE_SCREEN, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_CREATE_RAIL, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.createRailS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_REMOVE_NODE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.removeNodeS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_REMOVE_RAIL, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.removeRailConnectionS2C(minecraftClient, packet));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_STATION, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteStation(minecraftClient, packet, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_PLATFORM, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeletePlatform(minecraftClient, packet, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_SIDING, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteSiding(minecraftClient, packet, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_ROUTE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteRoute(minecraftClient, packet, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_DEPOT, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteDepot(minecraftClient, packet, false));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_STATION, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteStation(minecraftClient, packet, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_PLATFORM, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeletePlatform(minecraftClient, packet, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_SIDING, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteSiding(minecraftClient, packet, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_ROUTE, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteRoute(minecraftClient, packet, true));
		ClientPlayNetworking.registerGlobalReceiver(PACKET_DELETE_DEPOT, (minecraftClient, handler, packet, sender) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteDepot(minecraftClient, packet, true));

		Config.refreshProperties();

		ClientEntityEvents.ENTITY_LOAD.register((entity, clientWorld) -> {
			if (entity == MinecraftClient.getInstance().player) {
				Config.refreshProperties();
			}
		});
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new CustomResources());
	}

	private static void registerStationColor(Block block) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			final int defaultColor = 0x7F7F7F;
			if (pos != null) {
				return ClientData.stations.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
			} else {
				return defaultColor;
			}
		}, block);
	}

	private static class ModelProvider implements ModelResourceProvider {

		private static final Identifier APG_DOOR_MODEL = new Identifier("mtr:block/apg_door");
		private static final Identifier PSD_DOOR_MODEL_1 = new Identifier("mtr:block/psd_door_1");
		private static final Identifier PSD_DOOR_MODEL_2 = new Identifier("mtr:block/psd_door_2");
		private static final Identifier PSD_TOP_MODEL = new Identifier("mtr:block/psd_top");

		@Override
		public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
			if (identifier.equals(APG_DOOR_MODEL)) {
				return new APGDoorModel();
			} else if (identifier.equals(PSD_DOOR_MODEL_1)) {
				return new PSDDoorModel(0);
			} else if (identifier.equals(PSD_DOOR_MODEL_2)) {
				return new PSDDoorModel(1);
			} else if (identifier.equals(PSD_TOP_MODEL)) {
				return new PSDTopModel();
			} else {
				return null;
			}
		}
	}
}
