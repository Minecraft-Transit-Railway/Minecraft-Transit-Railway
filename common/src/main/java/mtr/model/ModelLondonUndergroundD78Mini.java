package mtr.model;

public class ModelLondonUndergroundD78Mini extends ModelLondonUndergroundD78 {

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