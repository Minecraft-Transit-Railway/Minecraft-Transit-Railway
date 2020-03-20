package mtr.entity;

import com.google.common.collect.Maps;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.common.carts.CartTools;
import mods.railcraft.common.carts.EntityCartWorldspikeAdmin;
import mods.railcraft.common.carts.LinkageManager;
import mods.railcraft.common.carts.Train;
import mods.railcraft.common.core.RailcraftConfig;
import mods.railcraft.common.util.entity.RCEntitySelectors;
import mods.railcraft.common.util.entity.RailcraftDamageSource;
import mtr.MTRUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

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
		ignoreFrustumCheck = true;
	}

	public final Vec3d[] connectionVectorClient = new Vec3d[8];

	private UUID uuidSibling;
	private EntityTrain entitySibling, entityConnection;

	private int trainType, speedBoost;
	private float prevPassengerAngleYaw, leftDoorClient, rightDoorClient;
	private long leftDoorTimeClient, rightDoorTimeClient;

	private static final int RC_LINKING_DISTANCE_OFFSET = 100;
	private static final int ID_DEFAULT = -1, TRAIN_TYPE_DEFAULT = 0;
	private static final float ENGINE_TRIGGER_SPEED = 0.05F;
	private static final ITextComponent DISMOUNT_TEXT = new TextComponentTranslation("mount.onboard", GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()));

	private static final DataParameter<Boolean> MTR_DOOR_LEFT_OPENED = EntityDataManager.createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MTR_DOOR_RIGHT_OPENED = EntityDataManager.createKey(EntityTrain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> MTR_SIBLING_ID = EntityDataManager.createKey(EntityTrain.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MTR_CONNECTION_ID = EntityDataManager.createKey(EntityTrain.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MTR_TRAIN_TYPE = EntityDataManager.createKey(EntityTrain.class, DataSerializers.VARINT);

	private int cacheSiblingID = ID_DEFAULT;
	private int cacheTrainType = TRAIN_TYPE_DEFAULT;

	public abstract int getSiblingSpacing();

	public abstract int getEndSpacing();

	protected abstract Item getTrainItem();

	@Override
	public void setDead() {
		if (entitySibling != null)
			entitySibling.isDead = true;
		if (world.isRemote)
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0, 0, 0);
		super.setDead();
	}

	@Override
	public void killAndDrop(EntityMinecart cart) {
		super.killAndDrop(cart);
		dropItemWithOffset(getTrainItem(), 1, 0);
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
		if (!world.isRemote) {
			if (entitySibling == null) {
				final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
				if (server != null)
					entitySibling = syncEntity(server.getEntityFromUuid(uuidSibling), MTR_SIBLING_ID);

				if (entitySibling != null) {
					LinkageManager.INSTANCE.createLink(this, entitySibling);

					final EntityMinecart linkA = LinkageManager.INSTANCE.getLinkedCartA(this);
					final EntityMinecart linkB = LinkageManager.INSTANCE.getLinkedCartB(this);
					if (linkA instanceof EntityTrain && linkA != entitySibling)
						entityConnection = syncEntity(linkA, MTR_CONNECTION_ID);
					else if (linkB instanceof EntityTrain && linkB != entitySibling)
						entityConnection = syncEntity(linkB, MTR_CONNECTION_ID);
					else
						entityConnection = syncEntity(null, MTR_CONNECTION_ID);
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (!world.isRemote) {
			if (!entityIn.isEntityAlive())
				return;
			if (Train.streamCarts(this).noneMatch(t -> t.isPassenger(entityIn))
					&& (Math.abs(motionX) > 0.2 || Math.abs(motionZ) > 0.2 || CartTools.isTravellingHighSpeed(this))
					&& RCEntitySelectors.KILLABLE.test(entityIn)) {
				final EntityLivingBase living = (EntityLivingBase) entityIn;
				living.attackEntityFrom(RailcraftDamageSource.TRAIN, 25);
				if (living.getHealth() > 0) {
					final float yaw = (rotationYaw - 90) * (float) Math.PI / 180.0F;
					living.addVelocity(-MathHelper.sin(yaw) * 0.5F, 0.2D, MathHelper.cos(yaw) * 0.5F);
				}
				return;
			}
		}
		super.applyEntityCollision(entityIn);
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (isPassenger(passenger)) {
			// TODO allow passenger to move
			applyYawToPassenger(passenger);
			if (!world.isRemote && passenger instanceof EntityPlayer) {
				final float trainSpeed = (float) Math.sqrt(motionX * motionX + motionZ * motionZ) * 20;
				final boolean opened = trainSpeed == 0 && dataManager.get(MTR_DOOR_LEFT_OPENED) || dataManager.get(MTR_DOOR_RIGHT_OPENED);
				final ITextComponent message = opened ? DISMOUNT_TEXT : new TextComponentString(String.format("%s m/s (%s km/h)", Math.round(trainSpeed * 10) / 10F, Math.round(trainSpeed * 36) / 10F));
				((EntityPlayer) passenger).sendStatusMessage(message, true);
			}
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
	public float getDistance(Entity entityIn) {
		return super.getDistance(entityIn) - RC_LINKING_DISTANCE_OFFSET;
	}

	@Override
	public float getLinkageDistance(EntityMinecart cart) {
		return (cart == entitySibling ? getSiblingSpacing() / 2F : getEndSpacing()) + 2;
	}

	@Override
	public float getOptimalDistance(EntityMinecart cart) {
		return (cart == entitySibling ? getSiblingSpacing() / 2F : getEndSpacing()) - RC_LINKING_DISTANCE_OFFSET / 2F;
	}

	@Override
	public void onLinkCreated(EntityMinecart cart) {
		entityConnection = syncEntity(cart != entitySibling ? cart : null, MTR_CONNECTION_ID);
	}

	@Override
	public void onLinkBroken(EntityMinecart cart) {
		if (cart == entityConnection)
			entityConnection = syncEntity(null, MTR_CONNECTION_ID);

		LinkageManager.INSTANCE.repairLink(this, entitySibling);
		LinkageManager.INSTANCE.createLink(this, entitySibling);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(MTR_DOOR_LEFT_OPENED, false);
		dataManager.register(MTR_DOOR_RIGHT_OPENED, false);
		dataManager.register(MTR_SIBLING_ID, ID_DEFAULT);
		dataManager.register(MTR_CONNECTION_ID, ID_DEFAULT);
		dataManager.register(MTR_TRAIN_TYPE, TRAIN_TYPE_DEFAULT);
	}

	@Override
	protected void applyDrag() {
		if (world.isRemote || entitySibling == null) return;

		final boolean isHead = entityConnection == null;
		final boolean isMoving = Math.abs(motionX) > ENGINE_TRIGGER_SPEED || Math.abs(motionZ) > ENGINE_TRIGGER_SPEED;
		final boolean isSameDirection = (posX - entitySibling.posX) * motionX >= 0 && (posZ - entitySibling.posZ) * motionZ >= 0;
		if (isHead && isMoving && isSameDirection) {
			if (speedBoost < 20) {
				speedBoost++;
				super.applyDrag();
			} else {
				final float force = RailcraftConfig.locomotiveHorsepower() * 0.01F * (CartTools.isTravellingHighSpeed(this) ? 3.5F : 1);
				final double yaw = Math.toRadians(getTrainAngle(entitySibling));
				motionX += Math.cos(yaw) * force;
				motionZ += Math.sin(yaw) * force;
			}
		} else {
			speedBoost = 0;
			super.applyDrag();
		}
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
	public float getLeftDoorClient() {
		final boolean opened = dataManager.get(MTR_DOOR_LEFT_OPENED);
		final Tuple<Float, Long> tuple = MTRUtilities.updateDoor(opened, leftDoorClient, leftDoorTimeClient);
		leftDoorClient = tuple.getFirst();
		leftDoorTimeClient = tuple.getSecond();
		return leftDoorClient;
	}

	@SideOnly(Side.CLIENT)
	public float getRightDoorClient() {
		final boolean opened = dataManager.get(MTR_DOOR_RIGHT_OPENED);
		final Tuple<Float, Long> tuple = MTRUtilities.updateDoor(opened, rightDoorClient, rightDoorTimeClient);
		rightDoorClient = tuple.getFirst();
		rightDoorTimeClient = tuple.getSecond();
		return rightDoorClient;
	}

	@SideOnly(Side.CLIENT)
	public EntityTrain getSiblingClient() {
		if (cacheSiblingID == ID_DEFAULT)
			cacheSiblingID = dataManager.get(MTR_SIBLING_ID);
		final Entity entity = world.getEntityByID(cacheSiblingID);
		if (entity instanceof EntityTrain)
			return (EntityTrain) entity;
		else
			return null;
	}

	@SideOnly(Side.CLIENT)
	public Entity getConnectionClient() {
		return world.getEntityByID(dataManager.get(MTR_CONNECTION_ID));
	}

	@SideOnly(Side.CLIENT)
	public int getTrainTypeClient() {
		if (cacheTrainType == TRAIN_TYPE_DEFAULT)
			cacheTrainType = dataManager.get(MTR_TRAIN_TYPE);
		return cacheTrainType;
	}

	public void setUUID(UUID sibling) {
		uuidSibling = sibling;
	}

	public void setDoors(EnumFacing doorDirection) {
		if (doorDirection == null) {
			dataManager.set(MTR_DOOR_LEFT_OPENED, false);
			dataManager.set(MTR_DOOR_RIGHT_OPENED, false);
		} else if (entitySibling != null) {
			final EnumFacing trainFacing = EnumFacing.fromAngle(360 - getTrainAngle(entitySibling));
			if ((trainFacing == doorDirection || trainFacing.rotateY() == doorDirection) != trainType < 0)
				dataManager.set(MTR_DOOR_LEFT_OPENED, true);
			else
				dataManager.set(MTR_DOOR_RIGHT_OPENED, true);
		}
	}

	private void applyYawToPassenger(Entity passenger) {
		final Entity sibling = world.isRemote ? getSiblingClient() : entitySibling;
		if (sibling != null) {
			final float passengerAngleYaw = getTrainAngle(sibling);
			passenger.rotationYaw -= MTRUtilities.angleDifference(passengerAngleYaw, prevPassengerAngleYaw);
			prevPassengerAngleYaw = passengerAngleYaw;
			passenger.setRotationYawHead(passenger.rotationYaw);
		}
	}

	private float getTrainAngle(Entity sibling) {
		return (float) Math.toDegrees(MTRUtilities.angleBetweenPoints(posX, posZ, sibling.posX, sibling.posZ));
	}

	private EntityTrain syncEntity(Entity genericEntity, DataParameter<Integer> parameter) {
		if (genericEntity instanceof EntityTrain) {
			dataManager.set(parameter, genericEntity.getEntityId());
			return (EntityTrain) genericEntity;
		} else {
			dataManager.set(parameter, ID_DEFAULT);
			return null;
		}
	}

	private void setTrainType(int type) {
		trainType = type;
		dataManager.set(MTR_TRAIN_TYPE, trainType);
	}

	public enum EnumTrainType {
		HEAD(0, "head"), CAR(1, "car"), FIRST_CLASS(2, "first_class");

		private static final Map<Integer, EnumTrainType> BY_ID = Maps.newHashMap();
		private final int index;
		private final String name;

		EnumTrainType(int indexIn, String nameIn) {
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
