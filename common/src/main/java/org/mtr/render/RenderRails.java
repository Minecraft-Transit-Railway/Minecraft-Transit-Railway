package org.mtr.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
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
import org.mtr.item.ItemBlockClickingBase;
import org.mtr.item.ItemBrush;
import org.mtr.item.ItemNodeModifierBase;
import org.mtr.item.ItemRailModifier;
import org.mtr.model.OptimizedRenderer;
import org.mtr.packet.PacketUpdateLastRailStyles;
import org.mtr.registry.Items;
import org.mtr.resource.RailResource;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class RenderRails implements IGui {

	private static final Identifier IRON_BLOCK_TEXTURE = Identifier.of("textures/block/iron_block.png");
	private static final Identifier METAL_TEXTURE = Identifier.of(MTR.MOD_ID, "textures/block/metal.png");
	private static final Identifier RAIL_PREVIEW_TEXTURE = Identifier.of(MTR.MOD_ID, "textures/block/rail_preview.png");
	private static final Identifier RAIL_TEXTURE = Identifier.of("textures/block/rail.png");
	private static final Identifier WOOL_TEXTURE = Identifier.of("textures/block/white_wool.png");
	private static final Identifier ONE_WAY_RAIL_ARROW_TEXTURE = Identifier.of(MTR.MOD_ID, "textures/block/one_way_rail_arrow.png");
	private static final int INVALID_NODE_CHECK_RADIUS = 16;
	private static final double LIGHT_REFERENCE_OFFSET = 0.1;

	public static void render() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;

		if (clientWorld == null || clientPlayerEntity == null) {
			return;
		}

		final ObjectArrayList<Function<OcclusionCullingInstance, Runnable>> cullingTasks = new ObjectArrayList<>();
		final Vec3d cameraPosition = minecraftClient.gameRenderer.getCamera().getPos();
		final com.logisticscraft.occlusionculling.util.Vec3d camera = new com.logisticscraft.occlusionculling.util.Vec3d(cameraPosition.x, cameraPosition.y, cameraPosition.z);
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
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos != null) {
				final Rail rail = railAndBlockPos.left();
				final BlockPos blockPos = railAndBlockPos.right();
				if (clientPlayerEntity.isSneaking()) {
					if (PacketUpdateLastRailStyles.CLIENT_CACHE.canApplyStylesToRail(clientPlayerEntity.getUuid(), rail, false)) {
						final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUuid(), rail);
						hoverRails.add(newRail);
						railsToRender.remove(rail);
						railsToRender.add(newRail);
						final DoubleDoubleImmutablePair railRadii = newRail.railMath.getHorizontalRadii();
						renderRailStats(blockPos, null, newRail.railMath.getLength(), railRadii.leftDouble(), railRadii.rightDouble());
					}
				} else {
					hoverRails.add(rail);
					final DoubleDoubleImmutablePair railRadii = rail.railMath.getHorizontalRadii();
					renderRailStats(blockPos, null, rail.railMath.getLength(), railRadii.leftDouble(), railRadii.rightDouble());
				}
			}
		}

		// Ghost rail (when building rail)
		final ItemStack itemStack = getStackInHand();
		final Item item = itemStack.getItem();
		if (item instanceof ItemRailModifier) {
			final HitResult hitResult = minecraftClient.crosshairTarget;
			if (hitResult != null) {
				final Vec3d hitPos = hitResult.getPos();
				final BlockPos posStart = BlockPos.ofFloored(hitPos.x, hitPos.y, hitPos.z);
				final NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
				final NbtCompound nbtCompound = nbtComponent == null ? null : nbtComponent.copyNbt();

				if (nbtCompound != null && nbtCompound.contains(ItemBlockClickingBase.TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(nbtCompound.getLong(ItemBlockClickingBase.TAG_POS));
					final BlockState blockStateEnd = clientWorld.getBlockState(posEnd);

					if (blockStateEnd.getBlock() instanceof BlockNode) {
						final BlockState blockStateStart = clientWorld.getBlockState(posStart);
						final float angleEnd = BlockNode.getAngle(blockStateEnd);
						final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(
								MTR.blockPosToPosition(posStart), blockStateStart.getBlock() instanceof BlockNode ? BlockNode.getAngle(blockStateStart) : blockStateEnd.getBlock() instanceof BlockNode.BlockContinuousMovementNode ? angleEnd : clientPlayerEntity.getYaw() + 90,
								MTR.blockPosToPosition(posEnd), angleEnd
						);

						final Rail rail = ((ItemRailModifier) item).createRail(clientPlayerEntity.getUuid(), ItemNodeModifierBase.getTransportMode(nbtCompound), blockStateStart, blockStateEnd, posStart, posEnd, angles.left(), angles.right());
						if (rail != null) {
							final Rail newRail = PacketUpdateLastRailStyles.CLIENT_CACHE.getRailWithLastStyles(clientPlayerEntity.getUuid(), rail);
							railsToRender.add(newRail);
							hoverRails.add(newRail);
							final double railLength = newRail.railMath.getLength();
							final DoubleDoubleImmutablePair railRadii = newRail.railMath.getHorizontalRadii();
							renderRailStats(posStart, posEnd, railLength, railRadii.leftDouble(), railRadii.rightDouble());
							renderRailStats(posEnd, posStart, railLength, railRadii.rightDouble(), railRadii.leftDouble());
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
					MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (matrixStack, vertexConsumer, offset) -> renderWithinRenderDistance(rail, (blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> IDrawing.drawLineInWorld(
							matrixStack,
							vertexConsumer,
							(float) (x1 - offset.x),
							(float) (y1 - offset.y + 0.5),
							(float) (z1 - offset.z),
							(float) (x3 - offset.x),
							(float) (y2 - offset.y + 0.5),
							(float) (z3 - offset.z),
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
						final BlockPos blockPos = clientPlayerEntity.getBlockPos().add(x, y, z);
						final BlockState blockState = clientWorld.getBlockState(blockPos);
						renderNode(blockState, blockPos, () -> blockState.get(BlockNode.IS_CONNECTED) && !MinecraftClientData.getInstance().positionsToRail.containsKey(MTR.blockPosToPosition(blockPos)), MainRenderer.getFlashingLight());
					}
				}
			}
		}

		if (!OptimizedRenderer.renderingShadows()) {
			MainRenderer.WORKER_THREAD.scheduleRails(occlusionCullingInstance -> {
				final ObjectArrayList<Runnable> tasks = new ObjectArrayList<>();
				cullingTasks.forEach(occlusionCullingInstanceRunnableFunction -> tasks.add(occlusionCullingInstanceRunnableFunction.apply(occlusionCullingInstance)));
				minecraftClient.execute(() -> tasks.forEach(Runnable::run));
			});
		}
	}

	public static boolean isHoldingRailRelated(ClientPlayerEntity clientPlayerEntity) {
		return clientPlayerEntity.isHolding(itemStack -> {
			final Item item = itemStack.getItem();
			return item instanceof ItemNodeModifierBase || item instanceof ItemBrush ||
					Block.getBlockFromItem(item) instanceof BlockSignalLightBase ||
					Block.getBlockFromItem(item) instanceof BlockNode ||
					Block.getBlockFromItem(item) instanceof BlockSignalSemaphoreBase ||
					Block.getBlockFromItem(item) instanceof PlatformHelper;
		});
	}

	private static void renderRailOneWayArrows(Rail rail, float yOffset) {
		final long speedLimit1 = rail.getSpeedLimitKilometersPerHour(false);
		final long speedLimit2 = rail.getSpeedLimitKilometersPerHour(true);

		// Render one-way rail arrows
		if (speedLimit1 == 0 || speedLimit2 == 0) {
			renderWithinRenderDistance(rail, (blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> MainRenderer.scheduleRender(ONE_WAY_RAIL_ARROW_TEXTURE, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				IDrawing.drawTexture(matrixStack, vertexConsumer, x1, y1 + yOffset + 0.125, z1, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x3, y2 + yOffset + 0.125, z3, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
				IDrawing.drawTexture(matrixStack, vertexConsumer, x2, y1 + yOffset + 0.125 + SMALL_OFFSET, z2, x1, y1 + yOffset + 0.125, z1, x4, y2 + yOffset + 0.125 + SMALL_OFFSET, z4, x3, y2 + yOffset + 0.125, z3, offset, 0, speedLimit1 == 0 ? 0.25F : 0.75F, 1, speedLimit1 == 0 ? 0.75F : 0.25F, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
			}), 1, -1, 1);
		}
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, RenderState renderState, float railWidth) {
		renderRailStandard(clientWorld, rail, 0.065625F, renderState, railWidth, renderState.hasColor ? RAIL_PREVIEW_TEXTURE : RAIL_TEXTURE, -1, -1, -1, -1);
	}

	private static void renderRailStandard(ClientWorld clientWorld, Rail rail, float yOffset, RenderState renderState, float railWidth, Identifier defaultTexture, float u1, float v1, float u2, float v2) {
		// Render rail models
		final boolean[] renderType = {false, false}; // render default rail, rendered something
		for (final String style : rail.getStyles()) {
			final String newStyle;
			if (OptimizedRenderer.hasOptimizedRendering() && Config.getClient().getDefaultRail3D() && rail.getTransportMode() == TransportMode.TRAIN) {
				newStyle = style.equals(CustomResourceLoader.DEFAULT_RAIL_ID) ? rail.isSiding() ? CustomResourceLoader.DEFAULT_RAIL_3D_SIDING_ID : CustomResourceLoader.DEFAULT_RAIL_3D_ID : style;
			} else {
				newStyle = style;
			}

			if (newStyle.equals(CustomResourceLoader.DEFAULT_RAIL_ID)) {
				renderType[0] = true;
			} else {
				final boolean flip = newStyle.endsWith("_2");
				CustomResourceLoader.getRailById(RailResource.getIdWithoutDirection(newStyle), railResource -> renderWithinRenderDistance(rail, (blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
					final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.BLOCK, blockPos), clientWorld.getLightLevel(LightType.SKY, blockPos));
					final double differenceX = x3 - x1;
					final double differenceZ = z3 - z1;
					final double yaw = Math.atan2(differenceZ, differenceX);
					final double pitch = Math.atan2(y2 - y1, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ));
					final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations((x1 + x3) / 2, (y1 + y2) / 2 + railResource.getModelYOffset(), (z1 + z3) / 2);
					storedMatrixTransformations.add(matrixStack -> {
						IDrawing.rotateYRadians(matrixStack, (float) (Math.PI / 2 - yaw + (flip ? Math.PI : 0)));
						IDrawing.rotateXRadians(matrixStack, (float) (Math.PI - pitch * (flip ? -1 : 1)));
						IDrawing.rotateZDegrees(matrixStack, (float) ((x1 * z1) % 10) / 100);
					});
					railResource.render(storedMatrixTransformations, light);
					renderType[1] = true;
				}, railResource.getRepeatInterval(), 0, 0));
			}
		}

		// Render default rail or coloured rail
		if (renderType[0] || renderState.hasColor) {
			int color = renderState.hasColor ? renderState == RenderState.FLASHING ? MainRenderer.getFlashingColor(RailType.getRailColor(rail)) : RailType.getRailColor(rail) : ARGB_WHITE;

			final Identifier texture = renderType[1] && !renderType[0] ? IRON_BLOCK_TEXTURE : defaultTexture;
			renderWithinRenderDistance(rail, (blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F;
				final int light = renderState == RenderState.FLASHING || renderState == RenderState.COLORED ? DEFAULT_LIGHT : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.BLOCK, blockPos), clientWorld.getLightLevel(LightType.SKY, blockPos));
				MainRenderer.scheduleRender(texture, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
					IDrawing.drawTexture(matrixStack, vertexConsumer, x1, y1 + yOffset, z1, x2, y1 + yOffset + SMALL_OFFSET, z2, x3, y2 + yOffset, z3, x4, y2 + yOffset + SMALL_OFFSET, z4, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
					IDrawing.drawTexture(matrixStack, vertexConsumer, x2, y1 + yOffset + SMALL_OFFSET, z2, x1, y1 + yOffset, z1, x4, y2 + yOffset + SMALL_OFFSET, z4, x3, y2 + yOffset, z3, offset, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light);
				});
			}, 0.5, -railWidth, railWidth);
		}
	}

	private static void renderSignalsStandard(ClientWorld clientWorld, Rail rail) {
		final IntArrayList colors = new IntArrayList(rail.getSignalColors());
		Collections.sort(colors);
		final float width = 1F / 16;
		final LongArrayList blockedSignalColors = MinecraftClientData.getInstance().railIdToBlockedSignalColors.getOrDefault(rail.getHexId(), new LongArrayList());

		for (int i = 0; i < colors.size(); i++) {
			final int rawColor = colors.getInt(i);
			final boolean shouldFlash = blockedSignalColors.contains(rawColor);
			final int color = shouldFlash ? MainRenderer.getFlashingColor(ARGB_BLACK | rawColor) : ARGB_BLACK | rawColor;
			final float u1 = width * i + 1 - width * colors.size() / 2;
			final float u2 = u1 + width;

			renderWithinRenderDistance(rail, (blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
				final int light = shouldFlash ? DEFAULT_LIGHT : LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.BLOCK, blockPos), clientWorld.getLightLevel(LightType.SKY, blockPos));
				MainRenderer.scheduleRender(WOOL_TEXTURE, false, shouldFlash ? QueuedRenderLayer.EXTERIOR : QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
					IDrawing.drawTexture(matrixStack, vertexConsumer, x1, y1 + 0.125, z1, x2, y1 + 0.125 + SMALL_OFFSET, z2, x3, y2 + 0.125, z3, x4, y2 + 0.125 + SMALL_OFFSET, z4, offset, u1, 0, u2, 1, Direction.UP, color, light);
					IDrawing.drawTexture(matrixStack, vertexConsumer, x4, y2 + 0.125 + SMALL_OFFSET, z4, x3, y2 + 0.125, z3, x2, y1 + 0.125 + SMALL_OFFSET, z2, x1, y1 + 0.125, z1, offset, u1, 0, u2, 1, Direction.UP, color, light);
				});
			}, 1, u1 - 1, u2 - 1);
		}
	}

	private static void renderWithinRenderDistance(Rail rail, RenderRailWithBlockPos callback, double interval, float offsetRadius1, float offsetRadius2) {
		final Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		final Vec3d cameraPosition = camera.getPos();
		final int renderDistance = (int) MinecraftClient.getInstance().worldRenderer.getViewDistance() * 16;

		rail.railMath.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
			final BlockPos blockPos = BlockPos.ofFloored(x1, y1 + LIGHT_REFERENCE_OFFSET, z1);
			final int distance = blockPos.getManhattanDistance(camera.getBlockPos());
			if (distance <= renderDistance) {
				if (distance < 32) {
					callback.renderRail(blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2);
				} else {
					final Vec3d rotatedVector = new Vec3d(x1, y1, z1).subtract(cameraPosition).rotateY((float) Math.toRadians(camera.getYaw())).rotateX((float) Math.toRadians(camera.getPitch()));
					if (rotatedVector.z > 0) {
						callback.renderRail(blockPos, x1, z1, x2, z2, x3, z3, x4, z4, y1, y2);
					}
				}
			}
		}, interval, offsetRadius1, offsetRadius2);
	}

	private static void renderNode(BlockState blockState, BlockPos blockPos, BooleanSupplier shouldRender, int light) {
		if (blockState.getBlock() instanceof BlockNode && shouldRender.getAsBoolean()) {
			final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			storedMatrixTransformations.add(matrixStack -> {
				IDrawing.rotateYDegrees(matrixStack, (blockState.get(BlockNode.FACING) ? -90 : 0) + (blockState.get(BlockNode.IS_45) ? -45 : 0) + (blockState.get(BlockNode.IS_22_5) ? -22.5F : 0));
				matrixStack.scale(4, 0.5F, 0.5F);
				matrixStack.translate(-0.5, 0, -0.5);
			});
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				RenderPSDAPGDoor.MODEL_SMALL_CUBE.render(matrixStack, vertexConsumer, DEFAULT_LIGHT, OverlayTexture.DEFAULT_UV);
				matrixStack.pop();
			});
		}
	}

	private static void renderRailStats(BlockPos renderPos, @Nullable BlockPos otherPos, double railLength, double closerRadius, double otherRadius) {
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

			MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
				matrixStack.push();
				matrixStack.translate(renderPos.getX() - offset.x + 0.5, renderPos.getY() - offset.y + textOffset, renderPos.getZ() - offset.z + 0.5);
				MTRClient.transformToFacePlayer(matrixStack, renderPos.getX() + 0.5, renderPos.getY() + textOffset, renderPos.getZ() + 0.5);
				IDrawing.rotateZDegrees(matrixStack, 180);
				matrixStack.scale(1 / 32F, 1 / 32F, -1 / 32F);
				int line = 0;
				if (otherPos != null) {
					line = renderRailStat(matrixStack, textXYZOffsetLabel, textXYZOffset, line);
				}
				if (textXZRadius != null) {
					line = renderRailStat(matrixStack, textXZRadiusLabel, textXZRadius, line);
				}
				renderRailStat(matrixStack, textLengthLabel, textLength, line);
				matrixStack.pop();
			});
		}
	}

	private static int renderRailStat(MatrixStack matrixStack, String title, String data, int line) {
		int newLine = line - 9;
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//		textRenderer.draw(data, -textRenderer.getWidth(data) / 2, newLine, ARGB_WHITE, true, DEFAULT_LIGHT);
		matrixStack.push();
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		newLine -= 5;
//		textRenderer.draw(title, -textRenderer.getWidth(title) / 2, newLine * 2, ARGB_WHITE, true, DEFAULT_LIGHT);
		matrixStack.pop();
		return newLine - 1;
	}

	private static ItemStack getStackInHand() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if (clientPlayerEntity != null) {
			try {
				return clientPlayerEntity.getStackInHand(clientPlayerEntity.getActiveHand());
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
		void renderRail(BlockPos blockPos, double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double y1, double y2);
	}
}
