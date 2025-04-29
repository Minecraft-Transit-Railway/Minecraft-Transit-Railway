package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.client.IDrawing;

public class RenderSignalSemaphore<T extends BlockSignalSemaphoreBase.BlockEntityBase> extends RenderSignalBase<T> {

	private static final int ANGLE = 55;
	private static final int SPEED = 4;

	public RenderSignalSemaphore(Argument dispatcher) {
		super(dispatcher, 8, 2);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide) {
		final float angle = isBackSide ? entity.angle2 : entity.angle1;
		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawTexture(graphicsHolder, -0.0625F, 0.296875F, -0.190625F, 0.0625F, 0.453125F, -0.190625F, Direction.UP, angle < ANGLE / 2F ? 0xFFFF0000 : 0xFF00FF00, GraphicsHolder.getDefaultLight());
			graphicsHolder.pop();
		});

		final World world = entity.getWorld2();
		if (world != null) {
			final BlockPos pos = entity.getPos2();
			MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/semaphore.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				graphicsHolder.translate(0.1875, 0.375, 0);
				graphicsHolder.rotateZDegrees(-180 - angle);
				final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.getBlockMapped(), pos), world.getLightLevel(LightType.getSkyMapped(), pos));
				IDrawing.drawTexture(graphicsHolder, -0.705F, -0.5F, -0.19375F, 0.295F, 0.5F, -0.19375F, Direction.UP, ARGB_WHITE, light);
				IDrawing.drawTexture(graphicsHolder, 0.295F, -0.5F, -0.19375F, -0.705F, 0.5F, -0.19375F, 1, 0, 0, 1, Direction.UP, ARGB_WHITE, light);
				graphicsHolder.pop();
			});
		}

		final float newAngle;
		if (occupiedAspect == 1) {
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
