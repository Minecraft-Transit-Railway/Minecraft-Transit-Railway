package mtr.packet;

import mtr.block.BlockRailwaySign;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.RailwaySignScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketTrainDataGuiClient extends PacketTrainDataBase {

	public static void openDashboardScreenS2C(MinecraftClient minecraftClient) {
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof DashboardScreen)) {
				minecraftClient.openScreen(new DashboardScreen());
			}
		});
	}

	public static void openRailwaySignScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof RailwaySignScreen)) {
				minecraftClient.openScreen(new RailwaySignScreen(pos));
			}
		});
	}

	public static void sendAllC2S() {
		sendAllInChunks(ClientData.stations, ClientData.platforms, ClientData.routes, packet -> ClientPlayNetworking.send(PACKET_CHUNK_C2S, packet));
	}

	public static void sendSignTypesC2S(BlockPos signPos, int platformRouteIndex, BlockRailwaySign.SignType[] signTypes) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		packet.writeInt(platformRouteIndex);
		packet.writeInt(signTypes.length);
		for (final BlockRailwaySign.SignType signType : signTypes) {
			packet.writeString(signType == null ? "" : signType.toString());
		}
		ClientPlayNetworking.send(PACKET_SIGN_TYPES, packet);
	}
}
