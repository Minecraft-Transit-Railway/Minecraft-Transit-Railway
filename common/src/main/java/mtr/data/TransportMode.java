package mtr.data;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE), BOAT(1);

	public final int maxLength;

	TransportMode(int maxLength) {
		this.maxLength = maxLength;
	}
}
