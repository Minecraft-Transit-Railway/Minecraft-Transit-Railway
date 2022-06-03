package mtr.data;

import mtr.block.BlockLiftButtons;
import mtr.entity.EntityLift;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LiftInstructions {

	private final List<LiftInstruction> instructions = new ArrayList<>();
	private final Consumer<String> callback;

	public LiftInstructions(Consumer<String> callback) {
		this.callback = callback;
	}

	public void getTargetFloor(Consumer<Integer> callback) {
		if (hasInstructions()) {
			callback.accept(instructions.get(0).floor);
		}
	}

	public void arrived() {
		if (hasInstructions()) {
			instructions.remove(0);
			callback.accept(toString());
		}
	}

	public boolean hasInstructions() {
		return !instructions.isEmpty();
	}

	public void addInstruction(int currentFloor, boolean currentMovingUp, int floor) {
		addInstruction(currentFloor, currentMovingUp, floor, false, true, true);
	}

	private int addInstruction(int currentFloor, boolean currentMovingUp, int newFloor, boolean newMovingUp, boolean noDirection, boolean shouldAdd) {
		if (currentFloor == newFloor) {
			return 0;
		}

		final List<LiftInstruction> tempInstructions = new ArrayList<>(instructions);
		tempInstructions.add(0, new LiftInstruction(currentFloor, currentMovingUp));

		int distance = 0;
		for (int i = 0; i < tempInstructions.size() - 1; i++) {
			final LiftInstruction previousInstruction = tempInstructions.get(i);
			final LiftInstruction nextInstruction = tempInstructions.get(i + 1);
			final boolean newMovingUpTemp = noDirection ? nextInstruction.movingUp : newMovingUp;

			if (instructions.contains(new LiftInstruction(newFloor, newMovingUpTemp))) {
				return -1;
			}

			if (nextInstruction.canInsert(previousInstruction, newFloor, newMovingUpTemp)) {
				if (shouldAdd) {
					instructions.add(i, new LiftInstruction(newFloor, newMovingUpTemp));
					callback.accept(toString());
				}
				return distance + Math.abs(newFloor - previousInstruction.floor);
			}

			distance += Math.abs(nextInstruction.floor - previousInstruction.floor);
		}

		final int lastInstruction = hasInstructions() ? instructions.get(instructions.size() - 1).floor : currentFloor;
		if (shouldAdd) {
			instructions.add(new LiftInstruction(newFloor, noDirection ? newFloor > lastInstruction : newMovingUp));
			callback.accept(toString());
		}
		return distance + Math.abs(newFloor - lastInstruction);
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		instructions.forEach(liftInstruction -> stringBuilder.append("floor_").append(liftInstruction.floor).append("_").append(liftInstruction.movingUp).append(","));
		return stringBuilder.toString();
	}

	public static void addInstruction(Level world, BlockPos pos, boolean topHalfClicked) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof BlockLiftButtons.TileEntityLiftButtons)) {
			return;
		}

		final int[] currentWeight = {Integer.MAX_VALUE};
		final LiftInstructions[] liftInstructionsToUse = {null};
		final int[] liftFloorToUse = {0};
		final boolean[] liftMovingUpToUse = {false};
		final int[] newLiftFloorToUse = {0};
		final boolean[] hasButtonOverall = {false, false};

		((BlockLiftButtons.TileEntityLiftButtons) blockEntity).forEachTrackPosition(world, (trackPosition, trackFloorTileEntity) -> {
			final EntityLift entityLift = trackFloorTileEntity.getEntityLift();

			if (entityLift != null) {
				final int liftFloor = (int) Math.round(entityLift.getY());

				final int newLiftFloor = trackPosition.getY();
				final boolean[] hasButton = {false, false};
				entityLift.hasButton(newLiftFloor, hasButton);
				final boolean newMovingUp;
				if (topHalfClicked) {
					newMovingUp = hasButton[0];
				} else {
					newMovingUp = !hasButton[1];
				}

				final boolean liftMovingUp = entityLift.getLiftDirection() == EntityLift.LiftDirection.UP;
				final int weight = entityLift.liftInstructions.addInstruction(liftFloor, liftMovingUp, newLiftFloor, newMovingUp, false, false);

				if (weight >= 0 && (topHalfClicked == newMovingUp && weight < currentWeight[0] || newMovingUp && !hasButtonOverall[0] || !newMovingUp && !hasButtonOverall[1])) {
					currentWeight[0] = weight;
					liftInstructionsToUse[0] = entityLift.liftInstructions;
					liftFloorToUse[0] = liftFloor;
					liftMovingUpToUse[0] = liftMovingUp;
					newLiftFloorToUse[0] = newLiftFloor;
				}

				if (hasButton[0]) {
					hasButtonOverall[0] = true;
				}
				if (hasButton[1]) {
					hasButtonOverall[1] = true;
				}
			}
		});

		if (liftInstructionsToUse[0] != null) {
			final boolean newMovingUp;
			if (topHalfClicked) {
				newMovingUp = hasButtonOverall[0];
			} else {
				newMovingUp = !hasButtonOverall[1];
			}
			liftInstructionsToUse[0].addInstruction(liftFloorToUse[0], liftMovingUpToUse[0], newLiftFloorToUse[0], newMovingUp, false, true);
		}
	}

	public static String getStringPart(int floor, boolean movingUp) {
		return "floor_" + floor + "_" + movingUp;
	}

	private static class LiftInstruction {

		private final int floor;
		private final boolean movingUp;

		private LiftInstruction(int floor, boolean movingUp) {
			this.floor = floor;
			this.movingUp = movingUp;
		}

		private boolean canInsert(LiftInstruction previousInstruction, int newFloor, boolean newMovingUp) {
			if (RailwayData.isBetween(newFloor, previousInstruction.floor, floor) && newMovingUp == movingUp) {
				return true;
			} else {
				return previousInstruction.movingUp != movingUp && previousInstruction.movingUp == (newFloor > previousInstruction.floor);
			}
		}

		@Override
		public boolean equals(Object object) {
			return object instanceof LiftInstruction && floor == ((LiftInstruction) object).floor && movingUp == ((LiftInstruction) object).movingUp;
		}
	}
}
