package mtr.block;

import mtr.block.BlockPlatform.EnumDoorState;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements ITileEntityProvider {

	public static final PropertyEnum<EnumPSDAPGDoor> SIDE_DOOR = PropertyEnum.create("side_door", EnumPSDAPGDoor.class);

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

		final EnumPSDAPGSide side = state.getValue(SIDE);
		final EnumFacing facing = state.getValue(FACING);

		BlockPos newPos = pos;
		if (side == EnumPSDAPGSide.LEFT)
			newPos = pos.offset(facing.rotateY());
		else if (side == EnumPSDAPGSide.RIGHT)
			newPos = pos.offset(facing.rotateYCCW());

		if (!Block.isEqualTo(this, worldIn.getBlockState(newPos).getBlock()))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		state = super.getActualState(state, worldIn, pos);

		if (isOpen(worldIn, pos))
			return state.withProperty(SIDE_DOOR, EnumPSDAPGDoor.OPEN);
		else
			return state.withProperty(SIDE_DOOR, getActualDoorState(worldIn, state, pos));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		if (isOpen(worldIn, pos))
			return NULL_AABB;
		else
			return getBoundingBox(blockState, worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, SIDE, SIDE_DOOR, TOP);
	}

	public final boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
		return getOpenedState(worldIn, pos) != EnumDoorState.CLOSED;
	}

	public final EnumDoorState getOpenedState(IBlockAccess worldIn, BlockPos pos) {
		for (int i = 1; i <= 2; i++) {
			final IBlockState platformState = worldIn.getBlockState(pos.down(i));
			if (platformState.getBlock() instanceof BlockPlatform)
				return platformState.getValue(BlockPlatform.OPEN);
		}
		return EnumDoorState.CLOSED;
	}

	public final EnumPSDAPGDoor getActualDoorState(IBlockAccess worldIn, IBlockState state, BlockPos pos) {
		final EnumPSDAPGSide side = state.getValue(SIDE);
		final EnumFacing facing = state.getValue(FACING);

		if (side == EnumPSDAPGSide.LEFT) {
			final BlockPos checkPos = pos.offset(facing.rotateYCCW());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDGlassEnd)
				return EnumPSDAPGDoor.LEFT_END;
			else
				return EnumPSDAPGDoor.LEFT;
		} else {
			final BlockPos checkPos = pos.offset(facing.rotateY());
			if (worldIn.getBlockState(checkPos).getBlock() instanceof BlockPSDGlassEnd)
				return EnumPSDAPGDoor.RIGHT_END;
			else
				return EnumPSDAPGDoor.RIGHT;
		}
	}

	public enum EnumPSDAPGDoor implements IStringSerializable {

		LEFT("left"), RIGHT("right"), LEFT_END("left_end"), RIGHT_END("right_end"), OPEN("open");

		private final String name;

		EnumPSDAPGDoor(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
