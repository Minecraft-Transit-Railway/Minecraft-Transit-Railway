package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.FillConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Landmark;
import org.mtr.core.data.Position;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.Grouped24HourSlidersComponent;
import org.mtr.widget.ScrollPanelComponent;
import org.mtr.widget.TextInputComponent;

public final class LandmarkScreen extends NameColorDataScreenBase<Landmark> {

	private final TextInputComponent minYTextInput;
	private final TextInputComponent maxYTextInput;
	private final CheckboxComponent useMinecraftTimeCheckbox;
	private final CheckboxComponent useRealTimeCheckbox;
	private final Grouped24HourSlidersComponent minecraftTimeSliders;

	private static final int MAX_DENSITY = 1000;

	public LandmarkScreen(Landmark landmark, @Nullable WindowBase previousScreen) {
		super(landmark, ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_LANDMARK.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_LANDMARK_COLOR.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.CLOCK_TEXTURE.get(), TranslationProvider.GUI_MTR_DENSITIES.getString())
		), TranslationProvider.GUI_MTR_LANDMARK_NAME, name -> TranslationProvider.GUI_MTR_LANDMARK.getString(Utilities.formatName(name)), TranslationProvider.GUI_MTR_LANDMARK_COLOR, previousScreen);

		GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_Y_BOUNDS.getString());

		final UIContainer zoneContainer = (UIContainer) new UIContainer()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		minYTextInput = HomeScreen.createBoundsTextInput(zoneContainer, landmark.getMinY());
		maxYTextInput = HomeScreen.createBoundsTextInput(zoneContainer, landmark.getMaxY());

		useMinecraftTimeCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(backgroundComponent.containers[2])
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		useMinecraftTimeCheckbox.setText(TranslationProvider.GUI_MTR_SCHEDULE_MODE_MINECRAFT_TIME.getString());
		useMinecraftTimeCheckbox.setChecked(!landmark.getUseRealTime());
		useMinecraftTimeCheckbox.onClick(this::toggleUseMinecraftTime);

		useRealTimeCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(backgroundComponent.containers[2])
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		useRealTimeCheckbox.setText(TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME.getString());
		useRealTimeCheckbox.setChecked(landmark.getUseRealTime());
		useRealTimeCheckbox.onClick(this::toggleUseRealTime);

		GuiHelper.createSpacing(backgroundComponent.containers[2]);

		final ScrollComponent densitiesScrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(backgroundComponent.containers[2])
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new FillConstraint())).contentContainer;

		minecraftTimeSliders = (Grouped24HourSlidersComponent) new Grouped24HourSlidersComponent(MAX_DENSITY, Minecraft.getInstance().font.width(getSliderString(MAX_DENSITY)), LandmarkScreen::getSliderString)
			.setChildOf(densitiesScrollComponent)
			.setWidth(new RelativeConstraint());

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			minecraftTimeSliders.setValue(i, (int) landmark.getDensity(i));
		}
	}

	@Override
	public void close() {
		try {
			final long minY = Long.parseLong(minYTextInput.getText());
			final long maxY = Long.parseLong(maxYTextInput.getText());
			data.setCorners(new Position(data.getMinX(), minY, data.getMinZ()), new Position(data.getMaxX(), maxY, data.getMaxZ()));
		} catch (Exception ignored) {
		}

		data.setUseRealTime(useRealTimeCheckbox.isChecked());

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			data.setDensity(i, minecraftTimeSliders.getValues(i));
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addLandmark(data)));
	}

	private void toggleUseMinecraftTime() {
		useRealTimeCheckbox.setChecked(!useMinecraftTimeCheckbox.isChecked());
	}

	private void toggleUseRealTime() {
		useMinecraftTimeCheckbox.setChecked(!useRealTimeCheckbox.isChecked());
	}

	private static String getSliderString(int value) {
		return TranslationProvider.GUI_MTR_DENSITY_VALUE.getString(value);
	}
}
