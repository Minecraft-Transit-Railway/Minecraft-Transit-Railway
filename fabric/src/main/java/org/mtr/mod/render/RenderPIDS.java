package org.mtr.mod.render;

import org.mtr.core.data.Station;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockArrivalProjectorBase;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.ArrivalsCache;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketFetchArrivals;
import org.mtr.mod.render.pids.PIDSModule;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.render.pids.TextModule;

public class RenderPIDS<T extends BlockPIDSBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, Utilities {

	private final float startX;
	private final float startY;
	private final float startZ;
	private final float maxHeight;
	private final float maxWidth;
	private final boolean rotate90;
	private final float textPadding;

	public static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final String DEFAULT_LAYOUT = "{\"_editor_size\":\"hb\",\"version\":2,\"id\":\"pids_demo\",\"modules\":[{\"typeID\":\"destination\",\"pos\":{\"x\":1.5,\"y\":1.5,\"w\":22.5,\"h\":1.75},\"data\":{\"align\":\"left\",\"color\":16777215,\"arrival\":0,\"template\":\"%s\"}},{\"typeID\":\"arrivalTime\",\"pos\":{\"x\":24.5,\"y\":1.5,\"w\":6,\"h\":1.75},\"data\":{\"align\":\"right\",\"color\":16777215,\"arrival\":0,\"mode\":\"i\",\"secText\":\"%s sec\",\"minText\":\"%s min\",\"mixText\":\"%s:%s\"}},{\"typeID\":\"time\",\"pos\":{\"x\":1.5,\"y\":6.625,\"w\":29,\"h\":1},\"data\":{\"align\":\"center\",\"color\":16777215,\"loc\":\"g\",\"template\":\"Fairview Docks - Platform 1 - %s:%s:%s %s\",\"show24Hour\":false,\"showHours\":true,\"showMinutes\":true,\"showSeconds\":true}},{\"typeID\":\"trainLength\",\"pos\":{\"x\":1.5,\"y\":5,\"w\":29,\"h\":1.25},\"data\":{\"align\":\"left\",\"color\":16777215,\"arrival\":0,\"template\":\"This train has %s coaches.\"}},{\"typeID\":\"text\",\"pos\":{\"x\":1.5,\"y\":3.5,\"w\":29,\"h\":1.25},\"data\":{\"align\":\"left\",\"color\":16777215,\"arrival\":0,\"template\":\"Calling At: Desert Grand Central only\"}}],\"name\":\"PIDS Demo\",\"author\":\"EpicPuppy613\",\"description\":\"A simple demonstration of the new PIDS layout system.\"}";

	public RenderPIDS(Argument dispatcher, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, float textPadding) {
		super(dispatcher);
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
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
			getArrivalsAndRender(entity, blockPos, facing, new LongImmutableList(platformIds));
		} else {
			getArrivalsAndRender(entity, blockPos, facing, new LongImmutableList(entity.getPlatformIds()));
		}
	}

	private void getArrivalsAndRender(T entity, BlockPos blockPos, Direction facing, LongImmutableList platformIds) {
		final int count = (entity.getDisplayPage() + 1) * entity.maxArrivals / (entity.alternateLines() ? 2 : 1);
		final ArrivalsResponse arrivalsResponse = ArrivalsCache.INSTANCE.requestArrivals(blockPos.asLong(), platformIds, count, count, false);
		RenderTrains.scheduleRender(RenderTrains.QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			render(entity, blockPos, facing, arrivalsResponse, graphicsHolder, offset);
			if (entity instanceof BlockPIDSHorizontalBase.BlockEntityHorizontalBase) {
				render(entity, blockPos.offset(facing), facing.getOpposite(), arrivalsResponse, graphicsHolder, offset);
			}
		});
	}

	private void render(T entity, BlockPos blockPos, Direction facing, ArrivalsResponse arrivalsResponse, GraphicsHolder graphicsHolder, Vector3d offset) {
		// Scale is 1 px scaled = 1 block pixel (1/16 block)
		// Scale value represents the number of text pixels per block
		final float scale = 16;
		final ObjectImmutableList<ArrivalResponse> arrivalResponseList = arrivalsResponse.getArrivals();
		final boolean hasDifferentCarLengths = hasDifferentCarLengths(arrivalResponseList);
		int arrivalIndex = entity.getDisplayPage() * entity.maxArrivals;

		// Scale the screen
		graphicsHolder.push();
		graphicsHolder.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped(), blockPos.getZ() - offset.getZMapped() + 0.5);
		graphicsHolder.rotateYDegrees((rotate90 ? 90 : 0) - facing.asRotation());
		graphicsHolder.rotateZDegrees(180);
		graphicsHolder.translate((startX - 8) / 16, -startY / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
		graphicsHolder.scale(1 / scale, 1 / scale, 1);

		// The screen should now be scaled according to the scale value

		// First PIDSRenderController Test
		final PIDSRenderController controller = InitClient.pidsLayoutCache.getController(entity.getLayout());

		if (controller == null) {
			graphicsHolder.pop();
			return;
		};

		for (PIDSModule module : controller.getModules()) {
			module.render(graphicsHolder, arrivalResponseList);
		}

		// Render test text
		//renderText(graphicsHolder, "This is a test. It should be 1 pixel in height.", 1, 1, 1.0f, 0xFF00FF, 30, TextModule.AlignType.CENTER);
		//renderText(graphicsHolder, "This is some very cool text.", 1.25f, 1.25f, 1.5f, 0xFF00FF, 30, TextModule.AlignType.CENTER);

		/*for (int i = 0; i < entity.maxArrivals; i++) {
			final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
			final ArrivalResponse arrivalResponse;
			final String customMessage = entity.getMessage(i);
			final String[] destinationSplit;
			final String[] customMessageSplit = customMessage.split("\\|");
			final boolean renderCustomMessage;
			final int languageIndex;

			if (entity.getHideArrival(i)) {
				if (customMessage.isEmpty()) {
					continue;
				}
				arrivalResponse = null;
				destinationSplit = new String[0];
				renderCustomMessage = true;
				languageIndex = languageTicks % customMessageSplit.length;
			} else {
				arrivalResponse = Utilities.getElement(arrivalResponseList, arrivalIndex);
				if (arrivalResponse == null) {
					if (customMessage.isEmpty() || customMessageSplit.length == 0) {
						continue;
					}
					destinationSplit = new String[0];
					renderCustomMessage = true;
					languageIndex = languageTicks % customMessageSplit.length;
				} else {
					destinationSplit = arrivalResponse.getDestination().split("\\|");
					final int messageCount = destinationSplit.length + (customMessage.isEmpty() ? 0 : customMessageSplit.length);
					renderCustomMessage = languageTicks % messageCount >= destinationSplit.length;
					languageIndex = (languageTicks % messageCount) - (renderCustomMessage ? destinationSplit.length : 0);
					if (!entity.alternateLines() || i % 2 == 1) {
						arrivalIndex++;
					}
				}
			}

			graphicsHolder.push();

			if (renderCustomMessage) {
				renderText(graphicsHolder, customMessageSplit[languageIndex], 0, 0, entity.textColor(), maxWidth * scale / 16, TextModule.AlignType.LEFT);
			} else {
				final long arrival = (arrivalResponse.getArrival() - PacketFetchArrivals.getMillisOffset() - System.currentTimeMillis()) / 1000;
				final int color = arrival <= 0 ? entity.textColorArrived() : entity.textColor();
				final String destination = destinationSplit[languageIndex];
				final String translationSuffix = IGui.isCjk(destination) ? "_cjk" : "";
				final String destinationFormatted;

				switch (arrivalResponse.getCircularState()) {
					case CLOCKWISE:
						destinationFormatted = TextHelper.translatable("gui.mtr.clockwise_via" + translationSuffix, destination).getString();
						break;
					case ANTICLOCKWISE:
						destinationFormatted = TextHelper.translatable("gui.mtr.anticlockwise_via" + translationSuffix, destination).getString();
						break;
					default:
						destinationFormatted = destination;
						break;
				}

				final String carLengthString = TextHelper.translatable("gui.mtr.arrival_car" + translationSuffix, arrivalResponse.getCarCount()).getString();
				final String arrivalString;

				if (arrival >= 60) {
					arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + TextHelper.translatable("gui.mtr.arrival_min" + translationSuffix, arrival / 60).getString();
				} else if (arrival > 0) {
					arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + TextHelper.translatable("gui.mtr.arrival_sec" + translationSuffix, arrival).getString();
				} else {
					arrivalString = "";
				}

				if (entity.alternateLines()) {
					if (i % 2 == 0) {
						renderText(graphicsHolder, destinationFormatted, 0, 0, color, maxWidth * scale / 16, TextModule.AlignType.LEFT);
					} else {
						if (hasDifferentCarLengths) {
							renderText(graphicsHolder, carLengthString, 0, 0, 0xFF0000, 32, TextModule.AlignType.LEFT);
							graphicsHolder.translate(32, 0, 0);
						}
						renderText(graphicsHolder, arrivalString, 0, 0, color, maxWidth * scale / 16 - (hasDifferentCarLengths ? 32 : 0), TextModule.AlignType.RIGHT);
					}
				} else {
					final boolean showPlatformNumber = entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase;

					if (entity.showArrivalNumber()) {
						renderText(graphicsHolder, String.valueOf(arrivalIndex), 0, 0, color, 12, TextModule.AlignType.LEFT);
						graphicsHolder.translate(12, 0, 0);
					}

					final float destinationWidth = maxWidth * scale / 16 - 40 - (hasDifferentCarLengths || showPlatformNumber ? showPlatformNumber ? 16 : 32 : 0) - (entity.showArrivalNumber() ? 12 : 0);
					renderText(graphicsHolder, destinationFormatted, i == 0 ? 1 : 0, 0, color, destinationWidth, TextModule.AlignType.LEFT);
					graphicsHolder.translate(destinationWidth, 0, 0);

					if (hasDifferentCarLengths || showPlatformNumber) {
						if (showPlatformNumber) {
							renderText(graphicsHolder, arrivalResponse.getPlatformName(), 0, 0, color, 16, TextModule.AlignType.LEFT);
							graphicsHolder.translate(16, 0, 0);
						} else {
							renderText(graphicsHolder, carLengthString, 0, 0, 0xFF0000, 32, TextModule.AlignType.LEFT);
							graphicsHolder.translate(32, 0, 0);
						}
					}

					renderText(graphicsHolder, arrivalString, 0, 0, color, 40, TextModule.AlignType.RIGHT);
				}
			}

			graphicsHolder.pop();
		}*/

		graphicsHolder.pop();
	}

	public static void renderText(GraphicsHolder graphicsHolder, String text, float x, float y, float size, int color, float availableWidth, TextModule.AlignType align) {
		graphicsHolder.push();
		// determine the text size and scale the screen; minecraft text is 8 pixels high
		final float scale = size / 8;
		// position the text
		graphicsHolder.translate(x, y, 0);
		// remember to scale the text width by the scale
		// we do this now so that we get the text width in block-pixel scale
		final float textWidth = GraphicsHolder.getTextWidth(text) * scale;
		if (availableWidth < textWidth) {
			graphicsHolder.scale(textWidth == 0 ? 1 : availableWidth / textWidth, 1, 1);
		}
		if (align == TextModule.AlignType.CENTER) {
			graphicsHolder.translate(Math.max(0, availableWidth - textWidth) / 2, 0, 0);
		} else if (align == TextModule.AlignType.RIGHT) {
			graphicsHolder.translate(Math.max(0, availableWidth - textWidth), 0, 0);
		}
		// scale the screen now, so that the text is the correct size
		graphicsHolder.scale(scale, scale, 1);
		graphicsHolder.drawText(text, 0, 0, color | ARGB_BLACK, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.pop();
	}

	private static boolean hasDifferentCarLengths(ObjectImmutableList<ArrivalResponse> arrivalResponseList) {
		int carCount = 0;
		for (final ArrivalResponse arrivalResponse : arrivalResponseList) {
			final int currentCarCount = arrivalResponse.getCarCount();
			if (carCount > 0 && currentCarCount != carCount) {
				return true;
			}
			carCount = currentCarCount;
		}
		return false;
	}
}
