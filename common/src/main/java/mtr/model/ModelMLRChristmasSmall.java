package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMLRChristmasSmall extends ModelMLRChristmas {

	public ModelMLRChristmasSmall() {
		super();
	}

	private ModelMLRChristmasSmall(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMLRChristmasSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMLRChristmasSmall(doorAnimationType, renderDoorOverlay);
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
