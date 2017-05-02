package MTR;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRouteChangerEntity extends TileEntity {

	int route;

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new S35PacketUpdateTileEntity(pos, metadata, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound) {
		super.writeToNBT(parentNBTTagCompound);
		parentNBTTagCompound.setInteger("route", route);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition = -1;
		if (parentNBTTagCompound.hasKey("route", NBT_INT_ID)) {
			readPosition = parentNBTTagCompound.getInteger("route");
			if (readPosition < 0)
				readPosition = -1;
		}
		route = readPosition;
	}
}
