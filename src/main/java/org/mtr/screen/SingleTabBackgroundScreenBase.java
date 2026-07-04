package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import net.minecraft.client.gui.screens.Screen;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ScrollPanelComponent;

import java.awt.*;

/**
 * Base class for simple modal-style Elementa screens that render on top of the standard Minecraft background panel.
 * The panel is centred automatically and exposes a single content container for subclasses to populate.
 */
public abstract class SingleTabBackgroundScreenBase extends WindowBase {

	protected final ScrollComponent contentContainer;
	private final BackgroundComponent backgroundComponent;

	protected SingleTabBackgroundScreenBase(String title) {
		this(null, title);
	}

	protected SingleTabBackgroundScreenBase(@Nullable WindowBase previousScreen, String title) {
		super(previousScreen);
		backgroundComponent = createBackgroundComponent();
		contentContainer = setupContentContainer(title);
	}

	@Deprecated
	protected SingleTabBackgroundScreenBase(@Nullable Screen previousScreenLegacy, String title) {
		super(previousScreenLegacy);
		backgroundComponent = createBackgroundComponent();
		contentContainer = setupContentContainer(title);
	}

	private BackgroundComponent createBackgroundComponent() {
		return new BackgroundComponent(getWindow(), ObjectImmutableList.of());
	}

	private ScrollComponent setupContentContainer(String title) {
		final UIWrappedText titleText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(backgroundComponent)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));
		titleText.setText(title);
		return ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(backgroundComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;
	}
}
