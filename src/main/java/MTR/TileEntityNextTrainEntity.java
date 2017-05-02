package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityNextTrainEntity extends TileEntity {

	int x, y, z;

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
		parentNBTTagCompound.setInteger("x", x);
		parentNBTTagCompound.setInteger("y", y);
		parentNBTTagCompound.setInteger("z", z);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition1 = -1;
		if (parentNBTTagCompound.hasKey("x", NBT_INT_ID)) {
			readPosition1 = parentNBTTagCompound.getInteger("x");
			if (readPosition1 < 0)
				readPosition1 = -1;
		}
		x = readPosition1;
		int readPosition2 = -1;
		if (parentNBTTagCompound.hasKey("y", NBT_INT_ID)) {
			readPosition2 = parentNBTTagCompound.getInteger("y");
			if (readPosition2 < 0)
				readPosition2 = -1;
		}
		y = readPosition2;
		int readPosition3 = -1;
		if (parentNBTTagCompound.hasKey("z", NBT_INT_ID)) {
			readPosition3 = parentNBTTagCompound.getInteger("z");
			if (readPosition3 < 0)
				readPosition3 = -1;
		}
		z = readPosition3;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
