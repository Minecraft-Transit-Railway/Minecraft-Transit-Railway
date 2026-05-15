package org.mtr.model;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.render.MainRenderer;
import org.mtr.render.StoredMatrixTransformations;
import org.mtr.resource.BlockbenchElement;
import org.mtr.resource.BlockbenchModel;
import org.mtr.resource.BlockbenchOutline;
import org.mtr.resource.GroupTransformations;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Blockbench ({@code .bbmodel}) loader producing {@link NewOptimizedModelGroup}s indexed
 * by outline name.
 *
 * <p>Blockbench's source format is a tree of named outlines containing transformed cuboid
 * elements. This loader walks the tree, applies the parent-chain transforms via
 * {@link GroupTransformations}, and emits one {@link NewOptimizedModelGroup} per outline so
 * a vehicle's {@code modelProperties.json} can address parts by their outline name.</p>
 *
 * <p>Parsing runs entirely on
 * {@link org.mtr.render.MainRenderer#WORKER_THREAD}'s virtual-thread executor.</p>
 */
public final class BlockbenchModelLoader extends ModelLoaderBase {

	public BlockbenchModelLoader(Identifier defaultTexture) {
		super(defaultTexture, VertexFormat.DrawMode.QUADS);
	}

	public void loadModel(BlockbenchModel blockbenchModel) {
		loadModel(() -> blockbenchModel);
	}

	/**
	 * Defer the entire build pipeline — including the supplier that parses the bbmodel
	 * JSON — onto the worker thread. Prefer this overload when the JSON content is
	 * available as a string from a thread-safe source (e.g.
	 * {@link org.mtr.client.CustomResourceLoader}) so the caller doesn't pay JSON parsing
	 * cost on the render thread.
	 *
	 * <p>See {@code docs/PERFORMANCE.md} §1.2.</p>
	 */
	public void loadModel(Supplier<BlockbenchModel> blockbenchModelSupplier) {
		if (canLoadModel()) {
			parseStarted();
			MainRenderer.WORKER_THREAD.worker.submit(() -> {
				try {
					final BlockbenchModel blockbenchModel = blockbenchModelSupplier.get();
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
				} catch (Exception e) {
					MTR.LOGGER.error("Failed to parse Blockbench model for texture [{}]", defaultTexture, e);
				} finally {
					parseFinished();
				}
			});
		}
	}

	private static void iterateChildren(BlockbenchOutline blockbenchOutline, @Nullable BlockbenchOutline previousBlockbenchOutline, GroupTransformations groupTransformations, BiConsumer<String, GroupTransformations> consumer) {
		final GroupTransformations newGroupTransformations = blockbenchOutline.add(groupTransformations, previousBlockbenchOutline);
		blockbenchOutline.childrenUuid.forEach(uuid -> consumer.accept(uuid, newGroupTransformations));
		blockbenchOutline.getChildren().forEach(childOutline -> iterateChildren(childOutline, blockbenchOutline, groupTransformations, consumer));
	}
}
