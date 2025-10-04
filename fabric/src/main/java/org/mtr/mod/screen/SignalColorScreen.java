package org.mtr.mod.screen;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockSignalBase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.item.ItemSignalModifier;
import org.mtr.mod.packet.PacketUpdateSignalConfig;
import org.mtr.mod.render.RenderSignalBase;

public class SignalColorScreen extends MTRScreenBase implements IGui {

	private final CheckboxWidgetExtension checkBoxAcceptRedstone;
	private final CheckboxWidgetExtension checkBoxOutputRedstone;
	private final CheckboxWidgetExtension checkBoxSelectAll;
	private final CheckboxWidgetExtension[] checkBoxes = new CheckboxWidgetExtension[ItemSignalModifier.COLORS.length];
	private final BlockPos blockPos;
	private final IntAVLTreeSet signalColors;
	private final IntAVLTreeSet detectedColors = new IntAVLTreeSet();
	private final boolean isBackSide;

	public SignalColorScreen(BlockPos blockPos, BlockSignalBase.BlockEntityBase blockEntity) {
		super();
		this.blockPos = blockPos;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.getWorldMapped();
		if (clientWorld == null) {
			signalColors = new IntAVLTreeSet();
			isBackSide = false;
		} else {
			final float angle = BlockSignalBase.getAngle(clientWorld.getBlockState(blockPos));

			final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
			if (clientPlayerEntity == null) {
				isBackSide = false;
			} else {
				isBackSide = blockEntity.isDoubleSided && Math.abs(Utilities.circularDifference(Math.round(EntityHelper.getYaw(new Entity(clientPlayerEntity.data))), Math.round(angle), 360)) > 90;
			}

			signalColors = new IntAVLTreeSet(blockEntity.getSignalColors(isBackSide));
			final RenderSignalBase.AspectState aspectState = RenderSignalBase.getAspectState(blockPos, angle + (isBackSide ? 180 : 0) + 90);
			detectedColors.addAll(aspectState == null ? new IntAVLTreeSet() : aspectState.detectedColors);
		}

		checkBoxAcceptRedstone = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> toggleRedstoneCheckboxes1());
		checkBoxAcceptRedstone.setMessage2(new Text(TextHelper.literal(TranslationProvider.GUI_MTR_ACCEPT_REDSTONE.getString() + " (BETA)").data));
		checkBoxAcceptRedstone.setChecked(blockEntity.getAcceptRedstone());

		checkBoxOutputRedstone = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> toggleRedstoneCheckboxes2());
		checkBoxOutputRedstone.setMessage2(new Text(TextHelper.literal(TranslationProvider.GUI_MTR_OUTPUT_REDSTONE.getString() + " (BETA)").data));
		checkBoxOutputRedstone.setChecked(blockEntity.getOutputRedstone());

		checkBoxSelectAll = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked) {
				signalColors.clear();
			} else if (signalColors.isEmpty()) {
				signalColors.add(ItemSignalModifier.COLORS[0]);
			}
			setButtons();
		});
		checkBoxSelectAll.setMessage2(TranslationProvider.GUI_MTR_SELECT_ALL.getText());

		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			final int color = ItemSignalModifier.COLORS[i];
			checkBoxes[i] = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, false, checked -> {
				if (checked) {
					signalColors.add(color);
				} else {
					signalColors.remove(color);
				}
				setButtons();
			});
		}
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(checkBoxAcceptRedstone, SQUARE_SIZE, SQUARE_SIZE, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxOutputRedstone, SQUARE_SIZE, SQUARE_SIZE * 2, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxSelectAll, SQUARE_SIZE, SQUARE_SIZE * 3, width - SQUARE_SIZE * 2);
		iterateGrid((x, y, index) -> {
			IDrawing.setPositionAndWidth(checkBoxes[index], SQUARE_SIZE + x * SQUARE_SIZE * 2, SQUARE_SIZE * 4 + y * SQUARE_SIZE, SQUARE_SIZE * 2);
			addChild(new ClickableWidget(checkBoxes[index]));
		});

		addChild(new ClickableWidget(checkBoxAcceptRedstone));
		addChild(new ClickableWidget(checkBoxOutputRedstone));
		addChild(new ClickableWidget(checkBoxSelectAll));
		setButtons();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);

		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		iterateGrid((x, y, index) -> {
			final int color = ItemSignalModifier.COLORS[index];
			guiDrawing.drawRectangle(SQUARE_SIZE * 2 + x * SQUARE_SIZE * 2, SQUARE_SIZE * 4 + y * SQUARE_SIZE, SQUARE_SIZE * 2 + x * SQUARE_SIZE * 2 + SQUARE_SIZE / (detectedColors.contains(color) ? 1 : 8F), SQUARE_SIZE * 5 + y * SQUARE_SIZE, color | ARGB_BLACK);
		});
		guiDrawing.finishDrawingRectangle();
	}

	@Override
	public void onClose2() {
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateSignalConfig(blockPos, checkBoxAcceptRedstone.isChecked2(), checkBoxOutputRedstone.isChecked2(), signalColors, isBackSide));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private void setButtons() {
		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			setChecked(checkBoxes[i], signalColors.contains(ItemSignalModifier.COLORS[i]));
		}
		setChecked(checkBoxSelectAll, signalColors.isEmpty());
	}

	private void toggleRedstoneCheckboxes1() {
		if (checkBoxAcceptRedstone.isChecked2()) {
			setChecked(checkBoxOutputRedstone, false);
		}
	}

	private void toggleRedstoneCheckboxes2() {
		if (checkBoxOutputRedstone.isChecked2()) {
			setChecked(checkBoxAcceptRedstone, false);
		}
	}

	private static void setChecked(CheckboxWidgetExtension checkboxWidgetExtension, boolean checked) {
		if (checkboxWidgetExtension.isChecked2() != checked) {
			checkboxWidgetExtension.setChecked(checked);
		}
	}

	private static void iterateGrid(GridConsumer gridConsumer) {
		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			gridConsumer.accept(i % 4, i / 4, i);
		}
	}

	@FunctionalInterface
	private interface GridConsumer {
		void accept(int x, int y, int index);
	}
}
