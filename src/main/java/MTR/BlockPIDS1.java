package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPIDS1 extends BlockWithDirection implements ITileEntityProvider {

	private static final String name = "BlockPIDS1";

	protected BlockPIDS1() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setLightLevel(0.3125F);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockPos pos2 = pos;
		switch (state.getValue(FACING)) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		if (!(worldIn.getBlockState(pos2).getBlock() instanceof BlockPIDS1))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing().rotateY();
		BlockPos pos2 = pos;
		switch (var9) {
		case NORTH:
			pos2 = pos.add(1, 0, 0);
			break;
		case EAST:
			pos2 = pos.add(0, 0, 1);
			break;
		case SOUTH:
			pos2 = pos.add(-1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(0, 0, -1);
			break;
		default:
			break;
		}
		if (worldIn.getBlockState(pos2).getBlock().isReplaceable(worldIn, pos2)) {
			worldIn.setBlockState(pos2, getDefaultState().withProperty(FACING, var9.getOpposite()));
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
		} else
			return Blocks.air.getDefaultState();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		if (var3.getAxis() == EnumFacing.Axis.X)
			setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		else
			setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPIDS1Entity();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		return var3;
	}

	public String getName() {
		return name;
	}
}
