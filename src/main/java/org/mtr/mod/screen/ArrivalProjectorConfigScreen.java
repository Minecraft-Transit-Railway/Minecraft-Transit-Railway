package org.mtr.mod.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.block.BlockArrivalProjectorBase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.packet.PacketUpdateArrivalProjectorConfig;

public class ArrivalProjectorConfigScreen extends ScreenExtension implements IGui, IPacket {

	private final BlockPos blockPos;
	private final LongAVLTreeSet filterPlatformIds;
	private final int displayPage;
	private final CheckboxWidgetExtension selectAllCheckbox;
	private final TextFieldWidgetExtension displayPageInput;
	private final ButtonWidgetExtension filterButton;

	public ArrivalProjectorConfigScreen(BlockPos blockPos) {
		super();
		this.blockPos = blockPos;

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			filterPlatformIds = new LongAVLTreeSet();
			displayPage = 0;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null && blockEntity.data instanceof BlockArrivalProjectorBase.BlockEntityBase) {
				filterPlatformIds = ((BlockArrivalProjectorBase.BlockEntityBase) blockEntity.data).getPlatformIds();
				displayPage = ((BlockArrivalProjectorBase.BlockEntityBase) blockEntity.data).getDisplayPage();
			} else {
				filterPlatformIds = new LongAVLTreeSet();
				displayPage = 0;
			}
		}

		selectAllCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		selectAllCheckbox.setMessage2(new Text(TextHelper.translatable("gui.mtr.select_all_platforms").data));

		displayPageInput = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", "1");

		filterButton = PIDSConfigScreen.getPlatformFilterButton(blockPos, selectAllCheckbox, filterPlatformIds, this);
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
		selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
		addChild(new ClickableWidget(selectAllCheckbox));

		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
		filterButton.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		addChild(new ClickableWidget(filterButton));

		IDrawing.setPositionAndWidth(displayPageInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH / 2 - TEXT_FIELD_PADDING);
		displayPageInput.setText2(String.valueOf(displayPage + 1));
		addChild(new ClickableWidget(displayPageInput));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		graphicsHolder.drawText(TextHelper.translatable("gui.mtr.filtered_platforms", selectAllCheckbox.isChecked2() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(TextHelper.translatable("gui.mtr.display_page"), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public void onClose2() {
		if (selectAllCheckbox.isChecked2()) {
			filterPlatformIds.clear();
		}
		int displayPage = 0;
		try {
			displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText2()) - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RegistryClient.sendPacketToServer(new PacketUpdateArrivalProjectorConfig(blockPos, filterPlatformIds, displayPage));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
