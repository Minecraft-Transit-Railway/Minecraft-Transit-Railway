package MTR;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailSlope2 extends BlockRailBase2 {

	private static final String name = "BlockRailSlope2";

	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 7);

	protected BlockRailSlope2() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(ROTATION, 0));
		setUnlocalizedName(name);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { ROTATION });
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}

	public static String getName() {
		return name;
	}
}