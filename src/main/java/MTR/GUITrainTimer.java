package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUITrainTimer extends GuiScreen {
	private GuiButton buttonDone, buttonA1, buttonA10, buttonA100, buttonS1, buttonS10, buttonS100;
	int time;
	TileEntityTrainTimerEntity te;

	public GUITrainTimer(TileEntityTrainTimerEntity tileEntityTrainTimerEntity) {
		te = tileEntityTrainTimerEntity;
		time = te.time;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height / 2 + 50, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonA1 = new GuiButton(2, width / 2 + 20, height / 2 - 50, 50, 20, I18n.format("gui.a1", new Object[0]));
		buttonA10 = new GuiButton(3, width / 2 + 70, height / 2 - 50, 50, 20, I18n.format("gui.a10", new Object[0]));
		buttonA100 = new GuiButton(4, width / 2 + 120, height / 2 - 50, 50, 20, I18n.format("gui.a100", new Object[0]));
		buttonS1 = new GuiButton(5, width / 2 - 70, height / 2 - 50, 50, 20, I18n.format("gui.s1", new Object[0]));
		buttonS10 = new GuiButton(6, width / 2 - 120, height / 2 - 50, 50, 20, I18n.format("gui.s10", new Object[0]));
		buttonS100 = new GuiButton(7, width / 2 - 170, height / 2 - 50, 50, 20, I18n.format("gui.s100", new Object[0]));
		buttonList.add(buttonDone);
		buttonList.add(buttonA1);
		buttonList.add(buttonA10);
		buttonList.add(buttonA100);
		buttonList.add(buttonS1);
		buttonList.add(buttonS10);
		buttonList.add(buttonS100);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("gui.timertext", new Object[0]), width / 2, height / 2 - 75,
				0xFFFFFF);
		drawCenteredString(fontRendererObj, Integer.toString(time) + "s", width / 2, height / 2 - 45, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			te.time = time;
			// MTRClientPacketHandler.sendStuff(te.time, te.getPos(), 3);
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			time++;
			break;
		case 3:
			time += 10;
			break;
		case 4:
			time += 100;
			break;
		case 5:
			time--;
			break;
		case 6:
			time -= 10;
			break;
		case 7:
			time -= 100;
			break;
		}
		if (time < 0)
			time = 0;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}