package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTRClient;
import mtr.block.BlockArrivalProjectorBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.*;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.*;

import static mtr.block.IBlock.HALF;

public class RenderPIDS<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {

	private final float scale;
	private final float totalScaledWidth;
	private final float destinationStart;
	private final float destinationMaxWidth;
	private final float platformMaxWidth;
	private final float arrivalMaxWidth;
	private final int maxArrivals;
	private final int linesPerArrival;
	private final float maxHeight;
	private final float startX;
	private final float startY;
	private final float startZ;
	private final boolean rotate90;
	private final boolean renderArrivalNumber;
	private final PIDSType renderType;
	private final int textColor;
	private final int firstTrainColor;
	private final boolean appendDotAfterMin;

	public static final int MAX_VIEW_DISTANCE = 16;
	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int CAR_TEXT_COLOR = 0xFF0000;

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, int linesPerArrival, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, PIDSType renderType, int textColor, int firstTrainColor, float textPadding, boolean appendDotAfterMin) {
		super(dispatcher);
		scale = 160 * (maxArrivals * linesPerArrival) / maxHeight * textPadding;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = renderType == PIDSType.PIDS_VERTICAL ? totalScaledWidth : totalScaledWidth * 0.7F;
		platformMaxWidth = renderType.showPlatformNumber ? scale * 2 / 16 : 0;
		arrivalMaxWidth = renderType == PIDSType.PIDS_VERTICAL ? totalScaledWidth *  8 / 16 : totalScaledWidth - destinationStart - destinationMaxWidth - platformMaxWidth;
		this.maxArrivals = maxArrivals;
		this.linesPerArrival = linesPerArrival;
		this.maxHeight = maxHeight;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.rotate90 = rotate90;
		this.renderArrivalNumber = renderArrivalNumber;
		this.renderType = renderType;
		this.textColor = textColor;
		this.firstTrainColor = firstTrainColor;
		this.appendDotAfterMin = appendDotAfterMin;
	}

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, int linesPerArrival, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, PIDSType renderType, int textColor, int firstTrainColor) {
		this(dispatcher, maxArrivals, linesPerArrival, startX, startY, startZ, maxHeight, maxWidth, rotate90, renderArrivalNumber, renderType, textColor, firstTrainColor, 1, false);
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
		if (IBlock.getStatePropertySafe(entity.getBlockState(), HALF) == DoubleBlockHalf.LOWER) {
			return;
		}

		final String[] customMessages = new String[maxArrivals * linesPerArrival];
		final boolean[] hideArrival = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals * linesPerArrival; i++) {
			if (entity instanceof IPIDS.TileEntityPIDS) {
				customMessages[i] = ((IPIDS.TileEntityPIDS) entity).getMessage(i);
			} else {
				customMessages[i] = "";
			}
		}
		for (int i = 0; i < maxArrivals; i++) {
			if (entity instanceof IPIDS.TileEntityPIDS) {
				hideArrival[i] = ((IPIDS.TileEntityPIDS) entity).getHideArrival(i);
			}
		}

		try {
			final Map<Long, String> platformIdToName = new HashMap<>();
			final List<ScheduleEntry> scheduleList = getSchedules(entity, pos, platformIdToName);

			final boolean showCarLength;
			final float carLengthMaxWidth;
			if (renderType.showCarCount) {
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

			final int displayPageOffset = entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase ? ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity).getDisplayPage() * maxArrivals : 0;

			for (int i = 0; i < maxArrivals * linesPerArrival; i++) {
				final boolean arrivalLine = i % linesPerArrival == 0;
				final int arrivalNum = (int) Math.floor(i / (float) linesPerArrival);

				final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
				final String destinationString;
				final boolean useCustomMessage;
				final ScheduleEntry currentSchedule = arrivalNum + displayPageOffset < scheduleList.size() ? scheduleList.get(arrivalNum + displayPageOffset) : null;
				final Route route = currentSchedule == null ? null : ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);

				if (arrivalNum < scheduleList.size() && !hideArrival[arrivalNum] && route != null) {
					final String[] destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "").split("\\|");
					final boolean isLightRailRoute = route.isLightRailRoute;
					final String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");

					if (customMessages[i].isEmpty()) {
						if (arrivalLine) destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
						else destinationString = "";
						useCustomMessage = false;
					} else {
						final String[] customMessageSplit = customMessages[i].split("\\|");
						final int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
						final int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);

						if (indexToUse < destinationMaxIndex) {
							if (arrivalLine) destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
							else destinationString = "";
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
				UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
				UtilitiesClient.rotateZDegrees(matrices, 180);
				matrices.translate((startX - 8) / 16, -startY / 16 + (i / (float) linesPerArrival) * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
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
					final boolean isCJK = IGui.isCjk(destinationString);
					if (seconds >= 60) {
						if (!arrivalLine || renderType != PIDSType.PIDS_VERTICAL) arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60).append(appendDotAfterMin && !isCJK ? "." : "");
						else arrivalText = Text.literal("");
					} else {
						if (!arrivalLine || renderType != PIDSType.PIDS_VERTICAL) arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds).append(appendDotAfterMin && !isCJK ? "." : "") : null;
						else arrivalText = Text.literal("");
					}
					final Component carText;
					if (!arrivalLine || renderType != PIDSType.PIDS_VERTICAL) carText = Text.translatable(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", currentSchedule.trainCars);
					else carText = Text.literal("");

					if (renderArrivalNumber) {
						textRenderer.draw(matrices, String.valueOf(i + 1), 0, 0, seconds > 0 ? textColor : firstTrainColor);
					}

					final float newDestinationMaxWidth = destinationMaxWidth - (renderType == PIDSType.PIDS_VERTICAL ? 0 : carLengthMaxWidth);

					if (renderType.showPlatformNumber) {
						final String platformName = platformIdToName.get(route.platformIds.get(currentSchedule.currentStationIndex).platformId);
						if (platformName != null) {
							textRenderer.draw(matrices, platformName, destinationStart + newDestinationMaxWidth, 0, seconds > 0 ? textColor : firstTrainColor);
						}
					}

					if (showCarLength) {
						matrices.pushPose();
						matrices.translate(renderType == PIDSType.PIDS_VERTICAL ? destinationStart : (destinationStart + newDestinationMaxWidth + platformMaxWidth), 0, 0);
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
							matrices.translate(totalScaledWidth - arrivalMaxWidth, 0, 0);
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

	public List<ScheduleEntry> getSchedules(T entity, BlockPos pos, final Map<Long, String> platformIdToName) {
		final Set<ScheduleEntry> schedules;

		final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
		if (station == null) {
			return new ArrayList<>();
		}

		final Map<Long, Platform> platforms = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
		if (platforms.isEmpty()) {
			return new ArrayList<>();
		}

		final Set<Long> platformIds;
		switch (renderType) {
			case ARRIVAL_PROJECTOR:
				if (entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) {
					platformIds = ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity).getPlatformIds();
				} else {
					platformIds = new HashSet<>();
				}
				break;
			case PIDS:
            case PIDS_VERTICAL:
				final Set<Long> tempPlatformIds;
				if (entity instanceof IPIDS.TileEntityPIDS) {
					tempPlatformIds = ((IPIDS.TileEntityPIDS) entity).getPlatformIds();
				} else {
					tempPlatformIds = new HashSet<>();
				}
				platformIds = tempPlatformIds.isEmpty() ? Collections.singleton(entity instanceof IPIDS.TileEntityPIDS ? ((IPIDS.TileEntityPIDS) entity).getPlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE) : 0) : tempPlatformIds;
				break;
			default:
				platformIds = new HashSet<>();
		}

		schedules = new HashSet<>();
		platforms.values().forEach(platform -> {
			if (platformIds.isEmpty() || platformIds.contains(platform.id)) {
				final Set<ScheduleEntry> scheduleForPlatform = ClientData.SCHEDULES_FOR_PLATFORM.get(platform.id);
				if (scheduleForPlatform != null) {
					scheduleForPlatform.forEach(scheduleEntry -> {
						final Route route = ClientData.DATA_CACHE.routeIdMap.get(scheduleEntry.routeId);
						if (route != null && (renderType.showTerminatingPlatforms || scheduleEntry.currentStationIndex < route.platformIds.size() - 1)) {
							schedules.add(scheduleEntry);
							platformIdToName.put(platform.id, platform.name);
						}
					});
				}
			}
		});

		final List<ScheduleEntry> scheduleList = new ArrayList<>(schedules);
		Collections.sort(scheduleList);
		return scheduleList;
	}
}