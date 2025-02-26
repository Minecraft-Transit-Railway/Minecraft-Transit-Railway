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

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(pos.getX() + 0.5, pos.getY() + 0.3125, pos.getZ() + 0.5);
		if (rotated) {
			storedMatrixTransformations.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(90));
		}

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
			storedMatrixTransformations.transform(graphicsHolderNew, offset);
			final long time = WorldHelper.getTimeOfDay(world) + 6000;

			drawHand(graphicsHolderNew, time * 360F / 12000, true);
			drawHand(graphicsHolderNew, time * 360F / 1000, false);

			graphicsHolderNew.rotateYDegrees(180);
			drawHand(graphicsHolderNew, time * 360F / 12000, true);
			drawHand(graphicsHolderNew, time * 360F / 1000, false);

			graphicsHolderNew.pop();
		});
	}

	private static void drawHand(GraphicsHolder graphicsHolder, float rotation, boolean isHourHand) {
		graphicsHolder.push();
		graphicsHolder.rotateZDegrees(-rotation);
		graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));
		IDrawing.drawTexture(graphicsHolder, -0.01F, isHourHand ? 0.15F : 0.24F, isHourHand ? 0.1F : 0.105F, 0.01F, -0.03F, isHourHand ? 0.1F : 0.105F, Direction.UP, ARGB_LIGHT_GRAY, MAX_LIGHT_INTERIOR);
		graphicsHolder.pop();
	}
}
