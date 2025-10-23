package org.mtr.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockEyeCandy;
import org.mtr.block.IBlock;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.registry.Blocks;

public class RenderEyeCandy extends BlockEntityRendererExtension<BlockEyeCandy.EyeCandyBlockEntity> implements IGui {

	@Override
	public void render(BlockEyeCandy.EyeCandyBlockEntity entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos().getX(), entity.getPos().getY(), 0.5 + entity.getPos().getZ());
		final int newLight = entity.getFullBrightness() ? DEFAULT_LIGHT : light;

		if ((RenderRails.isHoldingRailRelated(player) || player.isHolding(Blocks.EYE_CANDY.get().asItem())) && MinecraftClient.getInstance().currentScreen == null) {
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/item/eye_candy.png"), false, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(0, 0.5, 0);
				MTRClient.transformToFacePlayer(matrixStack, entity.getPos().getX() + 0.5, entity.getPos().getY() + 0.5, entity.getPos().getZ() + 0.5);
				IDrawing.rotateZDegrees(matrixStack, 180);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.5F, -0.5F, 1, 1, Direction.UP, DEFAULT_LIGHT);
				matrixStack.pop();
			});
		}

		final BlockPos blockPos = entity.getPos();
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, Properties.HORIZONTAL_FACING);
		final String modelId = entity.getModelId();
		if (modelId != null) {
			CustomResourceLoader.getObjectById(modelId, objectResource -> {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(matrixStack -> {
					matrixStack.translate(entity.getTranslateX(), entity.getTranslateY(), entity.getTranslateZ());
					IDrawing.rotateYDegrees(matrixStack, 180 - facing.getPositiveHorizontalDegrees());
					IDrawing.rotateXRadians(matrixStack, entity.getRotateX() + (float) Math.PI);
					IDrawing.rotateYRadians(matrixStack, entity.getRotateY());
					IDrawing.rotateZRadians(matrixStack, entity.getRotateZ());
				});
				objectResource.render(storedMatrixTransformationsNew, newLight);
			});
		}
	}

	@Override
	public boolean isInRenderDistance(BlockEyeCandy.EyeCandyBlockEntity entity, Vec3d position) {
		return true;
	}
}
