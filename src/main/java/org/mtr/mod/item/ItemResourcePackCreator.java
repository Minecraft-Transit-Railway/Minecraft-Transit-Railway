package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.packet.PacketOpenResourcePackCreatorScreen;

public class ItemResourcePackCreator extends ItemExtension {

	public ItemResourcePackCreator(ItemSettings itemSettings) {
		super(itemSettings);
	}

	@Override
	public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			Registry.sendPacketToClient(ServerPlayerEntity.cast(user), new PacketOpenResourcePackCreatorScreen());
		}
	}
}
