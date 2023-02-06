package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMLRMini extends ModelMLR {

	public ModelMLRMini(boolean isChristmas) {
		super(isChristmas);
	}

	private ModelMLRMini(boolean isChristmas, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isChristmas, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMLRMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMLRMini(isChristmas, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-32, 32};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-64, 64};
	}
}
