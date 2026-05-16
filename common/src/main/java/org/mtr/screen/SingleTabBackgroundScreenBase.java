package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import net.minecraft.client.gui.screen.Screen;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ScrollPanelComponent;

import java.awt.*;

/**
 * Base class for simple modal-style Elementa screens that render on top of the standard Minecraft background panel.
 * The panel is centred automatically and exposes a single content container for subclasses to populate.
 */
public abstract class SingleTabBackgroundScreenBase extends WindowBase {

	protected final BackgroundComponent backgroundComponent;
	protected final ScrollComponent contentContainer;

	protected SingleTabBackgroundScreenBase(String title) {
		this((WindowBase) null, title);
	}

	protected SingleTabBackgroundScreenBase(@Nullable WindowBase previousScreen, String title) {
		super(previousScreen);
		backgroundComponent = createBackgroundComponent(title);
		contentContainer = setupContentContainer(title);
	}

	@Deprecated
	protected SingleTabBackgroundScreenBase(@Nullable Screen previousScreenLegacy, String title) {
		super(previousScreenLegacy);
		backgroundComponent = createBackgroundComponent(title);
		contentContainer = setupContentContainer(title);
	}

	private BackgroundComponent createBackgroundComponent(String title) {
		return new BackgroundComponent(getWindow(), ObjectImmutableList.of(new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), title)));
	}

	private ScrollComponent setupContentContainer(String title) {
		final UIWrappedText titleText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(backgroundComponent.containers[0])
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));
		titleText.setText(title);
		return ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(backgroundComponent.containers[0])
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;
	}
}
