package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.DoorAnimationType;
import mtr.data.IGui;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class ModelTrainBase extends EntityModel<Entity> implements IGui {

	public final DoorAnimationType doorAnimationType;
	public final boolean renderDoorOverlay;

	public ModelTrainBase(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		this.doorAnimationType = doorAnimationType;
		this.renderDoorOverlay = renderDoorOverlay;
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public final void render(PoseStack matrices, MultiBufferSource vertexConsumers, ResourceLocation texture, int light, float doorLeftValue, float doorRightValue, boolean opening, int currentCar, int trainCars, boolean head1IsFront, boolean lightsOn, boolean isTranslucent, boolean renderDetails, int stopIndex, List<Long> routeIds) {
		final float doorLeftX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorLeftValue);
		final float doorRightX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorRightValue);
		final float doorLeftZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorLeftValue, opening);
		final float doorRightZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorRightValue, opening);

		final int lightOnInteriorLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
		final int lightOnGlowingLevel = lightsOn ? MAX_LIGHT_GLOWING : light;

		if (isTranslucent) {
			if (renderDetails) {
				final RenderType renderLayerInteriorTranslucent = lightsOn ? MoreRenderLayers.getInteriorTranslucent(texture) : MoreRenderLayers.getExteriorTranslucent(texture);
				render(matrices, vertexConsumers.getBuffer(renderLayerInteriorTranslucent), RenderStage.INTERIOR_TRANSLUCENT, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, true);
			}
		} else {
			final RenderType renderLayerLight = lightsOn ? MoreRenderLayers.getLight(texture, false) : MoreRenderLayers.getExterior(texture);
			final RenderType renderLayerInterior = lightsOn ? MoreRenderLayers.getInterior(texture) : MoreRenderLayers.getExterior(texture);
			render(matrices, vertexConsumers.getBuffer(renderLayerLight), RenderStage.LIGHTS, lightOnGlowingLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			render(matrices, vertexConsumers.getBuffer(renderLayerInterior), RenderStage.INTERIOR, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);

			if (renderDetails) {
				renderExtraDetails(matrices, vertexConsumers, light, lightOnInteriorLevel, lightsOn, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, stopIndex, routeIds);
			}

			render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), RenderStage.EXTERIOR, light, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(texture, true)), RenderStage.ALWAYS_ON_LIGHTS, MAX_LIGHT_GLOWING, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
		}
	}

	protected void renderExtraDetails(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int car, int totalCars, int stopIndex, List<Long> routeIds) {
	}

	protected float getDoorDuration() {
		return 0.5F;
	}

	protected abstract void render(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails);

	protected abstract int getDoorMax();

	protected static void setRotationAngle(ModelMapper bone, float x, float y, float z) {
		bone.setRotationAngle(x, y, z);
	}

	protected static void renderMirror(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		renderOnce(bone, matrices, vertices, light, position);
		renderOnceFlipped(bone, matrices, vertices, light, position);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		bone.render(matrices, vertices, 0, position, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.render(matrices, vertices, positionX, positionZ, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionY, float positionZ) {
		bone.render(matrices, vertices, positionX, positionY, positionZ, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		bone.render(matrices, vertices, 0, position, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.render(matrices, vertices, -positionX, positionZ, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionY, float positionZ) {
		bone.render(matrices, vertices, -positionX, positionY, positionZ, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static boolean isIndex(int index, int value, int[] array) {
		final int finalIndex = index < 0 ? array.length + index : index;
		return finalIndex < array.length && finalIndex >= 0 && array[finalIndex] == value;
	}

	public enum RenderStage {LIGHTS, ALWAYS_ON_LIGHTS, INTERIOR, INTERIOR_TRANSLUCENT, EXTERIOR}
}
