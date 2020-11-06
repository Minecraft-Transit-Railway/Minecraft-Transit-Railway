package mtr;

import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLightRail1;
import mtr.render.RenderMTrain;
import mtr.render.RenderMinecart;
import mtr.render.RenderSP1900;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.RenderLayer;

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

		EntityRendererRegistry.INSTANCE.register(MTR.MINECART, (dispatcher, context) -> new RenderMinecart(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.SP1900, (dispatcher, context) -> new RenderSP1900(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.M_TRAIN, (dispatcher, context) -> new RenderMTrain(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MTR.LIGHT_RAIL_1, (dispatcher, context) -> new RenderLightRail1(dispatcher));

		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_TRAINS, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveTrainsS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_DASHBOARD_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openDashboardScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_OPEN_TRAIN_SPAWNER_SCREEN, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.openTrainSpawnerScreenS2C(packetByteBuf));
		ClientSidePacketRegistry.INSTANCE.register(IPacket.ID_ALL, (packetContext, packetByteBuf) -> PacketTrainDataGuiClient.receiveAll(packetByteBuf));
	}
}
