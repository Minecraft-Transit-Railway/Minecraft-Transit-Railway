package mtr.model;

public class ModelMLRSmall extends ModelMLR {

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
