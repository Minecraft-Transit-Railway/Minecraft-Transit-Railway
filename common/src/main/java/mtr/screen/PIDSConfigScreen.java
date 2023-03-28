package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockArrivalProjectorBase;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;
import java.util.stream.Collectors;

public class PIDSConfigScreen extends ScreenMapper implements IGui, IPacket {

	private final BlockPos pos1;
	private final BlockPos pos2;
	private final String[] messages;
	private final boolean[] hideArrival;
	private final WidgetBetterTextField[] textFieldMessages;
	private final WidgetBetterCheckbox[] buttonsHideArrival;
	private final WidgetBetterTextField displayPageInput;
	private final Component messageText = Text.translatable("gui.mtr.pids_message");
	private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival");
	private final WidgetBetterCheckbox selectAllCheckbox;
	private final Button filterButton;
	private final ImageButton buttonNextPage;
	private final ImageButton buttonPrevPage;
	private final Set<Long> filterPlatformIds;
	private final int displayPage;
	private final int linesPerArrival;
	private final int MAX_ARRIVALS_PER_PAGE = 8;
	private int page = 0;
	private final int totalPages;

	private static final int MAX_MESSAGE_LENGTH = 2048;

	public PIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals, int linesPerArrival) {
		super(Text.literal(""));
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.linesPerArrival = linesPerArrival;
		messages = new String[maxArrivals * linesPerArrival];
		for (int i = 0; i < maxArrivals * linesPerArrival; i++) {
			messages[i] = "";
		}
		hideArrival = new boolean[maxArrivals];

		selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.automatically_detect_nearby_platform"), checked -> {
		});

		textFieldMessages = new WidgetBetterTextField[maxArrivals * linesPerArrival];
		for (int i = 0; i < maxArrivals * linesPerArrival; i++) {
			textFieldMessages[i] = new WidgetBetterTextField("", MAX_MESSAGE_LENGTH);
		}

		buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, hideArrivalText, checked -> {
			});
		}

		buttonNextPage = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_right.png"), 20, 40, button -> setPage(page + 1));
		buttonPrevPage = new ImageButton(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new ResourceLocation("mtr:textures/gui/icon_left.png"), 20, 40, button -> setPage(page - 1));

		totalPages = (int) Math.ceil((maxArrivals * linesPerArrival) / (float) MAX_ARRIVALS_PER_PAGE);
		page = 0;

		final Level world = Minecraft.getInstance().level;
		if (world == null) {
			filterPlatformIds = new HashSet<>();
			displayPage = 0;
		} else {
			final BlockEntity entity = world.getBlockEntity(pos1);
			if (entity instanceof IPIDS.TileEntityPIDS) {
				filterPlatformIds = ((IPIDS.TileEntityPIDS) entity).getPlatformIds();
				for (int i = 0; i < maxArrivals * linesPerArrival; i++) {
					messages[i] = ((IPIDS.TileEntityPIDS) entity).getMessage(i);
				}
				for (int i = 0; i < maxArrivals; i++) {
					hideArrival[i] = ((IPIDS.TileEntityPIDS) entity).getHideArrival(i);
				}
				displayPage = ((IPIDS.TileEntityPIDS) entity).getDisplayPage();
			} else {
				filterPlatformIds = new HashSet<>();
				displayPage = 0;
			}
		}

		filterButton = getPlatformFilterButton(pos1, selectAllCheckbox, filterPlatformIds, this);
		displayPageInput = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "1", 3);
	}

	@Override
	protected void init() {
		super.init();
		final int textWidth = font.width(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;

		IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
		addDrawableChild(selectAllCheckbox);

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
		filterButton.setMessage(Text.translatable("selectWorld.edit"));
		addDrawableChild(filterButton);

		IDrawing.setPositionAndWidth(displayPageInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH / 2 - TEXT_FIELD_PADDING);
		displayPageInput.setValue(String.valueOf(displayPage + 1));
		addDrawableChild(displayPageInput);

		IDrawing.setPositionAndWidth(buttonNextPage, SQUARE_SIZE * 4 + PANEL_WIDTH, SQUARE_SIZE * 7, SQUARE_SIZE);
		addDrawableChild(buttonNextPage);

		IDrawing.setPositionAndWidth(buttonPrevPage, SQUARE_SIZE + PANEL_WIDTH, SQUARE_SIZE * 7, SQUARE_SIZE);
		addDrawableChild(buttonPrevPage);

		for (int i = 0; i < textFieldMessages.length; i++) {
			final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
			int y = SQUARE_SIZE * 8 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i % MAX_ARRIVALS_PER_PAGE);
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, y, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setValue(messages[i]);
			if (Math.floor(i / (double) MAX_ARRIVALS_PER_PAGE) != page) {
				textFieldMessage.visible = false;
			}
			addDrawableChild(textFieldMessage);
			if (i % linesPerArrival == 0) {
				final int index = i / linesPerArrival;
				final WidgetBetterCheckbox buttonHideArrival = buttonsHideArrival[index];
				IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, y, textWidth);
				buttonHideArrival.setChecked(hideArrival[index]);
				if (Math.floor(i / (double) MAX_ARRIVALS_PER_PAGE) != page) {
					buttonHideArrival.visible = false;
				}
				addDrawableChild(buttonHideArrival);
			}
		}
	}

	private void setPage(int newPage) {
		page = Mth.clamp(newPage, 0, totalPages - 1);
		for (int i = 0; i < textFieldMessages.length; i++) {
			textFieldMessages[i].visible = Math.floor(i / (double) MAX_ARRIVALS_PER_PAGE) == page;
			if (i % linesPerArrival == 0) {
				final int index = i / linesPerArrival;
				buttonsHideArrival[index].visible = Math.floor(i / (double) MAX_ARRIVALS_PER_PAGE) == page;
			}
		}
	}

	@Override
	public void tick() {
		for (final WidgetBetterTextField textFieldMessage : textFieldMessages) {
			textFieldMessage.tick();
		}
	}

	@Override
	public void onClose() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getValue();
		}
		for (int i = 0; i < buttonsHideArrival.length; i++) {
			hideArrival[i] = buttonsHideArrival[i].selected();
		}
		if (selectAllCheckbox.selected()) {
			filterPlatformIds.clear();
		}
		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getValue()) - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PacketTrainDataGuiClient.sendPIDSConfigC2S(pos1, pos2, messages, hideArrival, filterPlatformIds, displayPage);
		super.onClose();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			font.draw(matrices, Text.translatable("gui.mtr.display_page"), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, Text.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.selected() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE);
			Gui.drawCenteredString(matrices, font, String.format("%s/%s", page + 1, totalPages), SQUARE_SIZE * 3 + PANEL_WIDTH, SQUARE_SIZE * 7 + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public static Button getPlatformFilterButton(BlockPos pos, WidgetBetterCheckbox selectAllCheckbox, Set<Long> filterPlatformIds, ScreenMapper thisScreen) {
		return UtilitiesClient.newButton(button -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station != null) {
				final List<NameColorDataBase> platformsForList = new ArrayList<>();
				final List<Platform> platforms = new ArrayList<>(ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id).values());
				Collections.sort(platforms);
				platforms.stream().map(platform -> new DataConverter(platform.id, platform.name + " " + IGui.mergeStations(ClientData.DATA_CACHE.requestPlatformIdToRoutes(platform.id).stream().map(route -> route.stationDetails.get(route.stationDetails.size() - 1).stationName).collect(Collectors.toList())), 0)).forEach(platformsForList::add);
				if (selectAllCheckbox.selected()) {
					filterPlatformIds.clear();
				}
				final Minecraft minecraft = Minecraft.getInstance();
				UtilitiesClient.setScreen(minecraft, new DashboardListSelectorScreen(() -> {
					UtilitiesClient.setScreen(minecraft, thisScreen);
					selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
				}, platformsForList, filterPlatformIds, false, false));
			}
		});
	}
}