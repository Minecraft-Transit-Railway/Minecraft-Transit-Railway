package org.mtr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.mod.resource.ModelResourceLoader;
import org.mtr.mod.resource.MqoModelConverter;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class MqoModelConverterTest {

	@Test
	public void convertTriangleWithMaterial() {
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Material 1 {",
				"	\"body\" col(0.250 0.500 0.750 0.800) dif(0.600)",
				"}",
				"Object \"body_part\" {",
				"	vertex 3 {",
				"		0 0 0",
				"		1 0 0",
				"		0 1 0",
				"	}",
				"	face 1 {",
				"		3 V(0 1 2) M(0)",
				"	}",
				"}"
		));

		Assertions.assertEquals("body_part", convertedModel.getModelParts().get(0));
		Assertions.assertTrue(convertedModel.getObjContent().contains("o body_part"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("v 0.010000 0.000000 0.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("vn 0.000000 0.000000 -1.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("usemtl mqo_material_0"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1//1 3//2 2//3"));
		Assertions.assertTrue(convertedModel.getMtlContent().contains("Kd 0.150000 0.300000 0.450000"));
		Assertions.assertTrue(convertedModel.getMtlContent().contains("d 0.800000"));
	}

	@Test
	public void convertTextureAndUv() {
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Material 1 {",
				"	\"window\" tex(\"textures/window.png\")",
				"}",
				"Object \"window\" {",
				"	vertex 3 {",
				"		0 0 0",
				"		1 0 0",
				"		0 1 0",
				"	}",
				"	face 1 {",
				"		3 V(0 1 2) M(0) UV(0 0 1 0 0 1)",
				"	}",
				"}"
		));

		Assertions.assertTrue(convertedModel.getObjContent().contains("vt 0.000000 0.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("vt 1.000000 0.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("vt 0.000000 1.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1/1/1 3/2/2 2/3/3"));
		Assertions.assertTrue(convertedModel.getMtlContent().contains("map_Kd textures/window.png"));
	}

	@Test
	public void convertQuadWithDefaultMaterial() {
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Object \"floor\" {",
				"	vertex 4 {",
				"		0 0 0",
				"		1 0 0",
				"		1 1 0",
				"		0 1 0",
				"	}",
				"	face 1 {",
				"		4 V(0 1 2 3) M(-1)",
				"	}",
				"}"
		));

		Assertions.assertTrue(convertedModel.getObjContent().contains("usemtl mqo_default"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1//1 3//2 2//3"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1//4 4//5 3//6"));
	}

	@Test
	public void convertSmoothNormals() {
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Object \"smooth\" {",
				"	shading 1",
				"	facet 180.0",
				"	vertex 4 {",
				"		0 0 0",
				"		1 0 0",
				"		0 1 0",
				"		0 0 1",
				"	}",
				"	face 2 {",
				"		3 V(0 1 2)",
				"		3 V(0 2 3)",
				"	}",
				"}"
		));

		Assertions.assertTrue(convertedModel.getObjContent().contains("vn -0.707107 0.000000 -0.707107"));
	}

	@Test
	public void ignoreUnknownObjectChildChunks() {
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Object \"body\" {",
				"	vertex 3 {",
				"		0 0 0",
				"		1 0 0",
				"		0 1 0",
				"	}",
				"	vertexattr {",
				"		uid {",
				"			1",
				"			2",
				"			3",
				"		}",
				"	}",
				"	face 1 {",
				"		3 V(0 1 2)",
				"	}",
				"}"
		));

		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1//1 3//2 2//3"));
	}

	@Test
	public void rejectUnsupportedFormats() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Compress Ver 1.1"
		)));

		Assertions.assertThrows(IllegalArgumentException.class, () -> MqoModelConverter.convert(mqo(
				"Metasequoia Document",
				"Format Text Ver 1.1",
				"Object \"binary\" {",
				"	BVertex 1 {",
				"	}",
				"}"
		)));
	}

	@Test
	public void extractMqozSingleMqo() {
		final byte[] bytes = mqoz(
				zipData("model.mqo", mqo(
						"Metasequoia Document",
						"Format Text Ver 1.1",
						"Material 1 {",
						"	\"body\" col(0.250 0.500 0.750 0.800) dif(0.600)",
						"}",
						"Object \"body_part\" {",
						"	vertex 3 {",
						"		0 0 0",
						"		1 0 0",
						"		0 1 0",
						"	}",
						"	face 1 {",
						"		3 V(0 1 2) M(0)",
						"	}",
						"}"
				)),
				zipData("thumbnail.png", "ignored")
		);
		final MqoModelConverter.ConvertedModel convertedModel = MqoModelConverter.convert(ModelResourceLoader.extractMqoContentFromMqoz("mtr:models/model.mqoz", bytes));

		Assertions.assertEquals("body_part", ModelResourceLoader.getModelParts("mtr:models/model.mqoz", bytes).get(0));
		Assertions.assertTrue(convertedModel.getObjContent().contains("v 0.010000 0.000000 0.000000"));
		Assertions.assertTrue(convertedModel.getObjContent().contains("f 1//1 3//2 2//3"));
		Assertions.assertTrue(convertedModel.getMtlContent().contains("Kd 0.150000 0.300000 0.450000"));
	}

	@Test
	public void extractMqozPrefersMatchingBaseName() {
		final byte[] bytes = mqoz(
				zipData("a_first.mqo", mqo(
						"Metasequoia Document",
						"Format Text Ver 1.1",
						"Object \"first\" {",
						"	vertex 3 {",
						"		0 0 0",
						"		1 0 0",
						"		0 1 0",
						"	}",
						"	face 1 {",
						"		3 V(0 1 2)",
						"	}",
						"}"
				)),
				zipData("folder/model.mqo", mqo(
						"Metasequoia Document",
						"Format Text Ver 1.1",
						"Object \"preferred\" {",
						"	vertex 3 {",
						"		0 0 0",
						"		1 0 0",
						"		0 1 0",
						"	}",
						"	face 1 {",
						"		3 V(0 1 2)",
						"	}",
						"}"
				))
		);

		Assertions.assertEquals("preferred", ModelResourceLoader.getModelParts("mtr:models/model.mqoz", bytes).get(0));
	}

	@Test
	public void rejectMqozWithoutMqo() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> ModelResourceLoader.extractMqoContentFromMqoz("mtr:models/model.mqoz", mqoz(zipData("model.mqx", "ignored"))));
	}

	private static String mqo(String... lines) {
		return String.join("\n", lines);
	}

	private static ZipData zipData(String name, String content) {
		return new ZipData(name, content);
	}

	private static byte[] mqoz(ZipData... zipDataArray) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			for (final ZipData zipData : zipDataArray) {
				zipOutputStream.putNextEntry(new ZipEntry(zipData.name));
				zipOutputStream.write(zipData.content.getBytes(StandardCharsets.UTF_8));
				zipOutputStream.closeEntry();
			}
			zipOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final class ZipData {

		private final String name;
		private final String content;

		private ZipData(String name, String content) {
			this.name = name;
			this.content = content;
		}
	}
}
