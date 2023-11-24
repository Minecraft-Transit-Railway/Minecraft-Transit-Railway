package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.RailAction;
import org.mtr.mod.screen.DashboardListItem;

public final class PacketBroadcastRailActions extends PacketHandler {

	private final ObjectArrayList<RailAction> railActions;
	private final ObjectArrayList<DashboardListItem> dashboardListItems;

	public PacketBroadcastRailActions(PacketBuffer packetBuffer) {
		railActions = new ObjectArrayList<>();
		dashboardListItems = new ObjectArrayList<>();
		final int actionCount = packetBuffer.readInt();
		for (int i = 0; i < actionCount; i++) {
			dashboardListItems.add(new DashboardListItem(packetBuffer.readLong(), readString(packetBuffer), packetBuffer.readInt()));
		}
	}

	public PacketBroadcastRailActions(ObjectArrayList<RailAction> railActions) {
		this.railActions = railActions;
		dashboardListItems = new ObjectArrayList<>();
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeInt(railActions.size());
		for (final RailAction railAction : railActions) {
			packetBuffer.writeLong(railAction.id);
			writeString(packetBuffer, railAction.getDescription());
			packetBuffer.writeInt(railAction.getColor());
		}
	}

	@Override
	public void runClientQueued() {
		ClientData.getInstance().railActions.clear();
		ClientData.getInstance().railActions.addAll(dashboardListItems);
	}
}
