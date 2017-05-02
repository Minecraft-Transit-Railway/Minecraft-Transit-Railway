package MTR;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAPGGlassBottom extends BlockWithDirection {

	private static final String name = "BlockAPGGlassBottom";

	public static final PropertyBool SIDE = PropertyBool.create("side");

	protected BlockAPGGlassBottom() {
		super();
		setCreativeTab(null);
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
		setUnlocalizedName(name);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
			break;
		default:
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockAPGGlassTop))
			worldIn.setBlockToAir(pos);
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
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE });
	}

	public String getName() {
		return name;
	}
}
