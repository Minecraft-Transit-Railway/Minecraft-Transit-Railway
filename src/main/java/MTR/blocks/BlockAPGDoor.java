package MTR.blocks;

import java.util.Random;

import MTR.MTRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAPGDoor extends BlockDoorBase {

	private static final String name = "BlockAPGDoor";

	public BlockAPGDoor() {
		super(name);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTRItems.itemapg;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itemapg);
	}
}
