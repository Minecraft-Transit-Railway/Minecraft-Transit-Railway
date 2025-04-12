package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.model.RawMesh;
import org.mtr.model.render.model.RawModel;
import org.mtr.model.render.obj.AtlasManager;
import org.mtr.model.render.obj.ObjModelLoader;
import org.mtr.model.render.object.VertexArray;
import org.mtr.model.render.vertex.CapturingVertexConsumer;
import org.mtr.model.render.vertex.VertexAttributeMapping;
import org.mtr.model.render.vertex.VertexAttributeSource;
import org.mtr.model.render.vertex.VertexAttributeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class OptimizedModel {

	final List<VertexArray> uploadedParts;
	private static final AtlasManager ATLAS_MANAGER = new AtlasManager();

	private static final VertexAttributeMapping DEFAULT_MAPPING = new VertexAttributeMapping.Builder()
			.set(VertexAttributeType.POSITION, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.COLOR, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_TEXTURE, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.UV_OVERLAY, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_LIGHTMAP, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.NORMAL, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.MATRIX_MODEL, VertexAttributeSource.GLOBAL)
			.build();

	public static OptimizedModel fromMaterialGroups(Collection<MaterialGroup> materialGroups) {
		final CapturingVertexConsumer capturingVertexConsumer = new CapturingVertexConsumer();

		materialGroups.forEach(materialGroup -> {
			capturingVertexConsumer.beginStage(materialGroup.materialProperties);
			materialGroup.modelPartConsumers.forEach(modelPartConsumer -> modelPartConsumer.accept(capturingVertexConsumer));
		});

		capturingVertexConsumer.rawModel.triangulate();
		final RawModel rawModel = new RawModel();
		capturingVertexConsumer.rawModel.iterateRawMeshList(rawModel::append);
		rawModel.distinct();
		return new OptimizedModel(rawModel.upload(DEFAULT_MAPPING));
	}

	public static OptimizedModel fromObjModels(Collection<ObjModel> objModels) {
		final List<VertexArray> uploadedParts = new ArrayList<>();
		objModels.forEach(objModel -> {
			objModel.rawModel.generateNormals();
			objModel.rawModel.distinct();
			uploadedParts.addAll(objModel.rawModel.upload(DEFAULT_MAPPING));
		});
		return new OptimizedModel(uploadedParts);
	}

	private OptimizedModel(List<VertexArray> uploadedParts) {
		this.uploadedParts = uploadedParts;
	}

	public OptimizedModel(OptimizedModel... optimizedModels) {
		uploadedParts = new ArrayList<>();
		for (final OptimizedModel optimizedModel : optimizedModels) {
			uploadedParts.addAll(optimizedModel.uploadedParts);
		}
	}

	public static final class MaterialGroup {

		private final MaterialProperties materialProperties;
		private final List<Consumer<CapturingVertexConsumer>> modelPartConsumers = new ArrayList<>();

		public MaterialGroup(ShaderType shaderType, Identifier texture) {
			materialProperties = new MaterialProperties(shaderType, texture, null);
		}

		public void addCube(ModelPart modelPart, double x, double y, double z, boolean flipped, int light) {
			modelPartConsumers.add(capturingVertexConsumer -> {
				final MatrixStack matrixStack = new MatrixStack();
				matrixStack.translate(x, y, z);
				if (flipped) {
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				modelPart.render(matrixStack, capturingVertexConsumer, light, OverlayTexture.DEFAULT_UV);
			});
		}
	}

	public static final class ObjModel {

		public final NewOptimizedModelGroup newOptimizedModelGroup;
		private final float minX;
		private final float minY;
		private final float minZ;
		private final float maxX;
		private final float maxY;
		private final float maxZ;
		private final List<RawMesh> rawMeshes;
		private final RawModel rawModel = new RawModel();

		private ObjModel(
				NewOptimizedModelGroup newOptimizedModelGroup, List<RawMesh> rawMeshes, boolean flipTextureV,
				float minX, float minY, float minZ,
				float maxX, float maxY, float maxZ
		) {
			this.newOptimizedModelGroup = newOptimizedModelGroup;
			if (flipTextureV) {
				rawMeshes.forEach(rawMesh -> rawMesh.applyUVMirror(false, true));
			}
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
			this.rawMeshes = rawMeshes;
		}

		public static Object2ObjectAVLTreeMap<String, ObjModel> loadModel(String objString, Function<String, String> mtlResolver, Function<String, Identifier> textureResolver, @Nullable Identifier atlasIndex, boolean splitModel, boolean flipTextureV) {
			if (atlasIndex != null) {
				ATLAS_MANAGER.load(atlasIndex);
			}

			final Object2ObjectAVLTreeMap<String, ObjModel> objModels = new Object2ObjectAVLTreeMap<>();
			ObjModelLoader.loadModel(objString, mtlResolver, textureResolver, ATLAS_MANAGER, splitModel, flipTextureV).forEach((key, rawMeshes) -> {
				final float[] bounds = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
				rawMeshes.left().forEach(rawMesh -> {
					rawMesh.applyRotation(new Vector3f(1, 0, 0), 180);
					rawMesh.vertices.forEach(vertex -> {
						final float x = vertex.position.x;
						final float y = vertex.position.y;
						final float z = vertex.position.z;
						bounds[0] = Math.min(bounds[0], x);
						bounds[1] = Math.min(bounds[1], y);
						bounds[2] = Math.min(bounds[2], z);
						bounds[3] = Math.max(bounds[3], x);
						bounds[4] = Math.max(bounds[4], y);
						bounds[5] = Math.max(bounds[5], z);
					});
				});
				objModels.put(key, new ObjModel(rawMeshes.right(), rawMeshes.left(), flipTextureV, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]));
			});

			return objModels;
		}

		public void addTransformation(ShaderType shaderType, double x, double y, double z, boolean flipped) {
			rawMeshes.forEach(rawMesh -> {
				final RawMesh newRawMesh = new RawMesh(shaderType, rawMesh);
				newRawMesh.applyTranslation((float) x, (float) y, (float) z);
				if (flipped) {
					newRawMesh.applyRotation(new Vector3f(0, 1, 0), 180);
				}
				rawModel.append(newRawMesh);
			});
		}

		public void applyTranslation(double x, double y, double z) {
			rawMeshes.forEach(rawMesh -> rawMesh.applyTranslation((float) x, (float) y, (float) z));
		}

		public void applyRotation(double x, double y, double z) {
			rawMeshes.forEach(rawMesh -> {
				rawMesh.applyRotation(new Vector3f(1, 0, 0), (float) x);
				rawMesh.applyRotation(new Vector3f(0, 1, 0), (float) y);
				rawMesh.applyRotation(new Vector3f(0, 0, 1), (float) z);
			});
		}

		public void applyScale(double x, double y, double z) {
			rawMeshes.forEach(rawMesh -> rawMesh.applyScale((float) x, (float) y, (float) z));
		}

		public void applyMirror(boolean x, boolean y, boolean z) {
			rawMeshes.forEach(rawMesh -> rawMesh.applyMirror(x, y, z, x, y, z));
		}

		public float getMinX() {
			return minX;
		}

		public float getMinY() {
			return minY;
		}

		public float getMinZ() {
			return minZ;
		}

		public float getMaxX() {
			return maxX;
		}

		public float getMaxY() {
			return maxY;
		}

		public float getMaxZ() {
			return maxZ;
		}
	}

	public enum ShaderType {
		CUTOUT, TRANSLUCENT,
		CUTOUT_BRIGHT, TRANSLUCENT_BRIGHT,
		CUTOUT_GLOWING, TRANSLUCENT_GLOWING
	}
}
