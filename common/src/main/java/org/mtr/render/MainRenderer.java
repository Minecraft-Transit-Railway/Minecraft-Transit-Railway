package org.mtr.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTRClient;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.MinecraftClientData;
import org.mtr.client.VehicleRidingMovement;
import org.mtr.config.Config;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.data.IGui;
import org.mtr.model.OptimizedRenderer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MainRenderer implements IGui {

	private static long lastRenderedMillis;

	public static final int PLAYER_RENDER_OFFSET = 1000;
	public static final WorkerThread WORKER_THREAD = new WorkerThread();

	private static final int FLASHING_INTERVAL = 1000;
	private static final int TOTAL_RENDER_STAGES = 2;
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>> rendersList = new ObjectArrayList<>(renderStageCount);
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>> currentRendersList = new ObjectArrayList<>(renderStageCount);

			for (int j = 0; j < renderStageCount; j++) {
				rendersList.add(j, new Object2ObjectArrayMap<>());
				currentRendersList.add(j, new Object2ObjectArrayMap<>());
			}

			RENDERS.add(i, rendersList);
			CURRENT_RENDERS.add(i, currentRendersList);
		}
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d offset) {
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
			lastRenderedMillis = MTRClient.getGameMillis();
			WORKER_THREAD.start();
			DynamicTextureCache.instance.tick();
			// Tick the riding cool down (dismount player if they are no longer riding a vehicle) and store the player offset cache
			VehicleRidingMovement.tick();
			ArrivalsCacheClient.INSTANCE.tick();
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final Vec3d cameraShakeOffset = clientPlayerEntity.getPos().subtract(offset);
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
					final RenderLayer renderLayer = switch (queuedRenderLayer) {
						case LIGHT -> MoreRenderLayers.getLight(key, false);
						case LIGHT_TRANSLUCENT -> MoreRenderLayers.getLight(key, true);
						case LIGHT_2 -> MoreRenderLayers.getLight2(key);
						case INTERIOR -> MoreRenderLayers.getInterior(key);
						case INTERIOR_TRANSLUCENT -> MoreRenderLayers.getInteriorTranslucent(key);
						case EXTERIOR -> MoreRenderLayers.getExterior(key);
						case EXTERIOR_TRANSLUCENT -> MoreRenderLayers.getExteriorTranslucent(key);
						case LINES -> RenderLayer.getLines();
						default -> null;
					};
					value.forEach(renderer -> renderer.accept(matrices, renderLayer == null ? null : vertexConsumers.getBuffer(renderLayer), offset));
				});
			}
		}

		CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.render(!Config.getClient().getHideTranslucentParts());
	}

	public static void scheduleRender(@Nullable Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, ScheduledRender scheduledRender) {
		if (identifier != null) {
			RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal()).computeIfAbsent(identifier, key -> new ObjectArrayList<>()).add(scheduledRender);
		}
	}

	public static void scheduleRender(QueuedRenderLayer queuedRenderLayer, ScheduledRender scheduledRender) {
		scheduleRender(Identifier.of(""), false, queuedRenderLayer, scheduledRender);
	}

	public static void cancelRender(Identifier identifier) {
		RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
		CURRENT_RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
	}

	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
	}

	public static int getFlashingLight() {
		final int light = (int) Math.round(((Math.sin(Math.PI * 2 * (System.currentTimeMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2) * 0xF);
		return LightmapTextureManager.pack(light, light);
	}

	public static int getFlashingColor(int color) {
		final double flashingProgress = ((Math.sin(Math.PI * 2 * (System.currentTimeMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2);
		final Color oldColor = new Color(color);
		return new Color(
				(int) (oldColor.getRed() * flashingProgress),
				(int) (oldColor.getGreen() * flashingProgress),
				(int) (oldColor.getBlue() * flashingProgress)
		).getRGB();
	}

	private static long getMillisElapsed() {
		final long millisElapsed = MTRClient.getGameMillis() - lastRenderedMillis;
		final long gameMillisElapsed = (long) (MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration() * 50);
		return Math.abs(gameMillisElapsed - millisElapsed) < 50 ? gameMillisElapsed : millisElapsed;
	}

	@FunctionalInterface
	public interface ScheduledRender {
		void accept(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d offset);
	}
}
