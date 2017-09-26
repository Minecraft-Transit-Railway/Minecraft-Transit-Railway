package MTR.blocks;

import java.util.Random;

import MTR.MTRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAPGDoorClosed extends BlockDoorClosedBase {

	private static final String name = "BlockAPGDoorClosed";

	public BlockAPGDoorClosed() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false)
				.withProperty(TOP, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		boolean top = state.getValue(TOP);
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, top ? 0.5F : 1.0F, 1.0F);
		case SOUTH:
			return new AxisAlignedBB(0.875F, 0.0F, 0.0F, 1.0F, top ? 0.5F : 1.0F, 1.0F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, top ? 0.5F : 1.0F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, top ? 0.5F : 1.0F, 1.0F);
		default:
			return NULL_AABB;
		}
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
