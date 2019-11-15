package mtr.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import mtr.MTR;
import mtr.message.MessageOBAController;
import mtr.tile.TileEntityOBAController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiOBAController extends GuiScreen {

	private GuiTextField commandTextField;

	private final TileEntityOBAController tileEntity;

	private GuiButton buttonDone;
	private GuiButton buttonCancel;

	public GuiOBAController(TileEntityOBAController tile) {
		tileEntity = tile;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonDone = this.addButton(new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
		buttonCancel = this.addButton(new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
		commandTextField = new GuiTextField(2, fontRenderer, width / 2 - 150, 50, 300, 20);
		commandTextField.setMaxStringLength(32500);
		commandTextField.setFocused(true);
		if (tileEntity != null)
			commandTextField.setText(tileEntity.getStops());
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		final String title = new TextComponentTranslation("gui.bus_stops").getFormattedText();
		drawCenteredString(fontRenderer, title, width / 2, 20, 0xFFFFFF);
		commandTextField.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			switch (button.id) {
			case 0:
				if (tileEntity != null)
					MTR.INSTANCE.sendToServer(new MessageOBAController(tileEntity.getPos(), commandTextField.getText()));
				break;
			default:
				break;
			}
			mc.displayGuiScreen((GuiScreen) null);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		commandTextField.textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 28 || keyCode == 156)
			actionPerformed(buttonDone);
		if (keyCode == 1)
			actionPerformed(buttonCancel);
	}
}
