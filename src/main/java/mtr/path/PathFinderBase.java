package mtr.path;

import mtr.block.IBlock;
import mtr.data.Pos3f;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public abstract class PathFinderBase {

	protected final WorldAccess world;
	protected final BlockPos destination;
	protected final List<BlockPos> path;
	protected final Set<BlockPos> blacklist;

	protected static final int SMOOTHING_RADIUS = 2;

	protected PathFinderBase(WorldAccess world, BlockPos start, BlockPos destination) {
		this.world = world;
		this.destination = destination;
		path = new ArrayList<>();
		path.add(start);
		blacklist = new HashSet<>();
		blacklist.add(start);
	}

	public abstract List<Pos3f> findPath();

	protected List<Pos3f> smoothPath() {
		final List<Pos3f> smoothedPath = new ArrayList<>();
		final int pathLength = path.size();

		for (int i = 0; i < pathLength; i++) {
			if (i >= SMOOTHING_RADIUS && i < pathLength - SMOOTHING_RADIUS) {
				final Pos3f positionFloat = new Pos3f(0, 0, 0);
				for (int j = i - SMOOTHING_RADIUS; j <= i + SMOOTHING_RADIUS; j++) {
					positionFloat.add(new Pos3f(path.get(j)));
				}

				positionFloat.scale(1F / (SMOOTHING_RADIUS * 2 + 1));
				smoothedPath.add(positionFloat);
			} else {
				smoothedPath.add(new Pos3f(path.get(i)));
			}
		}

		return smoothedPath;
	}

	protected Stream<BlockPos> getConnectedPositions(BlockPos pos) {
		final Set<BlockPos> positions = new HashSet<>();
		final BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof AbstractRailBlock) {
			final RailShape railShape = IBlock.getStatePropertySafe(state, ((AbstractRailBlock) state.getBlock()).getShapeProperty());
			final Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

			for (final Direction direction : directions) {
				if (shapeCheck(railShape, direction) && canConnectFromDirection(pos.offset(direction), direction, false)) {
					positions.add(pos.offset(direction));
				} else if (shapeCheckAscending(railShape, direction) && canConnectFromDirection(pos.offset(direction).up(), direction, false)) {
					positions.add(pos.offset(direction).up());
				} else if (shapeCheck(railShape, direction) && canConnectFromDirection(pos.offset(direction).down(), direction, true)) {
					positions.add(pos.offset(direction).down());
				}
			}
		}

		return positions.stream();
	}

	private boolean canConnectFromDirection(BlockPos newPos, Direction fromDirection, boolean isDescending) {
		final BlockState state = world.getBlockState(newPos);
		if (state.getBlock() instanceof AbstractRailBlock) {
			final RailShape railShape = IBlock.getStatePropertySafe(state, ((AbstractRailBlock) state.getBlock()).getShapeProperty());
			return isDescending ? shapeCheckAscending(railShape, fromDirection.getOpposite()) : shapeCheck(railShape, fromDirection.getOpposite());
		}
		return false;
	}

	private boolean shapeCheck(RailShape railShape, Direction direction) {
		switch (direction) {
			case NORTH:
				return railShape == RailShape.NORTH_SOUTH || railShape == RailShape.NORTH_EAST || railShape == RailShape.NORTH_WEST || railShape == RailShape.ASCENDING_SOUTH;
			case EAST:
				return railShape == RailShape.EAST_WEST || railShape == RailShape.NORTH_EAST || railShape == RailShape.SOUTH_EAST || railShape == RailShape.ASCENDING_WEST;
			case SOUTH:
				return railShape == RailShape.NORTH_SOUTH || railShape == RailShape.SOUTH_EAST || railShape == RailShape.SOUTH_WEST || railShape == RailShape.ASCENDING_NORTH;
			case WEST:
				return railShape == RailShape.EAST_WEST || railShape == RailShape.NORTH_WEST || railShape == RailShape.SOUTH_WEST || railShape == RailShape.ASCENDING_EAST;
		}
		return false;
	}

	private boolean shapeCheckAscending(RailShape railShape, Direction direction) {
		switch (direction) {
			case NORTH:
				return railShape == RailShape.ASCENDING_NORTH;
			case EAST:
				return railShape == RailShape.ASCENDING_EAST;
			case SOUTH:
				return railShape == RailShape.ASCENDING_SOUTH;
			case WEST:
				return railShape == RailShape.ASCENDING_WEST;
		}
		return false;
	}

	protected void removeLastItem() {
		path.remove(path.size() - 1);
	}

	public static float distanceSquaredBetween(BlockPos pos1, BlockPos pos2) {
		return distanceSquaredBetween(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
	}

	public static float distanceSquaredBetween(float x1, float y1, float z1, float x2, float y2, float z2) {
		return MathHelper.square(x1 - x2) + MathHelper.square(y1 - y2) + MathHelper.square(z1 - z2);
	}

	protected static class BlockPosWeighted implements Comparable<BlockPosWeighted> {

		protected final BlockPos pos;
		protected final float weight;

		protected BlockPosWeighted(BlockPos pos, float weight) {
			this.pos = pos;
			this.weight = weight;
		}

		@Override
		public int compareTo(RoutePathFinder.BlockPosWeighted o) {
			return Double.compare(weight, o.weight);
		}
	}
}
