package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class EditNameColorScreenBase<T extends NameColorDataBase> extends ScreenMapper implements IGui, IPacket {

	private int nameStart;
	private int colorStart;
	private int colorEnd;

	protected final T data;
	private final DashboardScreen dashboardScreen;
	private final Component nameText;
	private final Component colorText;

	private final WidgetBetterTextField textFieldName;
	private final WidgetBetterTextField textFieldColor;

	public EditNameColorScreenBase(T data, DashboardScreen dashboardScreen, String nameKey, String colorKey) {
		super(new TextComponent(""));
		this.data = data;
		this.dashboardScreen = dashboardScreen;
		nameText = new TranslatableComponent(nameKey);
		colorText = new TranslatableComponent(colorKey);

		textFieldName = new WidgetBetterTextField(null, "");
		textFieldColor = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.HEX, "", DashboardScreen.MAX_COLOR_ZONE_LENGTH);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldColor.tick();
	}

	@Override
	public void onClose() {
		super.onClose();
		if (minecraft != null) {
			UtilitiesClient.setScreen(minecraft, dashboardScreen);
		}
		saveData();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected void setPositionsAndInit(int nameStart, int colorStart, int colorEnd) {
		this.nameStart = nameStart;
		this.colorStart = colorStart;
		this.colorEnd = colorEnd;

		super.init();
		final int yStart = SQUARE_SIZE + TEXT_FIELD_PADDING / 2;
		IDrawing.setPositionAndWidth(textFieldName, nameStart + TEXT_FIELD_PADDING / 2, yStart, colorStart - nameStart - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldColor, colorStart + TEXT_FIELD_PADDING / 2, yStart, colorEnd - colorStart - TEXT_FIELD_PADDING);

		textFieldName.setValue(data.name);
		textFieldColor.setValue(DashboardScreen.colorIntToString(data.color));
		textFieldColor.setResponder(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setValue(newText);
			}
		});

		addDrawableChild(textFieldName);
		addDrawableChild(textFieldColor);
	}

	protected void renderTextFields(PoseStack matrices) {
		drawCenteredString(matrices, font, nameText, (nameStart + colorStart) / 2, TEXT_PADDING, ARGB_WHITE);
		drawCenteredString(matrices, font, colorText, (colorStart + colorEnd) / 2, TEXT_PADDING, ARGB_WHITE);
	}

	protected void saveData() {
		data.name = textFieldName.getValue();
		data.color = DashboardScreen.colorStringToInt(textFieldColor.getValue());
	}
}
