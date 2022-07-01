package mtr.model;

public class ModelR211Mini extends ModelR211 {

	public ModelR211Mini(boolean openGangway) {
		super(openGangway);
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
