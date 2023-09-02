package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelR179Mini extends ModelR179 {

	public ModelR179Mini() {
		super();
	}

	private ModelR179Mini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelR179Mini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelR179Mini(doorAnimationType, renderDoorOverlay);
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
