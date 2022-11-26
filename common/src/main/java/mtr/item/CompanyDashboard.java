package mtr.item;

import mtr.ItemGroups;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CompanyDashboard extends Item {

	public CompanyDashboard() {
		super(new Properties().tab(ItemGroups.CORE).stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
		if (!world.isClientSide()) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				PacketTrainDataGuiServer.openCompanyDashboardScreenS2C((ServerPlayer) player, railwayData.getUseTimeAndWindSync());
			}
		}
		return super.use(world, player, interactionHand);
	}
}
