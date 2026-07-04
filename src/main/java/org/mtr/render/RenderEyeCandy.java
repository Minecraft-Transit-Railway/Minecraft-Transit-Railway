package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockEyeCandy;
import org.mtr.block.IBlock;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.registry.Blocks;
import org.mtr.tool.Drawing;

public class RenderEyeCandy extends BlockEntityRendererExtension<BlockEyeCandy.EyeCandyBlockEntity> implements IGui {

	@Override
	public void render(BlockEyeCandy.EyeCandyBlockEntity blockEntity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), 0.5 + blockEntity.getBlockPos().getZ());
		final int newLight = blockEntity.isFullBrightness() ? DEFAULT_LIGHT : light;

		if ((RenderRails.isHoldingRailRelated(player) || player.isHolding(Blocks.EYE_CANDY.get().asItem())) && Minecraft.getInstance().screen == null) {
			MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/item/eye_candy.png"), false, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(0, 0.5, 0);
				MTRClient.transformToFacePlayer(matrixStack, blockEntity.getBlockPos().getX() + 0.5, blockEntity.getBlockPos().getY() + 0.5, blockEntity.getBlockPos().getZ() + 0.5);
				Drawing.rotateZDegrees(matrixStack, 180);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.5F, -0.5F, 1, 1, Direction.UP, DEFAULT_LIGHT);
				matrixStack.popPose();
			});
		}

		final BlockPos blockPos = blockEntity.getBlockPos();
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockStateProperties.HORIZONTAL_FACING);
		final String modelId = blockEntity.getModelId();
		if (modelId != null) {
			CustomResourceLoader.getObjectById(modelId, objectResource -> {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(matrixStack -> {
					matrixStack.translate(blockEntity.getTranslateX(), blockEntity.getTranslateY(), blockEntity.getTranslateZ());
					Drawing.rotateYDegrees(matrixStack, 180 - facing.toYRot());
					Drawing.rotateXRadians(matrixStack, blockEntity.getRotateX() + (float) Math.PI);
					Drawing.rotateYRadians(matrixStack, blockEntity.getRotateY());
					Drawing.rotateZRadians(matrixStack, blockEntity.getRotateZ());
				});
				objectResource.render(storedMatrixTransformationsNew, newLight);
			});
		}
	}

	@Override
	public boolean shouldRender(BlockEyeCandy.EyeCandyBlockEntity entity, Vec3 position) {
		return true;
	}
}
