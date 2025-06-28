package org.mtr.mod.screen;

import org.mtr.core.data.Platform;
import org.mtr.core.data.RoutePlatformData;
import org.mtr.core.data.Station;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdatePIDSConfig;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class PIDSConfigScreen extends ScreenExtension implements IGui {

	private final BlockPos blockPos;
	private final String[] messages;
	private final boolean[] hideArrivalArray;
	private final TextFieldWidgetExtension[] textFieldMessages;
	private final CheckboxWidgetExtension[] buttonsHideArrival;
	private final TextFieldWidgetExtension displayPageInput;
	private final MutableText messageText = TranslationProvider.GUI_MTR_PIDS_MESSAGE.getMutableText();
	private final MutableText hideArrivalText = TranslationProvider.GUI_MTR_HIDE_ARRIVAL.getMutableText();
	private final CheckboxWidgetExtension selectAllCheckbox;
	private final ButtonWidgetExtension filterButton;
	private final TexturedButtonWidgetExtension buttonPrevPage;
	private final TexturedButtonWidgetExtension buttonNextPage;
	private final LongAVLTreeSet filterPlatformIds;
	private final int displayPage;
	private final int maxArrivals;
	private int page = 0;

	private static final int MAX_MESSAGE_LENGTH = 2048;
	private static final int TEXT_FIELDS_Y_OFFSET = SQUARE_SIZE * 8 + TEXT_FIELD_PADDING / 2;

	public PIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		super();
		this.blockPos = blockPos;
		this.maxArrivals = maxArrivals;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}
		hideArrivalArray = new boolean[maxArrivals];

		selectAllCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		selectAllCheckbox.setMessage2(TranslationProvider.GUI_MTR_AUTOMATICALLY_DETECT_NEARBY_PLATFORM.getText());

		textFieldMessages = new TextFieldWidgetExtension[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, "");
		}

		buttonsHideArrival = new CheckboxWidgetExtension[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			});
			buttonsHideArrival[i].setMessage2(new Text(hideArrivalText.data));
		}

		buttonPrevPage = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_left.png"), new Identifier("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1));
		buttonNextPage = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_right.png"), new Identifier("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1));

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			filterPlatformIds = new LongAVLTreeSet();
			displayPage = 0;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null && blockEntity.data instanceof BlockPIDSBase.BlockEntityBase) {
				filterPlatformIds = ((BlockPIDSBase.BlockEntityBase) blockEntity.data).getPlatformIds();
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity.data).getMessage(i);
					hideArrivalArray[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity.data).getHideArrival(i);
				}
				displayPage = ((BlockPIDSBase.BlockEntityBase) blockEntity.data).getDisplayPage();
			} else {
				filterPlatformIds = new LongAVLTreeSet();
				displayPage = 0;
			}
		}

		filterButton = getPlatformFilterButton(blockPos, selectAllCheckbox, filterPlatformIds, this);
		displayPageInput = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", "1");
	}

	@Override
	protected void init2() {
		super.init2();
		final int customMessageWidth = GraphicsHolder.getTextWidth(messageText) + SQUARE_SIZE + TEXT_PADDING;
		final int textWidth = GraphicsHolder.getTextWidth(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;

		IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
		addChild(new ClickableWidget(selectAllCheckbox));

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
		filterButton.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		addChild(new ClickableWidget(filterButton));

		IDrawing.setPositionAndWidth(displayPageInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH / 2 - TEXT_FIELD_PADDING);
		displayPageInput.setText2(String.valueOf(displayPage + 1));
		addChild(new ClickableWidget(displayPageInput));

		IDrawing.setPositionAndWidth(buttonPrevPage, customMessageWidth, SQUARE_SIZE * 7, SQUARE_SIZE);
		addChild(new ClickableWidget(buttonPrevPage));

		IDrawing.setPositionAndWidth(buttonNextPage, customMessageWidth + SQUARE_SIZE * 3, SQUARE_SIZE * 7, SQUARE_SIZE);
		addChild(new ClickableWidget(buttonNextPage));

		for (int i = 0; i < textFieldMessages.length; i++) {
			final TextFieldWidgetExtension textFieldMessage = textFieldMessages[i];
			final int y = TEXT_FIELDS_Y_OFFSET + (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i % getMaxArrivalsPerPage());
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, y, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setText2(messages[i]);
			addChild(new ClickableWidget(textFieldMessage));

			final CheckboxWidgetExtension buttonHideArrival = buttonsHideArrival[i];
			IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, y + TEXT_FIELD_PADDING / 2, textWidth);
			buttonHideArrival.setChecked(hideArrivalArray[i]);
			addChild(new ClickableWidget(buttonHideArrival));
		}

		setPage(0);
	}

	private void setPage(int newPage) {
		final int maxArrivalsPerPage = getMaxArrivalsPerPage();
		final int maxPages = getMaxPages() - 1;
		page = MathHelper.clamp(newPage, 0, maxPages);
		for (int i = 0; i < textFieldMessages.length; i++) {
			textFieldMessages[i].visible = i / maxArrivalsPerPage == page;
		}
		for (int i = 0; i < buttonsHideArrival.length; i++) {
			buttonsHideArrival[i].visible = i / maxArrivalsPerPage == page;
		}
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < maxPages;
	}

	@Override
	public void tick2() {
		for (final TextFieldWidgetExtension textFieldMessage : textFieldMessages) {
			textFieldMessage.tick2();
		}
	}

	@Override
	public void onClose2() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getText2();
			hideArrivalArray[i] = buttonsHideArrival[i].isChecked2();
		}
		if (selectAllCheckbox.isChecked2()) {
			filterPlatformIds.clear();
		}
		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText2()) - 1);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdatePIDSConfig(blockPos, messages, hideArrivalArray, filterPlatformIds, displayPage));
		super.onClose2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_DISPLAY_PAGE.getMutableText(), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_FILTERED_PLATFORMS.getMutableText(selectAllCheckbox.isChecked2() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(messageText, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		final int maxPages = getMaxPages();
		if (maxPages > 1) {
			graphicsHolder.drawCenteredText(String.format("%s/%s", page + 1, maxPages), SQUARE_SIZE * 3 + GraphicsHolder.getTextWidth(messageText) + TEXT_PADDING, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE);
		}
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private int getMaxArrivalsPerPage() {
		return Math.max(1, (height - TEXT_FIELDS_Y_OFFSET - SQUARE_SIZE) / (SQUARE_SIZE + TEXT_FIELD_PADDING));
	}

	private int getMaxPages() {
		return (int) Math.ceil((float) maxArrivals / getMaxArrivalsPerPage());
	}

	public static ButtonWidgetExtension getPlatformFilterButton(BlockPos blockPos, CheckboxWidgetExtension selectAllCheckbox, LongAVLTreeSet filterPlatformIds, ScreenExtension thisScreen) {
		return new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			final Station station = InitClient.findStation(blockPos);

			final ObjectImmutableList<DashboardListItem> platformsForList;
			if(station != null) {
				platformsForList = getPlatformsForList(new ObjectArrayList<>(station.savedRails));
			} else {
				ObjectArrayList<Platform> nearbyPlatforms = new ObjectArrayList<>();
				InitClient.findClosePlatform(blockPos.down(4), 5, nearbyPlatforms::add);
				platformsForList = getPlatformsForList(nearbyPlatforms);
			}

			if (selectAllCheckbox.isChecked2()) {
				filterPlatformIds.clear();
			}

			MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(() -> selectAllCheckbox.setChecked(filterPlatformIds.isEmpty()), new ObjectImmutableList<>(platformsForList), filterPlatformIds, false, false, thisScreen)));
		});
	}

	public static ObjectImmutableList<DashboardListItem> getPlatformsForList(ObjectArrayList<Platform> platforms) {
		final ObjectArrayList<DashboardListItem> platformsForList = new ObjectArrayList<>();
		Collections.sort(platforms);
		platforms.forEach(platform -> platformsForList.add(new DashboardListItem(platform.getId(), platform.getName() + " " + IGui.mergeStations(platform.routes.stream().map(route -> {
			final RoutePlatformData lastRoutePlatformData = Utilities.getElement(route.getRoutePlatforms(), -1);
			if (lastRoutePlatformData == null) {
				return null;
			} else {
				final Station lastStation = lastRoutePlatformData.platform.area;
				return lastStation == null ? null : lastStation.getName();
			}
		}).filter(Objects::nonNull).collect(Collectors.toList())), 0)));
		return new ObjectImmutableList<>(platformsForList);
	}
}