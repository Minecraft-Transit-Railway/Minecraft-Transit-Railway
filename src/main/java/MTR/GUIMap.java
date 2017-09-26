package MTR;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUIMap extends GuiScreen {
	private GuiButton buttonDone;
	private TileEntityNextTrainEntity te;

	public GUIMap() {
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height - 20, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonList.add(buttonDone);
	}

	@Override
	public void drawScreen(int parWidth, int parHeight, float partialTicks) {
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation("MTR:textures/Map.png"));
		drawTexturedModalRect(width / 2 - 128, height / 2 - 128, 0, 0, 256, 256);
		super.drawScreen(parWidth, parHeight, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			mc.displayGuiScreen((GuiScreen) null);
			break;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}