package mtr.data;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE, false, true, true, true, 0),
	BOAT(1, false, true, true, true, 0),
	CABLE_CAR(1, true, false, false, false, -6),
	AIRPLANE(1, false, true, false, false, 0);

	public final int maxLength;
	public final boolean continuousMovement;
	public final boolean hasPitchAscending;
	public final boolean hasPitchDescending;
	public final boolean hasRouteTypeVariation;
	public final int railOffset;

	TransportMode(int maxLength, boolean continuousMovement, boolean hasPitchAscending, boolean hasPitchDescending, boolean hasRouteTypeVariation, int railOffset) {
		this.maxLength = maxLength;
		this.continuousMovement = continuousMovement;
		this.hasPitchAscending = hasPitchAscending;
		this.hasPitchDescending = hasPitchDescending;
		this.hasRouteTypeVariation = hasRouteTypeVariation;
		this.railOffset = railOffset;
	}
}
