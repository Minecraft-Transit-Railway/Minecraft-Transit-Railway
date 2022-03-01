package mtr.data;

import org.msgpack.value.Value;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MessagePackHelper {

	private final Map<String, Value> map;

	public MessagePackHelper(Map<String, Value> map) {
		this.map = map;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asBooleanValue().getBoolean());
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asIntegerValue().asInt());
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	public long getLong(String key, long defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asIntegerValue().asLong());
	}

	public float getFloat(String key) {
		return getFloat(key, 0);
	}

	public float getFloat(String key, float defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asFloatValue().toFloat());
	}

	public double getDouble(String key) {
		return getDouble(key, 0);
	}

	public double getDouble(String key, double defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asFloatValue().toDouble());
	}

	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return getOrDefault(key, defaultValue, value -> value.asStringValue().asString());
	}

	public void iterateArrayValue(String key, Consumer<Value> consumer) {
		if (map.containsKey(key)) {
			map.get(key).asArrayValue().forEach(consumer);
		}
	}

	public void iterateMapValue(String key, Consumer<Map.Entry<Value, Value>> consumer) {
		if (map.containsKey(key)) {
			map.get(key).asMapValue().entrySet().forEach(consumer);
		}
	}

	private <T> T getOrDefault(String key, T defaultValue, Function<Value, T> function) {
		return map.containsKey(key) ? function.apply(map.get(key)) : defaultValue;
	}
}
