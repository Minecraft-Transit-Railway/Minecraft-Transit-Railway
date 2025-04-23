package org.mtr.model;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.mtr.MTR;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.BlockbenchElement;
import org.mtr.resource.BlockbenchModel;
import org.mtr.resource.BlockbenchOutline;
import org.mtr.resource.GroupTransformations;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public final class BlockbenchModelLoader extends ModelLoaderBase {

	public BlockbenchModelLoader(Identifier defaultTexture) {
		super(defaultTexture, VertexFormat.DrawMode.QUADS);
	}

	public void loadModel(BlockbenchModel blockbenchModel) {
		if (canLoadModel()) {
			MainRenderer.WORKER_THREAD.worker.submit(() -> {
				final Object2ObjectOpenHashMap<String, BlockbenchElement> uuidToBlockbenchElement = new Object2ObjectOpenHashMap<>();
				blockbenchModel.getElements().forEach(blockbenchElement -> uuidToBlockbenchElement.put(blockbenchElement.getUuid(), blockbenchElement));

				blockbenchModel.getOutlines().forEach(blockbenchOutline -> {
					final ModelPartData modelPartData = new ModelData().getRoot();
					final NewOptimizedModelGroup newOptimizedModelGroup = new NewOptimizedModelGroup();
					final MutableBox mutableBox = new MutableBox();
					final ObjectArrayList<ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> rawModelDisplayParts = new ObjectArrayList<>();

					iterateChildren(blockbenchOutline, null, new GroupTransformations(), (uuid, groupTransformations) -> {
						final BlockbenchElement blockbenchElement = uuidToBlockbenchElement.remove(uuid);
						if (blockbenchElement != null) {
							final ObjectObjectImmutablePair<Box, ObjectObjectImmutablePair<StoredMatrixTransformations, IntIntImmutablePair>> modelPartDetails = blockbenchElement.setModelPart(modelPartData.addChild(MTR.randomString()), groupTransformations);
							mutableBox.add(modelPartDetails.left());
							rawModelDisplayParts.add(modelPartDetails.right());
						}
					});

					newOptimizedModelGroup.add(null, defaultTexture, storedVertexDataList -> StoredVertexData.write(modelPartData.createPart(blockbenchModel.getTextureWidth(), blockbenchModel.getTextureHeight()), storedVertexDataList), mutableBox.getAll());
					addModel(blockbenchOutline.getName(), newOptimizedModelGroup);

					if (!rawModelDisplayParts.isEmpty()) {
						addModelDisplayParts(blockbenchOutline.getName(), rawModelDisplayParts);
					}
				});

				setModelLoaded();
			});
		}
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, @Nullable BlockbenchOutline previousBlockbenchOutline, GroupTransformations groupTransformations, BiConsumer<String, GroupTransformations> consumer) {
		final GroupTransformations newGroupTransformations = blockbenchOutline.add(groupTransformations, previousBlockbenchOutline);
		blockbenchOutline.childrenUuid.forEach(uuid -> consumer.accept(uuid, newGroupTransformations));
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, blockbenchOutline, groupTransformations, consumer));
	}
}
