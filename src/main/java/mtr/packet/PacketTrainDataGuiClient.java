package mtr.packet;

import io.netty.buffer.ByteBuf;
import mtr.data.Depot;
import mtr.data.NameColorDataBase;
import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.gui.ClientData;
import mtr.gui.DashboardScreen;
import mtr.gui.RailwaySignScreen;
import mtr.gui.TicketMachineScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

	public static void openTicketMachineScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final int balance = packet.readInt();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof TicketMachineScreen)) {
				minecraftClient.openScreen(new TicketMachineScreen(balance));
			}
		});
	}

	public static void createRailS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final Rail rail1 = new Rail(packet);
		final Rail rail2 = new Rail(packet);
		final long savedRailId = packet.readLong();
		minecraftClient.execute(() -> {
			RailwayData.addRail(ClientData.rails, ClientData.platforms, ClientData.sidings, pos1, pos2, rail1, 0);
			RailwayData.addRail(ClientData.rails, ClientData.platforms, ClientData.sidings, pos2, pos1, rail2, savedRailId);
			RailwayData.validateData(ClientData.rails, ClientData.platforms, ClientData.sidings, ClientData.routes);
		});
	}

	public static void removeNodeS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			RailwayData.removeNode(null, ClientData.rails, pos);
			RailwayData.validateData(ClientData.rails, ClientData.platforms, ClientData.sidings, ClientData.routes);
		});
	}

	public static void removeRailConnectionS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		minecraftClient.execute(() -> {
			RailwayData.removeRailConnection(null, ClientData.rails, pos1, pos2);
			RailwayData.validateData(ClientData.rails, ClientData.platforms, ClientData.sidings, ClientData.routes);
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
				minecraftClient.execute(() -> {
					ClientPlayNetworking.send(PACKET_CHUNK_S2C, packetResponse);
					if (minecraftClient.player != null) {
						minecraftClient.player.sendMessage(new TranslatableText("gui.mtr.railway_loading"), true);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteS2C(MinecraftClient minecraftClient, PacketByteBuf packet, Set<T> dataSet, Function<Long, T> createDataWithId, boolean isDelete) {
		if (isDelete) {
			deleteData(dataSet, minecraftClient, packet, (updatePacket, fullPacket) -> {
			});
		} else {
			updateData(dataSet, minecraftClient, packet, (updatePacket, fullPacket) -> {
			}, createDataWithId);
		}
	}

	public static void sendUpdate(Identifier packetId, PacketByteBuf packet) {
		ClientPlayNetworking.send(packetId, packet);
	}

	public static void sendDeleteData(Identifier packetId, long id) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		sendUpdate(packetId, packet);
	}

	public static void generatePathS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final long depotId = packet.readLong();
		final int successfulSegments = packet.readInt();
		minecraftClient.execute(() -> {
			final Depot depot = RailwayData.getDataById(ClientData.depots, depotId);
			if (depot != null) {
				depot.clientPathGenerationSuccessfulSegments = successfulSegments;
			}
		});
	}

	public static void generatePathC2S(long sidingId) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(sidingId);
		ClientPlayNetworking.send(PACKET_GENERATE_PATH, packet);
	}

	public static void sendSignIdsC2S(BlockPos signPos, Set<Long> selectedIds, String[] signIds) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		packet.writeInt(selectedIds.size());
		selectedIds.forEach(packet::writeLong);
		packet.writeInt(signIds.length);
		for (final String signType : signIds) {
			packet.writeString(signType == null ? "" : signType);
		}
		ClientPlayNetworking.send(PACKET_SIGN_TYPES, packet);
	}

	public static void addBalanceC2S(int addAmount, int emeralds) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(addAmount);
		packet.writeInt(emeralds);
		ClientPlayNetworking.send(PACKET_ADD_BALANCE, packet);
	}
}
