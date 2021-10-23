package mtr.render;

import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.gui.ClientData;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class RenderPIDS<T extends BlockEntity> extends BlockEntityRenderer<T> implements IGui {

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

	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int CAR_TEXT_COLOR = 0xFF0000;
	private static final int MAX_VIEW_DISTANCE = 16;

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
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalFacingBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, Math.min(MAX_VIEW_DISTANCE, RenderTrains.maxTrainRenderDistance), rotate90 ? null : facing)) {
			return;
		}

		try {
			String customMessage = "";
			if(entity instanceof BlockPIDS1.TileEntityBlockPIDS1) {
				customMessage = ((BlockPIDS1.TileEntityBlockPIDS1) entity).getMessage();
			}

			if(entity instanceof BlockPIDS2.TileEntityBlockPIDS2) {
				customMessage = ((BlockPIDS2.TileEntityBlockPIDS2) entity).getMessage();
			}

			if(entity instanceof BlockPIDS3.TileEntityBlockPIDS3) {
				customMessage = ((BlockPIDS3.TileEntityBlockPIDS3) entity).getMessage();
			}

			if (!customMessage.equals("")) {
				String[] messages = customMessage.split("\\|");
				final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
				int i = 0;
				for(String msg : messages) {
					if(i > maxArrivals - 1) return;
					matrices.push();
					matrices.translate(0.5, 0, 0.5);
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((rotate90 ? 90 : 0) - facing.asRotation()));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
					matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
					matrices.scale(1F / scale, 1F / scale, 1F / scale);
					final int destinationWidth = textRenderer.getWidth(msg);
					if (destinationWidth > totalScaledWidth) {
						matrices.scale(totalScaledWidth / destinationWidth, 1, 1);
					}

					textRenderer.draw(matrices, msg, 0, 0, textColor);
					matrices.pop();
					i++;
				}
			} else {
				final Set<Route.ScheduleEntry> schedules;
				final Map<Long, String> platformIdToName = new HashMap<>();

				if (showAllPlatforms) {
					final Station station = ClientData.getStation(pos);
					if (station == null) {
						return;
					}

					final Map<Long, Platform> platforms = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
					if (platforms.isEmpty()) {
						return;
					}

					schedules = new HashSet<>();
					platforms.values().forEach(platform -> {
						final Set<Route.ScheduleEntry> scheduleForPlatform = ClientData.SCHEDULES_FOR_PLATFORM.get(platform.id);
						if (scheduleForPlatform != null) {
							scheduleForPlatform.forEach(scheduleEntry -> {
								if (!scheduleEntry.isTerminating) {
									schedules.add(scheduleEntry);
									platformIdToName.put(platform.id, platform.name);
								}
							});
						}
					});
				} else {
					final Platform platform = ClientData.getClosePlatform(pos);
					if (platform == null) {
						return;
					}

					schedules = ClientData.SCHEDULES_FOR_PLATFORM.get(platform.id);
					if (schedules == null) {
						return;
					}
				}

				final List<Route.ScheduleEntry> scheduleList = new ArrayList<>(schedules);
				Collections.sort(scheduleList);

				final boolean showCarLength;
				final float carLengthMaxWidth;
				if (!showAllPlatforms) {
					int maxCars = 0;
					int minCars = Integer.MAX_VALUE;
					for (final Route.ScheduleEntry scheduleEntry : scheduleList) {
						final int trainLength = scheduleEntry.trainLength;
						if (trainLength > maxCars) {
							maxCars = trainLength;
						}
						if (trainLength < minCars) {
							minCars = trainLength;
						}
					}
					showCarLength = minCars != maxCars;
					carLengthMaxWidth = showCarLength ? scale * 6 / 16 : 0;
				} else {
					showCarLength = false;
					carLengthMaxWidth = 0;
				}

				for (int i = 0; i < Math.min(maxArrivals, scheduleList.size()); i++) {
					final Route.ScheduleEntry currentSchedule = scheduleList.get(i);

					final String[] destinationSplit = currentSchedule.destination.split("\\|");
					final String destinationString = IGui.textOrUntitled(destinationSplit[((int) Math.floor(RenderTrains.getGameTicks()) / SWITCH_LANGUAGE_TICKS) % destinationSplit.length]);

					final Text arrivalText;
					final int seconds = (int) ((currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000);
					final boolean isCJK = destinationString.codePoints().anyMatch(Character::isIdeographic);
					if (seconds >= 60) {
						arrivalText = new TranslatableText(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60).append(appendDotAfterMin && !isCJK ? "." : "");
					} else {
						arrivalText = seconds > 0 ? new TranslatableText(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds).append(appendDotAfterMin && !isCJK ? "." : "") : null;
					}
					final Text carText = new TranslatableText(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", currentSchedule.trainLength);

					matrices.push();
					matrices.translate(0.5, 0, 0.5);
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((rotate90 ? 90 : 0) - facing.asRotation()));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
					matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
					matrices.scale(1F / scale, 1F / scale, 1F / scale);

					final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

					if (renderArrivalNumber) {
						textRenderer.draw(matrices, String.valueOf(i + 1), 0, 0, seconds > 0 ? textColor : firstTrainColor);
					}

					final float newDestinationMaxWidth = destinationMaxWidth - carLengthMaxWidth;

					if (showAllPlatforms) {
						final String platformName = platformIdToName.get(currentSchedule.platformId);
						if (platformName != null) {
							textRenderer.draw(matrices, platformName, destinationStart + newDestinationMaxWidth, 0, seconds > 0 ? textColor : firstTrainColor);
						}
					}

					if (showCarLength) {
						matrices.push();
						matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth, 0, 0);
						final int carTextWidth = textRenderer.getWidth(carText);
						if (carTextWidth > carLengthMaxWidth) {
							matrices.scale(carLengthMaxWidth / carTextWidth, 1, 1);
						}
						textRenderer.draw(matrices, carText, 0, 0, CAR_TEXT_COLOR);
						matrices.pop();
					}

					matrices.push();
					matrices.translate(destinationStart, 0, 0);
					final int destinationWidth = textRenderer.getWidth(destinationString);
					if (destinationWidth > newDestinationMaxWidth) {
						matrices.scale(newDestinationMaxWidth / destinationWidth, 1, 1);
					}
					textRenderer.draw(matrices, destinationString, 0, 0, seconds > 0 ? textColor : firstTrainColor);
					matrices.pop();

					if (arrivalText != null) {
						matrices.push();
						final int arrivalWidth = textRenderer.getWidth(arrivalText);
						if (arrivalWidth > arrivalMaxWidth) {
							matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth + carLengthMaxWidth, 0, 0);
							matrices.scale(arrivalMaxWidth / arrivalWidth, 1, 1);
						} else {
							matrices.translate(totalScaledWidth - arrivalWidth, 0, 0);
						}
						textRenderer.draw(matrices, arrivalText, 0, 0, textColor);
						matrices.pop();
					}

					matrices.pop();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
