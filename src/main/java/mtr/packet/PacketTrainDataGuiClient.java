package mtr.packet;

import io.netty.buffer.ByteBuf;
import mtr.block.BlockRailwaySign;
import mtr.data.Route;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.RailwaySignScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PacketTrainDataGuiClient extends PacketTrainDataBase {

	private static final Map<Long, PacketByteBuf> TEMP_PACKETS_RECEIVER = new HashMap<>();

	public static void openDashboardScreenS2C(MinecraftClient minecraftClient) {
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof DashboardScreen)) {
				minecraftClient.openScreen(new DashboardScreen());
			}
		});
	}

	public static void openRailwaySignScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof RailwaySignScreen)) {
				minecraftClient.openScreen(new RailwaySignScreen(pos));
			}
		});
	}

	public static void receiveChunk(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final long tempPacketId = packet.readLong();
		final int chunk = packet.readInt();
		final boolean complete = packet.readBoolean();

		if (complete) {
			if (TEMP_PACKETS_RECEIVER.containsKey(tempPacketId)) {
				try {
					minecraftClient.execute(() -> {
						ClientData.receivePacket(TEMP_PACKETS_RECEIVER.get(tempPacketId));
						TEMP_PACKETS_RECEIVER.remove(tempPacketId);
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			final ByteBuf packetChunk = packet.readBytes(packet.readableBytes());
			final PacketByteBuf packetExisting = TEMP_PACKETS_RECEIVER.containsKey(tempPacketId) ? TEMP_PACKETS_RECEIVER.get(tempPacketId) : PacketByteBufs.create();
			packetExisting.writeBytes(packetChunk);
			TEMP_PACKETS_RECEIVER.put(tempPacketId, packetExisting);

			final PacketByteBuf packetResponse = PacketByteBufs.create();
			packetResponse.writeLong(tempPacketId);
			packetResponse.writeInt(chunk + 1);

			try {
				minecraftClient.execute(() -> ClientPlayNetworking.send(PACKET_CHUNK_S2C, packetResponse));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void receiveUpdateOrDeleteStation(MinecraftClient minecraftClient, PacketByteBuf packet, boolean isDelete) {
		if (isDelete) {
			deleteData(ClientData.stations, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences());
		} else {
			updateData(ClientData.stations, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences(), Station::new);
		}
	}

	public static void receiveUpdateOrDeletePlatform(MinecraftClient minecraftClient, PacketByteBuf packet, boolean isDelete) {
		if (isDelete) {
			deleteData(ClientData.platforms, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences());
		} else {
			updateData(ClientData.platforms, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences(), null);
		}
	}

	public static void receiveUpdateOrDeleteRoute(MinecraftClient minecraftClient, PacketByteBuf packet, boolean isDelete) {
		if (isDelete) {
			deleteData(ClientData.routes, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences());
		} else {
			updateData(ClientData.routes, minecraftClient, packet, (updatePacket, fullPacket) -> ClientData.updateReferences(), Route::new);
		}
	}

	public static void sendUpdate(Identifier packetId, PacketByteBuf packet) {
		ClientPlayNetworking.send(packetId, packet);
		ClientData.updateReferences();
	}

	public static void sendDeleteData(Identifier packetId, long id) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		sendUpdate(packetId, packet);
	}

	public static void sendGenerateData(long id) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		ClientPlayNetworking.send(PACKET_GENERATE_ROUTE, packet);
	}

	public static void sendGenerateAllRoutes() {
		ClientPlayNetworking.send(PACKET_GENERATE_ALL_ROUTES, PacketByteBufs.create());
	}

	public static void sendSignTypesC2S(BlockPos signPos, Set<Long> selectedIds, BlockRailwaySign.SignType[] signTypes) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		packet.writeInt(selectedIds.size());
		selectedIds.forEach(packet::writeLong);
		packet.writeInt(signTypes.length);
		for (final BlockRailwaySign.SignType signType : signTypes) {
			packet.writeString(signType == null ? "" : signType.toString());
		}
		ClientPlayNetworking.send(PACKET_SIGN_TYPES, packet);
	}
}
