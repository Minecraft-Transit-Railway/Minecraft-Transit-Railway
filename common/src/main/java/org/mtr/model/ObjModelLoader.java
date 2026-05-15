package org.mtr.model;

import de.javagl.obj.*;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.render.MainRenderer;
import org.mtr.resource.RenderStage;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Function;

/**
 * Wavefront OBJ loader producing {@link NewOptimizedModelGroup}s grouped by material.
 *
 * <p>Parsing flow:</p>
 * <ol>
 *   <li>{@link #loadModel(String, java.util.function.Function, java.util.function.Function, boolean, boolean)}
 *       parses the {@code .obj} body and any referenced {@code .mtl} files on the calling
 *       thread, then hands the heavy face → vertex walk to
 *       {@link org.mtr.render.MainRenderer#WORKER_THREAD}'s virtual-thread executor.</li>
 *   <li>{@link ObjSplitting#splitByGroups} produces one group per OBJ group when
 *       {@code splitModel = true} (vehicles); otherwise a single unnamed group is emitted
 *       (rails / decorative objects).</li>
 *   <li>{@link ObjSplitting#splitByMaterialGroups} then splits each group again per
 *       material, with a custom suffix parser ({@link #splitMaterialOptions(String)}) that
 *       lets the author encode render-stage hints (e.g.
 *       {@code body#light}, {@code window#interior_translucent}) directly into material
 *       names.</li>
 * </ol>
 *
 * <p>See {@code docs/PERFORMANCE.md} §1.2 for the planned move of {@code ObjReader.read}
 * itself onto the worker thread.</p>
 */
public final class ObjModelLoader extends ModelLoaderBase {

	public ObjModelLoader(Identifier defaultTexture) {
		super(defaultTexture, VertexFormat.DrawMode.TRIANGLES);
	}

	/**
	 * Parse an OBJ source and submit its mesh-build to the worker thread.
	 *
	 * @param objString       raw OBJ file text
	 * @param mtlResolver     given an MTL file name (as it appears in {@code mtllib}),
	 *                        return its contents
	 * @param textureResolver given a texture path (as it appears in {@code map_Kd} or as
	 *                        the material group name when no MTL is supplied), return the
	 *                        Minecraft {@link Identifier} to bind
	 * @param splitModel      {@code true} to emit one group per OBJ {@code g} group
	 *                        (vehicles); {@code false} to merge all groups into one
	 *                        (rails / objects)
	 * @param flipTextureV    {@code true} to invert the V coordinate during vertex
	 *                        emission, for textures authored with the origin in the
	 *                        opposite corner
	 */
	public void loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, boolean splitModel, boolean flipTextureV) {
		if (canLoadModel()) {
			parseStarted();
			MainRenderer.WORKER_THREAD.worker.submit(() -> {
				try {
					final Obj sourceObj = ObjReader.read(IOUtils.toInputStream(objString, StandardCharsets.UTF_8));
					final Object2ObjectOpenHashMap<String, Mtl> materials = new Object2ObjectOpenHashMap<>();
					sourceObj.getMtlFileNames().forEach(mtlFileName -> {
						try {
							MtlReader.read(IOUtils.toInputStream(mtlResolver.apply(formatFilePath(mtlFileName)), StandardCharsets.UTF_8)).forEach(mtl -> materials.put(mtl.getName(), mtl));
						} catch (Exception e) {
							MTR.LOGGER.error("Failed to parse MTL file [{}] referenced by OBJ model", mtlFileName, e);
						}
					});

					if (splitModel) {
						ObjSplitting.splitByGroups(sourceObj).forEach((key, obj) -> addModel(key, loadModel(obj, materials, textureResolver, flipTextureV)));
					} else {
						addModel("", loadModel(sourceObj, materials, textureResolver, flipTextureV));
					}

					setModelLoaded();
				} catch (Exception e) {
					MTR.LOGGER.error("Failed to parse OBJ model for texture [{}]", defaultTexture, e);
				} finally {
					parseFinished();
				}
			});
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
