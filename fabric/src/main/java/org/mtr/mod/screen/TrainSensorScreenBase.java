package org.mtr.mod.screen;

import org.mtr.core.data.Route;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockTrainSensorBase;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketUpdateTrainSensorConfig;

import java.util.Collections;

public abstract class TrainSensorScreenBase extends ScreenExtension implements IGui {

	private boolean stoppedOnly;
	private boolean movingOnly;

	protected final BlockPos blockPos;
	protected final TextFieldWidgetExtension[] textFields;

	private final LongAVLTreeSet filterRouteIds;
	private final int textFieldCount;
	private final MutableText[] textFieldLabels;
	private final CheckboxWidgetExtension stoppedOnlyCheckbox;
	private final CheckboxWidgetExtension movingOnlyCheckbox;
	private final ButtonWidgetExtension filterButton;
	private final boolean hasSpeedCheckboxes;
	private final int yStart;

	@SafeVarargs
	public TrainSensorScreenBase(BlockPos blockPos, boolean hasSpeedCheckboxes, ObjectObjectImmutablePair<TextFieldWidgetExtension, MutableText>... textFieldsAndLabels) {
		super();
		this.blockPos = blockPos;

		textFieldCount = textFieldsAndLabels.length;
		textFields = new TextFieldWidgetExtension[textFieldCount];
		textFieldLabels = new MutableText[textFieldCount];

		for (int i = 0; i < textFieldCount; i++) {
			textFields[i] = textFieldsAndLabels[i].left();
			textFieldLabels[i] = textFieldsAndLabels[i].right();
		}

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			filterRouteIds = new LongAVLTreeSet();
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null && blockEntity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
				filterRouteIds = ((BlockTrainSensorBase.BlockEntityBase) blockEntity.data).getRouteIds();
				stoppedOnly = ((BlockTrainSensorBase.BlockEntityBase) blockEntity.data).getStoppedOnly();
				movingOnly = ((BlockTrainSensorBase.BlockEntityBase) blockEntity.data).getMovingOnly();
			} else {
				filterRouteIds = new LongAVLTreeSet();
			}
		}

		stoppedOnlyCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> setChecked(checked, movingOnly));
		stoppedOnlyCheckbox.setMessage2(new Text(TextHelper.translatable("gui.mtr.stopped_only").data));
		movingOnlyCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> setChecked(stoppedOnly, checked));
		movingOnlyCheckbox.setMessage2(new Text(TextHelper.translatable("gui.mtr.moving_only").data));

		filterButton = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			final ObjectArrayList<DashboardListItem> routes = new ObjectArrayList<>(MinecraftClientData.convertDataSet(MinecraftClientData.getInstance().routes));
			Collections.sort(routes);
			MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this, new ObjectImmutableList<>(routes), filterRouteIds, false, false)));
		});

		this.hasSpeedCheckboxes = hasSpeedCheckboxes;
		yStart = (textFieldCount == 0 ? SQUARE_SIZE : SQUARE_SIZE * 3 + TEXT_HEIGHT + TEXT_PADDING * 2 + TEXT_FIELD_PADDING) + (hasSpeedCheckboxes ? 2 * SQUARE_SIZE : 0);
	}

	@Override
	protected void init2() {
		super.init2();

		final int textFieldWidth = textFieldCount == 0 ? 0 : (width - SQUARE_SIZE * 2) / textFieldCount;
		for (int i = 0; i < textFieldCount; i++) {
			IDrawing.setPositionAndWidth(textFields[i], SQUARE_SIZE + TEXT_FIELD_PADDING / 2 + textFieldWidth * i, SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING / 2, textFieldWidth - TEXT_FIELD_PADDING);
			addChild(new ClickableWidget(textFields[i]));
		}

		if (hasSpeedCheckboxes) {
			IDrawing.setPositionAndWidth(stoppedOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE * 2, PANEL_WIDTH);
			IDrawing.setPositionAndWidth(movingOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE, PANEL_WIDTH);
			addChild(new ClickableWidget(stoppedOnlyCheckbox));
			addChild(new ClickableWidget(movingOnlyCheckbox));
			setChecked(stoppedOnly, movingOnly);
		}

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, yStart + SQUARE_SIZE * 2, PANEL_WIDTH / 2);
		filterButton.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		addChild(new ClickableWidget(filterButton));
	}

	@Override
	public void tick2() {
		for (final TextFieldWidgetExtension textField : textFields) {
			textField.tick3();
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			for (int i = 0; i < textFieldCount; i++) {
				graphicsHolder.drawText(textFieldLabels[i], SQUARE_SIZE + (width / 2 - SQUARE_SIZE) * i, SQUARE_SIZE, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			}
			graphicsHolder.drawText(TextHelper.translatable("gui.mtr.filtered_routes", filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			graphicsHolder.drawText(TextHelper.translatable(filterRouteIds.isEmpty() ? "gui.mtr.filtered_routes_empty" : "gui.mtr.filtered_routes_condition"), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = MinecraftClientData.getInstance().routeIdMap.get(routeId);
				if (route != null) {
					graphicsHolder.drawText(TextHelper.literal(IGui.formatStationName(route.getName())), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
				}
				i += TEXT_HEIGHT;
			}
			renderAdditional(graphicsHolder);
			super.render(graphicsHolder, mouseX, mouseY, delta);
		} catch (Exception e) {
			Init.logException(e);
		}
	}

	@Override
	public void onClose2() {
		final String[] strings = new String[textFieldCount];
		for (int i = 0; i < textFieldCount; i++) {
			strings[i] = textFields[i].getText2();
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateTrainSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, getNumber(), strings));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	protected void renderAdditional(GraphicsHolder graphicsHolder) {
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
