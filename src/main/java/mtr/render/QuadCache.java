package mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class QuadCache {

    public static class QuadCacheItem {
        float x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
        float u1, v1, u2, v2, u3, v3, u4, v4;
        float r, g, b, a;
        Direction facing;
        BlockPos pos;
        boolean isBlockFace;

        float[] brightness; int[] light;

        public void apply(VertexConsumer vertexConsumer, MatrixStack matrices, World world, boolean performLightUpdate) {
            if (isBlockFace) {
                applyBlock(vertexConsumer, matrices, world, performLightUpdate);
            } else {
                applyNonBlock(vertexConsumer, matrices, world, performLightUpdate);
            }
        }

        public void applyNonBlock(VertexConsumer vertexConsumer, MatrixStack matrices, World world, boolean performLightUpdate) {
            final Vec3i vec3i = facing.getVector();
            final Matrix4f matrix4f = matrices.peek().getModel();
            final Matrix3f matrix3f = matrices.peek().getNormal();
            if (this.light == null || performLightUpdate) {
                if (this.light == null) this.light = new int[1];
                this.light[0] = WorldRenderer.getLightmapCoordinates(world, pos);
            }
            vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light[0]).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
            vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).texture(u2, v2).overlay(OverlayTexture.DEFAULT_UV).light(light[0]).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
            vertexConsumer.vertex(matrix4f, x3, y3, z3).color(r, g, b, a).texture(u3, v3).overlay(OverlayTexture.DEFAULT_UV).light(light[0]).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
            vertexConsumer.vertex(matrix4f, x4, y4, z4).color(r, g, b, a).texture(u4, v4).overlay(OverlayTexture.DEFAULT_UV).light(light[0]).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
        }

        public void applyBlock(VertexConsumer vertexConsumer, MatrixStack matrices, World world, boolean performLightUpdate) {
            final BlockState bs = world.getBlockState(pos);
            final float yMax = Math.max(Math.max(y1, y2), Math.max(y3, y4));

            matrices.push();
            final float dY = 0.0625F + mtr.data.IGui.SMALL_OFFSET;
            matrices.translate(pos.getX(), pos.getY() - dY, pos.getZ());

            BlockPos lightRefPos;
            if (facing.getAxis() != Direction.Axis.Y) {
                lightRefPos = pos.offset(facing);
                // Sometimes on very steep slopes, a segment can cover multiple blocks horizontally.
                // This is to check an upper part when lower part is blocked, to prevent black faces from being shown.
                // Because such a steep slope is rarely used, it's not that necessary to divide it into multiple faces.
                while (!world.getBlockState(lightRefPos).isAir()) {
                    if (lightRefPos.getY() <= pos.getY() + yMax) {
                        lightRefPos = lightRefPos.up();
                    } else {
                        break;
                    }
                }

                // Update the positions to reject the invisible parts, making the lighting area correct.
                final float yRefOffset = lightRefPos.getY() - pos.getY();
                if (yRefOffset > 0) {
                    // This assumes y2 and y3 is at the bottom.
                    y2 = Math.min(y2, yRefOffset);
                    y3 = Math.min(y3, yRefOffset);
                }
            } else {
                lightRefPos = pos;
            }

            int[] vertexData = new int[] {
                    Float.floatToIntBits(x1), Float.floatToIntBits(y1), Float.floatToIntBits(z1), 0, Float.floatToIntBits(u1), Float.floatToIntBits(v1), 0, 0,
                    Float.floatToIntBits(x2), Float.floatToIntBits(y2), Float.floatToIntBits(z2), 0, Float.floatToIntBits(u2), Float.floatToIntBits(v2), 0, 0,
                    Float.floatToIntBits(x3), Float.floatToIntBits(y3), Float.floatToIntBits(z3), 0, Float.floatToIntBits(u3), Float.floatToIntBits(v3), 0, 0,
                    Float.floatToIntBits(x4), Float.floatToIntBits(y4), Float.floatToIntBits(z4), 0, Float.floatToIntBits(u4), Float.floatToIntBits(v4), 0, 0,
            };
            BakedQuad quad = new BakedQuad(vertexData, 0, facing, null, true);

            if (this.brightness == null || this.light == null || performLightUpdate) {
                if (MinecraftClient.isAmbientOcclusionEnabled() && (facing.getAxis() != Direction.Axis.Y)) {
                    BitSet flags = new BitSet(3);
                    float[] box = new float[Direction.values().length * 2];
                    blockModelRenderer.getQuadDimensions(world, bs, lightRefPos.offset(facing.getOpposite()), vertexData, facing, box, flags);
                    aoCalculator.apply(world, bs, lightRefPos.offset(facing.getOpposite()), facing, box, flags, true);
                    brightness = aoCalculator.brightness.clone();
                    light = aoCalculator.light.clone();
                } else {
                    final int light = WorldRenderer.getLightmapCoordinates(world, lightRefPos);
                    final float brightness = world.getBrightness(facing, true);
                    if (this.brightness == null) this.brightness = new float[4];
                    if (this.light == null) this.light = new int[4];
                    this.brightness[0] = brightness; this.brightness[1] = brightness;
                    this.brightness[2] = brightness; this.brightness[3] = brightness;
                    this.light[0] = light; this.light[1] = light;
                    this.light[2] = light; this.light[3] = light;
                }
            }
            vertexConsumer.quad(matrices.peek(), quad,  brightness, r, g, b, light, 0, false);

            matrices.pop();
        }
    }

    private static final BlockModelRenderer blockModelRenderer = new BlockModelRenderer(null);
    private static final BlockModelRenderer.AmbientOcclusionCalculator aoCalculator = blockModelRenderer.new AmbientOcclusionCalculator();

    public static class QuadCacheList {
        public Identifier texture;
        public boolean isSolid;
        public ArrayList<QuadCacheItem> cacheList = new ArrayList<>();

        public void addNonBlockFace(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4,
                                    float u1, float v1, float u2, float v2, Direction facing, int color, BlockPos lightRefPos) {
            final QuadCacheItem item = new QuadCacheItem();
            item.x1 = x1; item.y1 = y1; item.z1 = z1; item.x2 = x2; item.y2 = y2; item.z2 = z2; item.x3 = x3; item.y3 = y3; item.z3 = z3; item.x4 = x4; item.y4 = y4; item.z4 = z4;
            item.u1 = u1; item.v1 = v2; item.u2 = u2; item.v2 = v2; item.u3 = u2; item.v3 = v1; item.u4 = u1; item.v4 = v1;
            item.facing = facing; item.pos = lightRefPos; item.isBlockFace = false;
            item.a = ((color >> 24) & 0xFF) / 255F;
            item.r = ((color >> 16) & 0xFF) / 255F;
            item.g = ((color >> 8) & 0xFF)/ 255F;
            item.b = (color & 0xFF) / 255F;
            if (item.a == 0) return;
            cacheList.add(item);
        }

        public void addBlockFace(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4,
                                 float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4,
                                 Direction facing, BlockPos pos, int color) {
            final QuadCacheItem item = new QuadCacheItem();
            item.x1 = x1; item.y1 = y1; item.z1 = z1; item.x2 = x2; item.y2 = y2; item.z2 = z2; item.x3 = x3; item.y3 = y3; item.z3 = z3; item.x4 = x4; item.y4 = y4; item.z4 = z4;
            item.u1 = u1; item.v1 = v1; item.u2 = u2; item.v2 = v2; item.u3 = u3; item.v3 = v3; item.u4 = u4; item.v4 = v4;
            item.facing = facing; item.pos = pos; item.isBlockFace = true;
            item.r = ((color >> 16) & 0xFF) / 255F;
            item.g = ((color >> 8) & 0xFF) / 255F;
            item.b = (color & 0xFF) / 255F;
            item.a = 1.0F;
            cacheList.add(item);
        }

        private int lightUpdateIndex = 0;
        private static final int updatesPerFrame = 2;

        public void apply(VertexConsumerProvider vertexConsumers, MatrixStack matrices, World world) {
            final VertexConsumer vc = vertexConsumers.getBuffer(isSolid ? MoreRenderLayers.getSolid(texture) : MoreRenderLayers.getExterior(texture));

            // Caching light data - how much impact does it have?
            for (int i = 0; i < cacheList.size(); i++) {
                cacheList.get(i).apply(vc, matrices, world, i >= lightUpdateIndex && i < lightUpdateIndex + updatesPerFrame);
            }
            lightUpdateIndex += updatesPerFrame;
            if (lightUpdateIndex > cacheList.size()) lightUpdateIndex = 0;
        }
    }

    public HashMap<String, QuadCacheList> cacheMap = null;

    public void reset() {
        cacheMap.clear();
        cacheMap = null;
    }

    public QuadCacheList withTexture(String texture, boolean isSolid) {
        if (cacheMap.containsKey(texture)) return cacheMap.get(texture);
        final QuadCacheList item = new QuadCacheList();
        item.isSolid = isSolid; item.texture = new Identifier(texture);
        cacheMap.put(texture, item);
        return item;
    }

    public void renderWithCache(VertexConsumerProvider vertexConsumers, MatrixStack matrices, World world, RenderCallback callback) {
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
            callback.renderCallback(this);
        }
        cacheMap.forEach((texture, list) -> list.apply(vertexConsumers, matrices, world));
    }

    @FunctionalInterface
    interface RenderCallback {
        // Only called once. The result is then cached for all future draw calls.
        // Call QuadCache.reset() before rendering if the cache needs to be updated.
        void renderCallback(QuadCache cache);
    }

}
