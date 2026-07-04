package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.ScaleConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import org.jspecify.annotations.Nullable;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Home;
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
import org.mtr.widget.NumberInputComponent;
import org.mtr.widget.TextInputComponent;

public final class HomeScreen extends NameColorDataScreenBase<Home> {

	private final TextInputComponent minYTextInput;
	private final TextInputComponent maxYTextInput;
	private final NumberInputComponent populationNumberInput;

	private static final int MAX_POPULATION = 1000;

	public HomeScreen(Home home, @Nullable WindowBase previousScreen) {
		super(home, ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_HOME.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_HOME_COLOR.getString())
		), TranslationProvider.GUI_MTR_HOME_NAME, name -> TranslationProvider.GUI_MTR_HOME.getString(Utilities.formatName(name)), TranslationProvider.GUI_MTR_HOME_COLOR, previousScreen);

		GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_Y_BOUNDS.getString());

		final UIContainer zoneContainer = (UIContainer) new UIContainer()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		minYTextInput = createBoundsTextInput(zoneContainer, home.getMinY());
		maxYTextInput = createBoundsTextInput(zoneContainer, home.getMaxY());

		GuiHelper.createSpacing(firstTabScrollComponent);
		GuiHelper.createLabel(firstTabScrollComponent, TranslationProvider.GUI_MTR_POPULATION.getString());

		populationNumberInput = (NumberInputComponent) new NumberInputComponent(0, MAX_POPULATION, 1, false, null)
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		populationNumberInput.setValue(home.getPopulation());
	}

	@Override
	public void close() {
		try {
			final long minY = Long.parseLong(minYTextInput.getText());
			final long maxY = Long.parseLong(maxYTextInput.getText());
			data.setCorners(new Position(data.getMinX(), minY, data.getMinZ()), new Position(data.getMaxX(), maxY, data.getMaxZ()));
		} catch (Exception ignored) {
		}

		data.setPopulation((long) populationNumberInput.getValue());
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addHome(data)));
	}

	public static TextInputComponent createBoundsTextInput(UIContainer container, long existingValue) {
		final TextInputComponent textInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(container)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.5F))
			.setHeight(new PixelConstraint(20));

		textInput.setFilter("[^-\\d]");
		textInput.setText(String.valueOf(existingValue));
		return textInput;
	}
}
