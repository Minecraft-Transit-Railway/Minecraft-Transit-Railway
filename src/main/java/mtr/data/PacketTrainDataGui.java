package mtr.data;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.netty.buffer.Unpooled;
import mtr.MTR;
import mtr.gui.GuiDashboard;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public final class PacketTrainDataGui {

	public static final Identifier ID = new Identifier(MTR.MOD_ID, "train_data_gui");

	public static void sendS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		packet.writeInt(stations.size());
		for (Station station : stations) {
			station.writePacket(packet);
		}
		packet.writeInt(platforms.size());
		for (Platform platform : platforms) {
			platform.writePacket(packet);
		}
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, packet);
	}

	public static void receiveS2C(PacketContext packetContext, PacketByteBuf packet) {
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
		MinecraftClient.getInstance().openScreen(new CottonClientScreen(new GuiDashboard(stations, platforms)));
	}
}
