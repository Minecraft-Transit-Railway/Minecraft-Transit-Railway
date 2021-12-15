package mtr.gui;

import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.packet.IPacket;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class EditNameColorScreenBase<T extends NameColorDataBase> extends ScreenMapper implements IGui, IPacket {

	private int nameStart;
	private int colorStart;
	private int colorEnd;

	protected final T data;
	private final DashboardScreen dashboardScreen;
	private final Text nameText;
	private final Text colorText;

	private final WidgetBetterTextField textFieldName;
	private final WidgetBetterTextField textFieldColor;

	public EditNameColorScreenBase(T data, DashboardScreen dashboardScreen, String nameKey, String colorKey) {
		super(new LiteralText(""));
		this.data = data;
		this.dashboardScreen = dashboardScreen;
		nameText = new TranslatableText(nameKey);
		colorText = new TranslatableText(colorKey);

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
		if (client != null) {
			UtilitiesClient.setScreen(client, dashboardScreen);
		}

		data.name = textFieldName.getText();
		data.color = DashboardScreen.colorStringToInt(textFieldColor.getText());
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

		textFieldName.setText(data.name);
		textFieldColor.setText(DashboardScreen.colorIntToString(data.color));
		textFieldColor.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setText(newText);
			}
		});

		addDrawableChild(textFieldName);
		addDrawableChild(textFieldColor);
	}

	protected void renderTextFields(MatrixStack matrices) {
		drawCenteredText(matrices, textRenderer, nameText, (nameStart + colorStart) / 2, TEXT_PADDING, ARGB_WHITE);
		drawCenteredText(matrices, textRenderer, colorText, (colorStart + colorEnd) / 2, TEXT_PADDING, ARGB_WHITE);
	}
}
