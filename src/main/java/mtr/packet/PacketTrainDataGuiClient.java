package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.TrainSpawnerScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.Set;

public class PacketTrainDataGuiClient implements IPacket {

	public static void openDashboardScreenS2C(PacketByteBuf packet) {
		receiveAll(packet);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (!(minecraftClient.currentScreen instanceof DashboardScreen)) {
			MinecraftClient.getInstance().openScreen(new DashboardScreen());
		}
	}

	public static void openTrainSpawnerScreenS2C(PacketByteBuf packet) {
		receiveAll(packet);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (!(minecraftClient.currentScreen instanceof TrainSpawnerScreen)) {
			MinecraftClient.getInstance().openScreen(new TrainSpawnerScreen(packet.readBlockPos()));
		}
	}

	public static void receiveTrainsS2C(PacketByteBuf packet) {
		ClientData.trains = IPacket.receiveData(packet, Train::new);
	}

	public static void sendStationsAndRoutesC2S(Set<Station> stations, Set<Route> routes) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, routes);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_STATIONS_AND_ROUTES, packet);
	}

	public static void sendTrainSpawnerC2S(TrainSpawner trainSpawner) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		trainSpawner.writePacket(packet);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_TRAIN_SPAWNER, packet);
	}

	public static void receiveAll(PacketByteBuf packet) {
		ClientData.stations = IPacket.receiveData(packet, Station::new);
		ClientData.platforms = IPacket.receiveData(packet, Platform::new);
		ClientData.routes = IPacket.receiveData(packet, Route::new);
		ClientData.trainSpawners = IPacket.receiveData(packet, TrainSpawner::new);
	}
}
