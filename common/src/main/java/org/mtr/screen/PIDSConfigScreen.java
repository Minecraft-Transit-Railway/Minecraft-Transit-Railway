package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.ScaleConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.universal.UMinecraft;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockPIDSBase;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.packet.PacketUpdatePIDSConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.TextInputComponent;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Elementa screen for configuring PIDS custom messages and platform filters.
 */
public class PIDSConfigScreen extends SingleTabBackgroundScreenBase implements IGui {

	private final BlockPos blockPos;
	private final String[] messages;
	private final boolean[] hideArrivalArray;
	private final TextInputComponent[] textFieldMessages;
	private final CheckboxComponent[] buttonsHideArrival;
	private final TextInputComponent displayPageInput;
	private final CheckboxComponent selectAllCheckbox;
	private final ButtonComponent buttonPrevPage;
	private final ButtonComponent buttonNextPage;
	private final UIWrappedText filteredPlatformsText;
	private final UIWrappedText pageIndicatorText;
	private final LongAVLTreeSet filterPlatformIds;
	private final int displayPage;
	private final int maxArrivals;
	private int page = 0;

	private static final int MAX_MESSAGE_LENGTH = 2048;
	private static final int ARRIVALS_PER_PAGE = 6;

	public PIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		super(TranslationProvider.BLOCK_MTR_PIDS_1.getString());
		this.blockPos = blockPos;
		this.maxArrivals = maxArrivals;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}
		hideArrivalArray = new boolean[maxArrivals];

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			filterPlatformIds = new LongAVLTreeSet();
			displayPage = 0;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity instanceof BlockPIDSBase.BlockEntityBase) {
				filterPlatformIds = ((BlockPIDSBase.BlockEntityBase) blockEntity).getPlatformIds();
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity).getMessage(i);
					hideArrivalArray[i] = ((BlockPIDSBase.BlockEntityBase) blockEntity).getHideArrival(i);
				}
				displayPage = ((BlockPIDSBase.BlockEntityBase) blockEntity).getDisplayPage();
			} else {
				filterPlatformIds = new LongAVLTreeSet();
				displayPage = 0;
			}
		}

		selectAllCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		selectAllCheckbox.setText(TranslationProvider.GUI_MTR_AUTOMATICALLY_DETECT_NEARBY_PLATFORM.getString());
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());

		final ButtonComponent filterButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new PixelConstraint(80));
		filterButton.setText(net.minecraft.text.Text.translatable("selectWorld.edit").getString());
		filterButton.onClick(() -> openPlatformFilter(blockPos, selectAllCheckbox, filterPlatformIds, this));

		displayPageInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new PixelConstraint(60))
			.setHeight(new PixelConstraint(20));
		displayPageInput.setFilter("\\D");
		displayPageInput.setMaxLength(3);
		displayPageInput.setText(String.valueOf(displayPage + 1));
		displayPageInput.setPrefix(TranslationProvider.GUI_MTR_DISPLAY_PAGE.getString() + " ");

		final UIContainer pagingRow = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		buttonPrevPage = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(pagingRow)
			.setWidth(new PixelConstraint(30));
		buttonPrevPage.setText("<");
		buttonPrevPage.onClick(() -> setPage(page - 1));

		buttonNextPage = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(pagingRow)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(30));
		buttonNextPage.setText(">");
		buttonNextPage.onClick(() -> setPage(page + 1));

		pageIndicatorText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(pagingRow)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setColor(new java.awt.Color(GuiHelper.WHITE_COLOR));

		filteredPlatformsText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setColor(new java.awt.Color(GuiHelper.WHITE_COLOR));

		new UIWrappedText(TranslationProvider.GUI_MTR_PIDS_MESSAGE.getString(), false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setColor(new java.awt.Color(GuiHelper.WHITE_COLOR));

		textFieldMessages = new TextInputComponent[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			final UIContainer row = (UIContainer) new UIContainer()
				.setChildOf(contentContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));
			textFieldMessages[i] = (TextInputComponent) new TextInputComponent()
				.setChildOf(row)
				.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.75F))
				.setHeight(new PixelConstraint(20));
			textFieldMessages[i].setMaxLength(MAX_MESSAGE_LENGTH);
			textFieldMessages[i].setText(messages[i]);
		}

		buttonsHideArrival = new CheckboxComponent[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			final CheckboxComponent checkboxComponent = (CheckboxComponent) new CheckboxComponent()
				.setChildOf((UIContainer) textFieldMessages[i].getParent())
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint());
			checkboxComponent.setText(TranslationProvider.GUI_MTR_HIDE_ARRIVAL.getString());
			checkboxComponent.setChecked(hideArrivalArray[i]);
			buttonsHideArrival[i] = checkboxComponent;
		}

		setPage(0);
	}

	private void setPage(int newPage) {
		final int maxArrivalsPerPage = getMaxArrivalsPerPage();
		final int maxPages = getMaxPages() - 1;
		page = Math.clamp(newPage, 0, maxPages);
		for (int i = 0; i < textFieldMessages.length; i++) {
			if (i / maxArrivalsPerPage == page) {
				textFieldMessages[i].unhide(true);
			} else {
				textFieldMessages[i].hide(true);
			}
		}
		for (int i = 0; i < buttonsHideArrival.length; i++) {
			if (i / maxArrivalsPerPage == page) {
				buttonsHideArrival[i].unhide(true);
			} else {
				buttonsHideArrival[i].hide(true);
			}
		}
		buttonPrevPage.setDisabled(page <= 0);
		buttonNextPage.setDisabled(page >= maxPages);
		pageIndicatorText.setText(getMaxPages() > 1 ? String.format("%s/%s", page + 1, getMaxPages()) : "");
	}

	@Override
	public void onTick() {
		super.onTick();
		filteredPlatformsText.setText(TranslationProvider.GUI_MTR_FILTERED_PLATFORMS.getString(selectAllCheckbox.isChecked() ? 0 : filterPlatformIds.size()));
	}

	@Override
	public void onScreenClose() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getText();
			hideArrivalArray[i] = buttonsHideArrival[i].isChecked();
		}

		if (selectAllCheckbox.isChecked()) {
			filterPlatformIds.clear();
		}

		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText()) - 1);
		} catch (Exception e) {
			MTR.LOGGER.error("Failed to parse PIDS display page", e);
		}

		new PacketUpdatePIDSConfig(blockPos, messages, hideArrivalArray, filterPlatformIds, displayPage).send(MinecraftClient.getInstance().world);
		;
		super.onScreenClose();
	}

	private int getMaxArrivalsPerPage() {
		return ARRIVALS_PER_PAGE;
	}

	private int getMaxPages() {
		return (int) Math.ceil((float) maxArrivals / getMaxArrivalsPerPage());
	}

	public static void openPlatformFilter(BlockPos blockPos, CheckboxComponent selectAllCheckbox, LongAVLTreeSet filterPlatformIds, WindowBase thisScreen) {
		final Station station = MTRClient.findStation(blockPos);

		final ObjectImmutableList<DashboardListItem> platformsForList;
		if (station != null) {
			platformsForList = getPlatformsForList(new ObjectArrayList<>(station.savedRails));
		} else {
			ObjectArrayList<Platform> nearbyPlatforms = new ObjectArrayList<>();
			MTRClient.findClosePlatform(blockPos.down(4), 5, nearbyPlatforms::add);
			platformsForList = getPlatformsForList(nearbyPlatforms);
		}

		if (selectAllCheckbox.isChecked()) {
			filterPlatformIds.clear();
		}

		UMinecraft.setCurrentScreenObj(new DashboardListSelectorScreen(() -> selectAllCheckbox.setChecked(filterPlatformIds.isEmpty()), new ObjectImmutableList<>(platformsForList), filterPlatformIds, false, false, thisScreen));
	}

	public static ObjectImmutableList<DashboardListItem> getPlatformsForList(ObjectArrayList<Platform> platforms) {
		final ObjectArrayList<DashboardListItem> platformsForList = new ObjectArrayList<>();
		Collections.sort(platforms);
		platforms.forEach(platform -> platformsForList.add(new DashboardListItem(platform.getId(), platform.getName() + " " + IGui.mergeStations(MinecraftClientData.getInstance().simplifiedRoutes
			.stream()
			.filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platform.getId()) >= 0)
			.map(simplifiedRoute -> Utilities.getElement(simplifiedRoute.getPlatforms(), -1).getStationName())
			.collect(Collectors.toList())
		), 0)));
		return new ObjectImmutableList<>(platformsForList);
	}
}
