package MTR.blocks;

import MTR.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMTRSignPost extends BlockBase {

	private static final String name = "BlockMTRSignPost";
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool UP = PropertyBool.create("up");

	public BlockMTRSignPost() {
		super(Material.ROCK, name);
		setDefaultState(blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false)
				.withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.40625F, 0, 0.40625F, 0.59375F, 1, 0.59375F);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean north, east, south, west, up;
		Block blockNorth = worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock();
		Block blockEast = worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock();
		Block blockSouth = worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock();
		Block blockWest = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();
		north = blockNorth instanceof BlockMTRSignPost || blockNorth instanceof BlockEscalatorSign;
		east = blockEast instanceof BlockMTRSignPost || blockEast instanceof BlockEscalatorSign;
		south = blockSouth instanceof BlockMTRSignPost || blockSouth instanceof BlockEscalatorSign;
		west = blockWest instanceof BlockMTRSignPost || blockWest instanceof BlockEscalatorSign;
		up = worldIn.getBlockState(pos.up()).getBlock() instanceof BlockMTRSignPost;
		return state.withProperty(NORTH, north).withProperty(EAST, east).withProperty(SOUTH, south)
				.withProperty(WEST, west).withProperty(UP, up);
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
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, UP });
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

}
