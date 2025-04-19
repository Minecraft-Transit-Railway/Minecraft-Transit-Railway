package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.model.BlockbenchModelLoader;
import org.mtr.model.NewOptimizedModel;
import org.mtr.model.ObjModelLoader;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public interface StoredModelResourceBase {

	default Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> load(String modelResource, String textureResource, boolean flipTextureV, double modelYOffset, ResourceProvider resourceProvider) {
		final boolean isBlockbench = modelResource.endsWith(".bbmodel");
		final boolean isObj = modelResource.endsWith(".obj");
		final Identifier defaultTexture = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> models;

		if (isBlockbench) {
			final BlockbenchModelLoader blockbenchModelLoader = new BlockbenchModelLoader(defaultTexture);
			blockbenchModelLoader.loadModel(new BlockbenchModel(new JsonReader(Utilities.parseJson(resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "bbmodel"))))));
			models = blockbenchModelLoader.build();
		} else if (isObj) {
			final ObjModelLoader objModelLoader = new ObjModelLoader(defaultTexture);
			objModelLoader.loadModel(
					resourceProvider.get(CustomResourceTools.formatIdentifierWithDefault(modelResource, "obj")),
					mtlString -> resourceProvider.get(CustomResourceTools.getResourceFromSamePath(modelResource, mtlString, "mtl")),
					textureString -> StringUtils.isEmpty(textureString) ? defaultTexture : CustomResourceTools.getResourceFromSamePath(modelResource, textureString, "png"),
					true, flipTextureV
			);
			// TODO transform object if needed
			models = objModelLoader.build();
		} else {
			models = null;
		}

		return models;
	}

	default void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> models = getOptimizedModel();
		if (models != null) {
			MainRenderer.renderModel(models, storedMatrixTransformations, light);
		}
	}

	@Nullable
	Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel();

	void preload();
}
