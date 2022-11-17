package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelE44Mini extends ModelE44 {

	public ModelE44Mini() {
		super();
	}

	private ModelE44Mini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelE44Mini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelE44Mini(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-34, 34};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-62, 62};
	}
}
