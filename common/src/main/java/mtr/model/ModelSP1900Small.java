package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelSP1900Small extends ModelSP1900 {

	public ModelSP1900Small(boolean isC1141A) {
		super(isC1141A);
	}

	private ModelSP1900Small(boolean isC1141A, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isC1141A, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelSP1900 createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelSP1900Small(isC1141A, doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-64, 0, 64};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-96, -32, 32, 96};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-128, 128};
	}
}
