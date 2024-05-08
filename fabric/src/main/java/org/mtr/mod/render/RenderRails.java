package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Rail;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.block.BlockSignalLightBase;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.client.Config;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.RailType;
import org.mtr.mod.item.ItemBlockClickingBase;
import org.mtr.mod.item.ItemNodeModifierBase;
import org.mtr.mod.item.ItemRailModifier;
import org.mtr.mod.item.ItemRailShapeModifier;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Function;

public class RenderRails implements IGui {

	private static final Identifier IRON_BLOCK_TEXTURE = new Identifier("textures/block/iron_block.png");
	private static final Identifier METAL_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/metal.png");
	private static final Identifier RAIL_PREVIEW_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/rail_preview.png");
	private static final Identifier RAIL_TEXTURE = new Identifier("textures/block/rail.png");
	private static final Identifier WHITE_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/white.png");
	private static final Identifier WOOL_TEXTURE = new Identifier("textures/block/white_wool.png");
	private static final Identifier ONE_WAY_RAIL_ARROW_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/one_way_rail_arrow.png");

	public static void render() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasks = new ObjectArrayList<>();
		final Vector3d cameraPosition = minecraftClient.getGameRendererMapped().getCamera().getPos();
		final Vec3d camera = new Vec3d(cameraPosition.getXMapped(), cameraPosition.getYMapped(), cameraPosition.getZMapped());
		final boolean holdingRailRelated = isHoldingRailRelated(clientPlayerEntity);

		final Rail hoverRail;
		if (PlayerHelper.isHolding(new PlayerEntity(clientPlayerEntity.data), item -> item.data instanceof ItemRailShapeModifier)) {
			hoverRail = MinecraftClientData.getInstance().getFacingRail(false);
		} else {
			hoverRail = null;
		}

		final ObjectArrayList<Rail> railsToRender = new ObjectArrayList<>();
		MinecraftClientData.getInstance().railWrapperList.values().forEach(railWrapper -> {
			cullingTasks.add(occlusionCullingInstance -> {
				final boolean shouldRender = occlusionCullingInstance.isAABBVisible(railWrapper.startVector, railWrapper.endVector, camera);
				return () -> railWrapper.shouldRender = shouldRender;
			});
			if (railWrapper.shouldRender) {
				railsToRender.add(railWrapper.getRail());
			}
		});

		// Ghost rail
		final ItemStack itemStack = getStackInHand();
		final Item item = itemStack.getItem();
		if (item.data instanceof ItemRailModifier) {
			final HitResult hitResult = minecraftClient.getCrosshairTargetMapped();
			if (hitResult != null) {
				final Vector3d hitPos = hitResult.getPos();
				final BlockPos posStart = Init.newBlockPos(hitPos.getXMapped(), hitPos.getYMapped(), hitPos.getZMapped());
				final CompoundTag compoundTag = itemStack.getOrCreateTag();

				if (compoundTag.contains(ItemBlockClickingBase.TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(ItemBlockClickingBase.TAG_POS));
					final BlockState blockStateEnd = clientWorld.getBlockState(posEnd);

					if (blockStateEnd.getBlock().data instanceof BlockNode) {
						final BlockState blockStateStart = clientWorld.getBlockState(posStart);
						final float angleEnd = BlockNode.getAngle(blockStateEnd);
						final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(
								Init.blockPosToPosition(posStart), blockStateStart.getBlock().data instanceof BlockNode ? BlockNode.getAngle(blockStateStart) : blockStateEnd.getBlock().data instanceof BlockNode.BlockContinuousMovementNode ? angleEnd : EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) + 90,
								Init.blockPosToPosition(posEnd), angleEnd
						);
						final Rail rail = ((ItemRailModifier) item.data).createRail(ItemNodeModifierBase.getTransportMode(compoundTag), blockStateStart, blockStateEnd, posStart, posEnd, angles.left(), angles.right());
						if (rail != null) {
							railsToRender.add(rail);
						}
					}
				}
			}
		}

		railsToRender.forEach(rail -> {
			final boolean renderColors = holdingRailRelated || hoverRail == rail;
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
						renderRailStandard(clientWorld, rail, 0.25F + SMALL_OFFSET, renderColors, 0.25F, METAL_TEXTURE, 0.25F, 0, 0.75F, 1);
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
					), 2, 0, 0));

					break;
				case AIRPLANE:
					if (renderColors) {
						renderRailStandard(clientWorld, rail, true, 1);
						renderSignalsStandard(clientWorld, rail);
					} else {
						renderRailStandard(clientWorld, rail, 0.0625F + SMALL_OFFSET, false, 0.25F, IRON_BLOCK_TEXTURE, 0.25F, 0, 0.75F, 1);
					}
					break;
			}
		});

		RenderTrains.WORKER_THREAD.scheduleRails(occlusionCullingInstance -> {
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
		renderRailStandard(clientWorld, rail, 0.065625F, renderColors, railWidth, renderColors && RailType.getRailColor(rail) == RailType.QUARTZ.color ? RAIL_PREVIEW_TEXTURE : RAIL_TEXTURE, -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, float yOffset, boolean renderColors, float railWidth, @Nullable Identifier texture, float u1, float v1, float u2, float v2) {
		final long speedLimit1 = rail.getSpeedLimitKilometersPerHour(false);
		final long speedLimit2 = rail.getSpeedLimitKilometersPerHour(true);
		final boolean useObj = RenderVehicles.useOptimizedRendering(); // TODO

		if (renderColors && (speedLimit1 == 0 || speedLimit2 == 0)) {
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				RenderTrains.scheduleRender(ONE_WAY_RAIL_ARROW_TEXTURE, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset + 0.125, z1, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x3, y2 + yOffset + 0.125, z3, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
					IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x1, y1 + yOffset + 0.125, z1, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, x3, y2 + yOffset + 0.125, z3, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
				});
			}, 1, -1, 1);
		}

		if (useObj) {
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
				final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));

				CustomResourceLoader.getRailById(rail.getTransportMode(), "default", optimizedModel -> {
					final double differenceX = x3 - x1;
					final double differenceZ = z3 - z1;
					final double yaw = Math.atan2(differenceZ, differenceX);
					final double pitch = Math.atan2(y2 - y1, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ));
					RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
						graphicsHolder.push();
						graphicsHolder.translate((x1 + x3) / 2 - offset.getXMapped(), (y1 + y2) / 2 - offset.getYMapped(), (z1 + z3) / 2 - offset.getZMapped());
						graphicsHolder.rotateYRadians((float) (Math.PI / 2 - yaw));
						graphicsHolder.rotateXRadians((float) -pitch);
						CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(optimizedModel, graphicsHolder, light);
						graphicsHolder.pop();
					});
				});
			}, 2, 0, 0);
		}

		final boolean isNonObjRendering = !useObj && texture != null;
		if (isNonObjRendering || renderColors) {
			final int color = renderColors || !Config.hideSpecialRailColors() && (rail.isPlatform() || rail.isSiding()) ? RailType.getRailColor(rail) : ARGB_WHITE;
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
				final int light = renderColors ? GraphicsHolder.getDefaultLight() : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));

				RenderTrains.scheduleRender(isNonObjRendering ? texture : IRON_BLOCK_TEXTURE, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
				});
			}, 2, -railWidth, railWidth);
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
				final int light = shouldGlow ? GraphicsHolder.getDefaultLight() : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
				RenderTrains.scheduleRender(shouldGlow ? WHITE_TEXTURE : WOOL_TEXTURE, false, shouldGlow ? RenderTrains.QueuedRenderLayer.LIGHT : RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1, z1, x2, y1 + SMALL_OFFSET, z2, x3, y2, z3, x4, y2 + SMALL_OFFSET, z4, offset, u1, 0, u2, 1, Direction.UP, color, light);
					IDrawing.drawTexture(graphicsHolder, x4, y2 + SMALL_OFFSET, z4, x3, y2, z3, x2, y1 + SMALL_OFFSET, z2, x1, y1, z1, offset, u1, 0, u2, 1, Direction.UP, color, light);
				});
			}, 1, u1 - 1, u2 - 1);
		}
	}

	private static ItemStack getStackInHand() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			try {
				return clientPlayerEntity.getStackInHand(clientPlayerEntity.getActiveHand());
			} catch (Exception ignored) {
			}
		}
		return ItemStack.getEmptyMapped();
	}
}
