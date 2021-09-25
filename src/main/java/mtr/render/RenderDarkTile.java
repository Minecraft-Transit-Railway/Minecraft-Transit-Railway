package mtr.render;

import mtr.Blocks;
import mtr.block.TunnelDarknessBlock;
import mtr.block.TunnelEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RenderDarkTile<T extends TunnelDarknessBlock.TileEntityTunnelDarkness> extends BlockEntityRenderer<T> {

    public static final TunnelEntity tunnel_entity = new TunnelEntity();

    public RenderDarkTile(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final PlayerEntity player = MinecraftClient.getInstance().player;
        if(!player.isHolding(item -> item == Blocks.TUNNEL_DARKNESS.asItem())) {
            if (player == null || entity.getPos().getSquaredDistance(player.getBlockPos()) < 64) {
                return;
            }
        }

        matrices.push();
        final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(tunnel_entity.getLayer(new Identifier("mtr:textures/block/tunnel_darkness.png")));
        tunnel_entity.render(matrices, vertexConsumer, light, overlay, 1,1,1, 1);
        matrices.pop();
    }
    @Override
    public boolean rendersOutsideBoundingBox(T tunnel_entity) {
        return true;
    }
}
