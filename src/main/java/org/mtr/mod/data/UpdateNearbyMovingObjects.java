package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class UpdateNearbyMovingObjects<T extends NameColorDataBase> implements IPacket {

	public final Map<Player, Set<T>> newDataSetInPlayerRange = new HashMap<>();
	public final Set<T> dataSetToSync = new HashSet<>();
	private final Map<Player, Set<T>> dataSetInPlayerRange = new HashMap<>();
	private final ResourceLocation deletePacketId;
	private final ResourceLocation updatePacketId;

	public UpdateNearbyMovingObjects(ResourceLocation deletePacketId, ResourceLocation updatePacketId) {
		this.deletePacketId = deletePacketId;
		this.updatePacketId = updatePacketId;
	}

	public void startTick() {
		newDataSetInPlayerRange.clear();
		dataSetToSync.clear();
	}

	public void tick() {
		dataSetInPlayerRange.forEach((player, dataSet) -> {
			for (final T data : dataSet) {
				if (!newDataSetInPlayerRange.containsKey(player) || !newDataSetInPlayerRange.get(player).contains(data)) {
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

					if (newDataSetInPlayerRange.containsKey(player)) {
						packet.writeInt(newDataSetInPlayerRange.get(player).size());
						newDataSetInPlayerRange.get(player).forEach(dataToKeep -> packet.writeLong(dataToKeep.id));
					} else {
						packet.writeInt(0);
					}

					if (packet.readableBytes() <= MAX_PACKET_BYTES) {
						Registry.sendToPlayer((ServerPlayer) player, deletePacketId, packet);
					}

					break;
				}
			}
		});

		newDataSetInPlayerRange.forEach((player, dataSet) -> {
			final List<FriendlyByteBuf> dataSetPacketsToUpdate = new ArrayList<>();
			dataSet.forEach(data -> {
				if (dataSetToSync.contains(data) || !dataSetInPlayerRange.containsKey(player) || !dataSetInPlayerRange.get(player).contains(data)) {
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
					data.writePacket(packet);
					if (packet.readableBytes() < MAX_PACKET_BYTES) {
						dataSetPacketsToUpdate.add(packet);
					}
				}
			});

			while (!dataSetPacketsToUpdate.isEmpty()) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

				while (!dataSetPacketsToUpdate.isEmpty()) {
					final FriendlyByteBuf dataPacket = dataSetPacketsToUpdate.get(0);
					if (packet.readableBytes() + dataPacket.readableBytes() < MAX_PACKET_BYTES) {
						packet.writeBytes(dataPacket);
						dataSetPacketsToUpdate.remove(0);
					} else {
						break;
					}
				}

				Registry.sendToPlayer((ServerPlayer) player, updatePacketId, packet);
			}
		});

		dataSetInPlayerRange.clear();
		dataSetInPlayerRange.putAll(newDataSetInPlayerRange);
	}
}
