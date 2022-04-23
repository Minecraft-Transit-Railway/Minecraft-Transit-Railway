package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

public class RenderSignalLight<T extends BlockEntityMapper> extends RenderSignalBase<T> {

	final boolean redOnTop;
	final int proceedColor;

	public RenderSignalLight(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, boolean redOnTop, int proceedColor) {
		super(dispatcher, isSingleSided);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide) {
		final float y = isOccupied == redOnTop ? 0.4375F : 0.0625F;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), isOccupied ? 0xFFFF0000 : proceedColor, MAX_LIGHT_GLOWING);
	}
}
