package org.mtr.model;

import de.javagl.obj.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mtr.MTR;
import org.mtr.render.MainRenderer;
import org.mtr.resource.RenderStage;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Function;

public final class ObjModelLoader extends ModelLoaderBase {

	public ObjModelLoader(Identifier defaultTexture) {
		super(defaultTexture, VertexFormat.DrawMode.TRIANGLES);
	}

	public void loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, boolean splitModel, boolean flipTextureV) {
		if (canLoadModel()) {
			try {
				final Obj sourceObj = ObjReader.read(IOUtils.toInputStream(objString, StandardCharsets.UTF_8));
				final Object2ObjectOpenHashMap<String, Mtl> materials = new Object2ObjectOpenHashMap<>();
				sourceObj.getMtlFileNames().forEach(mtlFileName -> {
					try {
						MtlReader.read(IOUtils.toInputStream(mtlResolver.apply(formatFilePath(mtlFileName)), StandardCharsets.UTF_8)).forEach(mtl -> materials.put(mtl.getName(), mtl));
					} catch (Exception e) {
						MTR.LOGGER.error("", e);
					}
				});

				MainRenderer.WORKER_THREAD.worker.submit(() -> {
					if (splitModel) {
						ObjSplitting.splitByGroups(sourceObj).forEach((key, obj) -> addModel(key, loadModel(obj, materials, textureResolver, flipTextureV)));
					} else {
						addModel("", loadModel(sourceObj, materials, textureResolver, flipTextureV));
					}

					setModelLoaded();
				});
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}
	}

	private NewOptimizedModelGroup loadModel(Obj sourceObj, Object2ObjectOpenHashMap<String, Mtl> materials, Function<String, Identifier> textureResolver, boolean flipTextureV) {
		final NewOptimizedModelGroup newOptimizedModelGroup = new NewOptimizedModelGroup();

		ObjSplitting.splitByMaterialGroups(sourceObj).forEach((key, obj) -> {
			if (obj.getNumFaces() > 0) {
				final Object2ObjectOpenHashMap<String, String> materialOptions = splitMaterialOptions(key);
				final String materialGroupName = materialOptions.get("");
				final boolean newFlipTextureV = flipTextureV || materialOptions.getOrDefault("flipv", "0").equals("1");
				final Identifier texture;
				final Color color;

				if (!materials.isEmpty()) {
					final Mtl objMaterial = materials.get(key);
					if (objMaterial == null) {
						texture = null;
						color = null;
					} else {
						if (StringUtils.isEmpty(objMaterial.getMapKd())) {
							texture = null;
						} else {
							texture = textureResolver.apply(formatFilePath(objMaterial.getMapKd()));
						}
						final FloatTuple kd = objMaterial.getKd();
						color = kd == null ? null : new Color(
								kd.getX(),
								kd.getY(),
								kd.getZ(),
								objMaterial.getD() == null ? 1 : objMaterial.getD()
						);
					}
				} else {
					texture = materialGroupName.equals("_") ? null : textureResolver.apply(formatFilePath(materialGroupName));
					color = null;
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
						getRenderStage(materialOptions.get("#")),
						texture == null && color != null ? Identifier.of(MTR.MOD_ID, "textures/block/white.png") : texture == null ? defaultTexture : texture,
						storedVertexDataList -> storedVertexDataList.addAll(modifications),
						null
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

	private static String formatFilePath(String path) {
		return path.replace("\\\\", "/").replace("\\", "/");
	}

	@Nullable
	private static RenderStage getRenderStage(@Nullable String type) {
		if (type == null) {
			return null;
		} else {
			return switch (type.toLowerCase(Locale.ENGLISH)) {
				case "light" -> RenderStage.LIGHT;
				case "always_on_light" -> RenderStage.ALWAYS_ON_LIGHT;
				case "interior" -> RenderStage.INTERIOR;
				case "interior_translucent" -> RenderStage.INTERIOR_TRANSLUCENT;
				default -> null;
			};
		}
	}
}
