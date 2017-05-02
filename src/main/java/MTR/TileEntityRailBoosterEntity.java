package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityRailBoosterEntity extends TileEntity {

	int speedBoost, speedSlow;

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
		parentNBTTagCompound.setInteger("speedboost", speedBoost);
		parentNBTTagCompound.setInteger("speedslow", speedSlow);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition1 = -1;
		if (parentNBTTagCompound.hasKey("speedboost", NBT_INT_ID)) {
			readPosition1 = parentNBTTagCompound.getInteger("speedboost");
			if (readPosition1 < 0)
				readPosition1 = -1;
		}
		speedBoost = readPosition1;
		if (speedBoost < 1)
			speedBoost = 1;
		int readPosition2 = -1;
		if (parentNBTTagCompound.hasKey("speedslow", NBT_INT_ID)) {
			readPosition2 = parentNBTTagCompound.getInteger("speedslow");
			if (readPosition2 < 0)
				readPosition2 = -1;
		}
		speedSlow = readPosition2;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
