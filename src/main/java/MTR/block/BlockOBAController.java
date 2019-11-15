package mtr.block;

import mtr.MTR;
import mtr.message.MessageOBAController;
import mtr.tile.TileEntityOBAController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOBAController extends BlockContainer {

	public static final PropertyBool ENABLED = PropertyBool.create("enabled");

	public BlockOBAController() {
		super(Material.ROCK);
		setHardness(2);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(ENABLED, false));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			final TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof TileEntityOBAController)
				MTR.INSTANCE.sendTo(new MessageOBAController(pos, ((TileEntityOBAController) tileEntity).getStops()), (EntityPlayerMP) playerIn);
		}
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(ENABLED, getScreenDirection(worldIn, pos) != null);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ENABLED });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityOBAController();
	}

	public static EnumFacing getScreenDirection(IBlockAccess worldIn, BlockPos pos) {
		for (final EnumFacing direction : EnumFacing.HORIZONTALS) {
			final BlockPos newPos = pos.offset(direction);
			if (worldIn.getBlockState(newPos).getBlock() instanceof BlockOBAScreen && worldIn.getBlockState(newPos).getValue(BlockOBAScreen.FACING) == direction.getOpposite())
				return direction;
		}
		return null;
	}
}
