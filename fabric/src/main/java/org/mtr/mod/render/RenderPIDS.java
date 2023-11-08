package org.mtr.mod.render;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSType;
import org.mtr.mod.packet.PacketFetchArrivals;

public class RenderPIDS<T extends BlockEntityExtension> extends BlockEntityRenderer<T> implements IGui, Utilities {

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
	private final PIDSType renderType;
	private final int textColor;
	private final int firstTrainColor;
	private final boolean appendDotAfterMin;

	private static final int SWITCH_LANGUAGE_TICKS = 60;

	public RenderPIDS(Argument dispatcher, int maxArrivals, int linesPerArrival, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, PIDSType renderType, int textColor, int firstTrainColor, float textPadding, boolean appendDotAfterMin) {
		super(dispatcher);
		scale = 160 * maxArrivals / maxHeight * textPadding;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = totalScaledWidth * 0.7F;
		platformMaxWidth = renderType.showPlatformNumber ? scale * 2 / 16 : 0;
		arrivalMaxWidth = totalScaledWidth - destinationStart - destinationMaxWidth - platformMaxWidth;
		this.maxArrivals = maxArrivals;
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

	public RenderPIDS(Argument dispatcher, int maxArrivals, int linesPerArrival, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, PIDSType renderType, int textColor, int firstTrainColor) {
		this(dispatcher, maxArrivals, linesPerArrival, startX, startY, startZ, maxHeight, maxWidth, rotate90, renderArrivalNumber, renderType, textColor, firstTrainColor, 1, false);
	}

	@Override
	public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos blockPos = entity.getPos2();
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, DirectionHelper.FACING);

		ClientData.instance.platforms.stream().filter(platform -> platform.closeTo(Init.blockPosToPosition(entity.getPos2()), 5)).findFirst().ifPresent(platform -> {
			try {
				final ObjectArrayList<PacketFetchArrivals.Arrival> arrivals = new ObjectArrayList<>();
				final JsonObject response = ClientData.instance.requestArrivals(platform.getId());
				response.getAsJsonArray("data").forEach(arrivalElement -> arrivals.add(new PacketFetchArrivals.Arrival(arrivalElement.getAsJsonObject())));

				RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolderNew, offset) -> {
					for (int i = 0; i < Math.min(maxArrivals, arrivals.size()); i++) {
						final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;

						graphicsHolderNew.push();
						graphicsHolderNew.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
						graphicsHolderNew.rotateYDegrees((rotate90 ? 90 : 0) - facing.asRotation());
						graphicsHolderNew.rotateZDegrees(180);
						graphicsHolderNew.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
						graphicsHolderNew.scale(1F / scale, 1F / scale, 1F / scale);

						final PacketFetchArrivals.Arrival arrival = arrivals.get(i);
						final long arrivalSeconds = Math.max(0, (arrival.arrivalTime - PacketFetchArrivals.millisOffset - System.currentTimeMillis()) / MILLIS_PER_SECOND);
						final long delay = arrival.delay / MILLIS_PER_SECOND;
						// TODO formatting
						graphicsHolderNew.drawText(arrival.destination.split("\\|")[0] + " D: " + delay + " A: " + (arrivalSeconds == 0 ? "" : arrivalSeconds), 0, 0, textColor, false, MAX_LIGHT_GLOWING);

						graphicsHolderNew.pop();
					}
				});
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		});
	}
}
