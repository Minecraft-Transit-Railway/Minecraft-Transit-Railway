package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUIMakePlatform extends GuiScreen {
	private GuiButton buttonDone, buttonA1, buttonA10, buttonA100, buttonS1, buttonS10, buttonS100, buttonPNumAdd,
			buttonPNumSubtract;
	private int x, y, z, station, number;

	public GUIMakePlatform(int x1, int y1, int z1) {
		x = x1;
		y = y1;
		z = z1;
		if (number < 1)
			number = 1;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height / 2 + 50, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonA1 = new GuiButton(2, width / 2 + 20, height / 2 - 50, 50, 20, "+1");
		buttonA10 = new GuiButton(3, width / 2 + 70, height / 2 - 50, 50, 20, "+10");
		buttonA100 = new GuiButton(4, width / 2 + 120, height / 2 - 50, 50, 20, "+50");
		buttonS1 = new GuiButton(5, width / 2 - 70, height / 2 - 50, 50, 20, "-1");
		buttonS10 = new GuiButton(6, width / 2 - 120, height / 2 - 50, 50, 20, "-10");
		buttonS100 = new GuiButton(7, width / 2 - 170, height / 2 - 50, 50, 20, "-50");
		buttonPNumAdd = new GuiButton(8, width / 2 + 20, height / 2 + 10, 20, 20, "+");
		buttonPNumSubtract = new GuiButton(9, width / 2 - 40, height / 2 + 10, 20, 20, "-");
		buttonList.add(buttonDone);
		buttonList.add(buttonA1);
		buttonList.add(buttonA10);
		buttonList.add(buttonA100);
		buttonList.add(buttonS1);
		buttonList.add(buttonS10);
		buttonList.add(buttonS100);
		buttonList.add(buttonPNumAdd);
		buttonList.add(buttonPNumSubtract);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj,
				I18n.format("gui.platformmake", new Object[0]) + ": " + x + ", " + y + ", " + z, width / 2,
				height / 2 - 110, 0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("gui.platformname", new Object[0]), width / 2, height / 2 - 75,
				0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("station." + station + "c", new Object[0]) + " "
				+ I18n.format("station." + station, new Object[0]), width / 2, height / 2 - 65, 0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("gui.platformnumber", new Object[0]), width / 2, height / 2 - 5,
				0xFFFFFF);
		drawCenteredString(fontRendererObj, String.valueOf(number), width / 2, height / 2 + 15, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			MTR.network.sendToServer(new MessagePlatformMaker(x, y, z, station, number));
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			station++;
			break;
		case 3:
			station += 10;
			break;
		case 4:
			station += 50;
			break;
		case 5:
			station--;
			break;
		case 6:
			station -= 10;
			break;
		case 7:
			station -= 50;
			break;
		case 8:
			number++;
			if (number > 8)
				number = 1;
			break;
		case 9:
			number--;
			if (number < 1)
				number = 8;
			break;
		}
		if (station < 0)
			station = 0;
		if (station > 99)
			station = 99;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}