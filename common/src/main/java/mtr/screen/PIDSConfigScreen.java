package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockPIDSBase;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
	private final Component messageText = Text.translatable("gui.mtr.pids_message");
	private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival");
	private final WidgetBetterCheckbox selectAllCheckbox;
	private final Button filterButton;
	private final Set<Long> filterPlatformIds;

	private static final int MAX_MESSAGE_LENGTH = 2048;

	public PIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals) {
		super(Text.literal(""));
		this.pos1 = pos1;
		this.pos2 = pos2;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}
		hideArrival = new boolean[maxArrivals];

		selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.automatically_detect_nearby_platform"), checked -> {
		});

		textFieldMessages = new WidgetBetterTextField[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new WidgetBetterTextField("", MAX_MESSAGE_LENGTH);
		}

		buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, hideArrivalText, checked -> {
			});
		}

		final Level world = Minecraft.getInstance().level;
		if (world == null) {
			filterPlatformIds = new HashSet<>();
		} else {
			final BlockEntity entity = world.getBlockEntity(pos1);
			if (entity instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				filterPlatformIds = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getPlatformIds();
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getMessage(i);
					hideArrival[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getHideArrival(i);
				}
			} else {
				filterPlatformIds = new HashSet<>();
			}
		}

		filterButton = getPlatformFilterButton(pos1, selectAllCheckbox, filterPlatformIds, this);
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

		for (int i = 0; i < textFieldMessages.length; i++) {
			final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setValue(messages[i]);
			addDrawableChild(textFieldMessage);

			final WidgetBetterCheckbox buttonHideArrival = buttonsHideArrival[i];
			IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, textWidth);
			buttonHideArrival.setChecked(hideArrival[i]);
			addDrawableChild(buttonHideArrival);
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
			hideArrival[i] = buttonsHideArrival[i].selected();
		}
		if (selectAllCheckbox.selected()) {
			filterPlatformIds.clear();
		}
		PacketTrainDataGuiClient.sendPIDSConfigC2S(pos1, pos2, messages, hideArrival, filterPlatformIds);
		super.onClose();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			font.draw(matrices, Text.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.selected() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
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
		return new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
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