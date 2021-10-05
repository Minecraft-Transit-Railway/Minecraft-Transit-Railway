package mtr.model;

import java.util.HashMap;
import java.util.Map;

public class TrainModelRegistry {

	private static final Map<String, ModelTrainBase> MODELS = new HashMap<>();

	public static void register(String key, ModelTrainBase model) {
		MODELS.put(key, model);
	}

	public static ModelTrainBase getModel(String key) {
		return MODELS.get(key.toLowerCase());
	}
}
