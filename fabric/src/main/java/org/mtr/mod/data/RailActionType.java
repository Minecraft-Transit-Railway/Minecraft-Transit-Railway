package org.mtr.mod.data;

public enum RailActionType {
	BRIDGE("percentage_complete_bridge", "rail_action_bridge", 0xFFCCCCCC),
	TUNNEL("percentage_complete_tunnel", "rail_action_tunnel", 0xFF663300),
	TUNNEL_WALL("percentage_complete_tunnel_wall", "rail_action_tunnel_wall", 0xFF666666);

	public final String progressTranslation;
	public final String nameTranslation;
	public final int color;

	RailActionType(String progressTranslation, String nameTranslation, int color) {
		this.progressTranslation = progressTranslation;
		this.nameTranslation = nameTranslation;
		this.color = color;
	}
}
