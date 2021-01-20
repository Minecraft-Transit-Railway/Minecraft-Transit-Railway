package mtr.render;

import mtr.block.BlockClock;
import mtr.block.IBlock;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderClock extends BlockEntityRenderer<BlockClock.TileEntityClock> implements IGui, IBlock {

	public RenderClock(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

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
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
		}

		final long time = world.getTimeOfDay() + 6000;

		drawHand(matrices, vertexConsumers, time * 360F / 12000, light, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, light, false);

		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
		drawHand(matrices, vertexConsumers, time * 360F / 12000, light, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, light, false);

		matrices.pop();
	}

	private static void drawHand(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float rotation, int light, boolean isHourHand) {
		matrices.push();
		matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(rotation));
		IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/block/white.png", -0.01F, isHourHand ? 0.15F : 0.25F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, 0, 0, 1, 1, -1, light);
		matrices.pop();
	}
}
