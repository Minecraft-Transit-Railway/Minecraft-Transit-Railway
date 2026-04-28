package org.mtr.mod.resource;

import org.apache.commons.lang3.StringUtils;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MqoModelConverter {

	private static final Pattern PARAMETER_PATTERN = Pattern.compile("([A-Za-z_][A-Za-z0-9_]*)\\(([^)]*)\\)");
	private static final double MQO_TO_OBJ_SCALE = 0.01;

	public static ConvertedModel convert(String content) {
		final String[] lines = content.replace("\r\n", "\n").replace('\r', '\n').split("\n");
		final Parser parser = new Parser(lines);
		parser.validateHeader();
		parser.parseRootChunks();
		return parser.toConvertedModel();
	}

	private static String extractQuotedOrFirstToken(String text) {
		final String trimmedText = text.trim();
		if (trimmedText.startsWith("\"")) {
			final int endIndex = findStringEnd(trimmedText, 1);
			return endIndex < 0 ? trimmedText.substring(1) : trimmedText.substring(1, endIndex);
		} else {
			final String[] splitText = trimmedText.split("\\s+", 2);
			return splitText.length == 0 ? "" : splitText[0];
		}
	}

	private static int findStringEnd(String text, int startIndex) {
		boolean escaped = false;
		for (int i = startIndex; i < text.length(); i++) {
			final char character = text.charAt(i);
			if (escaped) {
				escaped = false;
			} else if (character == '\\') {
				escaped = true;
			} else if (character == '"') {
				return i;
			}
		}
		return -1;
	}

	private static String getParameter(String line, String key) {
		final Matcher matcher = PARAMETER_PATTERN.matcher(line);
		while (matcher.find()) {
			if (StringUtils.equals(matcher.group(1), key)) {
				return matcher.group(2).trim();
			}
		}
		return "";
	}

	private static boolean isNonZeroSetting(String line) {
		final String[] splitLine = line.trim().split("\\s+");
		return splitLine.length > 1 && !StringUtils.equals(splitLine[1], "0");
	}

	public static final class ConvertedModel {

		private final String objContent;
		private final String mtlContent;
		private final ObjectArrayList<String> modelParts;

		private ConvertedModel(String objContent, String mtlContent, ObjectArrayList<String> modelParts) {
			this.objContent = objContent;
			this.mtlContent = mtlContent;
			this.modelParts = modelParts;
		}

		public String getObjContent() {
			return objContent;
		}

		public String getMtlContent() {
			return mtlContent;
		}

		public ObjectArrayList<String> getModelParts() {
			return new ObjectArrayList<>(modelParts);
		}
	}

	private static final class Parser {

		private final String[] lines;
		private final ObjectArrayList<Material> materials = new ObjectArrayList<>();
		private final ObjectArrayList<MqoObject> objects = new ObjectArrayList<>();
		private int index;

		private Parser(String[] lines) {
			this.lines = lines;
		}

		private void validateHeader() {
			final String documentLine = nextNonEmptyLine();
			final String formatLine = nextNonEmptyLine();
			if (!StringUtils.equals(documentLine, "Metasequoia Document")) {
				throw new IllegalArgumentException("Invalid MQO header");
			}
			if (formatLine == null || !formatLine.startsWith("Format Text ")) {
				throw new IllegalArgumentException("Only MQO Format Text files are supported");
			}
		}

		private void parseRootChunks() {
			while (index < lines.length) {
				final String line = lines[index].trim();
				if (line.startsWith("Material ")) {
					parseMaterialChunk();
				} else if (line.startsWith("Object ")) {
					parseObjectChunk();
				} else if (line.startsWith("BVertex ") || line.startsWith("Blob ")) {
					throw new IllegalArgumentException("Unsupported MQO chunk: " + line.split("\\s+")[0]);
				} else if (line.endsWith("{")) {
					skipChunk();
				} else {
					index++;
				}
			}
		}

		private ConvertedModel toConvertedModel() {
			final StringBuilder objBuilder = new StringBuilder("mtllib generated.mtl\n");
			final StringBuilder mtlBuilder = new StringBuilder();
			final ObjectArrayList<String> modelParts = new ObjectArrayList<>();

			mtlBuilder.append("newmtl mqo_default\nKd 1.000000 1.000000 1.000000\nd 1.000000\n\n");
			for (int i = 0; i < materials.size(); i++) {
				final Material material = materials.get(i);
				mtlBuilder.append("newmtl ").append(getMaterialName(i)).append('\n');
				mtlBuilder.append(String.format(Locale.US, "Kd %.6f %.6f %.6f%n", material.red * material.diffuse, material.green * material.diffuse, material.blue * material.diffuse));
				mtlBuilder.append(String.format(Locale.US, "d %.6f%n", material.alpha));
				if (!material.texture.isEmpty()) {
					mtlBuilder.append("map_Kd ").append(material.texture).append('\n');
				}
				mtlBuilder.append('\n');
			}

			int vertexOffset = 1;
			int textureOffset = 1;
			int normalOffset = 1;
			for (final MqoObject object : objects) {
				if (!object.visible || object.vertices.isEmpty() || object.faces.isEmpty()) {
					continue;
				}

				String objectName = object.name.isEmpty() ? "Object" : object.name;
				if (modelParts.contains(objectName)) {
					int suffix = 2;
					while (modelParts.contains(objectName + "_" + suffix)) {
						suffix++;
					}
					objectName = objectName + "_" + suffix;
				}
				modelParts.add(objectName);

				objBuilder.append("o ").append(objectName).append('\n');
				objBuilder.append("g ").append(objectName).append('\n');
				for (final Vertex vertex : object.vertices) {
					objBuilder.append(String.format(Locale.US, "v %.6f %.6f %.6f%n", vertex.x * MQO_TO_OBJ_SCALE, vertex.y * MQO_TO_OBJ_SCALE, vertex.z * MQO_TO_OBJ_SCALE));
				}

				final ObjectArrayList<Triangle> triangles = new ObjectArrayList<>();
				object.faces.forEach(face -> {
					final int vertexCount = face.vertexIndices.size();
					for (int i = 1; i < vertexCount - 1; i++) {
						triangles.add(new Triangle(object.vertices, face, 0, i + 1, i));
					}
				});

				for (final Triangle triangle : triangles) {
					for (int i = 0; i < 3; i++) {
						final Vector normal = object.shading == 0 ? triangle.normal : getSmoothedNormal(triangles, triangle, triangle.vertexIndices[i], object.facet);
						objBuilder.append(String.format(Locale.US, "vn %.6f %.6f %.6f%n", normal.x, normal.y, normal.z));
						triangle.normalIndices[i] = normalOffset++;
					}
				}

				for (final Triangle triangle : triangles) {
					objBuilder.append("usemtl ").append(triangle.materialIndex >= 0 && triangle.materialIndex < materials.size() ? getMaterialName(triangle.materialIndex) : "mqo_default").append('\n');
					for (int i = 0; i < 3; i++) {
						if (triangle.hasUv) {
							objBuilder.append(String.format(Locale.US, "vt %.6f %.6f%n", triangle.u[i], triangle.v[i]));
							triangle.textureIndices[i] = textureOffset++;
						}
					}
					objBuilder.append("f");
					for (int i = 0; i < 3; i++) {
						objBuilder.append(' ').append(vertexOffset + triangle.vertexIndices[i]);
						if (triangle.hasUv) {
							objBuilder.append('/').append(triangle.textureIndices[i]).append('/').append(triangle.normalIndices[i]);
						} else {
							objBuilder.append("//").append(triangle.normalIndices[i]);
						}
					}
					objBuilder.append('\n');
				}

				vertexOffset += object.vertices.size();
			}

			return new ConvertedModel(objBuilder.toString(), mtlBuilder.toString(), modelParts);
		}

		private String nextNonEmptyLine() {
			while (index < lines.length) {
				final String line = lines[index++].trim();
				if (!line.isEmpty()) {
					return line;
				}
			}
			return null;
		}

		private void parseMaterialChunk() {
			index++;
			while (index < lines.length) {
				final String line = lines[index++].trim();
				if (StringUtils.equals(line, "}")) {
					return;
				}
				if (!line.isEmpty()) {
					materials.add(Material.parse(line));
				}
			}
			throw new IllegalArgumentException("Unclosed MQO Material chunk");
		}

		private void parseObjectChunk() {
			final String objectHeader = lines[index++].trim();
			final MqoObject object = new MqoObject(extractQuotedOrFirstToken(objectHeader.substring("Object".length()).replace("{", "").trim()));

			while (index < lines.length) {
				final String line = lines[index].trim();
				if (StringUtils.equals(line, "}")) {
					index++;
					objects.add(object);
					return;
				} else if (line.startsWith("visible ")) {
					object.visible = !line.endsWith(" 0");
					index++;
				} else if (line.startsWith("shading ")) {
					object.shading = parseInt(line, object.shading);
					index++;
				} else if (line.startsWith("facet ")) {
					object.facet = parseDouble(line, object.facet);
					index++;
				} else if (line.startsWith("vertex ")) {
					parseVertexChunk(object);
				} else if (line.startsWith("face ")) {
					parseFaceChunk(object);
				} else if (line.startsWith("BVertex ")) {
					throw new IllegalArgumentException("Unsupported MQO BVertex chunk");
				} else if ((line.startsWith("patch ") || line.startsWith("lathe ") || line.startsWith("mirror ")) && isNonZeroSetting(line)) {
					throw new IllegalArgumentException("Unsupported MQO object setting: " + line);
				} else if (line.endsWith("{")) {
					skipChunk();
				} else {
					index++;
				}
			}
			throw new IllegalArgumentException("Unclosed MQO Object chunk");
		}

		private void parseVertexChunk(MqoObject object) {
			index++;
			while (index < lines.length) {
				final String line = lines[index++].trim();
				if (StringUtils.equals(line, "}")) {
					return;
				}
				final String[] splitLine = line.split("\\s+");
				if (splitLine.length >= 3) {
					object.vertices.add(new Vertex(parseDouble(splitLine[0]), parseDouble(splitLine[1]), parseDouble(splitLine[2])));
				}
			}
			throw new IllegalArgumentException("Unclosed MQO vertex chunk");
		}

		private void parseFaceChunk(MqoObject object) {
			index++;
			while (index < lines.length) {
				final String line = lines[index++].trim();
				if (StringUtils.equals(line, "}")) {
					return;
				}
				if (!line.isEmpty()) {
					object.faces.add(Face.parse(line));
				}
			}
			throw new IllegalArgumentException("Unclosed MQO face chunk");
		}

		private void skipChunk() {
			int depth = 0;
			while (index < lines.length) {
				final String line = lines[index++];
				for (int i = 0; i < line.length(); i++) {
					final char character = line.charAt(i);
					if (character == '{') {
						depth++;
					} else if (character == '}') {
						depth--;
					}
				}
				if (depth <= 0) {
					return;
				}
			}
			throw new IllegalArgumentException("Unclosed MQO chunk");
		}

		private static String getMaterialName(int index) {
			return "mqo_material_" + index;
		}

		private static double parseDouble(String value) {
			return Double.parseDouble(value);
		}

		private static double parseDouble(String line, double fallback) {
			try {
				return Double.parseDouble(line.trim().split("\\s+")[1]);
			} catch (Exception ignored) {
				return fallback;
			}
		}

		private static int parseInt(String line, int fallback) {
			try {
				return Integer.parseInt(line.trim().split("\\s+")[1]);
			} catch (Exception ignored) {
				return fallback;
			}
		}
	}

	private static final class Material {

		private final double red;
		private final double green;
		private final double blue;
		private final double alpha;
		private final double diffuse;
		private final String texture;

		private Material(double red, double green, double blue, double alpha, double diffuse, String texture) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
			this.diffuse = diffuse;
			this.texture = texture;
		}

		private static Material parse(String line) {
			double red = 1;
			double green = 1;
			double blue = 1;
			double alpha = 1;
			double diffuse = 1;

			final String color = getParameter(line, "col");
			if (!color.isEmpty()) {
				final String[] colorSplit = color.split("\\s+");
				if (colorSplit.length >= 4) {
					red = Double.parseDouble(colorSplit[0]);
					green = Double.parseDouble(colorSplit[1]);
					blue = Double.parseDouble(colorSplit[2]);
					alpha = Double.parseDouble(colorSplit[3]);
				}
			}

			final String diffuseString = getParameter(line, "dif");
			if (!diffuseString.isEmpty()) {
				diffuse = Double.parseDouble(diffuseString);
			}

			final String texture = extractQuotedOrFirstToken(getParameter(line, "tex"));
			return new Material(red, green, blue, alpha, diffuse, texture);
		}
	}

	private static final class MqoObject {

		private final String name;
		private final ObjectArrayList<Vertex> vertices = new ObjectArrayList<>();
		private final ObjectArrayList<Face> faces = new ObjectArrayList<>();
		private boolean visible = true;
		private int shading = 1;
		private double facet = 59.5;

		private MqoObject(String name) {
			this.name = name;
		}
	}

	private static Vector getSmoothedNormal(ObjectArrayList<Triangle> triangles, Triangle baseTriangle, int vertexIndex, double facet) {
		final double minDot = Math.cos(Math.toRadians(facet));
		final Vector combinedNormal = new Vector(0, 0, 0);
		triangles.forEach(triangle -> {
			if (triangle.hasVertex(vertexIndex) && baseTriangle.normal.dot(triangle.normal) + 1E-6 >= minDot) {
				combinedNormal.add(triangle.normal);
			}
		});
		return combinedNormal.normalizeOr(baseTriangle.normal);
	}

	private static final class Triangle {

		private final int[] vertexIndices = new int[3];
		private final int[] textureIndices = new int[3];
		private final int[] normalIndices = new int[3];
		private final double[] u = new double[3];
		private final double[] v = new double[3];
		private final int materialIndex;
		private final boolean hasUv;
		private final Vector normal;

		private Triangle(ObjectArrayList<Vertex> vertices, Face face, int index1, int index2, int index3) {
			final int[] sourceIndices = {index1, index2, index3};
			for (int i = 0; i < 3; i++) {
				final int sourceIndex = sourceIndices[i];
				vertexIndices[i] = face.vertexIndices.get(sourceIndex);
				if (face.uvCoordinates.size() >= face.vertexIndices.size() * 2) {
					u[i] = face.uvCoordinates.get(sourceIndex * 2);
					v[i] = face.uvCoordinates.get(sourceIndex * 2 + 1);
				}
			}
			materialIndex = face.materialIndex;
			hasUv = face.uvCoordinates.size() >= face.vertexIndices.size() * 2;
			normal = Vector.normal(vertices.get(vertexIndices[0]), vertices.get(vertexIndices[1]), vertices.get(vertexIndices[2]));
		}

		private boolean hasVertex(int vertexIndex) {
			return vertexIndices[0] == vertexIndex || vertexIndices[1] == vertexIndex || vertexIndices[2] == vertexIndex;
		}
	}

	private static final class Vector {

		private double x;
		private double y;
		private double z;

		private Vector(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		private static Vector normal(Vertex vertex1, Vertex vertex2, Vertex vertex3) {
			final double ax = vertex2.x - vertex1.x;
			final double ay = vertex2.y - vertex1.y;
			final double az = vertex2.z - vertex1.z;
			final double bx = vertex3.x - vertex1.x;
			final double by = vertex3.y - vertex1.y;
			final double bz = vertex3.z - vertex1.z;
			return new Vector(ay * bz - az * by, az * bx - ax * bz, ax * by - ay * bx).normalizeOr(new Vector(0, 1, 0));
		}

		private void add(Vector vector) {
			x += vector.x;
			y += vector.y;
			z += vector.z;
		}

		private double dot(Vector vector) {
			return x * vector.x + y * vector.y + z * vector.z;
		}

		private Vector normalizeOr(Vector fallback) {
			final double length = Math.sqrt(x * x + y * y + z * z);
			if (length <= 0) {
				return fallback;
			} else {
				return new Vector(x / length, y / length, z / length);
			}
		}
	}

	private static final class Vertex {

		private final double x;
		private final double y;
		private final double z;

		private Vertex(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static final class Face {

		private final ObjectArrayList<Integer> vertexIndices = new ObjectArrayList<>();
		private final ObjectArrayList<Double> uvCoordinates = new ObjectArrayList<>();
		private final int materialIndex;

		private Face(int materialIndex) {
			this.materialIndex = materialIndex;
		}

		private static Face parse(String line) {
			final String[] splitLine = line.split("\\s+", 2);
			final int vertexCount = Integer.parseInt(splitLine[0]);
			final int materialIndex = parseIntParameter(getParameter(line, "M"), -1);
			final Face face = new Face(materialIndex);

			final String vertexIndices = getParameter(line, "V");
			if (!vertexIndices.isEmpty()) {
				final String[] vertexIndexSplit = vertexIndices.split("\\s+");
				for (int i = 0; i < Math.min(vertexCount, vertexIndexSplit.length); i++) {
					face.vertexIndices.add(Integer.parseInt(vertexIndexSplit[i]));
				}
			}

			final String uvCoordinates = getParameter(line, "UV");
			if (!uvCoordinates.isEmpty()) {
				for (final String uvCoordinate : uvCoordinates.split("\\s+")) {
					face.uvCoordinates.add(Double.parseDouble(uvCoordinate));
				}
			}

			return face;
		}

		private static int parseIntParameter(String value, int fallback) {
			try {
				return value.isEmpty() ? fallback : Integer.parseInt(value);
			} catch (Exception ignored) {
				return fallback;
			}
		}
	}
}
