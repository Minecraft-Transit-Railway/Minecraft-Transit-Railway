package mtr.item;

import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemDashboard extends Item {

	public ItemDashboard(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				PacketTrainDataGuiServer.openDashboardScreenS2C(user, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes());
			}
		}
		return super.use(world, user, hand);
	}
}
