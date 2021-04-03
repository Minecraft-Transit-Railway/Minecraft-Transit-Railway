package mtr.render;

import mtr.block.IBlock;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.entity.EntitySeat;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class RenderPIDS<T extends BlockEntity> extends BlockEntityRenderer<T> implements IGui {

	private final float scale;
	private final float totalScaledWidth;
	private final float destinationStart;
	private final float destinationMaxWidth;
	private final float arrivalMaxWidth;
	private final int maxArrivals;
	private final float maxHeight;
	private final float startX;
	private final float startY;
	private final boolean renderArrivalNumber;

	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int TEXT_COLOR = 0xFF9900;

	public RenderPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float maxHeight, int maxWidth, boolean renderArrivalNumber) {
		super(dispatcher);
		scale = 160 * maxArrivals / maxHeight;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = totalScaledWidth * 0.6F;
		arrivalMaxWidth = totalScaledWidth - destinationStart - destinationMaxWidth;
		this.maxArrivals = maxArrivals;
		this.maxHeight = maxHeight;
		this.startX = startX;
		this.startY = startY;
		this.renderArrivalNumber = renderArrivalNumber;
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		if (RenderSeat.shouldNotRender(pos, EntitySeat.DETAIL_RADIUS)) {
			return;
		}

		final Platform platform = ClientData.getClosePlatform(pos);
		if (platform == null) {
			return;
		}

		final Set<Route.ScheduleEntry> schedules = ClientData.schedulesForPlatform.get(platform.id);
		if (schedules == null) {
			return;
		}

		try {
			final int worldTime = (int) (world.getLunarTime() % Route.TICKS_PER_DAY);
			final ArrayList<Route.ScheduleEntry> scheduleList = new ArrayList<>(schedules);
			scheduleList.sort(Comparator.comparingInt(schedule -> (int) Route.wrapTime(schedule.departureTime, worldTime)));

			for (int i = 0; i < Math.min(maxArrivals, scheduleList.size()); i++) {
				final Route.ScheduleEntry currentSchedule = scheduleList.get(i);

				final Station destinationStation = ClientData.platformIdToStation.get(currentSchedule.lastStationId);
				if (destinationStation == null) {
					return;
				}

				final float departureTime = Route.wrapTime(currentSchedule.departureTime, worldTime);
				if (departureTime > Route.TICKS_PER_DAY / 2F) {
					return;
				}

				final String[] destinationSplit = destinationStation.name.split("\\|");
				final String destinationString = destinationSplit[(worldTime / SWITCH_LANGUAGE_TICKS) % destinationSplit.length];

				final float arrivalTime = Route.wrapTime(currentSchedule.arrivalTime, worldTime);
				final Text arrivalText;
				if (arrivalTime < Route.TICKS_PER_DAY / 2F) {
					final int seconds = (int) Math.ceil(arrivalTime / 20);
					final boolean isCJK = destinationString.codePoints().anyMatch(Character::isIdeographic);
					if (seconds >= 60) {
						arrivalText = new TranslatableText(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60);
					} else {
						arrivalText = seconds > 0 ? new TranslatableText(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", seconds) : null;
					}
				} else {
					arrivalText = null;
				}

				matrices.push();
				matrices.translate(0.5, 0, 0.5);
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90 - IBlock.getStatePropertySafe(world, pos, HorizontalFacingBlock.FACING).asRotation()));
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
				matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, -0.125 - SMALL_OFFSET * 2);
				matrices.scale(1F / scale, 1F / scale, 1F / scale);

				final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

				if (renderArrivalNumber) {
					textRenderer.draw(matrices, String.valueOf(i + 1), 0, 0, TEXT_COLOR);
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
						matrices.translate(destinationStart + destinationMaxWidth, 0, 0);
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
