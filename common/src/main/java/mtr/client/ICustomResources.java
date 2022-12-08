package mtr.client;

import com.google.gson.JsonObject;

public interface ICustomResources {

	String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	String CUSTOM_TRAIN_ID_PREFIX = "mtr_custom_train_";
	String CUSTOM_SIGN_ID_PREFIX = "mtr_custom_sign_";

	String CUSTOM_TRAINS_KEY = "custom_trains";
	String CUSTOM_SIGNS_KEY = "custom_signs";

	String CUSTOM_TRAINS_BASE_TRAIN_TYPE = "base_train_type";
	String CUSTOM_TRAINS_NAME = "name";
	String CUSTOM_TRAINS_COLOR = "color";
	String CUSTOM_TRAINS_MODEL = "model";
	String CUSTOM_TRAINS_MODEL_PROPERTIES = "model_properties";
	String CUSTOM_TRAINS_DESCRIPTION = "description";
	String CUSTOM_TRAINS_WIKIPEDIA_ARTICLE = "wikipedia_article";
	String CUSTOM_TRAINS_TEXTURE_ID = "texture_id";
	String CUSTOM_TRAINS_GANGWAY_CONNECTION_ID = "gangway_connection_id";
	String CUSTOM_TRAINS_TRAIN_BARRIER_ID = "train_barrier_id";
	String CUSTOM_TRAINS_DOOR_ANIMATION_TYPE = "door_animation_type";
	String CUSTOM_TRAINS_RENDER_DOOR_OVERLAY = "render_door_overlay";
	String CUSTOM_TRAINS_RIDER_OFFSET = "rider_offset";
	String CUSTOM_TRAINS_BVE_SOUND_BASE_ID = "bve_sound_base_id";
	String CUSTOM_TRAINS_SPEED_SOUND_COUNT = "speed_sound_count";
	String CUSTOM_TRAINS_SPEED_SOUND_BASE_ID = "speed_sound_base_id";
	String CUSTOM_TRAINS_DOOR_SOUND_BASE_ID = "door_sound_base_id";
	String CUSTOM_TRAINS_DOOR_CLOSE_SOUND_TIME = "door_close_sound_time";
	String CUSTOM_TRAINS_ACCEL_SOUND_AT_COAST = "accel_sound_at_coast";
	String CUSTOM_TRAINS_CONST_PLAYBACK_SPEED = "const_playback_speed";

	String CUSTOM_SIGNS_TEXTURE_ID = "texture_id";
	String CUSTOM_SIGNS_FLIP_TEXTURE = "flip_texture";
	String CUSTOM_SIGNS_CUSTOM_TEXT = "custom_text";
	String CUSTOM_SIGNS_FLIP_CUSTOM_TEXT = "flip_custom_text";
	String CUSTOM_SIGNS_SMALL = "small";
	String CUSTOM_SIGNS_BACKGROUND_COLOR = "background_color";

	static void createCustomTrainSchema(JsonObject jsonObject, String id, String name, String description, String wikipediaArticle, String color, String gangwayConnectionId, String trainBarrierId, String doorAnimationType, boolean renderDoorOverlay, float riderOffset) {
		final JsonObject customTrainsObject = new JsonObject();
		jsonObject.add(CUSTOM_TRAINS_KEY, customTrainsObject);
		final JsonObject customTrainObject = new JsonObject();
		customTrainsObject.add(id, customTrainObject);
		customTrainObject.addProperty(CUSTOM_TRAINS_NAME, name);
		customTrainObject.addProperty(CUSTOM_TRAINS_DESCRIPTION, description);
		customTrainObject.addProperty(CUSTOM_TRAINS_WIKIPEDIA_ARTICLE, wikipediaArticle);
		customTrainObject.addProperty(CUSTOM_TRAINS_COLOR, color);
		customTrainObject.addProperty(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID, gangwayConnectionId);
		customTrainObject.addProperty(CUSTOM_TRAINS_TRAIN_BARRIER_ID, trainBarrierId);
		customTrainObject.addProperty(CUSTOM_TRAINS_DOOR_ANIMATION_TYPE, doorAnimationType);
		customTrainObject.addProperty(CUSTOM_TRAINS_RENDER_DOOR_OVERLAY, renderDoorOverlay);
		customTrainObject.addProperty(CUSTOM_TRAINS_RIDER_OFFSET, riderOffset);
		final String resource = String.format("mtr:%s/%s", id, id);
		customTrainObject.addProperty(CUSTOM_TRAINS_TEXTURE_ID, resource);
		customTrainObject.addProperty(CUSTOM_TRAINS_MODEL, resource + ".bbmodel");
		customTrainObject.addProperty(CUSTOM_TRAINS_MODEL_PROPERTIES, resource + ".json");
	}
}
