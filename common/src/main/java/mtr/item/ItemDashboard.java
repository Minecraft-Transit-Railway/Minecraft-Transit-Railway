package mtr.item;

import mtr.CreativeModeTabs;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemDashboard extends ItemWithCreativeTabBase {

	private final TransportMode transportMode;

	public ItemDashboard(TransportMode transportMode) {
		super(CreativeModeTabs.CORE, properties -> properties.stacksTo(1));
		this.transportMode = transportMode;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
		if (!world.isClientSide()) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				PacketTrainDataGuiServer.openDashboardScreenS2C((ServerPlayer) player, transportMode, railwayData.getUseTimeAndWindSync());
			}
		}
		return super.use(world, player, interactionHand);
	}
}
