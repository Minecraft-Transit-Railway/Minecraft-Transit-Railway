package mtr.model;

import mtr.data.IGui;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public abstract class ModelTrainBase extends EntityModel<Entity> implements IGui {

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public final void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier texture, int light, float doorLeftValue, float doorRightValue, boolean opening, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, boolean lightsOn, boolean renderDetails) {
		final float doorLeftX = getDoorAnimationX(doorLeftValue, opening);
		final float doorRightX = getDoorAnimationX(doorRightValue, opening);
		final float doorLeftZ = getDoorAnimationZ(doorLeftValue, opening);
		final float doorRightZ = getDoorAnimationZ(doorRightValue, opening);

		final int lightOnInteriorLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
		final int lightOnGlowingLevel = lightsOn ? MAX_LIGHT_GLOWING : light;
		final RenderLayer renderLayerLight = lightsOn ? MoreRenderLayers.getLight(texture) : MoreRenderLayers.getExterior(texture);
		final RenderLayer renderLayerInterior = lightsOn ? MoreRenderLayers.getInterior(texture) : MoreRenderLayers.getExterior(texture);
		final RenderLayer renderLayerInteriorTranslucent = lightsOn ? MoreRenderLayers.getInteriorTranslucent(texture) : MoreRenderLayers.getExteriorTranslucent(texture);

		render(matrices, vertexConsumers.getBuffer(renderLayerLight), RenderStage.LIGHTS, lightOnGlowingLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head, head1IsFront, renderDetails);
		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(texture)), RenderStage.ALWAYS_ON_LIGHTS, MAX_LIGHT_GLOWING, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head, head1IsFront, renderDetails);
		render(matrices, vertexConsumers.getBuffer(renderLayerInterior), RenderStage.INTERIOR, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head, head1IsFront, renderDetails);

		if (renderDetails) {
			for (int position : getDoorPositions()) {
				final ModelDoorOverlay modelDoorOverlay = getModelDoorOverlay();
				if (modelDoorOverlay != null) {
					modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.INTERIOR, lightOnInteriorLevel, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
					modelDoorOverlay.render(matrices, vertexConsumers, RenderStage.EXTERIOR, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ, lightsOn);
				}
				final ModelDoorOverlayTopBase modelDoorOverlayTop = getModelDoorOverlayTop();
				if (modelDoorOverlayTop != null) {
					modelDoorOverlayTop.render(matrices, vertexConsumers, light, position, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
				}
			}
			render(matrices, vertexConsumers.getBuffer(renderLayerInteriorTranslucent), RenderStage.INTERIOR_TRANSLUCENT, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head, head1IsFront, renderDetails);
		}

		render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), RenderStage.EXTERIOR, light, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head, head1IsFront, renderDetails);
	}

	private void render(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head, boolean head1IsFront, boolean renderDetails) {
		for (int position : getWindowPositions()) {
			renderWindowPositions(matrices, vertices, renderStage, light, position, renderDetails, isEnd1Head, isEnd2Head);
		}
		for (int position : getDoorPositions()) {
			renderDoorPositions(matrices, vertices, renderStage, light, position, renderDetails, doorLeftX, doorRightX, doorLeftZ, doorRightZ, isEnd1Head, isEnd2Head);
		}

		if (isEnd1Head) {
			renderHeadPosition1(matrices, vertices, renderStage, light, getEndPositions()[0], renderDetails, head1IsFront);
		} else {
			renderEndPosition1(matrices, vertices, renderStage, light, getEndPositions()[0], renderDetails);
		}

		if (isEnd2Head) {
			renderHeadPosition2(matrices, vertices, renderStage, light, getEndPositions()[1], renderDetails, !head1IsFront);
		} else {
			renderEndPosition2(matrices, vertices, renderStage, light, getEndPositions()[1], renderDetails);
		}
	}

	protected abstract void renderWindowPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderDoorPositions(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, boolean isEnd1Head, boolean isEnd2Head);

	protected abstract void renderHeadPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights);

	protected abstract void renderHeadPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails, boolean useHeadlights);

	protected abstract void renderEndPosition1(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails);

	protected abstract void renderEndPosition2(MatrixStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, int position, boolean renderDetails);

	protected abstract ModelDoorOverlay getModelDoorOverlay();

	protected abstract ModelDoorOverlayTopBase getModelDoorOverlayTop();

	protected abstract int[] getWindowPositions();

	protected abstract int[] getDoorPositions();

	protected abstract int[] getEndPositions();

	protected abstract float getDoorAnimationX(float value, boolean opening);

	protected abstract float getDoorAnimationZ(float value, boolean opening);

	protected static void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	protected static void renderMirror(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		renderOnce(bone, matrices, vertices, light, position);
		renderOnceFlipped(bone, matrices, vertices, light, position);
	}

	protected static void renderOnce(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		bone.setPivot(0, 0, position);
		setRotationAngle(bone, 0, 0, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected static void renderOnce(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.setPivot(positionX, 0, positionZ);
		setRotationAngle(bone, 0, 0, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected static void renderOnceFlipped(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float position) {
		bone.setPivot(0, 0, position);
		setRotationAngle(bone, 0, (float) Math.PI, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected static void renderOnceFlipped(ModelPart bone, MatrixStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.setPivot(-positionX, 0, positionZ);
		setRotationAngle(bone, 0, (float) Math.PI, 0);
		bone.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
	}

	protected static boolean isIndex(int index, int value, int[] array) {
		final int finalIndex = index < 0 ? array.length + index : index;
		return finalIndex < array.length && finalIndex >= 0 && array[finalIndex] == value;
	}

	protected static float smoothEnds(float startValue, float endValue, float startTime, float endTime, float time) {
		if (time < startTime) {
			return startValue;
		}
		if (time > endTime) {
			return endValue;
		}

		final float timeChange = endTime - startTime;
		final float valueChange = endValue - startValue;
		return valueChange * (float) (1 - Math.cos(Math.PI * (time - startTime) / timeChange)) / 2 + startValue;
	}

	protected enum RenderStage {LIGHTS, ALWAYS_ON_LIGHTS, INTERIOR, INTERIOR_TRANSLUCENT, EXTERIOR}
}
