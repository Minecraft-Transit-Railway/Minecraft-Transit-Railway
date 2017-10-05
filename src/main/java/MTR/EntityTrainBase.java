package MTR;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import MTR.blocks.BlockRailBase2;
import MTR.blocks.BlockRailBooster;
import MTR.blocks.BlockRailCurved;
import MTR.blocks.BlockRailDetector2;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockRailIntersection;
import MTR.blocks.BlockRailReverse;
import MTR.blocks.BlockRailSlope1;
import MTR.blocks.BlockRailSlope2;
import MTR.blocks.BlockRailStation;
import MTR.blocks.BlockRailStraight;
import MTR.blocks.BlockRailSwitch;
import MTR.blocks.BlockTrainTimer;
import MTR.items.ItemKillTrain;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityTrainBase extends Entity implements LoadingCallback {

	private int turnProgress;
	private double trainX, trainY, trainZ;
	private double trainYaw, trainPitch;
	private double velocityX, velocityY, velocityZ;
	private static final double accleration = 0.01;

	protected UUID uuidWheel, uuidConnected;
	private EntityTrainBase entityWheel, entityConnected;
	private double goalX, goalY, goalZ, goalSpeed;
	private int radius, centerX, centerZ;
	public int leftDoor = -40, rightDoor = -40;
	protected boolean front;

	private Ticket trainTicket;
	private static List<Ticket> allTickets;
	protected SoundEvent doorOpen = null;
	protected SoundEvent doorClose = null;
	protected boolean arrivedAtStation = false;

	private final boolean isNormalTrain = !(this instanceof EntityMinecartSpecial);

	private static final DataParameter<Boolean> MTR_DOORLEFT = EntityDataManager
			.<Boolean>createKey(EntityTrainBase.class, DataSerializers.BOOLEAN);
	private boolean mtrDoorLeft;
	private static final DataParameter<Boolean> MTR_DOORRIGHT = EntityDataManager
			.<Boolean>createKey(EntityTrainBase.class, DataSerializers.BOOLEAN);
	private boolean mtrDoorRight;
	private static final DataParameter<Integer> MTR_WHEELID = EntityDataManager
			.<Integer>createKey(EntityTrainBase.class, DataSerializers.VARINT);
	public int mtrWheelID;
	private static final DataParameter<Integer> MTR_CONNECTEDID = EntityDataManager
			.<Integer>createKey(EntityTrainBase.class, DataSerializers.VARINT);
	public int mtrConnectedID;
	private static final DataParameter<Integer> MTR_HEAD = EntityDataManager.<Integer>createKey(EntityTrainBase.class,
			DataSerializers.VARINT);
	public int mtrHead;
	private static final DataParameter<Float> MTR_YAW = EntityDataManager.<Float>createKey(EntityTrainBase.class,
			DataSerializers.FLOAT);
	private float mtrYaw;
	private static final DataParameter<Float> MTR_PITCH = EntityDataManager.<Float>createKey(EntityTrainBase.class,
			DataSerializers.FLOAT);
	private float mtrPitch;
	private static final DataParameter<Float> MTR_SPEED = EntityDataManager.<Float>createKey(EntityTrainBase.class,
			DataSerializers.FLOAT);
	private float mtrSpeed;

	public EntityTrainBase(World worldIn) {
		super(worldIn);
		setSize(2F, 4F);
		noClip = true;
		ignoreFrustumCheck = true;
		preventEntitySpawning = true;
	}

	public EntityTrainBase(World worldIn, double x, double y, double z, boolean f, int h) {
		this(worldIn);
		setPosition(x, y, z);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		front = f;
		mtrHead = h;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!worldObj.isRemote)
			serverOnUpdate();
		else
			clientOnUpdate();
	}

	private void serverOnUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (isNormalTrain) {
			entityWheel = getEntityWheel();
			entityConnected = getEntityConnected();
		}

		int blockX = MathHelper.floor_double(posX);
		int blockY = (int) Math.round(posY);
		int blockZ = MathHelper.floor_double(posZ);

		readBlock(blockX, blockY, blockZ);
		loadChunks(blockX, blockY, blockZ);

		if (!(front && entityConnected == null))
			try {
				EntityTrainBase entityFront;
				int offset;
				if (front) {
					entityFront = entityConnected;
					offset = 9;
				} else {
					entityFront = entityWheel;
					offset = getTrainLength();
				}
				goalSpeed = MathTools.distanceBetweenPoints(entityFront.posX, entityFront.posY, entityFront.posZ, posX,
						posY, posZ) - offset;
				if (goalSpeed < 0)
					goalSpeed = 0;
				setSpeed(goalSpeed);
			} catch (Exception e) {
				goalSpeed = 0;
			}

		if (leftDoor > 0 || rightDoor > 0) {
			List<EntityPlayer> e = worldObj.getEntitiesWithinAABB(EntityPlayer.class,
					new AxisAlignedBB(posX - 10, posY - 3, posZ - 10, posX + 10, posY + 5, posZ + 10));
			if (e.size() > 0) {
				double yaw = Math.toRadians(rotationYaw);
				EntityPlayer entity = e.get(0);
				int a = 0, b = 0;
				if (this instanceof EntityLightRail1)
					a = front ? 6 : 0;
				if (this instanceof EntitySP1900) {
					a = front ? 4 : -4;
					b = front ? 8 : 0;
				}
				BlockPos pos0 = entity.getPosition();
				BlockPos pos1 = new BlockPos(posX - a * Math.cos(yaw), posY, posZ + a * Math.sin(yaw));
				BlockPos pos2 = new BlockPos(posX - b * Math.cos(yaw), posY, posZ + b * Math.sin(yaw));
				BlockPos pos3 = new BlockPos(posX, posY, posZ);
				double d1 = MathTools.distanceBetweenPoints(pos1, pos0);
				double d2 = MathTools.distanceBetweenPoints(pos2, pos0);
				double d3 = MathTools.distanceBetweenPoints(pos3, pos0);
				if (d1 <= 1.5 || d2 <= 1.5 || d3 <= 1.5)
					entity.startRiding(this);
			}
		}

		getToGoalSpeed();
		if (radius == 0)
			moveInStraightLine(mtrSpeed);
		else
			moveInCurve(mtrSpeed);

		moveEntity(motionX, motionY, motionZ);
		setRotation(rotationYaw, rotationPitch);
		setTrainRotation(rotationYaw, rotationPitch);
	}

	private void clientOnUpdate() {
		trainYaw = getYaw();
		trainPitch = getPitch();
		mtrSpeed = getSpeed();
		getWheelID();
		getConnectedID();
		getHead();

		if (turnProgress > 0) {
			double clientX = posX + (trainX - posX) / turnProgress;
			double clientY = posY + (trainY - posY) / turnProgress;
			double clientZ = posZ + (trainZ - posZ) / turnProgress;
			rotationYaw = (float) (rotationYaw + MathTools.angleDifference(trainYaw, rotationYaw) / turnProgress);
			rotationPitch = (float) (rotationPitch + (trainPitch - rotationPitch) / turnProgress);
			--turnProgress;
			setPosition(clientX, clientY, clientZ);
			setRotation(rotationYaw, rotationPitch);
		} else {
			setPosition(posX, posY, posZ);
			setRotation(rotationYaw, rotationPitch);
		}

		if (leftDoor < 0)
			leftDoor = 0;
		if (rightDoor < 0)
			rightDoor = 0;
		if (mtrSpeed == 0) {
			mtrDoorLeft = getLeftDoor();
			mtrDoorRight = getRightDoor();
			if (leftDoor < 60 && mtrDoorLeft)
				leftDoor++;
			if (rightDoor < 60 && mtrDoorRight)
				rightDoor++;
			if (leftDoor > 0 && !mtrDoorLeft)
				leftDoor--;
			if (rightDoor > 0 && !mtrDoorRight)
				rightDoor--;
		}

		if (isBeingRidden() && getPassengers().contains(Minecraft.getMinecraft().thePlayer)) {
			float speed1 = Math.round(mtrSpeed * 200) / 10F, speed2 = Math.round(mtrSpeed * 720) / 10F;
			String speedText = I18n.format("gui.trainspeed", new Object[0]) + " " + speed1 + "m/s (" + speed2 + "km/h)";
			String mountText = I18n.format("mount.onboard", new Object[] { GameSettings
					.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) });
			Minecraft.getMinecraft().ingameGUI.setRecordPlaying(leftDoor > 0 || rightDoor > 0 ? mountText : speedText,
					false);
		}
	}

	/** Called when the train goes over a straight rail. Serverside only. **/
	protected void onStraightRailPass(BlockPos pos) {
	}

	/** Called when the train goes over a detector rail. Serverside only. **/
	protected void onDetectorRailPass(BlockPos pos) {
	}

	/** Called when the train goes over a booster rail. Serverside only. **/
	protected void onBoosterRailPass(BlockPos pos, boolean powered) {
		int var3 = worldObj.getBlockState(pos).getValue(BlockRailBooster.ROTATION);
		int angle = (int) Math.round((-45D * var3 + 450D) % 360);
		if (Math.abs(angle - rotationYaw) < 2) {
			TileEntityRailBoosterEntity te = (TileEntityRailBoosterEntity) worldObj.getTileEntity(pos);
			if (powered) {
				float speed = te.speedBoost / 200F; // meters per tick
				if (goalSpeed < speed)
					goalSpeed = speed;
			} else {
				float speed = te.speedSlow / 200F; // meters per tick
				if (goalSpeed > speed)
					goalSpeed = speed;
			}
		}
	}

	/** Called when the train goes over a station rail. Serverside only. **/
	protected void onStationRailPass(BlockPos pos, boolean powered) {
		SoundEvent soundIn = null;

		if (powered && mtrSpeed == 0) {
			setDoors(false, false);
			if (leftDoor == -40 && rightDoor == -40)
				goalSpeed = 0.25;
			else {
				if (leftDoor == 60 || rightDoor == 60)
					soundIn = doorClose;
				if (leftDoor == 59 || rightDoor == 59)
					soundIn = MTRSounds.trainDoorclose;
				if (leftDoor > -40)
					leftDoor--;
				if (rightDoor > -40)
					rightDoor--;
			}
		} else if (!powered) {
			goalSpeed = 0;
			setSpeed(0);

			int x1 = (int) Math.round(Math.sin(Math.toRadians(rotationYaw + 180)));
			int z1 = (int) Math.round(Math.cos(Math.toRadians(rotationYaw + 180)));
			int x2 = (int) Math.round(Math.sin(Math.toRadians(rotationYaw)));
			int z2 = (int) Math.round(Math.cos(Math.toRadians(rotationYaw)));
			BlockPos posLeft = pos.add(x1, 0, z1), posRight = pos.add(x2, 0, z2);
			Block blockLeft = worldObj.getBlockState(posLeft).getBlock(),
					blockRight = worldObj.getBlockState(posRight).getBlock();
			boolean left = blockLeft instanceof BlockTrainTimer, right = blockRight instanceof BlockTrainTimer;

			if (left)
				((BlockTrainTimer) blockLeft).onTrainCollidedWithBlock(worldObj, posLeft,
						worldObj.getBlockState(posLeft), this);
			if (right)
				((BlockTrainTimer) blockRight).onTrainCollidedWithBlock(worldObj, posRight,
						worldObj.getBlockState(posRight), this);

			if ((left || right) && !powered) {
				if (leftDoor == -18 || rightDoor == -18)
					soundIn = MTRSounds.trainDooropen;
				if (leftDoor == 0 || rightDoor == 0)
					soundIn = doorOpen;
				if (leftDoor == 59 || rightDoor == 59)
					soundIn = MTRSounds.trainDoorclosetrain;
				setDoors(leftDoor > 0, rightDoor > 0);
				if (left && leftDoor < 60)
					leftDoor++;
				if (right && rightDoor < 60)
					rightDoor++;
			}
		}

		moveDoorBehind(soundIn);

		try {
			TileEntityNextTrainEntity te = (TileEntityNextTrainEntity) worldObj.getTileEntity(pos.down());
			PlatformData data = PlatformData.get(worldObj);
			for (int i = 0; i < 100; i++) {
				int platform = te.platformID[i];
				if (platform != 0)
					data.addTrain(platform, getUniqueID());
			}
		} catch (Exception e) {
		}
	}

	/** Called when the train goes over a reverse rail. Serverside only. **/
	protected void onReverseRailPass(BlockPos pos, boolean powered) {
		if (powered) {
			try {
				EntityTrainBase entityBehind = getEntityBehind();
				entityBehind.onReverseRailPass(pos, powered);
			} catch (Exception e) {
			}
			rotationYaw += 180;
			if (rotationYaw >= 360)
				rotationYaw -= 360;
			rotationPitch = -rotationPitch;
			radius = -radius;
			if (isNormalTrain)
				front = !front;
		}
	}

	private void getToGoalSpeed() {
		if (goalSpeed > mtrSpeed) {
			if (mtrSpeed + accleration <= goalSpeed)
				setSpeed(mtrSpeed + accleration);
			else
				setSpeed(goalSpeed);
			if (mtrSpeed == 0)
				setSpeed(0.05);
		}
		if (goalSpeed < mtrSpeed)
			if (mtrSpeed - accleration >= goalSpeed)
				setSpeed(mtrSpeed - accleration);
			else
				setSpeed(goalSpeed);
	}

	private void setTrainRotation(float yaw, float pitch) {
		if (mtrYaw != yaw) {
			mtrYaw = yaw;
			dataManager.set(MTR_YAW, yaw);
		}
		if (mtrPitch != pitch) {
			mtrPitch = pitch;
			dataManager.set(MTR_PITCH, pitch);
		}
	}

	private void setSpeed(double s) {
		float s2 = (float) s;
		if (mtrSpeed != s2) {
			mtrSpeed = s2;
			dataManager.set(MTR_SPEED, s2);
		}
	}

	private void setWheelID(int id) {
		if (front)
			id = -id;
		if (mtrWheelID != id) {
			mtrWheelID = id;
			dataManager.set(MTR_WHEELID, id);
		}
	}

	private void setConnectedID(int id) {
		if (mtrConnectedID != id) {
			mtrConnectedID = id;
			dataManager.set(MTR_CONNECTEDID, id);
		}
	}

	private void setDoors(boolean left, boolean right) {
		if (left != mtrDoorLeft) {
			mtrDoorLeft = left;
			dataManager.set(MTR_DOORLEFT, left);
		}
		if (right != mtrDoorRight) {
			mtrDoorRight = right;
			dataManager.set(MTR_DOORRIGHT, right);
		}
	}

	private void setGoals(double x, double y, double z) {
		goalX = x;
		goalY = y;
		goalZ = z;
	}

	private float getYaw() {
		return dataManager.get(MTR_YAW);
	}

	private float getPitch() {
		return dataManager.get(MTR_PITCH);
	}

	private float getSpeed() {
		return dataManager.get(MTR_SPEED);
	}

	private boolean getLeftDoor() {
		return dataManager.get(MTR_DOORLEFT);
	}

	private boolean getRightDoor() {
		return dataManager.get(MTR_DOORRIGHT);
	}

	private int getWheelID() {
		if (mtrWheelID == 0)
			mtrWheelID = dataManager.get(MTR_WHEELID);
		return mtrWheelID;
	}

	private int getConnectedID() {
		if (mtrConnectedID == 0)
			mtrConnectedID = dataManager.get(MTR_CONNECTEDID);
		return mtrConnectedID;
	}

	private int getHead() {
		if (mtrHead == 0)
			mtrHead = dataManager.get(MTR_HEAD);
		return mtrHead;
	}

	private void moveInStraightLine(double speed) {
		double a = MathTools.angleBetweenPoints(goalX, goalZ, posX, posZ);
		motionX = Math.sin(a) * speed;
		motionY = Math.signum(goalY - posY) * speed / 16D;
		motionZ = Math.cos(a) * speed;
		if (Math.abs(goalY - posY) < speed / 16D)
			motionY = goalY - posY;
	}

	private void moveInCurve(double speed) {
		rotationYaw += Math.toDegrees(speed / radius);
		if (rotationYaw < 0)
			rotationYaw += 360;
		if (rotationYaw >= 360)
			rotationYaw -= 360;
		int a = radius < 0 ? 180 : 0;
		int r = Math.abs(radius);
		double x = r * Math.sin(Math.toRadians(rotationYaw + a)) + centerX + 0.5;
		double z = r * Math.cos(Math.toRadians(rotationYaw + a)) + centerZ + 0.5;
		setGoals(x, posY, z);
		moveInStraightLine(speed);
	}

	private void setStraightGoals(int a, int x, int y, int z, int vertical) {
		double x1 = x, z1 = z;
		switch (a) {
		case 0:
			x1 = x + 16;
			z1 = z;
			break;
		case 45:
			x1 = x + 16;
			z1 = z - 16;
			break;
		case 90:
			x1 = x;
			z1 = z - 16;
			break;
		case 135:
			x1 = x - 16;
			z1 = z - 16;
			break;
		case 180:
			x1 = x - 16;
			z1 = z;
			break;
		case 225:
			x1 = x - 16;
			z1 = z + 16;
			break;
		case 270:
			x1 = x;
			z1 = z + 16;
			break;
		case 315:
			x1 = x + 16;
			z1 = z + 16;
			break;
		}
		x1 += 0.5;
		z1 += 0.5;
		rotationYaw = a;
		switch (vertical) {
		case 0:
			rotationPitch = 0;
			break;
		case 1:
			rotationPitch = 3.5763F;
			break;
		case -1:
			rotationPitch = -3.5763F;
			break;
		}
		setGoals(x1, y + vertical, z1);
	}

	private void setCurveGoals(BlockPos pos) {
		BlockPos switchPos = BlockRailCurved.getSwitchPos(worldObj, pos);
		int roundedYaw = MathTools.roundToNearest45(rotationYaw);
		if (switchPos != null && !worldObj.getBlockState(switchPos).getValue(BlockRailSwitch.POWERED)) {
			radius = 0;
			setStraightGoals(roundedYaw, pos.getX(), pos.getY(), pos.getZ(), 0);
		} else {
			TileEntityRailEntity te = (TileEntityRailEntity) worldObj.getTileEntity(pos);
			int r = te.radius;
			int xc = te.xc;
			int zc = te.zc;
			if (Math.abs(radius) != r || centerX != xc || centerZ != zc) {
				int ac = te.angleChange;
				int sig = (int) Math.signum(ac);
				radius = r * (roundedYaw == te.startAngle ^ ac < 0 ? sig : -sig);
				centerX = xc;
				centerZ = zc;
			}
		}
	}

	private void readBlock(int blockX, int blockY, int blockZ) {
		BlockPos pos = new BlockPos(blockX, blockY, blockZ);
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof BlockRailStraight || block instanceof BlockRailBooster
				|| block instanceof BlockRailDetector2 || block instanceof BlockRailStation
				|| block instanceof BlockRailReverse || radius == 0 && block instanceof BlockRailDummy
				|| radius == 0 && block instanceof BlockRailIntersection) {
			// straight rails
			radius = 0;
			int railAngle1, railAngle2;
			int roundedYaw = MathTools.roundToNearest45(rotationYaw);
			if (block instanceof BlockRailIntersection)
				railAngle1 = railAngle2 = roundedYaw;
			else {
				int i = state.getValue(block instanceof BlockRailBooster ? BlockRailBooster.ROTATION
						: block instanceof BlockRailDummy ? BlockRailDummy.ROTATION : BlockRailBase2.ROTATION);
				railAngle1 = (int) Math.round((-45D * i + 630D) % 180);
				railAngle2 = railAngle1 + 180;
				if (Math.abs(MathTools.angleDifference(roundedYaw, railAngle1)) >= 20
						&& Math.abs(MathTools.angleDifference(roundedYaw, railAngle2)) >= 20)
					railAngle1 = railAngle2 = roundedYaw;
			}

			boolean powered = worldObj.isBlockPowered(pos);
			if (block instanceof BlockRailReverse && front && entityConnected == null)
				onReverseRailPass(pos, powered);

			setStraightGoals((int) Math.round(MathTools.findCloserAngle(rotationYaw, railAngle1, railAngle2)), blockX,
					blockY, blockZ, 0);

			if (block instanceof BlockRailStraight)
				onStraightRailPass(pos);
			if (block instanceof BlockRailBooster)
				onBoosterRailPass(pos, powered);
			if (block instanceof BlockRailDetector2) {
				((BlockRailDetector2) block).customCollide(worldObj, pos);
				onDetectorRailPass(pos);
			}
			if (block instanceof BlockRailStation && front && entityConnected == null)
				onStationRailPass(pos, worldObj.isBlockPowered(pos));
		} else if (block instanceof BlockRailCurved)
			// curved rail
			setCurveGoals(pos);
		else if (block instanceof BlockRailSlope1) {
			// slope rail 1
			radius = 0;
			int var3 = worldObj.getBlockState(pos.add(0, 1, 0)).getValue(BlockRailSlope2.ROTATION);
			int angle = (int) (-45D * var3 + 450D) % 360;
			if (rotationYaw == angle)
				setStraightGoals(angle, blockX, blockY, blockZ, 1);
		} else if (block instanceof BlockRailSlope2) {
			// slope rail 2
			radius = 0;
			int var3 = state.getValue(BlockRailSlope2.ROTATION) + 4;
			int angle = (int) (-45D * var3 + 450D) % 360;
			if (rotationYaw == angle)
				setStraightGoals(angle, blockX, blockY, blockZ, -1);
		}
	}

	private void moveDoorBehind(SoundEvent soundIn) {
		try {
			EntityTrainBase entityBack;
			broadcastSound(soundIn);
			entityBack = getEntityBehind();
			entityBack.leftDoor = leftDoor;
			entityBack.rightDoor = rightDoor;
			entityBack.setDoors(mtrDoorLeft, mtrDoorRight);
			entityBack.moveDoorBehind(soundIn);
		} catch (Exception e) {
		}
	}

	private void broadcastSound(SoundEvent soundIn) {
		EntityPlayer player = worldObj.getClosestPlayerToEntity(this, 16);
		if (player != null)
			worldObj.playSound(null, player.getPosition(), soundIn, SoundCategory.RECORDS, 1000000, 1);
	}

	private EntityTrainBase getEntityWheel() {
		if (entityWheel == null) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
			try {
				entityWheel = (EntityTrainBase) server.getEntityFromUuid(uuidWheel);
				setWheelID(entityWheel.getEntityId());
				dataManager.set(MTR_HEAD, mtrHead);
			} catch (Exception e) {
			}
		}
		return entityWheel;
	}

	private EntityTrainBase getEntityConnected() {
		if (entityConnected == null) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
			try {
				entityConnected = (EntityTrainBase) server.getEntityFromUuid(uuidConnected);
				setConnectedID(entityConnected.getEntityId());
			} catch (Exception e) {
				setConnectedID(-1);
			}
		}
		return entityConnected;
	}

	private EntityTrainBase getEntityBehind() {
		try {
			EntityTrainBase entityBack;
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
			if (front)
				entityBack = (EntityTrainBase) server.getEntityFromUuid(uuidWheel);
			else
				entityBack = (EntityTrainBase) server.getEntityFromUuid(uuidConnected);
			return entityBack;
		} catch (Exception e) {
			return null;
		}
	}

	private void loadChunks(int blockX, int blockY, int blockZ) {
		if (trainTicket == null) {
			try {
				for (Ticket ticket : allTickets) {
					int x = ticket.getModData().getInteger("trainX"), y = ticket.getModData().getInteger("trainY"),
							z = ticket.getModData().getInteger("trainZ");
					if (worldObj.getEntitiesWithinAABB(EntityTrainBase.class,
							new AxisAlignedBB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1)).isEmpty()) {
						ForgeChunkManager.releaseTicket(ticket);
						System.out.println("Released ticket " + ticket);
					}
				}
			} catch (Exception e) {
			}
			trainTicket = ForgeChunkManager.requestTicket(MTR.instance, worldObj, ForgeChunkManager.Type.NORMAL);
			System.out
					.println(ForgeChunkManager.ticketCountAvailableFor(MTR.instance, worldObj) + " tickets remaining");
		}
		if (trainTicket != null) {
			int chunkX = blockX >> 4, chunkZ = blockZ >> 4;
			trainTicket.getModData().setInteger("x", chunkX);
			trainTicket.getModData().setInteger("z", chunkZ);
			trainTicket.getModData().setInteger("trainX", blockX);
			trainTicket.getModData().setInteger("trainY", blockY);
			trainTicket.getModData().setInteger("trainZ", blockZ);
			ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX, chunkZ));
			if (front) {
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX + 1, chunkZ));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX + 1, chunkZ + 1));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX, chunkZ + 1));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX - 1, chunkZ + 1));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX - 1, chunkZ));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX - 1, chunkZ - 1));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX, chunkZ - 1));
				ForgeChunkManager.forceChunk(trainTicket, new ChunkPos(chunkX + 1, chunkZ - 1));
			}
		}
	}

	public void killTrain() {
		setDead();
		try {
			EntityTrainBase wheel = getEntityWheel();
			EntityTrainBase connected = getEntityConnected();
			if (!wheel.isDead)
				wheel.killTrain();
			if (!connected.isDead)
				connected.killTrain();
		} catch (Exception e) {
		}
		ForgeChunkManager.releaseTicket(trainTicket);
		System.out.println(ForgeChunkManager.ticketCountAvailableFor(MTR.instance, worldObj) + " tickets remaining");
	}

	public int getTrainLength() {
		return 0;
	}

	public void setID(UUID wheel, UUID connected) {
		uuidWheel = wheel;
		uuidConnected = connected;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
	}

	@Override
	public double getMountedYOffset() {
		return 1D;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
		if (!worldObj.isRemote && stack != null && stack.getItem() instanceof ItemKillTrain)
			killTrain();
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
		dataManager.register(MTR_DOORLEFT, false);
		dataManager.register(MTR_DOORRIGHT, false);
		dataManager.register(MTR_WHEELID, 0);
		dataManager.register(MTR_CONNECTEDID, 0);
		dataManager.register(MTR_HEAD, 0);
		dataManager.register(MTR_YAW, 0F);
		dataManager.register(MTR_PITCH, 0F);
		dataManager.register(MTR_SPEED, 0F);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		setSpeed(nbt.getDouble("speed"));
		setGoals(nbt.getDouble("goalX"), nbt.getDouble("goalY"), nbt.getDouble("goalZ"));
		goalSpeed = nbt.getDouble("goalSpeed");
		radius = nbt.getInteger("radius");
		centerX = nbt.getInteger("centerX");
		centerZ = nbt.getInteger("centerZ");
		front = nbt.getBoolean("front");
		mtrHead = nbt.getInteger("head");
		setID(nbt.getUniqueId("wheel"), nbt.getUniqueId("connected"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("speed", getSpeed());
		compound.setDouble("goalX", goalX);
		compound.setDouble("goalY", goalY);
		compound.setDouble("goalZ", goalZ);
		compound.setDouble("goalSpeed", goalSpeed);
		compound.setInteger("radius", radius);
		compound.setInteger("centerX", centerX);
		compound.setInteger("centerZ", centerZ);
		compound.setBoolean("front", front);
		compound.setInteger("head", mtrHead);
		compound.setUniqueId("wheel", uuidWheel);
		compound.setUniqueId("connected", uuidConnected);
		return compound;
	}

	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
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
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			int x = ticket.getModData().getInteger("x"), z = ticket.getModData().getInteger("z");
			if (ForgeChunkManager.getPersistentChunksFor(world).containsKey(new ChunkPos(x, z))) {
				ForgeChunkManager.releaseTicket(ticket);
				System.out.println("Released ticket " + ticket);
			} else {
				ForgeChunkManager.forceChunk(ticket, new ChunkPos(x, z));
				System.out.println("Loaded chunk " + x + ", " + z);
			}
		}
		System.out.println(ForgeChunkManager.ticketCountAvailableFor(MTR.instance, world) + " tickets remaining");
		allTickets = tickets;
	}
}
