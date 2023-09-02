package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMTrainSmall extends ModelMTrain {

	public ModelMTrainSmall() {
		super();
	}

	private ModelMTrainSmall(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMTrainSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMTrainSmall(doorAnimationType, renderDoorOverlay);
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
