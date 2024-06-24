package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelSP1900Mini extends ModelSP1900 {

	public ModelSP1900Mini(boolean isC1141A) {
		super(isC1141A);
	}

	private ModelSP1900Mini(boolean isC1141A, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isC1141A, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelSP1900 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelSP1900Mini(isC1141A, doorAnimationType, renderDoorOverlay);
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
