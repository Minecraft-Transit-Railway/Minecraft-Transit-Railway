package MTR.blocks;

import MTR.GUIStationName;
import MTR.TileEntityStationNameEntity;
import MTR.items.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStationNameWall1 extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockStationNameWall1";
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockStationNameWall1() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
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

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			TileEntityStationNameEntity te = (TileEntityStationNameEntity) worldIn.getTileEntity(pos);
			if (worldIn.isRemote)
				Minecraft.getMinecraft().displayGuiScreen(new GUIStationName(te));
			return true;
		} else
			return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0F, 0F, 0F, 0.0625F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.9375F, 0F, 0F, 1F, 1F, 1F);
		case EAST:
			return new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0F, 0F, 0.9375F, 1F, 1F, 1F);
		default:
			return NULL_AABB;
		}
	}

	protected boolean func_176381_b(World worldIn, BlockPos pos, EnumFacing facing) {
		return worldIn.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != Blocks.AIR;
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityStationNameEntity();
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE });
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
}
