package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUIRailBooster extends GuiScreen {
	private GuiButton buttonDone, buttonBoostAdd1, buttonBoostSubtract1, buttonBoostAdd10, buttonBoostSubtract10,
			buttonBoostAdd100, buttonBoostSubtract100, buttonSlowAdd1, buttonSlowSubtract1, buttonSlowAdd10,
			buttonSlowSubtract10, buttonSlowAdd100, buttonSlowSubtract100;
	int speedBoost, speedSlow, arrow;
	TileEntityRailBoosterEntity te;

	public GUIRailBooster(TileEntityRailBoosterEntity tEntity) {
		te = tEntity;
		speedBoost = te.speedBoost;
		speedSlow = te.speedSlow;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height / 2 + 50, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonBoostAdd1 = new GuiButton(2, width / 2 + 55, height / 2 - 30, 40, 20, "+0.1");
		buttonBoostAdd10 = new GuiButton(3, width / 2 + 95, height / 2 - 30, 30, 20, "+1");
		buttonBoostAdd100 = new GuiButton(4, width / 2 + 125, height / 2 - 30, 30, 20, "+10");
		buttonBoostSubtract1 = new GuiButton(5, width / 2 - 85, height / 2 - 30, 40, 20, "-0.1");
		buttonBoostSubtract10 = new GuiButton(6, width / 2 - 115, height / 2 - 30, 30, 20, "-1");
		buttonBoostSubtract100 = new GuiButton(7, width / 2 - 145, height / 2 - 30, 30, 20, "-10");
		buttonSlowAdd1 = new GuiButton(8, width / 2 + 55, height / 2, 40, 20, "+0.1");
		buttonSlowAdd10 = new GuiButton(9, width / 2 + 95, height / 2, 30, 20, "+1");
		buttonSlowAdd100 = new GuiButton(10, width / 2 + 125, height / 2, 30, 20, "+10");
		buttonSlowSubtract1 = new GuiButton(11, width / 2 - 85, height / 2, 40, 20, "-0.1");
		buttonSlowSubtract10 = new GuiButton(12, width / 2 - 115, height / 2, 30, 20, "-1");
		buttonSlowSubtract100 = new GuiButton(13, width / 2 - 145, height / 2, 30, 20, "-10");
		buttonList.add(buttonDone);
		buttonList.add(buttonBoostAdd1);
		buttonList.add(buttonBoostAdd10);
		buttonList.add(buttonBoostAdd100);
		buttonList.add(buttonBoostSubtract1);
		buttonList.add(buttonBoostSubtract10);
		buttonList.add(buttonBoostSubtract100);
		buttonList.add(buttonSlowAdd1);
		buttonList.add(buttonSlowAdd10);
		buttonList.add(buttonSlowAdd100);
		buttonList.add(buttonSlowSubtract1);
		buttonList.add(buttonSlowSubtract10);
		buttonList.add(buttonSlowSubtract100);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("tile.BlockRailBooster.name", new Object[0]), width / 2,
				height / 2 - 75, 0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("gui.boostto", new Object[0]), width / 2, height / 2 - 30,
				0xFFFFFF);
		drawCenteredString(fontRendererObj, String.valueOf(speedBoost / 10F) + "m/s", width / 2, height / 2 - 20,
				0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("gui.slowto", new Object[0]), width / 2, height / 2, 0xFFFFFF);
		drawCenteredString(fontRendererObj, String.valueOf(speedSlow / 10F) + "m/s", width / 2, height / 2 + 10,
				0xFFFFFF);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			te.speedBoost = speedBoost;
			te.speedSlow = speedSlow;
			MTR.network.sendToServer(new MessageBoosterRail(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(),
					speedBoost, speedSlow));
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			speedBoost++;
			break;
		case 3:
			speedBoost += 10;
			break;
		case 4:
			speedBoost += 100;
			break;
		case 5:
			speedBoost--;
			break;
		case 6:
			speedBoost -= 10;
			break;
		case 7:
			speedBoost -= 100;
			break;
		case 8:
			speedSlow++;
			break;
		case 9:
			speedSlow += 10;
			break;
		case 10:
			speedSlow += 100;
			break;
		case 11:
			speedSlow--;
			break;
		case 12:
			speedSlow -= 10;
			break;
		case 13:
			speedSlow -= 100;
			break;
		}
		if (speedBoost > 500)
			speedBoost = 500;
		if (speedBoost < 1)
			speedBoost = 1;
		if (speedSlow > 500)
			speedSlow = 500;
		if (speedSlow < 0)
			speedSlow = 0;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}