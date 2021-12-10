package mtr.data;

public enum RouteType {
	NORMAL("gui.mtr.route_type_normal"), LIGHT_RAIL("gui.mtr.route_type_light_rail"), HIGH_SPEED("gui.mtr.route_type_high_speed");

	public final String key;

	RouteType(String key) {
		this.key = key;
	}

	public RouteType next() {
		return values()[(ordinal() + 1) % values().length];
	}
}
