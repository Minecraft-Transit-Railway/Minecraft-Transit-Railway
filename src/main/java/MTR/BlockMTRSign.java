package MTR;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMTRSign extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockMTRSign";

	protected BlockMTRSign() {
		super();
		// GameRegistry.registerBlock(this, name);
		setCreativeTab(MTR.MTRtab);
		setUnlocalizedName(name);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityMTRSignEntity();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
		if (var3.getAxis() == EnumFacing.Axis.X)
			setBlockBounds(0.125F, 0.0F, 0.375F, 0.875F, 1.0F, 0.625F);
		else
			setBlockBounds(0.375F, 0.0F, 0.125F, 0.625F, 1.0F, 0.875F);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		return var3;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	public String getName() {
		return name;
	}

}
