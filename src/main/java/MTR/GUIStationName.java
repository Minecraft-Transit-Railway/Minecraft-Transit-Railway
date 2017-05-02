package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUIStationName extends GuiScreen {

	private GuiButton buttonDone, buttonA1, buttonA10, buttonA100, buttonS1, buttonS10, buttonS100;
	int station;
	TileEntityStationNameEntity te;

	public GUIStationName(TileEntityStationNameEntity te2) {
		te = te2;
		station = te2.station;
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
		drawCenteredString(fontRendererObj, I18n.format("station." + station + "c", new Object[0]) + " "
				+ I18n.format("station." + station, new Object[0]), width / 2, height / 2 - 75, 0xFFFFFF);
		drawCenteredString(fontRendererObj, Integer.toString(station), width / 2, height / 2 - 45, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			te.station = station;
			MTR.network.sendToServer(
					new MessageRouteChanger(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), te.station));
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
		}
		if (station < 0)
			station = 0;
		if (station > 92)
			station = 92;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}