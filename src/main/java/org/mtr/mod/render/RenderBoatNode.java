package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockNode;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RenderBoatNode extends BlockEntityRendererMapper<BlockNode.TileEntityBoatNode> {

	public RenderBoatNode(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockNode.TileEntityBoatNode entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockState state = world.getBlockState(entity.getBlockPos());
		if (state.getBlock() instanceof BlockNode.BlockBoatNode && !IBlock.getStatePropertySafe(state, BlockNode.IS_CONNECTED)) {
			return;
		}

		final Player player = Minecraft.getInstance().player;
		if (player == null || !RenderTrains.isHoldingRailRelated(player)) {
			return;
		}

		matrices.pushPose();
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("textures/block/oak_log.png")));
		IDrawing.drawTexture(matrices, vertexConsumer, 0.25F, 0, 0.25F, 0.25F, 0, 0.75F, 0.75F, 0, 0.75F, 0.75F, 0, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, Direction.EAST, -1, light);
		IDrawing.drawTexture(matrices, vertexConsumer, 0.75F, 0, 0.25F, 0.75F, 0, 0.75F, 0.25F, 0, 0.75F, 0.25F, 0, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, Direction.DOWN, -1, light);
		matrices.popPose();
	}
}
