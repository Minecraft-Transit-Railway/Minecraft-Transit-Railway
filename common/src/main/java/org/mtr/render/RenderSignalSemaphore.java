package org.mtr.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.BlockSignalSemaphoreBase;
import org.mtr.client.IDrawing;

public class RenderSignalSemaphore<T extends BlockSignalSemaphoreBase.BlockEntityBase> extends RenderSignalBase<T> {

	private static final int ANGLE = 55;
	private static final int SPEED = 4;

	public RenderSignalSemaphore() {
		super(8, 2);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, ClientWorld world, float tickDelta, int light, int occupiedAspect, boolean isBackSide) {
		final float angle = isBackSide ? entity.angle2 : entity.angle1;
		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.drawTexture(matrixStack, vertexConsumer, -0.0625F, 0.296875F, -0.190625F, 0.0625F, 0.453125F, -0.190625F, Direction.UP, angle < ANGLE / 2F ? 0xFFFF0000 : 0xFF00FF00, DEFAULT_LIGHT);
			matrixStack.pop();
		});

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/semaphore.png"), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			matrixStack.translate(0.1875, 0.375, 0);
			IDrawing.rotateZDegrees(matrixStack, -180 - angle);
			IDrawing.drawTexture(matrixStack, vertexConsumer, -0.705F, -0.5F, -0.19375F, 0.295F, 0.5F, -0.19375F, Direction.UP, ARGB_WHITE, light);
			IDrawing.drawTexture(matrixStack, vertexConsumer, 0.295F, -0.5F, -0.19375F, -0.705F, 0.5F, -0.19375F, 1, 0, 0, 1, Direction.UP, ARGB_WHITE, light);
			matrixStack.pop();
		});

		final float newAngle;
		if (occupiedAspect > 0) {
			newAngle = Math.max(0, angle - SPEED * tickDelta);
		} else {
			newAngle = Math.min(ANGLE, angle + SPEED * tickDelta);
		}
		if (isBackSide) {
			entity.angle2 = newAngle;
		} else {
			entity.angle1 = newAngle;
		}
	}
}
