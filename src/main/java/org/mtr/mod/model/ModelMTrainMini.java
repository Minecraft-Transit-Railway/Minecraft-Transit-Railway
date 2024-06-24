package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMTrainMini extends ModelMTrain {

	public ModelMTrainMini() {
		super();
	}

	private ModelMTrainMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMTrainMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMTrainMini(doorAnimationType, renderDoorOverlay);
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