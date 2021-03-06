package mtr.packet;

import mtr.block.BlockRailwaySign;
import mtr.data.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Set;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	public static void openDashboardScreenS2C(ServerPlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		sendAllInChunks(stations, platforms, routes, packet -> ServerPlayNetworking.send(player, PACKET_CHUNK_S2C, packet));
		final PacketByteBuf packet = PacketByteBufs.create();
		ServerPlayNetworking.send(player, PACKET_OPEN_DASHBOARD_SCREEN, packet);
	}

	public static void openRailwaySignScreenS2C(ServerPlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, BlockPos signPos) {
		sendAllInChunks(stations, platforms, routes, packet -> ServerPlayNetworking.send(player, PACKET_CHUNK_S2C, packet));
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		ServerPlayNetworking.send(player, PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet);
	}

	public static void receiveAllC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final Set<Station> stations = deserializeData(packet, Station::new);
			final Set<Platform> platforms = deserializeData(packet, Platform::new);
			final Set<Route> routes = deserializeData(packet, Route::new);
			minecraftServer.execute(() -> {
				railwayData.setData(world, stations, platforms, routes);
				broadcastS2C(world, railwayData);
			});
		}
	}

	public static void receivePlatformC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final Platform platform = new Platform(packet);
			minecraftServer.execute(() -> {
				railwayData.setData(world, platform);
				broadcastS2C(world, railwayData);
			});
		}
	}

	public static void receiveSignTypesC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final BlockPos signPos = packet.readBlockPos();
		final int platformRouteIndex = packet.readInt();
		final int signLength = packet.readInt();
		final BlockRailwaySign.SignType[] signTypes = new BlockRailwaySign.SignType[signLength];
		for (int i = 0; i < signLength; i++) {
			try {
				signTypes[i] = BlockRailwaySign.SignType.valueOf(packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH));
			} catch (Exception e) {
				signTypes[i] = null;
			}
		}

		minecraftServer.execute(() -> {
			final BlockEntity entity = player.world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				((BlockRailwaySign.TileEntityRailwaySign) entity).setData(platformRouteIndex, signTypes);
			}
		});
	}

	public static void broadcastS2C(WorldAccess world, RailwayData railwayData) {
		final Set<Station> stations = railwayData.getStations();
		final Set<Platform> platforms = railwayData.getPlatforms(world);
		final Set<Route> routes = railwayData.getRoutes();
		world.getPlayers().forEach(player -> sendAllInChunks(stations, platforms, routes, packet -> ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_CHUNK_S2C, packet)));
	}
}
