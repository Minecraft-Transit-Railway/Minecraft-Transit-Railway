package mtr.packet;

import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.PlatformScreen;
import mtr.gui.ScheduleScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PacketTrainDataGuiClient implements IPacket {

	public static void openDashboardScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		receiveAll(packet);
		if (!(minecraftClient.currentScreen instanceof DashboardScreen)) {
			minecraftClient.openScreen(new DashboardScreen());
		}
	}

	public static void openPlatformScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		receiveAll(packet);
		if (!(minecraftClient.currentScreen instanceof PlatformScreen)) {
			minecraftClient.openScreen(new PlatformScreen(packet.readBlockPos()));
		}
	}

	public static void openScheduleScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		receiveAll(packet);
		if (!(minecraftClient.currentScreen instanceof ScheduleScreen)) {
			minecraftClient.openScreen(new ScheduleScreen(packet.readBlockPos()));
		}
	}

	public static void receiveTrainsS2C(PacketByteBuf packet) {
		ClientData.trains = IPacket.receiveData(packet, Train::new);
	}

	public static void sendStationsAndRoutesC2S(Set<Station> stations, Set<Route> routes) {
		final PacketByteBuf packet = PacketByteBufs.create();
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, routes);
		ClientPlayNetworking.send(ID_STATIONS_AND_ROUTES, packet);
	}

	public static void sendPlatformC2S(Platform platform) {
		final PacketByteBuf packet = PacketByteBufs.create();
		platform.writePacket(packet);
		ClientPlayNetworking.send(ID_PLATFORM, packet);
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
