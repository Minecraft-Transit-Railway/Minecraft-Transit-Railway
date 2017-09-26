package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUIRouteChanger extends GuiScreen {
	private GuiButton buttonDone, buttonA1, buttonA10, buttonA100, buttonS1, buttonS10, buttonS100, buttonUpDown,
			buttonP;
	private int route;
	private boolean p, down;
	private TileEntityRouteChangerEntity te;

	public GUIRouteChanger(TileEntityRouteChangerEntity tileEntityRouteChangerEntity) {
		te = tileEntityRouteChangerEntity;
		int route2 = 0;
		if (te.route != 0)
			route2 = te.route - 2000;
		down = route2 < 0;
		route = Math.abs(route2);
		p = route >= 1000;
		if (p)
			route = route - 1000;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height / 2 + 50, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonA1 = new GuiButton(2, width / 2 + 20, height / 2 - 50, 50, 20, "+1");
		buttonA10 = new GuiButton(3, width / 2 + 70, height / 2 - 50, 50, 20, "+10");
		buttonA100 = new GuiButton(4, width / 2 + 120, height / 2 - 50, 50, 20, "+100");
		buttonS1 = new GuiButton(5, width / 2 - 70, height / 2 - 50, 50, 20, "-1");
		buttonS10 = new GuiButton(6, width / 2 - 120, height / 2 - 50, 50, 20, "-10");
		buttonS100 = new GuiButton(7, width / 2 - 170, height / 2 - 50, 50, 20, "-100");
		buttonUpDown = new GuiButton(8, width / 2 - 75, height / 2 + 30, 130, 20,
				I18n.format(down ? "gui.u" : "gui.d", new Object[0]));
		buttonP = new GuiButton(9, width / 2 + 55, height / 2 + 30, 20, 20, "P");
		buttonList.add(buttonDone);
		buttonList.add(buttonA1);
		buttonList.add(buttonA10);
		buttonList.add(buttonA100);
		buttonList.add(buttonS1);
		buttonList.add(buttonS10);
		buttonList.add(buttonS100);
		buttonList.add(buttonUpDown);
		buttonList.add(buttonP);
	}

	@Override
	public void drawScreen(int parWidth, int parHeight, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, Integer.toString(route) + (p ? "P" : ""), width / 2, height / 2 - 45,
				0xFFFFFF);
		super.drawScreen(parWidth, parHeight, p_73863_3_);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			te.route = (down ? -1 : 1) * (route + (p ? 1000 : 0)) + 2000;
			MTR.network.sendToServer(
					new MessageRouteChanger(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), te.route));
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			route++;
			break;
		case 3:
			route += 10;
			break;
		case 4:
			route += 100;
			break;
		case 5:
			route--;
			break;
		case 6:
			route -= 10;
			break;
		case 7:
			route -= 100;
			break;
		case 8:
			down = !down;
			if (down)
				buttonUpDown.displayString = I18n.format("gui.u", new Object[0]);
			else
				buttonUpDown.displayString = I18n.format("gui.d", new Object[0]);
			break;
		case 9:
			p = !p;
			break;
		}
		if (route < 0)
			route = 0;
		if (route > 999)
			route = 999;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}