package mtr.model;

public class ModelE44Mini extends ModelE44 {

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
