package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.*;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class PacketTrainDataGuiServer implements IPacket {

	public static void sendStationsPlatformsAndRoutesS2C(PlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, boolean openGui) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, platforms);
		IPacket.sendData(packet, routes);
		packet.writeBoolean(openGui);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_STATIONS_PLATFORMS_AND_ROUTES, packet);
	}

	public static void sendStationsAndRoutesS2C(PlayerEntity player, Set<Station> stations, Set<Route> routes) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, routes);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_STATIONS_AND_ROUTES, packet);
	}

	public static void receiveStationsAndRoutesC2S(PacketContext packetContext, PacketByteBuf packet) {
		final World world = packetContext.getPlayer().world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final Set<Station> stations = IPacket.receiveData(packet, Station::new);
			final Set<Route> routes = IPacket.receiveData(packet, Route::new);
			railwayData.setData(world, stations, routes);
			world.getPlayers().forEach(player -> sendStationsAndRoutesS2C(player, stations, routes));
		}
	}

	public static void sendRoutesTrainSpawnersAndPosS2C(PlayerEntity player, Set<Route> routes, Set<TrainSpawner> trainSpawners, BlockPos pos, boolean openGui) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, routes);
		IPacket.sendData(packet, trainSpawners);
		packet.writeBlockPos(pos);
		packet.writeBoolean(openGui);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID_ROUTES_TRAIN_SPAWNERS_AND_POS, packet);
	}

	public static void receiveTrainSpawnerC2S(PacketContext packetContext, PacketByteBuf packet) {
		final World world = packetContext.getPlayer().world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final TrainSpawner trainSpawner = new TrainSpawner(packet);
			railwayData.setData(world, trainSpawner);
			world.getPlayers().forEach(player -> sendRoutesTrainSpawnersAndPosS2C(player, railwayData.getRoutes(), railwayData.getTrainSpawners(), trainSpawner.pos, false));
		}
	}
}
