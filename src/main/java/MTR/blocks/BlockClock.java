package MTR.blocks;

import MTR.TileEntityClockEntity;
import MTR.items.ItemBrush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClock extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockClock";
	public static final PropertyBool FACING2 = PropertyBool.create("facing2");
	public static final PropertyInteger TIME = PropertyInteger.create("time", 0, 7);

	private static final int[] times = { 15, 30, 45, 60, 90, 120, 180 };

	public BlockClock() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING2, false).withProperty(TIME, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityClockEntity();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing();
		boolean facing2 = var9 == EnumFacing.EAST || var9 == EnumFacing.WEST;
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING2, facing2);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			worldIn.setBlockState(pos, state.cycleProperty(TIME));
			int time = worldIn.getBlockState(pos).getValue(TIME);
			if (time > 0)
				worldIn.scheduleUpdate(pos, this, 20 * times[time - 1]);
			if (!worldIn.isRemote)
				playerIn.addChatComponentMessage(new TextComponentString(
						time > 0 ? times[time - 1] + " " + I18n.format("gui.seconds", new Object[0]) : ""));
			return true;
		} else
			return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		boolean facing2 = state.getValue(FACING2);
		if (facing2)
			return new AxisAlignedBB(0.375F, 0.0F, 0.1875F, 0.625F, 1.0F, 0.8125F);
		else
			return new AxisAlignedBB(0.1875F, 0.0F, 0.375F, 0.8125F, 1.0F, 0.625F);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING2, meta >= 8).withProperty(TIME, meta % 8);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(TIME);
		if (state.getValue(FACING2))
			var3 += 8;
		return var3 > 15 ? 0 : var3;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, FACING2, TIME });
	}
}
