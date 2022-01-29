package mtr.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.model.ModelDoorOverlay;
import mtr.model.ModelDoorOverlayTopBase;
import mtr.model.ModelTrainBase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DynamicTrainModel extends ModelTrainBase {

	private final Map<String, ModelMapper> parts = new HashMap<>();
	private final JsonObject properties;
	private final int doorMax;

	public DynamicTrainModel(JsonObject model, JsonObject properties) {
		final JsonObject resolution = model.getAsJsonObject("resolution");
		final int textureWidth = resolution.get("width").getAsInt();
		final int textureHeight = resolution.get("height").getAsInt();

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		final Map<String, ModelMapper> elementsByKey = new HashMap<>();
		model.getAsJsonArray("elements").forEach(element -> elementsByKey.put(element.getAsJsonObject().get("uuid").getAsString(), new ModelMapper(modelDataWrapper)));

		model.getAsJsonArray("outliner").forEach(element -> {
			final JsonObject elementObject = element.getAsJsonObject();
			parts.put(elementObject.get("name").getAsString(), addChildren(elementObject, elementsByKey, modelDataWrapper));
		});

		model.getAsJsonArray("elements").forEach(element -> {
			final JsonObject elementObject = element.getAsJsonObject();
			final ModelMapper child = elementsByKey.get(elementObject.get("uuid").getAsString());

			final Double[] origin = {0D, 0D, 0D};
			getArrayFromValue(origin, elementObject, "origin", JsonElement::getAsDouble);
			child.setPos(-origin[0].floatValue(), -origin[1].floatValue(), origin[2].floatValue());

			final Double[] rotation = {0D, 0D, 0D};
			getArrayFromValue(rotation, elementObject, "rotation", JsonElement::getAsDouble);
			setRotationAngle(child, -(float) Math.toRadians(rotation[0]), -(float) Math.toRadians(rotation[1]), (float) Math.toRadians(rotation[2]));

			final Integer[] uvOffset = {0, 0};
			getArrayFromValue(uvOffset, elementObject, "uv_offset", JsonElement::getAsInt);

			final Double[] posFrom = {0D, 0D, 0D};
			getArrayFromValue(posFrom, elementObject, "from", JsonElement::getAsDouble);
			final Double[] posTo = {0D, 0D, 0D};
			getArrayFromValue(posTo, elementObject, "to", JsonElement::getAsDouble);

			final double inflate = elementObject.has("inflate") ? elementObject.get("inflate").getAsDouble() : 0;
			final boolean mirror = elementObject.has("shade") && !elementObject.get("shade").getAsBoolean();

			child.texOffs(uvOffset[0], uvOffset[1]).addBox(
					origin[0].floatValue() - posTo[0].floatValue(), origin[1].floatValue() - posTo[1].floatValue(), posFrom[2].floatValue() - origin[2].floatValue(),
					Math.round(posTo[0].floatValue() - posFrom[0].floatValue()), Math.round(posTo[1].floatValue() - posFrom[1].floatValue()), Math.round(posTo[2].floatValue() - posFrom[2].floatValue()),
					(float) inflate, mirror
			);
		});

		this.properties = properties;
		doorMax = properties.get("door_max").getAsInt();

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		parts.values().forEach(part -> {
			part.setPos(0, 0, 0);
			part.texOffs(0, 0).addBox(0, 0, 0, 0, 0, 0, 0, false);
			part.setModelPart();
		});
	}

	@Override
	protected void renderWindowPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
	}

	@Override
	protected void renderDoorPositions(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head) {
		renderParts("parts_normal", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		if (doorLeftZ > 0 || doorRightZ > 0) {
			renderParts("parts_door_opened", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		} else {
			renderParts("parts_door_closed", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		}
	}

	@Override
	protected void renderHeadPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderParts("parts_head_1", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		if (useHeadlights) {
			renderParts("parts_head_1_headlights", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		} else {
			renderParts("parts_head_1_tail_lights", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		}
	}

	@Override
	protected void renderHeadPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean useHeadlights) {
		renderParts("parts_head_2", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		if (useHeadlights) {
			renderParts("parts_head_2_headlights", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		} else {
			renderParts("parts_head_2_tail_lights", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
		}
	}

	@Override
	protected void renderEndPosition1(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderParts("parts_end_1", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
	}

	@Override
	protected void renderEndPosition2(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
		renderParts("parts_end_2", matrices, vertices, renderStage, light, renderDetails, doorLeftZ, doorRightZ);
	}

	@Override
	protected ModelDoorOverlay getModelDoorOverlay() {
		return null;
	}

	@Override
	protected ModelDoorOverlayTopBase getModelDoorOverlayTop() {
		return null;
	}

	@Override
	protected int[] getWindowPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getDoorPositions() {
		return new int[]{0};
	}

	@Override
	protected int[] getEndPositions() {
		return new int[]{0, 0};
	}

	@Override
	protected float getDoorAnimationX(float value, boolean opening) {
		return 0;
	}

	@Override
	protected float getDoorAnimationZ(float value, boolean opening) {
		return smoothEnds(0, doorMax, 0, 0.5F, value);
	}

	private ModelMapper addChildren(JsonObject jsonObject, Map<String, ModelMapper> children, ModelDataWrapper modelDataWrapper) {
		final ModelMapper part = new ModelMapper(modelDataWrapper);
		jsonObject.getAsJsonArray("children").forEach(child -> part.addChild(child.isJsonObject() ? addChildren(child.getAsJsonObject(), children, modelDataWrapper) : children.get(child.getAsString())));
		return part;
	}

	private void renderParts(String category, PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, boolean renderDetails, float doorLeftZ, float doorRightZ) {
		properties.getAsJsonArray(category).forEach(partElement -> {
			final JsonObject partObject = partElement.getAsJsonObject();
			final boolean shouldRender = renderDetails || !partObject.has("skip_rendering_if_too_far") || !partObject.get("skip_rendering_if_too_far").getAsBoolean();

			if (shouldRender && renderStage.toString().equals(partObject.get("stage").getAsString().toUpperCase())) {
				final ModelMapper part = parts.get(partObject.get("part_name").getAsString());

				if (part != null) {
					final float zOffset;
					if (partObject.has("door_offset_z")) {
						switch (partObject.get("door_offset_z").getAsString()) {
							case "left":
								zOffset = doorLeftZ;
								break;
							case "right":
								zOffset = doorRightZ;
								break;
							case "left_negative":
								zOffset = -doorLeftZ;
								break;
							case "right_negative":
								zOffset = -doorRightZ;
								break;
							default:
								zOffset = 0;
								break;
						}
					} else {
						zOffset = 0;
					}

					if (partObject.has("positions")) {
						partObject.getAsJsonArray("positions").forEach(positionElement -> {
							final float x = positionElement.getAsJsonArray().get(0).getAsFloat();
							final float z = positionElement.getAsJsonArray().get(1).getAsFloat();
							renderOnce(part, matrices, vertices, light, x, z + zOffset);
						});
					}

					if (partObject.has("positions_flipped")) {
						partObject.getAsJsonArray("positions_flipped").forEach(positionElement -> {
							final float x = positionElement.getAsJsonArray().get(0).getAsFloat();
							final float z = positionElement.getAsJsonArray().get(1).getAsFloat();
							renderOnceFlipped(part, matrices, vertices, light, x, z - zOffset);
						});
					}
				}
			}
		});
	}

	private static <T> void getArrayFromValue(T[] array, JsonObject jsonObject, String key, Function<JsonElement, T> function) {
		if (jsonObject.has(key)) {
			final JsonArray jsonArray = jsonObject.getAsJsonArray(key);
			for (int i = 0; i < array.length; i++) {
				array[i] = function.apply(jsonArray.get(i));
			}
		}
	}
}
