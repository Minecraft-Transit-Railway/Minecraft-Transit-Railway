package org.mtr.mod.render;

import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketFetchArrivals;

public class RenderPIDS<T extends BlockPIDSBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, Utilities {

	private final float maxHeight;
	private final float startX;
	private final float startY;
	private final float startZ;
	private final boolean rotate90;
	private final int textColor = 0xFF9900; // TODO
	private final float textPadding;

	private static final int SWITCH_LANGUAGE_TICKS = 60;

	public RenderPIDS(Argument dispatcher, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, float textPadding) {
		super(dispatcher);
		this.maxHeight = maxHeight;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.rotate90 = rotate90;
		this.textPadding = textPadding;
	}

	@Override
	public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos blockPos = entity.getPos2();
		if (!entity.canStoreData.test(world, blockPos)) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, DirectionHelper.FACING);
		final boolean isHorizontal = entity instanceof BlockPIDSHorizontalBase.BlockEntityHorizontalBase;

		InitClient.findClosePlatform(entity.getPos2(), 5, platform -> {
			final ArrivalsResponse arrivalsResponse = ClientData.getInstance().requestArrivals(blockPos.asLong(), LongImmutableList.of(platform.getId()), entity.maxArrivals, 0, true);
			RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolderNew, offset) -> {
				render(blockPos, facing, arrivalsResponse, entity.maxArrivals, graphicsHolderNew, offset, false);
				if (isHorizontal) {
					render(blockPos.offset(facing), facing, arrivalsResponse, entity.maxArrivals, graphicsHolderNew, offset, true);
				}
			});
		});
	}

	private void render(BlockPos blockPos, Direction facing, ArrivalsResponse arrivalsResponse, int maxArrivals, GraphicsHolder graphicsHolder, Vector3d offset, boolean addRotation) {
		final float scale = 160 * maxArrivals / maxHeight * textPadding;

		arrivalsResponse.iterateArrivals((arrivalIndex, arrivalResponse) -> {
			final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;

			graphicsHolder.push();
			graphicsHolder.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
			graphicsHolder.rotateYDegrees((rotate90 ? 90 : 0) + (addRotation ? 180 : 0) - facing.asRotation());
			graphicsHolder.rotateZDegrees(180);
			graphicsHolder.translate((startX - 8) / 16, -startY / 16 + arrivalIndex * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
			graphicsHolder.scale(1 / scale, 1 / scale, 1 / scale);

			final long arrivalSeconds = Math.max(0, (arrivalResponse.getArrival() - PacketFetchArrivals.getMillisOffset() - System.currentTimeMillis()) / MILLIS_PER_SECOND);
			final long delay = arrivalResponse.getDeviation() / MILLIS_PER_SECOND;
			// TODO formatting
			graphicsHolder.drawText(arrivalResponse.getDestination().split("\\|")[0] + " D: " + delay + " A: " + (arrivalSeconds == 0 ? "" : arrivalSeconds) + " I: " + arrivalResponse.getDepartureIndex(), 0, 0, textColor, false, MAX_LIGHT_GLOWING);

			graphicsHolder.pop();
		});
	}
}
