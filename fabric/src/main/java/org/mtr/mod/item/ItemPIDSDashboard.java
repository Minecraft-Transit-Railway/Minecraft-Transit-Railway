package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.Init;
import org.mtr.mod.Keys;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.packet.PacketOpenDashboardScreen;
import org.mtr.mod.packet.PacketOpenPIDSDashboardScreen;

public class ItemPIDSDashboard extends ItemExtension {


    public ItemPIDSDashboard(ItemSettings itemSettings) {
        super(itemSettings.maxCount(1));
    }

    @Override
    public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(user), new PacketOpenPIDSDashboardScreen());
        }
    }
}
