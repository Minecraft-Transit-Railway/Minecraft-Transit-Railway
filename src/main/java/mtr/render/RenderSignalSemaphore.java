package mtr.render;

import mtr.block.BlockSignalSemaphoreBase;
import mtr.gui.IDrawing;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class RenderSignalSemaphore<T extends BlockSignalSemaphoreBase.TileEntitySignalSemaphoreBase> extends RenderSignalBase<T> {

	private static final int ANGLE = 55;
	private static final int SPEED = 4;

	public RenderSignalSemaphore(boolean isSingleSided) {
		super(isSingleSided);
	}

	@Override
	protected void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide) {
		final float angle = isBackSide ? entity.angle2 : entity.angle1;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.0625F, 0.296875F, -0.190625F, 0.0625F, 0.453125F, -0.190625F, facing.getOpposite(), angle < ANGLE / 2F ? 0xFFFF0000 : 0xFF00FF00, MAX_LIGHT_GLOWING);
		matrices.translate(0.1875, 0.375, 0);
		matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180 + angle));

		final World world = entity.getWorld();
		if (world != null) {
			final BlockPos pos = entity.getPos();
			final VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new Identifier("mtr:textures/block/semaphore.png")));
			final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
			IDrawing.drawTexture(matrices, vertexConsumer2, -0.705F, -0.5F, -0.19375F, 0.295F, 0.5F, -0.19375F, facing.getOpposite(), ARGB_WHITE, light);
			IDrawing.drawTexture(matrices, vertexConsumer2, 0.295F, -0.5F, -0.19375F, -0.705F, 0.5F, -0.19375F, 1, 0, 0, 1, facing.getOpposite(), ARGB_WHITE, light);
		}

		final float newAngle;
		if (isOccupied) {
			newAngle = Math.max(0, angle - SPEED * tickDelta);
		} else {
			newAngle = Math.min(ANGLE, angle + SPEED * tickDelta);
		}
		if (isBackSide) {
			entity.angle2 = newAngle;
		} else {
			entity.angle1 = newAngle;
		}
	}
}
