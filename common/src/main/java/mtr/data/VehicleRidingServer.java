package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class VehicleRidingServer {

	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public static void mountRider(Level world, Set<UUID> ridingEntities, long id, long routeId, double carX, double carY, double carZ, double length, double width, float carYaw, float carPitch, boolean doorOpen, boolean canMount, int percentageOffset, ResourceLocation packetId, Function<Player, Boolean> canRide, Consumer<Player> ridingCallback) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final double halfLength = length / 2;
		final double halfWidth = width / 2;

		if (canMount) {
			final double margin = halfLength + BOX_PADDING;
			world.getEntitiesOfClass(Player.class, new AABB(carX + margin, carY + margin, carZ + margin, carX - margin, carY - margin, carZ - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUUID()) && railwayData.railwayDataCoolDownModule.canRide(player) && canRide.apply(player)).forEach(player -> {
				final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
				if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 2.5 && Math.abs(positionRotated.z) <= halfLength && !railwayData.railwayDataCoolDownModule.shouldDismount(player)) {
					ridingEntities.add(player.getUUID());
					final float percentageX = (float) (positionRotated.x / width + 0.5);
					final float percentageZ = (float) (length == 0 ? 0 : positionRotated.z / length + 0.5) + percentageOffset;
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
					packet.writeLong(id);
					packet.writeFloat(percentageX);
					packet.writeFloat(percentageZ);
					packet.writeUUID(player.getUUID());
					world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, packetId, packet));
				}
			});
		}

		final Set<UUID> ridersToRemove = new HashSet<>();
		ridingEntities.forEach(uuid -> {
			final Player player = world.getPlayerByUUID(uuid);

			if (player != null) {
				final boolean remove;

				if (player.isSpectator() || railwayData.railwayDataCoolDownModule.shouldDismount(player)) {
					remove = true;
				} else if (doorOpen) {
					final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
					remove = Math.abs(positionRotated.z) <= halfLength && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 10);
				} else {
					remove = false;
				}

				if (remove) {
					ridersToRemove.add(uuid);
				}

				railwayData.railwayDataCoolDownModule.updatePlayerRiding(player, routeId);
				ridingCallback.accept(player);
			}
		});

		if (!ridersToRemove.isEmpty()) {
			ridersToRemove.forEach(ridingEntities::remove);
		}
	}
}
