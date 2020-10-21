package mtr.data;

import mtr.block.BlockPlatformRail;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class RailPathFinder {

	private final WorldAccess world;
	private final List<BlockPos> path;
	private final Station destinationStation;
	private final BlockPos destinationPos;
	private final Set<BlockPos> blacklist;

	private static final int SMOOTHING_RADIUS = 2;

	public RailPathFinder(WorldAccess world, BlockPos start, Station destinationStation) {
		this.world = world;
		path = new ArrayList<>();
		path.add(start);
		this.destinationStation = destinationStation;
		destinationPos = new BlockPos((destinationStation.corner1.getLeft() + destinationStation.corner2.getLeft()) / 2, 0, (destinationStation.corner1.getRight() + destinationStation.corner2.getRight()) / 2);
		blacklist = new HashSet<>();
	}

	public List<Pos3f> findPath() {
		boolean enteredPlatform = false;
		while (!path.isEmpty()) {
			final BlockPos lastPos = path.get(path.size() - 1);
			final boolean inStation = destinationStation.inStation(lastPos.getX(), lastPos.getZ());
			final boolean inStationPlatform = inStation && world.getBlockState(lastPos).getBlock() instanceof BlockPlatformRail;

			if (enteredPlatform && !inStationPlatform) {
				removeLastItem();

				List<Pos3f> smoothedPath = new ArrayList<>();
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
			} else {
				if (inStationPlatform) {
					enteredPlatform = true;
				}

				Optional<BlockPosWeighted> blockPosWeighted = getConnectedPositions(lastPos).filter(blockPos -> !blacklist.contains(blockPos)).map(blockPos -> new BlockPosWeighted(blockPos, distanceToDestination(blockPos))).min(BlockPosWeighted::compareTo);
				if (blockPosWeighted.isPresent()) {
					blacklist.add(blockPosWeighted.get().pos);
					path.add(blockPosWeighted.get().pos);
				} else {
					removeLastItem();
				}
			}
		}
		return new ArrayList<>();
	}

	private Stream<BlockPos> getConnectedPositions(BlockPos pos) {
		final Set<BlockPos> positions = new HashSet<>();
		final BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof AbstractRailBlock) {
			final RailShape railShape = state.get(((AbstractRailBlock) state.getBlock()).getShapeProperty());
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
			final RailShape railShape = state.get(((AbstractRailBlock) state.getBlock()).getShapeProperty());
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

	private double distanceToDestination(BlockPos pos) {
		return MathHelper.fastInverseSqrt((double) (MathHelper.square(pos.getX() - destinationPos.getX()) + MathHelper.square(pos.getY() - destinationPos.getY()) + MathHelper.square(pos.getZ() - destinationPos.getZ())));
	}

	private void removeLastItem() {
		path.remove(path.size() - 1);
	}

	private static class BlockPosWeighted implements Comparable<BlockPosWeighted> {

		private final BlockPos pos;
		private final double weight;

		private BlockPosWeighted(BlockPos pos, double weight) {
			this.pos = pos;
			this.weight = weight;
		}

		@Override
		public int compareTo(@NotNull RailPathFinder.BlockPosWeighted o) {
			return Double.compare(weight, o.weight);
		}
	}
}
