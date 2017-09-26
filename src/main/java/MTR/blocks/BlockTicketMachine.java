package MTR.blocks;

import MTR.MTRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTicketMachine extends BlockWithDirection {

	private static final String name = "BlockTicketMachine";
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockTicketMachine() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false));
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
				&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
			worldIn.setBlockState(pos.up(),
					MTRBlocks.blockticketmachine.getDefaultState().withProperty(FACING, var9).withProperty(TOP, true));
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9)
					.withProperty(TOP, false);
		} else
			return worldIn.getBlockState(pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		BlockPos var10 = state.getValue(TOP) ? pos.add(0, -1, 0) : pos;
		if (worldIn.getBlockState(var10).getBlock() != MTRBlocks.blockticketmachine
				|| worldIn.getBlockState(var10.up()).getBlock() != MTRBlocks.blockticketmachine) {
			worldIn.setBlockToAir(var10);
			worldIn.setBlockToAir(var10.up());
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		boolean top = state.getValue(TOP);
		if (var3.getAxis() == EnumFacing.Axis.X)
			return new AxisAlignedBB(0.0F, 0.0F, 0.125F, 1.0F, top ? 0.875F : 1F, 0.875F);
		else
			return new AxisAlignedBB(0.125F, 0.0F, 0.0F, 0.875F, top ? 0.875F : 1F, 1.0F);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(TOP,
				(meta & 4) > 0);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(TOP))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TOP });
	}
}
