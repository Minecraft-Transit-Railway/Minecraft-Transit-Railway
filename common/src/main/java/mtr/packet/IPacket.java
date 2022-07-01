package mtr.packet;

import mtr.MTR;
import net.minecraft.resources.ResourceLocation;

public interface IPacket {

	ResourceLocation PACKET_VERSION_CHECK = new ResourceLocation(MTR.MOD_ID, "packet_version_check");

	ResourceLocation PACKET_OPEN_DASHBOARD_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_dashboard_screen");
	ResourceLocation PACKET_OPEN_PIDS_CONFIG_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_pids_config_screen");
	ResourceLocation PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_arrival_projector_config_screen");
	ResourceLocation PACKET_OPEN_RAILWAY_SIGN_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_railway_sign_screen");
	ResourceLocation PACKET_OPEN_TICKET_MACHINE_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_ticket_machine_screen");
	ResourceLocation PACKET_OPEN_TRAIN_SENSOR_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_train_sensor_screen");
	ResourceLocation PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_lift_track_floor_screen");
	ResourceLocation PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN = new ResourceLocation(MTR.MOD_ID, "packet_open_resource_pack_creator_screen");

	ResourceLocation PACKET_ANNOUNCE = new ResourceLocation(MTR.MOD_ID, "packet_announce");

	ResourceLocation PACKET_CREATE_RAIL = new ResourceLocation(MTR.MOD_ID, "packet_create_rail");
	ResourceLocation PACKET_CREATE_SIGNAL = new ResourceLocation(MTR.MOD_ID, "packet_create_signal");
	ResourceLocation PACKET_REMOVE_NODE = new ResourceLocation(MTR.MOD_ID, "packet_remove_node");
	ResourceLocation PACKET_REMOVE_RAIL = new ResourceLocation(MTR.MOD_ID, "packet_remove_rail");
	ResourceLocation PACKET_REMOVE_SIGNALS = new ResourceLocation(MTR.MOD_ID, "packet_remove_signals");
	ResourceLocation PACKET_REMOVE_RAIL_ACTION = new ResourceLocation(MTR.MOD_ID, "packet_remove_rail_action");

	ResourceLocation PACKET_GENERATE_PATH = new ResourceLocation(MTR.MOD_ID, "packet_generate_path");
	ResourceLocation PACKET_CLEAR_TRAINS = new ResourceLocation(MTR.MOD_ID, "packet_clear_trains");
	ResourceLocation PACKET_SIGN_TYPES = new ResourceLocation(MTR.MOD_ID, "packet_sign_types");
	ResourceLocation PACKET_DRIVE_TRAIN = new ResourceLocation(MTR.MOD_ID, "packet_drive_train");
	ResourceLocation PACKET_PRESS_LIFT_BUTTON = new ResourceLocation(MTR.MOD_ID, "packet_press_lift_button");
	ResourceLocation PACKET_ADD_BALANCE = new ResourceLocation(MTR.MOD_ID, "packet_add_balance");
	ResourceLocation PACKET_PIDS_UPDATE = new ResourceLocation(MTR.MOD_ID, "packet_pids_update");
	ResourceLocation PACKET_ARRIVAL_PROJECTOR_UPDATE = new ResourceLocation(MTR.MOD_ID, "packet_arrival_projector_update");
	ResourceLocation PACKET_CHUNK_S2C = new ResourceLocation(MTR.MOD_ID, "packet_chunk_s2c");

	ResourceLocation PACKET_UPDATE_STATION = new ResourceLocation(MTR.MOD_ID, "packet_update_station");
	ResourceLocation PACKET_UPDATE_PLATFORM = new ResourceLocation(MTR.MOD_ID, "packet_update_platform");
	ResourceLocation PACKET_UPDATE_SIDING = new ResourceLocation(MTR.MOD_ID, "packet_update_siding");
	ResourceLocation PACKET_UPDATE_ROUTE = new ResourceLocation(MTR.MOD_ID, "packet_update_route");
	ResourceLocation PACKET_UPDATE_DEPOT = new ResourceLocation(MTR.MOD_ID, "packet_update_depot");

	ResourceLocation PACKET_DELETE_STATION = new ResourceLocation(MTR.MOD_ID, "packet_delete_station");
	ResourceLocation PACKET_DELETE_PLATFORM = new ResourceLocation(MTR.MOD_ID, "packet_delete_platform");
	ResourceLocation PACKET_DELETE_SIDING = new ResourceLocation(MTR.MOD_ID, "packet_delete_siding");
	ResourceLocation PACKET_DELETE_ROUTE = new ResourceLocation(MTR.MOD_ID, "packet_delete_route");
	ResourceLocation PACKET_DELETE_DEPOT = new ResourceLocation(MTR.MOD_ID, "packet_delete_depot");

	ResourceLocation PACKET_WRITE_RAILS = new ResourceLocation(MTR.MOD_ID, "write_rails");
	ResourceLocation PACKET_UPDATE_TRAINS = new ResourceLocation(MTR.MOD_ID, "update_trains");
	ResourceLocation PACKET_DELETE_TRAINS = new ResourceLocation(MTR.MOD_ID, "delete_trains");
	ResourceLocation PACKET_UPDATE_TRAIN_PASSENGERS = new ResourceLocation(MTR.MOD_ID, "update_train_passengers");
	ResourceLocation PACKET_UPDATE_TRAIN_PASSENGER_POSITION = new ResourceLocation(MTR.MOD_ID, "update_train_passenger_position");
	ResourceLocation PACKET_UPDATE_ENTITY_SEAT_POSITION = new ResourceLocation(MTR.MOD_ID, "update_entity_seat_position");
	ResourceLocation PACKET_UPDATE_RAIL_ACTIONS = new ResourceLocation(MTR.MOD_ID, "update_rail_actions");
	ResourceLocation PACKET_UPDATE_SCHEDULE = new ResourceLocation(MTR.MOD_ID, "update_schedule");
	ResourceLocation PACKET_UPDATE_TRAIN_SENSOR = new ResourceLocation(MTR.MOD_ID, "packet_update_train_announcer");
	ResourceLocation PACKET_UPDATE_LIFT_TRACK_FLOOR = new ResourceLocation(MTR.MOD_ID, "packet_update_lift_track_floor");

	int MAX_PACKET_BYTES = 1048576;
}
