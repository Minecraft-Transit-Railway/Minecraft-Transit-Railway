package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelATrainMini extends ModelATrain {

	public ModelATrainMini(boolean isAel) {
		super(isAel);
	}

	private ModelATrainMini(boolean isAel, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isAel, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelATrainMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelATrainMini(isAel, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return isAel ? new int[]{-93, -67, -41, 41, 67, 93} : new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return isAel ? new int[]{0} : new int[]{-40, 40};
	}

	@Override
	protected int[] getEndPositions() {
		return isAel ? new int[]{-104, 104} : new int[]{-64, 64};
	}
}