package mtr.data;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE, false, true, 0),
	BOAT(1, false, true, 0),
	CABLE_CAR(1, true, false, -6);

	public final int maxLength;
	public final boolean continuousMovement;
	public final boolean hasPitch;
	public final int railOffset;

	TransportMode(int maxLength, boolean continuousMovement, boolean hasPitch, int railOffset) {
		this.maxLength = maxLength;
		this.continuousMovement = continuousMovement;
		this.hasPitch = hasPitch;
		this.railOffset = railOffset;
	}
}
