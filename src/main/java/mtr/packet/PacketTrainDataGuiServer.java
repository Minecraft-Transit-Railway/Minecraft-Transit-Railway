package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.*;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Set;

public class PacketTrainDataGuiServer implements IPacket {

	public static void openDashboardScreenS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<TrainSpawner> trainSpawners) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_OPEN_DASHBOARD_SCREEN, sendAll(stations, platforms, routes, trainSpawners));
	}

	public static void openTrainSpawnerScreenS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<TrainSpawner> trainSpawners, BlockPos trainSpawnerPos) {
		final PacketByteBuf packet = sendAll(stations, platforms, routes, trainSpawners);
		packet.writeBlockPos(trainSpawnerPos);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_OPEN_TRAIN_SPAWNER_SCREEN, packet);
	}

	public static void sendTrainsS2C(WorldAccess world, Set<Train> trains) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, trains);
		world.getPlayers().forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_TRAINS, packet));
	}

	public static void receiveStationsAndRoutesC2S(PacketContext packetContext, PacketByteBuf packet) {
		final World world = packetContext.getPlayer().world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final Set<Station> stations = IPacket.receiveData(packet, Station::new);
			final Set<Route> routes = IPacket.receiveData(packet, Route::new);
			railwayData.setData(stations, routes);
			broadcastS2C(world, railwayData);
		}
	}

	public static void receiveTrainSpawnerC2S(PacketContext packetContext, PacketByteBuf packet) {
		final World world = packetContext.getPlayer().world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final TrainSpawner trainSpawner = new TrainSpawner(packet);
			railwayData.setData(world, trainSpawner);
			broadcastS2C(world, railwayData);
		}
	}

	public static void broadcastS2C(WorldAccess world, RailwayData railwayData) {
		final PacketByteBuf packet = sendAll(railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), railwayData.getTrainSpawners());
		world.getPlayers().forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_ALL, packet));
	}

	private static PacketByteBuf sendAll(Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<TrainSpawner> trainSpawners) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, platforms);
		IPacket.sendData(packet, routes);
		IPacket.sendData(packet, trainSpawners);
		return packet;
	}
}
