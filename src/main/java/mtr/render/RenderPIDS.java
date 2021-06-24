package mtr.render;

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

	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int TEXT_COLOR = 0xFF9900;

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, boolean showAllPlatforms) {
		super(dispatcher);
		scale = 160 * maxArrivals / maxHeight;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = totalScaledWidth * 0.6F;
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
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance)) {
			return;
		}

		try {
			final Set<Route.ScheduleEntry> schedules;
			final Map<Long, String> platformIdToName = new HashMap<>();

			if (showAllPlatforms) {
				final Station station = ClientData.getStation(pos);
				if (station == null) {
					return;
				}

				schedules = new HashSet<>();
				ClientData.platformsInStation.get(station.id).values().forEach(platform -> {
					final Set<Route.ScheduleEntry> scheduleForPlatform = ClientData.schedulesForPlatform.get(platform.id);
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

				schedules = ClientData.schedulesForPlatform.get(platform.id);
				if (schedules == null) {
					return;
				}
			}

			final int worldTime = (int) (world.getLunarTime() % Route.TICKS_PER_DAY);
			final List<Route.ScheduleEntry> scheduleList = new ArrayList<>(schedules);
			scheduleList.sort((a, b) -> {
				if (a.arrivalMillis == b.arrivalMillis) {
					return a.destination.compareTo(b.destination);
				} else {
					return a.arrivalMillis > b.arrivalMillis ? 1 : -1;
				}
			});

			for (int i = 0; i < Math.min(maxArrivals, scheduleList.size()); i++) {
				final Route.ScheduleEntry currentSchedule = scheduleList.get(i);

				final String[] destinationSplit = currentSchedule.destination.split("\\|");
				final String destinationString = destinationSplit[(worldTime / SWITCH_LANGUAGE_TICKS) % destinationSplit.length];

				final Text arrivalText;
				final int seconds = (int) Math.ceil(currentSchedule.arrivalMillis / 1000);
				final boolean isCJK = destinationString.codePoints().anyMatch(Character::isIdeographic);
				if (seconds >= 60) {
					arrivalText = new TranslatableText(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60);
				} else {
					arrivalText = seconds > 0 ? new TranslatableText(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds) : null;
				}

				matrices.push();
				matrices.translate(0.5, 0, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((rotate90 ? 90 : 0) - IBlock.getStatePropertySafe(world, pos, HorizontalFacingBlock.FACING).asRotation()));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
				matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
				matrices.scale(1F / scale, 1F / scale, 1F / scale);

				final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

				if (renderArrivalNumber) {
					textRenderer.draw(matrices, String.valueOf(i + 1), 0, 0, TEXT_COLOR);
				}

				if (showAllPlatforms) {
					final String platformName = platformIdToName.get(currentSchedule.platformId);
					if (platformName != null) {
						textRenderer.draw(matrices, platformName, destinationStart + destinationMaxWidth, 0, TEXT_COLOR);
					}
				}

				matrices.push();
				matrices.translate(destinationStart, 0, 0);
				final int destinationWidth = textRenderer.getWidth(destinationString);
				if (destinationWidth > destinationMaxWidth) {
					matrices.scale(destinationMaxWidth / destinationWidth, 1, 1);
				}
				textRenderer.draw(matrices, destinationString, 0, 0, TEXT_COLOR);
				matrices.pop();

				if (arrivalText != null) {
					matrices.push();
					final int arrivalWidth = textRenderer.getWidth(arrivalText);
					if (arrivalWidth > arrivalMaxWidth) {
						matrices.translate(destinationStart + destinationMaxWidth + platformMaxWidth, 0, 0);
						matrices.scale(arrivalMaxWidth / arrivalWidth, 1, 1);
					} else {
						matrices.translate(totalScaledWidth - arrivalWidth, 0, 0);
					}
					textRenderer.draw(matrices, arrivalText, 0, 0, TEXT_COLOR);
					matrices.pop();
				}

				matrices.pop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
