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
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class PacketTrainDataGuiClient implements IPacket {

	public static void receiveStationsPlatformsAndRoutesS2C(PacketContext packetContext, PacketByteBuf packet) {
		final Set<Station> stations = IPacket.receiveData(packet, Station::new);
		final Set<Platform> platforms = IPacket.receiveData(packet, Platform::new);
		final Set<Route> routes = IPacket.receiveData(packet, Route::new);
		final boolean openGui = packet.readBoolean();

		ScreenBase.GuiBase.stations.clear();
		ScreenBase.GuiBase.stations.addAll(stations);
		ScreenBase.GuiBase.platforms.clear();
		ScreenBase.GuiBase.platforms.addAll(platforms);
		ScreenBase.GuiBase.routes.clear();
		ScreenBase.GuiBase.routes.addAll(routes);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenBase) {
			((ScreenBase.GuiBase) ((ScreenBase) screen).getDescription()).refreshInterface();
		} else if (openGui) {
			MinecraftClient.getInstance().openScreen(new DashboardScreen());
		}
	}

	public static void receiveStationsAndRoutesS2C(PacketContext packetContext, PacketByteBuf packet) {
		final Set<Station> stations = IPacket.receiveData(packet, Station::new);
		final Set<Route> routes = IPacket.receiveData(packet, Route::new);

		ScreenBase.GuiBase.stations.clear();
		ScreenBase.GuiBase.stations.addAll(stations);
		ScreenBase.GuiBase.routes.clear();
		ScreenBase.GuiBase.routes.addAll(routes);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenBase) {
			((ScreenBase.GuiBase) ((ScreenBase) screen).getDescription()).refreshInterface();
		}
	}

	public static void sendStationsAndRoutesC2S(Set<Station> stations, Set<Route> routes) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, routes);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_STATIONS_AND_ROUTES, packet);
	}

	public static void receiveRoutesAndTrainSpawnerS2C(PacketContext packetContext, PacketByteBuf packet) {
		final Set<Route> routes = IPacket.receiveData(packet, Route::new);
		final Set<TrainSpawner> trainSpawners = IPacket.receiveData(packet, TrainSpawner::new);
		final BlockPos pos = packet.readBlockPos();
		final boolean openGui = packet.readBoolean();

		ScreenBase.GuiBase.routes.clear();
		ScreenBase.GuiBase.routes.addAll(routes);
		ScreenBase.GuiBase.trainSpawners.clear();
		ScreenBase.GuiBase.trainSpawners.addAll(trainSpawners);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenBase) {
			((ScreenBase.GuiBase) ((ScreenBase) screen).getDescription()).refreshInterface();
		} else if (openGui) {
			MinecraftClient.getInstance().openScreen(new TrainSpawnerScreen(pos));
		}
	}

	public static void sendTrainSpawnerC2S(TrainSpawner trainSpawner) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		trainSpawner.writePacket(packet);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_ROUTES_TRAIN_SPAWNERS_AND_POS, packet);
	}
}
