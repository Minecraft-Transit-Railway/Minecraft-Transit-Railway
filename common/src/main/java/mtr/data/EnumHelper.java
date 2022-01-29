package mtr.data;

public interface EnumHelper {

	static <T extends Enum<T>> T valueOf(T defaultValue, String name) {
		try {
			return Enum.valueOf(defaultValue.getDeclaringClass(), name);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
