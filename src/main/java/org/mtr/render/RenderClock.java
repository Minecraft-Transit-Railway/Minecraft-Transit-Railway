package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.MTR;
import org.mtr.block.BlockClock;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.tool.Drawing;

public class RenderClock extends BlockEntityRendererExtension<BlockClock.ClockBlockEntity> implements IGui, IBlock {

	@Override
	public void render(BlockClock.ClockBlockEntity blockEntity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos pos = blockEntity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final boolean rotated = IBlock.getStatePropertySafe(state, BlockClock.FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(pos.getX() + 0.5, pos.getY() + 0.3125, pos.getZ() + 0.5);
		if (rotated) {
			storedMatrixTransformations.add(matrixStack -> Drawing.rotateYDegrees(matrixStack, 90));
		}

		MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			final long time = world.getDayTime() + 6000;

			drawHand(matrixStack, vertexConsumer, time * 360F / 12000, true);
			drawHand(matrixStack, vertexConsumer, time * 360F / 1000, false);

			Drawing.rotateYDegrees(matrixStack, 180);
			drawHand(matrixStack, vertexConsumer, time * 360F / 12000, true);
			drawHand(matrixStack, vertexConsumer, time * 360F / 1000, false);

			matrixStack.popPose();
		});
	}

	private static void drawHand(PoseStack matrixStack, VertexConsumer vertexConsumer, float rotation, boolean isHourHand) {
		matrixStack.pushPose();
		Drawing.rotateZDegrees(matrixStack, -rotation);
		IDrawing.drawTexture(matrixStack, vertexConsumer, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		matrixStack.popPose();
	}
}
