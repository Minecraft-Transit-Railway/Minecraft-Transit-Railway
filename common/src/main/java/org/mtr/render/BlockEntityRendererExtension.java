package org.mtr.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

public abstract class BlockEntityRendererExtension<T extends BlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public final void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (world instanceof ClientWorld clientWorld && clientPlayerEntity != null) {
			render(entity, clientWorld, clientPlayerEntity, tickDelta, light, overlay);
		}
	}

	public abstract void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay);
}
