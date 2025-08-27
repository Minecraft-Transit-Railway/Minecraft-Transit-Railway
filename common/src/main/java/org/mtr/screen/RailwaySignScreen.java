package org.mtr.screen;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.MTRClient;
import org.mtr.block.BlockRailwaySign;
import org.mtr.block.BlockRouteSignBase;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Station;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateRailwaySignConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderRailwaySign;
import org.mtr.resource.SignResource;
import org.mtr.widget.BetterTexturedButtonWidget;

import javax.annotation.Nullable;

public class RailwaySignScreen extends ScreenBase implements IGui {

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
	private final ObjectImmutableList<DashboardListItem> exitsForList;
	private final ObjectImmutableList<DashboardListItem> platformsForList;
	private final ObjectArraySet<DashboardListItem> routesForList;
	private final ObjectArraySet<DashboardListItem> stationsForList;
	private final ObjectArrayList<String> allSignIds = new ObjectArrayList<>();

	private final ButtonWidget[] buttonsEdit;
	private final ButtonWidget[] buttonsSelection;
	private final ButtonWidget buttonClear;
	private final BetterTexturedButtonWidget buttonPrevPage;
	private final BetterTexturedButtonWidget buttonNextPage;

	private static final int SIGN_SIZE = 32;
	private static final int SIGN_BUTTON_SIZE = 16;
	private static final int BUTTON_Y_START = SIGN_SIZE + SQUARE_SIZE + SQUARE_SIZE / 2;

	public RailwaySignScreen(BlockPos signPos) {
		super();
		editingIndex = -1;
		this.signPos = signPos;
		final ClientWorld world = MinecraftClient.getInstance().world;

		allSignIds.addAll(CustomResourceLoader.getSortedSignIds());

		final Station station = MTRClient.findStation(signPos);
		if (station == null) {
			exitsForList = ObjectImmutableList.of();
			platformsForList = ObjectImmutableList.of();
			stationsForList = new ObjectArraySet<>();
			routesForList = new ObjectArraySet<>();
		} else {
			exitsForList = ObjectImmutableList.of(); // TODO
			platformsForList = PIDSConfigScreen.getPlatformsForList(new ObjectArrayList<>(station.savedRails));

			final ObjectArraySet<Station> connectingStationsIncludingThisOne = new ObjectArraySet<>(station.connectedStations);
			connectingStationsIncludingThisOne.add(station);
			stationsForList = MinecraftClientData.convertDataSet(connectingStationsIncludingThisOne);

			final LongAVLTreeSet platformIds = new LongAVLTreeSet();
			connectingStationsIncludingThisOne.forEach(connectingStation -> connectingStation.savedRails.forEach(platform -> platformIds.add(platform.getId())));
			routesForList = new ObjectArraySet<>();
			final IntAVLTreeSet addedColors = new IntAVLTreeSet();
			MinecraftClientData.getInstance().simplifiedRoutes.forEach(simplifiedRoute -> {
				final int color = simplifiedRoute.getColor();
				if (!addedColors.contains(color) && simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
					routesForList.add(new DashboardListItem(color, simplifiedRoute.getName().split("\\|\\|")[0], color));
					addedColors.add(color);
				}
			});
		}

		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.RailwaySignBlockEntity railwaySignBlockEntity) {
				signIds = railwaySignBlockEntity.getSignIds();
				selectedIds = railwaySignBlockEntity.getSelectedIds();
				isRailwaySign = true;
			} else {
				signIds = new String[0];
				selectedIds = new LongAVLTreeSet();
				isRailwaySign = false;
				if (entity instanceof BlockRouteSignBase.BlockEntityBase) {
					selectedIds.add(((BlockRouteSignBase.BlockEntityBase) entity).getPlatformId());
				}
			}
			final Block block = world.getBlockState(signPos).getBlock();
			if (block instanceof BlockRailwaySign) {
				length = ((BlockRailwaySign) block).length;
			} else {
				length = 0;
			}
		} else {
			length = 0;
			signIds = new String[0];
			selectedIds = new LongAVLTreeSet();
			isRailwaySign = false;
		}

		buttonsEdit = new ButtonWidget[length];
		for (int i = 0; i < buttonsEdit.length; i++) {
			final int index = i;
			buttonsEdit[i] = ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> edit(index)).build();
		}

		buttonsSelection = new ButtonWidget[allSignIds.size()];
		for (int i = 0; i < allSignIds.size(); i++) {
			final int index = i;
			buttonsSelection[i] = ButtonWidget.builder(Text.empty(), button -> setNewSignId(allSignIds.get(index))).build();
		}

		buttonClear = ButtonWidget.builder(TranslationProvider.GUI_MTR_RESET.getMutableText(), button -> setNewSignId(null)).build();
		buttonPrevPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_left.png"), Identifier.of("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1), true);
		buttonNextPage = new BetterTexturedButtonWidget(Identifier.of("textures/gui/sprites/mtr/icon_right.png"), Identifier.of("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1), true);
	}

	@Override
	protected void init() {
		super.init();

		for (int i = 0; i < buttonsEdit.length; i++) {
			IDrawing.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
			addDrawableChild(buttonsEdit[i]);
		}

		columns = Math.max((width - SIGN_BUTTON_SIZE * 3) / (SIGN_BUTTON_SIZE * 8) * 2, 1);
		rows = Math.max((height - SIGN_SIZE - SQUARE_SIZE * 4) / SIGN_BUTTON_SIZE, 1);

		final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
		final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

		totalPages = loopSigns((index, x, y, isBig) -> {
			IDrawing.setPositionAndWidth(buttonsSelection[index], (isBig ? xOffsetBig : xOffsetSmall) + x, BUTTON_Y_START + y, isBig ? SIGN_BUTTON_SIZE * 3 : SIGN_BUTTON_SIZE);
			buttonsSelection[index].visible = false;
			addDrawableChild(buttonsSelection[index]);
		}, true);

		final int buttonClearX = (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2;
		final int buttonY = height - SQUARE_SIZE * 2;

		IDrawing.setPositionAndWidth(buttonClear, buttonClearX, buttonY, PANEL_WIDTH);
		buttonClear.visible = false;
		addDrawableChild(buttonClear);

		IDrawing.setPositionAndWidth(buttonPrevPage, buttonClearX + PANEL_WIDTH, buttonY, SQUARE_SIZE);
		buttonPrevPage.visible = false;
		addDrawableChild(buttonPrevPage);
		IDrawing.setPositionAndWidth(buttonNextPage, buttonClearX + PANEL_WIDTH + SQUARE_SIZE * 3, buttonY, SQUARE_SIZE);
		buttonNextPage.visible = false;
		addDrawableChild(buttonNextPage);

		if (!isRailwaySign) {
			MinecraftClient.getInstance().setScreen(new DashboardListSelectorScreen(this::close, platformsForList, selectedIds, true, false, null));
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		for (int i = 0; i < signIds.length; i++) {
			if (signIds[i] != null) {
				RenderRailwaySign.drawSign(null, signPos, signIds[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, RenderRailwaySign.getMaxWidth(signIds, i, false), RenderRailwaySign.getMaxWidth(signIds, i, true), selectedIds, Direction.UP, 0, (textureId, x, y, size, flipTexture) -> {
//					context.drawGuiTexture(RenderLayer::getGuiTextured, textureId, x, y, x + size, y + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
				});
			}
		}

		if (editingIndex >= 0) {
			final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
			final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

			loopSigns((index, x, y, isBig) -> {
				final String signId = allSignIds.get(index);
				final SignResource sign = RenderRailwaySign.getSign(signId);
				if (sign != null) {
					final boolean moveRight = sign.hasCustomText && sign.getFlipCustomText();
					RenderRailwaySign.drawSign(null, signPos, signId, (isBig ? xOffsetBig : xOffsetSmall) + x + (moveRight ? SIGN_BUTTON_SIZE * 2 : 0), BUTTON_Y_START + y, SIGN_BUTTON_SIZE, 2, 2, selectedIds, Direction.UP, 0, (textureId, x1, y1, size, flipTexture) -> {
//						context.drawGuiTexture(RenderLayer::getGuiTextured, textureId, x1, y1, x1 + size, y1 + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
					});
				}
			}, false);

			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, String.format("%s/%s", page + 1, totalPages), (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2 + PANEL_WIDTH + SQUARE_SIZE * 2, height - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		setPage(page + (int) Math.signum(-verticalAmount));
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public void close() {
		RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignConfig(signPos, selectedIds, signIds));
		super.close();
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
			final SignResource sign = RenderRailwaySign.getSign(allSignIds.get(i));
			final boolean isBig = sign != null && sign.hasCustomText;

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
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		buttonClear.visible = true;
		setPage(page);
		buttonsEdit[editingIndex].active = false;
	}

	private void setNewSignId(@Nullable String newSignId) {
		if (editingIndex >= 0 && editingIndex < signIds.length) {
			signIds[editingIndex] = newSignId;
			final boolean isExitLetter = newSignId != null && (newSignId.equals("exit_letter") || newSignId.equals("exit_letter_flipped"));
			final boolean isPlatform = newSignId != null && (newSignId.equals("platform") || newSignId.equals("platform_flipped"));
			final boolean isLine = newSignId != null && (newSignId.equals("line") || newSignId.equals("line_flipped"));
			final boolean isStation = newSignId != null && (newSignId.equals("station") || newSignId.equals("station_flipped"));
			if ((isExitLetter || isPlatform || isLine || isStation)) {
				MinecraftClient.getInstance().setScreen(new DashboardListSelectorScreen(new ObjectImmutableList<>(isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList), selectedIds, false, false, this));
			}
		}
	}

	private void setPage(int newPage) {
		page = Math.clamp(newPage, 0, totalPages - 1);
		buttonPrevPage.visible = editingIndex >= 0 && page > 0;
		buttonNextPage.visible = editingIndex >= 0 && page < totalPages - 1;
	}

	@FunctionalInterface
	private interface LoopSignsCallback {
		void loopSignsCallback(int index, int x, int y, boolean isBig);
	}
}
