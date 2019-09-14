package mtr.entity;

import mtr.Items;
import mtr.item.ItemCrowbar;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class EntityTrain extends EntityMinecart {

	public EntityTrain(World worldIn) {
		super(worldIn);
	}

	public EntityTrain(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	private final boolean overrideSpeed = true;
	private EntityTrain sibling, connection;
	private boolean isLead;

	public void setSibling(EntityTrain train) {
		if (sibling == null)
			sibling = train;
	}

	private EntityTrain getSibling() {
		if (sibling == null) {
			isDead = true;
			return this;
		} else {
			return sibling;
		}
	}

	public int getSpacing() {
		return 3;
	}

	@Override
	public void setDead() {
		getSibling().isDead = true;
		super.setDead();
	}

	@Override
	public Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	protected void applyDrag() {
		final double tolerance = 0.05;
		final boolean isConnected = connection != null;
		final EntityTrain connected = isConnected ? connection : getSibling();
		if (isConnected || connected.isLead || Math.abs(motionX) < tolerance && Math.abs(motionZ) < tolerance) {
			isLead = false;
		} else {
			final boolean aheadX = (motionX > 0) == (posX > connected.posX);
			final boolean aheadZ = (motionZ > 0) == (posZ > connected.posZ);
			if (motionX != 0 && motionZ != 0) {
				isLead = aheadX && aheadZ;
			} else {
				if (motionX != 0)
					isLead = aheadX;
				if (motionZ != 0)
					isLead = aheadZ;
			}
		}
		if (!isLead) {
			final double diffX = connected.posX - posX;
			final double diffZ = connected.posZ - posZ;
			final double distance = Math.sqrt(sq(diffX) + sq(diffZ));
			final double difference = distance - getSpacing();
			if (difference != 0) {
				System.out.println(Math.round(difference * 1000) / 1000D);
				final double ratio = difference / distance;
				motionX = ratio * diffX;
				motionZ = ratio * diffZ;
				if (Math.abs(motionX) < tolerance)
					motionX = 0;
				if (Math.abs(motionZ) < tolerance)
					motionZ = 0;
			}
		}
	}

	private double sq(double d) {
		return d * d;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		final Item item = player.getHeldItem(hand).getItem();
		if (!world.isRemote && item == Items.crowbar) {
			if (connection != null) {
				connection.connection = null;
				connection = null;
			} else {
				final ItemCrowbar itemCrowbar = ((ItemCrowbar) item);
				if (itemCrowbar.train == null) {
					itemCrowbar.train = this;
				} else {
					itemCrowbar.train.connection = this;
					connection = itemCrowbar.train;
				}
			}
			return true;
		}

		if (player.isSneaking()) {
			return false;
		} else if (isBeingRidden()) {
			return true;
		} else {
			if (!world.isRemote)
				player.startRiding(this);
			return true;
		}
	}

	@Override
	protected double getMaxSpeed() {
		return super.getMaxSpeed() * (isLead ? 1 : 2);
	}
}
