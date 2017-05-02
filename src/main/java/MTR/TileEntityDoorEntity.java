package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityDoorEntity extends TileEntity {

	int position;

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
		parentNBTTagCompound.setInteger("position", position);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition = -1;
		if (parentNBTTagCompound.hasKey("position", NBT_INT_ID)) {
			readPosition = parentNBTTagCompound.getInteger("position");
			if (readPosition < 0)
				readPosition = -1;
		}
		position = readPosition;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
