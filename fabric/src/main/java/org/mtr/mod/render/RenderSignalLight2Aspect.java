package org.mtr.mod.render;

import org.mtr.mapping.holder.BlockEntityRendererArgument;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.client.IDrawing;

public class RenderSignalLight2Aspect<T extends BlockEntityExtension> extends RenderSignalBase<T> {

	private final boolean redOnTop;
	private final int proceedColor;

	public RenderSignalLight2Aspect(BlockEntityRendererArgument dispatcher, boolean isSingleSided, boolean redOnTop, int proceedColor) {
		super(dispatcher, isSingleSided, 2);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(GraphicsHolder graphicsHolder, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide) {
		final float y = occupiedAspect > 0 == redOnTop ? 0.4375F : 0.0625F;
		IDrawing.drawTexture(graphicsHolder, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), occupiedAspect > 0 ? 0xFFFF0000 : proceedColor, MAX_LIGHT_GLOWING);
	}
}
