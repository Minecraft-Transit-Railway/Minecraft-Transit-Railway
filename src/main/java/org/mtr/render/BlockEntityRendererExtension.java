package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * This implementation of a {@link BlockEntityRenderer} provides pre-transformed matrices and other helpful parameters.
 *
 * @param <T> the block entity type
 */
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
