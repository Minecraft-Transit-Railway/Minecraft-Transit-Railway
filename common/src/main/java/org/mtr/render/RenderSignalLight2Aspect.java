package org.mtr.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;
import org.mtr.client.IDrawing;

public class RenderSignalLight2Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	private final boolean redOnTop;
	private final int proceedColor;

	public RenderSignalLight2Aspect(boolean redOnTop, int proceedColor) {
		super(12, 2);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, ClientWorld world, float tickDelta, int light, int occupiedAspect, boolean isBackSide) {
		final float y = (occupiedAspect == 1) == redOnTop ? 0.4375F : 0.0625F;
		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.drawTexture(matrixStack, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, Direction.UP, occupiedAspect == 1 ? 0xFFFF0000 : proceedColor, DEFAULT_LIGHT);
			matrixStack.pop();
		});
	}
}
