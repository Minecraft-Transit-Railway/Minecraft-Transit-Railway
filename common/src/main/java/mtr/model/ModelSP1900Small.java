package mtr.model;

public class ModelSP1900Small extends ModelSP1900 {

	public ModelSP1900Small(boolean isC1141A) {
		super(isC1141A);
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
