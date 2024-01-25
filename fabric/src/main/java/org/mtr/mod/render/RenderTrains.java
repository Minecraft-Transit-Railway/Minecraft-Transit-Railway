package org.mtr.mod.render;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.Config;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.data.IGui;
import org.mtr.mod.entity.EntityRendering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderTrains extends EntityRenderer<EntityRendering> implements IGui {

	private static long lastRenderedMillis;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	private static final int TOTAL_RENDER_STAGES = 2;
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<BiConsumer<GraphicsHolder, Vector3d>>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<BiConsumer<GraphicsHolder, Vector3d>>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<BiConsumer<GraphicsHolder, Vector3d>>>> rendersList = new ObjectArrayList<>(renderStageCount);
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<BiConsumer<GraphicsHolder, Vector3d>>>> currentRendersList = new ObjectArrayList<>(renderStageCount);

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
		InitClient.incrementGameMillis();
		render(graphicsHolder, entityRendering.offset);
	}

	@Nonnull
	@Override
	public Identifier getTexture2(EntityRendering entityRendering) {
		return new Identifier("");
	}

	public static void render(GraphicsHolder graphicsHolder, Vector3d offset) {
		final long millisElapsed = InitClient.getGameMillis() - lastRenderedMillis;
		ClientData.getInstance().vehicles.forEach(vehicle -> vehicle.simulate(millisElapsed));
		ClientData.getInstance().lifts.forEach(lift -> lift.tick(millisElapsed));
		lastRenderedMillis = InitClient.getGameMillis();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		// Tick the riding cool down (dismount player if they are no longer riding a vehicle) and store the player offset cache
		VehicleRidingMovement.tick();
		RenderVehicles.render(millisElapsed);
		RenderLifts.render(millisElapsed);
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

		CustomResourceLoader.OPTIMIZED_RENDERER.render(!Config.hideTranslucentParts());
	}

	public static boolean shouldNotRender(BlockPos pos, @Nullable Direction facing) {
		final Entity camera = MinecraftClient.getInstance().getCameraEntityMapped();
		return shouldNotRender(camera == null ? null : camera.getPos(), pos, facing);
	}

	public static void scheduleRender(@Nullable Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, BiConsumer<GraphicsHolder, Vector3d> callback) {
		if (identifier != null) {
			final Object2ObjectArrayMap<Identifier, ObjectArraySet<BiConsumer<GraphicsHolder, Vector3d>>> map = RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal());
			if (!map.containsKey(identifier)) {
				map.put(identifier, new ObjectArraySet<>());
			}
			map.get(identifier).add(callback);
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
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > MinecraftClientHelper.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1);
	}

	public enum QueuedRenderLayer {LIGHT, INTERIOR, EXTERIOR, LIGHT_TRANSLUCENT, INTERIOR_TRANSLUCENT, EXTERIOR_TRANSLUCENT, LINES, TEXT}
}
