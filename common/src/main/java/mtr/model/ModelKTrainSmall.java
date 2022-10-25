package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelKTrainSmall extends ModelKTrain {

	public ModelKTrainSmall(boolean isTcl) {
		super(isTcl);
	}

	private ModelKTrainSmall(boolean isTcl, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isTcl, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelKTrainSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelKTrainSmall(isTcl, doorAnimationType, renderDoorOverlay);
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
