package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockTrainSensorBase;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.data.Route;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public abstract class TrainSensorScreenBase extends ScreenMapper implements IGui, IPacket {

	private boolean stoppedOnly;
	private boolean movingOnly;

	protected final BlockPos pos;
	protected final WidgetBetterTextField textField;

	private final Set<Long> filterRouteIds;
	private final Component textFieldLabel;
	private final WidgetBetterCheckbox stoppedOnlyCheckbox;
	private final WidgetBetterCheckbox movingOnlyCheckbox;
	private final Button filterButton;
	private final boolean hasSpeedCheckboxes;
	private final int yStart;

	public TrainSensorScreenBase(BlockPos pos, boolean hasSpeedCheckboxes, WidgetBetterTextField textField, Component textFieldLabel) {
		super(new TextComponent(""));
		this.pos = pos;
		this.textField = textField;
		this.textFieldLabel = textFieldLabel;

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

		stoppedOnlyCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.stopped_only"), checked -> setChecked(checked, movingOnly));
		movingOnlyCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.moving_only"), checked -> setChecked(stoppedOnly, checked));

		filterButton = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> {
			if (minecraft != null) {
				final List<NameColorDataBase> routes = new ArrayList<>(ClientData.ROUTES);
				Collections.sort(routes);
				UtilitiesClient.setScreen(minecraft, new DashboardListSelectorScreen(this, routes, filterRouteIds, false, false));
			}
		});

		this.hasSpeedCheckboxes = hasSpeedCheckboxes;
		yStart = (textField == null ? SQUARE_SIZE : SQUARE_SIZE * 4 + TEXT_FIELD_PADDING) + (hasSpeedCheckboxes ? 2 * SQUARE_SIZE : 0);
	}

	@Override
	protected void init() {
		super.init();

		if (textField != null) {
			IDrawing.setPositionAndWidth(textField, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
			addDrawableChild(textField);
		}

		if (hasSpeedCheckboxes) {
			IDrawing.setPositionAndWidth(stoppedOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE * 2, PANEL_WIDTH);
			IDrawing.setPositionAndWidth(movingOnlyCheckbox, SQUARE_SIZE, yStart - SQUARE_SIZE, PANEL_WIDTH);
			addDrawableChild(stoppedOnlyCheckbox);
			addDrawableChild(movingOnlyCheckbox);
			setChecked(stoppedOnly, movingOnly);
		}

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, yStart + SQUARE_SIZE * 2, PANEL_WIDTH / 2);
		filterButton.setMessage(new TranslatableComponent("selectWorld.edit"));
		addDrawableChild(filterButton);
	}

	@Override
	public void tick() {
		if (textField != null) {
			textField.tick();
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (textFieldLabel != null) {
				font.draw(matrices, textFieldLabel, SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}
			font.draw(matrices, new TranslatableComponent("gui.mtr.filtered_routes", filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, new TranslatableComponent(filterRouteIds.isEmpty() ? "gui.mtr.filtered_routes_empty" : "gui.mtr.filtered_routes_condition"), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = ClientData.DATA_CACHE.routeIdMap.get(routeId);
				if (route != null) {
					font.draw(matrices, new TextComponent(IGui.formatStationName(route.name)), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE);
				}
				i += TEXT_HEIGHT;
			}
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendTrainSensorC2S(pos, filterRouteIds, stoppedOnly, movingOnly, getNumber(), getString());
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected int getNumber() {
		return 0;
	}

	protected String getString() {
		return "";
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
