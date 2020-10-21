package mtr.data;

import mtr.gui.DashboardScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.Set;

public class PacketTrainDataGuiClient extends PacketTrainDataGuiServer {

	public static void receiveS2C(PacketContext packetContext, PacketByteBuf packet) {
		Quadruple<Set<Station>, Set<Platform>, Set<Route>, Set<Train>> data = receive(packet);
		MinecraftClient.getInstance().openScreen(new DashboardScreen(data.t1, data.t2, data.t3, data.t4));
	}

	public static void sendC2S(Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Train> trains) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, send(stations, platforms, routes, trains));
	}
}
