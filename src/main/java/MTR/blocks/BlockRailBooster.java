package MTR.blocks;

import MTR.MTR;
import MTR.TileEntityRailBoosterEntity;
import MTR.items.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailBooster extends BlockRailBase2 implements ITileEntityProvider {

	private static final String name = "BlockRailBooster";
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 7);

	public BlockRailBooster() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(POWERED, false).withProperty(ROTATION, 0));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		neighborChanged(state, worldIn, pos, null);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (worldIn.isRemote && itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			TileEntityRailBoosterEntity te = (TileEntityRailBoosterEntity) worldIn.getTileEntity(pos);
			MTR.proxy.openGUI(te);
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 8).withProperty(POWERED, meta >= 8);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION) + (state.getValue(POWERED) ? 8 : 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { POWERED, ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRailBoosterEntity();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}
}
