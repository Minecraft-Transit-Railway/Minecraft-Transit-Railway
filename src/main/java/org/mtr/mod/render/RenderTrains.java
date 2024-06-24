package org.mtr.mod.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.block.BlockSignalLightBase;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.client.ResourcePackCreatorProperties;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemNodeModifierBase;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderTrains implements IGui {

	public static int maxTrainRenderDistance;
	public static ResourcePackCreatorProperties creatorProperties = new ResourcePackCreatorProperties();

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
		// TODO
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, @Nullable Direction facing) {
		final Entity camera = MinecraftClient.getInstance().getCameraEntityMapped();
		return shouldNotRender(camera == null ? null : camera.getPos(), pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	public static boolean isHoldingRailRelated(ClientPlayerEntity clientPlayerEntity) {
		return PlayerHelper.isHolding(new PlayerEntity(clientPlayerEntity.data),
				item -> item.data instanceof ItemNodeModifierBase ||
						Block.getBlockFromItem(item).data instanceof BlockSignalLightBase ||
						Block.getBlockFromItem(item).data instanceof BlockNode ||
						Block.getBlockFromItem(item).data instanceof BlockSignalSemaphoreBase ||
						Block.getBlockFromItem(item).data instanceof BlockPlatform
		);
	}

	public static void scheduleRender(Identifier identifier, boolean priority, QueuedRenderLayer queuedRenderLayer, Consumer<GraphicsHolder> callback) {
		final Object2ObjectArrayMap<Identifier, ObjectArraySet<Consumer<GraphicsHolder>>> map = RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal());
		if (!map.containsKey(identifier)) {
			map.put(identifier, new ObjectArraySet<>());
		}
		map.get(identifier).add(callback);
	}

	public static String getInterchangeRouteNames(Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges) {
		final ObjectArrayList<String> interchangeRouteNames = new ObjectArrayList<>();
		getInterchanges.accept((connectingStationName, interchangeColorsForStationName) -> interchangeColorsForStationName.forEach((color, interchangeRouteNamesForColor) -> interchangeRouteNamesForColor.forEach(interchangeRouteNames::add)));
		return IGui.mergeStationsWithCommas(interchangeRouteNames);
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

	public enum QueuedRenderLayer {LIGHT, LIGHT_TRANSLUCENT, INTERIOR, EXTERIOR}
}
