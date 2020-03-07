package mtr.tile;

import mtr.MTRUtilities;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform.EnumDoorState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityPSDAPGDoorBase extends TileEntity {

	private float doorClient;
	private long doorTimeClient;

	@SideOnly(Side.CLIENT)
	public float getDoorClient() {
		final EnumDoorState opened = ((BlockPSDAPGDoorBase) world.getBlockState(pos).getBlock()).getOpenedState(world, pos);
		final Tuple<Float, Long> tuple = MTRUtilities.updateDoor(opened == EnumDoorState.OPENED, doorClient, doorTimeClient);
		doorClient = tuple.getFirst();
		doorTimeClient = tuple.getSecond();
		return doorClient;
	}
}
