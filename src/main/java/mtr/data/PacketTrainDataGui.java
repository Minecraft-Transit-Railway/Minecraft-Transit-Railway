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
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashSet;
import java.util.Set;

public final class PacketTrainDataGui {

	public static final Identifier ID = new Identifier(MTR.MOD_ID, "train_data_gui");

	public static void sendS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, send(stations, platforms, routes));
	}

	public static void receiveS2C(PacketContext packetContext, PacketByteBuf packet) {
		Triple<Set<Station>, Set<Platform>, Set<Route>> data = receive(packet);
		MinecraftClient.getInstance().openScreen(new CottonClientScreen(new GuiDashboard(data.getLeft(), data.getMiddle(), data.getRight())));
	}

	public static void sendC2S(Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, send(stations, platforms, routes));
	}

	public static void receiveC2S(PacketContext packetContext, PacketByteBuf packet) {
		World world = packetContext.getPlayer().world;
		TrainData trainData = TrainData.getInstance(world);
		if (trainData != null) {
			Triple<Set<Station>, Set<Platform>, Set<Route>> data = receive(packet);
			trainData.setData(world, data.getLeft(), data.getMiddle(), data.getRight());
		}
	}

	private static PacketByteBuf send(Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
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
		return packet;
	}

	private static Triple<Set<Station>, Set<Platform>, Set<Route>> receive(PacketByteBuf packet) {
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
		return new ImmutableTriple<>(stations, platforms, routes);
	}
}
