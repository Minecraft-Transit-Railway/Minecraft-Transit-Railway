package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailReverse extends BlockRailBase2 {

	private static final String name = "BlockRailReverse";
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockRailReverse() {
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 4).withProperty(POWERED, meta >= 4);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION) + (state.getValue(POWERED) ? 4 : 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { POWERED, ROTATION });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 6;
	}
}
