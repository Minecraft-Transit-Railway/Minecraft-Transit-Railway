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
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide) {
		final float y = isOccupied ? 0.6875F : 0.125F;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.09375F, y, -0.19375F, 0.09375F, y + 0.1875F, -0.19375F, facing.getOpposite(), isOccupied ? 0xFFFF0000 : proceedColor, MAX_LIGHT_GLOWING);
	}
}
