package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.data.TrainSpawner;
import mtr.gui.DashboardScreen;
import mtr.gui.ScreenBase;
import mtr.gui.TrainSpawnerScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;

import java.util.Set;

public class PacketTrainDataGuiClient implements IPacket {

	public static void openDashboardScreenS2C(PacketByteBuf packet) {
		if (receiveAll(packet)) {
			MinecraftClient.getInstance().openScreen(new DashboardScreen());
		}
	}

	public static void openTrainSpawnerScreenS2C(PacketByteBuf packet) {
		if (receiveAll(packet)) {
			MinecraftClient.getInstance().openScreen(new TrainSpawnerScreen(packet.readBlockPos()));
		}
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

	public static boolean receiveAll(PacketByteBuf packet) {
		final Set<Station> stations = IPacket.receiveData(packet, Station::new);
		final Set<Platform> platforms = IPacket.receiveData(packet, Platform::new);
		final Set<Route> routes = IPacket.receiveData(packet, Route::new);
		final Set<TrainSpawner> trainSpawners = IPacket.receiveData(packet, TrainSpawner::new);

		ScreenBase.GuiBase.stations.clear();
		ScreenBase.GuiBase.stations.addAll(stations);
		ScreenBase.GuiBase.platforms.clear();
		ScreenBase.GuiBase.platforms.addAll(platforms);
		ScreenBase.GuiBase.routes.clear();
		ScreenBase.GuiBase.routes.addAll(routes);
		ScreenBase.GuiBase.trainSpawners.clear();
		ScreenBase.GuiBase.trainSpawners.addAll(trainSpawners);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenBase) {
			((ScreenBase.GuiBase) ((ScreenBase) screen).getDescription()).refreshInterface();
			return false;
		} else {
			return true;
		}
	}
}
