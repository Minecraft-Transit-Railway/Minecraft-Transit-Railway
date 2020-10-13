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
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public final class PacketTrainDataGui {

	public static final Identifier ID = new Identifier(MTR.MOD_ID, "train_data_gui");

	public static void sendS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, send(stations, platforms));
	}

	public static void receiveS2C(PacketContext packetContext, PacketByteBuf packet) {
		Pair<Set<Station>, Set<Platform>> data = receive(packet);
		MinecraftClient.getInstance().openScreen(new CottonClientScreen(new GuiDashboard(data.getLeft(), data.getRight())));
	}

	public static void sendC2S(Set<Station> stations, Set<Platform> platforms) {
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, send(stations, platforms));
	}

	public static void receiveC2S(PacketContext packetContext, PacketByteBuf packet) {
		World world = packetContext.getPlayer().world;
		TrainData trainData = TrainData.getInstance(world);
		if (trainData != null) {
			Pair<Set<Station>, Set<Platform>> data = receive(packet);
			trainData.setData(world, data.getLeft(), data.getRight());
		}
	}

	private static PacketByteBuf send(Set<Station> stations, Set<Platform> platforms) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		packet.writeInt(stations.size());
		for (Station station : stations) {
			station.writePacket(packet);
		}
		packet.writeInt(platforms.size());
		for (Platform platform : platforms) {
			platform.writePacket(packet);
		}
		return packet;
	}

	private static Pair<Set<Station>, Set<Platform>> receive(PacketByteBuf packet) {
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
		return new Pair<>(stations, platforms);
	}
}
