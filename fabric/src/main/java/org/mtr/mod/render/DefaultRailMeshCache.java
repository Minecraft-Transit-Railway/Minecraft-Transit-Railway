package org.mtr.mod.render;

import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mapping.render.model.Mesh;
import org.mtr.mapping.render.model.RawMesh;
import org.mtr.mapping.render.object.IndexBuffer;
import org.mtr.mapping.render.object.VertexArray;
import org.mtr.mapping.render.object.VertexBuffer;
import org.mtr.mapping.render.vertex.Vertex;
import org.mtr.mapping.render.vertex.VertexAttributeMapping;
import org.mtr.mapping.render.vertex.VertexAttributeSource;
import org.mtr.mapping.render.vertex.VertexAttributeType;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public final class DefaultRailMeshCache implements IGui {

	static final double PAGE_LENGTH_METERS = 64;
	static final int SEGMENTS_PER_PAGE = 128;
	private static final int MAX_PREPARES_PER_FRAME = 16;
	private static final int MAX_OUTSTANDING_PREPARES = 16;
	private static final int MAX_PROACTIVE_OUTSTANDING = 2;
	private static final int MAX_PAGE_UPLOADS_PER_FRAME = 8;
	private static final long UPLOAD_BUDGET_NANOS = 2_000_000;
	private static final long MAX_CACHE_BYTES = 128L * 1024 * 1024;
	private static final long ESTIMATED_BYTES_PER_SEGMENT = 352;
	private static final float RAIL_Y_OFFSET = 0.065625F;
	private static final double LIGHT_REFERENCE_OFFSET = 0.1;
	static final VertexAttributeMapping RAIL_MAPPING = new VertexAttributeMapping.Builder()
			.set(VertexAttributeType.POSITION, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.COLOR, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_TEXTURE, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.UV_OVERLAY, VertexAttributeSource.GLOBAL)
			.set(VertexAttributeType.UV_LIGHTMAP, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.NORMAL, VertexAttributeSource.VERTEX_BUFFER)
			.set(VertexAttributeType.MATRIX_MODEL, VertexAttributeSource.GLOBAL)
			.build();
	private static final MaterialProperties RAIL_MATERIAL = new MaterialProperties(OptimizedModel.ShaderType.CUTOUT, new Identifier("textures/block/rail.png"), null);
	private static final LinkedHashMap<String, CacheEntry> CACHE = new LinkedHashMap<>(256, 0.75F, false);
	private static final Map<String, PreparingEntry> PREPARING = new HashMap<>();
	private static final Map<String, Rail> FAILED_RAILS = new HashMap<>();
	private static final Set<String> DEFERRED_REMOVALS = new HashSet<>();
	private static final ObjectArrayList<UploadedPage> VISIBLE_PAGES = new ObjectArrayList<>();
	private static final ExecutorService PREPARE_EXECUTOR = Executors.newFixedThreadPool(Math.max(1, Math.min(2, Runtime.getRuntime().availableProcessors() / 4)), runnable -> {
		final Thread thread = new Thread(runnable, "MTR Default Rail Mesh Builder");
		thread.setDaemon(true);
		return thread;
	});

	private static ClientWorld currentWorld;
	private static long estimatedCacheBytes;
	private static int preparesThisFrame;
	private static int pageUploadsThisFrame;
	private static long uploadDeadlineNanos;
	private static boolean shadowPass;
	private static double cameraX;
	private static double cameraY;
	private static double cameraZ;
	private static double renderDistance;
	private static float yawCos;
	private static float yawSin;
	private static float pitchCos;
	private static float pitchSin;

	static boolean isEligible(boolean normalRenderState, TransportMode transportMode, Iterable<String> styles, boolean defaultRail3D) {
		if (!normalRenderState || transportMode != TransportMode.TRAIN || defaultRail3D || styles == null) {
			return false;
		}
		final Iterator<String> iterator = styles.iterator();
		return iterator.hasNext() && CustomResourceLoader.DEFAULT_RAIL_ID.equals(iterator.next()) && !iterator.hasNext();
	}

	public static void beginFrame(ClientWorld clientWorld, boolean renderingShadows) {
		VISIBLE_PAGES.clear();
		DEFERRED_REMOVALS.forEach(DefaultRailMeshCache::removeEntry);
		DEFERRED_REMOVALS.clear();
		preparesThisFrame = 0;
		pageUploadsThisFrame = 0;
		uploadDeadlineNanos = System.nanoTime() + UPLOAD_BUDGET_NANOS;
		shadowPass = renderingShadows;
		if (currentWorld == null || !currentWorld.equals(clientWorld)) {
			clear();
			currentWorld = clientWorld;
		}
		if (!renderingShadows) {
			harvestPreparedEntries();
		}
		trimToBudget();

		final Camera camera = MinecraftClient.getInstance().getGameRendererMapped().getCamera();
		final Vector3d cameraPosition = camera.getPos();
		cameraX = cameraPosition.getXMapped();
		cameraY = cameraPosition.getYMapped();
		cameraZ = cameraPosition.getZMapped();
		renderDistance = MinecraftClientHelper.getRenderDistance() * 16D;
		final float yaw = (float) Math.toRadians(camera.getYaw());
		final float pitch = (float) Math.toRadians(camera.getPitch());
		yawCos = MathHelper.cos(yaw);
		yawSin = MathHelper.sin(yaw);
		pitchCos = MathHelper.cos(pitch);
		pitchSin = MathHelper.sin(pitch);
	}

	static boolean tryRender(ClientWorld clientWorld, Rail rail, boolean normalRenderState, float railWidth) {
		if (currentWorld == null || !currentWorld.equals(clientWorld) || !OptimizedRenderer.hasOptimizedRendering() ||
				!isEligible(normalRenderState, rail.getTransportMode(), rail.getStyles(), Config.getClient().getDefaultRail3D())) {
			return false;
		}

		final String id = rail.getHexId();
		final long fingerprint = getGeometryFingerprint(rail);
		CacheEntry entry = CACHE.get(id);
		if (entry != null && entry.fingerprint != fingerprint) {
			removeEntry(id);
			entry = null;
		} else if (entry != null) {
			CACHE.remove(id);
			CACHE.put(id, entry);
			entry.rail = rail;
		}

		PreparingEntry preparingEntry = PREPARING.get(id);
		if (preparingEntry != null && preparingEntry.fingerprint != fingerprint) {
			preparingEntry.cancel();
			PREPARING.remove(id);
			preparingEntry = null;
		} else if (preparingEntry != null) {
			preparingEntry.proactive = false;
		}
		if (entry == null) {
			if (shadowPass || FAILED_RAILS.get(id) == rail) {
				return false;
			}
			if (preparingEntry == null) {
				cancelProactivePreparingEntries();
				if (preparesThisFrame >= MAX_PREPARES_PER_FRAME || PREPARING.size() >= MAX_OUTSTANDING_PREPARES) {
					return false;
				}
				preparesThisFrame++;
				preparingEntry = new PreparingEntry(rail, fingerprint, PREPARE_EXECUTOR.submit(() -> prepare(rail, railWidth)));
				PREPARING.put(id, preparingEntry);
			}
			if (!preparingEntry.future.isDone()) {
				return false;
			}
			try {
				final PreparedRail preparedRail = preparingEntry.future.get();
				PREPARING.remove(id);
				if (preparedRail == null || preparedRail.pages.isEmpty() || preparedRail.estimatedBytes > MAX_CACHE_BYTES) {
					FAILED_RAILS.put(id, rail);
					return false;
				}
				entry = new CacheEntry(rail, fingerprint, preparedRail);
				CACHE.put(id, entry);
				estimatedCacheBytes += entry.estimatedBytes;
			} catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
				PREPARING.remove(id);
				return false;
			} catch (ExecutionException | RuntimeException | Error exception) {
				PREPARING.remove(id);
				FAILED_RAILS.put(id, rail);
				Init.LOGGER.warn("Unable to prepare the cached default rail mesh for {}", id, exception);
				return false;
			}
		}

		if (entry.hasMissingVisiblePages()) {
			if (shadowPass || pageUploadsThisFrame >= MAX_PAGE_UPLOADS_PER_FRAME || (pageUploadsThisFrame > 0 && System.nanoTime() >= uploadDeadlineNanos)) {
				return false;
			}
			try {
				pageUploadsThisFrame += entry.uploadMissingVisiblePages(clientWorld, 1, uploadDeadlineNanos);
			} catch (RuntimeException | Error exception) {
				removeEntry(id);
				FAILED_RAILS.put(id, rail);
				Init.LOGGER.warn("Unable to upload the cached default rail mesh for {}", id, exception);
				return false;
			}
			if (entry.hasMissingVisiblePages()) {
				return false;
			}
		}

		for (final UploadedPage page : entry.pages) {
			if (page != null && page.isVisible()) {
				VISIBLE_PAGES.add(page);
			}
		}
		return true;
	}

	public static void scheduleRender() {
		if (!VISIBLE_PAGES.isEmpty()) {
			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, DefaultRailMeshCache::queueVisiblePages);
		}
	}

	public static void finishFrame(ClientWorld clientWorld) {
		if (shadowPass || currentWorld == null || !currentWorld.equals(clientWorld)) {
			return;
		}
		prewarmNearbyRails();
		if (pageUploadsThisFrame >= MAX_PAGE_UPLOADS_PER_FRAME || (pageUploadsThisFrame > 0 && System.nanoTime() >= uploadDeadlineNanos)) {
			return;
		}
		for (final Map.Entry<String, CacheEntry> mapEntry : CACHE.entrySet()) {
			final CacheEntry entry = mapEntry.getValue();
			if (entry.hasMissingLoadedPages(clientWorld)) {
				try {
					final int uploadedPages = entry.uploadMissingPages(clientWorld, 1, uploadDeadlineNanos, false, true);
					pageUploadsThisFrame += uploadedPages;
					if (uploadedPages == 0) {
						continue;
					}
				} catch (RuntimeException | Error exception) {
					FAILED_RAILS.put(mapEntry.getKey(), entry.rail);
					DEFERRED_REMOVALS.add(mapEntry.getKey());
					Init.LOGGER.warn("Unable to upload the cached default rail mesh for {}", mapEntry.getKey(), exception);
				}
				return;
			}
		}
	}

	public static void reconcile(Iterable<MinecraftClientData.RailWrapper> liveWrappers) {
		final Set<String> liveIds = new HashSet<>();
		for (final MinecraftClientData.RailWrapper wrapper : liveWrappers) {
			final String id = wrapper.hexId;
			final Rail rail = wrapper.getRail();
			liveIds.add(id);
			final CacheEntry entry = CACHE.get(id);
			if (entry != null) {
				if (entry.fingerprint == getGeometryFingerprint(rail)) {
					entry.rail = rail;
				} else {
					removeEntry(id);
				}
			}
			final PreparingEntry preparingEntry = PREPARING.get(id);
			if (preparingEntry != null && preparingEntry.fingerprint != getGeometryFingerprint(rail)) {
				preparingEntry.cancel();
				PREPARING.remove(id);
			}
			if (FAILED_RAILS.get(id) != rail) {
				FAILED_RAILS.remove(id);
			}
		}
		removeMissingEntries(liveIds);
		FAILED_RAILS.keySet().removeIf(id -> !liveIds.contains(id));
	}

	public static void invalidateChunk(int chunkX, int chunkZ) {
		invalidateBounds(chunkX * 16D, Double.NEGATIVE_INFINITY, chunkZ * 16D, (chunkX + 1) * 16D, Double.POSITIVE_INFINITY, (chunkZ + 1) * 16D);
	}

	public static void invalidateLightSection(int sectionX, int sectionY, int sectionZ) {
		invalidateBounds(sectionX * 16D, sectionY * 16D, sectionZ * 16D, (sectionX + 1) * 16D, (sectionY + 1) * 16D, (sectionZ + 1) * 16D);
	}

	public static void clear() {
		PREPARING.values().forEach(PreparingEntry::cancel);
		PREPARING.clear();
		CACHE.values().forEach(CacheEntry::close);
		CACHE.clear();
		FAILED_RAILS.clear();
		DEFERRED_REMOVALS.clear();
		VISIBLE_PAGES.clear();
		estimatedCacheBytes = 0;
		currentWorld = null;
	}

	static int pageIndexForSegment(int segmentIndex) {
		return segmentIndex / SEGMENTS_PER_PAGE;
	}

	static int pageCountForSegments(int segmentCount) {
		return segmentCount <= 0 ? 0 : 1 + (segmentCount - 1) / SEGMENTS_PER_PAGE;
	}

	static long geometryFingerprint(double angle1, double angle2, Rail.Shape shape, double verticalRadius, double length) {
		long hash = 0xCBF29CE484222325L;
		hash = mix(hash, Double.doubleToLongBits(angle1));
		hash = mix(hash, Double.doubleToLongBits(angle2));
		hash = mix(hash, shape == null ? -1 : shape.ordinal());
		hash = mix(hash, Double.doubleToLongBits(verticalRadius));
		return mix(hash, Double.doubleToLongBits(length));
	}

	static void appendSegmentVertices(
			Consumer<Vertex> consumer,
			double originX, double originY, double originZ,
			double x1, double z1, double x2, double z2,
			double x3, double z3, double x4, double z4,
			double y1, double y2,
			float yOffset, int light
	) {
		final float textureOffset = ((int) (x1 + z1) % 4) * 0.25F;
		final float v1 = 0.1875F + textureOffset;
		final float v2 = 0.3125F + textureOffset;
		final float relativeX1 = (float) (x1 - originX);
		final float relativeX2 = (float) (x2 - originX);
		final float relativeX3 = (float) (x3 - originX);
		final float relativeX4 = (float) (x4 - originX);
		final float relativeY1 = (float) (y1 - originY) + yOffset;
		final float relativeY2 = (float) (y2 - originY) + yOffset;
		final float relativeZ1 = (float) (z1 - originZ);
		final float relativeZ2 = (float) (z2 - originZ);
		final float relativeZ3 = (float) (z3 - originZ);
		final float relativeZ4 = (float) (z4 - originZ);

		consumer.accept(vertex(relativeX1, relativeY1, relativeZ1, 0, v2, light));
		consumer.accept(vertex(relativeX2, relativeY1 + IGui.SMALL_OFFSET, relativeZ2, 1, v2, light));
		consumer.accept(vertex(relativeX3, relativeY2, relativeZ3, 1, v1, light));
		consumer.accept(vertex(relativeX4, relativeY2 + IGui.SMALL_OFFSET, relativeZ4, 0, v1, light));
		consumer.accept(vertex(relativeX2, relativeY1 + IGui.SMALL_OFFSET, relativeZ2, 0, v2, light));
		consumer.accept(vertex(relativeX1, relativeY1, relativeZ1, 1, v2, light));
		consumer.accept(vertex(relativeX4, relativeY2 + IGui.SMALL_OFFSET, relativeZ4, 1, v1, light));
		consumer.accept(vertex(relativeX3, relativeY2, relativeZ3, 0, v1, light));
	}

	static boolean isPageVisible(
			double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ,
			double cameraX, double cameraY, double cameraZ,
			double renderDistance,
			float yawCos, float yawSin, float pitchCos, float pitchSin
	) {
		final double closestX = Math.max(minX, Math.min(maxX, cameraX));
		final double closestZ = Math.max(minZ, Math.min(maxZ, cameraZ));
		final double differenceX = closestX - cameraX;
		final double differenceZ = closestZ - cameraZ;
		final double distanceSquared = differenceX * differenceX + differenceZ * differenceZ;
		if (distanceSquared > renderDistance * renderDistance) {
			return false;
		}
		if (distanceSquared <= 32 * 32) {
			return true;
		}
		final double coefficientX = -yawSin * pitchCos;
		final double coefficientY = -pitchSin;
		final double coefficientZ = yawCos * pitchCos;
		final double x = coefficientX >= 0 ? maxX : minX;
		final double y = coefficientY >= 0 ? maxY : minY;
		final double z = coefficientZ >= 0 ? maxZ : minZ;
		return (x - cameraX) * coefficientX + (y - cameraY) * coefficientY + (z - cameraZ) * coefficientZ >= 0;
	}

	private static PreparedRail prepare(Rail rail, float railWidth) {
		final ObjectArrayList<PreparedPageBuilder> builders = new ObjectArrayList<>();
		final PreparedPageBuilder[] currentBuilder = {null};
		final int[] segmentIndex = {0};
		rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
			if (Thread.currentThread().isInterrupted()) {
				throw new CancellationException();
			}
			if (segmentIndex[0] % SEGMENTS_PER_PAGE == 0) {
				currentBuilder[0] = new PreparedPageBuilder(x1, Math.min(y1, y2), z1);
				builders.add(currentBuilder[0]);
			}
			currentBuilder[0].addSegment(x1, z1, x2, z2, x3, z3, x4, z4, y1, y2);
			segmentIndex[0]++;
		}, 0.5, -railWidth, railWidth);

		if (builders.isEmpty()) {
			return null;
		}

		final ObjectArrayList<PreparedPage> pages = new ObjectArrayList<>(builders.size());
		long estimatedBytes = 0;
		for (final PreparedPageBuilder builder : builders) {
			final PreparedPage page = builder.build();
			pages.add(page);
			estimatedBytes += page.estimatedBytes;
		}
		return pages.isEmpty() ? null : new PreparedRail(pages, estimatedBytes);
	}

	private static long getGeometryFingerprint(Rail rail) {
		return geometryFingerprint(
				rail.getStartAngle(false).angleRadians,
				rail.getStartAngle(true).angleRadians,
				rail.railMath.getShape(),
				rail.railMath.getVerticalRadius(),
				rail.railMath.getLength()
		);
	}

	private static void queueVisiblePages(GraphicsHolder graphicsHolder, Vector3d offset) {
		for (final UploadedPage page : VISIBLE_PAGES) {
			graphicsHolder.push();
			graphicsHolder.translate(page.originX - offset.getXMapped(), page.originY - offset.getYMapped(), page.originZ - offset.getZMapped());
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(page.model, graphicsHolder, ARGB_WHITE, GraphicsHolder.getDefaultLight());
			graphicsHolder.pop();
		}
	}

	private static void trimToBudget() {
		final Iterator<Map.Entry<String, CacheEntry>> iterator = CACHE.entrySet().iterator();
		while (estimatedCacheBytes > MAX_CACHE_BYTES && iterator.hasNext()) {
			final CacheEntry entry = iterator.next().getValue();
			iterator.remove();
			estimatedCacheBytes -= entry.estimatedBytes;
			entry.close();
		}
	}

	private static void harvestPreparedEntries() {
		final Iterator<Map.Entry<String, PreparingEntry>> iterator = PREPARING.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry<String, PreparingEntry> mapEntry = iterator.next();
			final PreparingEntry preparingEntry = mapEntry.getValue();
			if (!preparingEntry.future.isDone()) {
				continue;
			}
			iterator.remove();
			try {
				final PreparedRail preparedRail = preparingEntry.future.get();
				if (preparedRail == null || preparedRail.pages.isEmpty() || preparedRail.estimatedBytes > MAX_CACHE_BYTES) {
					FAILED_RAILS.put(mapEntry.getKey(), preparingEntry.rail);
				} else if (!CACHE.containsKey(mapEntry.getKey())) {
					final CacheEntry entry = new CacheEntry(preparingEntry.rail, preparingEntry.fingerprint, preparedRail);
					CACHE.put(mapEntry.getKey(), entry);
					estimatedCacheBytes += entry.estimatedBytes;
				}
			} catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
				return;
			} catch (ExecutionException | RuntimeException | Error exception) {
				FAILED_RAILS.put(mapEntry.getKey(), preparingEntry.rail);
				Init.LOGGER.warn("Unable to prepare the cached default rail mesh for {}", mapEntry.getKey(), exception);
			}
		}
	}

	private static void prewarmNearbyRails() {
		if (!OptimizedRenderer.hasOptimizedRendering() || estimatedCacheBytes >= MAX_CACHE_BYTES) {
			return;
		}
		int proactiveOutstanding = 0;
		for (final PreparingEntry entry : PREPARING.values()) {
			if (entry.proactive) {
				proactiveOutstanding++;
			}
		}
		for (final MinecraftClientData.RailWrapper wrapper : MinecraftClientData.getInstance().railWrapperList.values()) {
			if (preparesThisFrame >= MAX_PREPARES_PER_FRAME || PREPARING.size() >= MAX_OUTSTANDING_PREPARES || proactiveOutstanding >= MAX_PROACTIVE_OUTSTANDING) {
				return;
			}
			final Rail rail = wrapper.getRail();
			final String id = rail.getHexId();
			if (CACHE.containsKey(id) || PREPARING.containsKey(id) || FAILED_RAILS.get(id) == rail ||
					!isEligible(true, rail.getTransportMode(), rail.getStyles(), Config.getClient().getDefaultRail3D())) {
				continue;
			}
			final double closestX = Math.max(rail.railMath.minX, Math.min(rail.railMath.maxX, cameraX));
			final double closestZ = Math.max(rail.railMath.minZ, Math.min(rail.railMath.maxZ, cameraZ));
			final double differenceX = closestX - cameraX;
			final double differenceZ = closestZ - cameraZ;
			if (differenceX * differenceX + differenceZ * differenceZ > renderDistance * renderDistance) {
				continue;
			}
			preparesThisFrame++;
			proactiveOutstanding++;
			final long fingerprint = getGeometryFingerprint(rail);
			PREPARING.put(id, new PreparingEntry(rail, fingerprint, PREPARE_EXECUTOR.submit(() -> prepare(rail, 1)), true));
		}
	}

	private static void cancelProactivePreparingEntries() {
		final Iterator<Map.Entry<String, PreparingEntry>> iterator = PREPARING.entrySet().iterator();
		while (iterator.hasNext()) {
			final PreparingEntry entry = iterator.next().getValue();
			if (entry.proactive) {
				entry.cancel();
				iterator.remove();
			}
		}
	}

	private static void removeMissingEntries(Set<String> liveIds) {
		final Iterator<Map.Entry<String, CacheEntry>> iterator = CACHE.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry<String, CacheEntry> mapEntry = iterator.next();
			if (!liveIds.contains(mapEntry.getKey())) {
				final CacheEntry entry = mapEntry.getValue();
				iterator.remove();
				estimatedCacheBytes -= entry.estimatedBytes;
				entry.close();
			}
		}
		final Iterator<Map.Entry<String, PreparingEntry>> preparingIterator = PREPARING.entrySet().iterator();
		while (preparingIterator.hasNext()) {
			final Map.Entry<String, PreparingEntry> mapEntry = preparingIterator.next();
			if (!liveIds.contains(mapEntry.getKey())) {
				mapEntry.getValue().cancel();
				preparingIterator.remove();
			}
		}
	}

	private static void invalidateBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		for (final CacheEntry entry : CACHE.values()) {
			entry.invalidateBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}

	private static void removeEntry(String id) {
		final PreparingEntry preparingEntry = PREPARING.remove(id);
		if (preparingEntry != null) {
			preparingEntry.cancel();
		}
		final CacheEntry entry = CACHE.remove(id);
		if (entry != null) {
			estimatedCacheBytes -= entry.estimatedBytes;
			entry.close();
		}
	}

	private static long mix(long hash, long value) {
		return (hash ^ value) * 0x100000001B3L;
	}

	private static Vertex vertex(float x, float y, float z, float u, float v, int light) {
		final Vertex vertex = new Vertex();
		vertex.position = new Vector3f(x, y, z);
		// Vertex equality omits light; subnormal components keep differently lit vertices distinct without changing the rendered normal.
		vertex.normal = new Vector3f((light & 0xFFFF) * Float.MIN_VALUE, 1, ((light >>> 16) & 0xFFFF) * Float.MIN_VALUE);
		vertex.u = u;
		vertex.v = v;
		vertex.light = light;
		return vertex;
	}

	private static final class PreparedPageBuilder {

		private final double originX;
		private final double originY;
		private final double originZ;
		private final DoubleArrayList segments = new DoubleArrayList(SEGMENTS_PER_PAGE * 10);
		private int segmentCount;
		private double minX = Double.POSITIVE_INFINITY;
		private double minY = Double.POSITIVE_INFINITY;
		private double minZ = Double.POSITIVE_INFINITY;
		private double maxX = Double.NEGATIVE_INFINITY;
		private double maxY = Double.NEGATIVE_INFINITY;
		private double maxZ = Double.NEGATIVE_INFINITY;

		private PreparedPageBuilder(double originX, double originY, double originZ) {
			this.originX = originX;
			this.originY = originY;
			this.originZ = originZ;
		}

		private void addSegment(double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double y1, double y2) {
			segments.add(x1);
			segments.add(z1);
			segments.add(x2);
			segments.add(z2);
			segments.add(x3);
			segments.add(z3);
			segments.add(x4);
			segments.add(z4);
			segments.add(y1);
			segments.add(y2);
			minX = Math.min(minX, Math.min(Math.min(x1, x2), Math.min(x3, x4)));
			minY = Math.min(minY, Math.min(y1, y2) + RAIL_Y_OFFSET);
			minZ = Math.min(minZ, Math.min(Math.min(z1, z2), Math.min(z3, z4)));
			maxX = Math.max(maxX, Math.max(Math.max(x1, x2), Math.max(x3, x4)));
			maxY = Math.max(maxY, Math.max(y1, y2) + RAIL_Y_OFFSET + IGui.SMALL_OFFSET);
			maxZ = Math.max(maxZ, Math.max(Math.max(z1, z2), Math.max(z3, z4)));

			final double lightX = Math.floor(x1);
			final double lightY = Math.floor(y1 + LIGHT_REFERENCE_OFFSET);
			final double lightZ = Math.floor(z1);
			minX = Math.min(minX, lightX);
			minY = Math.min(minY, lightY);
			minZ = Math.min(minZ, lightZ);
			maxX = Math.max(maxX, lightX + 1);
			maxY = Math.max(maxY, lightY + 1);
			maxZ = Math.max(maxZ, lightZ + 1);
			segmentCount++;
		}

		private PreparedPage build() {
			return new PreparedPage(
					originX, originY, originZ,
					minX, minY, minZ, maxX, maxY, maxZ,
					segments.toDoubleArray(), segmentCount,
					segmentCount * ESTIMATED_BYTES_PER_SEGMENT
			);
		}
	}

	private static final class PreparedPage {

		private final double originX;
		private final double originY;
		private final double originZ;
		private final double minX;
		private final double minY;
		private final double minZ;
		private final double maxX;
		private final double maxY;
		private final double maxZ;
		private final double[] segments;
		private final int segmentCount;
		private final long estimatedBytes;

		private PreparedPage(
				double originX, double originY, double originZ,
				double minX, double minY, double minZ,
				double maxX, double maxY, double maxZ,
				double[] segments, int segmentCount, long estimatedBytes
		) {
			this.originX = originX;
			this.originY = originY;
			this.originZ = originZ;
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
			this.segments = segments;
			this.segmentCount = segmentCount;
			this.estimatedBytes = estimatedBytes;
		}

		private boolean intersects(double otherMinX, double otherMinY, double otherMinZ, double otherMaxX, double otherMaxY, double otherMaxZ) {
			return minX <= otherMaxX && maxX >= otherMinX && minY <= otherMaxY && maxY >= otherMinY && minZ <= otherMaxZ && maxZ >= otherMinZ;
		}

		private boolean isVisible() {
			return isPageVisible(minX, minY, minZ, maxX, maxY, maxZ, cameraX, cameraY, cameraZ, renderDistance, yawCos, yawSin, pitchCos, pitchSin);
		}

		private boolean areChunksLoaded(ClientWorld clientWorld) {
			final int minChunkX = (int) Math.floor(minX / 16);
			final int minChunkZ = (int) Math.floor(minZ / 16);
			final int maxChunkX = (int) Math.floor(Math.nextDown(maxX) / 16);
			final int maxChunkZ = (int) Math.floor(Math.nextDown(maxZ) / 16);
			for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
				for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
					if (!clientWorld.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
						return false;
					}
				}
			}
			return true;
		}
	}

	private static final class PreparedRail {

		private final ObjectArrayList<PreparedPage> pages;
		private final long estimatedBytes;

		private PreparedRail(ObjectArrayList<PreparedPage> pages, long estimatedBytes) {
			this.pages = pages;
			this.estimatedBytes = estimatedBytes;
		}
	}

	private static final class PreparingEntry {

		private final Rail rail;
		private final long fingerprint;
		private final Future<PreparedRail> future;
		private boolean proactive;

		private PreparingEntry(Rail rail, long fingerprint, Future<PreparedRail> future) {
			this(rail, fingerprint, future, false);
		}

		private PreparingEntry(Rail rail, long fingerprint, Future<PreparedRail> future, boolean proactive) {
			this.rail = rail;
			this.fingerprint = fingerprint;
			this.future = future;
			this.proactive = proactive;
		}

		private void cancel() {
			future.cancel(true);
		}
	}

	private static final class UploadedPage implements AutoCloseable {

		private final double originX;
		private final double originY;
		private final double originZ;
		private final double minX;
		private final double minY;
		private final double minZ;
		private final double maxX;
		private final double maxY;
		private final double maxZ;
		private final Mesh mesh;
		private final OptimizedModel model;
		private boolean closed;

		private UploadedPage(PreparedPage page, Mesh mesh, OptimizedModel model) {
			originX = page.originX;
			originY = page.originY;
			originZ = page.originZ;
			minX = page.minX;
			minY = page.minY;
			minZ = page.minZ;
			maxX = page.maxX;
			maxY = page.maxY;
			maxZ = page.maxZ;
			this.mesh = mesh;
			this.model = model;
		}

		private static UploadedPage upload(ClientWorld clientWorld, PreparedPage page) {
			final RawMesh rawMesh = new RawMesh(RAIL_MATERIAL);
			final Consumer<Vertex> vertexConsumer = rawMesh::addVertex;
			for (int offset = 0; offset < page.segments.length; offset += 10) {
				final double x1 = page.segments[offset];
				final double z1 = page.segments[offset + 1];
				final double y1 = page.segments[offset + 8];
				final BlockPos blockPos = Init.newBlockPos(x1, y1 + LIGHT_REFERENCE_OFFSET, z1);
				final int light = LightmapTextureManager.pack(
						clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos),
						clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos)
				);
				appendSegmentVertices(
						vertexConsumer, page.originX, page.originY, page.originZ,
						x1, z1, page.segments[offset + 2], page.segments[offset + 3],
						page.segments[offset + 4], page.segments[offset + 5], page.segments[offset + 6], page.segments[offset + 7],
						y1, page.segments[offset + 9], RAIL_Y_OFFSET, light
				);
			}
			rawMesh.distinct();
			rawMesh.triangulate();
			final Mesh mesh = new Mesh(new VertexBuffer(), new IndexBuffer(rawMesh.faces.size(), 0x1405), RAIL_MATERIAL);
			VertexArray vertexArray = null;
			try {
				rawMesh.upload(mesh, RAIL_MAPPING);
				vertexArray = new VertexArray(mesh, RAIL_MAPPING);
				return new UploadedPage(page, mesh, new OptimizedModel(Collections.singletonList(vertexArray)));
			} catch (RuntimeException | Error exception) {
				if (vertexArray != null) {
					vertexArray.close();
				}
				mesh.close();
				throw exception;
			}
		}

		private boolean isVisible() {
			return isPageVisible(minX, minY, minZ, maxX, maxY, maxZ, cameraX, cameraY, cameraZ, renderDistance, yawCos, yawSin, pitchCos, pitchSin);
		}

		@Override
		public void close() {
			if (!closed) {
				closed = true;
				model.close();
				mesh.close();
			}
		}
	}

	private static final class CacheEntry implements AutoCloseable {

		private Rail rail;
		private final long fingerprint;
		private final PreparedRail preparedRail;
		private final ObjectArrayList<UploadedPage> pages;
		private final long estimatedBytes;

		private CacheEntry(Rail rail, long fingerprint, PreparedRail preparedRail) {
			this.rail = rail;
			this.fingerprint = fingerprint;
			this.preparedRail = preparedRail;
			this.pages = new ObjectArrayList<>(preparedRail.pages.size());
			for (int i = 0; i < preparedRail.pages.size(); i++) {
				pages.add(null);
			}
			this.estimatedBytes = preparedRail.estimatedBytes;
		}

		private boolean hasMissingVisiblePages() {
			for (int i = 0; i < pages.size(); i++) {
				if (pages.get(i) == null && preparedRail.pages.get(i).isVisible()) {
					return true;
				}
			}
			return false;
		}

		private int uploadMissingVisiblePages(ClientWorld clientWorld, int maximumPages, long deadlineNanos) {
			return uploadMissingPages(clientWorld, maximumPages, deadlineNanos, true, false);
		}

		private boolean hasMissingPages() {
			for (final UploadedPage page : pages) {
				if (page == null) {
					return true;
				}
			}
			return false;
		}

		private boolean hasMissingLoadedPages(ClientWorld clientWorld) {
			for (int i = 0; i < pages.size(); i++) {
				if (pages.get(i) == null && preparedRail.pages.get(i).areChunksLoaded(clientWorld)) {
					return true;
				}
			}
			return false;
		}

		private int uploadMissingPages(ClientWorld clientWorld, int maximumPages, long deadlineNanos, boolean visibleOnly, boolean loadedOnly) {
			final int[] uploadedCount = {0};
			final Boolean uploaded = CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.upload(() -> {
				for (int i = 0; i < pages.size(); i++) {
					if (pages.get(i) == null && (!visibleOnly || preparedRail.pages.get(i).isVisible()) && (!loadedOnly || preparedRail.pages.get(i).areChunksLoaded(clientWorld))) {
						if (uploadedCount[0] >= maximumPages || (uploadedCount[0] > 0 && System.nanoTime() >= deadlineNanos)) {
							break;
						}
						pages.set(i, UploadedPage.upload(clientWorld, preparedRail.pages.get(i)));
						uploadedCount[0]++;
					}
				}
				return true;
			});
			return uploaded == null ? 0 : uploadedCount[0];
		}

		private void invalidateBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			for (int i = 0; i < pages.size(); i++) {
				final UploadedPage uploadedPage = pages.get(i);
				if (uploadedPage != null && preparedRail.pages.get(i).intersects(minX, minY, minZ, maxX, maxY, maxZ)) {
					uploadedPage.close();
					pages.set(i, null);
				}
			}
		}

		@Override
		public void close() {
			for (final UploadedPage page : pages) {
				if (page != null) {
					page.close();
				}
			}
		}
	}

	private DefaultRailMeshCache() {
	}
}
