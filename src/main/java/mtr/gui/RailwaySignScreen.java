package mtr.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderRailwaySign;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.stream.Collectors;

public class RailwaySignScreen extends Screen implements IGui {

	private int editingIndex;
	private boolean isSelectingExitLetter;
	private boolean isSelectingPlatform;
	private boolean isSelectingRoute;

	private int page;
	private int totalPages;
	private int columns;
	private int rows;

	private final BlockPos signPos;
	private final boolean isRailwaySign;
	private final int length;
	private final String[] signIds;
	private final Set<Long> selectedIds;
	private final List<Long> exitIds;
	private final List<Long> platformIds;
	private final List<Integer> routeColors;
	private final List<NameColorDataBase> exitsForList;
	private final List<NameColorDataBase> platformsForList;
	private final List<NameColorDataBase> routesForList;
	private final List<Integer> availableIndices;
	private final List<Integer> selectedIndices;
	private final List<NameColorDataBase> availableData;
	private final List<NameColorDataBase> selectedData;
	private final List<String> allSignIds = new ArrayList<>();

	private final ButtonWidget[] buttonsEdit;
	private final ButtonWidget[] buttonsSelection;
	private final ButtonWidget buttonClear;
	private final ButtonWidget buttonDone;
	private final TexturedButtonWidget buttonPrevPage;
	private final TexturedButtonWidget buttonNextPage;

	private final DashboardList availableList;
	private final DashboardList selectedList;

	private static final int SIGN_SIZE = 32;
	private static final int BUTTON_Y_START = SIGN_SIZE + SQUARE_SIZE + SQUARE_SIZE / 2;

	public RailwaySignScreen(BlockPos signPos) {
		super(new LiteralText(""));
		editingIndex = -1;
		this.signPos = signPos;
		availableIndices = new ArrayList<>();
		selectedIndices = new ArrayList<>();
		availableData = new ArrayList<>();
		selectedData = new ArrayList<>();
		final ClientWorld world = MinecraftClient.getInstance().world;

		exitsForList = new ArrayList<>();
		exitIds = new ArrayList<>();
		platformsForList = new ArrayList<>();
		platformIds = new ArrayList<>();
		routesForList = new ArrayList<>();
		routeColors = new ArrayList<>();

		for (final BlockRailwaySign.SignType signType : BlockRailwaySign.SignType.values()) {
			allSignIds.add(signType.toString());
		}
		final List<String> sortedKeys = new ArrayList<>(CustomResources.customSigns.keySet());
		Collections.sort(sortedKeys);
		allSignIds.addAll(sortedKeys);

		try {
			final Station station = ClientData.getStation(signPos);
			if (station != null) {
				final Map<String, List<String>> exits = station.getGeneratedExits();
				final List<String> exitParents = new ArrayList<>(exits.keySet());
				exitParents.sort(String::compareTo);
				exitParents.forEach(exitParent -> {
					exitIds.add(Station.serializeExit(exitParent));
					final List<String> destinations = exits.get(exitParent);
					exitsForList.add(new DataConverter(exitParent + " " + (destinations.size() > 0 ? destinations.get(0) : ""), 0));
				});

				final List<Platform> platforms = new ArrayList<>(ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id).values());
				Collections.sort(platforms);
				platforms.stream().map(platform -> platform.id).forEach(platformIds::add);
				platforms.stream().map(platform -> new DataConverter(platform.name + " " + IGui.mergeStations(ClientData.DATA_CACHE.requestPlatformIdToRoutes(platform.id).stream().map(route -> route.stationDetails.get(route.stationDetails.size() - 1).stationName).collect(Collectors.toList())), 0)).forEach(platformsForList::add);

				final Map<Integer, ClientCache.ColorNamePair> routeMap = ClientData.DATA_CACHE.stationIdToRoutes.get(station.id);
				routeMap.forEach((color, route) -> {
					routeColors.add(color);
					routesForList.add(new DataConverter(route.name, route.color));
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				signIds = ((BlockRailwaySign.TileEntityRailwaySign) entity).getSignIds();
				selectedIds = ((BlockRailwaySign.TileEntityRailwaySign) entity).getSelectedIds();
				isRailwaySign = true;
			} else {
				signIds = new String[0];
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
			signIds = new String[0];
			selectedIds = new HashSet<>();
			isRailwaySign = false;
		}

		buttonsEdit = new ButtonWidget[length];
		for (int i = 0; i < buttonsEdit.length; i++) {
			final int index = i;
			buttonsEdit[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("selectWorld.edit"), button -> edit(index));
		}

		buttonsSelection = new ButtonWidget[allSignIds.size()];
		for (int i = 0; i < allSignIds.size(); i++) {
			final int index = i;
			buttonsSelection[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> setNewSignId(allSignIds.get(index)));
		}

		buttonClear = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.reset_sign"), button -> setNewSignId(null));
		buttonDone = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> setIsSelecting(false, false, false));

		buttonPrevPage = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_left.png"), 20, 40, button -> setPage(page - 1));
		buttonNextPage = new TexturedButtonWidget(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier("mtr:textures/gui/icon_right.png"), 20, 40, button -> setPage(page + 1));

		availableList = new DashboardList(this::addDrawableChild, null, null, null, null, this::onAdd, null, null, () -> ClientData.ROUTES_PLATFORMS_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SEARCH = text);
		selectedList = new DashboardList(this::addDrawableChild, null, null, null, null, null, this::onDelete, null, () -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH = text);
	}

	@Override
	protected void init() {
		super.init();
		setIsSelecting(false, !isRailwaySign, false);

		for (int i = 0; i < buttonsEdit.length; i++) {
			IDrawing.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
			addDrawableChild(buttonsEdit[i]);
		}

		columns = Math.max((width - SQUARE_SIZE * 3) / (SQUARE_SIZE * 8) * 2, 1);
		rows = Math.max((height - SIGN_SIZE - SQUARE_SIZE * 4) / SQUARE_SIZE, 1);
		totalPages = allSignIds.size() / (rows * columns);

		final int xOffsetSmall = (width - SQUARE_SIZE * (columns * 4 + 3)) / 2 + SQUARE_SIZE;
		final int xOffsetBig = xOffsetSmall + SQUARE_SIZE * (columns + 1);

		loopSigns((index, x, y, isBig) -> {
			IDrawing.setPositionAndWidth(buttonsSelection[index], (isBig ? xOffsetBig : xOffsetSmall) + x, BUTTON_Y_START + y, isBig ? SQUARE_SIZE * 3 : SQUARE_SIZE);
			buttonsSelection[index].visible = false;
			addDrawableChild(buttonsSelection[index]);
		}, true);

		availableList.y = selectedList.y = SQUARE_SIZE * 2;
		availableList.height = selectedList.height = height - SQUARE_SIZE * 5;
		availableList.width = selectedList.width = PANEL_WIDTH;

		final int buttonClearX = (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2;
		final int buttonY = height - SQUARE_SIZE * 2;

		IDrawing.setPositionAndWidth(buttonClear, buttonClearX, buttonY, PANEL_WIDTH);
		buttonClear.visible = false;
		addDrawableChild(buttonClear);

		IDrawing.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, buttonY, PANEL_WIDTH);
		addDrawableChild(buttonDone);

		IDrawing.setPositionAndWidth(buttonPrevPage, buttonClearX + PANEL_WIDTH, buttonY, SQUARE_SIZE);
		buttonPrevPage.visible = false;
		addDrawableChild(buttonPrevPage);
		IDrawing.setPositionAndWidth(buttonNextPage, buttonClearX + PANEL_WIDTH + SQUARE_SIZE * 3, buttonY, SQUARE_SIZE);
		buttonNextPage.visible = false;
		addDrawableChild(buttonNextPage);

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

			if (isSelectingExitLetter || isSelectingPlatform || isSelectingRoute) {
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

				for (int i = 0; i < signIds.length; i++) {
					if (signIds[i] != null) {
						RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signIds[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, i, signIds.length - i - 1, selectedIds, Direction.UP, (textureId, x, y, size, flipTexture) -> {
							RenderSystem.setShader(GameRenderer::getPositionTexShader);
							RenderSystem.setShaderTexture(0, textureId);
							drawTexture(matrices, (int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size);
						});
					}
				}

				if (editingIndex >= 0) {
					final int xOffsetSmall = (width - SQUARE_SIZE * (columns * 4 + 3)) / 2 + SQUARE_SIZE;
					final int xOffsetBig = xOffsetSmall + SQUARE_SIZE * (columns + 1);

					loopSigns((index, x, y, isBig) -> {
						final String signId = allSignIds.get(index);
						final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signId);
						if (sign != null) {
							final boolean moveRight = sign.hasCustomText() && sign.flipCustomText;
							RenderSystem.setShader(GameRenderer::getPositionTexShader);
							RenderSystem.setShaderTexture(0, sign.textureId);
							RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signId, (isBig ? xOffsetBig : xOffsetSmall) + x + (moveRight ? SQUARE_SIZE * 2 : 0), BUTTON_Y_START + y, SQUARE_SIZE, 2, 2, selectedIds, Direction.UP, (textureId, x1, y1, size, flipTexture) -> drawTexture(matrices, (int) x1, (int) y1, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));
						}
					}, false);

					DrawableHelper.drawCenteredText(matrices, textRenderer, String.format("%s/%s", page + 1, totalPages), (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2 + PANEL_WIDTH + SQUARE_SIZE * 2, height - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
				}
			}
		} catch (Exception e) {
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

		if (!isSelectingPlatform && !isSelectingRoute && !isSelectingExitLetter) {
			setPage(page + (int) Math.signum(-amount));
		}

		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendSignIdsC2S(signPos, selectedIds, signIds);
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

	private void loopSigns(LoopSignsCallback loopSignsCallback, boolean ignorePage) {
		int pageCount = rows * columns;
		int indexSmall = 0;
		int indexBig = 0;
		int columnSmall = 0;
		int columnBig = 0;
		int rowSmall = 0;
		int rowBig = 0;
		for (int i = 0; i < allSignIds.size(); i++) {
			final CustomResources.CustomSign sign = RenderRailwaySign.getSign(allSignIds.get(i));
			final boolean isBig = sign != null && sign.hasCustomText();

			final boolean onPage = (isBig ? indexBig : indexSmall) / pageCount == page;
			buttonsSelection[i].visible = onPage;
			if (ignorePage || onPage) {
				loopSignsCallback.loopSignsCallback(i, (isBig ? columnBig * 3 : columnSmall) * SQUARE_SIZE, (isBig ? rowBig : rowSmall) * SQUARE_SIZE, isBig);
			}

			if (isBig) {
				columnBig++;
				if (columnBig >= columns) {
					columnBig = 0;
					rowBig++;
					if (rowBig >= rows) {
						rowBig = 0;
					}
				}
				indexBig++;
			} else {
				columnSmall++;
				if (columnSmall >= columns) {
					columnSmall = 0;
					rowSmall++;
					if (rowSmall >= rows) {
						rowSmall = 0;
					}
				}
				indexSmall++;
			}
		}
	}

	private void edit(int editingIndex) {
		this.editingIndex = editingIndex;
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		buttonClear.visible = true;
		setPage(page);
		buttonsEdit[editingIndex].active = false;
	}

	private void setNewSignId(String newSignId) {
		if (editingIndex >= 0 && editingIndex < signIds.length) {
			final boolean isExitLetter = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.EXIT_LETTER.toString()) || newSignId.equals(BlockRailwaySign.SignType.EXIT_LETTER_FLIPPED.toString()));
			final boolean isPlatform = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.PLATFORM.toString()) || newSignId.equals(BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString()));
			final boolean isLine = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.LINE.toString()) || newSignId.equals(BlockRailwaySign.SignType.LINE_FLIPPED.toString()));
			setIsSelecting(isExitLetter, isPlatform, isLine);
			final CustomResources.CustomSign newSign = RenderRailwaySign.getSign(newSignId);

			if (newSign != null && newSign.hasCustomText()) {
				if (newSign.flipCustomText) {
					for (int i = editingIndex - 1; i >= 0; i--) {
						signIds[i] = null;
					}
				} else {
					for (int i = editingIndex + 1; i < signIds.length; i++) {
						signIds[i] = null;
					}
				}
			}

			for (int i = 0; i < signIds.length; i++) {
				final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signIds[i]);
				if (signIds[i] != null && sign != null && sign.hasCustomText() && (i < editingIndex && !sign.flipCustomText || i > editingIndex && sign.flipCustomText)) {
					signIds[i] = null;
				}
			}

			signIds[editingIndex] = newSignId;
		}
	}

	private void setIsSelecting(boolean isSelectingExitLetter, boolean isSelectingPlatform, boolean isSelectingRoute) {
		this.isSelectingExitLetter = isSelectingExitLetter;
		this.isSelectingPlatform = isSelectingPlatform;
		this.isSelectingRoute = isSelectingRoute;
		final boolean isSelecting = isSelectingExitLetter || isSelectingPlatform || isSelectingRoute;
		for (final ButtonWidget button : buttonsEdit) {
			button.visible = !isSelecting;
		}
		if (isSelecting) {
			for (final ButtonWidget button : buttonsSelection) {
				button.visible = false;
			}
			buttonPrevPage.visible = false;
			buttonNextPage.visible = false;
		} else {
			setPage(page);
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
		final List<NameColorDataBase> initialData = isSelectingExitLetter ? exitsForList : isSelectingPlatform ? platformsForList : routesForList;
		final List<? extends Number> idList = isSelectingExitLetter ? exitIds : isSelectingPlatform ? platformIds : routeColors;

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

	private void setPage(int newPage) {
		page = MathHelper.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = page > 0;
		buttonNextPage.visible = page < totalPages - 1;
	}

	private void onAdd(NameColorDataBase data, int index) {
		try {
			final int finalIndex = availableIndices.get(availableData.indexOf(data));
			if (!isRailwaySign) {
				selectedIds.clear();
			}
			if (isSelectingExitLetter) {
				selectedIds.add(exitIds.get(finalIndex));
			} else if (isSelectingPlatform) {
				selectedIds.add(platformIds.get(finalIndex));
			} else if (isSelectingRoute) {
				selectedIds.add((long) routeColors.get(finalIndex));
			}
			updateList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onDelete(NameColorDataBase data, int index) {
		final int finalIndex = selectedIndices.get(selectedData.indexOf(data));
		if (isSelectingExitLetter) {
			selectedIds.remove(exitIds.get(finalIndex));
		} else if (isSelectingPlatform) {
			selectedIds.remove(platformIds.get(finalIndex));
		} else if (isSelectingRoute) {
			selectedIds.remove((long) routeColors.get(finalIndex));
		}
		updateList();
	}

	@FunctionalInterface
	private interface LoopSignsCallback {
		void loopSignsCallback(int index, int x, int y, boolean isBig);
	}
}
