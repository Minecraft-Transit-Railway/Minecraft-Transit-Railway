package mtr.packet;

import io.netty.buffer.ByteBuf;
import mtr.data.*;
import mtr.gui.*;
import mtr.path.PathData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class PacketTrainDataGuiClient extends PacketTrainDataBase {

	private static final Map<Integer, ByteBuf> TEMP_PACKETS_RECEIVER = new HashMap<>();
	private static long tempPacketId = 0;
	private static int expectedSize = 0;

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

	public static void openTrainAnnouncerScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof TrainAnnouncerScreen)) {
				minecraftClient.openScreen(new TrainAnnouncerScreen(pos));
			}
		});
	}

	public static void openPIDSConfigScreenS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final int maxArrivals = packet.readInt();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.currentScreen instanceof PIDSConfigScreen)) {
				minecraftClient.openScreen(new PIDSConfigScreen(pos1, pos2, maxArrivals));
			}
		});
	}

	public static void announceS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final String message = packet.readString();
		minecraftClient.execute(() -> IDrawing.narrateOrAnnounce(message));
	}

	public static void createRailS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final Rail rail1 = new Rail(packet);
		final Rail rail2 = new Rail(packet);
		final long savedRailId = packet.readLong();
		minecraftClient.execute(() -> {
			RailwayData.addRail(ClientData.RAILS, ClientData.PLATFORMS, ClientData.SIDINGS, pos1, pos2, rail1, 0);
			RailwayData.addRail(ClientData.RAILS, ClientData.PLATFORMS, ClientData.SIDINGS, pos2, pos1, rail2, savedRailId);
		});
	}

	public static void createSignalS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final long id = packet.readLong();
		final DyeColor dyeColor = DyeColor.values()[packet.readInt()];
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		minecraftClient.execute(() -> ClientData.SIGNAL_BLOCKS.add(id, dyeColor, PathData.getRailProduct(pos1, pos2)));
	}

	public static void removeNodeS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> RailwayData.removeNode(null, ClientData.RAILS, pos));
	}

	public static void removeRailConnectionS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		minecraftClient.execute(() -> RailwayData.removeRailConnection(null, ClientData.RAILS, pos1, pos2));
	}

	public static void removeSignalS2C(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final DyeColor dyeColor = DyeColor.values()[packet.readInt()];
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		minecraftClient.execute(() -> ClientData.SIGNAL_BLOCKS.remove(dyeColor, PathData.getRailProduct(pos1, pos2)));
	}

	public static void receiveChunk(MinecraftClient minecraftClient, PacketByteBuf packet) {
		final long id = packet.readLong();
		final int chunk = packet.readInt();
		final boolean complete = packet.readBoolean();

		if (tempPacketId != id) {
			TEMP_PACKETS_RECEIVER.clear();
			tempPacketId = id;
			expectedSize = Integer.MAX_VALUE;
		}

		if (complete) {
			expectedSize = chunk + 1;
		}

		TEMP_PACKETS_RECEIVER.put(chunk, packet.readBytes(packet.readableBytes()));

		if (TEMP_PACKETS_RECEIVER.size() == expectedSize) {
			final PacketByteBuf newPacket = PacketByteBufs.create();
			for (int i = 0; i < expectedSize; i++) {
				newPacket.writeBytes(TEMP_PACKETS_RECEIVER.get(i));
			}
			TEMP_PACKETS_RECEIVER.clear();

			try {
				minecraftClient.execute(() -> ClientData.receivePacket(newPacket));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteS2C(MinecraftClient minecraftClient, PacketByteBuf packet, Set<T> dataSet, Map<Long, T> cacheMap, Function<Long, T> createDataWithId, boolean isDelete) {
		final PacketCallback packetCallback = (updatePacket, fullPacket) -> ClientData.DATA_CACHE.sync();
		if (isDelete) {
			deleteData(dataSet, minecraftClient, packet, packetCallback);
		} else {
			updateData(dataSet, cacheMap, minecraftClient, packet, packetCallback, createDataWithId);
		}
	}

	public static void sendUpdate(Identifier packetId, PacketByteBuf packet) {
		ClientPlayNetworking.send(packetId, packet);
		ClientData.DATA_CACHE.sync();
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
			final Depot depot = ClientData.DATA_CACHE.depotIdMap.get(depotId);
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

	public static void clearTrainsC2S(Collection<Siding> sidings) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(sidings.size());
		sidings.forEach(siding -> packet.writeLong(siding.id));
		ClientPlayNetworking.send(PACKET_CLEAR_TRAINS, packet);
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

	public static void sendTrainAnnouncerMessageC2S(BlockPos pos, String message) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos);
		packet.writeString(message);
		ClientPlayNetworking.send(PACKET_TRAIN_ANNOUNCER, packet);
	}

	public static void sendPIDSConfigC2S(BlockPos pos1, BlockPos pos2, String[] messages) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		packet.writeInt(messages.length);
		for (final String message : messages) {
			packet.writeString(message);
		}
		ClientPlayNetworking.send(PACKET_PIDS_UPDATE, packet);
	}
}
