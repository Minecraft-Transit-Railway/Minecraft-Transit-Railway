package mtr.render;

import mtr.gui.IDrawing;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class RenderSignalLight<T extends BlockEntity> extends RenderSignalBase<T> {

	final boolean redOnTop;
	final int proceedColor;

	public RenderSignalLight(boolean isSingleSided, boolean redOnTop, int proceedColor) {
		super(isSingleSided);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide) {
		final float y = isOccupied == redOnTop ? 0.4375F : 0.0625F;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), isOccupied ? 0xFFFF0000 : proceedColor, MAX_LIGHT_GLOWING);
	}
}
