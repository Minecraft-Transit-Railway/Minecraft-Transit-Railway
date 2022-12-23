package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelMLRChristmasMini extends ModelMLRChristmas {

	public ModelMLRChristmasMini() {
		super();
	}

	private ModelMLRChristmasMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelMLRChristmasMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelMLRChristmasMini(doorAnimationType, renderDoorOverlay);
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
