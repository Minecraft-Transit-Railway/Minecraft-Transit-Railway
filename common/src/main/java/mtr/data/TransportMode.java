package mtr.data;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE, false, true, true, 0),
	BOAT(1, false, true, true, 0),
	CABLE_CAR(1, true, false, false, -6),
	AIRPLANE(1, false, true, false, 0);

	public final int maxLength;
	public final boolean continuousMovement;
	public final boolean hasPitch;
	public final boolean hasRouteTypeVariation;
	public final int railOffset;

	TransportMode(int maxLength, boolean continuousMovement, boolean hasPitch, boolean hasRouteTypeVariation, int railOffset) {
		this.maxLength = maxLength;
		this.continuousMovement = continuousMovement;
		this.hasPitch = hasPitch;
		this.hasRouteTypeVariation = hasRouteTypeVariation;
		this.railOffset = railOffset;
	}
}
