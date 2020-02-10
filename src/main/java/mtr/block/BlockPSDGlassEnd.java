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

	public static final PropertyEnum<EnumPSDGlassEnd> SIDE_END = PropertyEnum.create("side_end", EnumPSDGlassEnd.class);

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
		final EnumPSDGlassEnd side = getSideEnd(worldIn, pos, state);
		return super.getActualState(state, worldIn, pos).withProperty(SIDE_END, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (isVeryEnd(source, pos, state))
			return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		else
			return super.getBoundingBox(state, source, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, SIDE_END, TOP });
	}

	public boolean isVeryEnd(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		final EnumPSDGlassEnd sideEnd = getSideEnd(worldIn, pos, state);
		return sideEnd == EnumPSDGlassEnd.LEFT_DIAGONAL || sideEnd == EnumPSDGlassEnd.RIGHT_DIAGONAL;
	}

	private EnumPSDGlassEnd getSideEnd(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		final EnumPSDAPGSide side = state.getValue(SIDE);

		if (side == EnumPSDAPGSide.MIDDLE || side == EnumPSDAPGSide.SINGLE)
			return EnumPSDGlassEnd.valueOf(side.name());

		final EnumFacing facing = state.getValue(FACING);

		if (side == EnumPSDAPGSide.LEFT) {
			final BlockPos checkPos = pos.offset(facing.rotateYCCW());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDDoor)
				return EnumPSDGlassEnd.LEFT_RED;
			else if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase)
				return EnumPSDGlassEnd.LEFT;
			else
				return EnumPSDGlassEnd.LEFT_DIAGONAL;
		} else if (side == EnumPSDAPGSide.RIGHT) {
			final BlockPos checkPos = pos.offset(facing.rotateY());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDDoor)
				return EnumPSDGlassEnd.RIGHT_RED;
			else if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase)
				return EnumPSDGlassEnd.RIGHT;
			else
				return EnumPSDGlassEnd.RIGHT_DIAGONAL;
		}

		return EnumPSDGlassEnd.SINGLE;
	}

	private enum EnumPSDGlassEnd implements IStringSerializable {

		LEFT("left"), RIGHT("right"), LEFT_RED("left_red"), RIGHT_RED("right_red"), LEFT_DIAGONAL("left_diagonal"), RIGHT_DIAGONAL("right_diagonal"), MIDDLE("middle"), SINGLE("single");

		private final String name;

		private EnumPSDGlassEnd(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
