package org.mtr.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.client.MinecraftClientData;
import org.mtr.data.RailAction;
import org.mtr.screen.DashboardListItem;

public final class PacketBroadcastRailActions extends PacketHandler {

	private final ObjectArrayList<RailAction> railActions;
	private final ObjectArrayList<DashboardListItem> dashboardListItems;

	public PacketBroadcastRailActions(PacketBufferReceiver packetBufferReceiver) {
		railActions = new ObjectArrayList<>();
		dashboardListItems = new ObjectArrayList<>();
		final int actionCount = packetBufferReceiver.readInt();
		for (int i = 0; i < actionCount; i++) {
			dashboardListItems.add(new DashboardListItem(packetBufferReceiver.readLong(), packetBufferReceiver.readString(), packetBufferReceiver.readInt()));
		}
	}

	public PacketBroadcastRailActions(ObjectArrayList<RailAction> railActions) {
		this.railActions = railActions;
		dashboardListItems = new ObjectArrayList<>();
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeInt(railActions.size());
		for (final RailAction railAction : railActions) {
			packetBufferSender.writeLong(railAction.id);
			packetBufferSender.writeString(railAction.getDescription());
			packetBufferSender.writeInt(railAction.getColor());
		}
	}

	@Override
	public void runClient() {
		MinecraftClientData.getInstance().railActions.clear();
		MinecraftClientData.getInstance().railActions.addAll(dashboardListItems);
	}
}
