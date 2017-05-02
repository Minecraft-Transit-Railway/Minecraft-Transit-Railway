package MTR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GUIPSD extends GuiScreen {
	private GuiButton buttonDone, buttonColorAdd, buttonColorSubtract, buttonPNumAdd, buttonPNumSubtract,
			buttonBoundAdd, buttonBoundSubtract, buttonArrowLeft, buttonArrowRight;
	int color, number, bound, arrow;
	TileEntityPSDBase te;
	String colors[] = { "none", "red", "green", "brown", "blue", "purple", "cyan", "light_gray", "gray", "pink",
			"light_green", "yellow", "light_blue", "magenta", "orange", "black" };

	public GUIPSD(TileEntityPSDBase tEntity) {
		te = tEntity;
		color = te.color;
		number = te.number;
		bound = te.bound;
		arrow = te.arrow;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height / 2 + 50, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonColorAdd = new GuiButton(2, width / 2 - 150, height / 2 - 50, 80, 20,
				I18n.format("gui.color", new Object[0]) + " +");
		buttonColorSubtract = new GuiButton(3, width / 2 - 150, height / 2 - 10, 80, 20,
				I18n.format("gui.color", new Object[0]) + " -");
		buttonPNumAdd = new GuiButton(4, width / 2 - 70, height / 2 - 50, 70, 20,
				I18n.format("gui.platform", new Object[0]) + " +");
		buttonPNumSubtract = new GuiButton(5, width / 2 - 70, height / 2 - 10, 70, 20,
				I18n.format("gui.platform", new Object[0]) + " -");
		buttonBoundAdd = new GuiButton(6, width / 2, height / 2 - 50, 150, 20,
				I18n.format("gui.bound", new Object[0]) + " +");
		buttonBoundSubtract = new GuiButton(7, width / 2, height / 2 - 10, 150, 20,
				I18n.format("gui.bound", new Object[0]) + " -");
		buttonArrowLeft = new GuiButton(8, width / 2 - 170, height / 2 - 30, 20, 20, "<");
		buttonArrowRight = new GuiButton(9, width / 2 + 150, height / 2 - 30, 20, 20, ">");
		buttonList.add(buttonDone);
		buttonList.add(buttonColorAdd);
		buttonList.add(buttonColorSubtract);
		buttonList.add(buttonPNumAdd);
		buttonList.add(buttonPNumSubtract);
		buttonList.add(buttonBoundAdd);
		buttonList.add(buttonBoundSubtract);
		buttonList.add(buttonArrowLeft);
		buttonList.add(buttonArrowRight);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("tile.BlockPSDDoor.name", new Object[0]), width / 2,
				height / 2 - 75, 0xFFFFFF);
		if (color > 0)
			drawCenteredString(fontRendererObj,
					I18n.format("destination." + String.valueOf(color * 4 - 4 + bound) + "c", new Object[0]) + " "
							+ I18n.format("destination." + String.valueOf(color * 4 - 4 + bound), new Object[0]),
					width / 2 + 75, height / 2 - 25, 0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format("gui." + colors[color], new Object[0]), width / 2 - 110,
				height / 2 - 25, 0xFFFFFF);
		drawCenteredString(fontRendererObj, String.valueOf(number), width / 2 - 35, height / 2 - 25, 0xFFFFFF);
		drawCenteredString(fontRendererObj, arrow == 1 ? ">" : "<", width / 2 + (arrow == 1 ? 145 : -145),
				height / 2 - 25, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			te.color = color;
			te.number = number;
			te.bound = bound;
			te.arrow = arrow;
			Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(te.getPos());
			MTR.network.sendToServer(new MessagePSD(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(),
					color, number, bound, arrow));
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			color++;
			bound = 0;
			if (color > 15)
				color = 0;
			break;
		case 3:
			color--;
			bound = 0;
			if (color < 0)
				color = 15;
			break;
		case 4:
			number++;
			if (number > 8)
				number = 1;
			break;
		case 5:
			number--;
			if (number < 1)
				number = 8;
			break;
		case 6:
			bound++;
			if (bound > 3)
				bound = 0;
			break;
		case 7:
			bound--;
			if (bound < 0)
				bound = 3;
			break;
		case 8:
			arrow = 0;
			break;
		case 9:
			arrow = 1;
			break;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}