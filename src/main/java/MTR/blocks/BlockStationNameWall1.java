package MTR.blocks;

import MTR.MTR;
import MTR.TileEntityStationNameEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameWall1 extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockStationNameWall1";
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockStationNameWall1() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
		setUnlocalizedName(name);
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
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing var5 = (EnumFacing) state.getValue(FACING);
		if (!func_176381_b(worldIn, pos, var5)) {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() == MTR.itembrush) {
			TileEntityStationNameEntity te = (TileEntityStationNameEntity) worldIn.getTileEntity(pos);
			if (worldIn.isRemote)
				MTR.proxy.openGUI(te);
			return true;
		} else
			return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0F, 0F, 0F, 0.0625F, 1F, 1F);
			break;
		case SOUTH:
			setBlockBounds(0.9375F, 0F, 0F, 1F, 1F, 1F);
			break;
		case EAST:
			setBlockBounds(0F, 0F, 0F, 1F, 1F, 0.0625F);
			break;
		case WEST:
			setBlockBounds(0F, 0F, 0.9375F, 1F, 1F, 1F);
			break;
		default:
		}
	}

	protected boolean func_176381_b(World worldIn, BlockPos pos, EnumFacing facing) {
		return worldIn.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != Blocks.air;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(SIDE,
				(meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if ((Boolean) state.getValue(SIDE))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityStationNameEntity();
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE });
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	public static String getName() {
		return name;
	}
}
