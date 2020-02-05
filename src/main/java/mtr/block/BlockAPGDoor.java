package mtr.block;

import mtr.tile.TileEntityAPGDoor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAPGDoor extends BlockPSDAPGDoorBase {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAPGDoor();
	}
}
