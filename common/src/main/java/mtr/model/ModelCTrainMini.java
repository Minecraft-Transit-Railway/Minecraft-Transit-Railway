package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelCTrainMini extends ModelCTrain {

	public ModelCTrainMini() {
		super();
	}

	private ModelCTrainMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelCTrainMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelCTrainMini(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-40, 40};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-64, 64};
	}
}