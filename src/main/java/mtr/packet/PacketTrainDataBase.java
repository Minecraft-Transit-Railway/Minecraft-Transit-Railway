package mtr.packet;

import io.netty.buffer.ByteBuf;
import mtr.MTR;
import mtr.data.NameColorDataBase;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class PacketTrainDataBase {

	public static final Identifier PACKET_OPEN_DASHBOARD_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_dashboard_screen");
	public static final Identifier PACKET_OPEN_RAILWAY_SIGN_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_railway_sign_screen");
	public static final Identifier PACKET_PLATFORM = new Identifier(MTR.MOD_ID, "packet_platform");
	public static final Identifier PACKET_SIGN_TYPES = new Identifier(MTR.MOD_ID, "packet_sign_types");
	public static final Identifier PACKET_CHUNK_S2C = new Identifier(MTR.MOD_ID, "packet_chunk_s2c");
	public static final Identifier PACKET_CHUNK_C2S = new Identifier(MTR.MOD_ID, "packet_chunk_c2s");

	private static final int PACKET_CHUNK_SIZE = (int) Math.pow(2, 14); // 16384
	private static final Map<Long, PacketByteBuf> tempPacketsSender = new HashMap<>();
	private static final Map<Long, PacketByteBuf> tempPacketsReceiver = new HashMap<>();

	public static void receiveChunk(PacketByteBuf packet, PacketCallback sendPacket, PacketCallback packetCallback) {
		final long tempPacketId = packet.readLong();
		final int chunk = packet.readInt();
		final boolean complete = packet.readBoolean();

		if (complete) {
			if (tempPacketsReceiver.containsKey(tempPacketId)) {
				try {
					packetCallback.packetCallback(tempPacketsReceiver.get(tempPacketId));
				} catch (Exception e) {
					e.printStackTrace();
				}
				tempPacketsReceiver.remove(tempPacketId);
			}
		} else {
			final ByteBuf packetChunk = packet.readBytes(packet.readableBytes());
			final PacketByteBuf packetExisting = tempPacketsReceiver.containsKey(tempPacketId) ? tempPacketsReceiver.get(tempPacketId) : PacketByteBufs.create();
			packetExisting.writeBytes(packetChunk);
			tempPacketsReceiver.put(tempPacketId, packetExisting);

			final PacketByteBuf packetResponse = PacketByteBufs.create();
			packetResponse.writeLong(tempPacketId);
			packetResponse.writeInt(chunk + 1);

			try {
				sendPacket.packetCallback(packetResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void handleResponseFromReceiver(PacketByteBuf packet, PacketCallback sendPacket) {
		final long tempPacketId = packet.readLong();
		final int chunk = packet.readInt();
		sendChunk(tempPacketId, chunk, sendPacket);
	}

	public static <T extends NameColorDataBase> Set<T> deserializeData(PacketByteBuf packet, CreateInstance<T> supplier) {
		final Set<T> objects = new HashSet<>();
		final int dataCount = packet.readInt();
		for (int i = 0; i < dataCount; i++) {
			objects.add(supplier.create(packet));
		}
		return objects;
	}

	public static void sendAllInChunks(Set<Station> stations, Set<Platform> platforms, Set<Route> routes, PacketCallback sendPacket) {
		final long tempPacketId = new Random().nextLong();
		tempPacketsSender.put(tempPacketId, PacketTrainDataBase.serializeAll(stations, platforms, routes));
		sendChunk(tempPacketId, 0, sendPacket);
	}

	protected static PacketByteBuf serializeAll(Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		final PacketByteBuf packet = PacketByteBufs.create();
		PacketTrainDataBase.serializeData(packet, stations);
		PacketTrainDataBase.serializeData(packet, platforms);
		PacketTrainDataBase.serializeData(packet, routes);
		return packet;
	}

	private static <T extends NameColorDataBase> void serializeData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	private static void sendChunk(Long tempPacketId, int chunk, PacketCallback sendPacket) {
		final PacketByteBuf packetChunk = PacketByteBufs.create();
		packetChunk.writeLong(tempPacketId);
		packetChunk.writeInt(chunk);

		final PacketByteBuf tempPacket = tempPacketsSender.get(tempPacketId);
		if (chunk * PACKET_CHUNK_SIZE > tempPacket.readableBytes()) {
			tempPacketsSender.remove(tempPacketId);
			packetChunk.writeBoolean(true);
		} else {
			packetChunk.writeBoolean(false);
			packetChunk.writeBytes(tempPacket.copy(chunk * PACKET_CHUNK_SIZE, Math.min(PACKET_CHUNK_SIZE, tempPacket.readableBytes() - chunk * PACKET_CHUNK_SIZE)));
		}

		try {
			sendPacket.packetCallback(packetChunk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FunctionalInterface
	public interface CreateInstance<T extends NameColorDataBase> {
		T create(PacketByteBuf packet);
	}

	@FunctionalInterface
	public interface PacketCallback {
		void packetCallback(PacketByteBuf packet);
	}
}
