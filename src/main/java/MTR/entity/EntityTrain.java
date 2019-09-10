package mtr.entity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class EntityTrain extends EntityMinecart {

	public EntityTrain(World worldIn) {
		super(worldIn);
	}

	public EntityTrain(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	private boolean overrideSpeed = true;

	@Override
	public Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		if (player.isSneaking()) {
			return false;
		} else if (this.isBeingRidden()) {
			return true;
		} else {
			if (!this.world.isRemote) {
				player.startRiding(this);
			}
			return true;
		}
	}

	@Override
	protected double getMaxSpeed() {
		return overrideSpeed ? getMaxCartSpeedOnRail() : super.getMaxSpeed();
	}
}
