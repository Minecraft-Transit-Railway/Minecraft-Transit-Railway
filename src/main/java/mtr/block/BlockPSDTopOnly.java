package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockPSDTopOnly extends HorizontalFacingBlock implements BlockEntityProvider, IBlock, IPropagateBlock {

	public BlockPSDTopOnly() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.OFF_WHITE).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			for (int y = -1; y <= 1; y++) {
				BlockState scanState = world.getBlockState(pos.up(y));
				if (is(scanState.getBlock())) {
					connectGlass(world, pos.up(y), scanState);
				}
			}
		});
	}

	private void connectGlass(World world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = (leftState.getBlock() instanceof BlockPSDTopOnly);

		if (leftValid) {
			final EnumSide side = IBlock.getStatePropertySafe(leftState, SIDE_EXTENDED);
			EnumSide newLeftSide;

			if (side == EnumSide.RIGHT) {
				newLeftSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newLeftSide = EnumSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockState(leftPos, leftState.with(SIDE_EXTENDED, newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = (rightState.getBlock() instanceof BlockPSDTopOnly);

		if (rightValid) {
			final EnumSide side = IBlock.getStatePropertySafe(rightState, SIDE_EXTENDED);
			EnumSide newRightSide;

			if (side == EnumSide.LEFT) {
				newRightSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newRightSide = EnumSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockState(rightPos, rightState.with(SIDE_EXTENDED, newRightSide));
		}

		EnumSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumSide.LEFT;
		} else {
			newSide = EnumSide.SINGLE;
		}

		world.setBlockState(pos, state.with(SIDE_EXTENDED, newSide));
	}

	/*@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		onBreak(world, pos, null, null);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN && !(newState.getBlock() instanceof BlockPSDAPGBase)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return getActualState(world, pos);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 6, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}*/

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, SIDE_EXTENDED, PROPAGATE_PROPERTY);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityPSDTopOnly();
	}

	/*public static BlockState getActualState(WorldAccess world, BlockPos pos) {
		Direction facing = Direction.NORTH;
		EnumSide side = EnumSide.SINGLE;

		final BlockState oldState = world.getBlockState(pos);
		return (oldState.getBlock() instanceof BlockPSDTopOnly ? oldState : mtr.Blocks.PSD_TOP.getDefaultState()).with(SIDE_EXTENDED, side);
	}*/
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing());
	}

	public static class TileEntityPSDTopOnly extends BlockEntity {

		public TileEntityPSDTopOnly() {
			super(MTR.PSD_TOP_ONLY_TILE_ENTITY);
		}
	}
}
