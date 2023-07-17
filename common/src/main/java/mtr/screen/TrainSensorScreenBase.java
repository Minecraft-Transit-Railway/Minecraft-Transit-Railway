package mtr.screen;

import mtr.block.BlockTrainSensorBase;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.data.Route;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public abstract class TrainSensorScreenBase extends ScreenMapper implements IGui, IPacket {

	private boolean stoppedOnly;
	private boolean movingOnly;

	protected final BlockPos pos;
	protected final WidgetBetterTextField[] textFields;

	private final Set<Long> filterRouteIds;
	private final int textFieldCount;
	private final Component[] textFieldLabels;
	private final WidgetBetterCheckbox stoppedOnlyCheckbox;
	private final WidgetBetterCheckbox movingOnlyCheckbox;
	private final Button filterButton;
	private final boolean hasSpeedCheckboxes;
	private final int yStart;

	@SafeVarargs
	public TrainSensorScreenBase(BlockPos pos, boolean hasSpeedCheckboxes, Tuple<WidgetBetterTextField, Component>... textFieldsAndLabels) {
		super(Text.literal(""));
		this.pos = pos;

		textFieldCount = textFieldsAndLabels.length;
		textFields = new WidgetBetterTextField[textFieldCount];
		textFieldLabels = new Component[textFieldCount];

		for (int i = 0; i < textFieldCount; i++) {
			textFields[i] = textFieldsAndLabels[i].getA();
			textFieldLabels[i] = textFieldsAndLabels[i].getB();
		}

		final Level world = Minecraft.getInstance().level;
		if (world == null) {
			filterRouteIds = new HashSet<>();
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				filterRouteIds = ((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).getRouteIds();
				stoppedOnly = ((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).getStoppedOnly();
				movingOnly = ((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).getMovingOnly();
			} else {
				filterRouteIds = new HashSet<>();
			}
		}

		stoppedOnlyCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.stopped_only"), checked -> setChecked(checked, movingOnly));
		movingOnlyCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.moving_only"), checked -> setChecked(stoppedOnly, checked));

		filterButton = UtilitiesClient.newButton(button -> {
			if (minecraft != null) {
				final List<NameColorDataBase> routes = new ArrayList<>(ClientData.ROUTES);
				Collections.sort(routes);
				UtilitiesClient.setScreen(minecraft, new DashboardListSelectorScreen(this, routes, filterRouteIds, false, false));
			}
		});

		this.hasSpeedCheckboxes = hasSpeedCheckboxes;
		yStart = (textFieldCount == 0 ? SQUARE_SIZE : SQUARE_SIZE * 3 + TEXT_HEIGHT + TEXT_PADDING * 2 + TEXT_FIELD_PADDING) + (hasSpeedCheckboxes ? 2 * SQUARE_SIZE : 0);
	}

	@Override
	protected void init() {
		super.init();

		final int textFieldWidth = textFieldCount == 0 ? 0 : (width - SQUARE_SIZE * 2) / textFieldCount;
		for (int i = 0; i < textFieldCount; i++) {
			IDrawing.setPositionAndWidth(textFields[i], SQUARE_SIZE + TEXT_FIELD_PADDING / 2 + textFieldWidth * i, SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING / 2, textFieldWidth - TEXT_FIELD_PADDING);
			addDrawableChild(textFields[i]);
		}

		if (hasSpeedCheckboxes) {
			IDrawing.setPositionAndWidth(stoppedOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE * 2, PANEL_WIDTH);
			IDrawing.setPositionAndWidth(movingOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE, PANEL_WIDTH);
			addDrawableChild(stoppedOnlyCheckbox);
			addDrawableChild(movingOnlyCheckbox);
			setChecked(stoppedOnly, movingOnly);
		}

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, yStart + SQUARE_SIZE * 2, PANEL_WIDTH / 2);
		filterButton.setMessage(Text.translatable("selectWorld.edit"));
		addDrawableChild(filterButton);
	}

	@Override
	public void tick() {
		for (final WidgetBetterTextField textField : textFields) {
			textField.tick();
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(guiGraphics);
			for (int i = 0; i < textFieldCount; i++) {
				guiGraphics.drawString(font, textFieldLabels[i], SQUARE_SIZE + (width / 2 - SQUARE_SIZE) * i, SQUARE_SIZE, ARGB_WHITE);
			}
			guiGraphics.drawString(font, Text.translatable("gui.mtr.filtered_routes", filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE);
			guiGraphics.drawString(font, Text.translatable(filterRouteIds.isEmpty() ? "gui.mtr.filtered_routes_empty" : "gui.mtr.filtered_routes_condition"), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = ClientData.DATA_CACHE.routeIdMap.get(routeId);
				if (route != null) {
					guiGraphics.drawString(font, Text.literal(IGui.formatStationName(route.name)), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE);
				}
				i += TEXT_HEIGHT;
			}
			renderAdditional(guiGraphics);
			super.render(guiGraphics, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		final String[] strings = new String[textFieldCount];
		for (int i = 0; i < textFieldCount; i++) {
			strings[i] = textFields[i].getValue();
		}
		PacketTrainDataGuiClient.sendTrainSensorC2S(pos, filterRouteIds, stoppedOnly, movingOnly, getNumber(), strings);
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected void renderAdditional(GuiGraphics guiGraphics) {
	}

	protected int getNumber() {
		return 0;
	}

	private void setChecked(boolean newStoppedOnly, boolean newMovingOnly) {
		if (newMovingOnly && stoppedOnly) {
			stoppedOnly = false;
		} else {
			stoppedOnly = newStoppedOnly;
		}
		if (newStoppedOnly && movingOnly) {
			movingOnly = false;
		} else {
			movingOnly = newMovingOnly;
		}
		stoppedOnlyCheckbox.setChecked(stoppedOnly);
		movingOnlyCheckbox.setChecked(movingOnly);
	}
}
