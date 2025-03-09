package org.mtr.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;
import org.mtr.client.IDrawing;

public class RenderSignalLight4Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	public RenderSignalLight4Aspect() {
		super(16, 4);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, ClientWorld world, float tickDelta, int light, int occupiedAspect, boolean isBackSide) {
		final float y;
		final int color = switch (occupiedAspect) {
			case 1 -> {
				y = 0.03125F;
				yield 0xFFFF0000;
			}
			case 2, 3 -> {
				y = 0.28125F;
				yield 0xFFFFAA00;
			}
			default -> {
				y = 0.53125F;
				yield 0xFF00FF00;
			}
		};

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.drawTexture(matrixStack, vertexConsumer, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, Direction.UP, color, DEFAULT_LIGHT);
			if (occupiedAspect == 3) {
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.09375F, 0.78125F, -0.19375F, 0.09375F, 0.96875F, -0.19375F, Direction.UP, 0xFFFFAA00, DEFAULT_LIGHT);
			}
			matrixStack.pop();
		});
	}
}
