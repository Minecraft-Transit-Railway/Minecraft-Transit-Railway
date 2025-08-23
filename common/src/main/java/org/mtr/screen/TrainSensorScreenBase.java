package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockTrainSensorBase;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Route;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateTrainSensorConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

import java.util.stream.Collectors;

public abstract class TrainSensorScreenBase extends ScreenBase implements IGui {

	private boolean stoppedOnly;
	private boolean movingOnly;

	protected final BlockPos blockPos;
	protected final BetterTextFieldWidget[] textFields;

	private final LongAVLTreeSet filterRouteIds;
	private final int textFieldCount;
	private final MutableText[] textFieldLabels;
	private final CheckboxWidget stoppedOnlyCheckbox;
	private final CheckboxWidget movingOnlyCheckbox;
	private final ButtonWidget filterButton;
	private final boolean hasSpeedCheckboxes;
	private final int yStart;

	@SafeVarargs
	public TrainSensorScreenBase(BlockPos blockPos, boolean hasSpeedCheckboxes, ObjectObjectImmutablePair<BetterTextFieldWidget, MutableText>... textFieldsAndLabels) {
		super();
		this.blockPos = blockPos;

		textFieldCount = textFieldsAndLabels.length;
		textFields = new BetterTextFieldWidget[textFieldCount];
		textFieldLabels = new MutableText[textFieldCount];

		for (int i = 0; i < textFieldCount; i++) {
			textFields[i] = textFieldsAndLabels[i].left();
			textFieldLabels[i] = textFieldsAndLabels[i].right();
		}

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			filterRouteIds = new LongAVLTreeSet();
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity instanceof BlockTrainSensorBase.BlockEntityBase blockEntityBase) {
				filterRouteIds = blockEntityBase.getRouteIds();
				stoppedOnly = blockEntityBase.getStoppedOnly();
				movingOnly = blockEntityBase.getMovingOnly();
			} else {
				filterRouteIds = new LongAVLTreeSet();
			}
		}

		stoppedOnlyCheckbox = CheckboxWidget.builder(TranslationProvider.GUI_MTR_STOPPED_ONLY.getText(), textRenderer).callback((checkboxWidget, checked) -> setChecked(checked, movingOnly)).build();
		movingOnlyCheckbox = CheckboxWidget.builder(TranslationProvider.GUI_MTR_MOVING_ONLY.getText(), textRenderer).callback((checkboxWidget, checked) -> setChecked(stoppedOnly, checked)).build();

		filterButton = ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> {
			final ObjectArrayList<DashboardListItem> routes = MinecraftClientData.getInstance().simplifiedRoutes.stream().map(simplifiedRoute -> new DashboardListItem(simplifiedRoute.getId(), simplifiedRoute.getName(), simplifiedRoute.getColor())).sorted().collect(Collectors.toCollection(ObjectArrayList::new));
			MinecraftClient.getInstance().setScreen(new DashboardListSelectorScreen(new ObjectImmutableList<>(routes), filterRouteIds, false, false, this));
		}).build();

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
		addDrawableChild(filterButton);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context, mouseX, mouseY, delta);
			for (int i = 0; i < textFieldCount; i++) {
				context.drawText(textRenderer, textFieldLabels[i], SQUARE_SIZE + ((width - SQUARE_SIZE * 2) / textFieldLabels.length) * i, SQUARE_SIZE, ARGB_WHITE, false);
			}
			context.drawText(textRenderer, TranslationProvider.GUI_MTR_FILTERED_ROUTES.getMutableText(filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE, false);
			context.drawText(textRenderer, (filterRouteIds.isEmpty() ? TranslationProvider.GUI_MTR_FILTERED_ROUTES_EMPTY : TranslationProvider.GUI_MTR_FILTERED_ROUTES_CONDITION).getMutableText(), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = MinecraftClientData.getInstance().routeIdMap.get(routeId);
				if (route != null) {
					context.drawText(textRenderer, Text.literal(IGui.formatStationName(route.getName())), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE, false);
				}
				i += TEXT_HEIGHT;
			}
			renderAdditional(context);
			super.render(context, mouseX, mouseY, delta);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	@Override
	public void close() {
		sendUpdate(blockPos, filterRouteIds, stoppedOnly, movingOnly);
		super.close();
	}

	protected void renderAdditional(DrawContext context) {
	}

	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		RegistryClient.sendPacketToServer(new PacketUpdateTrainSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly));
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
		IGui.setChecked(stoppedOnlyCheckbox, stoppedOnly);
		IGui.setChecked(movingOnlyCheckbox, movingOnly);
	}
}
