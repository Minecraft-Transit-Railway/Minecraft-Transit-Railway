package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.Keys;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.packet.PacketOpenDashboardScreen;

public class ItemDashboard extends ItemExtension {

	private final TransportMode transportMode;

	public ItemDashboard(TransportMode transportMode, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.transportMode = transportMode;
	}

	@Override
	public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
		if (Keys.DEBUG && user.isSneaking()) {
			if (world.isClient()) {
				CustomResourceLoader.reload();
			}
		} else {
			if (!world.isClient()) {
				PacketOpenDashboardScreen.sendDirectlyToServer(ServerWorld.cast(world), ServerPlayerEntity.cast(user), transportMode);
			}
		}
	}
}
