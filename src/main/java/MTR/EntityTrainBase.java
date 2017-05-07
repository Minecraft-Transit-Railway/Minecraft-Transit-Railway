package MTR;

import java.util.List;
import java.util.UUID;

import MTR.blocks.BlockRailBase2;
import MTR.blocks.BlockRailBooster;
import MTR.blocks.BlockRailCurved;
import MTR.blocks.BlockRailDetector2;
import MTR.blocks.BlockRailSlope1;
import MTR.blocks.BlockRailSlope2;
import MTR.blocks.BlockRailStation;
import MTR.blocks.BlockRailStraight;
import MTR.blocks.BlockTrainTimer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class EntityTrainBase extends Entity implements LoadingCallback {

	private int turnProgress;
	private double trainX, trainY, trainZ;
	private double trainYaw, trainPitch;
	private double velocityX, velocityY, velocityZ;

	private static final double accleration = 0.01;
	public double clientYaw;
	protected double clientYawOld;
	public double clientPitch;
	protected double clientX;
	protected double clientY;
	protected double clientZ;
	protected UUID carFront, carBack;
	private double goalX, goalY, goalZ, goalSpeed;
	private int radius, wait;
	private Ticket trainTicket;
	private static List<Ticket> allTickets;
	protected String name = "";

	public EntityTrainBase(World worldIn) {
		super(worldIn);
		setSize(2F, 4F);
		noClip = true;
		ignoreFrustumCheck = true;
		preventEntitySpawning = true;
	}

	public EntityTrainBase(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x, y, z);
		motionX = 0D;
		motionY = 0D;
		motionZ = 0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
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

		EntityTrainBase entityFront = null, entityBack = null;
		int headOrTail = getHeadOrTail();
		try {
			MinecraftServer server = MinecraftServer.getServer();

			// 1-0-2 // 3
			if (headOrTail != 1 && headOrTail != 3) {
				entityFront = (EntityTrainBase) server.getEntityFromUuid(carFront);
				if (!(entityFront instanceof EntityTrainBase))
					killTrain();
			}
			if (headOrTail != 2 && headOrTail != 3) {
				entityBack = (EntityTrainBase) server.getEntityFromUuid(carBack);
				if (!(entityBack instanceof EntityTrainBase))
					killTrain();
			}

		} catch (Exception e) {
			killTrain();
		}

		int blockX = MathHelper.floor_double(posX);
		int blockY = (int) Math.round(posY);
		int blockZ = MathHelper.floor_double(posZ);
		BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
		Block block = worldObj.getBlockState(blockPos).getBlock();

		loadChunks(blockPos);

		if (block instanceof BlockRailStraight || block instanceof BlockRailDetector2
				|| block instanceof BlockRailStation) {
			// straight rails
			radius = 0;
			int i = worldObj.getBlockState(blockPos).getValue(BlockRailBase2.ROTATION);
			int railAngle1 = (int) Math.round((-45D * i + 270D) % 180);
			int railAngle2 = railAngle1 + 180;
			setStraightGoals((int) Math.round(findCloserAngle(rotationYaw, railAngle1, railAngle2)), blockX, blockY,
					blockZ, 0);

			if (block instanceof BlockRailStraight)
				onStraightRailPass(blockPos);
			if (block instanceof BlockRailDetector2) {
				((BlockRailDetector2) block).customCollide(worldObj, blockPos);
				onDetectorRailPass(blockPos);
			}
			if (block instanceof BlockRailStation && (headOrTail == 1 || headOrTail == 3))
				onStationRailPass(blockPos, worldObj.isBlockPowered(blockPos));
		} else if (block instanceof BlockRailCurved)
			// curved rail
			setCurveGoals(blockPos);
		else if (block instanceof BlockRailBooster) {
			// booster rail
			radius = 0;
			onBoosterRailPass(blockPos, worldObj.isBlockPowered(blockPos));
		} else if (block instanceof BlockRailSlope1) {
			// slope rail 1
			radius = 0;
			int var3 = worldObj.getBlockState(blockPos.add(0, 1, 0)).getValue(BlockRailSlope2.ROTATION);
			int angle = (int) (-45D * var3 + 450D) % 360;
			if (rotationYaw == angle)
				setStraightGoals(angle, blockX, blockY, blockZ, 1);
		} else if (block instanceof BlockRailSlope2) {
			// slope rail 2
			radius = 0;
			int var3 = worldObj.getBlockState(blockPos).getValue(BlockRailSlope2.ROTATION) + 4;
			int angle = (int) (-45D * var3 + 450D) % 360;
			if (rotationYaw == angle)
				setStraightGoals(angle, blockX, blockY, blockZ, -1);
		}

		// if connected car is too far away
		if (headOrTail != 1 && headOrTail != 3)
			try {
				double distance = Math.sqrt(Math.pow(entityFront.posX - posX, 2) + Math.pow(entityFront.posY - posY, 2)
						+ Math.pow(entityFront.posZ - posZ, 2));
				setSpeed(entityFront.getSpeed() * Math.pow(2, distance - getTrainLength()));
				goalSpeed = getSpeed();
				if (distance > getTrainLength() + 4)
					goalSpeed = 0;
			} catch (Exception e) {
				goalSpeed = 0;
			}

		getToGoalSpeed();

		if (radius == 0)
			moveInStraightLine();
		else
			moveInCurve();

		if (riddenByEntity == null && (getLeftDoor() > 0 || getRightDoor() > 0)) {
			List<Entity> e = worldObj.getEntitiesWithinAABB(EntityPlayer.class,
					AxisAlignedBB.fromBounds(posX - 10, posY - 3, posZ - 10, posX + 10, posY + 5, posZ + 10));
			if (e.size() > 0) {
				double yaw = Math.toRadians(rotationYaw);
				EntityPlayer entity = (EntityPlayer) e.get(0);
				int a = 0, b = 0;
				if (this instanceof EntityLightRail1)
					a = 6;
				if (this instanceof EntitySP1900) {
					a = 8;
					b = 4;
				}
				BlockPos pos0 = entity.getPosition();
				BlockPos pos1 = new BlockPos(posX + a * Math.cos(yaw), posY, posZ - a * Math.sin(yaw));
				BlockPos pos2 = new BlockPos(posX + b * Math.cos(yaw), posY, posZ - b * Math.sin(yaw));
				BlockPos pos3 = new BlockPos(posX - b * Math.cos(yaw), posY, posZ + b * Math.sin(yaw));
				BlockPos pos4 = new BlockPos(posX - a * Math.cos(yaw), posY, posZ + a * Math.sin(yaw));
				BlockPos pos5 = new BlockPos(posX, posY, posZ);
				double d1 = findHorizontalDistance(pos1, pos0);
				double d2 = findHorizontalDistance(pos2, pos0);
				double d3 = findHorizontalDistance(pos3, pos0);
				double d4 = findHorizontalDistance(pos4, pos0);
				double d5 = findHorizontalDistance(pos5, pos0);
				if (d1 <= 1.5 || d2 <= 1.5 || d3 <= 1.5 || d4 <= 1.5 || d5 <= 1.5)
					entity.mountEntity(this);
			}
		}

		moveEntity(motionX, motionY, motionZ);
		setRotation(rotationYaw, rotationPitch);
		setTrainRotation(rotationYaw, rotationPitch);
	}

	private void clientOnUpdate() {
		rotationYaw = getYaw();
		rotationPitch = getPitch();

		double s1 = getSpeed() / 3D;
		if (Math.abs(rotationPitch - clientPitch) < s1)
			clientPitch = rotationPitch;
		if (clientPitch < rotationPitch)
			clientPitch += s1;
		if (clientPitch > rotationPitch)
			clientPitch -= s1;

		if (turnProgress > 0) {
			clientX = posX + (trainX - posX) / turnProgress;
			clientY = posY + (trainY - posY) / turnProgress;
			clientZ = posZ + (trainZ - posZ) / turnProgress;

			double angleDifference = Math.abs(clientYaw - rotationYaw);
			if (angleDifference > 180)
				angleDifference = 360 - angleDifference;
			if (angleDifference > 30)
				clientYaw = rotationYaw;
			else {
				double s2 = angleDifference / turnProgress;
				clientYaw = (float) findCloserAngle((rotationYaw + 360) % 360, (clientYaw + 360) % 360 + s2,
						(clientYaw + 360) % 360 - s2);
			}

			--turnProgress;
			setPosition(clientX, clientY, clientZ);
			setRotation((float) clientYaw, (float) clientPitch);
		} else {
			clientX = posX;
			clientY = posY;
			clientZ = posZ;
			clientYaw = (rotationYaw + 360) % 360;
			setPosition(posX, posY, posZ);
			setRotation((float) clientYaw, (float) clientPitch);
		}

		if (riddenByEntity != null) {
			riddenByEntity.rotationYaw += (float) clientYawOld - clientYaw;
			float speed1 = Math.round(getSpeed() * 200) / 10F, speed2 = Math.round(getSpeed() * 720) / 10F;
			String speedText = I18n.format("gui.trainspeed", new Object[0]) + " " + speed1 + "m/s (" + speed2 + "km/h)";
			String mountText = I18n.format("mount.onboard", new Object[] { GameSettings
					.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) });
			Minecraft.getMinecraft().ingameGUI
					.setRecordPlaying(getLeftDoor() != 0 || getRightDoor() != 0 ? mountText : speedText, false);
		}

		clientYawOld = clientYaw;
	}

	/** Called when the train goes over a straight rail. Serverside only. **/
	protected void onStraightRailPass(BlockPos pos) {
	}

	/** Called when the train goes over a detector rail. Serverside only. **/
	protected void onDetectorRailPass(BlockPos pos) {
	}

	/** Called when the train goes over a booster rail. Serverside only. **/
	protected void onBoosterRailPass(BlockPos pos, boolean powered) {
		IBlockState state = worldObj.getBlockState(pos);
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
		int var3 = state.getValue(BlockRailBooster.ROTATION);
		int angle = (int) (-45D * var3 + 450D) % 360;
		setStraightGoals(angle, pos.getX(), pos.getY(), pos.getZ(), 0);
	}

	/** Called when the train goes over a station rail. Serverside only. **/
	protected void onStationRailPass(BlockPos pos, boolean powered) {
		String playsound = "";

		if (powered && getSpeed() == 0) {
			if (getLeftDoor() == 0 && getRightDoor() == 0 && wait == 0)
				goalSpeed = 0.25;
			else if (getLeftDoor() == 0 && getRightDoor() == 0)
				wait--;
			else {
				if (getLeftDoor() == 60 || getRightDoor() == 60)
					playsound = name + ".DoorClose";
				if (getLeftDoor() > 0)
					setDoors(getLeftDoor() - 1, getRightDoor());
				if (getRightDoor() > 0)
					setDoors(getLeftDoor(), getRightDoor() - 1);
			}
		} else if (!powered) {
			goalSpeed = 0;
			setSpeed(0);
		}

		int x1 = (int) Math.round(Math.sin(Math.toRadians(rotationYaw + 180)));
		int z1 = (int) Math.round(Math.cos(Math.toRadians(rotationYaw + 180)));
		int x2 = (int) Math.round(Math.sin(Math.toRadians(rotationYaw)));
		int z2 = (int) Math.round(Math.cos(Math.toRadians(rotationYaw)));
		BlockPos posLeft = pos.add(x1, 0, z1), posRight = pos.add(x2, 0, z2);
		Block blockLeft = worldObj.getBlockState(posLeft).getBlock(),
				blockRight = worldObj.getBlockState(posRight).getBlock();
		boolean left = blockLeft instanceof BlockTrainTimer, right = blockRight instanceof BlockTrainTimer;

		if (left)
			((BlockTrainTimer) blockLeft).onTrainCollidedWithBlock(worldObj, posLeft, worldObj.getBlockState(posLeft),
					this);
		if (right)
			((BlockTrainTimer) blockRight).onTrainCollidedWithBlock(worldObj, posRight,
					worldObj.getBlockState(posRight), this);

		if ((left || right) && !powered)
			if (wait < 20) {
				if (wait == 2)
					playsound = "Train.DoorOpen";
				if (wait == 19)
					playsound = name + ".DoorOpen";
				wait++;
			} else {
				if (left && getLeftDoor() < 60)
					setDoors(getLeftDoor() + 1, getRightDoor());
				if (right && getRightDoor() < 60)
					setDoors(getLeftDoor(), getRightDoor() + 1);
			}

		moveDoorBehind(playsound);
	}

	private void getToGoalSpeed() {
		if (goalSpeed > getSpeed()) {
			if (getSpeed() + accleration <= goalSpeed)
				setSpeed(getSpeed() + accleration);
			else
				setSpeed(goalSpeed);
			if (getSpeed() == 0)
				setSpeed(0.05);
		}
		if (goalSpeed < getSpeed())
			if (getSpeed() - accleration >= goalSpeed)
				setSpeed(getSpeed() - accleration);
			else
				setSpeed(goalSpeed);
	}

	private void loadChunks(BlockPos pos) {
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
			int chunkX = pos.getX() >> 4, chunkZ = pos.getZ() >> 4;
			int chunkXplus = pos.getX() + 3 >> 4;
			trainTicket.getModData().setInteger("x", chunkX);
			trainTicket.getModData().setInteger("z", chunkZ);
			trainTicket.getModData().setInteger("trainX", pos.getX());
			trainTicket.getModData().setInteger("trainY", pos.getY());
			trainTicket.getModData().setInteger("trainZ", pos.getZ());
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX, chunkZ));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX + 1, chunkZ));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX + 1, chunkZ + 1));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX, chunkZ + 1));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX - 1, chunkZ + 1));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX - 1, chunkZ));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX - 1, chunkZ - 1));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX, chunkZ - 1));
			ForgeChunkManager.forceChunk(trainTicket, new ChunkCoordIntPair(chunkX + 1, chunkZ - 1));
		}
	}

	private void setTrainRotation(float yaw, float pitch) {
		dataWatcher.updateObject(26, yaw);
		dataWatcher.updateObject(27, pitch);
	}

	private void setSpeed(double s) {
		dataWatcher.updateObject(28, (float) s);
	}

	private void setGoals(double x, double y, double z) {
		goalX = x;
		goalY = y;
		goalZ = z;
	}

	private void setHeadOrTail() {
		// 1-0-2 // 3
		int a = 0, front = 0, back = 0;
		if (carBack.getMostSignificantBits() == 0)
			a += 2;
		if (carFront.getMostSignificantBits() == 0)
			a += 1;
		dataWatcher.updateObject(20, a);
	}

	protected void setDoors(int left, int right) {
		dataWatcher.updateObject(24, left);
		dataWatcher.updateObject(25, right);
	}

	private float getYaw() {
		return dataWatcher.getWatchableObjectFloat(26);
	}

	private float getPitch() {
		return dataWatcher.getWatchableObjectFloat(27);
	}

	private double getSpeed() {
		return dataWatcher.getWatchableObjectFloat(28);
	}

	public int getLeftDoor() {
		return dataWatcher.getWatchableObjectInt(24);
	}

	public int getRightDoor() {
		return dataWatcher.getWatchableObjectInt(25);
	}

	public int getHeadOrTail() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	private void moveInStraightLine() {
		double distance = Math.sqrt(Math.pow(goalX - posX, 2) + Math.pow(goalZ - posZ, 2));
		double a = Math.acos((goalZ - posZ) / distance);
		if (goalX < posX)
			a = 2D * Math.PI - a;
		motionX = Math.sin(a) * getSpeed();
		motionY = Math.signum(goalY - posY) * getSpeed() / 16D;
		motionZ = Math.cos(a) * getSpeed();
		if (distance < getSpeed()) {
			motionX = goalX - posX;
			motionZ = goalZ - posZ;
		}
		if (Math.abs(goalY - posY) < getSpeed() / 16D)
			motionY = goalY - posY;
	}

	private void moveInCurve() {
		rotationYaw += Math.toDegrees(getSpeed() / radius);
		if (rotationYaw < 0)
			rotationYaw += 360;
		if (rotationYaw >= 360)
			rotationYaw -= 360;
		int a = radius < 0 ? 180 : 0;
		motionX = Math.abs(radius) * Math.sin(Math.toRadians(rotationYaw + a)) + goalX - posX;
		motionY = 0;
		motionZ = Math.abs(radius) * Math.cos(Math.toRadians(rotationYaw + a)) + goalZ - posZ;
	}

	private double findCloserAngle(double angle, double a1d, double a2d) {
		double b1 = Math.abs(a1d - angle), b2 = Math.abs(a2d - angle);
		if (b1 > 180)
			b1 = 360 - b1;
		if (b2 > 180)
			b2 = 360 - b2;
		if (b2 < b1)
			return a2d;
		else
			return a1d;
	}

	private double findHorizontalDistance(BlockPos pos1, BlockPos pos2) {
		return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getZ() - pos2.getZ(), 2));
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
		double x = 0, z = 0, x1 = 0, z1 = 0, x2 = 0, z2 = 0;
		radius = 1;
		BlockPos blockPos0 = pos;
		BlockPos left = pos, right = pos;
		double temp = Math.sqrt(2) / 2D;
		if (rotationYaw >= 337.5 || rotationYaw < 22.5) { // 0
			left = pos.add(0, 0, -1);
			right = pos.add(0, 0, 1);
			z1 = -1;
			z2 = 1;
		} else if (rotationYaw >= 22.5 && rotationYaw < 67.5) { // 45
			left = pos.add(-1, 0, -1);
			right = pos.add(1, 0, 1);
			x1 = -temp;
			x2 = temp;
			z1 = -temp;
			z2 = temp;
		} else if (rotationYaw >= 67.5 && rotationYaw < 112.5) { // 90
			left = pos.add(-1, 0, 0);
			right = pos.add(1, 0, 0);
			x1 = -1;
			x2 = 1;
		} else if (rotationYaw >= 112.5 && rotationYaw < 157.5) { // 135
			left = pos.add(-1, 0, 1);
			right = pos.add(1, 0, -1);
			x1 = -temp;
			x2 = temp;
			z1 = temp;
			z2 = -temp;
		} else if (rotationYaw >= 157.5 && rotationYaw < 202.5) { // 180
			left = pos.add(0, 0, 1);
			right = pos.add(0, 0, -1);
			z1 = 1;
			z2 = -1;
		} else if (rotationYaw >= 202.5 && rotationYaw < 247.5) { // 225
			left = pos.add(1, 0, 1);
			right = pos.add(-1, 0, -1);
			x1 = temp;
			x2 = -temp;
			z1 = temp;
			z2 = -temp;
		} else if (rotationYaw >= 247.5 && rotationYaw < 292.5) { // 270
			left = pos.add(1, 0, 0);
			right = pos.add(-1, 0, 0);
			x1 = 1;
			x2 = -1;
		} else if (rotationYaw >= 292.5 && rotationYaw < 337.5) { // 315
			left = pos.add(1, 0, -1);
			right = pos.add(-1, 0, 1);
			x1 = temp;
			x2 = -temp;
			z1 = -temp;
			z2 = temp;
		}
		if (worldObj.getBlockState(left).getBlock() instanceof BlockRailCurved) {
			blockPos0 = left;
			x = x1;
			z = z1;
		}
		if (worldObj.getBlockState(right).getBlock() instanceof BlockRailCurved) {
			blockPos0 = right;
			x = x2;
			z = z2;
			radius = -1;
		}
		if (pos != blockPos0) {
			int a = worldObj.getBlockState(pos).getValue(BlockRailCurved.ROTATION);
			int b = worldObj.getBlockState(blockPos0).getValue(BlockRailCurved.ROTATION);
			radius *= a + 16 * b;
			setGoals(pos.getX() + x * Math.abs(radius) + 0.5, pos.getY(), pos.getZ() + z * Math.abs(radius) + 0.5);
		}
	}

	private boolean testBlock(BlockPos pos) {
		Block block = worldObj.getBlockState(pos).getBlock();
		if (block instanceof BlockRailBase2 && !(block instanceof BlockRailCurved)) {
			setGoals(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			return true;
		}
		return false;
	}

	private void moveDoorBehind(String playsound) {
		if (playsound != "" && !playsound.startsWith("."))
			worldObj.playSoundEffect(posX, posY, posZ, "mtr:" + playsound, 1F, 1F);
		try {
			EntityTrainBase entityBack = (EntityTrainBase) MinecraftServer.getServer().getEntityFromUuid(carBack);
			entityBack.setDoors(getLeftDoor(), getRightDoor());
			entityBack.moveDoorBehind(playsound);
		} catch (Exception e) {
		}
	}

	public void killTrain() {
		setDead();
		ForgeChunkManager.releaseTicket(trainTicket);
		System.out.println(ForgeChunkManager.ticketCountAvailableFor(MTR.instance, worldObj) + " tickets remaining");
	}

	protected int getTrainLength() {
		return 0;
	}

	public void setID(UUID f, UUID b) {
		carFront = f;
		carBack = b;
		setHeadOrTail();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer playerIn) {
		if (!worldObj.isRemote && Math.abs(playerIn.posX - posX) < 1.5 && Math.abs(playerIn.posZ - posZ) < 1.5
				&& (getLeftDoor() > 0 || getRightDoor() > 0) && riddenByEntity == null)
			playerIn.mountEntity(this);
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
	public boolean interactFirst(EntityPlayer playerIn) {
		ItemStack itemStack = playerIn.getHeldItem();
		if (!worldObj.isRemote && itemStack != null && itemStack.getItem() == MTR.itemkilltrain)
			killTrain();
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(20, new Integer(0)); // 0=middle, 1=front, 2=back
		dataWatcher.addObject(24, new Integer(0)); // movedoor left
		dataWatcher.addObject(25, new Integer(0)); // movedoor right
		dataWatcher.addObject(26, new Float(0F)); // yaw
		dataWatcher.addObject(27, new Float(0F)); // pitch
		dataWatcher.addObject(28, new Float(0F)); // speed
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		setSpeed(tagCompound.getDouble("speed"));
		setGoals(tagCompound.getDouble("goalX"), tagCompound.getDouble("goalY"), tagCompound.getDouble("goalZ"));
		goalSpeed = tagCompound.getDouble("goalSpeed");
		radius = tagCompound.getInteger("radius");
		setID(new UUID(tagCompound.getLong("fronta"), tagCompound.getLong("frontb")),
				new UUID(tagCompound.getLong("backa"), tagCompound.getLong("backb")));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setDouble("speed", getSpeed());
		tagCompound.setDouble("goalX", goalX);
		tagCompound.setDouble("goalY", goalY);
		tagCompound.setDouble("goalZ", goalZ);
		tagCompound.setDouble("goalSpeed", goalSpeed);
		tagCompound.setInteger("radius", radius);
		tagCompound.setLong("fronta", carFront.getMostSignificantBits());
		tagCompound.setLong("frontb", carFront.getLeastSignificantBits());
		tagCompound.setLong("backa", carBack.getMostSignificantBits());
		tagCompound.setLong("backb", carBack.getLeastSignificantBits());
	}

	@Override
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean p_180426_10_) {
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
			if (ForgeChunkManager.getPersistentChunksFor(world).containsKey(new ChunkCoordIntPair(x, z))) {
				ForgeChunkManager.releaseTicket(ticket);
				System.out.println("Released ticket " + ticket);
			} else {
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x, z));
				System.out.println("Loaded chunk " + x + ", " + z);
			}
		}
		System.out.println(ForgeChunkManager.ticketCountAvailableFor(MTR.instance, world) + " tickets remaining");
		allTickets = tickets;
	}
}
