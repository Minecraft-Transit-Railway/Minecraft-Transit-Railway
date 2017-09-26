package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockRailIntersection extends BlockRailBase2 {

	private static final String name = "BlockRailIntersection";
	public static final PropertyBool N = PropertyBool.create("n");
	public static final PropertyBool NE = PropertyBool.create("ne");
	public static final PropertyBool E = PropertyBool.create("e");
	public static final PropertyBool SE = PropertyBool.create("se");
	public static final PropertyBool NONE = PropertyBool.create("none");

	public BlockRailIntersection() {
		super(name);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean n = checkRail(worldIn, pos.north()) || checkRail(worldIn, pos.south());
		boolean ne = checkRail(worldIn, pos.north().east()) || checkRail(worldIn, pos.south().west());
		boolean e = checkRail(worldIn, pos.east()) || checkRail(worldIn, pos.west());
		boolean se = checkRail(worldIn, pos.south().east()) || checkRail(worldIn, pos.north().west());
		return state.withProperty(N, n).withProperty(NE, ne).withProperty(E, e).withProperty(SE, se).withProperty(NONE,
				!(n || ne || e || se));
	}

	private boolean checkRail(IBlockAccess worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		return block instanceof BlockRailBase2 && !(block instanceof BlockRailDummy);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { N, NE, E, SE, NONE });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 7;
	}
}