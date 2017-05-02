package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailBooster extends BlockRailBase2 implements ITileEntityProvider {

	private static final String name = "BlockRailBooster";
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 7);

	protected BlockRailBooster() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(POWERED, false).withProperty(ROTATION, 0));
		setUnlocalizedName(name);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() == MTR.itembrush) {
			TileEntityRailBoosterEntity te = (TileEntityRailBoosterEntity) worldIn.getTileEntity(pos);
			if (worldIn.isRemote)
				MTR.proxy.openGUI(te);
			return true;
		} else
			return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 8).withProperty(POWERED, meta >= 8);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (Integer) state.getValue(ROTATION) + ((Boolean) state.getValue(POWERED) ? 8 : 0);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { POWERED, ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRailBoosterEntity();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}

	public static String getName() {
		return name;
	}
}
