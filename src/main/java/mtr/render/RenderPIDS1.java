package mtr.render;

import mtr.block.BlockPIDS1;
import mtr.block.IBlock;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import mtr.entity.EntitySeat;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.HorizontalFacingBlock;
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

import java.util.Set;

public class RenderPIDS1 extends BlockEntityRenderer<BlockPIDS1.TileEntityBlockPIDS1> implements IGui {

	private static final int SCALE = 64;
	private static final int TOTAL_SCALED_WIDTH = SCALE * 30 / 16;
	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int TEXT_COLOR = 0xFF9900;

	public RenderPIDS1(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockPIDS1.TileEntityBlockPIDS1 entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		if (RenderSeat.shouldNotRender(pos, EntitySeat.DETAIL_RADIUS)) {
			return;
		}

		final Platform platform = ClientData.platforms.stream().filter(platform2 -> platform2.isCloseToPlatform(pos)).findFirst().orElse(null);
		if (platform == null) {
			return;
		}

		final Set<Route.ScheduleEntry> schedules = ClientData.schedulesForPlatform.get(platform.id);
		if (schedules == null) {
			return;
		}

		final int worldTime = (int) (world.getLunarTime() % Route.TICKS_PER_DAY);
		Route.ScheduleEntry currentSchedule = null;
		float timeDifference = Route.TICKS_PER_DAY;
		for (Route.ScheduleEntry schedule : schedules) {
			final float newTimeDifference = Route.wrapTime(schedule.departureTime, worldTime);
			if (newTimeDifference <= timeDifference) {
				currentSchedule = schedule;
				timeDifference = newTimeDifference;
			}
		}

		if (currentSchedule == null) {
			return;
		}

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
		matrices.translate(-0.4375, -0.203125, -0.125 - SMALL_OFFSET * 2);
		matrices.scale(1F / SCALE, 1F / SCALE, 1F / SCALE);

		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		textRenderer.draw(matrices, destinationString, 0, 0, TEXT_COLOR);
		if (arrivalText != null) {
			textRenderer.draw(matrices, arrivalText, TOTAL_SCALED_WIDTH - textRenderer.getWidth(arrivalText), 0, TEXT_COLOR);
		}

		matrices.pop();
	}
}
