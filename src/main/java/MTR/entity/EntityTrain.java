package mtr.entity;

import java.util.UUID;

import mtr.Items;
import mtr.item.ItemCrowbar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class EntityTrain extends EntityMinecart {

	public EntityTrain(World worldIn) {
		super(worldIn);
		init();
	}

	public EntityTrain(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		init();
	}

	private void init() {
		setSize(1, 3);
		ignoreFrustumCheck = true;
	}

	private UUID uuidSibling, uuidConnection;
	private EntityTrain entitySibling, entityConnection;
	private int section = -1;
	private static final double TOLERANCE = 0.05;
	private static final double ONE_OVER_ROOT_2 = 1 / Math.sqrt(2);
	private static final double ROOT_2 = Math.sqrt(2);

	private static final DataParameter<Boolean> MTR_DOOR_LEFT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private boolean mtrDoorLeft;
	private static final DataParameter<Boolean> MTR_DOOR_RIGHT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private boolean mtrDoorRight;
	private static final DataParameter<Integer> MTR_SIBLING_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int mtrSiblingID;
	private static final DataParameter<Integer> MTR_CONNECTION_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int mtrConnectionID;

	public abstract int getSpacing();

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
	public void moveMinecartOnRail(BlockPos pos) {
		double mX = motionX, mZ = motionZ;
		if (getSibling() != this) {
			if (entityConnection == null) {
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
				final EntityTrain connection = getSection(section - 1, false);
				final double diffX = connection.posX - posX;
				final double diffZ = connection.posZ - posZ;
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
		}
		move(MoverType.SELF, mX, 0, mZ);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		final Item item = player.getHeldItem(hand).getItem();
		if (!world.isRemote && item == Items.crowbar) {
			final ItemCrowbar itemCrowbar = ((ItemCrowbar) item);
			if (entityConnection != null || itemCrowbar.train == this || itemCrowbar.train == getSibling()) {
				if (entityConnection != null)
					entityConnection.entityConnection = null;
				entityConnection = null;
				itemCrowbar.train = null;
			} else {
				if (itemCrowbar.train == null) {
					itemCrowbar.train = this;
				} else {
					itemCrowbar.train.entityConnection = this;
					entityConnection = itemCrowbar.train;
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		setUUID(nbt.getUniqueId("sibling"), nbt.getUniqueId("connection"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setUniqueId("sibling", uuidSibling);
		compound.setUniqueId("connection", uuidConnection);
		return compound;
	}

	@Override
	public void onUpdate() {
		if (!world.isRemote && entitySibling == null) {
			final Entity generic = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getEntityFromUuid(uuidSibling);
			if (generic != null && (generic instanceof EntityTrain)) {
				entitySibling = (EntityTrain) generic;
				dataManager.set(MTR_SIBLING_ID, entitySibling.getEntityId());
			}
		}
		super.onUpdate();
	}

	@Override
	public Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(MTR_DOOR_LEFT_OPENED, false);
		dataManager.register(MTR_DOOR_RIGHT_OPENED, false);
		dataManager.register(MTR_SIBLING_ID, 0);
		dataManager.register(MTR_CONNECTION_ID, 0);
	}

	@Override
	protected void applyDrag() {
	}

	@Override
	protected double getMaxSpeed() {
		return super.getMaxSpeed(); // TODO
	}

	public int getLeftDoor() {
		return 0;
	}

	public int getRightDoor() {
		return 0;
	}

	public int getSiblingIDClient() {
		if (mtrSiblingID == 0)
			mtrSiblingID = dataManager.get(MTR_SIBLING_ID);
		return mtrSiblingID;
	}

	public void setUUID(UUID sibling, UUID connection) {
		uuidSibling = sibling;
		uuidConnection = connection;
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

	private EntityTrain getSection(int number, boolean invert) {
		if ((getSibling().section == number) == !invert)
			return getSibling();
		if ((entityConnection != null && entityConnection.section == number) == !invert)
			return entityConnection;
		return null;
	}

	private EntityTrain getSibling() {
		if (entitySibling == null) {
			// isDead = true;
			return this;
		} else {
			return entitySibling;
		}
	}

	private double sq(double d) {
		return d * d;
	}
}
