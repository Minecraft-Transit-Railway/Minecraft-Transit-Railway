package mtr.render;

import mtr.block.BlockClock;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.gui.IDrawing;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class RenderClock implements IGui, IBlock, BlockEntityRenderer<BlockClock.TileEntityClock> {

	@Override
	public void render(BlockClock.TileEntityClock entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();

		final BlockState state = world.getBlockState(pos);
		final boolean rotated = IBlock.getStatePropertySafe(state, BlockClock.FACING);

		matrices.push();
		matrices.translate(0.5, 0.3125, 0.5);
		if (rotated) {
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
		}

		final long time = world.getTimeOfDay() + 6000;

		drawHand(matrices, vertexConsumers, time * 360F / 12000, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, false);

		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
		drawHand(matrices, vertexConsumers, time * 360F / 12000, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, false);

		matrices.pop();
	}

	private static void drawHand(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float rotation, boolean isHourHand) {
		matrices.push();
		matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(rotation));
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png"), false));
		IDrawing.drawTexture(matrices, vertexConsumer, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		matrices.pop();
	}
}
