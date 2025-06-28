package org.mtr.mod.resource;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonPrimitive;
import org.mtr.mod.Init;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

public final class BlockbenchModelValidator {

	/**
	 * Cleans up a Blockbench model file, most notably removes any textures stored as base64. This will drastic reduce file size.
	 *
	 * @param modelObject the Blockbench model file parsed into a JSON object
	 * @param id          the id of the model
	 * @param assertTrue  an optional assertion, if running this in a JUnit
	 */
	public static void validate(JsonObject modelObject, String id, @Nullable BiConsumer<Boolean, String> assertTrue) {
		iterateChildren(modelObject, value -> {
		}, value -> {
		});

		modelObject.addProperty("name", id);
		modelObject.addProperty("model_identifier", id);
		modelObject.addProperty("modded_entity_version", "1.17_yarn");
		modelObject.remove("fabricOptions");

		try {
			modelObject.getAsJsonArray("textures").forEach(textureElement -> {
				final JsonObject textureObject = textureElement.getAsJsonObject();
				textureObject.remove("path");
				textureObject.remove("source");
				if (assertTrue != null) {
					final String relativePath = textureObject.get("relative_path").getAsString();
					assertTrue.accept(relativePath.startsWith("../../textures/vehicle/") || relativePath.startsWith("../../textures/overlay/"), relativePath);
				}
			});
		} catch (Exception ignored) {
			Init.LOGGER.warn("Failed to read textures from Blockbench model [{}]", id);
		}
	}

	/**
	 * Rounds a numeric value when it makes sense.
	 *
	 * @param valueString    the input value as a string
	 * @param setValueDouble a callback for if the value is parsed to be a double
	 * @param setValueInt    a callback for if the value is parsed to be an integer
	 */
	public static void processValue(String valueString, DoubleConsumer setValueDouble, IntConsumer setValueInt) {
		if (valueString.matches("-?\\d\\.\\d+e-\\d+")) {
			setValueInt.accept(0);
		} else {
			final int index1 = valueString.indexOf("999");
			final int index2 = valueString.indexOf("000");
			if (valueString.matches("-?\\d+\\.\\d*999\\d*") && (index2 < 0 || index1 < index2)) {
				setValue(valueString, "999", setValueDouble, setValueInt);
			} else if (valueString.matches("-?\\d+\\.\\d*000\\d*") && (index1 < 0 || index2 < index1)) {
				setValue(valueString, "000", setValueDouble, setValueInt);
			}
		}
	}

	private static void iterateChildren(JsonElement jsonElement, DoubleConsumer setValueDouble, IntConsumer setValueInt) {
		if (jsonElement.isJsonObject()) {
			final JsonObject jsonObject = jsonElement.getAsJsonObject();
			jsonObject.entrySet().forEach(entry -> iterateChildren(entry.getValue(), value -> jsonObject.addProperty(entry.getKey(), value), value -> jsonObject.addProperty(entry.getKey(), value)));
		} else if (jsonElement.isJsonArray()) {
			final JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				final int index = i;
				iterateChildren(jsonArray.get(index), value -> jsonArray.set(index, new JsonPrimitive(value)), value -> jsonArray.set(index, new JsonPrimitive(value)));
			}
		} else {
			processValue(jsonElement.toString().toLowerCase(Locale.ENGLISH), setValueDouble, setValueInt);
		}
	}

	private static void setValue(String valueString, String matchingToken, DoubleConsumer setValueDouble, IntConsumer setValueInt) {
		final String[] valueSplit = valueString.split("\\.");
		final String decimalString = valueSplit[valueSplit.length - 1];
		final BigDecimal decimalValue = BigDecimal.valueOf(Utilities.round(Double.parseDouble(String.format("%s0.%s", valueString.startsWith("-") ? "-" : "", decimalString)), decimalString.indexOf(matchingToken) + 2));
		final BigDecimal value;

		if (valueSplit.length > 1) {
			value = decimalValue.add(BigDecimal.valueOf(Integer.parseInt(valueSplit[0])));
		} else {
			value = decimalValue;
		}

		if (decimalValue.equals(BigDecimal.ZERO)) {
			setValueInt.accept(value.intValueExact());
		} else {
			setValueDouble.accept(value.doubleValue());
		}
	}
}
