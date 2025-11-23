package org.mtr.render;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.Drawing;

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
	public final void render(T blockEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos blockPos = blockEntity.getPos();
		if (!blockEntity.canStoreData.test(world, blockPos)) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, Properties.HORIZONTAL_FACING);

		if (blockEntity.getPlatformIds().isEmpty()) {
			final LongArrayList platformIds = new LongArrayList();
			if (blockEntity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase) {
				final Station station = MTRClient.findStation(blockPos);
				if (station != null) {
					station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
				}
			} else {
				MTRClient.findClosePlatform(blockEntity.getPos().down(4), 5, platform -> platformIds.add(platform.getId()));
			}
			getArrivalsAndRender(blockEntity, blockPos, facing, platformIds);
		} else {
			getArrivalsAndRender(blockEntity, blockPos, facing, blockEntity.getPlatformIds());
		}
	}

	public void renderText(MatrixStack matrixStack, String text, int x, int y, int color) {
		FontGroupRegistry.MINECRAFT.get().render(new Drawing(matrixStack, RenderLayer.getGui()), text, FontRenderOptions.builder().offsetX(x).offsetY(y).color(color).build());
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
		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (matrixStack, vertexConsumer, offset) -> {
			render(entity, blockPos, facing, arrivalResponseList, matrixStack, vertexConsumer, offset);
			if (entity instanceof BlockPIDSHorizontalBase.BlockEntityHorizontalBase) {
				render(entity, blockPos.offset(facing), facing.getOpposite(), arrivalResponseList, matrixStack, vertexConsumer, offset);
			}
		});
	}

	private void render(T entity, BlockPos blockPos, Direction facing, ObjectArrayList<ArrivalResponse> arrivalResponseList, MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d offset) {
		final float scale = 160 * entity.maxArrivals / maxHeight * textPadding;
		final boolean hasDifferentCarLengths = hasDifferentCarLengths(arrivalResponseList);
		final boolean isSingleArrival = entity instanceof BlockPIDSVerticalSingleArrival1.PIDSVerticalSingleArrival1BlockEntity;
		final int arrivalsPerPage = isSingleArrival ? 1 : entity.alternateLines() ? entity.maxArrivals / 2 : entity.maxArrivals;
		int arrivalIndex = entity.getDisplayPage() * arrivalsPerPage;

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

			matrixStack.push();
			matrixStack.translate(blockPos.getX() - offset.x + 0.5, blockPos.getY() - offset.y, blockPos.getZ() - offset.z + 0.5);
			Drawing.rotateYDegrees(matrixStack, (rotate90 ? 90 : 0) - facing.getPositiveHorizontalDegrees());
			Drawing.rotateZDegrees(matrixStack, 180);
			matrixStack.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / entity.maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
			matrixStack.scale(1 / scale, 1 / scale, 1 / scale);

			if (renderCustomMessage) {
				renderText(matrixStack, customMessageSplit[languageIndex], entity.textColor(), maxWidth * scale / 16, HorizontalAlignment.LEFT);
			} else {
				final long arrival = (arrivalResponse.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000;
				final int color = arrival <= 0 ? entity.textColorArrived() : entity.textColor();
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
						renderText(matrixStack, carLengthString, 0xFF0000, maxWidth * scale / 16, HorizontalAlignment.RIGHT);
					}
				} else {
					if (entity.alternateLines()) {
						if (i % 2 == 0) {
							renderText(matrixStack, destinationFormatted, color, maxWidth * scale / 16, HorizontalAlignment.LEFT);
						} else {
							if (hasDifferentCarLengths) {
								renderText(matrixStack, carLengthString, 0xFF0000, 32, HorizontalAlignment.LEFT);
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
								renderText(matrixStack, carLengthString, 0xFF0000, 32, HorizontalAlignment.LEFT);
								matrixStack.translate(32, 0, 0);
							}
						}

						renderText(matrixStack, arrivalString, color, 40, HorizontalAlignment.RIGHT);
					}
				}
			}

			matrixStack.pop();
		}
	}

	private void renderText(MatrixStack matrixStack, String text, int color, float availableWidth, HorizontalAlignment horizontalAlignment) {
		matrixStack.push();
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		final int textWidth = textRenderer.getWidth(text);
		if (availableWidth < textWidth) {
			matrixStack.scale(textWidth == 0 ? 1 : availableWidth / textWidth, 1, 1);
		}
		renderText(matrixStack, text, (int) horizontalAlignment.getOffset(0, textWidth - availableWidth), 0, color | ARGB_BLACK);
		matrixStack.pop();
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
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		final ObjectArrayList<String> lines = new ObjectArrayList<>();
		final String[] textSplit = text.split("\\s");
		String tempText = "";

		for (final String textPart : textSplit) {
			final String newText = tempText + " " + textPart;
			if (!tempText.isEmpty() && textRenderer.getWidth(newText) > availableWidth) {
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
