package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateLastRailStyles;
import org.mtr.registry.RegistryClient;
import org.mtr.resource.RailResource;

import java.util.stream.Collectors;

public class RailStyleSelectorScreen extends DashboardListSelectorScreen {

	private final Rail rail;
	private final ObjectImmutableList<RailResource> allRails = CustomResourceLoader.getRails();

	private RailStyleSelectorScreen(Rail rail, ObjectImmutableList<DashboardListItem> rails, LongArrayList selectedRailIndices) {
		super(rails, selectedRailIndices, false, false, null);
		this.rail = rail;
	}

	@Override
	public void close() {
		super.close();
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (clientPlayerEntity != null) {
			RegistryClient.sendPacketToServer(new PacketUpdateLastRailStyles(clientPlayerEntity.getUuid(), rail.getTransportMode(), getStyles()));
		}
	}

	@Override
	protected void updateList() {
		super.updateList();
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, getStyles()))));
	}

	public static RailStyleSelectorScreen create(Rail rail) {
		final ObjectImmutableList<RailResource> allRails = CustomResourceLoader.getRails();
		final ObjectArrayList<DashboardListItem> railsForList = new ObjectArrayList<>();
		final LongArrayList selectedIds = new LongArrayList();
		final ObjectArrayList<String> railStylesWithoutDirection = rail.getStyles().stream().map(RailResource::getIdWithoutDirection).collect(Collectors.toCollection(ObjectArrayList::new));
		for (int i = 0; i < allRails.size(); i++) {
			final RailResource railResource = allRails.get(i);
			railsForList.add(new DashboardListItem(i, railResource.getName(), railResource.getColor() | ARGB_BLACK));
			if (railStylesWithoutDirection.contains(railResource.getId())) {
				selectedIds.add(i);
			}
		}
		return new RailStyleSelectorScreen(rail, new ObjectImmutableList<>(railsForList), selectedIds);
	}

	private ObjectArrayList<String> getStyles() {
		final ObjectArrayList<String> styles = new ObjectArrayList<>();
		final Object2BooleanArrayMap<String> existingDirections = new Object2BooleanArrayMap<>();
		rail.getStyles().forEach(style -> {
			if (!style.equals(CustomResourceLoader.DEFAULT_RAIL_ID)) {
				existingDirections.put(RailResource.getIdWithoutDirection(style), style.endsWith("_2"));
			}
		});
		selectedIds.forEach(index -> {
			final String id = allRails.get((int) index).getId();
			styles.add(id.equals(CustomResourceLoader.DEFAULT_RAIL_ID) ? id : id + (existingDirections.getBoolean(id) ? "_2" : "_1"));
		});
		styles.sort(String::compareTo);
		return styles;
	}
}
