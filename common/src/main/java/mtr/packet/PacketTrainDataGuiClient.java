package mtr.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mtr.Keys;
import mtr.RegistryClient;
import mtr.block.BlockTrainAnnouncer;
import mtr.block.BlockTrainScheduleSensor;
import mtr.block.BlockTrainSensorBase;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.RegistryUtilities;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;
import java.util.function.BiFunction;

public class PacketTrainDataGuiClient extends PacketTrainDataBase {

	private static final Map<Integer, ByteBuf> TEMP_PACKETS_RECEIVER = new HashMap<>();
	private static long tempPacketId = 0;
	private static int expectedSize = 0;

	public static void openVersionCheckS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final String version = packet.readUtf();
		minecraftClient.execute(() -> {
			if (!Keys.MOD_VERSION.split("-hotfix-")[0].equals(version)) {
				final ClientPacketListener connection = minecraftClient.getConnection();
				if (connection != null) {
					final int widthDifference1 = minecraftClient.font.width(Text.translatable("gui.mtr.mismatched_versions_your_version")) - minecraftClient.font.width(Text.translatable("gui.mtr.mismatched_versions_server_version"));
					final int widthDifference2 = minecraftClient.font.width(Keys.MOD_VERSION) - minecraftClient.font.width(version);
					final int spaceWidth = minecraftClient.font.width(" ");

					final StringBuilder text = new StringBuilder();
					for (int i = 0; i < -widthDifference1 / spaceWidth; i++) {
						text.append(" ");
					}
					text.append(Text.translatable("gui.mtr.mismatched_versions_your_version", Keys.MOD_VERSION).getString());
					for (int i = 0; i < -widthDifference2 / spaceWidth; i++) {
						text.append(" ");
					}
					text.append("\n");
					for (int i = 0; i < widthDifference1 / spaceWidth; i++) {
						text.append(" ");
					}
					text.append(Text.translatable("gui.mtr.mismatched_versions_server_version", version).getString());
					for (int i = 0; i < widthDifference2 / spaceWidth; i++) {
						text.append(" ");
					}
					text.append("\n\n");

					connection.getConnection().disconnect(Text.literal(text.toString()).append(Text.translatable("gui.mtr.mismatched_versions")));
				}
			}
		});
	}

	public static void openDashboardScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final TransportMode transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packet.readUtf());
		final boolean useTimeAndWindSync = packet.readBoolean();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof DashboardScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new DashboardScreen(transportMode, useTimeAndWindSync));
			}
		});
	}

	public static void openRailwaySignScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof RailwaySignScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new RailwaySignScreen(pos));
			}
		});
	}

	public static void openTrainSensorScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (minecraftClient.level != null && !(minecraftClient.screen instanceof TrainSensorScreenBase)) {
				final BlockEntity entity = minecraftClient.level.getBlockEntity(pos);
				if (entity instanceof BlockTrainAnnouncer.TileEntityTrainAnnouncer) {
					UtilitiesClient.setScreen(minecraftClient, new TrainAnnouncerScreen(pos));
				} else if (entity instanceof BlockTrainScheduleSensor.TileEntityTrainScheduleSensor) {
					UtilitiesClient.setScreen(minecraftClient, new TrainScheduleSensorScreen(pos));
				} else if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
					UtilitiesClient.setScreen(minecraftClient, new TrainBasicSensorScreen(pos));
				}
			}
		});
	}

	public static void openLiftTrackFloorS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof LiftTrackFloorScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new LiftTrackFloorScreen(pos));
			}
		});
	}

	public static void openLiftCustomizationS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final long id = packet.readLong();
		minecraftClient.execute(() -> {
			final LiftClient lift = ClientData.DATA_CACHE.liftsClientIdMap.get(id);
			if (!(minecraftClient.screen instanceof LiftCustomizationScreen) && lift != null) {
				UtilitiesClient.setScreen(minecraftClient, new LiftCustomizationScreen(lift));
			}
		});
	}

	public static void openTicketMachineScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final int balance = packet.readInt();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof TicketMachineScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new TicketMachineScreen(balance));
			}
		});
	}

	public static void openPIDSConfigScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final int maxArrivals = packet.readInt();
		final int linesPerArrival = packet.readInt();
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof PIDSConfigScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new PIDSConfigScreen(pos1, pos2, maxArrivals, linesPerArrival));
			}
		});
	}

	public static void openArrivalProjectorConfigScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();

		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof ArrivalProjectorConfigScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new ArrivalProjectorConfigScreen(pos));
			}
		});
	}

	public static void openResourcePackCreatorScreen(Minecraft minecraftClient) {
		minecraftClient.execute(() -> {
			if (!(minecraftClient.screen instanceof ResourcePackCreatorScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new ResourcePackCreatorScreen());
			}
		});
	}

	public static void announceS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final String message = packet.readUtf();
		final String soundIdString = packet.readUtf();
		minecraftClient.execute(() -> {
			IDrawing.narrateOrAnnounce(message);
			final ClientLevel world = minecraftClient.level;
			final LocalPlayer player = minecraftClient.player;
			if (!soundIdString.isEmpty() && world != null && player != null) {
				world.playLocalSound(player.blockPosition(), RegistryUtilities.createSoundEvent(new ResourceLocation(soundIdString)), SoundSource.BLOCKS, 1000000, 1, false);
			}
		});
	}

	public static void createRailS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final TransportMode transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packet.readUtf());
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final Rail rail1 = new Rail(packet);
		final Rail rail2 = new Rail(packet);
		final long savedRailId = packet.readLong();
		minecraftClient.execute(() -> {
			RailwayData.addRail(ClientData.RAILS, ClientData.PLATFORMS, ClientData.SIDINGS, transportMode, pos1, pos2, rail1, 0);
			RailwayData.addRail(ClientData.RAILS, ClientData.PLATFORMS, ClientData.SIDINGS, transportMode, pos2, pos1, rail2, savedRailId);
		});
	}

	public static void createSignalS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final long id = packet.readLong();
		final DyeColor dyeColor = DyeColor.values()[packet.readInt()];
		final UUID rail = packet.readUUID();
		minecraftClient.execute(() -> ClientData.SIGNAL_BLOCKS.add(id, dyeColor, rail));
	}

	public static void removeNodeS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> RailwayData.removeNode(null, ClientData.RAILS, pos));
	}

	public static void removeLiftFloorTrackS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		minecraftClient.execute(() -> RailwayData.removeLiftFloorTrack(null, ClientData.LIFTS, pos));
	}

	public static void removeRailConnectionS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		minecraftClient.execute(() -> RailwayData.removeRailConnection(null, ClientData.RAILS, pos1, pos2));
	}

	public static void removeSignalsS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
		final long removeCount = packet.readInt();
		final List<Long> ids = new ArrayList<>();
		final List<DyeColor> colors = new ArrayList<>();
		final List<UUID> rails = new ArrayList<>();
		for (int i = 0; i < removeCount; i++) {
			ids.add(packet.readLong());
			colors.add(DyeColor.values()[packet.readInt()]);
			rails.add(packet.readUUID());
		}
		minecraftClient.execute(() -> {
			for (int i = 0; i < removeCount; i++) {
				ClientData.SIGNAL_BLOCKS.remove(ids.get(i), colors.get(i), rails.get(i));
			}
		});
	}

	public static void receiveChunk(Minecraft minecraftClient, FriendlyByteBuf packet) {
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
			final FriendlyByteBuf newPacket = new FriendlyByteBuf(Unpooled.buffer());
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

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteS2C(Minecraft minecraftClient, FriendlyByteBuf packet, Set<T> dataSet, Map<Long, T> cacheMap, BiFunction<Long, TransportMode, T> createDataWithId, boolean isDelete) {
		final PacketCallback packetCallback = (updatePacket, fullPacket) -> {
			ClientData.DATA_CACHE.sync();
			ClientData.DATA_CACHE.refreshDynamicResources();
		};
		if (isDelete) {
			deleteData(dataSet, cacheMap, minecraftClient, packet, packetCallback, null);
		} else {
			updateData(dataSet, cacheMap, minecraftClient, packet, packetCallback, createDataWithId, null);
		}
	}

	public static void sendUpdate(ResourceLocation packetId, FriendlyByteBuf packet) {
		RegistryClient.sendToServer(packetId, packet);
		ClientData.DATA_CACHE.sync();
		ClientData.DATA_CACHE.refreshDynamicResources();
	}

	public static void sendDeleteData(ResourceLocation packetId, long id) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		sendUpdate(packetId, packet);
	}

	public static void sendTrainSensorC2S(BlockPos pos, Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		packet.writeInt(filterRouteIds.size());
		filterRouteIds.forEach(packet::writeLong);
		packet.writeBoolean(stoppedOnly);
		packet.writeBoolean(movingOnly);
		packet.writeInt(number);
		packet.writeInt(strings.length);
		for (final String string : strings) {
			packet.writeUtf(string);
		}
		RegistryClient.sendToServer(PACKET_UPDATE_TRAIN_SENSOR, packet);
	}

	public static void sendLiftTrackFloorC2S(BlockPos pos, String floorNumber, String floorDescription, boolean shouldDing) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		packet.writeUtf(floorNumber);
		packet.writeUtf(floorDescription);
		packet.writeBoolean(shouldDing);
		RegistryClient.sendToServer(PACKET_UPDATE_LIFT_TRACK_FLOOR, packet);
	}

	public static void generatePathS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
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
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(sidingId);
		RegistryClient.sendToServer(PACKET_GENERATE_PATH, packet);
	}

	public static void clearTrainsC2S(long depotId, Collection<Siding> sidings) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(depotId);
		packet.writeInt(sidings.size());
		sidings.forEach(siding -> packet.writeLong(siding.id));
		RegistryClient.sendToServer(PACKET_CLEAR_TRAINS, packet);
	}

	public static void sendUpdateEntitySeatPassengerPosition(double x, double y, double z) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeDouble(x);
		packet.writeDouble(y);
		packet.writeDouble(z);
		RegistryClient.sendToServer(PACKET_UPDATE_ENTITY_SEAT_POSITION, packet);
	}

	public static void sendSignIdsC2S(BlockPos signPos, Set<Long> selectedIds, String[] signIds) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(signPos);
		packet.writeInt(selectedIds.size());
		selectedIds.forEach(packet::writeLong);
		packet.writeInt(signIds.length);
		for (final String signType : signIds) {
			packet.writeUtf(signType == null ? "" : signType);
		}
		RegistryClient.sendToServer(PACKET_SIGN_TYPES, packet);
	}

	public static void sendDriveTrainC2S(boolean pressingAccelerate, boolean pressingBrake, boolean pressingDoors) {
		if (Train.isHoldingKey(Minecraft.getInstance().player) && (pressingAccelerate || pressingBrake || pressingDoors)) {
			final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			packet.writeBoolean(pressingAccelerate);
			packet.writeBoolean(pressingBrake);
			packet.writeBoolean(pressingDoors);
			RegistryClient.sendToServer(PACKET_DRIVE_TRAIN, packet);
		}
	}

	public static void sendPressLiftButtonC2S(long id, int floor) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeInt(floor);
		RegistryClient.sendToServer(PACKET_PRESS_LIFT_BUTTON, packet);
	}

	public static void addBalanceC2S(int addAmount, int emeralds) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeInt(addAmount);
		packet.writeInt(emeralds);
		RegistryClient.sendToServer(PACKET_ADD_BALANCE, packet);
	}

	public static void sendPIDSConfigC2S(BlockPos pos1, BlockPos pos2, String[] messages, boolean[] hideArrival, Set<Long> filterPlatformIds, int displayPage) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		packet.writeInt(messages.length);
		for (String message : messages) {
			packet.writeUtf(message);
		}
		packet.writeInt(hideArrival.length);
		for (boolean b : hideArrival) {
			packet.writeBoolean(b);
		}
		packet.writeInt(filterPlatformIds.size());
		filterPlatformIds.forEach(packet::writeLong);
		packet.writeInt(displayPage);
		RegistryClient.sendToServer(PACKET_PIDS_UPDATE, packet);
	}

	public static void sendArrivalProjectorConfigC2S(BlockPos pos, Set<Long> filterPlatformIds, int displayPage) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		packet.writeInt(filterPlatformIds.size());
		filterPlatformIds.forEach(packet::writeLong);
		packet.writeInt(displayPage);
		RegistryClient.sendToServer(PACKET_ARRIVAL_PROJECTOR_UPDATE, packet);
	}

	public static void sendUseTimeAndWindSyncC2S(boolean useTimeAndWindSync) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBoolean(useTimeAndWindSync);
		RegistryClient.sendToServer(PACKET_USE_TIME_AND_WIND_SYNC, packet);
	}
}
