package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

public class RenderSignalLight3Aspect<T extends BlockEntityMapper> extends RenderSignalBase<T> {

	final int proceedColor;
	final int cautionColor;

	public RenderSignalLight3Aspect(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, int proceedColor, int cautionColor) {
		super(dispatcher, isSingleSided, 3);
		this.proceedColor = proceedColor;
		this.cautionColor = cautionColor;
	}

	@Override
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, int aspect, boolean isBackSide) {
		float y = 0.6875F;
		int color = 0xFFFF0000;
		switch (aspect) {
			case 1:
				y = 0.40625F;
				color = cautionColor;
				break;
			case 2:
				y = 0.125F;
				color = proceedColor;
		}
		IDrawing.drawTexture(matrices, vertexConsumer, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, facing.getOpposite(), color, MAX_LIGHT_GLOWING);
	}
}
