package mtr.gui;

import mtr.data.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClientCache extends DataCache {

	public final Map<BlockPos, List<Platform>> posToPlatforms = new HashMap<>();
	public final Map<BlockPos, List<Siding>> posToSidings = new HashMap<>();
	public final Map<Long, Map<Integer, ColorNamePair>> stationIdToRoutes = new HashMap<>();
	public final Map<BlockPos, Long> renderingStateMap = new HashMap<>();

	private final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
	private final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
	private final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();
	private final Map<BlockPos, List<RenderingInstruction>> blockPosToRenderingInstructions = new HashMap<>();
	private final List<BlockPos> blockPosToRenderingInstructionsToClear = new ArrayList<>();

	private static final int RENDER_CACHE_MAX_DISTANCE = 128;

	public ClientCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
		super(stations, platforms, sidings, routes, depots);
	}

	@Override
	protected void syncAdditional() {
		mapPosToSavedRails(posToPlatforms, platforms);
		mapPosToSavedRails(posToSidings, sidings);

		stationIdToRoutes.clear();
		routes.forEach(route -> route.platformIds.forEach(platformId -> {
			final Station station = platformIdToStation.get(platformId);
			if (station != null) {
				if (!stationIdToRoutes.containsKey(station.id)) {
					stationIdToRoutes.put(station.id, new HashMap<>());
				}
				stationIdToRoutes.get(station.id).put(route.color, new ColorNamePair(route.color, route.name.split("\\|\\|")[0]));
			}
		}));

		stationIdToPlatforms.clear();
		depotIdToSidings.clear();
		platformIdToRoutes.clear();
		blockPosToRenderingInstructions.keySet().forEach(pos -> {
			if (!blockPosToRenderingInstructionsToClear.contains(pos)) {
				blockPosToRenderingInstructionsToClear.add(pos);
			}
		});
	}

	public Map<Long, Platform> requestStationIdToPlatforms(long stationId) {
		if (!stationIdToPlatforms.containsKey(stationId)) {
			final Station station = stationIdMap.get(stationId);
			if (station != null) {
				stationIdToPlatforms.put(stationId, areaIdToSavedRails(station, platforms));
			} else {
				stationIdToPlatforms.put(stationId, new HashMap<>());
			}
		}
		return stationIdToPlatforms.get(stationId);
	}

	public Map<Long, Siding> requestDepotIdToSidings(long depotId) {
		if (!depotIdToSidings.containsKey(depotId)) {
			final Depot depot = depotIdMap.get(depotId);
			if (depot != null) {
				depotIdToSidings.put(depotId, areaIdToSavedRails(depot, sidings));
			} else {
				depotIdToSidings.put(depotId, new HashMap<>());
			}
		}
		return depotIdToSidings.get(depotId);
	}

	public List<PlatformRouteDetails> requestPlatformIdToRoutes(long platformId) {
		if (!platformIdToRoutes.containsKey(platformId)) {
			platformIdToRoutes.put(platformId, routes.stream().filter(route -> route.platformIds.contains(platformId)).map(route -> {
				final List<PlatformRouteDetails.StationDetails> stationDetails = route.platformIds.stream().map(platformId2 -> {
					final Station station = platformIdToStation.get(platformId2);
					if (station == null) {
						return new PlatformRouteDetails.StationDetails("", new ArrayList<>());
					} else {
						return new PlatformRouteDetails.StationDetails(station.name, stationIdToRoutes.get(station.id).values().stream().filter(colorNamePair -> colorNamePair.color != route.color).collect(Collectors.toList()));
					}
				}).collect(Collectors.toList());
				return new PlatformRouteDetails(route.name.split("\\|\\|")[0], route.color, route.circularState, route.platformIds.indexOf(platformId), stationDetails);
			}).collect(Collectors.toList()));
		}
		return platformIdToRoutes.get(platformId);
	}

	public void requestRenderForPos(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, BlockPos pos, boolean forceRefresh, Supplier<List<RenderingInstruction>> renderingInstructionsProvider) {
		if (forceRefresh && !blockPosToRenderingInstructionsToClear.contains(pos)) {
			blockPosToRenderingInstructionsToClear.add(pos);
		}

		if (!blockPosToRenderingInstructions.containsKey(pos)) {
			blockPosToRenderingInstructions.put(pos, renderingInstructionsProvider.get());

			final PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				final BlockPos playerPos = player.getBlockPos();
				blockPosToRenderingInstructions.keySet().forEach(checkPos -> {
					if (checkPos.getManhattanDistance(playerPos) > RENDER_CACHE_MAX_DISTANCE) {
						blockPosToRenderingInstructionsToClear.add(checkPos);
					}
				});
			}
		}

		RenderingInstruction.render(matrices, vertexConsumers, immediate, blockPosToRenderingInstructions.get(pos));
	}

	public void clearBlockPosToRenderingInstructionsIfNeeded() {
		if (!blockPosToRenderingInstructionsToClear.isEmpty()) {
			blockPosToRenderingInstructions.remove(blockPosToRenderingInstructionsToClear.remove(0));
		}
	}

	private static <U extends AreaBase, V extends SavedRailBase> Map<Long, V> areaIdToSavedRails(U area, Set<V> savedRails) {
		final Map<Long, V> savedRailMap = new HashMap<>();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos();
			if (area.inArea(pos.getX(), pos.getZ())) {
				savedRailMap.put(savedRail.id, savedRail);
			}
		});
		return savedRailMap;
	}

	private static <U extends SavedRailBase> void mapPosToSavedRails(Map<BlockPos, List<U>> posToSavedRails, Set<U> savedRails) {
		posToSavedRails.clear();
		savedRails.forEach(savedRail -> {
			final BlockPos pos = savedRail.getMidPos(true);
			if (!posToSavedRails.containsKey(pos)) {
				posToSavedRails.put(pos, savedRails.stream().filter(savedRail1 -> savedRail1.getMidPos().getX() == pos.getX() && savedRail1.getMidPos().getZ() == pos.getZ()).sorted(Comparator.comparingInt(savedRail1 -> savedRail1.getMidPos().getY())).collect(Collectors.toList()));
			}
		});
	}

	public static class PlatformRouteDetails {

		public final String routeName;
		public final int routeColor;
		public final Route.CircularState circularState;
		public final int currentStationIndex;
		public final List<StationDetails> stationDetails;

		public PlatformRouteDetails(String routeName, int routeColor, Route.CircularState circularState, int currentStationIndex, List<StationDetails> stationDetails) {
			this.routeName = routeName;
			this.routeColor = routeColor;
			this.circularState = circularState;
			this.currentStationIndex = currentStationIndex;
			this.stationDetails = stationDetails;
		}

		public static class StationDetails {

			public final String stationName;
			public final List<ColorNamePair> interchangeRoutes;

			public StationDetails(String stationName, List<ColorNamePair> interchangeRoutes) {
				this.stationName = stationName;
				this.interchangeRoutes = interchangeRoutes;
			}
		}
	}

	public static class ColorNamePair {

		public final int color;
		public final String name;

		public ColorNamePair(int color, String name) {
			this.color = color;
			this.name = name;
		}
	}
}
