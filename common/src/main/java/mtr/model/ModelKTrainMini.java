package mtr.model;

public class ModelKTrainMini extends ModelKTrain {

	public ModelKTrainMini(boolean isTcl) {
		super(isTcl);
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

	@Override
	protected int[] getBogiePositions() {
		return new int[]{0};
	}
}