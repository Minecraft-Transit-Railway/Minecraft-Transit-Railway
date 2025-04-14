package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.mtr.resource.RenderStage;

import java.util.function.Consumer;

public final class NewOptimizedModelGroup {

	private final Object2ObjectOpenHashMap<RenderStage, Object2ObjectOpenHashMap<Identifier, ObjectArrayList<StoredVertexData>>> storedVertexConsumersForRenderStageAndTexture = new Object2ObjectOpenHashMap<>(RenderStage.values().length);

	public NewOptimizedModelGroup() {
		for (final RenderStage renderStage : RenderStage.values()) {
			storedVertexConsumersForRenderStageAndTexture.put(renderStage, new Object2ObjectOpenHashMap<>());
		}
	}

	public void add(RenderStage renderStage, Identifier texture, Consumer<ObjectArrayList<StoredVertexData>> consumer) {
		consumer.accept(storedVertexConsumersForRenderStageAndTexture
				.get(renderStage)
				.computeIfAbsent(texture, key -> new ObjectArrayList<>())
		);
	}

	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> build(VertexFormat.DrawMode drawMode) {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> newOptimizedModels = new Object2ObjectOpenHashMap<>(RenderStage.values().length);
		storedVertexConsumersForRenderStageAndTexture.forEach((renderStage, storedVertexConsumerForTexture) -> storedVertexConsumerForTexture
				.forEach((texture, storedVertexDataList) -> newOptimizedModels
						.computeIfAbsent(renderStage, key -> new ObjectArrayList<>())
						.add(new NewOptimizedModel(texture, drawMode, storedVertexDataList.isEmpty() ? null : vertexConsumer -> StoredVertexData.apply(storedVertexDataList, vertexConsumer)))
				)
		);
		return newOptimizedModels;
	}

	public void merge(NewOptimizedModelGroup newOptimizedModelGroup, double translateX, double translateY, double translateZ, boolean flip) {
		newOptimizedModelGroup.storedVertexConsumersForRenderStageAndTexture.forEach((renderStage, storedVertexConsumerForTexture) -> storedVertexConsumerForTexture
				.forEach((texture, storedVertexDataList) -> storedVertexDataList.forEach(storedVertexData -> storedVertexConsumersForRenderStageAndTexture
						.get(renderStage)
						.computeIfAbsent(texture, key -> new ObjectArrayList<>())
						.add(storedVertexData.modify(translateX, translateY, translateZ, flip))
				))
		);
	}
}
