package MTR.blocks;

import MTR.MTR;
import MTR.TileEntityPSDBase;
import MTR.items.ItemBrush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPSDTopBase extends BlockWithDirection {

	public static final PropertyInteger COLOR = PropertyInteger.create("color", 0, 15);
	public static final PropertyInteger NUMBER = PropertyInteger.create("number", 1, 8);
	public static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 19);

	public BlockPSDTopBase(String name) {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(COLOR, 0).withProperty(FACING, EnumFacing.NORTH)
				.withProperty(NUMBER, 1).withProperty(SIDE, 0));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (worldIn.isRemote && itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			TileEntityPSDBase te = (TileEntityPSDBase) worldIn.getTileEntity(pos);
			MTR.proxy.openGUI(te);
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { COLOR, FACING, NUMBER, SIDE });
	}

	protected boolean getWarning(EnumFacing facing, BlockPos pos, boolean side) {
		int warningX, warningZ;
		BlockPos pos2 = pos;
		switch (facing) {
		case NORTH:
			pos2 = pos.add(0, 0, 1);
			break;
		case EAST:
			pos2 = pos.add(-1, 0, 0);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, -1);
			break;
		case WEST:
			pos2 = pos.add(1, 0, 0);
			break;
		default:
		}
		warningX = ((side ? pos2 : pos).getX() % 8 + 8) % 8;
		warningZ = ((side ? pos2 : pos).getZ() % 8 + 8) % 8;
		return warningX < 4 ^ warningZ < 4;
	}
}
