package org.mtr.model;

import de.javagl.obj.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mtr.MTR;
import org.mtr.resource.RenderStage;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Function;

public final class ObjModelLoader {

	public static Object2ObjectOpenHashMap<String, NewOptimizedModelGroup> loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, boolean splitModel, boolean flipTextureV) {
		final Object2ObjectOpenHashMap<String, NewOptimizedModelGroup> result = new Object2ObjectOpenHashMap<>();

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
				ObjSplitting.splitByGroups(sourceObj).forEach((key, obj) -> result.put(key, loadModel(obj, materials, textureResolver, flipTextureV)));
			} else {
				result.put("", loadModel(sourceObj, materials, textureResolver, flipTextureV));
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}

		return result;
	}

	private static NewOptimizedModelGroup loadModel(Obj sourceObj, Object2ObjectOpenHashMap<String, Mtl> materials, Function<String, Identifier> textureResolver, boolean flipTextureV) {
		final NewOptimizedModelGroup newOptimizedModelGroup = new NewOptimizedModelGroup();

		ObjSplitting.splitByMaterialGroups(sourceObj).forEach((key, obj) -> {
			if (obj.getNumFaces() > 0) {
				final Object2ObjectOpenHashMap<String, String> materialOptions = splitMaterialOptions(key);
				final String materialGroupName = materialOptions.get("");
				final RenderStage renderStage = legacyMapping(materialOptions.getOrDefault("#", ""));
				final boolean newFlipTextureV = flipTextureV || materialOptions.getOrDefault("flipv", "0").equals("1");
				final String texture;
				final Color color;

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
						color = kd == null ? new Color(0xFF, 0xFF, 0xFF, 0xFF) : new Color(
								kd.getX(),
								kd.getY(),
								kd.getZ(),
								objMaterial.getD() == null ? 1 : objMaterial.getD()
						);
					}
				} else {
					texture = materialGroupName.equals("_") ? "" : materialGroupName;
					color = new Color(0xFF, 0xFF, 0xFF, 0xFF);
				}

				final Obj renderObjMesh = ObjUtils.convertToRenderable(obj);
				final ObjectArrayList<StoredVertexData> modifications = new ObjectArrayList<>();

				for (int i = 0; i < renderObjMesh.getNumFaces(); i++) {
					final ObjFace face = renderObjMesh.getFace(i);
					modifications.add(writeVertex(renderObjMesh, face, 0, newFlipTextureV, color));
					modifications.add(writeVertex(renderObjMesh, face, 1, newFlipTextureV, color));
					modifications.add(writeVertex(renderObjMesh, face, 2, newFlipTextureV, color));
				}

				newOptimizedModelGroup.add(
						renderStage,
						texture.isEmpty() && color != null ? Identifier.of(MTR.MOD_ID, "textures/block/white.png") : textureResolver.apply(texture.replace("\\\\", "/").replace("\\", "/")),
						storedVertexDataList -> storedVertexDataList.addAll(modifications)
				);
			}
		});

		return newOptimizedModelGroup;
	}

	private static StoredVertexData writeVertex(Obj obj, ObjFace face, int faceVertexIndex, boolean flipTextureV, @Nullable Color color) {
		final FloatTuple vertex = obj.getVertex(face.getVertexIndex(faceVertexIndex));
		final FloatTuple uv = obj.getTexCoord(face.getTexCoordIndex(faceVertexIndex));
		final FloatTuple normal = obj.getNormal(face.getNormalIndex(faceVertexIndex));
		return new StoredVertexData(
				vertex.getX(), vertex.getY(), vertex.getZ(),
				color == null ? new Color(0xFF, 0xFF, 0xFF, 0xFF) : color,
				uv.getX(), flipTextureV ? 1 - uv.getY() : uv.getY(),
				normal.getX(), normal.getY(), normal.getZ()
		);
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

	private static RenderStage legacyMapping(String type) {
		return switch (type.toLowerCase(Locale.ENGLISH)) {
			case "light" -> RenderStage.LIGHT;
			case "always_on_light" -> RenderStage.ALWAYS_ON_LIGHT;
			case "interior" -> RenderStage.INTERIOR;
			case "interior_translucent" -> RenderStage.INTERIOR_TRANSLUCENT;
			default -> RenderStage.EXTERIOR;
		};
	}
}
