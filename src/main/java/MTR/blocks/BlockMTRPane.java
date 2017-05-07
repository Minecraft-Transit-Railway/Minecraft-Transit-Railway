package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMTRPane extends BlockWithDirection {

	public static final PropertyBool NAME = PropertyBool.create("name");
	public static final PropertyInteger NUMBER = PropertyInteger.create("number", 0, 6);
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockMTRPane() {
		super();
		setCreativeTab(null);
		setHardness(1F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NAME, false)
				.withProperty(NUMBER, 0).withProperty(TOP, false));
		setStepSound(soundTypeGlass);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockPos var10 = (Boolean) state.getValue(TOP) ? pos.add(0, -1, 0) : pos;
		if (!(worldIn.getBlockState(var10).getBlock() instanceof BlockMTRPane)
				|| !(worldIn.getBlockState(var10.add(0, 1, 0)).getBlock() instanceof BlockMTRPane))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
		Boolean name = (Boolean) access.getBlockState(pos).getValue(BlockMTRPane.NAME);
		Boolean top = (Boolean) access.getBlockState(pos).getValue(BlockMTRPane.TOP);
		if (name)
			switch (var3) {
			case NORTH:
				setBlockBounds(0.0F, 0.0F, 0.0F, 0.6875F, 1.0F, 1.0F);
				break;
			case SOUTH:
				setBlockBounds(0.3125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case EAST:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.6875F);
				break;
			case WEST:
				setBlockBounds(0.0F, 0.0F, 0.3125F, 1.0F, 1.0F, 1.0F);
				break;
			default:
			}
		else
			switch (var3) {
			case NORTH:
				setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, top ? 0.25F : 1.0F, 1.0F);
				break;
			case SOUTH:
				setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, top ? 0.25F : 1.0F, 1.0F);
				break;
			case EAST:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, top ? 0.25F : 1.0F, 0.1875F);
				break;
			case WEST:
				setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, top ? 0.25F : 1.0F, 1.0F);
				break;
			default:
			}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (side != EnumFacing.UP && side != EnumFacing.DOWN && itemStack != null
				&& (itemStack.getItem() == MTR.itemstationname || itemStack.getItem() == MTR.itemstationnamepole)) {
			boolean top = (Boolean) state.getValue(TOP);
			worldIn.setBlockState(pos, state.withProperty(NAME, true));
			worldIn.setBlockState(pos.add(0, top ? -1 : 1, 0), state.withProperty(NAME, true).withProperty(TOP, !top));
			return true;
		} else
			return false;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int x = (pos.getX() % 7 + 7) % 7;
		int z = (pos.getZ() % 7 + 7) % 7;
		if (state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.WEST)
			return state.withProperty(NUMBER, -(x + z) % 7 + 6);
		else
			return state.withProperty(NUMBER, (x + z) % 7);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
				.withProperty(NAME, (meta & 4) > 0).withProperty(TOP, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if ((Boolean) state.getValue(NAME))
			var3 = var3 + 4;
		if ((Boolean) state.getValue(TOP))
			var3 = var3 + 8;
		return var3;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itemmtrpane;
	}

	@Override
	public Item getItem(World arg0, BlockPos arg1) {
		return MTR.itemmtrpane;
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, NAME, NUMBER, TOP });
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
}
