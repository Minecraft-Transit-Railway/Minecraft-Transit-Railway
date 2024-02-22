package org.mtr.mod.screen;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;

import java.util.function.Consumer;

/**
 * Represents a World Map that is drawn as the background in the WidgetMap.
 */
public class WorldMap {
    private double updateMapTimer;
    private MapOverlayMode mapOverlayMode;
    private IntIntImmutablePair topLeft;
    private IntIntImmutablePair bottomRight;
    private final Object2ObjectOpenHashMap<IntIntImmutablePair, MapImage> mapImages = new Object2ObjectOpenHashMap<>();
    private static final int CHUNK_SIZE = 16;
    private static final int UPDATE_FREQUENCY = 40;
    private static final double LIGHT_FACTOR = 4.0;

    public WorldMap() {
        this.updateMapTimer = -1;
        this.mapOverlayMode = MapOverlayMode.TOP_VIEW;
    }

    public void update(World world, ClientPlayerEntity player, float delta) {
        if(updateMapTimer == -1 || updateMapTimer >= UPDATE_FREQUENCY) {
            updateMapTimer = 0;
            updateMap(World.cast(world), player, false);
        }

        updateMapTimer += delta;
    }

    public void setRegion(IntIntImmutablePair topLeft, IntIntImmutablePair bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void forEachTile(World world, Consumer<MapImage> callback) {
        for(MapImage mapImage : mapImages.values()) {
            if(!world.getChunkManager().isChunkLoaded(mapImage.chunkX, mapImage.chunkZ)) continue;
            callback.accept(mapImage);
        }
    }

    public void forceRefresh(World world, ClientPlayerEntity player) {
        disposeImages();
        updateMap(world, player, false);
    }
    public void refresh(World world, ClientPlayerEntity player) {
        updateMap(world, player, true);
    }

    private void updateMap(World world, ClientPlayerEntity player, boolean lazy) {
        if(topLeft == null || bottomRight == null) return;

        for (int i = topLeft.leftInt(); i <= bottomRight.leftInt(); i += CHUNK_SIZE) {
            for (int j = topLeft.rightInt(); j <= bottomRight.rightInt(); j += CHUNK_SIZE) {
                // Chunk coordinates
                int chunkX = (int)Math.floor(i / 16.0);
                int chunkZ = (int)Math.floor(j / 16.0);

                if(!world.getChunkManager().isChunkLoaded(chunkX, chunkZ)) continue;

                int chunkXStart = chunkX * CHUNK_SIZE;
                int chunkZStart = chunkZ * CHUNK_SIZE;
                IntIntImmutablePair chunkXZ = new IntIntImmutablePair(chunkX, chunkZ);

                MapImage mapTexture = mapImages.get(chunkXZ);
                if(lazy && mapTexture != null) continue;

                if(mapTexture == null) {
                    NativeImage nativeImage = new NativeImage(CHUNK_SIZE, CHUNK_SIZE, false);
                    mapTexture = new MapImage(chunkX, chunkZ, new NativeImageBackedTexture(nativeImage));
                }

                // Loop each block in chunk
                for(int k = 0; k < 16; k++) {
                    for(int l = 0; l < 16; l++) {
                        final int blockX = chunkXStart + k;
                        final int blockZ = chunkZStart + l;
                        // The y position of the highest block in this coordinates
                        final int topY = world.getTopY(HeightMapType.getMotionBlockingMapped(), blockX, blockZ) - 1;
                        final int blockY;
                        if(mapOverlayMode == MapOverlayMode.TOP_VIEW) {
                            blockY = topY;
                        } else {
                            int currentY = Math.min(topY, player.getBlockPos().getY());

                            if(currentY != topY) {
                                // Find lowest Y level so it can be projected
                                while(true) {
                                    if(currentY < -64 || !world.getBlockState(Init.newBlockPos(blockX, currentY, blockZ)).isAir()) {
                                        break;
                                    } else {
                                        currentY--;
                                    }
                                }
                            }

                            blockY = currentY;
                        }

                        final BlockPos finalPos = Init.newBlockPos(blockX, blockY, blockZ);
                        final BlockPos lightReferencePos = finalPos.up();
                        int lightLevelBlock = world.getLightLevel(LightType.getBlockMapped(), lightReferencePos);
                        int lightLevelSky = world.getLightLevel(LightType.getSkyMapped(), lightReferencePos);
                        // Light Level determined by the max brightness of either what the sky produce or what the block produce
                        int lightLevel = Math.max(lightLevelBlock, lightLevelSky);

                        int color = divideColorRGB(world.getBlockState(finalPos).getBlock().getDefaultMapColor().getColorMapped(), mapOverlayMode == MapOverlayMode.TOP_VIEW ? 2 : 1 + ((15 - lightLevel) / LIGHT_FACTOR));
                        mapTexture.texture.getImage().setPixelColor(k, l, convertColorABGR(color));
                    }
                }

                mapTexture.texture.upload();
                mapImages.put(chunkXZ, mapTexture);
            }
        }
    }

    private static int convertColorABGR(int rgb) {
        int a = 255;
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = (rgb) & 255;
        return a << 24 | b << 16 | g << 8 | r;
    }

    private static int divideColorRGB(int color, double amount) {
        final int r = (int)(((color >> 16) & 0xFF) / amount);
        final int g = (int)(((color >> 8) & 0xFF) / amount);
        final int b = (int)((color & 0xFF) / amount);
        return (r << 16) + (g << 8) + b;
    }

    public void setMapOverlayMode(MapOverlayMode overlayMode) {
        this.mapOverlayMode = overlayMode;
    }

    public void disposeImages() {
        for(MapImage image : mapImages.values()) {
            image.close();
        }
        mapImages.clear();
    }

    public static class MapImage {
        public final Identifier textureId;
        public final NativeImageBackedTexture texture;
        public final int chunkX;
        public final int chunkZ;
        public boolean disposed;
        private MapImage(int chunkX, int chunkZ, NativeImageBackedTexture texture)  {
            this.textureId = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("mtr_dashboard_map", texture);
            this.texture = texture;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        public void close() {
            this.disposed = true;
            MinecraftClient.getInstance().getTextureManager().destroyTexture(this.textureId);
        }
    }

    public enum MapOverlayMode { TOP_VIEW, CURRENT_Y }
}
