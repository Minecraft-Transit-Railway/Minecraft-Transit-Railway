package mtr;

import mtr.gui.ClientData;
import mtr.model.APGDoorModel;
import mtr.model.PSDDoorModel;
import mtr.model.PSDTopModel;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class MTRClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS_END, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LOGO, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_COLOR_STAINED_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STATION_NAME_TALL_WALL, RenderLayer.getCutout());

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> new ModelProvider());

		EntityRendererRegistry.INSTANCE.register(MTR.MINECART, (dispatcher, context) -> new RenderMinecart(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.SP1900, (dispatcher, context) -> new RenderSP1900(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.SP1900_MINI, (dispatcher, context) -> new RenderSP1900Mini(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.M_TRAIN, (dispatcher, context) -> new RenderMTrain(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.M_TRAIN_MINI, (dispatcher, context) -> new RenderMTrainMini(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.LIGHT_RAIL_1, (dispatcher, context) -> new RenderLightRail1(dispatcher));

		BlockEntityRendererRegistry.INSTANCE.register(MTR.CLOCK_TILE_ENTITY, RenderClock::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_TALL_BLOCK_TILE_ENTITY, RenderStationName::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_TALL_WALL_TILE_ENTITY, RenderStationName::new);
		BlockEntityRendererRegistry.INSTANCE.register(MTR.STATION_NAME_WALL_TILE_ENTITY, RenderStationName::new);

		registerStationColor(Blocks.STATION_COLOR_ANDESITE);
		registerStationColor(Blocks.STATION_COLOR_BEDROCK);
		registerStationColor(Blocks.STATION_COLOR_BIRCH_WOOD);
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
		registerStationColor(Blocks.STATION_COLOR_SMOOTH_STONE);
		registerStationColor(Blocks.STATION_COLOR_STAINED_GLASS);
		registerStationColor(Blocks.STATION_COLOR_STONE);
		registerStationColor(Blocks.STATION_COLOR_STONE_BRICKS);
		registerStationColor(Blocks.STATION_COLOR_WOOL);
		registerStationColor(Blocks.STATION_NAME_TALL_BLOCK);
		registerStationColor(Blocks.STATION_NAME_TALL_WALL);
		registerStationColor(Blocks.STATION_POLE);

		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_TRAINS, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveTrainsS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_DASHBOARD_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openDashboardScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_PLATFORM_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openPlatformScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_SCHEDULE_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openScheduleScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_ALL, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveAll(packetByteBuf));
	}

	private static void registerStationColor(Block block) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			final int defaultColor = 0x7F7F7F;
			if (pos != null) {
				return ClientData.stations.stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
			} else {
				return defaultColor;
			}
		}, block);
	}

	private static class ModelProvider implements ModelResourceProvider {

		private static final Identifier APG_DOOR_MODEL = new Identifier("mtr:block/apg_door");
		private static final Identifier PSD_DOOR_MODEL = new Identifier("mtr:block/psd_door");
		private static final Identifier PSD_TOP_MODEL = new Identifier("mtr:block/psd_top");

		@Override
		public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
			if (identifier.equals(APG_DOOR_MODEL)) {
				return new APGDoorModel();
			} else if (identifier.equals(PSD_DOOR_MODEL)) {
				return new PSDDoorModel();
			} else if (identifier.equals(PSD_TOP_MODEL)) {
				return new PSDTopModel();
			} else {
				return null;
			}
		}
	}
}
