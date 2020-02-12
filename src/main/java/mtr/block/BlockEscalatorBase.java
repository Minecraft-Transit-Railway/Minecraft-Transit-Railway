package mtr.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockEscalatorBase extends BlockHorizontal {

	public static final PropertyBool DIRECTION = PropertyBool.create("direction");
	public static final PropertyEnum<EnumEscalatorOrientation> ORIENTATION = PropertyEnum.create("orientation", EnumEscalatorOrientation.class);
	public static final PropertyBool SIDE = PropertyBool.create("side");

	protected BlockEscalatorBase() {
		super(Material.ROCK);
		setHardness(2);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return super.getActualState(state, worldIn, pos);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(SIDE) ? 4 : 0) + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIDE, meta >= 4).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	private enum EnumEscalatorOrientation implements IStringSerializable {

		LANDING("landing"), FLAT("flat"), SLOPE("slope");

		private final String name;

		private EnumEscalatorOrientation(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
