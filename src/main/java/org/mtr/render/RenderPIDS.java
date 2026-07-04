package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.mtr.MTRClient;
import org.mtr.block.*;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.SimplifiedRoute;
import org.mtr.core.data.SimplifiedRoutePlatform;
import org.mtr.core.data.Station;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.data.IGui;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.tool.Drawing;

import java.awt.*;

public class RenderPIDS<T extends BlockPIDSBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, Utilities {

	private final float startX;
	private final float startY;
	private final float startZ;
	private final float maxHeight;
	private final float maxWidth;
	private final boolean rotate90;
	private final float textPadding;

	public static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int STATIONS_PER_PAGE = 10;
	private static final int SWITCH_PAGE_TICKS = 120;

	public RenderPIDS(float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, float textPadding) {
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.rotate90 = rotate90;
		this.textPadding = textPadding;
	}

	@Override
	public final void render(T blockEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos blockPos = blockEntity.getBlockPos();
		if (!blockEntity.canStoreData.test(world, blockPos)) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockStateProperties.HORIZONTAL_FACING);

		if (blockEntity.getPlatformIds().isEmpty()) {
			final LongArrayList platformIds = new LongArrayList();
			if (blockEntity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase) {
				final Station station = MTRClient.findStation(blockPos);
				if (station != null) {
					station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
				}
			} else {
				MTRClient.findClosePlatform(blockEntity.getBlockPos().below(4), 5, platform -> platformIds.add(platform.getId()));
			}
			getArrivalsAndRender(blockEntity, blockPos, facing, platformIds);
		} else {
			getArrivalsAndRender(blockEntity, blockPos, facing, blockEntity.getPlatformIds());
		}
	}

	public void renderText(PoseStack matrixStack, String text, int x, int y, Color color) {
		FontRenderHelper.render(matrixStack, text, FontRenderOptions.builder().offsetX(x).offsetY(y).color(color).build());
	}

	public String getArrivalString(long arrival, boolean isRealtime, boolean isCjk) {
		if (arrival >= 60) {
			return (isRealtime ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_MIN_CJK : TranslationProvider.GUI_MTR_ARRIVAL_MIN).getString(arrival / 60);
		} else if (arrival > 0) {
			return (isRealtime ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_SEC_CJK : TranslationProvider.GUI_MTR_ARRIVAL_SEC).getString(arrival);
		} else {
			return "";
		}
	}

	private void getArrivalsAndRender(T entity, BlockPos blockPos, Direction facing, LongCollection platformIds) {
		final ObjectArrayList<ArrivalResponse> arrivalResponseList = ArrivalsCacheClient.INSTANCE.requestArrivals(platformIds);
		MainRenderer.scheduleTextRender((matrixStack, offset) -> {
			render(entity, blockPos, facing, arrivalResponseList, matrixStack, offset);
			if (entity instanceof BlockPIDSHorizontalBase.BlockEntityHorizontalBase) {
				render(entity, blockPos.relative(facing), facing.getOpposite(), arrivalResponseList, matrixStack, offset);
			}
		});
	}

	private void render(T entity, BlockPos blockPos, Direction facing, ObjectArrayList<ArrivalResponse> arrivalResponseList, PoseStack matrixStack, Vec3 offset) {
		final float scale = 160 * entity.maxArrivals / maxHeight * textPadding;
		final boolean hasDifferentCarLengths = hasDifferentCarLengths(arrivalResponseList);
		final boolean isSingleArrival = entity instanceof BlockPIDSVerticalSingleArrival1.PIDSVerticalSingleArrival1BlockEntity;
		final int arrivalsPerPage = isSingleArrival ? 1 : (entity.alternateLines() ? entity.maxArrivals / 2 : entity.maxArrivals);
		int arrivalIndex = entity.getDisplayPage() * arrivalsPerPage;

		final Integer customColor = entity.getCustomColor();
		final Color textColor = customColor == null ? new Color(entity.textColor()) : new Color(customColor);
		final Color textColorArrived = entity.textColorArrived() == entity.textColor() ? textColor : new Color(entity.textColorArrived());

		for (int i = 0; i < entity.maxArrivals; i++) {
			final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
			final ArrivalResponse arrivalResponse;
			final String customMessage = entity.getMessage(i);
			final String[] destinationSplit;
			final String[] customMessageSplit = customMessage.split("\\|");
			final boolean renderCustomMessage;
			final int languageIndex;

			if (entity.getHideArrival(i)) {
				if (customMessage.isEmpty()) {
					continue;
				}
				arrivalResponse = null;
				destinationSplit = new String[0];
				renderCustomMessage = true;
				languageIndex = languageTicks % customMessageSplit.length;
			} else {
				arrivalResponse = Utilities.getElement(arrivalResponseList, arrivalIndex);
				if (arrivalResponse == null) {
					if (customMessage.isEmpty() || customMessageSplit.length == 0) {
						continue;
					}
					destinationSplit = new String[0];
					renderCustomMessage = true;
					languageIndex = languageTicks % customMessageSplit.length;
				} else {
					final String[] tempDestinationSplit = arrivalResponse.getDestination().split("\\|");
					if (arrivalResponse.getRouteNumber().isEmpty()) {
						destinationSplit = tempDestinationSplit;
					} else {
						final String[] tempNumberSplit = arrivalResponse.getRouteNumber().split("\\|");
						int destinationIndex = 0;
						int numberIndex = 0;
						final ObjectArrayList<String> newDestinations = new ObjectArrayList<>();
						while (true) {
							final String newDestination = String.format("%s %s", tempNumberSplit[numberIndex % tempNumberSplit.length], tempDestinationSplit[destinationIndex % tempDestinationSplit.length]);
							if (newDestinations.contains(newDestination)) {
								break;
							} else {
								newDestinations.add(newDestination);
							}
							destinationIndex++;
							numberIndex++;
						}
						destinationSplit = newDestinations.toArray(new String[0]);
					}
					final int messageCount = destinationSplit.length + (customMessage.isEmpty() ? 0 : customMessageSplit.length);
					renderCustomMessage = languageTicks % messageCount >= destinationSplit.length;
					languageIndex = (languageTicks % messageCount) - (renderCustomMessage ? destinationSplit.length : 0);
					if (!isSingleArrival && (!entity.alternateLines() || i % 2 == 1)) {
						arrivalIndex++;
					}
				}
			}

			matrixStack.pushPose();
			matrixStack.translate(blockPos.getX() - offset.x + 0.5, blockPos.getY() - offset.y, blockPos.getZ() - offset.z + 0.5);
			Drawing.rotateYDegrees(matrixStack, (rotate90 ? 90 : 0) - facing.toYRot());
			Drawing.rotateZDegrees(matrixStack, 180);
			matrixStack.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / entity.maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
			matrixStack.scale(1 / scale, 1 / scale, 1 / scale);

			if (renderCustomMessage) {
				renderText(matrixStack, customMessageSplit[languageIndex], textColor, maxWidth * scale / 16, HorizontalAlignment.LEFT);
			} else {
				final long arrival = (arrivalResponse.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000;
				final Color color = arrival <= 0 ? textColorArrived : textColor;
				final String destination = destinationSplit[languageIndex];
				final boolean isCjk = IGui.isCjk(destination);
				final String destinationFormatted = switch (arrivalResponse.getCircularState()) {
					case CLOCKWISE -> (isCjk ? TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_CLOCKWISE_VIA).getString(destination);
					case ANTICLOCKWISE -> (isCjk ? TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA).getString(destination);
					default -> destination;
				};

				final String carLengthString = (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_CAR_CJK : TranslationProvider.GUI_MTR_ARRIVAL_CAR).getString(arrivalResponse.getCarCount());
				final String arrivalString = getArrivalString(arrival, arrivalResponse.getRealtime(), isCjk);

				if (isSingleArrival) {
					if (i == 0) {
						final float halfWidth = maxWidth * scale / 16 / 2;
						renderText(matrixStack, arrivalString, color, halfWidth, HorizontalAlignment.LEFT);
						matrixStack.translate(halfWidth, 0, 0);
						renderText(matrixStack, (isCjk ? TranslationProvider.GUI_MTR_PLATFORM_ABBREVIATED_CJK : TranslationProvider.GUI_MTR_PLATFORM_ABBREVIATED).getString(arrivalResponse.getPlatformName()), color, halfWidth, HorizontalAlignment.RIGHT);
					} else if (i == 1) {
						renderText(matrixStack, destinationFormatted, color, maxWidth * scale / 16, HorizontalAlignment.LEFT);
					} else if (i == 3) {
						final SimplifiedRoute simplifiedRoute = MinecraftClientData.getInstance().simplifiedRouteIdMap.get(arrivalResponse.getRouteId());
						final ObjectArrayList<SimplifiedRoutePlatform> stations = new ObjectArrayList<>();
						if (simplifiedRoute != null) {
							for (int j = simplifiedRoute.getPlatformIndex(arrivalResponse.getPlatformId()) + 1; j < simplifiedRoute.getPlatforms().size(); j++) {
								stations.add(simplifiedRoute.getPlatforms().get(j));
							}
						}

						final ObjectArrayList<String> lines = new ObjectArrayList<>();

						if (stations.isEmpty()) {
							lines.addAll(wrapLines((isCjk ? TranslationProvider.GUI_MTR_TERMINATES_HERE_CJK : TranslationProvider.GUI_MTR_TERMINATES_HERE).getString(), maxWidth * scale / 16));
						} else {
							final int callingAtMaxPages = (int) Math.max(Math.ceil(stations.size() / (float) STATIONS_PER_PAGE), 1);
							final int callingAtPage = callingAtMaxPages == 1 ? 0 : (int) Math.floor(MTRClient.getGameTick() / SWITCH_PAGE_TICKS) % callingAtMaxPages;
							lines.add((isCjk ? TranslationProvider.GUI_MTR_CALLING_AT_CJK : TranslationProvider.GUI_MTR_CALLING_AT).getString(callingAtPage + 1, callingAtMaxPages));
							for (int j = 0; j < STATIONS_PER_PAGE; j++) {
								final SimplifiedRoutePlatform simplifiedRoutePlatform = Utilities.getElement(stations, j + callingAtPage * STATIONS_PER_PAGE);
								if (simplifiedRoutePlatform != null) {
									final String[] stationNameSplit = simplifiedRoutePlatform.getStationName().split("\\|");
									lines.add(stationNameSplit[languageTicks % stationNameSplit.length]);
								}
							}
						}

						lines.forEach(line -> {
							renderText(matrixStack, line, color, maxWidth * scale / 16, stations.isEmpty() ? HorizontalAlignment.CENTER : HorizontalAlignment.LEFT);
							matrixStack.translate(0, maxHeight * scale / entity.maxArrivals / 16, 0);
						});
					} else if (i == 15) {
						renderText(matrixStack, carLengthString, Color.RED, maxWidth * scale / 16, HorizontalAlignment.RIGHT);
					}
				} else {
					if (entity.alternateLines()) {
						if (i % 2 == 0) {
							renderText(matrixStack, destinationFormatted, color, maxWidth * scale / 16, HorizontalAlignment.LEFT);
						} else {
							if (hasDifferentCarLengths) {
								renderText(matrixStack, carLengthString, Color.RED, 32, HorizontalAlignment.LEFT);
								matrixStack.translate(32, 0, 0);
							}
							renderText(matrixStack, arrivalString, color, maxWidth * scale / 16 - (hasDifferentCarLengths ? 32 : 0), HorizontalAlignment.RIGHT);
						}
					} else {
						final boolean showPlatformNumber = entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase;

						if (entity.showArrivalNumber()) {
							renderText(matrixStack, String.valueOf(arrivalIndex), color, 12, HorizontalAlignment.LEFT);
							matrixStack.translate(12, 0, 0);
						}

						final float destinationWidth = maxWidth * scale / 16 - 40 - (hasDifferentCarLengths || showPlatformNumber ? showPlatformNumber ? 16 : 32 : 0) - (entity.showArrivalNumber() ? 12 : 0);
						renderText(matrixStack, destinationFormatted, color, destinationWidth, HorizontalAlignment.LEFT);
						matrixStack.translate(destinationWidth, 0, 0);

						if (hasDifferentCarLengths || showPlatformNumber) {
							if (showPlatformNumber) {
								renderText(matrixStack, arrivalResponse.getPlatformName(), color, 16, HorizontalAlignment.LEFT);
								matrixStack.translate(16, 0, 0);
							} else {
								renderText(matrixStack, carLengthString, Color.RED, 32, HorizontalAlignment.LEFT);
								matrixStack.translate(32, 0, 0);
							}
						}

						renderText(matrixStack, arrivalString, color, 40, HorizontalAlignment.RIGHT);
					}
				}
			}

			matrixStack.popPose();
		}
	}

	private void renderText(PoseStack matrixStack, String text, Color color, float availableWidth, HorizontalAlignment horizontalAlignment) {
		matrixStack.pushPose();
		final Font textRenderer = Minecraft.getInstance().font;
		final int textWidth = textRenderer.width(text);
		if (availableWidth < textWidth) {
			matrixStack.scale(textWidth == 0 ? 1 : availableWidth / textWidth, 1, 1);
		}
		renderText(matrixStack, text, (int) horizontalAlignment.getOffset(0, textWidth - availableWidth), 0, color);
		matrixStack.popPose();
	}

	private static boolean hasDifferentCarLengths(ObjectArrayList<ArrivalResponse> arrivalResponseList) {
		int carCount = 0;
		for (final ArrivalResponse arrivalResponse : arrivalResponseList) {
			final int currentCarCount = arrivalResponse.getCarCount();
			if (carCount > 0 && currentCarCount != carCount) {
				return true;
			}
			carCount = currentCarCount;
		}
		return false;
	}

	private static ObjectArrayList<String> wrapLines(String text, float availableWidth) {
		final Font textRenderer = Minecraft.getInstance().font;
		final ObjectArrayList<String> lines = new ObjectArrayList<>();
		final String[] textSplit = text.split("\\s");
		String tempText = "";

		for (final String textPart : textSplit) {
			final String newText = tempText + " " + textPart;
			if (!tempText.isEmpty() && textRenderer.width(newText) > availableWidth) {
				lines.add(tempText);
				tempText = textPart;
			} else {
				tempText = newText;
			}
		}

		lines.add(tempText);
		return lines;
	}
}
