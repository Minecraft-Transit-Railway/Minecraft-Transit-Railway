package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelCMStockSmall extends ModelCMStock {

	public ModelCMStockSmall() {
		super();
	}

	private ModelCMStockSmall(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelCMStockSmall createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelCMStockSmall(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{-80, 0, 80};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-120, -40, 40, 120};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-144, 144};
	}
}
