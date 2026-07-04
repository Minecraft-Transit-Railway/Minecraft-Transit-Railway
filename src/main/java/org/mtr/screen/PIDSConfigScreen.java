package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.mtr.MTRClient;
import org.mtr.block.BlockPIDSBase;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdatePIDSConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

import java.awt.*;

/**
 * Elementa screen for configuring PIDS custom messages and platform filters.
 */
public final class PIDSConfigScreen extends WindowBase {

	private final BlockPos blockPos;
	private final LongAVLTreeSet filterPlatformIds;
	private final int maxArrivals;

	private final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of(
		new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_PIDS_OPTIONS.getString()),
		new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.OAK_SIGN_TEXTURE.get(), TranslationProvider.GUI_MTR_PIDS_MESSAGES.getString())
	));

	private final TextInputComponent[] messagesTextInputs;
	private final CheckboxComponent[] hideArrivalCheckboxes;
	private final NumberInputComponent displayPageNumberInput;
	private final CheckboxComponent selectAllCheckbox;
	private final UIWrappedText filteredPlatformsText;

	private static final int LEFT_WIDTH = 96;

	public PIDSConfigScreen(BlockPos blockPos, BlockPIDSBase.BlockEntityBase blockEntity) {
		this.blockPos = blockPos;
		this.maxArrivals = blockEntity.maxArrivals;

		final String[] messages = new String[maxArrivals];
		final boolean[] hideArrivalArray = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = blockEntity.getMessage(i);
			hideArrivalArray[i] = blockEntity.getHideArrival(i);
		}

		filterPlatformIds = blockEntity.getPlatformIds();

		final ScrollComponent scrollComponent1 = createMainComponents(0, TranslationProvider.GUI_MTR_PIDS_OPTIONS);
		filteredPlatformsText = GuiHelper.createLabel(scrollComponent1, "");

		final ButtonComponent filterButton = (ButtonComponent) new ButtonComponent(true)
			.setChildOf(scrollComponent1)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		GuiHelper.createSpacing(scrollComponent1);

		selectAllCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(scrollComponent1)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		filterButton.setText(Component.translatable("selectWorld.edit").getString());
		filterButton.onClick(() -> openPlatformFilter(blockPos, selectAllCheckbox, filterPlatformIds, this));
		selectAllCheckbox.setText(TranslationProvider.GUI_MTR_AUTOMATICALLY_DETECT_NEARBY_PLATFORM.getString());
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());

		GuiHelper.createSpacing(scrollComponent1);
		GuiHelper.createLabel(scrollComponent1, TranslationProvider.GUI_MTR_DISPLAY_PAGE.getString());

		displayPageNumberInput = (NumberInputComponent) new NumberInputComponent(1, 1000, 1, false, null)
			.setChildOf(scrollComponent1)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		displayPageNumberInput.setValue(blockEntity.getDisplayPage() + 1);

		final ScrollComponent scrollComponent2 = createMainComponents(1, TranslationProvider.GUI_MTR_PIDS_MESSAGES);

		messagesTextInputs = new TextInputComponent[maxArrivals];
		hideArrivalCheckboxes = new CheckboxComponent[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			final UIContainer rowContainer = (UIContainer) new UIContainer()
				.setChildOf(scrollComponent2)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			messagesTextInputs[i] = (TextInputComponent) new TextInputComponent()
				.setChildOf(rowContainer)
				.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.6F))
				.setHeight(new PixelConstraint(20));

			messagesTextInputs[i].setText(messages[i]);

			hideArrivalCheckboxes[i] = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(rowContainer)
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new SubtractiveConstraint(new ScaleConstraint(new RelativeConstraint(), 0.4F), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

			hideArrivalCheckboxes[i].setText(TranslationProvider.GUI_MTR_HIDE_ARRIVAL.getString());
			hideArrivalCheckboxes[i].setChecked(hideArrivalArray[i]);
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		filteredPlatformsText.setText(TranslationProvider.GUI_MTR_FILTERED_PLATFORMS.getString(selectAllCheckbox.isChecked() ? 0 : filterPlatformIds.size()));
	}

	@Override
	public void onScreenClose() {
		final String[] messages = new String[maxArrivals];
		final boolean[] hideArrivalArray = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = messagesTextInputs[i].getText();
			hideArrivalArray[i] = hideArrivalCheckboxes[i].isChecked();
		}

		if (selectAllCheckbox.isChecked()) {
			filterPlatformIds.clear();
		}

		final int displayPage = (int) Math.max(0, displayPageNumberInput.getValue() - 1);
		new PacketUpdatePIDSConfig(blockPos, messages, hideArrivalArray, filterPlatformIds, displayPage).send(Minecraft.getInstance().level);
		super.onScreenClose();
	}

	private ScrollComponent createMainComponents(int index, TranslationProvider.TranslationHolder title) {
		new UIWrappedText(title.getString(), false)
			.setChildOf(backgroundComponent.containers[index])
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		return ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(backgroundComponent.containers[index])
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;
	}

	public static void openPlatformFilter(BlockPos blockPos, CheckboxComponent selectAllCheckbox, LongAVLTreeSet filterPlatformIds, WindowBase thisScreen) {
		final Station station = MTRClient.findStation(blockPos);

		final ObjectArraySet<Platform> platforms;
		if (station != null) {
			platforms = station.savedRails;
		} else {
			platforms = new ObjectArraySet<>();
			MTRClient.findClosePlatform(blockPos.below(4), 5, platforms::add);
		}

		if (selectAllCheckbox.isChecked()) {
			filterPlatformIds.clear();
		}

		final PlatformListSelectorScreen platformListSelectorScreen = new PlatformListSelectorScreen(selectedPlatforms -> {
			filterPlatformIds.clear();
			selectedPlatforms.forEach(platform -> filterPlatformIds.add(platform.getId()));
			selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
		}, thisScreen);

		platformListSelectorScreen.setAvailableList(platforms);
		platforms.forEach(platform -> {
			if (filterPlatformIds.contains(platform.getId())) {
				platformListSelectorScreen.selectData(platform);
			}
		});

		UMinecraft.setCurrentScreenObj(platformListSelectorScreen);
	}
}
