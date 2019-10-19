package mtr.gui;

import mtr.container.ContainerBridgeCreator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBridgeCreator extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private final InventoryPlayer inventoryPlayer;
	private final IInventory tileBridgeCreator;

	public GuiBridgeCreator(InventoryPlayer inventory, IInventory tileEntity) {
		super(new ContainerBridgeCreator(inventory, tileEntity));
		inventoryPlayer = inventory;
		tileBridgeCreator = tileEntity;
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
		fontRenderer.drawString(tileBridgeCreator.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		final int i = (width - xSize) / 2;
		final int j = (height - ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, xSize, 125);
		this.drawTexturedModalRect(i, j + 125, 0, 126, xSize, 96);
	}
}
