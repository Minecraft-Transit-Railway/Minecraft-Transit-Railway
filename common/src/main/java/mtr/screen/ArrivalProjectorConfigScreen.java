package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockArrivalProjectorBase;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.Set;

public class ArrivalProjectorConfigScreen extends ScreenMapper implements IGui, IPacket {

	private final BlockPos pos;
	private final Set<Long> filterPlatformIds;
	private final WidgetBetterCheckbox selectAllCheckbox;
	private final Button filterButton;

	public ArrivalProjectorConfigScreen(BlockPos pos) {
		super(Text.literal(""));
		this.pos = pos;

		final Level world = Minecraft.getInstance().level;
		if (world == null) {
			filterPlatformIds = new HashSet<>();
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) {
				filterPlatformIds = ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity).getPlatformIds();
			} else {
				filterPlatformIds = new HashSet<>();
			}
		}

		selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.select_all_platforms"), checked -> {
		});

		filterButton = PIDSConfigScreen.getPlatformFilterButton(pos, selectAllCheckbox, filterPlatformIds, this);
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
		addDrawableChild(selectAllCheckbox);

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
		filterButton.setMessage(Text.translatable("selectWorld.edit"));
		addDrawableChild(filterButton);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			font.draw(matrices, Text.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.selected() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		if (selectAllCheckbox.selected()) {
			filterPlatformIds.clear();
		}
		PacketTrainDataGuiClient.sendArrivalProjectorConfigC2S(pos, filterPlatformIds);
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
