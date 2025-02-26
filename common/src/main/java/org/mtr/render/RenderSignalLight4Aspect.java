package org.mtr.mod.render;

import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockSignalBase;
import org.mtr.mod.client.IDrawing;

public class RenderSignalLight4Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	public RenderSignalLight4Aspect(Argument dispatcher) {
		super(dispatcher, 16, 4);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide) {
		final float y;
		final int color;
		switch (occupiedAspect) {
			case 1:
				y = 0.03125F;
				color = 0xFFFF0000;
				break;
			case 2:
			case 3:
				y = 0.28125F;
				color = 0xFFFFAA00;
				break;
			default:
				y = 0.53125F;
				color = 0xFF00FF00;
				break;
		}

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawTexture(graphicsHolder, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, Direction.UP, color, GraphicsHolder.getDefaultLight());
			if (occupiedAspect == 3) {
				IDrawing.drawTexture(graphicsHolder, -0.09375F, 0.78125F, -0.19375F, 0.09375F, 0.96875F, -0.19375F, Direction.UP, 0xFFFFAA00, GraphicsHolder.getDefaultLight());
			}
			graphicsHolder.pop();
		});
	}
}
