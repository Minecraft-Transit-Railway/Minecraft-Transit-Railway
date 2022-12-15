package mtr.model;

import mtr.client.DoorAnimationType;

public class ModelLondonUndergroundD78Mini extends ModelLondonUndergroundD78 {

	public ModelLondonUndergroundD78Mini() {
		super();
	}

	private ModelLondonUndergroundD78Mini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelLondonUndergroundD78Mini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelLondonUndergroundD78Mini(doorAnimationType, renderDoorOverlay);
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