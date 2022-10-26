package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelKTrainMini extends ModelKTrain {

	public ModelKTrainMini(boolean isTcl) {
		super(isTcl);
	}

	private ModelKTrainMini(boolean isTcl, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isTcl, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelKTrainMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelKTrainMini(isTcl, doorAnimationType, renderDoorOverlay);
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