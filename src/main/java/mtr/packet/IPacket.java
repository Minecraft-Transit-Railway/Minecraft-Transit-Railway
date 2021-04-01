package mtr.packet;

import mtr.MTR;
import net.minecraft.util.Identifier;

public interface IPacket {

	Identifier PACKET_OPEN_DASHBOARD_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_dashboard_screen");
	Identifier PACKET_OPEN_RAILWAY_SIGN_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_railway_sign_screen");
	Identifier PACKET_SIGN_TYPES = new Identifier(MTR.MOD_ID, "packet_sign_types");
	Identifier PACKET_CHUNK_S2C = new Identifier(MTR.MOD_ID, "packet_chunk_s2c");
	Identifier PACKET_UPDATE_STATION = new Identifier(MTR.MOD_ID, "packet_update_station");
	Identifier PACKET_UPDATE_PLATFORM = new Identifier(MTR.MOD_ID, "packet_update_platform");
	Identifier PACKET_UPDATE_ROUTE = new Identifier(MTR.MOD_ID, "packet_update_route");
	Identifier PACKET_DELETE_STATION = new Identifier(MTR.MOD_ID, "packet_delete_station");
	Identifier PACKET_DELETE_PLATFORM = new Identifier(MTR.MOD_ID, "packet_delete_platform");
	Identifier PACKET_DELETE_ROUTE = new Identifier(MTR.MOD_ID, "packet_delete_route");
}
