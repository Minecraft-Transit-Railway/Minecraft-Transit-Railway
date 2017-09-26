package MTR.blocks;

import MTR.MTRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPSDGlass extends BlockPSD {

	private static final String name = "BlockPSDGlass";

	public BlockPSDGlass() {
		super(name);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itempsd, 1, 1);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}
}
