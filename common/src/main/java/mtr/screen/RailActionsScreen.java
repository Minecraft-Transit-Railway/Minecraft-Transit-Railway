package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class RailActionsScreen extends ScreenMapper implements IGui, IPacket {

	final DashboardList railActionsList;

	public RailActionsScreen() {
		super(new TextComponent(""));
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			railActionsList.render(matrices, font);
			drawCenteredString(matrices, font, new TranslatableComponent("gui.mtr.rail_actions"), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		railActionsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		railActionsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void tick() {
		railActionsList.tick();
		railActionsList.setData(ClientData.RAIL_ACTIONS, false, false, false, false, false, true);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onDelete(NameColorDataBase data, int index) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(data.id);
		RegistryClient.sendToServer(PACKET_REMOVE_RAIL_ACTION, packet);
	}
}
