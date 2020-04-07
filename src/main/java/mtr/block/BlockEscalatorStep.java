package mtr.block;

import mtr.item.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockEscalatorStep extends BlockEscalatorBase {

	public static final PropertyBool DIRECTION = PropertyBool.create("direction");

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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemBrush) {
			final boolean direction = !state.getValue(DIRECTION);
			final EnumFacing blockFacing = state.getValue(FACING);

			update(worldIn, pos, blockFacing, direction);
			update(worldIn, pos, blockFacing.getOpposite(), direction);

			final BlockPos sidePos = pos.offset(state.getValue(SIDE) ? blockFacing.rotateYCCW() : blockFacing.rotateY());
			if (isStep(worldIn, sidePos, direction)) {
				final BlockEscalatorStep block = (BlockEscalatorStep) worldIn.getBlockState(sidePos).getBlock();
				block.update(worldIn, sidePos, blockFacing, direction);
				block.update(worldIn, sidePos, blockFacing.getOpposite(), direction);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(DIRECTION) ? 8 : 0) + super.getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(DIRECTION, (meta & 8) > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, DIRECTION, ORIENTATION, SIDE);
	}

	private void update(World worldIn, BlockPos pos, EnumFacing offset, boolean direction) {
		worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(DIRECTION, direction));
		final BlockPos offsetPos = pos.offset(offset);

		if (isStep(worldIn, offsetPos, direction))
			update(worldIn, offsetPos, offset, direction);
		if (isStep(worldIn, offsetPos.up(), direction))
			update(worldIn, offsetPos.up(), offset, direction);
		if (isStep(worldIn, offsetPos.down(), direction))
			update(worldIn, offsetPos.down(), offset, direction);
	}

	private boolean isStep(World worldIn, BlockPos pos, boolean direction) {
		final Block block = worldIn.getBlockState(pos).getBlock();
		return block instanceof BlockEscalatorStep;
	}
}
