package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelATrainSmall extends ModelATrain {

	public ModelATrainSmall(boolean isAel) {
		super(isAel);
	}

	@Override
	public ModelATrainSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelATrainSmall(isAel, doorAnimationType, renderDoorOverlay);
	}

	private ModelATrainSmall(boolean isAel, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isAel, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-144, 144};
	}
}
