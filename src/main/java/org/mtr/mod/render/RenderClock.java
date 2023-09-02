package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockClock;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RenderClock extends BlockEntityRendererMapper<BlockClock.TileEntityClock> implements IGui, IBlock {

	public RenderClock(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockClock.TileEntityClock entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();

		final BlockState state = world.getBlockState(pos);
		final boolean rotated = IBlock.getStatePropertySafe(state, BlockClock.FACING);

		matrices.pushPose();
		matrices.translate(0.5, 0.3125, 0.5);
		if (rotated) {
			UtilitiesClient.rotateYDegrees(matrices, 90);
		}

		final long time = world.getDayTime() + 6000;

		drawHand(matrices, vertexConsumers, time * 360F / 12000, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, false);

		UtilitiesClient.rotateYDegrees(matrices, 180);
		drawHand(matrices, vertexConsumers, time * 360F / 12000, true);
		drawHand(matrices, vertexConsumers, time * 360F / 1000, false);

		matrices.popPose();
	}

	private static void drawHand(PoseStack matrices, MultiBufferSource vertexConsumers, float rotation, boolean isHourHand) {
		matrices.pushPose();
		UtilitiesClient.rotateZDegrees(matrices, -rotation);
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false));
		IDrawing.drawTexture(matrices, vertexConsumer, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		matrices.popPose();
	}
}
