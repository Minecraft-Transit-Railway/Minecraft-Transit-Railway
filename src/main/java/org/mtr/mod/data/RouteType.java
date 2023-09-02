package mtr.data;

public enum RouteType {
	NORMAL, LIGHT_RAIL, HIGH_SPEED;

	public RouteType next() {
		return values()[(ordinal() + 1) % values().length];
	}
}
