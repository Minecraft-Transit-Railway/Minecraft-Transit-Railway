package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import net.minecraft.network.chat.Component;

public abstract class EditNameColorScreenBase<T extends NameColorDataBase> extends ScreenMapper implements IGui, IPacket {

	private int nameStart;
	private int colorStart;
	private int colorEnd;

	protected final T data;
	private final DashboardScreen dashboardScreen;
	private final Component nameText;
	private final Component colorText;

	private final WidgetBetterTextField textFieldName;
	private final WidgetColorSelector colorSelector;

	public EditNameColorScreenBase(T data, DashboardScreen dashboardScreen, String nameKey, String colorKey) {
		super(Text.literal(""));
		this.data = data;
		this.dashboardScreen = dashboardScreen;
		nameText = Text.translatable(nameKey);
		colorText = Text.translatable(colorKey);

		textFieldName = new WidgetBetterTextField("");
		colorSelector = new WidgetColorSelector(this, () -> {
		});
	}

	@Override
	public void tick() {
		textFieldName.tick();
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
		IDrawing.setPositionAndWidth(colorSelector, colorStart + TEXT_FIELD_PADDING / 2, yStart, colorEnd - colorStart - TEXT_FIELD_PADDING);

		textFieldName.setValue(data.name);
		colorSelector.setColor(data.color);

		addDrawableChild(textFieldName);
		addDrawableChild(colorSelector);
	}

	protected void renderTextFields(PoseStack matrices) {
		drawCenteredString(matrices, font, nameText, (nameStart + colorStart) / 2, TEXT_PADDING, ARGB_WHITE);
		drawCenteredString(matrices, font, colorText, (colorStart + colorEnd) / 2, TEXT_PADDING, ARGB_WHITE);
	}

	protected void saveData() {
		data.name = textFieldName.getValue();
		data.color = colorSelector.getColor();
	}
}
