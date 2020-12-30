package mtr.entity;

import mtr.MTR;
import mtr.data.Train;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EntityMinecart extends EntityTrainBase {

	public EntityMinecart(World world, double x, double y, double z) {
		super(MTR.MINECART, world, x, y, z);
	}

	public EntityMinecart(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.shouldCancelInteraction()) {
			return ActionResult.PASS;
		} else if (hasPassengers()) {
			return ActionResult.PASS;
		} else if (!world.isClient) {
			return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
		} else {
			return ActionResult.SUCCESS;
		}
	}

	@Override
	public double getMountedHeightOffset() {
		return -0.5;
	}

	@Override
	protected Train.TrainType getTrainType() {
		return Train.TrainType.MINECART;
	}
}
