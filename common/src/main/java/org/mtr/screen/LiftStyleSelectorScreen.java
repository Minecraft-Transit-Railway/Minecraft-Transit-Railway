package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.resource.LiftResource;

import java.util.Objects;

public class LiftStyleSelectorScreen extends DashboardListSelectorScreen {

	private final Lift lift;
	private final ObjectImmutableList<LiftResource> allLifts = CustomResourceLoader.getLifts();

	private LiftStyleSelectorScreen(Lift lift, ObjectImmutableList<DashboardListItem> lifts, LongArrayList selectedLiftIndices, Screen previousScreen) {
		super(lifts, selectedLiftIndices, true, false, previousScreen);
		this.lift = lift;
	}

	@Override
	public void close() {
		super.close();
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

		selectedIds.forEach(index -> {
			lift.setStyle(allLifts.get((int) index).getId());
		});

		if (clientPlayerEntity != null) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		}
	}

	@Override
	protected void updateList() {
		super.updateList();
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
	}

	public static LiftStyleSelectorScreen create(Lift lift, Screen previousScreen) {
		final ObjectImmutableList<LiftResource> allLifts = CustomResourceLoader.getLifts();
		final ObjectArrayList<DashboardListItem> liftsForList = new ObjectArrayList<>();
		final LongArrayList selectedIds = new LongArrayList();

		for (int i = 0; i < allLifts.size(); i++) {
			final LiftResource liftResource = allLifts.get(i);
			liftsForList.add(new DashboardListItem(i, liftResource.getName(), liftResource.getColor() | ARGB_BLACK));
			if (Objects.equals(liftResource.getId(), lift.getStyle())) {
				selectedIds.add(i);
			}
		}

		return new LiftStyleSelectorScreen(lift, new ObjectImmutableList<>(liftsForList), selectedIds, previousScreen);
	}
}
