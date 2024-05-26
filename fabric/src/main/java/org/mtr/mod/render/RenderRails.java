package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Rail;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.Items;
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
import org.mtr.mod.item.ItemBrush;
import org.mtr.mod.item.ItemNodeModifierBase;
import org.mtr.mod.item.ItemRailModifier;
import org.mtr.mod.model.ModelSmallCube;
import org.mtr.mod.packet.PacketUpdateLastRailStyles;
import org.mtr.mod.resource.RailResource;

import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class RenderRails implements IGui {

	private static final Identifier IRON_BLOCK_TEXTURE = new Identifier("textures/block/iron_block.png");
	private static final Identifier METAL_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/metal.png");
	private static final Identifier RAIL_PREVIEW_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/rail_preview.png");
	private static final Identifier RAIL_TEXTURE = new Identifier("textures/block/rail.png");
	private static final Identifier WHITE_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/white.png");
	private static final Identifier WOOL_TEXTURE = new Identifier("textures/block/white_wool.png");
	private static final Identifier ONE_WAY_RAIL_ARROW_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/one_way_rail_arrow.png");
	private static final int INVALID_NODE_CHECK_RADIUS = 16;
	private static final int FLASHING_INTERVAL = 1000;
	private static final ModelSmallCube MODEL_SMALL_CUBE = new ModelSmallCube(new Identifier(Init.MOD_ID, "textures/block/white.png"));

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

		// Finding visible rails
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

		// Ghost rails (when holding brush)
		final ObjectArraySet<Rail> hoverRails = new ObjectArraySet<>();
		if (clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final Rail rail = MinecraftClientData.getInstance().getFacingRail(false);
			if (rail != null) {
				if (clientPlayerEntity.isSneaking()) {
					if (PacketUpdateLastRailStyles.CLIENT_CACHE.canApplyStylesToRail(clientPlayerEntity.getUuid(), rail, false)) {
						final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUuid(), rail);
						hoverRails.add(newRail);
						railsToRender.remove(rail);
						railsToRender.add(newRail);
					}
				} else {
					hoverRails.add(rail);
				}
			}
		}

		// Ghost rail (when building rail)
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
						final Rail rail = ((ItemRailModifier) item.data).createRail(clientPlayerEntity.getUuid(), ItemNodeModifierBase.getTransportMode(compoundTag), blockStateStart, blockStateEnd, posStart, posEnd, angles.left(), angles.right());
						if (rail != null) {
							final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUuid(), rail);
							railsToRender.add(newRail);
							hoverRails.add(newRail);
						}
					}
				}
			}
		}

		railsToRender.forEach(rail -> {
			final RenderState renderState = holdingRailRelated ? hoverRails.contains(rail) ? RenderState.FLASHING : RenderState.COLORED : RenderState.NORMAL;
			switch (rail.getTransportMode()) {
				case TRAIN:
					renderRailStandard(clientWorld, rail, renderState, 1);
					if (renderState.hasColor) {
						renderRailOneWayArrows(rail, 0.25F);
						renderSignalsStandard(clientWorld, rail);
					}
					break;
				case BOAT:
					renderRailStandard(clientWorld, rail, renderState, 0.5F);
					if (renderState.hasColor) {
						renderRailOneWayArrows(rail, 0.25F);
						renderSignalsStandard(clientWorld, rail);
					}
					break;
				case CABLE_CAR:
					if (rail.isPlatform() || rail.isSiding() || rail.getSpeedLimitKilometersPerHour(false) == RailType.CABLE_CAR_STATION.speedLimit || rail.getSpeedLimitKilometersPerHour(true) == RailType.CABLE_CAR_STATION.speedLimit) {
						renderRailStandard(clientWorld, rail, 0.25F + SMALL_OFFSET, renderState, 0.25F, METAL_TEXTURE, 0.25F, 0, 0.75F, 1);
					}
					if (renderState.hasColor && !rail.isPlatform() && !rail.isSiding()) {
						renderRailOneWayArrows(rail, 0.5F + SMALL_OFFSET);
					}
					RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.LINES, (graphicsHolder, offset) -> rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> graphicsHolder.drawLineInWorld(
							(float) (x1 - offset.getXMapped()),
							(float) (y1 - offset.getYMapped() + 0.5),
							(float) (z1 - offset.getZMapped()),
							(float) (x3 - offset.getXMapped()),
							(float) (y2 - offset.getYMapped() + 0.5),
							(float) (z3 - offset.getZMapped()),
							holdingRailRelated ? RailType.getRailColor(rail) : ARGB_BLACK
					), 0.5, 0, 0));
					break;
				case AIRPLANE:
					renderRailStandard(clientWorld, rail, 0.0625F + SMALL_OFFSET, renderState, 0.25F, IRON_BLOCK_TEXTURE, 0.25F, 0, 0.75F, 1);
					if (renderState.hasColor) {
						renderRailOneWayArrows(rail, 0.25F);
						renderSignalsStandard(clientWorld, rail);
					}
					break;
			}
		});

		// Render nodes
		if (holdingRailRelated) {
			MinecraftClientData.getInstance().positionsToRail.keySet().forEach(position -> {
				final BlockPos blockPos = Init.positionToBlockPos(position);
				renderNode(clientWorld.getBlockState(blockPos), blockPos, () -> true, GraphicsHolder.getDefaultLight());
			});
		}

		// Render nodes with the connected block state but isn't actually connected
		for (int x = -INVALID_NODE_CHECK_RADIUS; x <= INVALID_NODE_CHECK_RADIUS; x++) {
			for (int y = -INVALID_NODE_CHECK_RADIUS; y <= INVALID_NODE_CHECK_RADIUS; y++) {
				for (int z = -INVALID_NODE_CHECK_RADIUS; z <= INVALID_NODE_CHECK_RADIUS; z++) {
					final BlockPos blockPos = clientPlayerEntity.getBlockPos().add(x, y, z);
					final BlockState blockState = clientWorld.getBlockState(blockPos);
					renderNode(blockState, blockPos, () -> blockState.get(new Property<>(BlockNode.IS_CONNECTED.data)) && !MinecraftClientData.getInstance().positionsToRail.containsKey(Init.blockPosToPosition(blockPos)), getFlashingLight());
				}
			}
		}

		// Raytracing
		RenderTrains.WORKER_THREAD.scheduleRails(occlusionCullingInstance -> {
			final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
			cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
			minecraftClient.execute(() -> tasks.forEach(Runnable::run));
		});
	}

	public static boolean isHoldingRailRelated(ClientPlayerEntity clientPlayerEntity) {
		return PlayerHelper.isHolding(new PlayerEntity(clientPlayerEntity.data),
				item -> item.data instanceof ItemNodeModifierBase || item.data instanceof ItemBrush ||
						Block.getBlockFromItem(item).data instanceof BlockSignalLightBase ||
						Block.getBlockFromItem(item).data instanceof BlockNode ||
						Block.getBlockFromItem(item).data instanceof BlockSignalSemaphoreBase ||
						Block.getBlockFromItem(item).data instanceof BlockPlatform
		);
	}

	private static void renderRailOneWayArrows(Rail rail, float yOffset) {
		final long speedLimit1 = rail.getSpeedLimitKilometersPerHour(false);
		final long speedLimit2 = rail.getSpeedLimitKilometersPerHour(true);

		// Render one-way rail arrows
		if (speedLimit1 == 0 || speedLimit2 == 0) {
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				RenderTrains.scheduleRender(ONE_WAY_RAIL_ARROW_TEXTURE, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset + 0.125, z1, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x3, y2 + yOffset + 0.125, z3, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
					IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x1, y1 + yOffset + 0.125, z1, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, x3, y2 + yOffset + 0.125, z3, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
				});
			}, 1, -1, 1);
		}
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, RenderState renderState, float railWidth) {
		renderRailStandard(clientWorld, rail, 0.065625F, renderState, railWidth, renderState.hasColor ? RAIL_PREVIEW_TEXTURE : RAIL_TEXTURE, -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, float yOffset, RenderState renderState, float railWidth, Identifier defaultTexture, float u1, float v1, float u2, float v2) {
		// Render rail models
		final boolean[] renderType = {false, false}; // render default rail, rendered something
		for (final String style : rail.getStyles()) {
			if (style.equals(CustomResourceLoader.DEFAULT_RAIL_ID)) {
				renderType[0] = true;
			} else {
				final boolean flip = style.endsWith("_2");
				CustomResourceLoader.getRailById(RailResource.getIdWithoutDirection(style), railResource -> rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
					final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
					final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
					final double differenceX = x3 - x1;
					final double differenceZ = z3 - z1;
					final double yaw = Math.atan2(differenceZ, differenceX);
					final double pitch = Math.atan2(y2 - y1, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ));
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations((x1 + x3) / 2, (y1 + y2) / 2 + railResource.getModelYOffset(), (z1 + z3) / 2);
					storedMatrixTransformations.add(graphicsHolder -> {
						graphicsHolder.rotateYRadians((float) (Math.PI / 2 - yaw + (flip ? Math.PI : 0)));
						graphicsHolder.rotateXRadians((float) (Math.PI - pitch * (flip ? -1 : 1)));
					});
					railResource.render(storedMatrixTransformations, light);
					renderType[1] = true;
				}, railResource.getRepeatInterval(), 0, 0));
			}
		}

		// Render default rail or coloured rail
		if (renderType[0] || renderState.hasColor) {
			final int color = renderState.hasColor ? RailType.getRailColor(rail) : ARGB_WHITE;
			final Identifier texture = renderType[1] && !renderType[0] ? IRON_BLOCK_TEXTURE : defaultTexture;
			rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final BlockPos blockPos = Init.newBlockPos(x1, y1, z1);
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
				final int light = renderState == RenderState.FLASHING ? getFlashingLight() : renderState == RenderState.COLORED ? GraphicsHolder.getDefaultLight() : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
				RenderTrains.scheduleRender(texture, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
					IDrawing.drawTexture(graphicsHolder, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					IDrawing.drawTexture(graphicsHolder, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
				});
			}, 0.5, -railWidth, railWidth);
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

	private static void renderNode(BlockState blockState, BlockPos blockPos, BooleanSupplier shouldRender, int light) {
		if (blockState.getBlock().data instanceof BlockNode && shouldRender.getAsBoolean()) {
			final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			storedMatrixTransformations.add(graphicsHolder -> {
				graphicsHolder.rotateYDegrees((blockState.get(new Property<>(BlockNode.FACING.data)) ? -90 : 0) + (blockState.get(new Property<>(BlockNode.IS_45.data)) ? -45 : 0) + (blockState.get(new Property<>(BlockNode.IS_22_5.data)) ? -22.5F : 0));
				graphicsHolder.scale(4, 0.5F, 0.5F);
				graphicsHolder.translate(-0.5, 0, -0.5);
			});
			MODEL_SMALL_CUBE.render(storedMatrixTransformations, light);
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

	private static int getFlashingLight() {
		final int light = (int) Math.round((Math.sin(Math.PI * 2 * (System.currentTimeMillis() % FLASHING_INTERVAL) / FLASHING_INTERVAL) + 1) / 2 * 0xF);
		return LightmapTextureManager.pack(light, light);
	}

	private enum RenderState {
		NORMAL(false), COLORED(true), FLASHING(true);

		private final boolean hasColor;

		RenderState(boolean hasColor) {
			this.hasColor = hasColor;
		}
	}
}
