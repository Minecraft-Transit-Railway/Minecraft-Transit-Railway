package mtr.packet;

import mtr.MTR;
import mtr.data.DataBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public interface IPacket {

	Identifier ID_STATIONS_AND_ROUTES = new Identifier(MTR.MOD_ID, "train_data_gui_stations_and_routes");
	Identifier ID_STATIONS_PLATFORMS_AND_ROUTES = new Identifier(MTR.MOD_ID, "train_data_gui_stations_platforms_and_routes");
	Identifier ID_ROUTES_TRAIN_SPAWNERS_AND_POS = new Identifier(MTR.MOD_ID, "train_data_gui_routes_and_train_spawner");

	static <T extends DataBase> void sendData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		for (T station : objects) {
			station.writePacket(packet);
		}
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
