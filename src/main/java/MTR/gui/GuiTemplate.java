package mtr.gui;

import mtr.container.ContainerTemplate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiTemplate extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("mtr:textures/gui/template.png");
	private final InventoryPlayer inventoryPlayer;
	private final IInventory itemTemplate;

	public GuiTemplate(InventoryPlayer inventory, IInventory iInventory) {
		super(new ContainerTemplate(inventory, iInventory));
		inventoryPlayer = inventory;
		itemTemplate = iInventory;
		ySize = 222;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(itemTemplate.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
