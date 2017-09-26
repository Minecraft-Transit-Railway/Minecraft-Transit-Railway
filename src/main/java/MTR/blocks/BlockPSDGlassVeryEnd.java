package MTR.blocks;

import MTR.MTRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPSDGlassVeryEnd extends BlockPSD {

	private static final String name = "BlockPSDGlassVeryEnd";

	public BlockPSDGlassVeryEnd() {
		super(name);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itempsd, 1, 2);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}
}
