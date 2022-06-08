package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.block.BlockArrivalProjectorBase;
import mtr.block.BlockPIDSBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.*;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import java.util.*;

public class RenderPIDS<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {

	private final float scale;
	private final float totalScaledWidth;
	private final float destinationStart;
	private final float destinationMaxWidth;
	private final float platformMaxWidth;
	private final float arrivalMaxWidth;
	private final int maxArrivals;
	private final float maxHeight;
	private final float startX;
	private final float startY;
	private final float startZ;
	private final boolean rotate90;
	private final boolean renderArrivalNumber;
	private final boolean showAllPlatforms;
	private final int textColor;
	private final int firstTrainColor;
	private final boolean appendDotAfterMin;

	public static final int MAX_VIEW_DISTANCE = 16;
	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int CAR_TEXT_COLOR = 0xFF0000;

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, boolean showAllPlatforms, int textColor, int firstTrainColor, float textPadding, boolean appendDotAfterMin) {
		super(dispatcher);
		scale = 160 * maxArrivals / maxHeight * textPadding;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = totalScaledWidth * 0.7F;
		platformMaxWidth = showAllPlatforms ? scale * 2 / 16 : 0;
		arrivalMaxWidth = totalScaledWidth - destinationStart - destinationMaxWidth - platformMaxWidth;
		this.maxArrivals = maxArrivals;
		this.maxHeight = maxHeight;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.rotate90 = rotate90;
		this.renderArrivalNumber = renderArrivalNumber;
		this.showAllPlatforms = showAllPlatforms;
		this.textColor = textColor;
		this.firstTrainColor = firstTrainColor;
		this.appendDotAfterMin = appendDotAfterMin;
	}

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, boolean showAllPlatforms, int textColor, int firstTrainColor) {
		this(dispatcher, maxArrivals, startX, startY, startZ, maxHeight, maxWidth, rotate90, renderArrivalNumber, showAllPlatforms, textColor, firstTrainColor, 1, false);
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final BlockGetter world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, Math.min(MAX_VIEW_DISTANCE, RenderTrains.maxTrainRenderDistance), rotate90 ? null : facing)) {
			return;
		}

		final String[] customMessages = new String[maxArrivals];
		final boolean[] hideArrival = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			if (entity instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				customMessages[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getMessage(i);
				hideArrival[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getHideArrival(i);
			} else {
				customMessages[i] = "";
			}
		}

		try {
			final Set<ScheduleEntry> schedules;
			final Map<Long, String> platformIdToName = new HashMap<>();

			if (showAllPlatforms) {
				final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
				if (station == null) {
					return;
				}

				final Map<Long, Platform> platforms = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
				if (platforms.isEmpty()) {
					return;
				}

				final Set<Long> platformIds;
				if (entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) {
					platformIds = ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity).getPlatformIds();
				} else {
					platformIds = new HashSet<>();
				}

				schedules = new HashSet<>();
				platforms.values().forEach(platform -> {
					if (platformIds.isEmpty() || platformIds.contains(platform.id)) {
						final Set<ScheduleEntry> scheduleForPlatform = ClientData.SCHEDULES_FOR_PLATFORM.get(platform.id);
						if (scheduleForPlatform != null) {
							scheduleForPlatform.forEach(scheduleEntry -> {
								final Route route = ClientData.DATA_CACHE.routeIdMap.get(scheduleEntry.routeId);
								if (route != null && scheduleEntry.currentStationIndex < route.platformIds.size() - 1) {
									schedules.add(scheduleEntry);
									platformIdToName.put(platform.id, platform.name);
								}
							});
						}
					}
				});
			} else {
				final long platformId = RailwayData.getClosePlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE, pos);
				if (platformId == 0) {
					schedules = new HashSet<>();
				} else {
					final Set<ScheduleEntry> schedulesForPlatform = ClientData.SCHEDULES_FOR_PLATFORM.get(platformId);
					schedules = schedulesForPlatform == null ? new HashSet<>() : schedulesForPlatform;
				}
			}

			final List<ScheduleEntry> scheduleList = new ArrayList<>(schedules);
			Collections.sort(scheduleList);

			final boolean showCarLength;
			final float carLengthMaxWidth;
			if (!showAllPlatforms) {
				int maxCars = 0;
				int minCars = Integer.MAX_VALUE;
				for (final ScheduleEntry scheduleEntry : scheduleList) {
					final int trainCars = scheduleEntry.trainCars;
					if (trainCars > maxCars) {
						maxCars = trainCars;
					}
					if (trainCars < minCars) {
						minCars = trainCars;
					}
				}
				showCarLength = minCars != maxCars;
				carLengthMaxWidth = showCarLength ? scale * 6 / 16 : 0;
			} else {
				showCarLength = false;
				carLengthMaxWidth = 0;
			}

			for (int i = 0; i < maxArrivals; i++) {
				final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
				final String destinationString;
				final boolean useCustomMessage;
				final ScheduleEntry currentSchedule = i < scheduleList.size() ? scheduleList.get(i) : null;
				final Route route = currentSchedule == null ? null : ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);

				if (i < scheduleList.size() && !hideArrival[i] && route != null) {
					final String[] destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "").split("\\|");
					final boolean isLightRailRoute = route.isLightRailRoute;
					final String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");

					if (customMessages[i].isEmpty()) {
						destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
						useCustomMessage = false;
					} else {
						final String[] customMessageSplit = customMessages[i].split("\\|");
						final int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
						final int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);

						if (indexToUse < destinationMaxIndex) {
							destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
							useCustomMessage = false;
						} else {
							destinationString = customMessageSplit[indexToUse - destinationMaxIndex];
							useCustomMessage = true;
						}
					}
				} else {
					final String[] destinationSplit = customMessages[i].split("\\|");
					destinationString = destinationSplit[languageTicks % destinationSplit.length];
					useCustomMessage = true;
				}

				matrices.pushPose();
				matrices.translate(0.5, 0, 0.5);
				matrices.mulPose(Vector3f.YP.rotationDegrees((rotate90 ? 90 : 0) - facing.toYRot()));
				matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
				matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
				matrices.scale(1F / scale, 1F / scale, 1F / scale);

				final Font textRenderer = Minecraft.getInstance().font;

				if (useCustomMessage) {
					final int destinationWidth = textRenderer.width(destinationString);
					if (destinationWidth > totalScaledWidth) {
						matrices.scale(totalScaledWidth / destinationWidth, 1, 1);
					}
					textRenderer.draw(matrices, destinationString, 0, 0, textColor);
				} else {
					final Component arrivalText;
					final int seconds = (int) ((currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000);
					final boolean isCJK = destinationString.codePoints().anyMatch(Character::isIdeographic);
					if (seconds >= 60) {
						arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60).append(appendDotAfterMin && !isCJK ? "." : "");
					} else {
						arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds).append(appendDotAfterMin && !isCJK ? "." : "") : null;
					}
					final Component carText = Text.translatable(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", currentSchedule.trainCars);


					if (renderArrivalNumber) {
						textRenderer.draw(matrices, String.valueOf(i + 1), 0, 0, seconds > 0 ? textColor : firstTrainColor);
					}

					final float newDestinationMaxWidth = destinationMaxWidth - carLengthMaxWidth;

					if (showAllPlatforms) {
						final String platformName = platformIdToName.get(route.platformIds.get(currentSchedule.currentStationIndex));
						if (platformName != null) {
							textRenderer.draw(matrices, platformName, destinationStart + newDestinationMaxWidth, 0, seconds > 0 ? textColor : firstTrainColor);
						}
					}

					if (showCarLength) {
						matrices.pushPose();
						matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth, 0, 0);
						final int carTextWidth = textRenderer.width(carText);
						if (carTextWidth > carLengthMaxWidth) {
							matrices.scale(carLengthMaxWidth / carTextWidth, 1, 1);
						}
						textRenderer.draw(matrices, carText, 0, 0, CAR_TEXT_COLOR);
						matrices.popPose();
					}

					matrices.pushPose();
					matrices.translate(destinationStart, 0, 0);
					final int destinationWidth = textRenderer.width(destinationString);
					if (destinationWidth > newDestinationMaxWidth) {
						matrices.scale(newDestinationMaxWidth / destinationWidth, 1, 1);
					}
					textRenderer.draw(matrices, destinationString, 0, 0, seconds > 0 ? textColor : firstTrainColor);
					matrices.popPose();

					if (arrivalText != null) {
						matrices.pushPose();
						final int arrivalWidth = textRenderer.width(arrivalText);
						if (arrivalWidth > arrivalMaxWidth) {
							matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth + carLengthMaxWidth, 0, 0);
							matrices.scale(arrivalMaxWidth / arrivalWidth, 1, 1);
						} else {
							matrices.translate(totalScaledWidth - arrivalWidth, 0, 0);
						}
						textRenderer.draw(matrices, arrivalText, 0, 0, textColor);
						matrices.popPose();
					}
				}

				matrices.popPose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}