package mtr.data;

import mtr.packet.PacketTrainDataBase;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RailwayData extends PersistentState {

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_ROUTES = "routes";

	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Route> routes;

	private final List<Route> scheduleGenerate = new ArrayList<>();
	private final List<PlayerEntity> scheduleBroadcast = new ArrayList<>();

	private static final int BROADCAST_DELAY = 2;

	public RailwayData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
		routes = new HashSet<>();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		try {
			final CompoundTag tagStations = tag.getCompound(KEY_STATIONS);
			for (String key : tagStations.getKeys()) {
				stations.add(new Station(tagStations.getCompound(key)));
			}

			final CompoundTag tagNewPlatforms = tag.getCompound(KEY_PLATFORMS);
			for (String key : tagNewPlatforms.getKeys()) {
				platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
			}

			final CompoundTag tagNewRoutes = tag.getCompound(KEY_ROUTES);
			for (String key : tagNewRoutes.getKeys()) {
				routes.add(new Route(tagNewRoutes.getCompound(key)));
			}

			validateData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		try {
			validateData();
			final CompoundTag tagStations = new CompoundTag();
			int i = 0;
			for (Station station : stations) {
				tagStations.put(KEY_STATIONS + i, station.toCompoundTag());
				i++;
			}
			tag.put(KEY_STATIONS, tagStations);

			final CompoundTag tagNewPlatforms = new CompoundTag();
			int j = 0;
			for (Platform platform : platforms) {
				tagNewPlatforms.put(KEY_PLATFORMS + j, platform.toCompoundTag());
				j++;
			}
			tag.put(KEY_PLATFORMS, tagNewPlatforms);

			final CompoundTag tagNewRoutes = new CompoundTag();
			int k = 0;
			for (Route route : routes) {
				tagNewRoutes.put(KEY_ROUTES + k, route.toCompoundTag());
				k++;
			}
			tag.put(KEY_ROUTES, tagNewRoutes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tag;
	}

	public Set<Station> getStations() {
		return stations;
	}

	public Set<Platform> getPlatforms(WorldAccess world) {
		try {
			validatePlatforms(world);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return platforms;
	}

	public Set<Route> getRoutes() {
		try {
			validateData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routes;
	}

	public void simulateTrains(WorldAccess world) {
		final long lunarTime = world.getLunarTime();
		routes.forEach(route -> route.getPositionYaw(world, lunarTime));

		if (!scheduleGenerate.isEmpty() && lunarTime % BROADCAST_DELAY == 0) {
			final Route route = scheduleGenerate.remove(0);
			route.generateGraph(world, platforms);
			System.out.println("Generated route " + route.name);

			if (scheduleGenerate.isEmpty()) {
				scheduleBroadcast.addAll(world.getPlayers());
			}
		}

		if (!scheduleBroadcast.isEmpty() && (lunarTime + BROADCAST_DELAY / 2) % BROADCAST_DELAY == 0) {
			final PlayerEntity player = scheduleBroadcast.remove(0);
			PacketTrainDataGuiServer.sendAllInChunks(stations, platforms, routes, packet -> ServerPlayNetworking.send((ServerPlayerEntity) player, PacketTrainDataBase.PACKET_CHUNK_S2C, packet));
			System.out.println("Broadcasting to player " + player);
		}
	}

	public void addPlayerToBroadcast(PlayerEntity player) {
		scheduleBroadcast.add(player);
	}

	// writing data

	public void setData(WorldAccess world, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		try {
			this.stations.clear();
			this.stations.addAll(stations);
			this.platforms.clear();
			this.platforms.addAll(platforms);
			this.routes.clear();
			this.routes.addAll(routes);
			validatePlatforms(world);
			scheduleGenerate.clear();
			scheduleGenerate.addAll(this.routes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setData(WorldAccess world, Platform newPlatform) {
		try {
			platforms.removeIf(platform -> platform.isOverlapping(newPlatform));
			platforms.add(newPlatform);
			validatePlatforms(world);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// validation

	private void validatePlatforms(WorldAccess world) {
		platforms.removeIf(platform -> !platform.isValidPlatform(world));
		validateData();
	}

	private void validateData() {
		routes.forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
		markDirty();
	}

	// static finders

	public static <T extends NameColorDataBase> T getDataById(Set<T> data, long id) {
		try {
			return data.stream().filter(item -> item.id == id).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		try {
			return platforms.stream().filter(platform -> platform.containsPos(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Station getStationByPlatform(Set<Station> stations, Platform platform) {
		try {
			final BlockPos pos = platform.getMidPos();
			return stations.stream().filter(station -> station.inStation(pos.getX(), pos.getZ())).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// other

	public static boolean isBetween(double value, double value1, double value2) {
		return value >= Math.min(value1, value2) && value <= Math.max(value1, value2);
	}

	public static RailwayData getInstance(World world) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(RailwayData::new, NAME);
		} else {
			return null;
		}
	}
}
