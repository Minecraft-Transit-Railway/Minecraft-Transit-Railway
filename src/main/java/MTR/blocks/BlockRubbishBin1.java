package MTR.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRubbishBin1 extends BlockWithDirection {

	private static final String name = "BlockRubbishBin1";
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockRubbishBin1() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 0.28125F);
			break;
		case EAST:
			setBlockBounds(0.71875F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
			break;
		case SOUTH:
			setBlockBounds(0.125F, 0.0F, 0.71875F, 0.875F, 1.0F, 1.0F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.125F, 0.218275F, 1.0F, 0.875F);
			break;
		default:
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		BlockPos pos2 = pos;
		switch (state.getValue(FACING)) {
		case NORTH:
			pos2 = pos.add(0, 0, -1);
			break;
		case EAST:
			pos2 = pos.add(1, 0, 0);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, 1);
			break;
		case WEST:
			pos2 = pos.add(-1, 0, 0);
			break;
		default:
			break;
		}
		return state.withProperty(SIDE, worldIn.getBlockState(pos2).getBlock().isBlockNormalCube()
				&& worldIn.getBlockState(pos2.up()).getBlock().isBlockNormalCube());
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing();
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
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

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE });
	}

	public String getName() {
		return name;
	}
}
