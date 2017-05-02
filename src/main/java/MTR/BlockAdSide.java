package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAdSide extends BlockWithDirection {

	private static final String name = "BlockAdSide";
	public static final PropertyInteger AD = PropertyInteger.create("ad", 0, 3);

	protected BlockAdSide() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AD, 0));
		setUnlocalizedName(name);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		if (facing.getAxis().isHorizontal() && func_176381_b(worldIn, pos, facing.rotateYCCW())) {
			return getDefaultState().withProperty(FACING, facing.rotateYCCW());
		} else {
			return worldIn.getBlockState(pos);
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing var5 = (EnumFacing) state.getValue(FACING);
		if (!this.func_176381_b(worldIn, pos, var5)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
	}

	protected boolean func_176381_b(World worldIn, BlockPos pos, EnumFacing facing) {
		return worldIn.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != Blocks.air;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		state = state.cycleProperty(AD);
		worldIn.setBlockState(pos, state, 2);
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.125F, 0.0625F, 1.0F, 0.875F);
			break;
		case SOUTH:
			setBlockBounds(0.9375F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
			break;
		case EAST:
			setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 0.0625F);
			break;
		case WEST:
			setBlockBounds(0.125F, 0.0F, 0.9375F, 0.875F, 1.0F, 1.0F);
			break;
		default:
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(AD,
				meta >> 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		var3 = var3 + ((Integer) state.getValue(AD)).intValue() * 4;
		return var3;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, AD });
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	public String getName() {
		return name;
	}

}
