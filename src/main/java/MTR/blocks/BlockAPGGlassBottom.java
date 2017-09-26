package MTR.blocks;

import java.util.Random;

import MTR.MTRItems;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAPGGlassBottom extends BlockWithDirection {

	private static final String name = "BlockAPGGlassBottom";

	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockAPGGlassBottom() {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
		case SOUTH:
			return new AxisAlignedBB(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockAPGGlassTop))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(SIDE,
				(meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(SIDE))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTRItems.itemapg;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itemapg, 1, 1);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}
}
