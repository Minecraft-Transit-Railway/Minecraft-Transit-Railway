package org.mtr.render;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTRClient;
import org.mtr.block.BlockArrivalProjectorBase;
import org.mtr.block.BlockPIDSBase;
import org.mtr.block.BlockPIDSHorizontalBase;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Station;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.data.ArrivalsCacheClient;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

public class RenderPIDS<T extends BlockPIDSBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, Utilities {

	private final float startX;
	private final float startY;
	private final float startZ;
	private final float maxHeight;
	private final float maxWidth;
	private final boolean rotate90;
	private final float textPadding;

	public static final int SWITCH_LANGUAGE_TICKS = 60;

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
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos blockPos = entity.getPos();
		if (!entity.canStoreData.test(world, blockPos)) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, Properties.FACING);

		if (entity.getPlatformIds().isEmpty()) {
			final LongArrayList platformIds = new LongArrayList();
			if (entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase) {
				final Station station = MTRClient.findStation(blockPos);
				if (station != null) {
					station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
				}
			} else {
				MTRClient.findClosePlatform(entity.getPos().down(4), 5, platform -> platformIds.add(platform.getId()));
			}
			getArrivalsAndRender(entity, blockPos, facing, platformIds);
		} else {
			getArrivalsAndRender(entity, blockPos, facing, entity.getPlatformIds());
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
		int arrivalIndex = entity.getDisplayPage() * entity.maxArrivals;

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
					if (!entity.alternateLines() || i % 2 == 1) {
						arrivalIndex++;
					}
				}
			}

			matrixStack.push();
			matrixStack.translate(blockPos.getX() - offset.x + 0.5, blockPos.getY() - offset.y, blockPos.getZ() - offset.z + 0.5);
			IDrawing.rotateYDegrees(matrixStack, (rotate90 ? 90 : 0) - facing.getPositiveHorizontalDegrees());
			IDrawing.rotateZDegrees(matrixStack, 180);
			matrixStack.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / entity.maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
			matrixStack.scale(1 / scale, 1 / scale, 1 / scale);

			if (renderCustomMessage) {
				renderText(matrixStack, vertexConsumer, customMessageSplit[languageIndex], entity.textColor(), maxWidth * scale / 16, false);
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
				final String arrivalString;

				if (arrival >= 60) {
					arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_MIN_CJK : TranslationProvider.GUI_MTR_ARRIVAL_MIN).getString(arrival / 60);
				} else if (arrival > 0) {
					arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_SEC_CJK : TranslationProvider.GUI_MTR_ARRIVAL_SEC).getString(arrival);
				} else {
					arrivalString = "";
				}

				if (entity.alternateLines()) {
					if (i % 2 == 0) {
						renderText(matrixStack, vertexConsumer, destinationFormatted, color, maxWidth * scale / 16, false);
					} else {
						if (hasDifferentCarLengths) {
							renderText(matrixStack, vertexConsumer, carLengthString, 0xFF0000, 32, false);
							matrixStack.translate(32, 0, 0);
						}
						renderText(matrixStack, vertexConsumer, arrivalString, color, maxWidth * scale / 16 - (hasDifferentCarLengths ? 32 : 0), true);
					}
				} else {
					final boolean showPlatformNumber = entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase;

					if (entity.showArrivalNumber()) {
						renderText(matrixStack, vertexConsumer, String.valueOf(arrivalIndex), color, 12, false);
						matrixStack.translate(12, 0, 0);
					}

					final float destinationWidth = maxWidth * scale / 16 - 40 - (hasDifferentCarLengths || showPlatformNumber ? showPlatformNumber ? 16 : 32 : 0) - (entity.showArrivalNumber() ? 12 : 0);
					renderText(matrixStack, vertexConsumer, destinationFormatted, color, destinationWidth, false);
					matrixStack.translate(destinationWidth, 0, 0);

					if (hasDifferentCarLengths || showPlatformNumber) {
						if (showPlatformNumber) {
							renderText(matrixStack, vertexConsumer, arrivalResponse.getPlatformName(), color, 16, false);
							matrixStack.translate(16, 0, 0);
						} else {
							renderText(matrixStack, vertexConsumer, carLengthString, 0xFF0000, 32, false);
							matrixStack.translate(32, 0, 0);
						}
					}

					renderText(matrixStack, vertexConsumer, arrivalString, color, 40, true);
				}
			}

			matrixStack.pop();
		}
	}

	private static void renderText(MatrixStack matrixStack, VertexConsumer vertexConsumer, String text, int color, float availableWidth, boolean rightAlign) {
		matrixStack.push();
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		final int textWidth = textRenderer.getWidth(text);
		if (availableWidth < textWidth) {
			matrixStack.scale(textWidth == 0 ? 1 : availableWidth / textWidth, 1, 1);
		}
//		textRenderer.drawText(textRenderer, text, rightAlign ? Math.max(0, (int) availableWidth - textWidth) : 0, 0, color | ARGB_BLACK, false, DEFAULT_LIGHT);
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
}
