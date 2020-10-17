package mtr.block;

import mtr.data.Platform;
import mtr.data.RailwayData;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.RailShape;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockPlatformRail extends AbstractRailBlock {


	public static final EnumProperty<RailShape> SHAPE = EnumProperty.of("shape", RailShape.class, (shape) -> shape == RailShape.NORTH_SOUTH || shape == RailShape.EAST_WEST);

	public BlockPlatformRail(Settings settings) {
		super(true, settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.addPlatform(scanPlatform(world, pos, state));
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
			checkAndBreak(world, pos, state, Direction.NORTH);
			checkAndBreak(world, pos, state, Direction.SOUTH);
		} else {
			checkAndBreak(world, pos, state, Direction.EAST);
			checkAndBreak(world, pos, state, Direction.WEST);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isIn(Tag<Block> tag) {
		return tag == BlockTags.RAILS;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	private void checkAndBreak(World world, BlockPos pos, BlockState state, Direction direction) {
		BlockPos checkPos = pos.offset(direction);
		BlockState checkState = world.getBlockState(checkPos);
		if (checkState == state) {
			((BlockPlatformRail) checkState.getBlock()).checkAndBreak(world, checkPos, state, direction);
		}
		world.breakBlock(pos, false);
	}

	private Platform scanPlatform(WorldAccess world, BlockPos pos, BlockState state) {
		final Direction scanDirection;
		final Direction.Axis axis;

		if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
			scanDirection = Direction.NORTH;
			axis = Direction.Axis.Z;
		} else {
			scanDirection = Direction.WEST;
			axis = Direction.Axis.X;
		}

		int length = -2;
		BlockPos startPos = pos;
		do {
			startPos = startPos.offset(scanDirection);
			length++;
		} while (world.getBlockState(startPos).equals(state));

		BlockPos lengthPos = pos;
		do {
			lengthPos = lengthPos.offset(scanDirection.getOpposite());
			length++;
		} while (world.getBlockState(lengthPos).equals(state));

		return new Platform(startPos.offset(scanDirection.getOpposite()), axis, length);
	}
}
