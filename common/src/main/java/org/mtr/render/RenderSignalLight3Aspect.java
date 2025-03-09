package org.mtr.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;
import org.mtr.client.IDrawing;

public class RenderSignalLight3Aspect<T extends BlockSignalBase.BlockEntityBase> extends RenderSignalBase<T> {

	public RenderSignalLight3Aspect() {
		super(15, 3);
	}

	@Override
	protected void render(StoredMatrixTransformations storedMatrixTransformations, T entity, ClientWorld world, float tickDelta, int light, int occupiedAspect, boolean isBackSide) {
		final float y;
		final int color = switch (occupiedAspect) {
			case 1 -> {
				y = 0.09375F;
				yield 0xFFFF0000;
			}
			case 2 -> {
				y = 0.40625F;
				yield 0xFFFFAA00;
			}
			default -> {
				y = 0.71875F;
				yield 0xFF00FF00;
			}
		};

		MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
			storedMatrixTransformations.transform(matrixStack, offset);
			IDrawing.drawTexture(matrixStack, vertexConsumer, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, Direction.UP, color, DEFAULT_LIGHT);
			matrixStack.pop();
		});
	}
}
