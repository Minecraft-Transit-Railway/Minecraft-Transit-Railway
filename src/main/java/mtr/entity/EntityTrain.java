package mtr.entity;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.common.carts.EntityCartWorldspikeAdmin;
import mods.railcraft.common.carts.LinkageManager;
import mtr.MathTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityTrain extends EntityCartWorldspikeAdmin implements ILinkableCart {

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

	private UUID uuidSibling;
	private EntityTrain entitySibling, entityConnection;

	private int trainType;

	private int doorCooldown;
	private float passengerAngleYaw, prevPassengerAngleYaw, trainSpeed, trainSpeedKm;

	private static final int DOOR_COOLDOWN_MAX = 40, DISTANCE_OFFSET = 10;

	private static final DataParameter<Boolean> MTR_DOOR_LEFT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MTR_DOOR_RIGHT_OPENED = EntityDataManager.<Boolean>createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> MTR_SIBLING_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int cacheSiblingID;
	private static final DataParameter<Integer> MTR_CONNECTION_ID = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int cacheConnectionID;
	private static final DataParameter<Integer> MTR_TRAIN_TYPE = EntityDataManager.<Integer>createKey(EntityTrain.class, DataSerializers.VARINT);
	private int cacheTrainType;

	public abstract int getSiblingSpacing();

	public abstract float getEndSpacing();

	@Override
	public void setDead() {
		if (entitySibling != null)
			entitySibling.isDead = true;
		if (world.isRemote)
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0, 0, 0);
		super.setDead();
	}

	@Override
	public boolean doInteract(EntityPlayer player, EnumHand hand) {
		super.doInteract(player, hand);
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
	public void onUpdate() {
		if (!world.isRemote && entitySibling == null) {
			final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();

			entitySibling = syncEntity(server.getEntityFromUuid(uuidSibling), MTR_SIBLING_ID);

			final EntityMinecart linkA = LinkageManager.INSTANCE.getLinkedCartA(this);
			final EntityMinecart linkB = LinkageManager.INSTANCE.getLinkedCartB(this);
			if (linkA != null && linkA instanceof EntityTrain && linkA != entitySibling)
				entityConnection = syncEntity(linkA, MTR_CONNECTION_ID);
			else if (linkB != null && linkB instanceof EntityTrain && linkB != entitySibling)
				entityConnection = syncEntity(linkB, MTR_CONNECTION_ID);
			else
				entityConnection = syncEntity(null, MTR_CONNECTION_ID);
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
//
//	@Override
//	public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
//		if (!world.isRemote && section <= 0 && uuidConnection.getMostSignificantBits() == 0 && uuidConnection.getLeastSignificantBits() == 0) {
//			if (receivingPower) {
//				setAllSections(false, false);
//				resetAllSections();
//				if (doorCooldown > 0)
//					doorCooldown--;
//				if (doorCooldown == 0 && motionX == 0 && motionZ == 0 && entitySibling != null) {
//					motionX = Math.copySign(getMaxSpeed(), posX - entitySibling.posX);
//					motionZ = Math.copySign(getMaxSpeed(), posZ - entitySibling.posZ);
//				}
//			} else {
//				if (doorCooldown == 0) {
//					motionX = 0;
//					motionZ = 0;
//				}
//				if (doorCooldown == DOOR_COOLDOWN_MAX - 16) {
//					setAllSections(true, true);
//					resetAllSections();
//				}
//				if (doorCooldown < DOOR_COOLDOWN_MAX)
//					doorCooldown++;
//			}
//		}
//	}

	@Override
	public float getDistance(Entity entityIn) {
		return super.getDistance(entityIn) - DISTANCE_OFFSET;
	}

	@Override
	public float getLinkageDistance(EntityMinecart cart) {
		return (cart == entitySibling ? getSiblingSpacing() / 2F : getEndSpacing()) + 2;
	}

	@Override
	public float getOptimalDistance(EntityMinecart cart) {
		return (cart == entitySibling ? getSiblingSpacing() / 2F : getEndSpacing()) - DISTANCE_OFFSET / 2F;
	}

	@Override
	public void onLinkCreated(EntityMinecart cart) {
		if (cart != entitySibling && cart instanceof EntityTrain) {
			entityConnection = (EntityTrain) cart;
			dataManager.set(MTR_CONNECTION_ID, cart.getEntityId());
		} else {
			entityConnection = null;
		}
		System.out.println(cart);
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
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setUUID(compound.getUniqueId("sibling"));
		setTrainType(compound.getInteger("trainType"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setUniqueId("sibling", uuidSibling);
		compound.setInteger("trainType", trainType);
	}

	@Override
	protected void openRailcraftGui(EntityPlayer player) {
	}

	@SideOnly(Side.CLIENT)
	public boolean getLeftDoorClient() {
		return dataManager.get(MTR_DOOR_LEFT_OPENED);
	}

	@SideOnly(Side.CLIENT)
	public boolean getRightDoorClient() {
		return dataManager.get(MTR_DOOR_RIGHT_OPENED);
	}

	@SideOnly(Side.CLIENT)
	public Entity getSiblingClient() {
		if (cacheSiblingID == 0)
			cacheSiblingID = dataManager.get(MTR_SIBLING_ID);
		return world.getEntityByID(cacheSiblingID);
	}

	@SideOnly(Side.CLIENT)
	public Entity getConnectionClient() {
		if (cacheConnectionID == 0)
			cacheConnectionID = dataManager.get(MTR_CONNECTION_ID);
		return world.getEntityByID(cacheConnectionID);
	}

	@SideOnly(Side.CLIENT)
	public int getTrainTypeClient() {
		if (cacheTrainType == 0)
			cacheTrainType = dataManager.get(MTR_TRAIN_TYPE);
		return cacheTrainType;
	}

	public void setUUID(UUID sibling) {
		uuidSibling = sibling;
	}

	private void applyYawToPassenger(Entity passenger) {
		final Entity sibling = world.isRemote ? getSiblingClient() : entitySibling;
		if (sibling != null) {
			passengerAngleYaw = (float) Math.toDegrees(MathTools.angleBetweenPoints(posX, posZ, sibling.posX, sibling.posZ));
			passenger.rotationYaw -= MathTools.angleDifference(passengerAngleYaw, prevPassengerAngleYaw);
			prevPassengerAngleYaw = passengerAngleYaw;
			passenger.setRotationYawHead(passenger.rotationYaw);
		}
	}

	// private void setAllSections(boolean leftDoor, boolean rightDoor) {
//		section = 0;
//		dataManager.set(MTR_DOOR_LEFT_OPENED, leftDoor);
//		dataManager.set(MTR_DOOR_RIGHT_OPENED, rightDoor);
//		EntityTrain train = entitySibling;
//		int i = 1;
//		while (train != null && !isDead) {
//			train.section = i;
//			train.dataManager.set(MTR_DOOR_LEFT_OPENED, leftDoor);
//			train.dataManager.set(MTR_DOOR_RIGHT_OPENED, rightDoor);
//			train = train.getSection(i - 1, true);
//			i++;
//		}
//	}
//

	private EntityTrain syncEntity(Entity genericEntity, DataParameter<Integer> parameter) {
		if (genericEntity != null && genericEntity instanceof EntityTrain) {
			dataManager.set(parameter, genericEntity.getEntityId());
			return (EntityTrain) genericEntity;
		} else {
			dataManager.set(parameter, -1);
			return null;
		}
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
