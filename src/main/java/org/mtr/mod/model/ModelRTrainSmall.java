package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelRTrainSmall extends ModelRTrain {

	public ModelRTrainSmall() {
		super();
	}

	private ModelRTrainSmall(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelRTrainSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelRTrainSmall(doorAnimationType, renderDoorOverlay);
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
