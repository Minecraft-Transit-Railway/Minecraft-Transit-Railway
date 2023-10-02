package org.mtr.mod.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.ResourcePackCreatorProperties;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;
	public static ResourcePackCreatorProperties creatorProperties = new ResourcePackCreatorProperties();

	private static long lastRenderedMillis;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	public static final ObjectAVLTreeSet<String> AVAILABLE_TEXTURES = new ObjectAVLTreeSet<>();
	public static final ObjectAVLTreeSet<String> UNAVAILABLE_TEXTURES = new ObjectAVLTreeSet<>();

	public static final int DETAIL_RADIUS = 32;
	public static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	private static final int TOTAL_RENDER_STAGES = 2;
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>>> RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);
	private static final ObjectArrayList<ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>>> CURRENT_RENDERS = new ObjectArrayList<>(TOTAL_RENDER_STAGES);

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>> rendersList = new ObjectArrayList<>(renderStageCount);
			final ObjectArrayList<Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>>> currentRendersList = new ObjectArrayList<>(renderStageCount);

			for (int j = 0; j < renderStageCount; j++) {
				rendersList.add(j, new Object2ObjectArrayMap<>());
				currentRendersList.add(j, new Object2ObjectArrayMap<>());
			}

			RENDERS.add(i, rendersList);
			CURRENT_RENDERS.add(i, currentRendersList);
		}
	}

	public static void render(GraphicsHolder graphicsHolder) {
		ClientData.instance.vehicles.forEach(vehicle -> vehicle.simulate(InitClient.getGameMillis() - lastRenderedMillis));
		lastRenderedMillis = InitClient.getGameMillis();
		// TODO

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		RenderVehicles.render(graphicsHolder);
		RenderRails.render(graphicsHolder);

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
						default:
							renderLayer = null;
							break;
					}
					if (renderLayer != null) {
						graphicsHolder.createVertexConsumer(renderLayer);
					}
					value.forEach(renderer -> renderer.accept(graphicsHolder));
				});
			}
		}
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, @Nullable Direction facing) {
		final Entity camera = MinecraftClient.getInstance().getCameraEntityMapped();
		return shouldNotRender(camera == null ? null : camera.getPos(), pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	public static void scheduleRender(Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, Consumer<GraphicsHolder> callback) {
		final Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>> map = RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal());
		if (!map.containsKey(identifier)) {
			map.put(identifier, new ObjectArraySet<>());
		}
		map.get(identifier).add(callback);
	}

	public static void scheduleRender(Consumer<GraphicsHolder> callback) {
		scheduleRender(new Identifier(""), false, QueuedRenderLayer.TEXT, callback);
	}

	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
	}

	public static Vector3d getPlayerOffset() {
		return MinecraftClient.getInstance().getGameRendererMapped().getCamera().getPos();
	}

	private static double maxDistanceXZ(Vector3d pos1, BlockPos pos2) {
		return Math.max(Math.abs(pos1.getXMapped() - pos2.getX()), Math.abs(pos1.getZMapped() - pos2.getZ()));
	}

	private static boolean shouldNotRender(@Nullable Vector3d cameraPos, BlockPos pos, int maxDistance, @Nullable Direction facing) {
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
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > maxDistance;
	}

	public enum QueuedRenderLayer {LIGHT, INTERIOR, EXTERIOR, LIGHT_TRANSLUCENT, INTERIOR_TRANSLUCENT, EXTERIOR_TRANSLUCENT, TEXT}
}
