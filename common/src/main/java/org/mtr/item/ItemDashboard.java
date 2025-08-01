package org.mtr.item;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.mtr.Keys;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.*;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.packet.PacketOpenDashboardScreen;

import javax.annotation.Nullable;
import java.util.Comparator;

public class ItemDashboard extends Item {

	private final TransportMode transportMode;

	private static final int SEARCH_RADIUS = 5;

	public ItemDashboard(TransportMode transportMode, Item.Settings settings) {
		super(settings.maxCount(1));
		this.transportMode = transportMode;
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (Keys.DEBUG && user.isSneaking()) {
			if (world.isClient()) {
				CustomResourceLoader.reload();
			}
		} else {
			if (!world.isClient()) {
				if (user.isSneaking()) {
					final Position playerPosition = MTR.blockPosToPosition(user.getBlockPos());
					MTR.sendMessageC2S(OperationProcessor.GET_DATA, world.getServer(), world, new DataRequest(user.getUuid(), playerPosition, SEARCH_RADIUS), dataResponse -> {
						final ClientData tempClientData = new ClientData();
						new DataResponse(new JsonReader(Utilities.getJsonObjectFromData(dataResponse)), tempClientData).write();
						final Station station = findNearbyArea(playerPosition, tempClientData.stations);
						final Depot depot = findNearbyArea(playerPosition, tempClientData.depots);

						if (station != null) {
							final Platform platform = findNearbySavedRail(playerPosition, station.savedRails, tempClientData);
							if (platform == null) {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode, PacketOpenDashboardScreen.ScreenType.STATION, station.getId());
							} else {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode, PacketOpenDashboardScreen.ScreenType.PLATFORM, platform.getId());
							}
						} else if (depot != null) {
							final Siding siding = findNearbySavedRail(playerPosition, depot.savedRails, tempClientData);
							if (siding == null) {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode, PacketOpenDashboardScreen.ScreenType.DEPOT, depot.getId());
							} else {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode, PacketOpenDashboardScreen.ScreenType.SIDING, siding.getId());
							}
						} else {
							PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode);
						}
					}, DataResponse.class);
				} else {
					PacketOpenDashboardScreen.sendDirectlyToServer((ServerWorld) world, (ServerPlayerEntity) user, transportMode);
				}
			}
		}

		return super.use(world, user, hand);
	}

	@Nullable
	private static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> T findNearbyArea(Position position, ObjectArraySet<T> areas) {
		return areas.stream().filter(area -> area.inArea(position)).findFirst().orElse(null);
	}

	@Nullable
	private static <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> T findNearbySavedRail(Position position, ObjectArraySet<T> rails, Data data) {
		return rails.stream().filter(rail -> rail.closeTo(position, SEARCH_RADIUS)).min(Comparator.comparingDouble(rail -> rail.getApproximateClosestDistance(position, data))).orElse(null);
	}
}
