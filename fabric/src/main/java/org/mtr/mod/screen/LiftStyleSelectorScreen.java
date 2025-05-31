package org.mtr.mod.screen;

import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.resource.LiftResource;

import java.util.Objects;

public class LiftStyleSelectorScreen extends DashboardListSelectorScreen {

	private final Lift lift;
	private final ObjectImmutableList<LiftResource> allLifts = CustomResourceLoader.getLifts();

	private LiftStyleSelectorScreen(Lift lift, ObjectImmutableList<DashboardListItem> lifts, LongArrayList selectedLiftIndices, ScreenExtension previousScreenExtension) {
		super(lifts, selectedLiftIndices, true, false, previousScreenExtension);
		this.lift = lift;
	}

	@Override
	public void onClose2() {
		super.onClose2();
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();

		selectedIds.forEach(index -> {
			lift.setStyle(allLifts.get((int) index).getId());
		});

		if (clientPlayerEntity != null) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		}
	}

	@Override
	protected void updateList() {
		super.updateList();
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
	}

	public static LiftStyleSelectorScreen create(Lift lift, ScreenExtension previousScreenExtension) {
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

		return new LiftStyleSelectorScreen(lift, new ObjectImmutableList<>(liftsForList), selectedIds, previousScreenExtension);
	}
}
