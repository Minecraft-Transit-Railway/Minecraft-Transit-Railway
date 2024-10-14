package org.mtr.mod.item;

import org.mtr.core.data.Position;
import org.mtr.core.data.SavedRailBase;
import org.mtr.core.data.AreaBase;
import org.mtr.core.data.Station;
import org.mtr.core.data.Depot;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.Data;
import org.mtr.core.data.ClientData;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.Keys;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.packet.PacketOpenDashboardScreen;

import java.util.Comparator;
import java.util.Optional;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;

public class ItemDashboard extends ItemExtension {

	private final TransportMode transportMode;

	public ItemDashboard(TransportMode transportMode, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.transportMode = transportMode;
	}

	private <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> Optional<T> findNearbyArea(Position position, ObjectArraySet<T> areas){
		return areas.stream().filter(area -> area.inArea(position)).findFirst();
	}

	private <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> Optional<T> findNearbyRail(Position position, ObjectArraySet<T> rails, Data data){
		return rails.stream().filter(rail -> rail.closeTo(position, 5)).min(Comparator.comparingDouble(rail -> rail.getApproximateClosestDistance(position, data)));
	}

	@Override
	public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
		if (Keys.DEBUG && user.isSneaking()) {
			if (world.isClient()) {
				CustomResourceLoader.reload();
			}
		} else {
			if (!world.isClient()) {
				Position playerPos = new Position((long) user.getX(), (long) user.getY(), (long) user.getZ());
				String request = Utilities.getJsonObjectFromData(new DataRequest(user.getUuidAsString(), playerPos, 5)).toString();

				Init.sendHttpRequest("operation/get-data", world, request, content -> {
					final ClientData tempClientData = new ClientData();
					final MinecraftServer minecraftServer = world.getServer();

					if (minecraftServer != null) {
						minecraftServer.execute(() -> {
							Response.create(Utilities.parseJson(content)).getData(jsonReader -> new DataResponse(jsonReader, tempClientData)).write();

							Optional<Station> station = findNearbyArea(playerPos, tempClientData.stations);
							if(station.isPresent()){
								Optional<Platform> platform = findNearbyRail(playerPos, station.get().savedRails, tempClientData);

								if(platform.isPresent()){
									PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode, PacketOpenDashboardScreen.ScreenType.PLATFORM, platform.get().getId());
								}else{
									PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode, PacketOpenDashboardScreen.ScreenType.STATION, station.get().getId());
								}
							}else{
								Optional<Depot> depot = findNearbyArea(playerPos, tempClientData.depots);
								if(depot.isPresent()){
									Optional<Siding> siding = findNearbyRail(playerPos, depot.get().savedRails, tempClientData);

									if(siding.isPresent()){
										PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode, PacketOpenDashboardScreen.ScreenType.SIDING, siding.get().getId());
									}else{
										PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode, PacketOpenDashboardScreen.ScreenType.DEPOT, depot.get().getId());
									}
								}else{
									PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode);
								}
							}
						});
					}
				});
			}
		}
	}
}
