package mtr.gui;

import mtr.data.RailwayData;
import mtr.data.Train;
import mtr.data.TrainSpawner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Comparator;
import java.util.List;

public class ScheduleScreen extends Screen implements IGui {

	private final List<Triple<Integer, Long, Train.TrainType>> schedule;
	private final int maxRouteWidth, maxTrainTypeWidth;

	public ScheduleScreen(BlockPos spawnerPos) {
		super(new LiteralText(""));
		final TrainSpawner trainSpawner = RailwayData.getTrainSpawnerByPos(ClientData.trainSpawners, spawnerPos);
		schedule = trainSpawner.generateSchedule();
		textRenderer = MinecraftClient.getInstance().textRenderer;
		maxRouteWidth = trainSpawner.shuffleRoutes ? 0 : schedule.stream().map(scheduleEntry -> textRenderer.getWidth(RailwayData.getRouteById(ClientData.routes, scheduleEntry.getMiddle()).name)).max(Comparator.comparingInt(a -> a)).orElse(0);
		maxTrainTypeWidth = trainSpawner.shuffleTrains ? 0 : schedule.stream().map(scheduleEntry -> textRenderer.getWidth(scheduleEntry.getRight().getName())).max(Comparator.comparingInt(a -> a)).orElse(0);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		final int time = client != null && client.world != null ? (int) ((client.world.getTimeOfDay() + 6000) % (TrainSpawner.HOURS_IN_DAY * TrainSpawner.TICKS_PER_HOUR)) : 0;
		final int timeWidth = textRenderer.getWidth("00:00") + TEXT_PADDING;
		drawTextWithShadow(matrices, textRenderer, new TranslatableText("gui.mtr.train_spawner_schedule"), TEXT_PADDING, TEXT_PADDING, ARGB_LIGHT_GRAY);
		drawStringWithShadow(matrices, textRenderer, getTimeString(time), width - timeWidth, TEXT_PADDING, ARGB_LIGHT_GRAY);

		final int rows = (height - SQUARE_SIZE - TEXT_PADDING) / LINE_HEIGHT;
		final int columns = (int) Math.ceil((float) schedule.size() / rows);

		if (columns > 0) {
			final int columnWidth = (width - TEXT_PADDING) / columns;
			final int remainingWidth = columnWidth - timeWidth;
			int routeWidth = maxRouteWidth + TEXT_PADDING;
			int trainTypeWidth = maxTrainTypeWidth + TEXT_PADDING;
			if (routeWidth + trainTypeWidth > remainingWidth) {
				if (routeWidth <= remainingWidth / 2) {
					trainTypeWidth = remainingWidth - routeWidth;
				} else if (trainTypeWidth <= remainingWidth / 2) {
					routeWidth = remainingWidth - trainTypeWidth;
				} else {
					routeWidth = remainingWidth / 2;
					trainTypeWidth = remainingWidth / 2;
				}
			}

			for (int column = 0; column < columns; column++) {
				for (int row = 0; row < rows; row++) {
					final int index = row + rows * column;
					if (index < schedule.size()) {
						final Triple<Integer, Long, Train.TrainType> scheduleEntry = schedule.get(index);
						drawStringWithShadow(matrices, textRenderer, getTimeString(scheduleEntry.getLeft()), TEXT_PADDING + column * columnWidth, SQUARE_SIZE + row * LINE_HEIGHT, Math.abs(time - scheduleEntry.getLeft()) < 50 ? ARGB_GREEN : ARGB_WHITE);
						drawStringWithShadow(matrices, textRenderer, cutToWidth(getRouteString(scheduleEntry.getMiddle()), routeWidth - TEXT_PADDING), TEXT_PADDING + column * columnWidth + timeWidth, SQUARE_SIZE + row * LINE_HEIGHT, ARGB_LIGHT_GRAY);
						drawStringWithShadow(matrices, textRenderer, cutToWidth(getTrainTypeString(scheduleEntry.getRight()), trainTypeWidth - TEXT_PADDING), TEXT_PADDING + column * columnWidth + timeWidth + routeWidth, SQUARE_SIZE + row * LINE_HEIGHT, ARGB_LIGHT_GRAY);
					}
				}
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private String getTimeString(int time) {
		final String hourString = StringUtils.leftPad(String.valueOf(time / TrainSpawner.TICKS_PER_HOUR), 2, "0");
		final String minuteString = StringUtils.leftPad(String.valueOf((int) (0.06 * (time % TrainSpawner.TICKS_PER_HOUR))), 2, "0");
		return hourString + ":" + minuteString;
	}

	private String getRouteString(long routeId) {
		return routeId < 0 ? "" : RailwayData.getRouteById(ClientData.routes, routeId).name;
	}

	private String getTrainTypeString(Train.TrainType trainType) {
		return trainType == null ? "" : trainType.getName();
	}

	private String cutToWidth(String string, int maxWidth) {
		String cutString = string;
		while (textRenderer.getWidth(cutString) > maxWidth) {
			cutString = cutString.substring(0, cutString.length() - 1);
		}
		return cutString;
	}
}
