package org.mtr.mod.render;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.ArrivalsCacheClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.entity.EntityRendering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderTrains extends EntityRenderer<EntityRendering> implements IGui {

	private static long lastRenderedMillis;

	public static final int PLAYER_RENDER_OFFSET = 1000;
	public static final WorkerThread WORKER_THREAD = new WorkerThread();

	private static final int FLASHING_INTERVAL = 1000;
	private static final int TOTAL_RENDER_STAGES = 2;
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<BiConsumer<GraphicsHolder, Vector3d>>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<BiConsumer<GraphicsHolder, Vector3d>>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<BiConsumer<GraphicsHolder, Vector3d>>>> rendersList = new ObjectArrayList<>(renderStageCount);
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<BiConsumer<GraphicsHolder, Vector3d>>>> currentRendersList = new ObjectArrayList<>(renderStageCount);

			for (int j = 0; j < renderStageCount; j++) {
				rendersList.add(j, new Object2ObjectArrayMap<>());
				currentRendersList.add(j, new Object2ObjectArrayMap<>());
			}

			RENDERS.add(i, rendersList);
			CURRENT_RENDERS.add(i, currentRendersList);
		}
	}

	public RenderTrains(Argument argument) {
		super(argument);
	}

	@Override
	public void render(EntityRendering entityRendering, float yaw, float tickDelta, GraphicsHolder graphicsHolder, int i) {
		render(graphicsHolder, entityRendering.getCameraPosVec2(MinecraftClient.getInstance().getTickDelta()));
	}

	@Override
	public boolean shouldRender2(EntityRendering entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Nonnull
	@Override
	public Identifier getTexture2(EntityRendering entityRendering) {
		return new Identifier("");
	}

	public static void render(GraphicsHolder graphicsHolder, Vector3d offset) {
		final long millisElapsed;
		if (OptimizedRenderer.renderingShadows()) {
			if (Config.getClient().getDisableShadowsForShaders()) {
				return;
			}
			millisElapsed = 0;
		} else {
			millisElapsed = getMillisElapsed();
			MinecraftClientData.getInstance().vehicles.forEach(vehicle -> vehicle.simulate(millisElapsed));
			MinecraftClientData.getInstance().lifts.forEach(lift -> lift.tick(millisElapsed));
			lastRenderedMillis = InitClient.getGameMillis();
			WORKER_THREAD.start();
			DynamicTextureCache.instance.tick();
			// Tick the riding cool down (dismount player if they are no longer riding a vehicle) and store the player offset cache
			VehicleRidingMovement.tick();
			ArrivalsCacheClient.INSTANCE.tick();
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final Vector3d cameraShakeOffset = clientPlayerEntity.getPos().subtract(offset);
		RenderVehicles.render(millisElapsed, cameraShakeOffset);
		RenderLifts.render(millisElapsed, cameraShakeOffset);
		RenderRails.render();

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
				CURRENT_RENDERS.get(i).get(j).clear();
				CURRENT_RENDERS.get(i).get(j).putAll(RENDERS.get(i).get(j));
				RENDERS.get(i).get(j).clear();
			}
		}

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
				final QueuedRenderLayer queuedRenderLayer = QueuedRenderLayer.values()[j];
				CURRENT_RENDERS.get(i).get(j).forEach((key, value) -> {
					final RenderLayer renderLayer;
					switch (queuedRenderLayer) {
						case LIGHT:
							renderLayer = MoreRenderLayers.getLight(key, false);
							break;
						case LIGHT_TRANSLUCENT:
							renderLayer = MoreRenderLayers.getLight(key, true);
							break;
						case INTERIOR:
							renderLayer = MoreRenderLayers.getInterior(key);
							break;
						case INTERIOR_TRANSLUCENT:
							renderLayer = MoreRenderLayers.getInteriorTranslucent(key);
							break;
						case EXTERIOR:
							renderLayer = MoreRenderLayers.getExterior(key);
							break;
						case EXTERIOR_TRANSLUCENT:
							renderLayer = MoreRenderLayers.getExteriorTranslucent(key);
							break;
						case LINES:
							renderLayer = RenderLayer.getLines();
							break;
						default:
							renderLayer = null;
							break;
					}
					if (renderLayer != null) {
						graphicsHolder.createVertexConsumer(renderLayer);
					}
					value.forEach(renderer -> renderer.accept(graphicsHolder, offset));
				});
			}
		}

		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.render(!Config.getClient().getHideTranslucentParts());
	}

	public static boolean shouldNotRender(BlockPos pos, @Nullable Direction facing) {
		final Entity camera = MinecraftClient.getInstance().getCameraEntityMapped();
		return shouldNotRender(camera == null ? null : camera.getPos(), pos, facing);
	}

	public static void scheduleRender(@Nullable Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, BiConsumer<GraphicsHolder, Vector3d> callback) {
		if (identifier != null) {
			RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal()).computeIfAbsent(identifier, key -> new ObjectArrayList<>()).add(callback);
		}
	}

	public static void scheduleRender(QueuedRenderLayer queuedRenderLayer, BiConsumer<GraphicsHolder, Vector3d> callback) {
		scheduleRender(new Identifier(""), false, queuedRenderLayer, callback);
	}

	public static void cancelRender(Identifier identifier) {
		RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
	}

	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
	}

	public static int getFlashingLight() {
		final int light = (int) Math.round((Math.sin(Math.PI * 2 * (System.currentTimeMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2 * 0xF);
		return LightmapTextureManager.pack(light, light);
	}

	private static long getMillisElapsed() {
		final long millisElapsed = InitClient.getGameMillis() - lastRenderedMillis;
		final long gameMillisElapsed = (long) (MinecraftClient.getInstance().getLastFrameDuration() * 50);
		return Math.abs(gameMillisElapsed - millisElapsed) < 50 ? gameMillisElapsed : millisElapsed;
	}

	private static double maxDistanceXZ(Vector3d pos1, BlockPos pos2) {
		return Math.max(Math.abs(pos1.getXMapped() - pos2.getX()), Math.abs(pos1.getZMapped() - pos2.getZ()));
	}

	private static boolean shouldNotRender(@Nullable Vector3d cameraPos, BlockPos pos, @Nullable Direction facing) {
		final boolean playerFacingAway;
		if (cameraPos == null || facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Axis.X) {
				final double playerXOffset = cameraPos.getXMapped() - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getOffsetX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = cameraPos.getZMapped() - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getOffsetZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > MinecraftClientHelper.getRenderDistance();
	}

	public enum QueuedRenderLayer {LIGHT, INTERIOR, EXTERIOR, LIGHT_TRANSLUCENT, INTERIOR_TRANSLUCENT, EXTERIOR_TRANSLUCENT, LINES, TEXT}
}
