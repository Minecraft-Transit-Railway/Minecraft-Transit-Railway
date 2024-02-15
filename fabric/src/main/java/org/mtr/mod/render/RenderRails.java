package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.block.BlockSignalLightBase;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.Config;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.RailType;
import org.mtr.mod.item.ItemNodeModifierBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Function;

public class RenderRails implements IGui {

	private static final OcclusionCullingThread OCCLUSION_CULLING_THREAD = new OcclusionCullingThread();

	public static void render() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		OCCLUSION_CULLING_THREAD.start();
		final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasks = new ObjectArrayList<>();
		final Vector3d cameraPosition = minecraftClient.getGameRendererMapped().getCamera().getPos();
		final Vec3d camera = new Vec3d(cameraPosition.getXMapped(), cameraPosition.getYMapped(), cameraPosition.getZMapped());
		final boolean renderColors = isHoldingRailRelated(clientPlayerEntity);

		MinecraftClientData.getInstance().railWrapperList.values().forEach(railWrapper -> {
			final Rail rail = railWrapper.rail;

			cullingTasks.add(occlusionCullingInstance -> {
				final boolean shouldRender = occlusionCullingInstance.isAABBVisible(railWrapper.startVector, railWrapper.endVector, camera);
				return () -> railWrapper.shouldRender = shouldRender;
			});

			if (railWrapper.shouldRender) {
				switch (rail.getTransportMode()) {
					case TRAIN:
						renderRailStandard(clientWorld, rail, renderColors, 1);
						if (renderColors) {
							renderSignalsStandard(clientWorld, rail);
						}
						break;
					case BOAT:
						if (renderColors) {
							renderRailStandard(clientWorld, rail, true, 0.5F);
							renderSignalsStandard(clientWorld, rail);
						}
						break;
					case CABLE_CAR:
						if (rail.isPlatform() || rail.isSiding() || rail.getSpeedLimitKilometersPerHour(false) == RailType.CABLE_CAR_STATION.speedLimit || rail.getSpeedLimitKilometersPerHour(true) == RailType.CABLE_CAR_STATION.speedLimit) {
							renderRailStandard(clientWorld, rail, 0.25F + SMALL_OFFSET, renderColors, 0.25F, new Identifier(Init.MOD_ID, "textures/block/metal.png"), 0.25F, 0, 0.75F, 1);
						}
						if (renderColors && !rail.isPlatform() && !rail.isSiding()) {
							renderRailStandard(clientWorld, rail, 0.5F + SMALL_OFFSET, true, 1, null, 0, 0.75F, 1, 0.25F);
						}

						RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> graphicsHolder.drawLineInWorld(
								(float) (x1 - offset.getXMapped()),
								(float) (y1 - offset.getYMapped() + 0.5),
								(float) (z1 - offset.getZMapped()),
								(float) (x3 - offset.getXMapped()),
								(float) (y2 - offset.getYMapped() + 0.5),
								(float) (z3 - offset.getZMapped()),
								renderColors ? RailType.getRailColor(rail) : ARGB_BLACK
						), 0, 0));

						break;
					case AIRPLANE:
						if (renderColors) {
							renderRailStandard(clientWorld, rail, true, 1);
							renderSignalsStandard(clientWorld, rail);
						} else {
							renderRailStandard(clientWorld, rail, 0.0625F + SMALL_OFFSET, false, 0.25F, new Identifier("textures/block/iron_block.png"), 0.25F, 0, 0.75F, 1);
						}
						break;
				}
			}
		});

		OCCLUSION_CULLING_THREAD.schedule(occlusionCullingInstance -> {
			final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
			cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
			minecraftClient.execute(() -> tasks.forEach(Runnable::run));
		});
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

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, boolean renderColors, float railWidth) {
		renderRailStandard(clientWorld, rail, 0.065625F, renderColors, railWidth, renderColors && RailType.getRailColor(rail) == RailType.QUARTZ.color ? new Identifier(Init.MOD_ID, "textures/block/rail_preview.png") : new Identifier("textures/block/rail.png"), -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, float yOffset, boolean renderColors, float railWidth, @Nullable Identifier texture, float u1, float v1, float u2, float v2) {
		if (renderColors || texture != null) {
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
				final long speedLimit1 = rail.getSpeedLimitKilometersPerHour(false);
				final long speedLimit2 = rail.getSpeedLimitKilometersPerHour(true);

				if (renderColors && (speedLimit1 == 0 || speedLimit2 == 0)) {
					RenderTrains.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/one_way_rail_arrow.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
						IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
						IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
					});
				}

				if (texture != null) {
					final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
					final int color = renderColors || !Config.hideSpecialRailColors() && (rail.isPlatform() || rail.isSiding()) ? RailType.getRailColor(rail) : ARGB_WHITE;
					final int light = renderColors ? MAX_LIGHT_GLOWING : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
					RenderTrains.scheduleRender(texture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
						IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
						IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					});
				}
			}, -railWidth, railWidth);
		}
	}

	private static void renderSignalsStandard(ClientWorld clientWorld, Rail rail) {
		final IntArrayList colors = new IntArrayList(rail.getSignalColors());
		Collections.sort(colors);
		final float width = 1F / 16;

		for (int i = 0; i < colors.size(); i++) {
			final int color = ARGB_BLACK | colors.getInt(i);
			final boolean shouldGlow = false; // TODO
			final float u1 = width * i + 1 - width * colors.size() / 2;
			final float u2 = u1 + width;

			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
				final int light = shouldGlow ? MAX_LIGHT_GLOWING : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
				RenderTrains.scheduleRender(shouldGlow ? new Identifier(Init.MOD_ID, "textures/block/white.png") : new Identifier("textures/block/white_wool.png"), false, shouldGlow ? RenderTrains.QueuedRenderLayer.LIGHT : RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1, z1, x2, y1 + SMALL_OFFSET, z2, x3, y2, z3, x4, y2 + SMALL_OFFSET, z4, offset, u1, 0, u2, 1, Direction.UP, color, light);
					IDrawing.drawTexture(graphicsHolder, x4, y2 + SMALL_OFFSET, z4, x3, y2, z3, x2, y1 + SMALL_OFFSET, z2, x1, y1, z1, offset, u1, 0, u2, 1, Direction.UP, color, light);
				});
			}, u1 - 1, u2 - 1);
		}
	}
}
