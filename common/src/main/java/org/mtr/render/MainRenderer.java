package org.mtr.render;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
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
import org.jspecify.annotations.Nullable;
import org.mtr.MTRClient;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.MinecraftClientData;
import org.mtr.client.VehicleRidingMovement;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import org.mtr.model.NewOptimizedModel;
import org.mtr.registry.KeyBindings;
import org.mtr.resource.RenderStage;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Top-level coordinator for every per-frame rendering operation the mod performs.
 *
 * <p>Three responsibilities live here:</p>
 * <ol>
 *   <li><b>Tick management</b> — {@link #render(MatrixStack, VertexConsumerProvider, Vec3d)}
 *       is the single entry point the loader (Fabric / NeoForge) wires into the world-render
 *       pipeline. It advances {@link #timerMillis}, ticks {@link DynamicTextureCache},
 *       riding state, and the arrivals cache, then dispatches into the per-domain
 *       renderers ({@link RenderVehicles}, {@link RenderLifts}, {@link RenderRails}).</li>
 *   <li><b>Batched draw scheduling</b> — code anywhere in the mod can call
 *       {@link #scheduleRender(Identifier, boolean, QueuedRenderLayer, ScheduledRender)} or
 *       {@link #renderModel(Object2ObjectOpenHashMap, StoredMatrixTransformations, int)} to
 *       enqueue draw work for the current frame. The drains at the end of
 *       {@link #render(MatrixStack, VertexConsumerProvider, Vec3d)} sort and dispatch by
 *       {@link QueuedRenderLayer} and {@link RenderStage}.</li>
 *   <li><b>Shared frame utilities</b> — {@link #getFlashingLight()},
 *       {@link #getFlashingColor(Color, int)}, and {@link #getInterchangeRouteNames(Consumer)}
 *       hand out values derived from the current frame timer / world state, so renderers
 *       don't reinvent them.</li>
 * </ol>
 *
 * <p>The class also exposes the shared {@link #WORKER_THREAD} used for off-main-thread work
 * (occlusion culling, dynamic-texture generation, model parsing). See
 * {@link WorkerThread} for the queueing contract.</p>
 *
 * <p><b>Threading:</b> every method on this class except {@code WORKER_THREAD}'s public
 * surface must be called on the render thread. The internal {@code RENDERS} /
 * {@code CURRENT_RENDERS} / {@code MODEL_RENDERS*} maps are not thread-safe.</p>
 *
 * @see WorkerThread
 * @see QueuedRenderLayer
 * @see NewOptimizedModel
 */
public class MainRenderer {

	/**
	 * Get a continuously ticking timer for rendering, suitable for animations.
	 * Returns a value in milliseconds representing the time elapsed, incremented when
	 * {@link MainRenderer#render(MatrixStack, VertexConsumerProvider, Vec3d)} is invoked.
	 */
	@Getter
	private static long timerMillis;
	private static long lastRenderedMillis;

	/**
	 * Shared off-main-thread worker for occlusion culling and dynamic texture generation.
	 * Submit work via {@link WorkerThread#scheduleVehicles(Consumer)} and friends, or by
	 * posting directly to {@link WorkerThread#worker} for one-shot tasks like model parsing.
	 */
	public static final WorkerThread WORKER_THREAD = new WorkerThread();

	private static final int FLASHING_INTERVAL = 1000;
	private static final int TOTAL_RENDER_STAGES = 2;
	/**
	 * Cached enum-values arrays. {@link Enum#values()} allocates a fresh defensive copy on
	 * every call; the render loop walks these many times per frame so we hold a single
	 * shared snapshot. See {@code docs/PERFORMANCE.md} §3.3.
	 */
	private static final QueuedRenderLayer[] QUEUED_RENDER_LAYERS = QueuedRenderLayer.values();
	private static final RenderStage[] RENDER_STAGES = RenderStage.values();
	/** Reusable identifier for {@link #scheduleRender(QueuedRenderLayer, ScheduledRender)} — see {@code docs/PERFORMANCE.md} §4.3. */
	private static final Identifier EMPTY_IDENTIFIER = Identifier.of("");
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArrayList<ScheduledRender>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final Object2ObjectOpenHashMap<NewOptimizedModel, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<ObjectIntImmutablePair<StoredMatrixTransformations>>>> MODEL_RENDERS = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<NewOptimizedModel, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<ObjectIntImmutablePair<StoredMatrixTransformations>>>> MODEL_RENDERS_TRANSLUCENT = new Object2ObjectOpenHashMap<>();

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QUEUED_RENDER_LAYERS.length;
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

	/**
	 * Per-frame entry point invoked by the loader's world-render hook.
	 *
	 * <p>Advances {@link #timerMillis} by the elapsed wall-clock time, ticks the simulation
	 * mirror (vehicles, lifts, riding state, arrivals cache, dynamic texture cache), then
	 * dispatches into {@link RenderVehicles}, {@link RenderLifts} and {@link RenderRails}.
	 * Finally drains the {@code MODEL_RENDERS*} maps and the scheduled-render queues into
	 * actual draw calls grouped by render layer.</p>
	 *
	 * @param matrixStack            the current world matrix stack (caller-owned, do not retain)
	 * @param vertexConsumerProvider the buffer source for this frame
	 * @param offset                 the camera-relative offset for the world frame
	 */
	public static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Vec3d offset) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final long millisElapsed = getMillisElapsed();
		timerMillis += millisElapsed;

		MODEL_RENDERS.clear();
		MODEL_RENDERS_TRANSLUCENT.clear();
		MinecraftClientData.getInstance().blockedRailIds.clear();
		MinecraftClientData.getInstance().vehicles.forEach(vehicle -> vehicle.simulate(millisElapsed));
		MinecraftClientData.getInstance().lifts.forEach(lift -> {
			lift.tick(millisElapsed);
			if (VehicleRidingMovement.isRiding(lift.getId()) && VehicleRidingMovement.showShiftProgressBar()) {
				clientPlayerEntity.sendMessage(TranslationProvider.GUI_MTR_PRESS_TO_SELECT_FLOOR.getText(KeyBindings.LIFT_MENU.getBoundKeyLocalizedText().getString()), true);
			}
		});
		lastRenderedMillis = MTRClient.getGameMillis();
		WORKER_THREAD.start();
		DynamicTextureCache.instance.tick();
		// Tick the riding cool down (dismount player if they are no longer riding a vehicle) and store the player offset cache
		VehicleRidingMovement.tick();
		ArrivalsCacheClient.INSTANCE.tick();

		final Vec3d cameraShakeOffset = clientPlayerEntity.getPos().subtract(offset);
		RenderVehicles.render(millisElapsed, cameraShakeOffset);
		RenderLifts.render(millisElapsed, cameraShakeOffset);
		RenderRails.render(matrixStack, vertexConsumerProvider, offset);

		renderModel(matrixStack, MODEL_RENDERS, offset);
		renderModel(matrixStack, MODEL_RENDERS_TRANSLUCENT, offset);

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QUEUED_RENDER_LAYERS.length; j++) {
				CURRENT_RENDERS.get(i).get(j).clear();
				CURRENT_RENDERS.get(i).get(j).putAll(RENDERS.get(i).get(j));
				RENDERS.get(i).get(j).clear();
			}
		}

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QUEUED_RENDER_LAYERS.length; j++) {
				final QueuedRenderLayer queuedRenderLayer = QUEUED_RENDER_LAYERS[j];
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
					if (renderLayer != null) {
						value.forEach(renderer -> renderer.accept(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), offset));
					}
				});
			}
		}
	}

	/**
	 * Enqueue a set of pre-built {@link NewOptimizedModel} instances to be drawn this frame
	 * at the given transformation and light level. Models are grouped by render stage —
	 * translucent stages drain after opaque stages so transparency sorts correctly.
	 *
	 * @param models                       the pre-built optimised models keyed by render stage
	 * @param storedMatrixTransformations  the transform applied at draw time
	 * @param light                        packed lightmap value (block + sky in the low bits)
	 */
	public static void renderModel(Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> models, StoredMatrixTransformations storedMatrixTransformations, int light) {
		models.forEach((renderStage, newOptimizedModels) -> newOptimizedModels.forEach(newOptimizedModel -> (renderStage.isTranslucent ? MODEL_RENDERS_TRANSLUCENT : MODEL_RENDERS)
			.computeIfAbsent(newOptimizedModel, key -> new Object2ObjectOpenHashMap<>())
			.computeIfAbsent(renderStage, key -> new ObjectArrayList<>())
			.add(new ObjectIntImmutablePair<>(storedMatrixTransformations, light))
		));
	}

	/**
	 * Schedule a vertex-consumer-style draw to run later this frame against the given
	 * {@link QueuedRenderLayer}. Use {@code priority = true} when the draw should land in
	 * front of normal-priority work (e.g. UI overlays drawn over world geometry).
	 *
	 * @param identifier        the texture identifier driving the render layer, or {@code null} to skip
	 * @param priority          {@code true} for the priority bucket, {@code false} for normal
	 * @param queuedRenderLayer the render-layer family to dispatch into
	 * @param scheduledRender   the draw callback invoked during the drain pass
	 */
	public static void scheduleRender(@Nullable Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, ScheduledRender scheduledRender) {
		if (identifier != null) {
			RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal()).computeIfAbsent(identifier, key -> new ObjectArrayList<>()).add(scheduledRender);
		}
	}

	/**
	 * Convenience overload of
	 * {@link #scheduleRender(Identifier, boolean, QueuedRenderLayer, ScheduledRender)}
	 * for draws that aren't keyed to a specific texture (e.g. line / debug renders).
	 */
	public static void scheduleRender(QueuedRenderLayer queuedRenderLayer, ScheduledRender scheduledRender) {
		scheduleRender(EMPTY_IDENTIFIER, false, queuedRenderLayer, scheduledRender);
	}

	/**
	 * Drop every queued draw associated with the given identifier from both the pending
	 * and current frame buckets. Called when a dynamic texture is evicted so stale draws
	 * don't reference a disposed {@link net.minecraft.client.texture.NativeImageBackedTexture}.
	 */
	public static void cancelRender(Identifier identifier) {
		RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
		CURRENT_RENDERS.forEach(renderForPriority -> renderForPriority.forEach(renderForPriorityAndQueuedRenderLayer -> renderForPriorityAndQueuedRenderLayer.remove(identifier)));
	}

	/**
	 * Flatten interchange data for a station into the comma-joined display string used by
	 * "Interchange:" signs and PIDS rows.
	 *
	 * @param getInterchanges visitor producing {@code (connectingStationName, colors)} pairs
	 * @return the formatted interchange list, ready to render
	 */
	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
	}

	/**
	 * @return a packed lightmap value that pulses sinusoidally between 0 and 0xF on a
	 *         {@link #FLASHING_INTERVAL}-millisecond period. Suitable for signal-light and
	 *         door-warning glows.
	 */
	public static int getFlashingLight() {
		final int light = (int) Math.round(((Math.sin(Math.PI * 2 * (getTimerMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2) * 0xF);
		return LightmapTextureManager.pack(light, light);
	}

	public static Color getFlashingColor(Color color, int multiplier) {
		final double flashingProgress = ((Math.sin(Math.PI * 2 * (getTimerMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2);
		return new Color(
			(int) (color.getRed() * Math.min(1, flashingProgress * multiplier)),
			(int) (color.getGreen() * Math.min(1, flashingProgress * multiplier)),
			(int) (color.getBlue() * Math.min(1, flashingProgress * multiplier))
		);
	}

	public static int getFlashingColor(int color, int multiplier) {
		return getFlashingColor(new Color(color), multiplier).getRGB();
	}

	private static void renderModel(MatrixStack matrixStack, Object2ObjectOpenHashMap<NewOptimizedModel, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<ObjectIntImmutablePair<StoredMatrixTransformations>>>> modelRenders, Vec3d offset) {
		modelRenders.forEach((newOptimizedModel, modelsForRenderStage) -> modelsForRenderStage.forEach((renderStage, renderDetails) -> {
			final Identifier texture = newOptimizedModel.texture;
			final RenderLayer renderLayer = switch (renderStage) {
				case LIGHT -> MoreRenderLayers.getLight(texture, false);
				case ALWAYS_ON_LIGHT -> MoreRenderLayers.getLight(texture, true);
				case INTERIOR -> MoreRenderLayers.getInterior(texture);
				case INTERIOR_TRANSLUCENT -> MoreRenderLayers.getInteriorTranslucent(texture);
				case EXTERIOR -> MoreRenderLayers.getExterior(texture);
			};
			renderLayer.startDrawing();
			newOptimizedModel.begin(RenderSystem.getShader());
			renderDetails.forEach(renderDetailsEntry -> {
				renderDetailsEntry.left().transform(matrixStack, offset);
				newOptimizedModel.render(matrixStack.peek().getPositionMatrix(), renderStage.isFullBrightness ? 1 : (float) renderDetailsEntry.rightInt() / 0xF, RenderSystem.getShader());
				matrixStack.pop();
			});
			renderLayer.endDrawing();
		}));
	}

	private static long getMillisElapsed() {
		final long millisElapsed = MTRClient.getGameMillis() - lastRenderedMillis;
		final long gameMillisElapsed = (long) (MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration() * 50);
		return Math.abs(gameMillisElapsed - millisElapsed) < 50 ? gameMillisElapsed : millisElapsed;
	}

	@FunctionalInterface
	public interface ScheduledRender {
		/**
		 * Run the deferred draw against the given buffer and offset.
		 *
		 * @param matrixStack    the frame's matrix stack, already positioned for world space
		 * @param vertexConsumer the buffer for the resolved {@code RenderLayer}
		 * @param offset         camera-relative world offset
		 */
		void accept(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d offset);
	}
}
