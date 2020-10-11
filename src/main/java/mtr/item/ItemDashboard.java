package mtr.item;

import mtr.data.PacketTrainDataGui;
import mtr.data.TrainData;
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
			TrainData trainData = TrainData.getInstance(world);
			if (trainData != null) {
				PacketTrainDataGui.sendS2C(user, trainData.getStations(), trainData.getPlatforms(world));
			}
		}
		return super.use(world, user, hand);
	}
}
