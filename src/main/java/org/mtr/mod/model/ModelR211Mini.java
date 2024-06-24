package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelR211Mini extends ModelR211 {

	public ModelR211Mini(boolean openGangway) {
		super(openGangway);
	}

	private ModelR211Mini(boolean openGangway, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(openGangway, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelR211Mini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelR211Mini(openGangway, doorAnimationType, renderDoorOverlay);
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
