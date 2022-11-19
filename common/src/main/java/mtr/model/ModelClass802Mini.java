package mtr.model;

import mtr.mappings.ModelMapper;

public class ModelClass802Mini extends ModelClass802 {

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-105, 105};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-105, 105};
	}

	protected int[] getBogiePositions() {
		return new int[]{-79, 79};
	}

	@Override
	protected ModelMapper[] windowParts() {
		return windowPartsMini();
	}

	@Override
	protected ModelMapper[] windowEndParts() {
		return windowEndPartsMini();
	}
}