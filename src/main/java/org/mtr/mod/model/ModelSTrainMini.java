package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelSTrainMini extends ModelSTrain {

	public ModelSTrainMini() {
		super();
	}

	private ModelSTrainMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelSTrain createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelSTrainMini(doorAnimationType, renderDoorOverlay);
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