package org.mtr.test;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mod.Init;
import org.mtr.mod.resource.BlockbenchModelValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class BlockbenchModelValidationTest {

	@Test
	public void validate() throws IOException {
		try (final DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/assets/mtr/models/vehicle"))) {
			stream.forEach(path -> {
				final String id = FilenameUtils.getBaseName(path.toString());
				Init.LOGGER.info("Validating {}", id);

				try {
					final JsonObject modelObject = JsonParser.parseReader(Files.newBufferedReader(path)).getAsJsonObject();
					BlockbenchModelValidator.validate(modelObject, id, Assertions::assertTrue);
					Files.write(path, modelObject.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Test
	public void testRounding() {
		final double[] value = {0};

		BlockbenchModelValidator.processValue("10123.456999123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(10123.457, value[0]);
		BlockbenchModelValidator.processValue("20123.456999876", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(20123.457, value[0]);
		BlockbenchModelValidator.processValue("30123.456999", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(30123.457, value[0]);
		BlockbenchModelValidator.processValue("1.234567e-89", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("0.456999", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0.457, value[0]);
		BlockbenchModelValidator.processValue("10123.456000123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(10123.456, value[0]);
		BlockbenchModelValidator.processValue("20123.456000876", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(20123.456, value[0]);
		BlockbenchModelValidator.processValue("30123.456000", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(30123.456, value[0]);
		BlockbenchModelValidator.processValue("1.2345678999e-9", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("0.456000", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0.456, value[0]);
		BlockbenchModelValidator.processValue("1.23456789000234e-9", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("-10123.456999123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-10123.457, value[0]);
		BlockbenchModelValidator.processValue("-20123.456999876", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-20123.457, value[0]);
		BlockbenchModelValidator.processValue("-30123.456999", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-30123.457, value[0]);
		BlockbenchModelValidator.processValue("-1.234567e-89", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("-0.456999", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-0.457, value[0]);
		BlockbenchModelValidator.processValue("-10123.456000123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-10123.456, value[0]);
		BlockbenchModelValidator.processValue("-20123.456000876", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-20123.456, value[0]);
		BlockbenchModelValidator.processValue("-30123.456000", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-30123.456, value[0]);
		BlockbenchModelValidator.processValue("-1.2345678999e-9", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("-0.456000", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(-0.456, value[0]);
		BlockbenchModelValidator.processValue("-1.23456789000234e-9", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(0, value[0]);

		BlockbenchModelValidator.processValue("1.000999123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(1, value[0]);
		BlockbenchModelValidator.processValue("1.999000123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(2, value[0]);
		BlockbenchModelValidator.processValue("1.2000999123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(1.2, value[0]);
		BlockbenchModelValidator.processValue("1.2999000123", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(1.3, value[0]);
		BlockbenchModelValidator.processValue("1.000999", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(1, value[0]);
		BlockbenchModelValidator.processValue("1.999000", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(2, value[0]);

		BlockbenchModelValidator.processValue("32.784909999999996", newValue -> value[0] = newValue, newValue -> value[0] = newValue);
		Assertions.assertEquals(32.78491, value[0]);
	}
}
