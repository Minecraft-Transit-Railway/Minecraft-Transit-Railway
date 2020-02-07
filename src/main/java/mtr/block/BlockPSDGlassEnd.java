package mtr.block;

import java.util.Random;

import mtr.Items;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPSDGlassEnd extends BlockPSDAPGBase {

	public static final PropertyEnum<PSDGlassEnd> SIDE_END = PropertyEnum.create("side_end", PSDGlassEnd.class);

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.psd;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		final PSDGlassEnd side = getSideEnd(worldIn, pos, state);
		return super.getActualState(state, worldIn, pos).withProperty(SIDE_END, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		final PSDGlassEnd side = getSideEnd(source, pos, state);
		if (side == PSDGlassEnd.LEFT_DIAGONAL || side == PSDGlassEnd.RIGHT_DIAGONAL)
			return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		else
			return super.getBoundingBox(state, source, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, SIDE_END, TOP });
	}

	private PSDGlassEnd getSideEnd(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		final PSDAPGSide side = state.getValue(SIDE);

		if (side == PSDAPGSide.MIDDLE || side == PSDAPGSide.SINGLE)
			return PSDGlassEnd.valueOf(side.name());

		final EnumFacing facing = state.getValue(FACING);

		if (side == PSDAPGSide.LEFT) {
			final BlockPos checkPos = pos.offset(facing.rotateYCCW());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDDoor)
				return PSDGlassEnd.LEFT_RED;
			else if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase)
				return PSDGlassEnd.LEFT;
			else
				return PSDGlassEnd.LEFT_DIAGONAL;
		} else if (side == PSDAPGSide.RIGHT) {
			final BlockPos checkPos = pos.offset(facing.rotateY());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDDoor)
				return PSDGlassEnd.RIGHT_RED;
			else if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase)
				return PSDGlassEnd.RIGHT;
			else
				return PSDGlassEnd.RIGHT_DIAGONAL;
		}

		return PSDGlassEnd.SINGLE;
	}

	private enum PSDGlassEnd implements IStringSerializable {

		LEFT("left"), RIGHT("right"), LEFT_RED("left_red"), RIGHT_RED("right_red"), LEFT_DIAGONAL("left_diagonal"), RIGHT_DIAGONAL("right_diagonal"), MIDDLE("middle"), SINGLE("single");

		private final String name;

		private PSDGlassEnd(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
