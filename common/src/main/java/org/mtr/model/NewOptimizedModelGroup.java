package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import org.mtr.resource.RenderStage;

import java.util.function.Consumer;

public final class NewOptimizedModelGroup {

	private final Object2ObjectOpenHashMap<RenderStage, Object2ObjectOpenHashMap<Identifier, StoredVertexConsumer>> vertexBuildersForRenderStage = new Object2ObjectOpenHashMap<>(RenderStage.values().length);

	public NewOptimizedModelGroup() {
		for (final RenderStage renderStage : RenderStage.values()) {
			vertexBuildersForRenderStage.put(renderStage, new Object2ObjectOpenHashMap<>());
		}
	}

	public void add(RenderStage renderStage, Identifier texture, Consumer<VertexConsumer> consumer) {
		consumer.accept(vertexBuildersForRenderStage.get(renderStage).computeIfAbsent(texture, key -> new StoredVertexConsumer()));
	}

	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> build() {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> newOptimizedModels = new Object2ObjectOpenHashMap<>(RenderStage.values().length);
		vertexBuildersForRenderStage.forEach((renderStage, storedVertexConsumerForTexture) -> storedVertexConsumerForTexture.forEach((texture, storedVertexConsumer) -> newOptimizedModels.computeIfAbsent(renderStage, key -> new ObjectArrayList<>()).add(new NewOptimizedModel(texture, storedVertexConsumer.isEmpty() ? null : storedVertexConsumer::apply))));
		return newOptimizedModels;
	}

	public void merge(NewOptimizedModelGroup newOptimizedModelGroup) {
		newOptimizedModelGroup.vertexBuildersForRenderStage.forEach((renderStage, storedVertexConsumerForTexture) -> storedVertexConsumerForTexture.forEach((texture, storedVertexConsumer) -> vertexBuildersForRenderStage.get(renderStage).computeIfAbsent(texture, key -> new StoredVertexConsumer()).add(storedVertexConsumer)));
	}
}
