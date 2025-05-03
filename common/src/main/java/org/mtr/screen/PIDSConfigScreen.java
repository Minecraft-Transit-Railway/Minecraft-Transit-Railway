package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockPIDSBase;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Platform;
import org.mtr.core.data.RoutePlatformData;
import org.mtr.core.data.Station;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdatePIDSConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.BetterTexturedButtonWidget;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class PIDSConfigScreen extends ScreenBase implements IGui {

	private final BlockPos blockPos;
	private final String[] messages;
	private final boolean[] hideArrivalArray;
	private final BetterTextFieldWidget[] textFieldMessages;
	private final CheckboxWidget[] buttonsHideArrival;
	private final BetterTextFieldWidget displayPageInput;
	private final MutableText messageText = TranslationProvider.GUI_MTR_PIDS_MESSAGE.getMutableText();
	private final MutableText hideArrivalText = TranslationProvider.GUI_MTR_HIDE_ARRIVAL.getMutableText();
	private final CheckboxWidget selectAllCheckbox;
	private final ButtonWidget filterButton;
	private final BetterTexturedButtonWidget buttonPrevPage;
	private final BetterTexturedButtonWidget buttonNextPage;
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

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			filterPlatformIds = new LongAVLTreeSet();
			displayPage = 0;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity instanceof BlockPIDSBase.BlockEntityBase) {
				filterPlatformIds = ((BlockPIDSBase.BlockEntityBase) blockEntity).getPlatformIds();
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity).getMessage(i);
					hideArrivalArray[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity).getHideArrival(i);
				}
				displayPage = ((BlockPIDSBase.BlockEntityBase) blockEntity).getDisplayPage();
			} else {
				filterPlatformIds = new LongAVLTreeSet();
				displayPage = 0;
			}
		}

		selectAllCheckbox = CheckboxWidget.builder(TranslationProvider.GUI_MTR_AUTOMATICALLY_DETECT_NEARBY_PLATFORM.getText(), textRenderer).checked(filterPlatformIds.isEmpty()).callback((checkboxWidget, checked) -> {
		}).build();

		textFieldMessages = new BetterTextFieldWidget[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new BetterTextFieldWidget(MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, "", text -> {
			});
		}

		buttonsHideArrival = new CheckboxWidget[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = CheckboxWidget.builder(hideArrivalText, textRenderer).callback((checkboxWidget, checked) -> {
			}).build();
		}

		buttonPrevPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_left.png"), Identifier.of("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1), true);
		buttonNextPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_right.png"), Identifier.of("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1), true);

		filterButton = getPlatformFilterButton(blockPos, selectAllCheckbox, filterPlatformIds, this);
		displayPageInput = new BetterTextFieldWidget(3, TextCase.DEFAULT, "\\D", "1", text -> {
		});
	}

	@Override
	protected void init() {
		super.init();
		final int customMessageWidth = textRenderer.getWidth(messageText) + SQUARE_SIZE + TEXT_PADDING;
		final int textWidth = textRenderer.getWidth(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;

		IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
		addDrawableChild(selectAllCheckbox);

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
		addDrawableChild(filterButton);

		IDrawing.setPositionAndWidth(displayPageInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH / 2 - TEXT_FIELD_PADDING);
		displayPageInput.setText(String.valueOf(displayPage + 1));
		addDrawableChild(displayPageInput);

		IDrawing.setPositionAndWidth(buttonPrevPage, customMessageWidth, SQUARE_SIZE * 7, SQUARE_SIZE);
		addDrawableChild(buttonPrevPage);

		IDrawing.setPositionAndWidth(buttonNextPage, customMessageWidth + SQUARE_SIZE * 3, SQUARE_SIZE * 7, SQUARE_SIZE);
		addDrawableChild(buttonNextPage);

		for (int i = 0; i < textFieldMessages.length; i++) {
			final BetterTextFieldWidget textFieldMessage = textFieldMessages[i];
			final int y = TEXT_FIELDS_Y_OFFSET + (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i % getMaxArrivalsPerPage());
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, y, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setText(messages[i]);
			addDrawableChild(textFieldMessage);

			final CheckboxWidget buttonHideArrival = buttonsHideArrival[i];
			IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, y + TEXT_FIELD_PADDING / 2, textWidth);
			IGui.setChecked(buttonHideArrival, hideArrivalArray[i]);
			addDrawableChild(buttonHideArrival);
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
	public void close() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getText();
			hideArrivalArray[i] = buttonsHideArrival[i].isChecked();
		}
		if (selectAllCheckbox.isChecked()) {
			filterPlatformIds.clear();
		}
		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText()) - 1);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
		RegistryClient.sendPacketToServer(new PacketUpdatePIDSConfig(blockPos, messages, hideArrivalArray, filterPlatformIds, displayPage));
		super.close();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		context.drawText(textRenderer, TranslationProvider.GUI_MTR_DISPLAY_PAGE.getMutableText(), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, TranslationProvider.GUI_MTR_FILTERED_PLATFORMS.getMutableText(selectAllCheckbox.isChecked() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, messageText, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE, false);
		final int maxPages = getMaxPages();
		if (maxPages > 1) {
			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, String.format("%s/%s", page + 1, maxPages), SQUARE_SIZE * 3 + textRenderer.getWidth(messageText) + TEXT_PADDING, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE);
		}
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private int getMaxArrivalsPerPage() {
		return Math.max(1, (height - TEXT_FIELDS_Y_OFFSET - SQUARE_SIZE) / (SQUARE_SIZE + TEXT_FIELD_PADDING));
	}

	private int getMaxPages() {
		return (int) Math.ceil((float) maxArrivals / getMaxArrivalsPerPage());
	}

	public static ButtonWidget getPlatformFilterButton(BlockPos blockPos, CheckboxWidget selectAllCheckbox, LongAVLTreeSet filterPlatformIds, Screen thisScreen) {
		return ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> {
			final Station station = MTRClient.findStation(blockPos);
			if (station != null) {
				final ObjectImmutableList<DashboardListItem> platformsForList = getPlatformsForList(station);
				if (selectAllCheckbox.isChecked()) {
					filterPlatformIds.clear();
				}
				MinecraftClient.getInstance().setScreen(new DashboardListSelectorScreen(() -> IGui.setChecked(selectAllCheckbox, filterPlatformIds.isEmpty()), new ObjectImmutableList<>(platformsForList), filterPlatformIds, false, false, thisScreen));
			}
		}).build();
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