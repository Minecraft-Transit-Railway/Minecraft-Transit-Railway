package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.PlatformScreen;
import mtr.gui.ScheduleScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PacketTrainDataGuiClient implements IPacket {

	public static void openDashboardScreenS2C(PacketByteBuf packet) {
		receiveAll(packet);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (!(minecraftClient.currentScreen instanceof DashboardScreen)) {
			MinecraftClient.getInstance().openScreen(new DashboardScreen());
		}
	}

	public static void openPlatformScreenS2C(PacketByteBuf packet) {
		receiveAll(packet);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (!(minecraftClient.currentScreen instanceof PlatformScreen)) {
			MinecraftClient.getInstance().openScreen(new PlatformScreen(packet.readBlockPos()));
		}
	}

	public static void openScheduleScreenS2C(PacketByteBuf packet) {
		receiveAll(packet);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		if (!(minecraftClient.currentScreen instanceof ScheduleScreen)) {
			MinecraftClient.getInstance().openScreen(new ScheduleScreen(packet.readBlockPos()));
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

	public static void sendPlatformC2S(Platform platform) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		platform.writePacket(packet);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_PLATFORM, packet);
	}

	public static void receiveAll(PacketByteBuf packet) {
		ClientData.stations = IPacket.receiveData(packet, Station::new);
		ClientData.platforms = IPacket.receiveData(packet, Platform::new);
		ClientData.routes = IPacket.receiveData(packet, Route::new);
		ClientData.stationNames = ClientData.stations.stream().collect(Collectors.toMap(station -> station.id, station -> station.name));
		ClientData.platformToRoute = ClientData.platforms.stream().collect(Collectors.toMap(Platform::getPos1, platform -> ClientData.routes.stream().filter(route -> route.platformIds.contains(platform.id)).map(route -> {
			final List<String> stationNames = route.platformIds.stream().map(platformId -> {
				final Station station = RailwayData.getStationByPlatform(ClientData.stations, RailwayData.getDataById(ClientData.platforms, platformId));
				if (station == null) {
					return "";
				} else {
					return station.name;
				}
			}).collect(Collectors.toList());
			return new ClientData.PlatformRouteDetails(route.color, route.platformIds.indexOf(platform.id), stationNames);
		}).collect(Collectors.toList())));
	}
}
