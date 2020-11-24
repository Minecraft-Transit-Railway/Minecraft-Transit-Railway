package mtr;

import mtr.model.APGDoorModel;
import mtr.model.PSDDoorModel;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLightRail1;
import mtr.render.RenderMTrain;
import mtr.render.RenderMinecart;
import mtr.render.RenderSP1900;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class MTRClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.APG_GLASS_END, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LOGO, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PLATFORM_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PSD_GLASS_END, RenderLayer.getCutout());

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> new ModelProvider());

		EntityRendererRegistry.INSTANCE.register(MTR.MINECART, (dispatcher, context) -> new RenderMinecart(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.SP1900, (dispatcher, context) -> new RenderSP1900(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.M_TRAIN, (dispatcher, context) -> new RenderMTrain(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.LIGHT_RAIL_1, (dispatcher, context) -> new RenderLightRail1(dispatcher));

		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_TRAINS, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveTrainsS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_DASHBOARD_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openDashboardScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_PLATFORM_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openPlatformScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_SCHEDULE_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openScheduleScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_ALL, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveAll(packetByteBuf));
	}

	private static class ModelProvider implements ModelResourceProvider {

		private static final Identifier PSD_DOOR_MODEL = new Identifier("mtr:block/psd_door");
		private static final Identifier APG_DOOR_MODEL = new Identifier("mtr:block/apg_door");

		@Override
		public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
			if (identifier.equals(PSD_DOOR_MODEL)) {
				return new PSDDoorModel();
			} else if (identifier.equals(APG_DOOR_MODEL)) {
				return new APGDoorModel();
			} else {
				return null;
			}
		}
	}
}
