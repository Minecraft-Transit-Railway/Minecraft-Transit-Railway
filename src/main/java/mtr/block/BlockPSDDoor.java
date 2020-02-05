package mtr.block;

import mtr.tile.TileEntityPSDDoor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPSDDoor();
	}
}
