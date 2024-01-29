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
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.packet.PacketUpdatePIDSConfig;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class PIDSConfigScreen extends ScreenExtension implements IGui, IPacket {

	private final BlockPos blockPos;
	/**
	 * Contains the formatting for each line, including any custom messages.
	 * <br/>
	 * <br/>
	 * Placeholders:
	 * <ul>
	 * <li>{@code %destination1%} - Destination</li>
	 * <li>{@code %RAH1%} - Relative arrival hour</li>
	 * <li>{@code %RAm1%} - Relative arrival minute of hour (0-59)</li>
	 * <li>{@code %RAs1%} - Relative arrival second of minute (0-59)</li>
	 * <li>{@code %RA0H1%} - Relative arrival (zero-padded) hour</li>
	 * <li>{@code %RA0m1%} - Relative arrival (zero-padded) minute of hour (0-59)</li>
	 * <li>{@code %RA0s1%} - Relative arrival (zero-padded) second of minute (0-59)</li>
	 * <li>{@code %AAH1%} - Absolute arrival hour of day (24-hour) in the client's local timezone (0-23)</li>
	 * <li>{@code %AAh1%} - Absolute arrival hour of day (12-hour) in the client's local timezone (1-12)</li>
	 * <li>{@code %AAm1%} - Absolute arrival minute of hour in the client's local timezone (0-59)</li>
	 * <li>{@code %AAs1%} - Absolute arrival second of minute in the client's local timezone (0-59)</li>
	 * <li>{@code %AA0H1%} - Absolute arrival (zero-padded) hour of day (24-hour) in the client's local timezone (0-23)</li>
	 * <li>{@code %AA0h1%} - Absolute arrival (zero-padded) hour of day (12-hour) in the client's local timezone (1-12)</li>
	 * <li>{@code %AA0m1%} - Absolute arrival (zero-padded) minute of hour in the client's local timezone (0-59)</li>
	 * <li>{@code %AA0s1%} - Absolute arrival (zero-padded) second of minute in the client's local timezone (0-59)</li>
	 * <li>{@code %AAa1%} - Arrival AM/PM in the client's local timezone</li>
	 * <li>{@code %DH1%} - Deviation (always positive) hour</li>
	 * <li>{@code %Dm1%} - Deviation (always positive) minute of hour (0-59)</li>
	 * <li>{@code %Ds1%} - Deviation (always positive) second of minute (0-59)</li>
	 * <li>{@code %D0H1%} - Deviation (always positive, zero-padded) hour</li>
	 * <li>{@code %D0m1%} - Deviation (always positive, zero-padded) minute of hour (0-59)</li>
	 * <li>{@code %D0s1%} - Deviation (always positive, zero-padded) second of minute (0-59)</li>
	 * <li>{@code %index1%} - Departure index</li>
	 * <li>{@code %routeName1%} - Route name</li>
	 * <li>{@code %routeNumber1%} - Route number</li>
	 * <li>{@code %platformNumber1%} - Platform number</li>
	 * <li>{@code %cars1%} - Number of cars</li>
	 * </ul>
	 * All placeholders listed above end with {@code 1}, meaning that the first arrival's data is shown.
	 * Indices start at 1, not 0.
	 * Time-related values are based on {@link java.time.format.DateTimeFormatter} values.
	 * <br/>
	 * <br/>
	 * Tokens:
	 * <ul>
	 * <li>{@code $routeColor1$} - Any text following this token will have the route color</li>
	 * <li>{@code $#FF9900$} - Any text following this token will have the color {@code FF9900}</li>
	 * <li>{@code $ifAUnder60m1{text1}else{text2}$} - If the arrival is under 60 minutes, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifAUnder60s1{text1}else{text2}$} - If the arrival is under 60 seconds, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifAUnder1s1{text1}else{text2}$} - If the arrival is under 1 second, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifDUnder60m1{text1}else{text2}$} - If the delay is under 60 minutes, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifDUnder60s1{text1}else{text2}$} - If the delay is under 60 seconds, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifDUnder1s1{text1}else{text2}$} - If the delay is under 1 second, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifRealtime1{text1}else{text2}$} - If the arrival is realtime, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifDelayed1{text1}else{text2}$} - If the deviation is positive, show {@code text1}; otherwise, show {@code text2}</li>
	 * <li>{@code $ifTerminating1{text1}else{text2}$} - If the arrival vehicle is terminating, show {@code text1}; otherwise, show {@code text2}</li>
	 * </ul>
	 * <br/>
	 * <br/>
	 * Columns:
	 * <ul>
	 * <li>{@code @40-50L@} - Any following text will be written across the screen from the 40% to 50% space, aligned left</li>
	 * <li>{@code @60-70C@} - Any following text will be written across the screen from the 60% to 70% space, centered</li>
	 * <li>{@code @80-90R@} - Any following text will be written across the screen from the 80% to 90% space, aligned right</li>
	 * </ul>
	 */
	private final String[] messages;
	private final TextFieldWidgetExtension[] textFieldMessages;
	private final TextFieldWidgetExtension displayPageInput;
	private final MutableText messageText = TextHelper.translatable("gui.mtr.pids_message");
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

		selectAllCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		selectAllCheckbox.setMessage2(new Text(TextHelper.translatable("gui.mtr.automatically_detect_nearby_platform").data));

		textFieldMessages = new TextFieldWidgetExtension[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, "");
		}

		buttonPrevPage = new TexturedButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_left.png"), new Identifier("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1));
		buttonNextPage = new TexturedButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_right.png"), new Identifier("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1));

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
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, y, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
			textFieldMessage.setText2(messages[i]);
			addChild(new ClickableWidget(textFieldMessage));
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
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < maxPages;
	}

	@Override
	public void tick2() {
		for (final TextFieldWidgetExtension textFieldMessage : textFieldMessages) {
			textFieldMessage.tick3();
		}
	}

	@Override
	public void onClose2() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getText2();
		}
		if (selectAllCheckbox.isChecked2()) {
			filterPlatformIds.clear();
		}
		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText2()) - 1);
		} catch (Exception e) {
			Init.logException(e);
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdatePIDSConfig(blockPos, messages, filterPlatformIds, displayPage));
		super.onClose2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		graphicsHolder.drawText(TextHelper.translatable("gui.mtr.display_page"), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(TextHelper.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.isChecked2() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(messageText, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
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
			if (station != null) {
				final ObjectImmutableList<DashboardListItem> platformsForList = getPlatformsForList(station);
				if (selectAllCheckbox.isChecked2()) {
					filterPlatformIds.clear();
				}
				MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(() -> {
					MinecraftClient.getInstance().openScreen(new Screen(thisScreen));
					selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
				}, new ObjectImmutableList<>(platformsForList), filterPlatformIds, false, false)));
			}
		});
	}

	public static ObjectImmutableList<DashboardListItem> getPlatformsForList(Station station) {
		final ObjectArrayList<DashboardListItem> platformsForList = new ObjectArrayList<>();
		final ObjectArrayList<Platform> platforms = new ObjectArrayList<>(station.savedRails);
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