package org.mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.BlockClock;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.tool.Drawing;

public class RenderClock extends BlockEntityRendererExtension<BlockClock.ClockBlockEntity> implements IGui, IBlock {

	@Override
	public void render(BlockClock.ClockBlockEntity blockEntity, MatrixStack matrixStack2, VertexConsumerProvider vertexConsumerProvider, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos pos = blockEntity.getPos();
		final BlockState state = world.getBlockState(pos);
		final boolean rotated = IBlock.getStatePropertySafe(state, BlockClock.FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(pos.getX() + 0.5, pos.getY() + 0.3125, pos.getZ() + 0.5);
		if (rotated) {
			storedMatrixTransformations.add(matrixStack -> Drawing.rotateYDegrees(matrixStack, 90));
		}

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			final long time = world.getTimeOfDay() + 6000;

			drawHand(matrixStack, vertexConsumer, time * 360F / 12000, true);
			drawHand(matrixStack, vertexConsumer, time * 360F / 1000, false);

			Drawing.rotateYDegrees(matrixStack, 180);
			drawHand(matrixStack, vertexConsumer, time * 360F / 12000, true);
			drawHand(matrixStack, vertexConsumer, time * 360F / 1000, false);

			matrixStack.pop();
		});
	}

	private static void drawHand(MatrixStack matrixStack, VertexConsumer vertexConsumer, float rotation, boolean isHourHand) {
		matrixStack.push();
		Drawing.rotateZDegrees(matrixStack, -rotation);
		IDrawing.drawTexture(matrixStack, vertexConsumer, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		matrixStack.pop();
	}
}
