package MTR;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// this class is just an outline and does nothing
public class EntityBase extends Entity {

	private int turnProgress;
	private double trainX;
	private double trainY;
	private double trainZ;
	private double trainYaw;
	private double trainPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public EntityBase(World worldIn) {
		super(worldIn);
		preventEntitySpawning = true;
	}

	public EntityBase(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x, y, z);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!worldObj.isRemote) {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			moveEntity(motionX, motionY, motionZ);
			setRotation(rotationYaw, rotationPitch);
		} else if (turnProgress > 0) {
			double var15 = posX + (trainX - posX) / turnProgress;
			double var17 = posY + (trainY - posY) / turnProgress;
			double var18 = posZ + (trainZ - posZ) / turnProgress;
			double var7 = MathHelper.wrapDegrees(trainYaw - rotationYaw);
			rotationYaw = (float) (rotationYaw + var7 / turnProgress);
			rotationPitch = (float) (rotationPitch + (trainPitch - rotationPitch) / turnProgress);
			--turnProgress;
			setPosition(var15, var17, var18);
			setRotation(rotationYaw, rotationPitch);
		} else {
			setPosition(posX, posY, posZ);
			setRotation(rotationYaw, rotationPitch);
		}
	}

	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean p_180426_10_) {
		trainX = x;
		trainY = y;
		trainZ = z;
		trainYaw = yaw;
		trainPitch = pitch;
		turnProgress = posRotationIncrements + 2;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	@Override
	public void setVelocity(double x, double y, double z) {
		velocityX = motionX = x;
		velocityY = motionY = y;
		velocityZ = motionZ = z;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
	}
}
