package org.mtr.mod.render;

import org.mtr.core.data.Station;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockArrivalProjectorBase;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.ArrivalsCacheClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.pids.PIDSModule;
import org.mtr.mod.render.pids.PIDSRenderController;

import java.util.ArrayList;

public class RenderModularPIDS<T extends BlockPIDSBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, Utilities {

	private final float startX;
	private final float startY;
	private final float startZ;
	private final boolean rotate90;

	public static final int SWITCH_TEXT_TICKS = 60;
	public static final float SCALE = 16;

	public RenderModularPIDS(Argument dispatcher, float startX, float startY, float startZ, boolean rotate90) {
		super(dispatcher);
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.rotate90 = rotate90;
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

		final PIDSRenderController controller = InitClient.pidsLayoutCache.getController(entity.getLayout());

		if (controller == null) {
			// If controller does not exist, abort rendering and return
			return;
		}

		if (entity.getPlatformIds().isEmpty()) {
			final LongArrayList platformIds = new LongArrayList();
			if (entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase) {
				final Station station = InitClient.findStation(blockPos);
				if (station != null) {
					station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
				}
			} else {
				InitClient.findClosePlatform(entity.getPos2().down(4), 5, platform -> platformIds.add(platform.getId()));
			}
			getArrivalsAndRender(controller, entity, blockPos, facing, new LongImmutableList(platformIds));
		} else {
			getArrivalsAndRender(controller, entity, blockPos, facing, new LongImmutableList(entity.getPlatformIds()));
		}
	}

	private void getArrivalsAndRender(PIDSRenderController controller, T entity, BlockPos blockPos, Direction facing, LongImmutableList platformIds) {
		final int count = (entity.getDisplayPage() + 1) * controller.arrivals;
		final ObjectArrayList<ArrivalResponse> arrivalsResponse = ArrivalsCacheClient.INSTANCE.requestArrivals(platformIds);
		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			render(controller, entity, blockPos, facing, arrivalsResponse, graphicsHolder, offset);
			if (entity instanceof BlockPIDSHorizontalBase.BlockEntityHorizontalBase) {
				render(controller, entity, blockPos.offset(facing), facing.getOpposite(), arrivalsResponse, graphicsHolder, offset);
			}
		});
	}

	private void render(PIDSRenderController controller, T entity, BlockPos blockPos, Direction facing, ObjectArrayList<ArrivalResponse> arrivalsResponse, GraphicsHolder graphicsHolder, Vector3d offset) {

        // Scale the screen
		graphicsHolder.push();
		graphicsHolder.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
		graphicsHolder.rotateYDegrees((rotate90 ? 90 : 0) - facing.asRotation());
		graphicsHolder.rotateZDegrees(180);
		graphicsHolder.translate((startX - 8) / 16, -startY / 16, (startZ - 8) / 16);
		graphicsHolder.scale(1 / SCALE, 1 / SCALE, 1);

		// The screen should now be scaled according to the scale value

		int arrivalOffset = controller.arrivals * entity.getDisplayPage();

		final ObjectList<ArrivalResponse> subList;
		if (arrivalOffset < arrivalsResponse.size()) {
			subList = arrivalsResponse.subList(arrivalOffset, Math.min(arrivalOffset + controller.arrivals, arrivalsResponse.size()));
		} else {
			subList = new ObjectImmutableList<>(new ArrayList<>());
		}
		for (PIDSModule module : controller.getModules()) {
			module.render(graphicsHolder, subList, this, entity, blockPos, facing);
		}

		graphicsHolder.pop();
	}

	public static void renderText(GraphicsHolder graphicsHolder, String text, float x, float y, float size, int color, float availableWidth, IGui.HorizontalAlignment align, int layer) {
		graphicsHolder.push();
		// determine the text size and scale the screen; minecraft text is 8 pixels high
		final float scale = size / 8;
		// position the text and apply layer
		graphicsHolder.translate(x, y, -SMALL_OFFSET * (layer + 2));
		// remember to scale the text width by the scale
		// we do this now so that we get the text width in block-pixel scale
		final float textWidth = GraphicsHolder.getTextWidth(text) * scale;
		if (availableWidth < textWidth) {
			graphicsHolder.scale(textWidth == 0 ? 1 : availableWidth / textWidth, 1, 1);
		}
		if (align == IGui.HorizontalAlignment.CENTER) {
			graphicsHolder.translate(Math.max(0, availableWidth - textWidth) / 2, 0, 0);
		} else if (align == IGui.HorizontalAlignment.RIGHT) {
			graphicsHolder.translate(Math.max(0, availableWidth - textWidth), 0, 0);
		}
		// scale the screen now, so that the text is the correct size
		graphicsHolder.scale(scale, scale, 1);
		graphicsHolder.drawText(text, 0, 0, color | ARGB_BLACK, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.pop();
	}

	public void renderRect(T entity, BlockPos blockPos, Direction facing, float x, float y, float width, float height, int color, int layer) {
		World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
			graphicsHolderNew.push();
			graphicsHolderNew.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
			graphicsHolderNew.rotateYDegrees((rotate90 ? 90 : 0) - facing.asRotation());
			graphicsHolderNew.rotateZDegrees(180);
			graphicsHolderNew.translate((startX - 8) / 16, -startY / 16, (startZ - 8) / 16 - SMALL_OFFSET * (layer + 2));
			graphicsHolderNew.scale(1 / SCALE, 1 / SCALE, 1);
			IDrawing.drawTexture(graphicsHolderNew, x, y, 0, x + width, y + height, 0, facing, color, GraphicsHolder.getDefaultLight());
			graphicsHolderNew.pop();
		});
	}

	public static int getCharWidth(char c) {
		return GraphicsHolder.getTextWidth(String.valueOf(c));
	}

	public static int[] getCharWidths(String text) {
		int[] widths = new int[text.length()];
		for (int i = 0; i < text.length(); i++) {
			widths[i] = getCharWidth(text.charAt(i));
		}
		return widths;
	}
}
