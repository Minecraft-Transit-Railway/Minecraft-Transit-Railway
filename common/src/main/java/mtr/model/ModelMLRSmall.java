package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMLRSmall extends ModelMLR {

	public ModelMLRSmall(boolean isChristmas) {
		super(isChristmas);
	}

	private ModelMLRSmall(boolean isChristmas, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(isChristmas, doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMLRSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMLRSmall(isChristmas, doorAnimationType, renderDoorOverlay);
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
