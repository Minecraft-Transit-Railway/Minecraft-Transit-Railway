package mtr.packet;

import mtr.MTR;
import mtr.data.DataBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public interface IPacket {

	Identifier ID_OPEN_DASHBOARD_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_dashboard_screen");
	Identifier ID_OPEN_TRAIN_SPAWNER_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_train_spawner_screen");
	Identifier ID_STATIONS_AND_ROUTES = new Identifier(MTR.MOD_ID, "packet_stations_and_routes");
	Identifier ID_TRAIN_SPAWNER = new Identifier(MTR.MOD_ID, "packet_train_spawner");
	Identifier ID_ALL = new Identifier(MTR.MOD_ID, "packet_all");

	static <T extends DataBase> void sendData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	static <T extends DataBase> Set<T> receiveData(PacketByteBuf packet, CreateInstance<T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			objects.add(supplier.create(packet));
		}
		return objects;
	}

	@FunctionalInterface
	interface CreateInstance<T extends DataBase> {
		T create(PacketByteBuf packet);
	}
}
