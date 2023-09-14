package org.mtr.mod.render;

import org.mtr.mapping.holder.BlockEntityRendererArgument;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.client.IDrawing;

public class RenderSignalLight3Aspect<T extends BlockEntityExtension> extends RenderSignalBase<T> {

	public RenderSignalLight3Aspect(BlockEntityRendererArgument dispatcher, boolean isSingleSided) {
		super(dispatcher, isSingleSided, 3);
	}

	@Override
	protected void render(GraphicsHolder graphicsHolder, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide) {
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
		IDrawing.drawTexture(graphicsHolder, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, facing.getOpposite(), color, MAX_LIGHT_GLOWING);
	}
}
