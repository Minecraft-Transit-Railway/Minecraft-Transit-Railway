package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
//? if >= 26.1 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
*///? }

/**
 * This implementation of a {@link BlockEntityRenderer} provides pre-transformed matrices and other helpful parameters.
 *
 * @param <T> the block entity type
 */
//? if >= 26.1 {
/*public abstract class BlockEntityRendererExtension<T extends BlockEntity> implements BlockEntityRenderer<T, BlockEntityRendererExtension.ExtendedRenderState<T>> {

	@Override
	public ExtendedRenderState<T> createRenderState() {
		return new ExtendedRenderState<>();
	}

	@Override
	public void extractRenderState(T blockEntity, ExtendedRenderState<T> renderState, float tickDelta, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, tickDelta, cameraPosition, breakProgress);
		renderState.blockEntity = blockEntity;
		renderState.tickDelta = tickDelta;
	}

	@Override
	public void submit(ExtendedRenderState<T> renderState, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		final T blockEntity = renderState.blockEntity;

		if (blockEntity == null) {
			return;
		}

		final Level world = blockEntity.getLevel();
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;

		if (world instanceof ClientLevel clientWorld && clientPlayerEntity != null) {
			matrixStack.pushPose();
			matrixStack.translate(0.5, 0, 0.5);
			render(blockEntity, matrixStack, Minecraft.getInstance().renderBuffers().bufferSource(), clientWorld, clientPlayerEntity, renderState.tickDelta, renderState.lightCoords, OverlayTexture.NO_OVERLAY);
			matrixStack.popPose();
		}
	}

	// Holds the block entity itself rather than copies of the values read from it. The game's own
	// renderers copy values, which is the safer habit, but every renderer below this class reads a
	// different part of its block entity and several also read the world, so copying would mean
	// designing a state class for each of the twelve rather than one here. The reference is only
	// read back during submit, which the dispatcher calls in the same frame as the extraction.
	public static final class ExtendedRenderState<T extends BlockEntity> extends BlockEntityRenderState {

		@Nullable
		private T blockEntity;
		private float tickDelta;
	}
*///? } else {
public abstract class BlockEntityRendererExtension<T extends BlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public final void render(T blockEntity, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = blockEntity.getLevel();
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		if (world instanceof ClientLevel clientWorld && clientPlayerEntity != null) {
			matrixStack.pushPose();
			matrixStack.translate(0.5, 0, 0.5);
			render(blockEntity, matrixStack, vertexConsumers, clientWorld, clientPlayerEntity, tickDelta, light, overlay);
			matrixStack.popPose();
		}
	}
//? }

	/**
	 * A better implementation of the render method with helpful parameters.
	 *
	 * @param blockEntity            the {@link BlockEntity}
	 * @param matrixStack            a pre-transformed {@link PoseStack} centred at (0.5, 0, 0.5) of the block
	 * @param vertexConsumerProvider the provided {@link MultiBufferSource}
	 * @param clientWorld            the {@link ClientLevel} guaranteed to be non-null
	 * @param clientPlayerEntity     the {@link LocalPlayer} guaranteed to be non-null
	 * @param tickDelta              the number of ticks elapsed
	 * @param light                  the light level of the block
	 * @param overlay                the texture overlay
	 */
	public abstract void render(T blockEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, ClientLevel clientWorld, LocalPlayer clientPlayerEntity, float tickDelta, int light, int overlay);
}
