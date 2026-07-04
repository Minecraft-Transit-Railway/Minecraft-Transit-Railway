package org.mtr.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.mtr.Keys;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.*;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.packet.PacketOpenDashboardScreen;

import java.util.Comparator;

//? if >= 1.21.4 {
import net.minecraft.world.InteractionResult;
//? } else {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
*///? }

public class ItemDashboard extends Item {

	private final TransportMode transportMode;

	private static final int SEARCH_RADIUS = 5;

	public ItemDashboard(TransportMode transportMode, Item.Properties settings) {
		super(settings.stacksTo(1));
		this.transportMode = transportMode;
	}

	@Override
//? if >= 1.21.4 {
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
//? } else {
	/*public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
//
*///? }
		if (Keys.DEBUG && user.isShiftKeyDown()) {
			if (world.isClientSide()) {
				CustomResourceLoader.reload();
			}
		} else {
			if (!world.isClientSide()) {
				if (user.isShiftKeyDown()) {
					final Position playerPosition = MTR.blockPosToPosition(user.blockPosition());
					MTR.sendMessageC2S(OperationProcessor.GET_DATA, world.getServer(), world, new DataRequest(user.getUUID(), playerPosition, SEARCH_RADIUS), dataResponse -> {
						final ClientData tempClientData = new ClientData();
						new DataResponse(new JsonReader(Utilities.getJsonObjectFromData(dataResponse)), tempClientData).write();
						final Station station = findNearbyArea(playerPosition, tempClientData.stations);
						final Depot depot = findNearbyArea(playerPosition, tempClientData.depots);

						if (station != null) {
							final Platform platform = findNearbySavedRail(playerPosition, station.savedRails);
							if (platform == null) {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode, PacketOpenDashboardScreen.ScreenType.STATION, station.getId());
							} else {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode, PacketOpenDashboardScreen.ScreenType.PLATFORM, platform.getId());
							}
						} else if (depot != null) {
							final Siding siding = findNearbySavedRail(playerPosition, depot.savedRails);
							if (siding == null) {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode, PacketOpenDashboardScreen.ScreenType.DEPOT, depot.getId());
							} else {
								PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode, PacketOpenDashboardScreen.ScreenType.SIDING, siding.getId());
							}
						} else {
							PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode);
						}
					}, DataResponse.class);
				} else {
					PacketOpenDashboardScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) user, transportMode);
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
	private static <T extends SavedRailBase<T, U>, U extends AreaBase<U, T>> T findNearbySavedRail(Position position, ObjectArraySet<T> rails) {
		return rails.stream().filter(rail -> rail.closeTo(position, SEARCH_RADIUS)).min(Comparator.comparingDouble(rail -> rail.getApproximateClosestDistance(position))).orElse(null);
	}
}
