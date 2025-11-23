package org.mtr.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

/**
 * This implementation of a {@link BlockEntityRenderer} provides pre-transformed matrices and other helpful parameters.
 *
 * @param <T> the block entity type
 */
public abstract class BlockEntityRendererExtension<T extends BlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public final void render(T blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = blockEntity.getWorld();
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (world instanceof ClientWorld clientWorld && clientPlayerEntity != null) {
			matrixStack.push();
			matrixStack.translate(0.5, 0, 0.5);
			render(blockEntity, matrixStack, vertexConsumers, clientWorld, clientPlayerEntity, tickDelta, light, overlay);
			matrixStack.pop();
		}
	}

	/**
	 * A better implementation of the render method with helpful parameters.
	 *
	 * @param blockEntity            the {@link BlockEntity}
	 * @param matrixStack            a pre-transformed {@link MatrixStack} centred at (0.5, 0, 0.5) of the block
	 * @param vertexConsumerProvider the provided {@link VertexConsumerProvider}
	 * @param clientWorld            the {@link ClientWorld} guaranteed to be non-null
	 * @param clientPlayerEntity     the {@link ClientPlayerEntity} guaranteed to be non-null
	 * @param tickDelta              the number of ticks elapsed
	 * @param light                  the light level of the block
	 * @param overlay                the texture overlay
	 */
	public abstract void render(T blockEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, float tickDelta, int light, int overlay);
}
