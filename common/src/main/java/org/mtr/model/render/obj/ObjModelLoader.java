package org.mtr.model.render.obj;

import de.javagl.obj.*;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3f;
import org.mtr.MTR;
import org.mtr.model.OptimizedModel;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.model.Face;
import org.mtr.model.render.model.RawMesh;
import org.mtr.model.render.vertex.Vertex;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.function.Function;

public final class ObjModelLoader {

	public static Map<String, List<RawMesh>> loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, AtlasManager atlasManager, boolean splitModel) {
		final Map<String, List<RawMesh>> result = new HashMap<>();

		try {
			final Obj sourceObj = ObjReader.read(IOUtils.toInputStream(objString, StandardCharsets.UTF_8));

			final Map<String, Mtl> materials = new HashMap<>();
			sourceObj.getMtlFileNames().forEach(mtlFileName -> {
				try {
					MtlReader.read(IOUtils.toInputStream(mtlResolver.apply(mtlFileName.replace("\\\\", "/").replace("\\", "/")), StandardCharsets.UTF_8)).forEach(mtl -> materials.put(mtl.getName(), mtl));
				} catch (Exception e) {
					MTR.LOGGER.error("", e);
				}
			});

			if (splitModel) {
				ObjSplitting.splitByGroups(sourceObj).forEach((key, obj) -> result.put(key, loadModel(obj, materials, textureResolver, atlasManager)));
			} else {
				result.put("", loadModel(sourceObj, materials, textureResolver, atlasManager));
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}

		return result;
	}

	private static List<RawMesh> loadModel(Obj sourceObj, Map<String, Mtl> materials, Function<String, Identifier> textureResolver, AtlasManager atlasManager) {
		final List<RawMesh> rawMeshes = new ArrayList<>();

		ObjSplitting.splitByMaterialGroups(sourceObj).forEach((key, obj) -> {
			if (obj.getNumFaces() > 0) {
				final Map<String, String> materialOptions = splitMaterialOptions(key);
				final String materialGroupName = materialOptions.get("");
				final OptimizedModel.ShaderType shaderType = legacyMapping(materialOptions.getOrDefault("#", ""));
				final boolean flipTextureV = materialOptions.getOrDefault("flipv", "0").equals("1");
				final String texture;
				final Integer color;

				if (!materials.isEmpty()) {
					final Mtl objMaterial = materials.getOrDefault(key, null);
					if (objMaterial == null) {
						texture = "";
						color = null;
					} else {
						if (StringUtils.isEmpty(objMaterial.getMapKd())) {
							texture = "";
						} else {
							texture = objMaterial.getMapKd();
						}
						final FloatTuple kd = objMaterial.getKd();
						color = kd == null ? mergeColor(0xFF, 0xFF, 0xFF, 0xFF) : mergeColor((int) (kd.getX() * 0xFF), (int) (kd.getY() * 0xFF), (int) (kd.getZ() * 0xFF), objMaterial.getD() == null ? 0xFF : (int) (objMaterial.getD() * 0xFF));
					}
				} else {
					texture = materialGroupName.equals("_") ? "" : materialGroupName;
					color = mergeColor(0xFF, 0xFF, 0xFF, 0xFF);
				}

				final Obj renderObjMesh = ObjUtils.convertToRenderable(obj);
				final RawMesh mesh = new RawMesh(new MaterialProperties(shaderType, textureResolver.apply(texture.replace("\\\\", "/").replace("\\", "/")), color));

				for (int i = 0; i < renderObjMesh.getNumVertices(); i++) {
					final FloatTuple pos = renderObjMesh.getVertex(i);
					final FloatTuple normal;
					final FloatTuple uv;
					if (i < renderObjMesh.getNumNormals()) {
						normal = renderObjMesh.getNormal(i);
					} else {
						normal = ZeroFloatTuple.ZERO3;
					}
					if (i < renderObjMesh.getNumTexCoords()) {
						uv = renderObjMesh.getTexCoord(i);
					} else {
						uv = ZeroFloatTuple.ZERO2;
					}
					final Vertex seVertex = new Vertex();
					seVertex.position = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
					seVertex.normal = new Vector3f(normal.getX(), normal.getY(), normal.getZ());
					seVertex.u = uv.getX();
					seVertex.v = flipTextureV ? 1 - uv.getY() : uv.getY();
					mesh.vertices.add(seVertex);
				}

				for (int i = 0; i < renderObjMesh.getNumFaces(); i++) {
					final ObjFace face = renderObjMesh.getFace(i);
					mesh.faces.add(new Face(new int[]{face.getVertexIndex(0), face.getVertexIndex(1), face.getVertexIndex(2)}));
				}

				atlasManager.applyToMesh(mesh);
				mesh.validateVertexIndex();
				rawMeshes.add(mesh);
			}
		});

		return rawMeshes;
	}

	private static Map<String, String> splitMaterialOptions(String source) {
		final Map<String, String> result = new HashMap<>();
		final String[] majorParts = source.split("#", 2);
		result.put("", majorParts[0]);
		if (majorParts.length > 1) {
			for (final String minorPart : majorParts[1].split(",")) {
				final String[] tokens = minorPart.split("=", 2);
				if (tokens.length > 1) {
					result.put(tokens[0], tokens[1]);
				} else if (!result.containsKey("#")) {
					result.put("#", tokens[0]);
				} else {
					result.put(tokens[0].toLowerCase(Locale.ROOT), "1");
				}
			}
		}
		return result;
	}

	private static int mergeColor(int r, int g, int b, int a) {
		return r << 24 | g << 16 | b << 8 | a;
	}

	public static Identifier resolveRelativePath(Identifier baseFile, String relative, @Nullable String expectExtension) {
		String result = relative.toLowerCase(Locale.ROOT).replace('\\', '/');

		if (result.contains(":")) {
			result = result.replaceAll("[^a-z0-9/.:_-]", "_");
			return Identifier.of(result);
		}

		result = result.replaceAll("[^a-z0-9/._-]", "_");

		if (result.endsWith(".jpg") || result.endsWith(".bmp") || result.endsWith(".tga")) {
			result = result.substring(0, result.length() - 4) + ".png";
		}

		if (expectExtension != null && !result.endsWith(expectExtension)) {
			result += expectExtension;
		}

		return Identifier.of(baseFile.getNamespace(), FileSystems.getDefault().getPath(baseFile.getPath()).getParent().resolve(result).normalize().toString().replace('\\', '/'));
	}

	private static OptimizedModel.ShaderType legacyMapping(String type) {
		return switch (type.toLowerCase(Locale.ENGLISH)) {
			case "light" -> OptimizedModel.ShaderType.CUTOUT_GLOWING;
			case "always_on_light" -> OptimizedModel.ShaderType.TRANSLUCENT_GLOWING;
			case "interior" -> OptimizedModel.ShaderType.CUTOUT_BRIGHT;
			case "interior_translucent" -> OptimizedModel.ShaderType.TRANSLUCENT_BRIGHT;
			default -> OptimizedModel.ShaderType.CUTOUT;
		};
	}

	private record ZeroFloatTuple(int dimensions) implements FloatTuple {

		public static final ZeroFloatTuple ZERO2 = new ZeroFloatTuple(2);
		public static final ZeroFloatTuple ZERO3 = new ZeroFloatTuple(3);

		@Override
		public float getX() {
			return 0;
		}

		@Override
		public float getY() {
			return 0;
		}

		@Override
		public float getZ() {
			return 0;
		}

		@Override
		public float getW() {
			return 0;
		}

		@Override
		public float get(int index) {
			return 0;
		}
	}
}
