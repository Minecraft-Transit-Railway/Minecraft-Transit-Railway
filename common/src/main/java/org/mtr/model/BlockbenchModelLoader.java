package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.mtr.MTR;
import org.mtr.resource.*;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public final class BlockbenchModelLoader extends ModelLoaderBase {

	public BlockbenchModelLoader(Identifier defaultTexture) {
		super(defaultTexture, VertexFormat.DrawMode.QUADS);
	}

	public void loadModel(BlockbenchModel blockbenchModel) {
		final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
		blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));

		blockbenchModel.getOutlines().forEach(blockbenchOutline -> {
			final ModelPartData modelPartData = new ModelData().getRoot();
			final NewOptimizedModelGroup newOptimizedModelGroup = new NewOptimizedModelGroup();
			final MutableBox mutableBox = new MutableBox();
			final ObjectArrayList<ModelDisplayPart> modelDisplayParts = new ObjectArrayList<>();

			iterateChildren(blockbenchOutline, null, new GroupTransformations(), (uuid, groupTransformations) -> {
				final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
				if (blockbenchElement != null) {
					final ModelDisplayPart modelDisplayPart = new ModelDisplayPart();
					modelDisplayParts.add(modelDisplayPart);
					mutableBox.add(blockbenchElement.setModelPart(modelPartData.addChild(MTR.randomString()), groupTransformations, modelDisplayPart));
				}
			});

			newOptimizedModelGroup.add(null, defaultTexture, storedVertexDataList -> StoredVertexData.write(modelPartData.createPart(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight()), storedVertexDataList), mutableBox.getAll());
			addModel(blockbenchOutline.getName(), newOptimizedModelGroup);

			if (!modelDisplayParts.isEmpty()) {
				addModelDisplayParts(blockbenchOutline.getName(), modelDisplayParts);
			}
		});
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, @Nullable BlockbenchOutline previousBlockbenchOutline, GroupTransformations groupTransformations, BiConsumer<String, GroupTransformations> consumer) {
		final GroupTransformations newGroupTransformations = blockbenchOutline.add(groupTransformations, previousBlockbenchOutline);
		blockbenchOutline.childrenUuid.forEach(uuid -> consumer.accept(uuid, newGroupTransformations));
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, blockbenchOutline, groupTransformations, consumer));
	}
}
