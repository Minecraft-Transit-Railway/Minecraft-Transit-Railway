package mtr.block;

import java.util.Random;

import mtr.Items;
import mtr.tile.TileEntityAPGDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAPGDoor extends BlockPSDAPGDoorBase {

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.apg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAPGDoor();
	}
}
