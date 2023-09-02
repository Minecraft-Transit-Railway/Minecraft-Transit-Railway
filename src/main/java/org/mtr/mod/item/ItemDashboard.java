package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.packet.PacketOpenDashboardScreen;

public class ItemDashboard extends ItemExtension {

	private final TransportMode transportMode;

	public ItemDashboard(TransportMode transportMode, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.transportMode = transportMode;
	}

	@Override
	public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			Registry.sendPacketToClient(ServerPlayerEntity.cast(user), new PacketOpenDashboardScreen(transportMode, false));
		}
	}
}
