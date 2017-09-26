package MTR.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import MTR.MTRItems;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEscalatorStep extends BlockWithDirection {

	private static final String name = "BlockEscalatorStep";
	public static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 3);
	public static final PropertyInteger UP = PropertyInteger.create("up", 0, 2);
	// stop, down, up

	public BlockEscalatorStep() {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, 0)
				.withProperty(UP, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean flat = getFlat(worldIn, pos, state.getValue(FACING));
		int side = 0;
		try {
			IBlockState stateAbove = worldIn.getBlockState(pos.up());
			if (stateAbove.getBlock() instanceof BlockEscalatorSide)
				side = stateAbove.getValue(BlockEscalatorSide.SIDE) ? 1 : 0;
		} catch (Exception e) {
		}
		return state.withProperty(SIDE, side + (flat ? 2 : 0));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockEscalatorSide))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		EnumFacing var3 = state.getValue(FACING);
		final float b = 256F;
		if (!getFlat(worldIn, pos, var3))
			for (int a = 0; a < b; a++) {
				AxisAlignedBB box = NULL_AABB;
				switch (var3) {
				case NORTH:
					box = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1F - a / b, a / b, 1.0F);
					break;
				case EAST:
					box = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, a / b, 1F - a / b);
					break;
				case SOUTH:
					box = new AxisAlignedBB(a / b, 0.0F, 0.0F, 1.0F, a / b, 1.0F);
					break;
				case WEST:
					box = new AxisAlignedBB(0.0F, 0.0F, a / b, 1.0F, a / b, 1.0F);
					break;
				default:
				}
				addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
			}
		else
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F));
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		EnumFacing var3 = state.getValue(FACING);
		int direction = state.getValue(UP);
		final double speed = 0.1;
		if (direction != 0) {
			boolean up = direction == 2;
			switch (var3) {
			case SOUTH:
				entityIn.motionX += up ? speed : -speed;
				break;
			case WEST:
				entityIn.motionZ += up ? speed : -speed;
				break;
			case NORTH:
				entityIn.motionX += up ? -speed : speed;
				break;
			case EAST:
				entityIn.motionZ += up ? -speed : speed;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(UP, meta >> 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + state.getValue(UP) * 4;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, UP });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itemescalator);
	}

	private boolean getFlat(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return !(worldIn.getBlockState(pos.offset(facing.rotateY()).down()).getBlock() instanceof BlockEscalatorStep);
	}

	protected void updateNeighbors(World worldIn, BlockPos pos) {
		EnumFacing facing = worldIn.getBlockState(pos).getValue(FACING);
		BlockPos pos1 = pos.offset(facing.rotateYCCW());
		IBlockState state = worldIn.getBlockState(pos);
		update2(worldIn, pos1, state);
		update2(worldIn, pos1.up(), state);
	}

	private void update2(World worldIn, BlockPos pos, IBlockState state) {
		try {
			BlockEscalatorStep block = (BlockEscalatorStep) worldIn.getBlockState(pos).getBlock();
			worldIn.setBlockState(pos, state);
			block.updateNeighbors(worldIn, pos);
		} catch (Exception e) {
		}
	}
}
