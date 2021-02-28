package mtr.packet;

import mtr.MTR;
import mtr.data.NameColorDataBase;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public interface IPacket {

	Identifier ID_OPEN_DASHBOARD_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_dashboard_screen");
	Identifier ID_OPEN_RAILWAY_SIGN_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_railway_sign_screen");
	Identifier ID_PLATFORM = new Identifier(MTR.MOD_ID, "packet_platform");
	Identifier ID_SIGN_TYPES = new Identifier(MTR.MOD_ID, "packet_sign_types");
	Identifier ID_ALL = new Identifier(MTR.MOD_ID, "packet_all");

	static <T extends NameColorDataBase> void sendData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	static <T extends NameColorDataBase> Set<T> receiveData(PacketByteBuf packet, CreateInstance<T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			objects.add(supplier.create(packet));
		}
		return objects;
	}

	static PacketByteBuf sendAll(Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		final PacketByteBuf packet = PacketByteBufs.create();
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, platforms);
		IPacket.sendData(packet, routes);
		return packet;
	}

	@FunctionalInterface
	interface CreateInstance<T extends NameColorDataBase> {
		T create(PacketByteBuf packet);
	}
}
