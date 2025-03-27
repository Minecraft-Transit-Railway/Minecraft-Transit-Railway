package org.mtr.map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.cache.CachedFileProvider;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * This class generates image tiles that can be used in the Dashboard or the Transport System Map.
 * Tiles are bigger than one chunk but are updated regularly.
 * Shading is done similarly to Squaremap's rendering.
 */
public final class MapTileProvider extends CachedFileProvider<MapTileResource> {

	private final World world;
	private final MapType mapType;

	public static final int TILE_SIZE = 256;

	public MapTileProvider(World world, String uniqueWorldId, MapType mapType) {
		super(MinecraftClient.getInstance().runDirectory.toPath().resolve("config/cache/map").resolve(uniqueWorldId).resolve(MTR.getWorldId(world)));
		this.world = world;
		this.mapType = mapType;
	}

	@Nullable
	public VertexBuffer getTile(BlockPos blockPos) {
		final int chunkX = Math.floorDiv(blockPos.getX(), TILE_SIZE);
		final int y = mapType == MapType.DYNAMIC ? blockPos.getY() : 0;
		final int chunkZ = Math.floorDiv(blockPos.getZ(), TILE_SIZE);
		final MapTileResource mapTileResource = get(new BlockPos(chunkX, y, chunkZ).asLong(), cacheDirectory -> new MapTileResource(
				world, mapType,
				chunkX, y, chunkZ,
				cacheDirectory.resolve(String.format("%s_%s_%s_%s", mapType.toString().toLowerCase(Locale.ENGLISH), chunkX, y, chunkZ))
		));
		return mapTileResource == null ? null : mapTileResource.getVertexBuffer();
	}

	public enum MapType {TERRAIN, ELEVATION, SATELLITE, DYNAMIC}
}
