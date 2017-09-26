package MTR.blocks;

import java.util.Random;

import MTR.MTRBlocks;
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

public class BlockEscalatorSideLanding extends BlockWithDirection {

	private static final String name = "BlockEscalatorSideLanding";
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockEscalatorSideLanding() {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		EnumFacing facing = state.getValue(FACING);
		BlockPos posSide = pos.offset(state.getValue(SIDE) ? facing.getOpposite() : facing);
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockEscalatorLanding
				&& worldIn.getBlockState(posSide).getBlock() instanceof BlockEscalatorSideLanding
				&& worldIn.getBlockState(posSide.down()).getBlock() instanceof BlockEscalatorLanding)) {
			worldIn.setBlockToAir(pos);
			worldIn.setBlockToAir(pos.up(2));
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		boolean side = state.getValue(SIDE);
		switch (var3.getHorizontalIndex()) {
		case 0:
			return new AxisAlignedBB(0.5F, 0.0F, side ? 0.75F : 0.0F, 1.0F, 1.0F, side ? 1.0F : 0.25F);
		case 1:
			return new AxisAlignedBB(side ? 0.0F : 0.75F, 0.0F, 0.5F, side ? 0.25F : 1.0F, 1.0F, 1.0F);
		case 2:
			return new AxisAlignedBB(0.0F, 0.0F, side ? 0.0F : 0.75F, 0.5F, 1.0F, side ? 0.25F : 1.0F);
		case 3:
			return new AxisAlignedBB(side ? 0.75F : 0.0F, 0.0F, 0.0F, side ? 1.0F : 0.25F, 1.0F, 0.5F);
		default:
			return NULL_AABB;
		}
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
		return Item.getItemFromBlock(MTRBlocks.blockescalatorlanding);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(MTRBlocks.blockescalatorlanding));
	}
}
