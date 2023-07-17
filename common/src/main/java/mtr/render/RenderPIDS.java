package mtr.render;

import com.mojang.blaze3d.systems.RenderSystem;
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
	private final float callingAtMaxWidth;
	private final float callingAtStationMaxWidth;
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
	private static final int STATIONS_PER_PAGE = 10;
	private static final int SWITCH_PAGE_TICKS = 120;

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, int linesPerArrival, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, PIDSType renderType, int textColor, int firstTrainColor, float textPadding, boolean appendDotAfterMin) {
		super(dispatcher);
		scale = 160 * (maxArrivals * linesPerArrival) / maxHeight * textPadding;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		switch (renderType) {
			case PIDS_VERTICAL:
				destinationMaxWidth = totalScaledWidth;
				platformMaxWidth = 0;
				arrivalMaxWidth = totalScaledWidth * 8 / 16;
				break;
			case PIDS_SINGLE_ARRIVAL:
				destinationMaxWidth = totalScaledWidth;
				platformMaxWidth = totalScaledWidth * 8 / 16;
				arrivalMaxWidth = totalScaledWidth * 7 / 16;
				break;
			default:
				destinationMaxWidth = totalScaledWidth * 0.7F;
				platformMaxWidth = renderType.showPlatformNumber ? scale * 2 / 16 : 0;
				arrivalMaxWidth = totalScaledWidth - destinationStart - destinationMaxWidth - platformMaxWidth;
		}
		callingAtMaxWidth = totalScaledWidth;
		callingAtStationMaxWidth = totalScaledWidth;
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

		// Get PIDS position and determine if it should render arrivals
		final BlockPos pos = entity.getBlockPos();
		final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, Math.min(MAX_VIEW_DISTANCE, RenderTrains.maxTrainRenderDistance), rotate90 ? null : facing)) {
			return;
		}
		if (IBlock.getStatePropertySafe(entity.getBlockState(), HALF) == DoubleBlockHalf.LOWER) {
			return;
		}

		// Get PIDS custom messages and show arrivals
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

		final MatrixStackHolder matrixStackHolder = new MatrixStackHolder(matrices);

		try {
			final Map<Long, String> platformIdToName = new HashMap<>();
			final List<ScheduleEntry> scheduleList = getSchedules(entity, pos, platformIdToName);

			// Determine PIDS tri-state based on renderType
			final boolean renderClassic = renderType == PIDSType.PIDS || renderType == PIDSType.ARRIVAL_PROJECTOR;
			final boolean renderVertical = renderType == PIDSType.PIDS_VERTICAL;
			final boolean renderSingle = renderType == PIDSType.PIDS_SINGLE_ARRIVAL;

			// Determine if car length should be shown
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
				showCarLength = minCars != maxCars || renderSingle;
				carLengthMaxWidth = showCarLength ? scale * 6 / 16 : 0;
			} else {
				showCarLength = false;
				carLengthMaxWidth = 0;
			}

			final int displayPageOffset = entity instanceof IPIDSRenderChild ? ((IPIDSRenderChild) entity).getDisplayPage() * maxArrivals : 0;

			// Loop through all lines
			for (int j = 0; j < maxArrivals; j++) {
				int arrivalLine;
				// Get current schedule
				final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
				final ScheduleEntry currentSchedule = j + displayPageOffset < scheduleList.size() ? scheduleList.get(j + displayPageOffset) : null;
				final Route route = currentSchedule == null ? null : ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);

				final boolean isCJK;
				// Check if there is a custom message (to determine CJK translations)
				if (j < scheduleList.size() && !hideArrival[j] && route != null) {
					final String[] destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "").split("\\|");
					final boolean isLightRailRoute = route.isLightRailRoute;
					final String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");
					final String checkString;
					if (customMessages[j * linesPerArrival].isEmpty()) {
						checkString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
					} else {
						final String[] customMessageSplit = customMessages[j * linesPerArrival].split("\\|");
						final int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
						final int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);

						if (indexToUse < destinationMaxIndex) {
							checkString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
						} else {
							checkString = customMessageSplit[indexToUse - destinationMaxIndex];
						}
					}
					isCJK = IGui.isCjk(checkString);
				} else {
					isCJK = false;
				}
				for (int k = 0; k < linesPerArrival; k++) {
					final int i = j * linesPerArrival + k;
					arrivalLine = i % linesPerArrival;
					final int arrivalNum = (int) Math.floor(i / (float) linesPerArrival);

					// Switch language based on SWITCH_LANGUAGE_TICKS
					final String destinationString;
					final boolean useCustomMessage;

					// Get current schedule
					final List<Route.RoutePlatform> stations = route == null ? null : route.platformIds.subList(currentSchedule.currentStationIndex + 1, route.platformIds.size());
					final int callingAtMaxPages = stations == null || !renderSingle ? 1 : (int) Math.max(Math.ceil(stations.size() / (float) STATIONS_PER_PAGE), 1);
					final int callingAtPage = callingAtMaxPages == 1 ? 0 : (int) Math.floor(MTRClient.getGameTick() / (float) SWITCH_PAGE_TICKS) % callingAtMaxPages;

					// Check if arrival number exists
					if (arrivalNum < scheduleList.size() && !hideArrival[arrivalNum] && route != null) {
						final String[] destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "").split("\\|");
						final boolean isLightRailRoute = route.isLightRailRoute;
						final String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");

						// Check if there is a custom message to be shown
						if (customMessages[i].isEmpty()) {
							if ((arrivalLine == 0 && !renderSingle) || (arrivalLine == 1 && renderSingle)) {
								destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
							} else {
								destinationString = "";
							}
							useCustomMessage = false;
						} else {
							final String[] customMessageSplit = customMessages[i].split("\\|");
							final int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
							final int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);

							if (indexToUse < destinationMaxIndex) {
								if ((arrivalLine == 0 && !renderSingle) || (arrivalLine == 1 && renderSingle)) {
									destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
								} else {
									destinationString = "";
								}
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

					// Translate the rendering matrix to the correct position
					matrixStackHolder.push();
					matrices.translate(0.5, 0, 0.5);
					UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
					UtilitiesClient.rotateZDegrees(matrices, 180);
					matrices.translate((startX - 8) / 16, -startY / 16 + (i / (float) linesPerArrival) * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
					matrices.scale(1F / scale, 1F / scale, 1F / scale);

					// Get text renderer
					final Font textRenderer = Minecraft.getInstance().font;

					if (useCustomMessage) {
						// Render custom message
						final int destinationWidth = textRenderer.width(destinationString);
						if (destinationWidth > totalScaledWidth) {
							matrices.scale(totalScaledWidth / destinationWidth, 1, 1);
						}
						textRenderer.drawInBatch(destinationString, 0, 0, textColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
					} else {
						// Render arrival
						final Component arrivalText;
						// Get arrival time
						final int seconds = (int) ((currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000);
						if (seconds >= 60) {
							if ((arrivalLine == 1 && renderVertical) || (arrivalLine == 0 && renderSingle) || renderClassic) {
								arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60).append(appendDotAfterMin && !isCJK ? "." : "");
							} else {
								arrivalText = Text.literal("");
							}
						} else {
							if ((arrivalLine == 1 && renderVertical) || (arrivalLine == 0 && renderSingle) || renderClassic) {
								arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds).append(appendDotAfterMin && !isCJK ? "." : "") : null;
							} else {
								arrivalText = Text.literal("");
							}
						}

						// Get car length text
						final Component carText;
						if ((arrivalLine == 1 && renderVertical) || (arrivalLine == 15 && renderSingle) || renderClassic) {
							carText = Text.translatable(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", currentSchedule.trainCars);
						} else {
							carText = Text.literal("");
						}

						// Get calling at text
						final Component callingAtText;
						if (renderSingle && arrivalLine == 3) {
							if (stations.size() > 0) {
								callingAtText = Text.translatable(isCJK ? "gui.mtr.calling_at_cjk" : "gui.mtr.calling_at", callingAtPage + 1, callingAtMaxPages);
							} else {
								callingAtText = Text.translatable(isCJK ? "gui.mtr.terminates_here_cjk" : "gui.mtr.terminates_here_1");
							}
						} else {
							callingAtText = Text.literal("");
						}

						// Get calling at station
						final String callingAtStationText;
						final int callingAtStationNumber = arrivalLine - 4 + callingAtPage * STATIONS_PER_PAGE;
						if (renderSingle && arrivalLine >= 4 && arrivalLine < 14 && callingAtStationNumber < stations.size()) {
							final Station station = ClientData.DATA_CACHE.platformIdToStation.get(stations.get(callingAtStationNumber).platformId);
							final String[] callingAtStationTextSplit = station == null ? new String[]{""} : station.name.split("\\|");
							final int indexToUse = languageTicks % callingAtStationTextSplit.length;
							callingAtStationText = callingAtStationTextSplit[indexToUse];
						} else {
							if (renderSingle && stations.size() == 0 && !isCJK && arrivalLine == 4) {
								callingAtStationText = Text.translatable("gui.mtr.terminates_here_2").getString();
							} else if (renderSingle && stations.size() == 0 && !isCJK && arrivalLine == 5) {
								callingAtStationText = Text.translatable("gui.mtr.terminates_here_3").getString();
							} else {
								callingAtStationText = "";
							}
						}

						// Render arrival number
						if (renderArrivalNumber) {
							textRenderer.drawInBatch(String.valueOf(i + 1), 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
						}

						final float newDestinationMaxWidth = destinationMaxWidth - (!renderClassic ? 0 : carLengthMaxWidth);

						// Render platform number
						if (renderType.showPlatformNumber && ((arrivalLine == 0 && renderSingle) || renderClassic)) {
							matrixStackHolder.push();
							final String platformName = platformIdToName.get(route.platformIds.get(currentSchedule.currentStationIndex).platformId);
							final Component platformNameComponent = platformName == null ? Text.literal("") : renderSingle ? Text.translatable(isCJK ? "gui.mtr.platform_abbreviated_cjk" : "gui.mtr.platform_abbreviated", platformName) : Text.literal(platformName);
							final int platformWidth = textRenderer.width(platformNameComponent);
							if (renderClassic) {
								matrices.translate(destinationStart + newDestinationMaxWidth, 0, 0);
							} else {
								if (platformWidth > platformMaxWidth) {
									matrices.translate(totalScaledWidth - platformMaxWidth, 0, 0);
									matrices.scale(platformMaxWidth / platformWidth, 1, 1);
								} else {
									matrices.translate(totalScaledWidth - platformWidth, 0, 0);
								}
							}
							textRenderer.drawInBatch(platformNameComponent, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							matrixStackHolder.pop();
						}

						// Render calling at text
						if (renderSingle && arrivalLine == 3) {
							matrixStackHolder.push();
							final int callingAtWidth = textRenderer.width(callingAtText);
							if (stations.size() != 0) {
								matrices.translate(destinationStart, 0, 0);
							}
							if (callingAtWidth > callingAtMaxWidth) {
								if (stations.size() == 0) {
									matrices.translate(totalScaledWidth / 2 - callingAtMaxWidth / 2, 0, 0);
								}
								matrices.scale(callingAtMaxWidth / callingAtWidth, 1, 1);
							} else if (stations.size() == 0) {
								matrices.translate(totalScaledWidth / 2 - callingAtWidth / 2F, 0, 0);
							}
							textRenderer.drawInBatch(callingAtText, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							matrixStackHolder.pop();
						}

						// Render calling at station
						if (renderSingle && arrivalLine >= 4 && arrivalLine < 14) {
							matrixStackHolder.push();
							final int callingAtStationWidth = textRenderer.width(callingAtStationText);
							if (stations.size() != 0) {
								matrices.translate(destinationStart, 0, 0);
							}
							if (callingAtStationWidth > callingAtStationMaxWidth) {
								if (stations.size() == 0) {
									matrices.translate(totalScaledWidth / 2 - callingAtStationMaxWidth / 2, 0, 0);
								}
								matrices.scale(callingAtStationMaxWidth / callingAtStationWidth, 1, 1);
							} else if (stations.size() == 0) {
								matrices.translate(totalScaledWidth / 2 - callingAtStationWidth / 2F, 0, 0);
							}
							textRenderer.drawInBatch(callingAtStationText, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							matrixStackHolder.pop();
						}

						// Render car length
						if (showCarLength) {
							matrixStackHolder.push();
							if (!renderSingle) {
								matrices.translate(renderVertical ? destinationStart : (destinationStart + newDestinationMaxWidth + platformMaxWidth), 0, 0);
							}
							final int carTextWidth = textRenderer.width(carText);
							if (carTextWidth > carLengthMaxWidth) {
								if (renderSingle) {
									matrices.translate(totalScaledWidth - carLengthMaxWidth, 0, 0);
								}
								matrices.scale(carLengthMaxWidth / carTextWidth, 1, 1);
							} else if (renderSingle) {
								matrices.translate(totalScaledWidth - carTextWidth, 0, 0);
							}
							textRenderer.drawInBatch(carText, 0, 0, CAR_TEXT_COLOR, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							matrixStackHolder.pop();
						}

						// Render destination
						matrixStackHolder.push();
						matrices.translate(destinationStart, 0, 0);
						final int destinationWidth = textRenderer.width(destinationString);
						if (destinationWidth > newDestinationMaxWidth) {
							matrices.scale(newDestinationMaxWidth / destinationWidth, 1, 1);
						}
						textRenderer.drawInBatch(destinationString, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
						textRenderer.drawInBatch(destinationString, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
						matrixStackHolder.pop();

						// Render arrival time
						if (arrivalText != null) {
							matrixStackHolder.push();
							final int arrivalWidth = textRenderer.width(arrivalText);
							if (renderSingle) {
								matrices.translate(destinationStart, 0, 0);
							}
							if (arrivalWidth > arrivalMaxWidth) {
								if (!renderSingle) {
									matrices.translate(totalScaledWidth - arrivalMaxWidth, 0, 0);
								}
								matrices.scale(arrivalMaxWidth / arrivalWidth, 1, 1);
							} else {
								if (!renderSingle) {
									matrices.translate(totalScaledWidth - arrivalWidth, 0, 0);
								}
							}
							textRenderer.drawInBatch(arrivalText, 0, 0, textColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							RenderSystem.disableDepthTest();
							((MultiBufferSource.BufferSource) vertexConsumers).endBatch();
							RenderSystem.enableDepthTest();
							textRenderer.drawInBatch(arrivalText, 0, 0, textColor, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
							matrixStackHolder.pop();
						}
					}
					matrixStackHolder.pop();
				}
			}
		} catch (Exception ignored) {
		}

		matrixStackHolder.popAll();
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
			case PIDS_SINGLE_ARRIVAL:
				if (entity instanceof IPIDS.TileEntityPIDS) {
					platformIds = ((IPIDS.TileEntityPIDS) entity).getPlatformIds();
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