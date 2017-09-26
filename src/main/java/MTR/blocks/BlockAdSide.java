package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAdSide extends BlockWithDirection {

	private static final String name = "BlockAdSide";
	public static final PropertyInteger AD = PropertyInteger.create("ad", 0, 3);

	public BlockAdSide() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AD, 0));
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		if (facing.getAxis().isHorizontal() && func_176381_b(worldIn, pos, facing.rotateYCCW()))
			return getDefaultState().withProperty(FACING, facing.rotateYCCW());
		else
			return worldIn.getBlockState(pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		EnumFacing var5 = state.getValue(FACING);
		if (!func_176381_b(worldIn, pos, var5)) {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		super.neighborChanged(state, worldIn, pos, blockIn);
	}

	private boolean func_176381_b(World worldIn, BlockPos pos, EnumFacing facing) {
		return worldIn.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != Blocks.AIR;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		state = state.cycleProperty(AD);
		worldIn.setBlockState(pos, state, 2);
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.125F, 0.0625F, 1.0F, 0.875F);
		case SOUTH:
			return new AxisAlignedBB(0.9375F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
		case EAST:
			return new AxisAlignedBB(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0.125F, 0.0F, 0.9375F, 0.875F, 1.0F, 1.0F);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(AD, meta >> 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + state.getValue(AD).intValue() * 4;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, AD });
	}
}
