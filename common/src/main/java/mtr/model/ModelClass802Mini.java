package mtr.model;

import mtr.client.DoorAnimationType;
import mtr.mappings.ModelMapper;

public class ModelClass802Mini extends ModelClass802 {

	public ModelClass802Mini() {
		super();
	}

	private ModelClass802Mini(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(doorAnimationType, renderDoorOverlay);
	}

	@Override
	public ModelClass802Mini createNew(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		return new ModelClass802Mini(doorAnimationType, renderDoorOverlay);
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{-105, 105};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{-105, 105};
	}

	@Override
	protected int[] getBogiePositions() {
		return new int[]{-79, 79};
	}

	@Override
	protected boolean renderFirstDestination(boolean isEnd1Head, boolean isEnd2Head) {
		return !isEnd2Head;
	}

	@Override
	protected boolean renderSecondDestination(boolean isEnd1Head, boolean isEnd2Head) {
		return !isEnd1Head;
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