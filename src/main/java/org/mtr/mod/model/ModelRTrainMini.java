package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelRTrainMini extends ModelRTrain {

	public ModelRTrainMini() {
		super();
	}

	private ModelRTrainMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelRTrainMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelRTrainMini(doorAnimationType, renderDoorOverlay);
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