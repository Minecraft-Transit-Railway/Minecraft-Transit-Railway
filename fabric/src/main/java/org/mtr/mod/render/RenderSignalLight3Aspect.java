package org.mtr.mod.render;

import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockSignalBase;
import org.mtr.mod.client.IDrawing;

public class RenderSignalLight3Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	public RenderSignalLight3Aspect(Argument dispatcher) {
		super(dispatcher, 15, 3);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide) {
		final float y;
		final int color;
		switch (occupiedAspect) {
			case 1:
				y = 0.09375F;
				color = 0xFFFF0000;
				break;
			case 2:
				y = 0.40625F;
				color = 0xFFFFAA00;
				break;
			default:
				y = 0.71875F;
				color = 0xFF00FF00;
				break;
		}

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawTexture(graphicsHolder, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, Direction.UP, color, GraphicsHolder.getDefaultLight());
			graphicsHolder.pop();
		});
	}
}
