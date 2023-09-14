package org.mtr.mod.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.data.Station;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockRailwaySign;
import org.mtr.mod.block.BlockRouteSignBase;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.CustomResources;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketUpdateRailwaySignConfig;
import org.mtr.mod.render.RenderRailwaySign;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RailwaySignScreen extends ScreenExtension implements IGui {

	private int editingIndex;
	private int page;
	private int totalPages;
	private int columns;
	private int rows;

	private final BlockPos signPos;
	private final boolean isRailwaySign;
	private final int length;
	private final String[] signIds;
	private final LongAVLTreeSet selectedIds;
	private final ObjectAVLTreeSet<DashboardListItem> exitsForList = new ObjectAVLTreeSet<>();
	private final ObjectImmutableList<DashboardListItem> platformsForList;
	private final ObjectAVLTreeSet<DashboardListItem> routesForList;
	private final ObjectAVLTreeSet<DashboardListItem> stationsForList;
	private final ObjectAVLTreeSet<String> allSignIds = new ObjectAVLTreeSet<>();

	private final ButtonWidgetExtension[] buttonsEdit;
	private final ButtonWidgetExtension[] buttonsSelection;
	private final ButtonWidgetExtension buttonClear;
	private final TexturedButtonWidgetExtension buttonPrevPage;
	private final TexturedButtonWidgetExtension buttonNextPage;

	private static final int SIGN_SIZE = 32;
	private static final int SIGN_BUTTON_SIZE = 16;
	private static final int BUTTON_Y_START = SIGN_SIZE + SQUARE_SIZE + SQUARE_SIZE / 2;

	public RailwaySignScreen(BlockPos signPos) {
		super();
		editingIndex = -1;
		this.signPos = signPos;
		final ClientWorld world = MinecraftClient.getInstance().getWorldMapped();

		for (final BlockRailwaySign.SignType signType : BlockRailwaySign.SignType.values()) {
			allSignIds.add(signType.toString());
		}
		final List<String> sortedKeys = new ArrayList<>(CustomResources.CUSTOM_SIGNS.keySet());
		Collections.sort(sortedKeys);
		allSignIds.addAll(sortedKeys);

		final Station station = InitClient.findStation(signPos);
		if (station == null) {
			platformsForList = ObjectImmutableList.of();
			stationsForList = new ObjectAVLTreeSet<>();
			routesForList = new ObjectAVLTreeSet<>();
		} else {
			final Object2ObjectAVLTreeMap<String, ObjectArrayList<String>> exits = new Object2ObjectAVLTreeMap<>(); // TODO

			final ObjectArrayList<String> exitParents = new ObjectArrayList<>(exits.keySet());
			exitParents.sort(String::compareTo);
			exitParents.forEach(exitParent -> {
				final ObjectArrayList<String> destinations = exits.get(exitParent);
				exitsForList.add(new DashboardListItem(InitClient.serializeExit(exitParent), exitParent + " " + (destinations.isEmpty() ? "" : destinations.get(0)), 0));
			});

			platformsForList = PIDSConfigScreen.getPlatformsForList(station);

			final ObjectAVLTreeSet<Station> connectingStationsIncludingThisOne = new ObjectAVLTreeSet<>(station.connectedStations);
			connectingStationsIncludingThisOne.add(station);
			stationsForList = ClientData.convertDataSet(connectingStationsIncludingThisOne);

			routesForList = ClientData.convertDataSet(station.getOneInterchangeRouteFromEachColor(true));
		}

		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(signPos);
			if (entity != null && entity.data instanceof BlockRailwaySign.BlockEntity) {
				signIds = ((BlockRailwaySign.BlockEntity) entity.data).getSignIds();
				selectedIds = ((BlockRailwaySign.BlockEntity) entity.data).getSelectedIds();
				isRailwaySign = true;
			} else {
				signIds = new String[0];
				selectedIds = new LongAVLTreeSet();
				isRailwaySign = false;
				if (entity != null && entity.data instanceof BlockRouteSignBase.BlockEntityBase) {
					selectedIds.add(((BlockRouteSignBase.BlockEntityBase) entity.data).getPlatformId());
				}
			}
			final Block block = world.getBlockState(signPos).getBlock();
			if (block.data instanceof BlockRailwaySign) {
				length = ((BlockRailwaySign) block.data).length;
			} else {
				length = 0;
			}
		} else {
			length = 0;
			signIds = new String[0];
			selectedIds = new LongAVLTreeSet();
			isRailwaySign = false;
		}

		buttonsEdit = new ButtonWidgetExtension[length];
		for (int i = 0; i < buttonsEdit.length; i++) {
			final int index = i;
			buttonsEdit[i] = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("selectWorld.edit"), button -> edit(index));
		}

		buttonsSelection = new ButtonWidgetExtension[allSignIds.size()];
		for (int i = 0; i < allSignIds.size(); i++) {
			final int index = i;
			buttonsSelection[i] = new ButtonWidgetExtension(0, 0, 0, SIGN_BUTTON_SIZE, button -> setNewSignId(allSignIds.get(index)));
		}

		buttonClear = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.reset_sign"), button -> setNewSignId(null));
		buttonPrevPage = new TexturedButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier(Init.MOD_ID, "textures/gui/icon_left.png"), 20, 40, button -> setPage(page - 1));
		buttonNextPage = new TexturedButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, 0, 0, 20, new Identifier(Init.MOD_ID, "textures/gui/icon_right.png"), 20, 40, button -> setPage(page + 1));
	}

	@Override
	protected void init2() {
		super.init2();

		for (int i = 0; i < buttonsEdit.length; i++) {
			IDrawing.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
			addChild(new ClickableWidget(buttonsEdit[i]));
		}

		columns = Math.max((width - SIGN_BUTTON_SIZE * 3) / (SIGN_BUTTON_SIZE * 8) * 2, 1);
		rows = Math.max((height - SIGN_SIZE - SQUARE_SIZE * 4) / SIGN_BUTTON_SIZE, 1);

		final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
		final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

		totalPages = loopSigns((index, x, y, isBig) -> {
			IDrawing.setPositionAndWidth(buttonsSelection[index], (isBig ? xOffsetBig : xOffsetSmall) + x, BUTTON_Y_START + y, isBig ? SIGN_BUTTON_SIZE * 3 : SIGN_BUTTON_SIZE);
			buttonsSelection[index].visible = false;
			addChild(new ClickableWidget(buttonsSelection[index]));
		}, true);

		final int buttonClearX = (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2;
		final int buttonY = height - SQUARE_SIZE * 2;

		IDrawing.setPositionAndWidth(buttonClear, buttonClearX, buttonY, PANEL_WIDTH);
		buttonClear.visible = false;
		addChild(new ClickableWidget(buttonClear));

		IDrawing.setPositionAndWidth(buttonPrevPage, buttonClearX + PANEL_WIDTH, buttonY, SQUARE_SIZE);
		buttonPrevPage.visible = false;
		addChild(new ClickableWidget(buttonPrevPage));
		IDrawing.setPositionAndWidth(buttonNextPage, buttonClearX + PANEL_WIDTH + SQUARE_SIZE * 3, buttonY, SQUARE_SIZE);
		buttonNextPage.visible = false;
		addChild(new ClickableWidget(buttonNextPage));

		if (!isRailwaySign) {
			MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this::onClose2, platformsForList, selectedIds, true, false)));
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);

		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signIds[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, RenderRailwaySign.getMaxWidth(signIds, i, false), RenderRailwaySign.getMaxWidth(signIds, i, true), selectedIds, Direction.UP, 0, (textureId, x, y, size, flipTexture) -> {
					final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
					guiDrawing.beginDrawingTexture(textureId);
					guiDrawing.drawTexture((int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size);
					guiDrawing.finishDrawingTexture();
				});
			}
		}

		if (editingIndex >= 0) {
			final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
			final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

			loopSigns((index, x, y, isBig) -> {
				final String signId = allSignIds.get(index);
				final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signId);
				if (sign != null) {
					final boolean moveRight = sign.hasCustomText() && sign.flipCustomText;
					final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
					guiDrawing.beginDrawingTexture(sign.textureId);
					RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signId, (isBig ? xOffsetBig : xOffsetSmall) + x + (moveRight ? SIGN_BUTTON_SIZE * 2 : 0), BUTTON_Y_START + y, SIGN_BUTTON_SIZE, 2, 2, selectedIds, Direction.UP, 0, (textureId, x1, y1, size, flipTexture) -> guiDrawing.drawTexture((int) x1, (int) y1, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));
					guiDrawing.finishDrawingTexture();
				}
			}, false);

			graphicsHolder.drawCenteredText(String.format("%s/%s", page + 1, totalPages), (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2 + PANEL_WIDTH + SQUARE_SIZE * 2, height - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		}
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		setPage(page + (int) Math.signum(-amount));
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	public void onClose2() {
		RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignConfig(signPos, selectedIds, signIds));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	@Override
	public void resize2(MinecraftClient client, int width, int height) {
		super.resize2(client, width, height);
		for (ButtonWidgetExtension button : buttonsEdit) {
			button.active = true;
		}
		for (ButtonWidgetExtension button : buttonsSelection) {
			button.visible = false;
		}
		editingIndex = -1;
	}

	private int loopSigns(LoopSignsCallback loopSignsCallback, boolean ignorePage) {
		int pageCount = rows * columns;
		int indexSmall = 0;
		int indexBig = 0;
		int columnSmall = 0;
		int columnBig = 0;
		int rowSmall = 0;
		int rowBig = 0;
		int totalPagesSmallCount = 1;
		int totalPagesBigCount = 1;
		for (int i = 0; i < allSignIds.size(); i++) {
			final CustomResources.CustomSign sign = RenderRailwaySign.getSign(allSignIds.get(i));
			final boolean isBig = sign != null && sign.hasCustomText();

			final boolean onPage = (isBig ? indexBig : indexSmall) / pageCount == page;
			buttonsSelection[i].visible = onPage;
			if (ignorePage || onPage) {
				loopSignsCallback.loopSignsCallback(i, (isBig ? columnBig * 3 : columnSmall) * SIGN_BUTTON_SIZE, (isBig ? rowBig : rowSmall) * SIGN_BUTTON_SIZE, isBig);
			}

			if (isBig) {
				columnBig++;
				if (totalPagesBigCount < 0) {
					totalPagesBigCount = -totalPagesBigCount + 1;
				}
				if (columnBig >= columns) {
					columnBig = 0;
					rowBig++;
					if (rowBig >= rows) {
						rowBig = 0;
						totalPagesBigCount = -totalPagesBigCount;
					}
				}
				indexBig++;
			} else {
				columnSmall++;
				if (totalPagesSmallCount < 0) {
					totalPagesSmallCount = -totalPagesSmallCount + 1;
				}
				if (columnSmall >= columns) {
					columnSmall = 0;
					rowSmall++;
					if (rowSmall >= rows) {
						rowSmall = 0;
						totalPagesSmallCount = -totalPagesSmallCount;
					}
				}
				indexSmall++;
			}
		}
		return Math.max(Math.abs(totalPagesBigCount), Math.abs(totalPagesSmallCount));
	}

	private void edit(int editingIndex) {
		this.editingIndex = editingIndex;
		for (ButtonWidgetExtension button : buttonsEdit) {
			button.active = true;
		}
		buttonClear.visible = true;
		setPage(page);
		buttonsEdit[editingIndex].active = false;
	}

	private void setNewSignId(@Nullable String newSignId) {
		if (editingIndex >= 0 && editingIndex < signIds.length) {
			signIds[editingIndex] = newSignId;
			final boolean isExitLetter = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.EXIT_LETTER.toString()) || newSignId.equals(BlockRailwaySign.SignType.EXIT_LETTER_FLIPPED.toString()));
			final boolean isPlatform = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.PLATFORM.toString()) || newSignId.equals(BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString()));
			final boolean isLine = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.LINE.toString()) || newSignId.equals(BlockRailwaySign.SignType.LINE_FLIPPED.toString()));
			final boolean isStation = newSignId != null && (newSignId.equals(BlockRailwaySign.SignType.STATION.toString()) || newSignId.equals(BlockRailwaySign.SignType.STATION_FLIPPED.toString()));
			if ((isExitLetter || isPlatform || isLine || isStation)) {
				MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this, new ObjectImmutableList<>(isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList), selectedIds, false, false)));
			}
		}
	}

	private void setPage(int newPage) {
		page = MathHelper.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = editingIndex >= 0 && page > 0;
		buttonNextPage.visible = editingIndex >= 0 && page < totalPages - 1;
	}

	@FunctionalInterface
	private interface LoopSignsCallback {
		void loopSignsCallback(int index, int x, int y, boolean isBig);
	}
}
