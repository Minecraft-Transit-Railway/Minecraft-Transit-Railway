package org.mtr.mod.render;

import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockSignalBase;
import org.mtr.mod.client.IDrawing;

public class RenderSignalLight2Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	private final boolean redOnTop;
	private final int proceedColor;

	public RenderSignalLight2Aspect(Argument dispatcher, boolean redOnTop, int proceedColor) {
		super(dispatcher, 12, 2);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide) {
		final float y = (occupiedAspect == 1) == redOnTop ? 0.4375F : 0.0625F;
		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			IDrawing.drawTexture(graphicsHolder, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, Direction.UP, occupiedAspect == 1 ? 0xFFFF0000 : proceedColor, GraphicsHolder.getDefaultLight());
			graphicsHolder.pop();
		});
	}
}
