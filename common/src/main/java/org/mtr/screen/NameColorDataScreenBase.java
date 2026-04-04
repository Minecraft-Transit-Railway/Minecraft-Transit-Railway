package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ColorInputComponent;
import org.mtr.widget.ScrollPanelComponent;
import org.mtr.widget.TextInputComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.Function;

public abstract class NameColorDataScreenBase<T extends NameColorDataBase> extends WindowBase {

	protected final T data;

	protected final BackgroundComponent backgroundComponent;
	protected final ScrollComponent firstTabScrollComponent;

	private final Function<String, String> nameLabelFormatter;

	private final UIWrappedText titleText;
	private final TextInputComponent nameTextInput;
	@Nullable
	private final ColorInputComponent colorInputComponent;

	public NameColorDataScreenBase(
			T data,
			ObjectImmutableList<ObjectObjectImmutablePair<ReleasedDynamicTexture, String>> tabs,
			TranslationProvider.TranslationHolder nameLabel,
			Function<String, String> nameLabelFormatter,
			@Nullable TranslationProvider.TranslationHolder colorLabel,
			@Nullable ScreenBase previousScreenLegacy
	) {
		super(previousScreenLegacy);
		this.data = data;
		this.nameLabelFormatter = nameLabelFormatter;

		backgroundComponent = new BackgroundComponent(getWindow(), tabs);

		titleText = (UIWrappedText) new UIWrappedText("", false)
				.setChildOf(backgroundComponent.containers[0])
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		firstTabScrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
				.setChildOf(backgroundComponent.containers[0])
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		GuiHelper.createLabel(firstTabScrollComponent, nameLabel.getString());

		nameTextInput = (TextInputComponent) new TextInputComponent()
				.setChildOf(firstTabScrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		nameTextInput.setText(data.getName());
		nameTextInput.onChange(this::updateTitle);
		updateTitle();

		GuiHelper.createSpacing(firstTabScrollComponent);

		if (colorLabel == null) {
			colorInputComponent = null;
		} else {
			new UIWrappedText(colorLabel.getString(), false)
					.setChildOf(backgroundComponent.containers[1])
					.setWidth(new RelativeConstraint())
					.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

			colorInputComponent = (ColorInputComponent) new ColorInputComponent()
					.setChildOf(backgroundComponent.containers[1])
					.setWidth(new RelativeConstraint())
					.setHeight(new RelativeConstraint());

			colorInputComponent.setSelectedColor(new Color(data.getColor()));
		}
	}

	@Override
	public final void onScreenClose() {
		data.setName(nameTextInput.getText());
		if (colorInputComponent != null) {
			data.setColor(colorInputComponent.getSelectedColor().getRGB());
		}

		onClose();
		super.onScreenClose();
	}

	protected abstract void onClose();

	private void updateTitle() {
		titleText.setText(nameLabelFormatter.apply(nameTextInput.getText()));
	}
}
