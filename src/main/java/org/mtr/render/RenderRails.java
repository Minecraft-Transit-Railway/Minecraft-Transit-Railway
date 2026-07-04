package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockNode;
import org.mtr.block.BlockSignalLightBase;
import org.mtr.block.BlockSignalSemaphoreBase;
import org.mtr.block.PlatformHelper;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.config.Config;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.data.RailType;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.item.ItemBrush;
import org.mtr.item.ItemNodeModifierBase;
import org.mtr.item.ItemRailModifier;
import org.mtr.libraries.it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateLastRailStyles;
import org.mtr.registry.DataComponentTypes;
import org.mtr.registry.Items;
import org.mtr.resource.RailResource;
import org.mtr.tool.CullingHelper;
import org.mtr.tool.Drawing;

import java.util.Collections;
import java.util.function.BooleanSupplier;

public final class RenderRails implements IGui {

	private static final ResourceLocation IRON_BLOCK_TEXTURE = ResourceLocation.parse("textures/block/iron_block.png");
	private static final ResourceLocation METAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/metal.png");
	private static final ResourceLocation RAIL_PREVIEW_TEXTURE = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/rail_preview.png");
	private static final ResourceLocation RAIL_TEXTURE = ResourceLocation.parse("textures/block/rail.png");
	private static final ResourceLocation WOOL_TEXTURE = ResourceLocation.parse("textures/block/white_wool.png");
	private static final ResourceLocation ONE_WAY_RAIL_ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/one_way_rail_arrow.png");
	private static final int INVALID_NODE_CHECK_RADIUS = 16;
	private static final double LIGHT_REFERENCE_OFFSET = 0.1;
	private static final int SIGNAL_DOTTED_LINE_SCALE = 4;
	private static final float SIGNAL_LINE_WIDTH = 1F / 16;

	public static void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, Vec3 offset) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		final ClientLevel clientWorld = minecraftClient.level;
		final LocalPlayer clientPlayerEntity = minecraftClient.player;

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final boolean holdingRailRelated = isHoldingRailRelated(clientPlayerEntity);

		// Finding visible rails.
		//
		// Cull each rail as a whole against its axis-aligned bounding box rather than
		// re-checking the camera distance at every interval step inside the rail. This
		// gives two wins at once:
		//   1. Either the whole rail renders or none of it does — long rails no longer
		//      end abruptly when their far end leaves the render-distance sphere.
		//   2. We skip the railMath walk entirely for off-screen rails, dropping the
		//      per-segment matrix maths and StoredMatrixTransformations allocations.
		// See docs/PERFORMANCE.md §3.10 for the analysis.
		final double renderDistance = minecraftClient.levelRenderer.getLastViewDistance() * 16;
		final ObjectArrayList<Rail> railsToRender = new ObjectArrayList<>();
		MinecraftClientData.getInstance().railWrapperList.values().forEach(railWrapper -> {
			final Rail rail = railWrapper.getRail();
			if (CullingHelper.getDistanceFromCameraToBox(rail.railMath.minX, rail.railMath.minY, rail.railMath.minZ, rail.railMath.maxX, rail.railMath.maxY, rail.railMath.maxZ) <= renderDistance) {
				railsToRender.add(rail);
			}
		});

		// Ghost rails (when holding brush)
		final ObjectArraySet<Rail> hoverRails = new ObjectArraySet<>();
		if (clientPlayerEntity.isHolding(Items.BRUSH.get())) {
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos != null) {
				final Rail rail = railAndBlockPos.left();
				final BlockPos blockPos = railAndBlockPos.right();
				if (clientPlayerEntity.isShiftKeyDown()) {
					if (PacketUpdateLastRailStyles.CLIENT_CACHE.canApplyStylesToRail(clientPlayerEntity.getUUID(), rail, false)) {
						final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUUID(), rail);
						hoverRails.add(newRail);
						railsToRender.remove(rail);
						railsToRender.add(newRail);
						final DoubleDoubleImmutablePair railRadii = newRail.railMath.getHorizontalRadii();
						renderRailStats(matrixStack, vertexConsumerProvider, offset, blockPos, null, newRail.railMath.getLength(), railRadii.leftDouble(), railRadii.rightDouble());
					}
				} else {
					hoverRails.add(rail);
					final DoubleDoubleImmutablePair railRadii = rail.railMath.getHorizontalRadii();
					renderRailStats(matrixStack, vertexConsumerProvider, offset, blockPos, null, rail.railMath.getLength(), railRadii.leftDouble(), railRadii.rightDouble());
				}
			}
		}

		// Ghost rail (when building rail)
		final ItemStack itemStack = getStackInHand();
		final Item item = itemStack.getItem();
		if (item instanceof ItemRailModifier) {
			final HitResult hitResult = minecraftClient.hitResult;
			if (hitResult != null) {
				final Vec3 hitPos = hitResult.getLocation();
				final BlockPos posStart = BlockPos.containing(hitPos.x, hitPos.y, hitPos.z);
				final BlockPos posEnd = itemStack.get(DataComponentTypes.START_POS.get());

				if (posEnd != null) {
					final BlockState blockStateEnd = clientWorld.getBlockState(posEnd);

					if (blockStateEnd.getBlock() instanceof BlockNode) {
						final BlockState blockStateStart = clientWorld.getBlockState(posStart);
						final float angleEnd = BlockNode.getAngle(blockStateEnd);
						final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(
							MTR.blockPosToPosition(posStart), blockStateStart.getBlock() instanceof BlockNode ? BlockNode.getAngle(blockStateStart) : (blockStateEnd.getBlock() instanceof BlockNode.BlockContinuousMovementNode ? angleEnd : clientPlayerEntity.getYRot() + 90),
							MTR.blockPosToPosition(posEnd), angleEnd
						);

						final Rail rail = ((ItemRailModifier) item).createRail(clientPlayerEntity.getUUID(), ItemNodeModifierBase.getTransportMode(itemStack), blockStateStart, blockStateEnd, posStart, posEnd, angles.left(), angles.right());
						if (rail != null) {
							final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUUID(), rail);
							railsToRender.add(newRail);
							hoverRails.add(newRail);
							final double railLength = newRail.railMath.getLength();
							final DoubleDoubleImmutablePair railRadii = newRail.railMath.getHorizontalRadii();
							renderRailStats(matrixStack, vertexConsumerProvider, offset, posStart, posEnd, railLength, railRadii.leftDouble(), railRadii.rightDouble());
							renderRailStats(matrixStack, vertexConsumerProvider, offset, posEnd, posStart, railLength, railRadii.rightDouble(), railRadii.leftDouble());
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
					MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack1, vertexConsumer, offset1) -> renderWithinRenderDistance(rail, (blockPos, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> IDrawing.drawLineInWorld(
						matrixStack1,
						vertexConsumer,
						(float) (x1 - offset1.x),
						(float) (y1 - offset1.y + 0.5),
						(float) (z1 - offset1.z),
						(float) (x3 - offset1.x),
						(float) (y3 - offset1.y + 0.5),
						(float) (z3 - offset1.z),
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

		if (holdingRailRelated) {
			// Render nodes
			MinecraftClientData.getInstance().positionsToRail.keySet().forEach(position -> {
				final BlockPos blockPos = MTR.positionToBlockPos(position);
				renderNode(clientWorld.getBlockState(blockPos), blockPos, () -> true, DEFAULT_LIGHT);
			});

			// Render nodes with the connected block state but isn't actually connected
			for (int x = -INVALID_NODE_CHECK_RADIUS; x <= INVALID_NODE_CHECK_RADIUS; x++) {
				for (int y = -INVALID_NODE_CHECK_RADIUS; y <= INVALID_NODE_CHECK_RADIUS; y++) {
					for (int z = -INVALID_NODE_CHECK_RADIUS; z <= INVALID_NODE_CHECK_RADIUS; z++) {
						final BlockPos blockPos = clientPlayerEntity.blockPosition().offset(x, y, z);
						final BlockState blockState = clientWorld.getBlockState(blockPos);
						renderNode(blockState, blockPos, () -> blockState.getValue(BlockNode.IS_CONNECTED) && !MinecraftClientData.getInstance().positionsToRail.containsKey(MTR.blockPosToPosition(blockPos)), MainRenderer.getFlashingLight());
					}
				}
			}
		}
	}

	public static boolean isHoldingRailRelated(LocalPlayer clientPlayerEntity) {
		return clientPlayerEntity.isHolding(itemStack -> {
			final Item item = itemStack.getItem();
			return item instanceof ItemNodeModifierBase || item instanceof ItemBrush ||
				Block.byItem(item) instanceof BlockSignalLightBase ||
				Block.byItem(item) instanceof BlockNode ||
				Block.byItem(item) instanceof BlockSignalSemaphoreBase ||
				Block.byItem(item) instanceof PlatformHelper;
		});
	}

	private static void renderRailOneWayArrows(Rail rail, float yOffset) {
		final long speedLimit1 = rail.getSpeedLimitKilometersPerHour(false);
		final long speedLimit2 = rail.getSpeedLimitKilometersPerHour(true);

		// Render one-way rail arrows
		if (speedLimit1 == 0 || speedLimit2 == 0) {
			renderWithinRenderDistance(rail, (blockPos, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> MainRenderer.scheduleRender(ONE_WAY_RAIL_ARROW_TEXTURE, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				IDrawing.drawTexture(matrixStack, vertexConsumer, x4, y4 + yOffset + 0.125 + SMALL_OFFSET, z4, x1, y1 + yOffset + 0.125, z1, x2, y2 + yOffset + 0.125 + SMALL_OFFSET, z2, x3, y3 + yOffset + 0.125, z3, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
				IDrawing.drawTexture(matrixStack, vertexConsumer, x3, y3 + yOffset + 0.125, z3, x2, y2 + yOffset + 0.125 + SMALL_OFFSET, z2, x1, y1 + yOffset + 0.125, z1, x4, y4 + yOffset + 0.125 + SMALL_OFFSET, z4, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
			}), 1, -1, 1);
		}
	}

	private static void renderRailStandard(ClientLevel clientWorld, Rail rail, RenderState renderState, float railWidth) {
		renderRailStandard(clientWorld, rail, 0.065625F, renderState, railWidth, renderState.hasColor ? RAIL_PREVIEW_TEXTURE : RAIL_TEXTURE, -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientLevel clientWorld, Rail rail, float yOffset, RenderState renderState, float railWidth, ResourceLocation defaultTexture, float u1, float v1, float u2, float v2) {
		// Render rail models
		final boolean[] renderType = {false, false}; // render default rail, rendered something
		for (final String style : rail.getStyles()) {
			final String newStyle;
			if (Config.getClient().getDefaultRail3D() && rail.getTransportMode() == TransportMode.TRAIN) {
				newStyle = style.equals(CustomResourceLoader.DEFAULT_RAIL_ID) ? rail.isSiding() ? CustomResourceLoader.DEFAULT_RAIL_3D_SIDING_ID : CustomResourceLoader.DEFAULT_RAIL_3D_ID : style;
			} else {
				newStyle = style;
			}

			if (newStyle.equals(CustomResourceLoader.DEFAULT_RAIL_ID)) {
				renderType[0] = true;
			} else {
				final boolean flip = newStyle.endsWith("_2");
				CustomResourceLoader.getRailById(RailResource.getIdWithoutDirection(newStyle), railResource -> renderWithinRenderDistance(rail, (blockPos, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> {
					final int light = clientWorld.getMaxLocalRawBrightness(blockPos);
					final double differenceX = x3 - x1;
					final double differenceZ = z3 - z1;
					final double yaw = Math.atan2(differenceZ, differenceX);
					final double pitch = Math.atan2((y3 + y4) / 2 - (y1 + y2) / 2, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ));
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations((x1 + x3) / 2, (y1 + y2) / 2 + railResource.getModelYOffset(), (z1 + z3) / 2);
					storedMatrixTransformations.add(matrixStack -> {
						Drawing.rotateYRadians(matrixStack, (float) (Math.PI / 2 - yaw + (flip ? Math.PI : 0)));
						Drawing.rotateXRadians(matrixStack, (float) pitch * (flip ? 1 : -1));
						Drawing.rotateZRadians(matrixStack, (float) (tiltAngle + Math.toRadians(((x1 * z1) % 10) / 100)));
					});
					railResource.render(storedMatrixTransformations, light);
					renderType[1] = true;
				}, railResource.getRepeatInterval(), 0, 0));
			}
		}

		// Render default rail or coloured rail
		if (renderType[0] || renderState.hasColor) {
			final int color = renderState.hasColor ? renderState == RenderState.FLASHING ? MainRenderer.getFlashingColor(RailType.getRailColor(rail), 1) : RailType.getRailColor(rail) : ARGB_WHITE;

			final ResourceLocation texture = renderType[1] && !renderType[0] ? IRON_BLOCK_TEXTURE : defaultTexture;
			renderWithinRenderDistance(rail, (blockPos, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> {
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F;
				final int light = renderState == RenderState.FLASHING || renderState == RenderState.COLORED ? DEFAULT_LIGHT : LightTexture.pack(clientWorld.getBrightness(LightLayer.BLOCK, blockPos), clientWorld.getBrightness(LightLayer.SKY, blockPos));
				MainRenderer.scheduleRender(texture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
					IDrawing.drawTexture(matrixStack, vertexConsumer, x4, y4 + yOffset + SMALL_OFFSET, z4, x1, y1 + yOffset, z1, x2, y2 + yOffset + SMALL_OFFSET, z2, x3, y3 + yOffset, z3, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					IDrawing.drawTexture(matrixStack, vertexConsumer, x3, y3 + yOffset, z3, x2, y2 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y4 + yOffset + SMALL_OFFSET, z4, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
				});
			}, 0.5, -railWidth, railWidth);
		}
	}

	private static void renderSignalsStandard(ClientLevel clientWorld, Rail rail) {
		final IntArrayList colors = new IntArrayList(rail.getSignalColors());
		Collections.sort(colors);
		final LongArrayList preBlockedSignalColors = MinecraftClientData.getInstance().railIdToPreBlockedSignalColors.getOrDefault(rail.getHexId(), new LongArrayList());
		final LongArrayList currentlyBlockedSignalColors = MinecraftClientData.getInstance().railIdToCurrentlyBlockedSignalColors.getOrDefault(rail.getHexId(), new LongArrayList());

		for (int i = 0; i < colors.size(); i++) {
			final int rawColor = colors.getInt(i);
			final boolean preBlocked = preBlockedSignalColors.contains(rawColor);
			final boolean currentlyBlocked = currentlyBlockedSignalColors.contains(rawColor);
			final boolean shouldFlash = preBlocked || currentlyBlocked;
			final int color = shouldFlash ? MainRenderer.getFlashingColor(rawColor, currentlyBlocked ? 1 : 4) : ARGB_BLACK | rawColor;
			final float u1 = SIGNAL_LINE_WIDTH * i + 1 - SIGNAL_LINE_WIDTH * colors.size() / 2;
			final float u2 = u1 + SIGNAL_LINE_WIDTH;
			final int[] index = {0};

			renderWithinRenderDistance(rail, (blockPos, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> {
				final boolean alternate = (rawColor & ARGB_BLACK) != 0 && (index[0] % 2) == 0;
				final float v1 = (float) Math.floorMod(index[0], SIGNAL_DOTTED_LINE_SCALE) / SIGNAL_DOTTED_LINE_SCALE;
				final float v2 = (float) Math.floorMod(index[0] + 1, SIGNAL_DOTTED_LINE_SCALE) / SIGNAL_DOTTED_LINE_SCALE;
				final int light = shouldFlash ? DEFAULT_LIGHT : LightTexture.pack(clientWorld.getBrightness(LightLayer.BLOCK, blockPos), clientWorld.getBrightness(LightLayer.SKY, blockPos));
				MainRenderer.scheduleRender(WOOL_TEXTURE, false, shouldFlash ? QueuedRenderLayer.EXTERIOR : QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
					IDrawing.drawTexture(matrixStack, vertexConsumer, x4, y4 + 0.125 + SMALL_OFFSET, z4, x1, y1 + 0.125, z1, x2, y2 + 0.125 + SMALL_OFFSET, z2, x3, y3 + 0.125, z3, offset, u1, v1, u2, v2, Direction.UP, alternate ? 0 : color, light);
					IDrawing.drawTexture(matrixStack, vertexConsumer, x1, y1 + 0.125, z1, x4, y4 + 0.125 + SMALL_OFFSET, z4, x3, y3 + 0.125, z3, x2, y2 + 0.125 + SMALL_OFFSET, z2, offset, u1, v1, u2, v2, Direction.UP, alternate ? 0 : color, light);
				});
				index[0]++;
			}, 1F / SIGNAL_DOTTED_LINE_SCALE, u1 - 1, u2 - 1);
		}
	}

	/**
	 * Walk every interval step along {@code rail} and emit the callback for each segment.
	 *
	 * <p>Per-segment distance culling has been removed (see docs/PERFORMANCE.md §3.10); the
	 * caller is expected to have already filtered out rails whose bounding boxes are out
	 * of range. As a result, once a rail is selected for rendering, every one of its
	 * segments is emitted — eliminating the "rail ends abruptly mid-curve" artifact that
	 * older per-segment culling produced.</p>
	 */
	private static void renderWithinRenderDistance(Rail rail, RenderRailWithBlockPos callback, double interval, float offsetRadius1, float offsetRadius2) {
		rail.railMath.render((x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> callback.renderRail(
			BlockPos.containing(x1, y1 + LIGHT_REFERENCE_OFFSET, z1),
			x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle
		), interval, offsetRadius1, offsetRadius2);
	}

	private static void renderNode(BlockState blockState, BlockPos blockPos, BooleanSupplier shouldRender, int light) {
		if (blockState.getBlock() instanceof BlockNode && shouldRender.getAsBoolean()) {
			final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			storedMatrixTransformations.add(matrixStack -> {
				Drawing.rotateYDegrees(matrixStack, (blockState.getValue(BlockNode.FACING) ? -90 : 0) + (blockState.getValue(BlockNode.IS_45) ? -45 : 0) + (blockState.getValue(BlockNode.IS_22_5) ? -22.5F : 0));
				matrixStack.scale(4, 0.5F, 0.5F);
				matrixStack.translate(-0.5, 0, -0.5);
			});
			MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				RenderPSDAPGDoor.MODEL_SMALL_CUBE.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
				matrixStack.popPose();
			});
		}
	}

	private static void renderRailStats(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, Vec3 offset, BlockPos renderPos, @Nullable BlockPos otherPos, double railLength, double closerRadius, double otherRadius) {
		if (railLength > 0) {
			final String textXYZOffsetLabel = otherPos == null ? null : TranslationProvider.GUI_MTR_RAIL_XYZ_OFFSET.getString();
			final String textXYZOffset = otherPos == null ? null : String.format("(%s, %s, %s)", renderPos.getX() - otherPos.getX(), renderPos.getY() - otherPos.getY(), renderPos.getZ() - otherPos.getZ());

			final String textXZRadiusLabel;
			final String textXZRadius;
			final double roundedCloserRadius = Utilities.round(closerRadius, 3);
			final double roundedOtherRadius = Utilities.round(otherRadius, 3);
			if (roundedCloserRadius == 0 || roundedOtherRadius == 0 || roundedCloserRadius == roundedOtherRadius) {
				if (roundedCloserRadius == 0 && roundedOtherRadius == 0) {
					textXZRadiusLabel = null;
					textXZRadius = null;
				} else {
					textXZRadiusLabel = TranslationProvider.GUI_MTR_RAIL_XZ_RADIUS.getString();
					textXZRadius = String.valueOf(roundedCloserRadius == 0 ? roundedOtherRadius : roundedCloserRadius);
				}
			} else {
				textXZRadiusLabel = TranslationProvider.GUI_MTR_RAIL_XZ_RADII.getString();
				textXZRadius = String.format("%s, %s", roundedCloserRadius, roundedOtherRadius);
			}

			final String textLengthLabel = TranslationProvider.GUI_MTR_RAIL_XZ_LENGTH.getString();
			final String textLength = String.valueOf(Utilities.round(railLength, 3));

			final double textOffset = otherPos == null ? 0.5 : 1;

			matrixStack.pushPose();
			matrixStack.translate(renderPos.getX() - offset.x + 0.5, renderPos.getY() - offset.y + textOffset, renderPos.getZ() - offset.z + 0.5);
			MTRClient.transformToFacePlayer(matrixStack, renderPos.getX() + 0.5, renderPos.getY() + textOffset, renderPos.getZ() + 0.5);
			Drawing.rotateZDegrees(matrixStack, 180);
			matrixStack.scale(1 / 32F, 1 / 32F, -1 / 32F);
			int line = 0;
			if (otherPos != null) {
				line = renderRailStat(matrixStack, vertexConsumerProvider, textXYZOffsetLabel, textXYZOffset, line);
			}
			if (textXZRadius != null) {
				line = renderRailStat(matrixStack, vertexConsumerProvider, textXZRadiusLabel, textXZRadius, line);
			}
			renderRailStat(matrixStack, vertexConsumerProvider, textLengthLabel, textLength, line);
			matrixStack.popPose();
		}
	}

	private static int renderRailStat(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, String title, String data, int line) {
		int newLine = line - 9;
		final Font textRenderer = Minecraft.getInstance().font;
		textRenderer.drawInBatch(data, -textRenderer.width(data) / 2F, newLine, ARGB_WHITE, true, matrixStack.last().pose(), vertexConsumerProvider, Font.DisplayMode.NORMAL, 0, DEFAULT_LIGHT);
		matrixStack.pushPose();
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		newLine -= 5;
		textRenderer.drawInBatch(title, -textRenderer.width(title) / 2F, newLine * 2, ARGB_WHITE, true, matrixStack.last().pose(), vertexConsumerProvider, Font.DisplayMode.NORMAL, 0, DEFAULT_LIGHT);
		matrixStack.popPose();
		return newLine - 1;
	}

	private static ItemStack getStackInHand() {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		if (clientPlayerEntity != null) {
			try {
				return clientPlayerEntity.getItemInHand(clientPlayerEntity.getUsedItemHand());
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	private enum RenderState {
		NORMAL(false), COLORED(true), FLASHING(true);

		private final boolean hasColor;

		RenderState(boolean hasColor) {
			this.hasColor = hasColor;
		}
	}

	@FunctionalInterface
	private interface RenderRailWithBlockPos {
		void renderRail(BlockPos blockPos, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, double tiltAngle);
	}
}
