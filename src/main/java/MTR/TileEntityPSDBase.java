package MTR;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPSDBase extends TileEntity {

	public int color, number, bound, arrow;

	public void update(EnumFacing facing) {
		try {
			TileEntityPSDBase te = (TileEntityPSDBase) worldObj.getTileEntity(pos.offset(facing));
			update2(te, facing);
		} catch (Exception e1) {
			try {
				TileEntityPSDBase te = (TileEntityPSDBase) worldObj.getTileEntity(pos.offset(facing, 2));
				update2(te, facing);
			} catch (Exception e2) {
				try {
					TileEntityPSDBase te = (TileEntityPSDBase) worldObj.getTileEntity(pos.offset(facing, 3));
					update2(te, facing);
				} catch (Exception e3) {
				}
			}
		}
	}

	private void update2(TileEntityPSDBase te, EnumFacing facing) {
		te.color = color;
		te.number = number;
		te.bound = bound;
		te.arrow = arrow;
		te.markDirty();
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		te.update(facing);
	}

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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("color", color);
		compound.setInteger("number", number);
		compound.setInteger("bound", bound);
		compound.setInteger("arrow", arrow);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		color = compound.getInteger("color");
		number = compound.getInteger("number");
		bound = compound.getInteger("bound");
		arrow = compound.getInteger("arrow");
		if (color > 15 || color < 0)
			color = 0;
		if (number > 8 || number < 1)
			number = 1;
		if (bound > 3 || bound < 0)
			bound = 0;
		if (arrow > 1 || arrow < 0)
			arrow = 0;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
