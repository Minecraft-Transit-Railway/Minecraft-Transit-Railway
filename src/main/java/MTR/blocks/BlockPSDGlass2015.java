package MTR.blocks;

import MTR.MTRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPSDGlass2015 extends BlockPSD {

	private static final String name = "BlockPSDGlass2015";

	public BlockPSDGlass2015() {
		super(name);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itempsd, 1, 3);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 3;
	}
}
