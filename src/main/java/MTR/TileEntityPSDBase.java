package MTR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityPSDBase extends TileEntity {

	public int color, number, bound, arrow;

	protected void updateNeighbors1() {
		BlockPos pos1 = pos.north();
		BlockPos pos2 = pos.east();
		BlockPos pos3 = pos.north(2);
		BlockPos pos4 = pos.east(2);
		BlockPos pos5 = pos.north(3);
		BlockPos pos6 = pos.east(3);
		update2(pos1, 1);
		update2(pos2, 1);
		update2(pos3, 1);
		update2(pos4, 1);
		update2(pos5, 1);
		update2(pos6, 1);
	}

	protected void updateNeighbors2() {
		BlockPos pos1 = pos.south();
		BlockPos pos2 = pos.west();
		BlockPos pos3 = pos.south(2);
		BlockPos pos4 = pos.west(2);
		BlockPos pos5 = pos.south(3);
		BlockPos pos6 = pos.west(3);
		update2(pos1, 2);
		update2(pos2, 2);
		update2(pos3, 2);
		update2(pos4, 2);
		update2(pos5, 2);
		update2(pos6, 2);
	}

	private void update2(BlockPos pos1, int a) {
		try {
			TileEntityPSDBase te = (TileEntityPSDBase) worldObj.getTileEntity(pos1);
			te.color = color;
			te.number = number;
			te.bound = bound;
			te.arrow = arrow;
			worldObj.markBlockForUpdate(pos1);
			if (a == 1)
				te.updateNeighbors1();
			else
				te.updateNeighbors2();
		} catch (Exception e) {
		}
		Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(pos1);
	}

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
		parentNBTTagCompound.setInteger("color", color);
		parentNBTTagCompound.setInteger("number", number);
		parentNBTTagCompound.setInteger("bound", bound);
		parentNBTTagCompound.setInteger("arrow", arrow);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition1 = -1;
		if (parentNBTTagCompound.hasKey("color", NBT_INT_ID)) {
			readPosition1 = parentNBTTagCompound.getInteger("color");
			if (readPosition1 < 0)
				readPosition1 = -1;
		}
		color = readPosition1;
		if (color > 15 || color < 0)
			color = 0;
		int readPosition2 = -1;
		if (parentNBTTagCompound.hasKey("number", NBT_INT_ID)) {
			readPosition2 = parentNBTTagCompound.getInteger("number");
			if (readPosition2 < 0)
				readPosition2 = -1;
		}
		number = readPosition2;
		if (number > 8 || number < 1)
			number = 1;
		int readPosition3 = -1;
		if (parentNBTTagCompound.hasKey("bound", NBT_INT_ID)) {
			readPosition3 = parentNBTTagCompound.getInteger("bound");
			if (readPosition3 < 0)
				readPosition3 = -1;
		}
		bound = readPosition3;
		if (bound > 3 || bound < 0)
			bound = 0;
		int readPosition4 = -1;
		if (parentNBTTagCompound.hasKey("arrow", NBT_INT_ID)) {
			readPosition4 = parentNBTTagCompound.getInteger("arrow");
			if (readPosition4 < 0)
				readPosition4 = -1;
		}
		arrow = readPosition4;
		if (arrow > 1 || arrow < 0)
			arrow = 0;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
