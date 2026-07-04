package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import net.minecraft.network.chat.Component;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.StitchedImageComponent;

import java.awt.*;

public final class DeleteConfirmationScreen extends WindowBase {

	private static final int INNER_PADDING = 8;
	private static final int OUTER_PADDING = 40;

	public DeleteConfirmationScreen(String title, Runnable onDelete, WindowBase previousScreen) {
		super(previousScreen);

		final StitchedImageComponent stitchedImageComponent = (StitchedImageComponent) new StitchedImageComponent(256, 256, 176, 222, 6, -INNER_PADDING, 6, 6, 170, 16, ReleasedDynamicTextureRegistry.BACKGROUND_TEXTURE.get())
			.setChildOf(getWindow())
			.setWidth(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(OUTER_PADDING * 2)), new PixelConstraint(160)))
			.setHeight(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(OUTER_PADDING * 2)), new ChildBasedSizeConstraint()))
			.setX(new CenterConstraint())
			.setY(new CenterConstraint());

		new UIWrappedText(TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getString(title), false)
			.setChildOf(stitchedImageComponent)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final UIContainer buttonContainer = (UIContainer) new UIContainer()
			.setChildOf(stitchedImageComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		final ButtonComponent deleteButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonContainer)
			.setWidth(new SubtractiveConstraint(new ScaleConstraint(new RelativeConstraint(), 0.5F), new PixelConstraint(GuiHelper.DEFAULT_PADDING / 2F)));

		deleteButton.setText(Component.translatable("gui.yes").getString());
		deleteButton.onClick(() -> {
			onDelete.run();
			markScreenForClose();
		});

		final ButtonComponent cancelButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonContainer)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new SubtractiveConstraint(new ScaleConstraint(new RelativeConstraint(), 0.5F), new PixelConstraint(GuiHelper.DEFAULT_PADDING / 2F)));

		cancelButton.setText(Component.translatable("gui.no").getString());
		cancelButton.onClick(this::markScreenForClose);
	}
}
