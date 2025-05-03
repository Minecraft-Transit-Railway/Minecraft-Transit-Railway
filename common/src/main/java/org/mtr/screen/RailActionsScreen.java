package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.mtr.client.MinecraftClientData;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketDeleteRailAction;
import org.mtr.registry.RegistryClient;

public class RailActionsScreen extends ScreenBase implements IGui {

	private final DashboardList railActionsList;

	public RailActionsScreen(Screen previousScreen) {
		super(previousScreen);
		railActionsList = new DashboardList(null, null, null, null, null, this::onDelete, null, () -> "", text -> {
		});
	}

	@Override
	protected void init() {
		super.init();
		railActionsList.x = SQUARE_SIZE;
		railActionsList.y = SQUARE_SIZE * 2;
		railActionsList.width = width - SQUARE_SIZE * 2;
		railActionsList.height = height - SQUARE_SIZE * 2;
		railActionsList.init(this::addDrawableChild);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		railActionsList.render(context);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_RAIL_ACTIONS.getMutableText(), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		railActionsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		railActionsList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public void tick() {
		railActionsList.tick();
		railActionsList.setData(MinecraftClientData.getInstance().railActions, false, false, false, false, false, true);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		RegistryClient.sendPacketToServer(new PacketDeleteRailAction(dashboardListItem.id));
	}
}
