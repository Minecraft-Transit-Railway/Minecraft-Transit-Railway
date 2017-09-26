package MTR;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlatformData extends WorldSavedData {

	private static final String name = "MTRPlatformData";

	public int[] platformX = new int[1000];
	public int[] platformY = new int[1000];
	public int[] platformZ = new int[1000];
	public int[] platformAlias = new int[1000];
	public int[] platformNumber = new int[1000];
	public UUID[] arrivals = new UUID[5000];

	public PlatformData() {
		super(name);
	}

	public PlatformData(String s) {
		super(s);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setIntArray("platformX", platformX);
		compound.setIntArray("platformY", platformY);
		compound.setIntArray("platformZ", platformZ);
		compound.setIntArray("platformAlias", platformAlias);
		compound.setIntArray("platformNumber", platformNumber);
		for (int i = 0; i < 5000; i++)
			compound.setUniqueId("train" + i, arrivals[i]);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		platformX = nbt.getIntArray("platformX");
		platformY = nbt.getIntArray("platformY");
		platformZ = nbt.getIntArray("platformZ");
		platformAlias = nbt.getIntArray("platformAlias");
		platformNumber = nbt.getIntArray("platformNumber");
		for (int i = 0; i < 5000; i++)
			arrivals[i] = nbt.getUniqueId("train" + i);
		try {
			int a = platformX[0] + platformY[0] + platformZ[0] + platformAlias[0] + platformNumber[0];
		} catch (Exception e) {
			platformX = new int[1000];
			platformY = new int[1000];
			platformZ = new int[1000];
			platformAlias = new int[1000];
			platformNumber = new int[1000];
		}
	}

	public static PlatformData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		PlatformData instance = (PlatformData) storage.getOrLoadData(PlatformData.class, name);
		if (instance == null) {
			instance = new PlatformData();
			storage.setData(name, instance);
		}
		return instance;
	}

	public void addTrain(int platform, UUID uuid) {
		platform--;
		if (platform >= 0) {
			arrivals[platform * 5] = uuid;
			markDirty();
		}
	}

	public int getArrival(int platform, int arrivalNum) {
		platform--;
		if (platform >= 0) {
			int x = platformX[platform], y = platformY[platform], z = platformZ[platform];
			UUID uuid = arrivals[platform * 5 + arrivalNum];
			if (uuid != null && uuid != new UUID(0, 0)) {
				Entity train = FMLCommonHandler.instance().getMinecraftServerInstance().getServer()
						.getEntityFromUuid(uuid);
				if (train instanceof EntityTrainBase) {
					EntityTrainBase train2 = (EntityTrainBase) train;
					double distance = MathTools.distanceBetweenPoints(x, y, z, train.posX, train.posY, train.posZ);
					if (distance < 10)
						train2.arrivedAtStation = true;
					if (distance >= 10 && train2.arrivedAtStation) {
						arrivals = shiftUp(arrivals, platform);
						train2.arrivedAtStation = false;
					}
					return (int) Math.round(distance / 300); // 5m/s
				}
			}
		}
		return -1;
	}

	private UUID[] shiftUp(UUID[] array, int platform) {
		for (int i = platform * 5; i < platform * 5 + 3; i++)
			array[i] = array[i + 1];
		array[platform * 5 + 4] = new UUID(0, 0);
		return array;
	}
}
