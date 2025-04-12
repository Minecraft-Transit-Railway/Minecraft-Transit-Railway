package org.mtr.model.render.obj;

import de.javagl.obj.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3f;
import org.mtr.MTR;
import org.mtr.model.NewOptimizedModelGroup;
import org.mtr.model.OptimizedModel;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.model.Face;
import org.mtr.model.render.model.RawMesh;
import org.mtr.model.render.vertex.Vertex;
import org.mtr.resource.RenderStage;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ObjModelLoader {

	public static Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ObjectArrayList<RawMesh>, NewOptimizedModelGroup>> loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, AtlasManager atlasManager, boolean splitModel, boolean flipTextureV) {
		final Object2ObjectOpenHashMap<String, ObjectObjectImmutablePair<ObjectArrayList<RawMesh>, NewOptimizedModelGroup>> result = new Object2ObjectOpenHashMap<>();

		try {
			final Obj sourceObj = ObjReader.read(IOUtils.toInputStream(objString, StandardCharsets.UTF_8));

			final Object2ObjectOpenHashMap<String, Mtl> materials = new Object2ObjectOpenHashMap<>();
			sourceObj.getMtlFileNames().forEach(mtlFileName -> {
				try {
					MtlReader.read(IOUtils.toInputStream(mtlResolver.apply(mtlFileName.replace("\\\\", "/").replace("\\", "/")), StandardCharsets.UTF_8)).forEach(mtl -> materials.put(mtl.getName(), mtl));
				} catch (Exception e) {
					MTR.LOGGER.error("", e);
				}
			});

			if (splitModel) {
				ObjSplitting.splitByGroups(sourceObj).forEach((key, obj) -> result.put(key, loadModel(obj, materials, textureResolver, atlasManager, flipTextureV)));
			} else {
				result.put("", loadModel(sourceObj, materials, textureResolver, atlasManager, flipTextureV));
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}

		return result;
	}

	private static ObjectObjectImmutablePair<ObjectArrayList<RawMesh>, NewOptimizedModelGroup> loadModel(Obj sourceObj, Object2ObjectOpenHashMap<String, Mtl> materials, Function<String, Identifier> textureResolver, AtlasManager atlasManager, boolean flipTextureV) {
		final ObjectArrayList<RawMesh> rawMeshes = new ObjectArrayList<>();
		final NewOptimizedModelGroup newOptimizedModelGroup = new NewOptimizedModelGroup();

		ObjSplitting.splitByMaterialGroups(sourceObj).forEach((key, obj) -> {
			if (obj.getNumFaces() > 0) {
				final Object2ObjectOpenHashMap<String, String> materialOptions = splitMaterialOptions(key);
				final String materialGroupName = materialOptions.get("");
				final OptimizedModel.ShaderType shaderType = legacyMapping(materialOptions.getOrDefault("#", ""));
				final boolean newFlipTextureV = flipTextureV || materialOptions.getOrDefault("flipv", "0").equals("1");
				System.out.println(newFlipTextureV);
				System.out.println(key);
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
				final Identifier textureId = textureResolver.apply(texture.replace("\\\\", "/").replace("\\", "/"));
				final RawMesh mesh = new RawMesh(new MaterialProperties(shaderType, textureId, color));
				final ObjectArrayList<Consumer<VertexConsumer>> modifications = new ObjectArrayList<>();

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
					seVertex.v = newFlipTextureV ? 1 - uv.getY() : uv.getY();
					mesh.vertices.add(seVertex);
				}

				for (int i = 0; i < renderObjMesh.getNumFaces(); i++) {
					final ObjFace face = renderObjMesh.getFace(i);
					modifications.add(vertexConsumer -> {
						writeVertex(renderObjMesh, face, 0, newFlipTextureV, vertexConsumer);
						writeVertex(renderObjMesh, face, 1, newFlipTextureV, vertexConsumer);
						writeVertex(renderObjMesh, face, 2, newFlipTextureV, vertexConsumer);
					});
					mesh.faces.add(new Face(new int[]{face.getVertexIndex(0), face.getVertexIndex(1), face.getVertexIndex(2)}));
				}

				newOptimizedModelGroup.add(Arrays.stream(RenderStage.values()).filter(a -> a.shaderType == shaderType).findFirst().orElse(RenderStage.EXTERIOR), textureId, vertexConsumer -> {
					modifications.forEach(modification -> modification.accept(vertexConsumer));
				});

				atlasManager.applyToMesh(mesh);
				mesh.validateVertexIndex();
				rawMeshes.add(mesh);
			}
		});

		return new ObjectObjectImmutablePair<>(rawMeshes, newOptimizedModelGroup);
	}

	private static void writeVertex(Obj obj, ObjFace face, int faceVertexIndex, boolean flipTextureV, VertexConsumer vertexConsumer) {
		final FloatTuple vertex = obj.getVertex(face.getVertexIndex(faceVertexIndex));
		vertexConsumer.vertex(vertex.getX(), vertex.getY(), vertex.getZ());
		final FloatTuple uv = obj.getTexCoord(face.getTexCoordIndex(faceVertexIndex));
		vertexConsumer.texture(uv.getX(), flipTextureV ? 1 - uv.getY() : uv.getY());
		final FloatTuple normal = obj.getNormal(face.getNormalIndex(faceVertexIndex));
		vertexConsumer.normal(normal.getX(), normal.getY(), normal.getZ());
	}

	private static Object2ObjectOpenHashMap<String, String> splitMaterialOptions(String source) {
		final Object2ObjectOpenHashMap<String, String> result = new Object2ObjectOpenHashMap<>();
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

		@Override
		public int getDimensions() {
			return 0;
		}
	}
}
