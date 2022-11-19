package mtr.model;

public class ModelSP1900Mini extends ModelSP1900 {

	public ModelSP1900Mini(boolean isC1141A) {
		super(isC1141A);
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
