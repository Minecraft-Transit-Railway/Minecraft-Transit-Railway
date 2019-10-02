package mtr.entity;

import mtr.Items;
import mtr.item.ItemCrowbar;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityTrain extends EntityMinecart {

	public EntityTrain(World worldIn) {
		super(worldIn);
	}

	public EntityTrain(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	private static final boolean OVERRIDE_SPEED = true;
	private EntityTrain sibling, connection;
	private int section = -1;
	private static final double TOLERANCE = 0.05;
	private static final double ONE_OVER_ROOT_2 = 1 / Math.sqrt(2);
	private static final double ROOT_2 = Math.sqrt(2);

	public abstract int getSpacing();

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

	private double getSpeed() {
		final double mX = Math.abs(motionX);
		final double mZ = Math.abs(motionZ);
		if (mX == 0)
			return mZ;
		if (mZ == 0)
			return mX;
		if (mX == mZ)
			return mX * ROOT_2;
		return Math.sqrt(sq(mX) + sq(mZ));
	}

	@Override
	public void killMinecart(DamageSource source) {
		setDead();
	}

	@Override
	public void setDead() {
		getSibling().isDead = true;
		if (world.isRemote)
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0, 0, 0);
		super.setDead();
	}

	@Override
	public Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		double mX = motionX, mZ = motionZ;
		if (connection == null) {
			if (section == 0 && mX == 0 && mZ == 0) {
				EntityTrain train = this;
				while (train != null && !isDead) {
					train.section = -1;
					train = train.getSection(-1, true);
				}
			} else if (section < 0 && isLeading()) {
				section = 0;
				EntityTrain train = getSibling();
				int i = 1;
				while (train != null && !isDead) {
					train.section = i;
					train = train.getSection(i - 1, true);
					i++;
				}
			}
		}

		if (section > 0) {
			final EntityTrain connected = getSection(section - 1, false);
			final double diffX = connected.posX - posX;
			final double diffZ = connected.posZ - posZ;
			final double distance = Math.sqrt(sq(diffX) + sq(diffZ));
			final double difference = distance - getSpacing();

			if (difference > 3)
				setDead();

			if (distance != 0) {
				final double ratio = difference / distance;
				mX = ratio * diffX;
				mZ = ratio * diffZ;
				if (Math.abs(mX) < TOLERANCE)
					mX = 0;
				if (Math.abs(mZ) < TOLERANCE)
					mZ = 0;
			}

			final double max = getMaxSpeed() + 0.05;
			mX = MathHelper.clamp(mX, -max, max);
			mZ = MathHelper.clamp(mZ, -max, max);
		} else if (section < 0) {
			mX = 0;
			mZ = 0;
		} else if (section == 0) {
			final double max = getMaxSpeed() * ((mX != 0 && mZ != 0) ? ONE_OVER_ROOT_2 : 1);
			mX = MathHelper.clamp(mX, -max, max);
			mZ = MathHelper.clamp(mZ, -max, max);
		}

		move(MoverType.SELF, mX, 0, mZ);
	}

	private boolean isLeading() {
		if (Math.abs(motionX) < TOLERANCE && Math.abs(motionZ) < TOLERANCE) {
			return false;
		} else {
			final EntityTrain sibling = getSibling();
			final boolean aheadX = (motionX > 0) == (posX > sibling.posX);
			final boolean aheadZ = (motionZ > 0) == (posZ > sibling.posZ);
			if (motionX != 0 && motionZ != 0) {
				return aheadX && aheadZ;
			} else {
				if (motionX != 0)
					return aheadX;
				else
					return aheadZ;
			}
		}
	}

	@Override
	protected void applyDrag() {
	}

	private EntityTrain getSection(int number, boolean invert) {
		if ((getSibling().section == number) == !invert)
			return getSibling();
		if ((connection != null && connection.section == number) == !invert)
			return connection;
		return null;
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
			final ItemCrowbar itemCrowbar = ((ItemCrowbar) item);
			if (connection != null || itemCrowbar.train == this || itemCrowbar.train == getSibling()) {
				if (connection != null)
					connection.connection = null;
				connection = null;
				itemCrowbar.train = null;
			} else {
				if (itemCrowbar.train == null) {
					itemCrowbar.train = this;
				} else {
					itemCrowbar.train.connection = this;
					connection = itemCrowbar.train;
					itemCrowbar.train = null;
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
		return super.getMaxSpeed(); // TODO
	}
}
