package MTR;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRailEntity extends TileEntity {

	public int radius, xc, zc, startAngle, angleChange;

	@Override
	public double getMaxRenderDistanceSquared() {
		return 65536;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.add(-300, -50, -300), pos.add(300, 50, 300));
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
		compound.setInteger("radius", radius);
		compound.setInteger("xc", xc);
		compound.setInteger("zc", zc);
		compound.setInteger("startAngle", startAngle);
		compound.setInteger("angleChange", angleChange);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		radius = compound.getInteger("radius");
		xc = compound.getInteger("xc");
		zc = compound.getInteger("zc");
		startAngle = compound.getInteger("startAngle");
		angleChange = compound.getInteger("angleChange");
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
