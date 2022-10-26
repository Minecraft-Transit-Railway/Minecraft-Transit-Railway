package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelCMStockMini extends ModelCMStock {

	public ModelCMStockMini() {
		super();
	}

	private ModelCMStockMini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelCMStockMini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelCMStockMini(doorAnimationType, renderDoorOverlay);
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