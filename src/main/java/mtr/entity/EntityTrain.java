package mtr.entity;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import mtr.Items;
import mtr.MathTools;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityTrain extends EntityMinecart {

	public EntityTrain(World worldIn) {
		super(worldIn);
		init();
	}

	public EntityTrain(World worldIn, double x, double y, double z, int type) {
		super(worldIn, x, y, z);
		setTrainType(type);
		init();
	}

	private void init() {
		setSize(1, 3);
		ignoreFrustumCheck = true;
	}

	public Vec3d[] connectionVectorClient = new Vec3d[8];

	private UUID uuidSibling, uuidConnection;
	private EntityTrain entitySibling, entityConnection;
	private int section = -1, trainType, doorCooldown;
	private float prevAngleYaw, trainSpeed, trainSpeedKm;

	private static final int DOOR_COOLDOWN_MAX = 40;
	private static final double TOLERANCE = 0.05;

	private static final DataParameter<Boolean> MTR_DOOR_LEFT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MTR_DOOR_RIGHT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> MTR_SIBLING_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int mtrSiblingID;
	private static final DataParameter<Integer> MTR_CONNECTION_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int mtrConnectionID;
	private static final DataParameter<Integer> MTR_TRAIN_TYPE = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int mtrTrainType;

	public abstract int getSiblingSpacing();

	public abstract float getEndSpacing();

	@Override
	public void killMinecart(DamageSource source) {
		setDead();
	}

	@Override
	public void setDead() {
		if (entitySibling != null)
			entitySibling.isDead = true;
		if (world.isRemote)
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0, 0, 0);
		super.setDead();
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		if (entitySibling != null) {
			double mX = motionX, mZ = motionZ;
			if (entityConnection == null) {
				if (section == 0 && mX == 0 && mZ == 0)
					resetAllSections();
				else if (section < 0 && isLeading())
					setAllSections(false, false);
			}

			if (section > 0) {
				final EntityTrain connection = getSection(section - 1, false);
				if (connection == null || connection.isDead) {
					resetAllSections();
					setConnection(null);
				} else {
					final double diffX = connection.posX - posX;
					final double diffZ = connection.posZ - posZ;
					final double distance = Math.sqrt(sq(diffX) + sq(diffZ));
					final double difference = distance - (connection == entitySibling ? getSiblingSpacing() : getEndSpacing() + connection.getEndSpacing());

					if (difference > 4)
						setDead();

					if (distance != 0) {
						final double ratio = difference / distance;
						mX = ratio * diffX;
						mZ = ratio * diffZ;
					}
				}
			}
			if (section < 0) {
				mX = 0;
				mZ = 0;
			}
			moveEntity(mX, mZ, section > 0);
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		final Item item = player.getHeldItem(hand).getItem();
		if (!world.isRemote && item == Items.crowbar) {
			final ItemCrowbar itemCrowbar = (ItemCrowbar) item;
			if (entityConnection != null && entityConnection.isDead)
				setConnection(null);

			if (entityConnection != null || itemCrowbar.train == this || itemCrowbar.train == entitySibling) {
				if (entityConnection != null)
					entityConnection.setConnection(null);
				setConnection(null);
				itemCrowbar.train = null;
				player.sendStatusMessage(new TextComponentTranslation("gui.crowbar_disconnected"), true);
			} else {
				if (itemCrowbar.train == null) {
					itemCrowbar.train = this;
					player.sendStatusMessage(new TextComponentTranslation("gui.crowbar_connecting"), true);
				} else {
					itemCrowbar.train.setConnection(this);
					setConnection(itemCrowbar.train);
					itemCrowbar.train = null;
					player.sendStatusMessage(new TextComponentTranslation("gui.crowbar_connected"), true);
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
		setTrainType(nbt.getInteger("trainType"));
		section = nbt.getInteger("section");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setUniqueId("sibling", uuidSibling);
		compound.setUniqueId("connection", uuidConnection);
		compound.setInteger("trainType", trainType);
		compound.setInteger("section", section);
		return compound;
	}

	@Override
	public void onUpdate() {
		if (!world.isRemote) {
			final boolean siblingNotSet = entitySibling == null;
			final boolean connectionNotSet = entityConnection == null && uuidConnection.getMostSignificantBits() != 0 && uuidConnection.getLeastSignificantBits() != 0;
			if (siblingNotSet || connectionNotSet) {
				final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
				entitySibling = getEntityFromUUID(server, uuidSibling, MTR_SIBLING_ID);
				entityConnection = getEntityFromUUID(server, uuidConnection, MTR_CONNECTION_ID);
				System.out.println("updating");
			}
		}
		super.onUpdate();
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (isPassenger(passenger)) {
			// TODO allow passenger to move
			applyYawToPassenger(passenger);
			if (!world.isRemote && passenger instanceof EntityPlayer)
				((EntityPlayer) passenger).sendStatusMessage(new TextComponentString(trainSpeed + " m/s (" + trainSpeedKm + " km/h)"), true);
		}
		super.updatePassenger(passenger);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(Entity entityToUpdate) {
		applyYawToPassenger(entityToUpdate);
	}

	@Override
	public double getMountedYOffset() {
		return 1;
	}

	@Override
	public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
		if (!world.isRemote && section <= 0 && uuidConnection.getMostSignificantBits() == 0 && uuidConnection.getLeastSignificantBits() == 0) {
			if (receivingPower) {
				setAllSections(false, false);
				resetAllSections();
				if (doorCooldown > 0)
					doorCooldown--;
				if (doorCooldown == 0 && motionX == 0 && motionZ == 0 && entitySibling != null) {
					motionX = Math.copySign(getMaxSpeed(), posX - entitySibling.posX);
					motionZ = Math.copySign(getMaxSpeed(), posZ - entitySibling.posZ);
				}
			} else {
				if (doorCooldown == 0) {
					motionX = 0;
					motionZ = 0;
				}
				if (doorCooldown == DOOR_COOLDOWN_MAX - 16) {
					setAllSections(true, true);
					resetAllSections();
				}
				if (doorCooldown < DOOR_COOLDOWN_MAX)
					doorCooldown++;
			}
		}
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
		dataManager.register(MTR_TRAIN_TYPE, 0);
	}

	@Override
	protected void applyDrag() {
	}

	@SideOnly(Side.CLIENT)
	public boolean getLeftDoorClient() {
		if (motionX != 0 || motionZ != 0)
			return false;
		else
			return dataManager.get(MTR_DOOR_LEFT_OPENED);
	}

	@SideOnly(Side.CLIENT)
	public boolean getRightDoorClient() {
		if (motionX != 0 || motionZ != 0)
			return false;
		else
			return dataManager.get(MTR_DOOR_RIGHT_OPENED);
	}

	@SideOnly(Side.CLIENT)
	public int getSiblingIDClient() {
		if (mtrSiblingID == 0)
			mtrSiblingID = dataManager.get(MTR_SIBLING_ID);
		return mtrSiblingID;
	}

	@SideOnly(Side.CLIENT)
	public int getConnectionIDClient() {
		if (mtrConnectionID == 0)
			mtrConnectionID = dataManager.get(MTR_CONNECTION_ID);
		return mtrConnectionID;
	}

	@SideOnly(Side.CLIENT)
	public int getTrainTypeClient() {
		if (mtrTrainType == 0)
			mtrTrainType = dataManager.get(MTR_TRAIN_TYPE);
		return mtrTrainType;
	}

	public void setUUID(UUID sibling, UUID connection) {
		uuidSibling = sibling;
		uuidConnection = connection;
	}

	private void applyYawToPassenger(Entity passenger) {
		final Entity sibling = world.isRemote ? world.getEntityByID(getSiblingIDClient()) : entitySibling;
		if (sibling != null) {
			final float angleYaw = (float) Math.toDegrees(MathTools.angleBetweenPoints(posX, posZ, sibling.posX, sibling.posZ));
			passenger.rotationYaw -= MathTools.angleDifference(angleYaw, prevAngleYaw);
			prevAngleYaw = angleYaw;
			passenger.setRotationYawHead(passenger.rotationYaw);
		}
	}

	private boolean isLeading() {
		if (Math.abs(motionX) < TOLERANCE && Math.abs(motionZ) < TOLERANCE) {
			return false;
		} else {
			final boolean aheadX = motionX > 0 == posX > entitySibling.posX;
			final boolean aheadZ = motionZ > 0 == posZ > entitySibling.posZ;
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

	private EntityTrain getSection(int number, boolean blacklist) {
		if ((entitySibling != null && entitySibling.section == number) == !blacklist)
			return entitySibling;
		if ((entityConnection != null && entityConnection.section == number) == !blacklist)
			return entityConnection;
		return null;
	}

	private void moveEntity(double mX, double mZ, boolean catchUp) {
		if (mX == 0 && mZ == 0) {
			trainSpeed = trainSpeedKm = 0;
		} else {
			final double max = catchUp ? getMaxSpeed() + 0.05 : getMaxSpeed();
			mX = MathHelper.clamp(mX, -max, max);
			mZ = MathHelper.clamp(mZ, -max, max);
			move(MoverType.SELF, mX, 0, mZ);

			final float speed = (float) Math.sqrt(sq(mX) + sq(mZ)) * 200;
			trainSpeed = Math.round(speed) / 10;
			trainSpeedKm = Math.round(speed * 3.6) / 10;
		}
	}

	private void resetAllSections() {
		EntityTrain train = this;
		while (train != null && !isDead) {
			train.section = -1;
			train = train.getSection(-1, true);
		}
	}

	private void setAllSections(boolean leftDoor, boolean rightDoor) {
		section = 0;
		dataManager.set(MTR_DOOR_LEFT_OPENED, leftDoor);
		dataManager.set(MTR_DOOR_RIGHT_OPENED, rightDoor);
		EntityTrain train = entitySibling;
		int i = 1;
		while (train != null && !isDead) {
			train.section = i;
			train.dataManager.set(MTR_DOOR_LEFT_OPENED, leftDoor);
			train.dataManager.set(MTR_DOOR_RIGHT_OPENED, rightDoor);
			train = train.getSection(i - 1, true);
			i++;
		}
	}

	private EntityTrain getEntityFromUUID(MinecraftServer server, UUID uuid, DataParameter<Integer> parameter) {
		final Entity genericEntity = server.getEntityFromUuid(uuid);
		if (genericEntity != null && genericEntity instanceof EntityTrain) {
			dataManager.set(parameter, genericEntity.getEntityId());
			return (EntityTrain) genericEntity;
		}
		return null;
	}

	private void setConnection(EntityTrain train) {
		uuidConnection = train == null ? new UUID(0, 0) : train.getUniqueID();
		entityConnection = train;
		dataManager.set(MTR_CONNECTION_ID, train == null ? 0 : train.getEntityId());
	}

	private void setTrainType(int type) {
		trainType = type;
		dataManager.set(MTR_TRAIN_TYPE, trainType);
	}

	private double sq(double d) {
		return d * d;
	}

	public static enum EnumTrainType {
		HEAD(0, "head"), CAR(1, "car"), FIRST_CLASS(2, "first_class");

		private static final Map<Integer, EnumTrainType> BY_ID = Maps.<Integer, EnumTrainType>newHashMap();
		private final int index;
		private final String name;

		private EnumTrainType(int indexIn, String nameIn) {
			index = indexIn;
			name = nameIn;
		}

		public String getName() {
			return name;
		}

		public static EnumTrainType getByIndex(int index) {
			final EnumTrainType trainType = BY_ID.get(index);
			return trainType == null ? CAR : trainType;
		}

		public static EnumTrainType getByDirection(int direction) {
			return getByIndex(Math.abs(direction) - 1);
		}

		static {
			for (final EnumTrainType trainType : values())
				BY_ID.put(trainType.index, trainType);
		}
	}
}
