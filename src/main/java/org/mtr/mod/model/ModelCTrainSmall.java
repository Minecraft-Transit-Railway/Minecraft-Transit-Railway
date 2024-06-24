package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelCTrainSmall extends ModelCTrain {

	public ModelCTrainSmall() {
		super();
	}

	private ModelCTrainSmall(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelCTrainSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelCTrainSmall(doorAnimationType, renderDoorOverlay);
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
