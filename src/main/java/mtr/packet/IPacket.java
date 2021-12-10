package mtr.packet;

import mtr.MTR;
import net.minecraft.util.Identifier;

public interface IPacket {

	Identifier PACKET_OPEN_DASHBOARD_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_dashboard_screen");
	Identifier PACKET_OPEN_PIDS_CONFIG_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_pids_config_screen");
	Identifier PACKET_OPEN_RAILWAY_SIGN_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_railway_sign_screen");
	Identifier PACKET_OPEN_TICKET_MACHINE_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_ticket_machine_screen");
	Identifier PACKET_OPEN_TRAIN_SENSOR_SCREEN = new Identifier(MTR.MOD_ID, "packet_open_train_sensor_screen");

	Identifier PACKET_ANNOUNCE = new Identifier(MTR.MOD_ID, "packet_announce");

	Identifier PACKET_CREATE_RAIL = new Identifier(MTR.MOD_ID, "packet_create_rail");
	Identifier PACKET_CREATE_SIGNAL = new Identifier(MTR.MOD_ID, "packet_create_signal");
	Identifier PACKET_REMOVE_NODE = new Identifier(MTR.MOD_ID, "packet_remove_node");
	Identifier PACKET_REMOVE_RAIL = new Identifier(MTR.MOD_ID, "packet_remove_rail");
	Identifier PACKET_REMOVE_SIGNALS = new Identifier(MTR.MOD_ID, "packet_remove_signals");

	Identifier PACKET_GENERATE_PATH = new Identifier(MTR.MOD_ID, "packet_generate_path");
	Identifier PACKET_CLEAR_TRAINS = new Identifier(MTR.MOD_ID, "packet_clear_trains");
	Identifier PACKET_SIGN_TYPES = new Identifier(MTR.MOD_ID, "packet_sign_types");
	Identifier PACKET_ADD_BALANCE = new Identifier(MTR.MOD_ID, "packet_add_balance");
	Identifier PACKET_PIDS_UPDATE = new Identifier(MTR.MOD_ID, "packet_pids_update");
	Identifier PACKET_CHUNK_S2C = new Identifier(MTR.MOD_ID, "packet_chunk_s2c");

	Identifier PACKET_UPDATE_STATION = new Identifier(MTR.MOD_ID, "packet_update_station");
	Identifier PACKET_UPDATE_PLATFORM = new Identifier(MTR.MOD_ID, "packet_update_platform");
	Identifier PACKET_UPDATE_SIDING = new Identifier(MTR.MOD_ID, "packet_update_siding");
	Identifier PACKET_UPDATE_ROUTE = new Identifier(MTR.MOD_ID, "packet_update_route");
	Identifier PACKET_UPDATE_DEPOT = new Identifier(MTR.MOD_ID, "packet_update_depot");

	Identifier PACKET_DELETE_STATION = new Identifier(MTR.MOD_ID, "packet_delete_station");
	Identifier PACKET_DELETE_PLATFORM = new Identifier(MTR.MOD_ID, "packet_delete_platform");
	Identifier PACKET_DELETE_SIDING = new Identifier(MTR.MOD_ID, "packet_delete_siding");
	Identifier PACKET_DELETE_ROUTE = new Identifier(MTR.MOD_ID, "packet_delete_route");
	Identifier PACKET_DELETE_DEPOT = new Identifier(MTR.MOD_ID, "packet_delete_depot");

	Identifier PACKET_WRITE_RAILS = new Identifier(MTR.MOD_ID, "write_rails");
	Identifier PACKET_UPDATE_TRAINS = new Identifier(MTR.MOD_ID, "update_trains");
	Identifier PACKET_DELETE_TRAINS = new Identifier(MTR.MOD_ID, "delete_trains");
	Identifier PACKET_UPDATE_TRAIN_RIDING_POSITION = new Identifier(MTR.MOD_ID, "update_train_riding_position");
	Identifier PACKET_UPDATE_SCHEDULE = new Identifier(MTR.MOD_ID, "update_schedule");
	Identifier PACKET_UPDATE_TRAIN_SENSOR = new Identifier(MTR.MOD_ID, "packet_update_train_announcer");

	int MAX_PACKET_BYTES = 1048576;
}
