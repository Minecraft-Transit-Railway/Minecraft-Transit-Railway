package mtr.gui;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.data.DataConverter;
import mtr.data.NameColorDataBase;
import mtr.data.Platform;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderRailwaySign;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;
import java.util.stream.Collectors;

public class RailwaySignScreen extends Screen implements IGui {

	private int editingIndex;
	private boolean isSelectingPlatform;
	private boolean isSelectingRoute;

	private final BlockPos signPos;
	private final boolean isRailwaySign;
	private final int length;
	private final BlockRailwaySign.SignType[] signTypes;
	private final Set<Long> selectedIds;
	private final List<Long> platformIds;
	private final List<Integer> routeColors;
	private final List<NameColorDataBase> platformsForList;
	private final List<NameColorDataBase> routesForList;
	private final List<Integer> availableIndices;
	private final List<Integer> selectedIndices;
	private final List<NameColorDataBase> availableData;
	private final List<NameColorDataBase> selectedData;

	private final ButtonWidget[] buttonsEdit;
	private final ButtonWidget[] buttonsSelection;
	private final ButtonWidget buttonClear;
	private final ButtonWidget buttonDone;

	private final DashboardList availableList;
	private final DashboardList selectedList;

	private static final int SIGN_SIZE = 32;
	private static final int ROW_START = 56;
	private static final int COLUMNS = 21;
	final BlockRailwaySign.SignType[] ALL_SIGN_TYPES = BlockRailwaySign.SignType.values();

	public RailwaySignScreen(BlockPos signPos) {
		super(new LiteralText(""));
		editingIndex = -1;
		this.signPos = signPos;
		availableIndices = new ArrayList<>();
		selectedIndices = new ArrayList<>();
		availableData = new ArrayList<>();
		selectedData = new ArrayList<>();
		final ClientWorld world = MinecraftClient.getInstance().world;

		final Optional<Long> stationId = ClientData.stations.stream().filter(station -> station.inStation(signPos.getX(), signPos.getZ())).map(station -> station.id).findFirst();
		List<NameColorDataBase> routesForList1 = new ArrayList<>();
		List<NameColorDataBase> platformsForList1 = new ArrayList<>();
		List<Integer> routeColors1 = new ArrayList<>();
		List<Long> platformIds1 = new ArrayList<>();
		if (stationId.isPresent()) {
			try {
				final List<Platform> platforms = new ArrayList<>(ClientData.platformsInStation.get(stationId.get()).values());
				platforms.sort(NameColorDataBase::compareTo);
				platformIds1 = platforms.stream().map(platform -> platform.id).collect(Collectors.toList());
				platformsForList1 = platforms.stream().map(platform -> new DataConverter(platform.name + " " + IGui.mergeStations(ClientData.platformToRoute.get(platform).stream().map(route -> route.stationDetails.get(route.stationDetails.size() - 1).stationName).collect(Collectors.toList())), 0)).collect(Collectors.toList());

				routeColors1 = new ArrayList<>();
				routesForList1 = new ArrayList<>();
				final Map<Integer, ClientData.ColorNamePair> routeMap = ClientData.routesInStation.get(stationId.get());
				List<Integer> finalRouteColors = routeColors1;
				List<NameColorDataBase> finalRoutesForList = routesForList1;
				routeMap.forEach((color, route) -> {
					finalRouteColors.add(color);
					finalRoutesForList.add(new DataConverter(route.name, route.color));
				});
			} catch (Exception e) {
				e.printStackTrace();
				platformIds1 = new ArrayList<>();
				routeColors1 = new ArrayList<>();
				platformsForList1 = new ArrayList<>();
				routesForList1 = new ArrayList<>();
			}
		}

		routesForList = routesForList1;
		platformsForList = platformsForList1;
		routeColors = routeColors1;
		platformIds = platformIds1;

		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				signTypes = ((BlockRailwaySign.TileEntityRailwaySign) entity).getSign();
				selectedIds = ((BlockRailwaySign.TileEntityRailwaySign) entity).getSelectedIds();
				isRailwaySign = true;
			} else {
				signTypes = new BlockRailwaySign.SignType[0];
				selectedIds = new HashSet<>();
				isRailwaySign = false;
				if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
					selectedIds.add(((BlockRouteSignBase.TileEntityRouteSignBase) entity).getPlatformId());
				}
			}
			if (world.getBlockState(signPos).getBlock() instanceof BlockRailwaySign) {
				length = ((BlockRailwaySign) world.getBlockState(signPos).getBlock()).length;
			} else {
				length = 0;
			}
		} else {
			length = 0;
			signTypes = new BlockRailwaySign.SignType[0];
			selectedIds = new HashSet<>();
			isRailwaySign = false;
		}

		buttonsEdit = new ButtonWidget[length];
		for (int i = 0; i < buttonsEdit.length; i++) {
			final int index = i;
			buttonsEdit[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("selectWorld.edit"), button -> edit(index));
		}

		buttonsSelection = new ButtonWidget[ALL_SIGN_TYPES.length];
		for (int i = 0; i < ALL_SIGN_TYPES.length; i++) {
			final int index = i;
			buttonsSelection[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> setNewSignType(ALL_SIGN_TYPES[index]));
		}

		buttonClear = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.reset_sign"), button -> setNewSignType(null));
		buttonDone = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> setIsSelecting(false, false));

		availableList = new DashboardList(this::addButton, this::addChild, null, null, null, this::onAdd, null, null);
		selectedList = new DashboardList(this::addButton, this::addChild, null, null, null, null, this::onDelete, null);
	}

	@Override
	protected void init() {
		super.init();
		setIsSelecting(!isRailwaySign, false);

		for (int i = 0; i < buttonsEdit.length; i++) {
			IGui.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
			addButton(buttonsEdit[i]);
		}

		int column = 0;
		int row = 0;
		for (int i = 0; i < buttonsSelection.length; i++) {
			final int columns = ALL_SIGN_TYPES[i].hasCustomText ? 3 : 1;

			IGui.setPositionAndWidth(buttonsSelection[i], (width - SQUARE_SIZE * COLUMNS) / 2 + column * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE * columns);
			buttonsSelection[i].visible = false;
			addButton(buttonsSelection[i]);

			column += columns;
			if (column >= COLUMNS) {
				column = 0;
				row++;
			}
		}

		availableList.y = SQUARE_SIZE * 2;
		availableList.height = height - SQUARE_SIZE * 5;
		availableList.width = PANEL_WIDTH;

		selectedList.y = SQUARE_SIZE * 2;
		selectedList.height = height - SQUARE_SIZE * 5;
		selectedList.width = PANEL_WIDTH;

		IGui.setPositionAndWidth(buttonClear, (width - SQUARE_SIZE * COLUMNS) / 2 + column * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE * (COLUMNS - column));
		buttonClear.visible = false;
		addButton(buttonClear);

		IGui.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		addButton(buttonDone);

		availableList.init();
		selectedList.init();
	}

	@Override
	public void tick() {
		availableList.tick();
		selectedList.tick();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);

			if (isSelectingPlatform || isSelectingRoute) {
				availableList.render(matrices, textRenderer, mouseX, mouseY, delta);
				selectedList.render(matrices, textRenderer, mouseX, mouseY, delta);
				super.render(matrices, mouseX, mouseY, delta);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.available"), width / 2 - PANEL_WIDTH / 2 - SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.selected"), width / 2 + PANEL_WIDTH / 2 + SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			} else {
				super.render(matrices, mouseX, mouseY, delta);
				if (client == null) {
					return;
				}

				for (int i = 0; i < signTypes.length; i++) {
					if (signTypes[i] != null) {
						client.getTextureManager().bindTexture(signTypes[i].id);
						RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signTypes[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, i, signTypes.length - i - 1, selectedIds, Direction.UP, (x, y, size, flipTexture) -> drawTexture(matrices, (int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));
					}
				}

				if (editingIndex >= 0) {
					int column = 0;
					int row = 0;
					for (final BlockRailwaySign.SignType signType : ALL_SIGN_TYPES) {
						final int columns = signType.hasCustomText ? 3 : 1;
						final boolean moveRight = signType.hasCustomText && signType.flipped;

						client.getTextureManager().bindTexture(signType.id);
						RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signType, (width - SQUARE_SIZE * COLUMNS) / 2F + (column + (moveRight ? 2 : 0)) * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE, 2, 2, selectedIds, Direction.UP, (x, y, size, flipTexture) -> drawTexture(matrices, (int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));

						column += columns;
						if (column >= COLUMNS) {
							column = 0;
							row++;
						}
					}
				}
			}
		} catch (
				Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableList.mouseMoved(mouseX, mouseY);
		selectedList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		availableList.mouseScrolled(mouseX, mouseY, amount);
		selectedList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendSignTypesC2S(signPos, selectedIds, signTypes);
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		for (ButtonWidget button : buttonsSelection) {
			button.visible = false;
		}
		editingIndex = -1;
	}

	private void edit(int editingIndex) {
		this.editingIndex = editingIndex;
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		for (ButtonWidget button : buttonsSelection) {
			button.visible = true;
		}
		buttonClear.visible = true;
		buttonsEdit[editingIndex].active = false;
	}

	private void setNewSignType(BlockRailwaySign.SignType newSignType) {
		if (editingIndex >= 0 && editingIndex < signTypes.length) {
			final boolean isPlatform = newSignType == BlockRailwaySign.SignType.PLATFORM || newSignType == BlockRailwaySign.SignType.PLATFORM_FLIPPED;
			final boolean isLine = newSignType == BlockRailwaySign.SignType.LINE || newSignType == BlockRailwaySign.SignType.LINE_FLIPPED;
			setIsSelecting(isPlatform, isLine);

			if (newSignType != null && newSignType.hasCustomText) {
				if (newSignType.flipped) {
					for (int i = editingIndex - 1; i >= 0; i--) {
						signTypes[i] = null;
					}
				} else {
					for (int i = editingIndex + 1; i < signTypes.length; i++) {
						signTypes[i] = null;
					}
				}
			}

			for (int i = 0; i < signTypes.length; i++) {
				if (signTypes[i] != null && signTypes[i].hasCustomText && (i < editingIndex && !signTypes[i].flipped || i > editingIndex && signTypes[i].flipped)) {
					signTypes[i] = null;
				}
			}

			signTypes[editingIndex] = newSignType;
		}
	}

	private void setIsSelecting(boolean isSelectingPlatform, boolean isSelectingRoute) {
		this.isSelectingPlatform = isSelectingPlatform;
		this.isSelectingRoute = isSelectingRoute;
		final boolean isSelecting = isSelectingPlatform || isSelectingRoute;
		for (final ButtonWidget button : buttonsEdit) {
			button.visible = !isSelecting;
		}
		for (final ButtonWidget button : buttonsSelection) {
			button.visible = !isSelecting;
		}
		buttonClear.visible = !isSelecting;
		buttonDone.visible = isSelecting && isRailwaySign;
		availableList.x = isSelecting ? width / 2 - PANEL_WIDTH - SQUARE_SIZE : width;
		selectedList.x = isSelecting ? width / 2 + SQUARE_SIZE : width;
		updateList();
	}

	private void updateList() {
		availableIndices.clear();
		selectedIndices.clear();
		availableData.clear();
		selectedData.clear();
		final List<NameColorDataBase> initialData = isSelectingPlatform ? platformsForList : routesForList;
		final List<? extends Number> idList = isSelectingPlatform ? platformIds : routeColors;

		for (int i = 0; i < initialData.size(); i++) {
			if (selectedIds.contains(idList.get(i).longValue())) {
				selectedIndices.add(i);
				selectedData.add(initialData.get(i));
			} else {
				availableIndices.add(i);
				availableData.add(initialData.get(i));
			}
		}

		availableList.setData(availableData, false, false, false, false, true, false);
		selectedList.setData(selectedData, false, false, false, false, false, true);
	}

	private void onAdd(NameColorDataBase data, int index) {
		final int finalIndex = availableIndices.get(availableData.indexOf(data));
		if (!isRailwaySign) {
			selectedIds.clear();
		}
		if (isSelectingPlatform) {
			selectedIds.add(platformIds.get(finalIndex));
		} else if (isSelectingRoute) {
			selectedIds.add((long) routeColors.get(finalIndex));
		}
		updateList();
	}

	private void onDelete(NameColorDataBase data, int index) {
		final int finalIndex = selectedIndices.get(selectedData.indexOf(data));
		if (isSelectingPlatform) {
			selectedIds.remove(platformIds.get(finalIndex));
		} else if (isSelectingRoute) {
			selectedIds.remove((long) routeColors.get(finalIndex));
		}
		updateList();
	}
}
