package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

public class RenderSignalLight4Aspect<T extends BlockEntityMapper> extends RenderSignalBase<T> {

	private final int proceedColor;

	public RenderSignalLight4Aspect(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, int proceedColor) {
		super(dispatcher, isSingleSided, 4);
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide) {
		final float y;
		final int color;
		switch (occupiedAspect) {
			case 1:
			case 2:
				y = 0.28125F;
				color = 0xFFFFFF00;
				break;
			case 3:
				y = 0.5F;
				color = proceedColor;
				break;
			default:
				y = 0.0625F;
				color = 0xFFFF0000;
				break;
		}
		IDrawing.drawTexture(matrices, vertexConsumer, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, facing.getOpposite(), color, MAX_LIGHT_GLOWING);
		if (occupiedAspect == 2) {
			IDrawing.drawTexture(matrices, vertexConsumer, -0.09375F, 0.71875F, -0.19375F, 0.09375F, 0.90625F, -0.19375F, facing.getOpposite(), 0xFFFFFF00, MAX_LIGHT_GLOWING);
		}
	}
}
