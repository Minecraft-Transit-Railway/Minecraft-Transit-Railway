package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;

public class RenderBoatNode extends BlockEntityRenderer<BlockNode.BlockEntity> {

	public RenderBoatNode(BlockEntityRendererArgument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockNode.BlockEntity entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockState state = world.getBlockState(entity.getPos2());
		if (state.getBlock().data instanceof BlockNode.BlockBoatNode && !IBlock.getStatePropertySafe(state, BlockNode.IS_CONNECTED)) {
			return;
		}

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null || !RenderRails.isHoldingRailRelated(clientPlayerEntity)) {
			return;
		}

		graphicsHolder.push();
		graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(new Identifier("textures/block/oak_log.png")));
		IDrawing.drawTexture(graphicsHolder, 0.25F, 0, 0.25F, 0.25F, 0, 0.75F, 0.75F, 0, 0.75F, 0.75F, 0, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, Direction.EAST, -1, light);
		IDrawing.drawTexture(graphicsHolder, 0.75F, 0, 0.25F, 0.75F, 0, 0.75F, 0.25F, 0, 0.75F, 0.25F, 0, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, Direction.DOWN, -1, light);
		graphicsHolder.pop();
	}
}
