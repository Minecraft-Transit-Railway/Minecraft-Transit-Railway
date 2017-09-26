package MTR;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStationNameEntity extends TileEntity {

	int station;

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
		super.writeToNBT(parentNBTTagCompound);
		parentNBTTagCompound.setInteger("station", station);
		return parentNBTTagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition = -1;
		if (parentNBTTagCompound.hasKey("station", NBT_INT_ID)) {
			readPosition = parentNBTTagCompound.getInteger("station");
			if (readPosition < 0)
				readPosition = -1;
		}
		station = readPosition;
		if (station < 0 || station > 99)
			station = 0;
	}
}
