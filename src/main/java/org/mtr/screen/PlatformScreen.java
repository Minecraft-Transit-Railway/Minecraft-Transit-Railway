package org.mtr.screen;

import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import org.jspecify.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Platform;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.DataHelper;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.MultiLineTextWidget;
import org.mtr.widget.NumberInputComponent;

import java.awt.*;

public final class PlatformScreen extends NameColorDataScreenBase<Platform> {

	@Nullable
	private final NumberInputComponent dwellTimeMinutesNumberInput;
	@Nullable
	private final NumberInputComponent dwellTimeSecondsNumberInput;

	private static final int MAX_DWELL_TIME_MINUTES = 10; // 10 minutes
	private static final int LEFT_WIDTH = 96;

	public PlatformScreen(Platform platform, @Nullable WindowBase previousScreen) {
		super(platform, ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_PLATFORM.getString())
		), TranslationProvider.GUI_MTR_PLATFORM_NUMBER, name -> (Utilities.formatName(platform.getStationName()) + "   " + TranslationProvider.GUI_MTR_PLATFORM.getString(Utilities.formatName(name))).trim(), null, previousScreen);

		if (!platform.getTransportMode().continuousMovement) {
			GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_DWELL_TIME.getString());

			dwellTimeMinutesNumberInput = (NumberInputComponent) new NumberInputComponent(0, MAX_DWELL_TIME_MINUTES, 1, false, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			dwellTimeMinutesNumberInput.setSuffix(TranslationProvider.GUI_MTR_MINUTES.getString(""));
			final long dwellTimeMinutes = platform.getDwellTime() / Utilities.MILLIS_PER_MINUTE;
			dwellTimeMinutesNumberInput.setValue(dwellTimeMinutes);

			dwellTimeSecondsNumberInput = (NumberInputComponent) new NumberInputComponent(0, 59.5, 0.5, true, null)
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new PixelConstraint(LEFT_WIDTH));

			dwellTimeSecondsNumberInput.setSuffix(TranslationProvider.GUI_MTR_SECONDS.getString(""));
			dwellTimeSecondsNumberInput.setValue(((double) (platform.getDwellTime() % Utilities.MILLIS_PER_MINUTE) / Utilities.MILLIS_PER_SECOND));
		} else {
			dwellTimeMinutesNumberInput = null;
			dwellTimeSecondsNumberInput = null;
		}

		new UIWrappedText(TranslationProvider.GUI_MTR_ROUTES_AT_PLATFORM.getMutableText(platform.routes.size()).getString(), false)
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		GuiHelper.createSpacing(firstTabScrollComponent);

		final MultiLineTextWidget multiLineTextWidget = (MultiLineTextWidget) new MultiLineTextWidget()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		final ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<String, @Nullable Color>>> lines = new ObjectArrayList<>();
		platform.routes.stream().sorted().forEach(route -> {
			final String routeNumber = Utilities.formatName(route.getRouteNumber());
			lines.add(ObjectArrayList.of(
				new ObjectObjectImmutablePair<>("- ", new Color(route.getColor())),
				new ObjectObjectImmutablePair<>(DataHelper.getNameOrUntitled(DataHelper.formatNameOnly(route.getName()) + (routeNumber.isEmpty() ? "" : " " + routeNumber)), null)
			));
		});
		multiLineTextWidget.write(lines);
	}

	@Override
	protected void close() {
		if (dwellTimeMinutesNumberInput != null && dwellTimeSecondsNumberInput != null) {
			data.setDwellTime((long) (dwellTimeMinutesNumberInput.getValue() * Utilities.MILLIS_PER_MINUTE + dwellTimeSecondsNumberInput.getValue() * Utilities.MILLIS_PER_SECOND));
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addPlatform(data)));
	}
}
