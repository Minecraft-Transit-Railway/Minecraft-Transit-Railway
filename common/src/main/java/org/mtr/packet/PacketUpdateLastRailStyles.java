package org.mtr.mod.packet;

import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.MinecraftClientData;

import java.util.UUID;
import java.util.stream.Collectors;

public final class PacketUpdateLastRailStyles extends PacketHandler {

	private final TransportMode transportMode;
	private final ObjectArrayList<String> styles;

	public static final Cache SERVER_CACHE = new Cache();
	public static final Cache CLIENT_CACHE = new Cache();

	public PacketUpdateLastRailStyles(PacketBufferReceiver packetBufferReceiver) {
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packetBufferReceiver.readString());
		final int count = packetBufferReceiver.readInt();
		styles = new ObjectArrayList<>(count);
		for (int i = 0; i < count; i++) {
			styles.add(packetBufferReceiver.readString());
		}
	}

	public PacketUpdateLastRailStyles(UUID uuid, TransportMode transportMode, ObjectArrayList<String> styles) {
		this.transportMode = transportMode;
		this.styles = styles;
		CLIENT_CACHE.setLastStyles(uuid, transportMode, styles);
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(transportMode.toString());
		packetBufferSender.writeInt(styles.size());
		styles.forEach(packetBufferSender::writeString);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		SERVER_CACHE.setLastStyles(serverPlayerEntity.getUuid(), transportMode, styles);
	}

	public static class Cache {

		private final Object2ObjectAVLTreeMap<UUID, Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<String>>> cache = new Object2ObjectAVLTreeMap<>(); // Cache for last used styles

		/**
		 * Sets the rail style to what was stored in the cache
		 *
		 * @param rail       the rail to change
		 * @param modifyRail whether to actually change the rail styles
		 * @return whether the operation was successful (if the rail originally had different styles)
		 */
		public boolean canApplyStylesToRail(UUID uuid, Rail rail, boolean modifyRail) {
			final ObjectArrayList<String> lastStyles = cache.getOrDefault(uuid, getDefaultStyles()).get(rail.getTransportMode());
			final ObjectImmutableList<String> railStyles = rail.getStyles();
			if (Utilities.sameItems(lastStyles, railStyles)) {
				return false;
			} else {
				if (modifyRail) {
					InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(getRailWithLastStyles(uuid, rail))));
				}
				return true;
			}
		}

		/**
		 * @param rail the base rail
		 * @return a new rail (which is a copy of the supplied rail) with the cached rail styles
		 */
		public Rail getRailWithLastStyles(UUID uuid, Rail rail) {
			return Rail.copy(rail, cache.getOrDefault(uuid, getDefaultStyles()).get(rail.getTransportMode()));
		}

		/**
		 * Saves the rail style that was most recently used
		 */
		private void setLastStyles(UUID uuid, TransportMode transportMode, ObjectList<String> styles) {
			final ObjectArrayList<String> existingStyles = cache.computeIfAbsent(uuid, key -> getDefaultStyles()).get(transportMode);
			existingStyles.clear();
			existingStyles.addAll(styles.stream().distinct().sorted().collect(Collectors.toCollection(ObjectArrayList::new)));
		}

		private static Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<String>> getDefaultStyles() {
			final Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<String>> stylesForTransportMode = new Object2ObjectAVLTreeMap<>();
			for (final TransportMode transportMode : TransportMode.values()) {
				stylesForTransportMode.put(transportMode, transportMode == TransportMode.BOAT ? new ObjectArrayList<>() : ObjectArrayList.of(CustomResourceLoader.DEFAULT_RAIL_ID));
			}
			return stylesForTransportMode;
		}
	}
}
