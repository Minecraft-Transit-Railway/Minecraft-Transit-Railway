package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockClock;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;

public class RenderClock extends BlockEntityRenderer<BlockClock.BlockEntity> implements IGui, IBlock {

	public RenderClock(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockClock.BlockEntity entity, float v, GraphicsHolder graphicsHolder, int i, int i1) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos2();

		final BlockState state = world.getBlockState(pos);
		final boolean rotated = IBlock.getStatePropertySafe(state, BlockClock.FACING);

		graphicsHolder.push();
		graphicsHolder.translate(0.5, 0.3125, 0.5);
		if (rotated) {
			graphicsHolder.rotateYDegrees(90);
		}

		final long time = WorldHelper.getTimeOfDay(world) + 6000;

		drawHand(graphicsHolder, time * 360F / 12000, true);
		drawHand(graphicsHolder, time * 360F / 1000, false);

		graphicsHolder.rotateYDegrees(180);
		drawHand(graphicsHolder, time * 360F / 12000, true);
		drawHand(graphicsHolder, time * 360F / 1000, false);

		graphicsHolder.pop();
	}

	private static void drawHand(GraphicsHolder graphicsHolder, float rotation, boolean isHourHand) {
		graphicsHolder.push();
		graphicsHolder.rotateZDegrees(-rotation);
		graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));
		IDrawing.drawTexture(graphicsHolder, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		graphicsHolder.pop();
	}
}
