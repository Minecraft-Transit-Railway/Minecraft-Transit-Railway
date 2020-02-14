package mtr.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEscalatorStep extends BlockEscalatorBase {

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockEscalatorSide))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		final EnumEscalatorOrientation orientation = getOrientation(worldIn, pos, state);
		if (orientation == EnumEscalatorOrientation.LANDING_BOTTOM || orientation == EnumEscalatorOrientation.LANDING_TOP)
			return;

		final EnumFacing facing = state.getValue(FACING);
		final boolean direction = state.getValue(DIRECTION);
		final float speed = 0.1F;

		switch (facing) {
		case NORTH:
			entityIn.motionZ += direction ? -speed : speed;
			break;
		case EAST:
			entityIn.motionX += direction ? speed : -speed;
			break;
		case SOUTH:
			entityIn.motionZ += direction ? speed : -speed;
			break;
		case WEST:
			entityIn.motionX += direction ? -speed : speed;
			break;
		default:
			break;
		}
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		final float maxSteps = 256F;
		final EnumEscalatorOrientation orientation = getOrientation(worldIn, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP)
			for (int step = 0; step < maxSteps; step++) {
				AxisAlignedBB box = NULL_AABB;
				switch (state.getValue(FACING)) {
				case NORTH:
					box = new AxisAlignedBB(0, 0, 0, 1, step / maxSteps, 1 - step / maxSteps);
					break;
				case EAST:
					box = new AxisAlignedBB(step / maxSteps, 0, 0, 1, step / maxSteps, 1);
					break;
				case SOUTH:
					box = new AxisAlignedBB(0, 0, step / maxSteps, 1, step / maxSteps, 1);
					break;
				case WEST:
					box = new AxisAlignedBB(0, 0, 0, 1 - step / maxSteps, step / maxSteps, 1);
					break;
				default:
				}
				addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
			}
		else
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.9375, 1));
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance * 0.5F);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, DIRECTION, ORIENTATION, SIDE });
	}
}
