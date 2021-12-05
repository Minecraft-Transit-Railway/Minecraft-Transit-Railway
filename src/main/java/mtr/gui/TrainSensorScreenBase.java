package mtr.gui;

import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.block.BlockTrainSensorBase;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.data.Route;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public abstract class TrainSensorScreenBase extends ScreenMapper implements IGui, IPacket {

	protected final BlockPos pos;
	protected final WidgetBetterTextField textField;

	private final Set<Long> filterRouteIds;
	private final Text textFieldLabel;
	private final ButtonWidget filterButton;
	private final int yStart;

	public TrainSensorScreenBase(BlockPos pos, WidgetBetterTextField textField, Text textFieldLabel) {
		super(new LiteralText(""));
		this.pos = pos;
		this.textField = textField;
		this.textFieldLabel = textFieldLabel;

		final World world = MinecraftClient.getInstance().world;
		if (world == null) {
			filterRouteIds = new HashSet<>();
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			filterRouteIds = entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase ? ((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).getRouteIds() : new HashSet<>();
		}

		filterButton = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			if (client != null) {
				final List<NameColorDataBase> routes = new ArrayList<>(ClientData.ROUTES);
				Collections.sort(routes);
				UtilitiesClient.setScreen(client, new DashboardListSelectorScreen(this, routes, filterRouteIds, false, false));
			}
		});

		yStart = textField == null ? SQUARE_SIZE : SQUARE_SIZE * 4 + TEXT_FIELD_PADDING;
	}

	@Override
	protected void init() {
		super.init();
		if (textField != null) {
			IDrawing.setPositionAndWidth(textField, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
			addDrawableChild(textField);
		}
		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, yStart + SQUARE_SIZE * 2, PANEL_WIDTH / 2);
		filterButton.setMessage(new TranslatableText("selectWorld.edit"));
		addDrawableChild(filterButton);
	}

	@Override
	public void tick() {
		if (textField != null) {
			textField.tick();
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (textFieldLabel != null) {
				textRenderer.draw(matrices, textFieldLabel, SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			}
			textRenderer.draw(matrices, new TranslatableText("gui.mtr.filtered_routes", filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE);
			textRenderer.draw(matrices, new TranslatableText(filterRouteIds.isEmpty() ? "gui.mtr.filtered_routes_empty" : "gui.mtr.filtered_routes_condition"), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = ClientData.DATA_CACHE.routeIdMap.get(routeId);
				if (route != null) {
					textRenderer.draw(matrices, new LiteralText(IGui.formatStationName(route.name)), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE);
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
		PacketTrainDataGuiClient.sendTrainSensorC2S(pos, filterRouteIds, getNumber(), getString());
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
}
