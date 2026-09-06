package org.mtr.mod.screen;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDeleteRailAction;

public class RailActionsScreen extends MTRScreenBase implements IGui {

	private final DashboardList railActionsList;
	private int lastQueueSize = 0;
	private int queueDisplayTicks = 0;

	public RailActionsScreen(ScreenExtension previousScreenExtension) {
		super(previousScreenExtension);
		railActionsList = new DashboardList(null, null, null, null, null, this::onDelete, null, () -> "", text -> {
		});
	}

	@Override
	protected void init2() {
		super.init2();
		railActionsList.x = SQUARE_SIZE;
		railActionsList.y = SQUARE_SIZE * 2;
		railActionsList.width = width - SQUARE_SIZE * 2;
		railActionsList.height = height - SQUARE_SIZE * 2;
		railActionsList.init(this::addChild);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		railActionsList.render(graphicsHolder);
		
		// Render main title using MTR Translation Provider
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_RAIL_ACTIONS.getMutableText(), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
		
		// Render queue status message cleanly across 1.16.5, 1.20.1, and 1.20.4
		if (queueDisplayTicks > 0 && lastQueueSize > 0) {
			graphicsHolder.drawCenteredText("Operations in queue: " + lastQueueSize, width / 2, SQUARE_SIZE * 2 - 12, ARGB_LIGHT_GRAY);
		}
		
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		railActionsList.mouseMoved(mouseX, mouseY);
	}

	// Updated to handle 1.20.4 dual-axis scroll inputs while maintaining 1.16.5/1.20.1 support
	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amountX, double amountY) {
		railActionsList.mouseScrolled(mouseX, mouseY, amountY);
		return super.mouseScrolled2(mouseX, mouseY, amountX, amountY);
	}

	@Override
	public void tick2() {
		railActionsList.tick();
		railActionsList.setData(MinecraftClientData.getInstance().railActions, false, false, false, false, false, true);

		// Check queue size changes to trigger temporary notice
		int currentSize = MinecraftClientData.getInstance().railActions.size();
		if (currentSize != lastQueueSize) {
			lastQueueSize = currentSize;
			if (currentSize > 0) {
				queueDisplayTicks = 60; // 3 seconds at 20 ticks/sec
			}
		}

		if (queueDisplayTicks > 0) {
			queueDisplayTicks--;
		}
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDeleteRailAction(dashboardListItem.id));
	}
}
