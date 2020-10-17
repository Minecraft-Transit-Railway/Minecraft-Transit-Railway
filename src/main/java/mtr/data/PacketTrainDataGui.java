package mtr.data;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.netty.buffer.Unpooled;
import mtr.MTR;
import mtr.gui.GuiDashboard;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public final class PacketTrainDataGui {

	public static final Identifier ID = new Identifier(MTR.MOD_ID, "train_data_gui");

	public static void sendS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Train> trains) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, send(stations, platforms, routes, trains));
	}

	public static void receiveS2C(PacketContext packetContext, PacketByteBuf packet) {
		Quadruple<Set<Station>, Set<Platform>, Set<Route>, Set<Train>> data = receive(packet);
		MinecraftClient.getInstance().openScreen(new CottonClientScreen(new GuiDashboard(data.t1, data.t2, data.t3, data.t4)));
	}

	public static void sendC2S(Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Train> trains) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, send(stations, platforms, routes, trains));
	}

	public static void receiveC2S(PacketContext packetContext, PacketByteBuf packet) {
		World world = packetContext.getPlayer().world;
		RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			Quadruple<Set<Station>, Set<Platform>, Set<Route>, Set<Train>> data = receive(packet);
			railwayData.setData(world, data.t1, data.t2, data.t3, data.t4);
		}
	}

	private static PacketByteBuf send(Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Train> trains) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		packet.writeInt(stations.size());
		for (Station station : stations) {
			station.writePacket(packet);
		}
		packet.writeInt(platforms.size());
		for (Platform platform : platforms) {
			platform.writePacket(packet);
		}
		packet.writeInt(routes.size());
		for (Route route : routes) {
			route.writePacket(packet);
		}
		packet.writeInt(trains.size());
		for (Train train : trains) {
			train.writePacket(packet);
		}
		return packet;
	}

	private static Quadruple<Set<Station>, Set<Platform>, Set<Route>, Set<Train>> receive(PacketByteBuf packet) {
		final Set<Station> stations = new HashSet<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stations.add(new Station(packet));
		}
		final Set<Platform> platforms = new HashSet<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platforms.add(new Platform(packet));
		}
		final Set<Route> routes = new HashSet<>();
		final int routeCount = packet.readInt();
		for (int i = 0; i < routeCount; i++) {
			routes.add(new Route(packet));
		}
		final Set<Train> trains = new HashSet<>();
		final int trainCount = packet.readInt();
		for (int i = 0; i < trainCount; i++) {
			trains.add(new Train(packet));
		}
		return new Quadruple<>(stations, platforms, routes, trains);
	}

	private static class Quadruple<T1, T2, T3, T4> {

		public final T1 t1;
		public final T2 t2;
		public final T3 t3;
		public final T4 t4;

		private Quadruple(T1 t1, T2 t2, T3 t3, T4 t4) {
			this.t1 = t1;
			this.t2 = t2;
			this.t3 = t3;
			this.t4 = t4;
		}
	}
}
