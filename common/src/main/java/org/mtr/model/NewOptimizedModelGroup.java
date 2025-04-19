package org.mtr.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.mtr.resource.RenderStage;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class NewOptimizedModelGroup {

	public final ObjectArrayList<Box> boxes = new ObjectArrayList<>();
	private final Object2ObjectOpenHashMap<RenderStage, Object2ObjectOpenHashMap<Identifier, ObjectArrayList<StoredVertexData>>> storedVertexConsumersForRenderStageAndTexture = new Object2ObjectOpenHashMap<>();

	public void add(@Nullable RenderStage renderStage, Identifier texture, Consumer<ObjectArrayList<StoredVertexData>> consumer, @Nullable ObjectArrayList<Box> boxes) {
		final ObjectArrayList<StoredVertexData> storedVertexDataList = new ObjectArrayList<>();
		consumer.accept(storedVertexDataList);
		storedVertexConsumersForRenderStageAndTexture.computeIfAbsent(renderStage, key -> new Object2ObjectOpenHashMap<>()).computeIfAbsent(texture, key -> new ObjectArrayList<>()).addAll(storedVertexDataList);

		if (boxes == null) {
			final double[] bounds = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
			storedVertexDataList.forEach(storedVertexData -> {
				bounds[0] = Math.min(bounds[0], storedVertexData.x());
				bounds[1] = Math.min(bounds[1], storedVertexData.y());
				bounds[2] = Math.min(bounds[2], storedVertexData.z());
				bounds[3] = Math.max(bounds[3], storedVertexData.x());
				bounds[4] = Math.max(bounds[4], storedVertexData.y());
				bounds[5] = Math.max(bounds[5], storedVertexData.z());
			});
			this.boxes.add(new Box(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]));
		} else {
			this.boxes.addAll(boxes);
		}
	}

	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> build(VertexFormat.DrawMode drawMode) {
		final Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> newOptimizedModels = new Object2ObjectOpenHashMap<>(RenderStage.values().length);
		storedVertexConsumersForRenderStageAndTexture.forEach((renderStage, storedVertexConsumerForTexture) -> {
			if (renderStage != null) {
				storedVertexConsumerForTexture.forEach((texture, storedVertexDataList) -> newOptimizedModels
						.computeIfAbsent(renderStage, key -> new ObjectArrayList<>())
						.add(new NewOptimizedModel(texture, drawMode, storedVertexDataList.isEmpty() ? null : vertexConsumer -> StoredVertexData.apply(storedVertexDataList, vertexConsumer)))
				);
			}
		});
		return newOptimizedModels;
	}

	public void merge(NewOptimizedModelGroup newOptimizedModelGroup, RenderStage defaultRenderStage, double translateX, double translateY, double translateZ, boolean flip) {
		newOptimizedModelGroup.storedVertexConsumersForRenderStageAndTexture.forEach((renderStage, storedVertexConsumerForTexture) -> {
			final RenderStage newRenderStage = renderStage == null ? defaultRenderStage : renderStage;
			storedVertexConsumerForTexture.forEach((texture, storedVertexDataList) -> storedVertexDataList
					.forEach(storedVertexData -> storedVertexConsumersForRenderStageAndTexture
							.computeIfAbsent(newRenderStage, key -> new Object2ObjectOpenHashMap<>())
							.computeIfAbsent(texture, key -> new ObjectArrayList<>())
							.add(storedVertexData.modify(translateX, translateY, translateZ, flip))
					)
			);
		});
	}
}
