package org.mtr.screen;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockSignalBase;
import org.mtr.client.IDrawing;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.item.ItemSignalModifier;
import org.mtr.packet.PacketUpdateSignalConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderSignalBase;

public class SignalColorScreen extends ScreenBase implements IGui {

	private final CheckboxWidget checkBoxAcceptRedstone;
	private final CheckboxWidget checkBoxOutputRedstone;
	private final CheckboxWidget checkBoxSelectAll;
	private final CheckboxWidget[] checkBoxes = new CheckboxWidget[ItemSignalModifier.COLORS.length];
	private final BlockPos blockPos;
	private final IntAVLTreeSet signalColors;
	private final IntAVLTreeSet detectedColors = new IntAVLTreeSet();
	private final boolean isBackSide;

	public SignalColorScreen(BlockPos blockPos, BlockSignalBase.BlockEntityBase blockEntity) {
		super();
		this.blockPos = blockPos;

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		if (clientWorld == null) {
			signalColors = new IntAVLTreeSet();
			isBackSide = false;
		} else {
			final float angle = BlockSignalBase.getAngle(clientWorld.getBlockState(blockPos));

			final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
			if (clientPlayerEntity == null) {
				isBackSide = false;
			} else {
				isBackSide = blockEntity.isDoubleSided && Math.abs(Utilities.circularDifference(Math.round(clientPlayerEntity.getYaw()), Math.round(angle), 360)) > 90;
			}

			signalColors = new IntAVLTreeSet(blockEntity.getSignalColors(isBackSide));
			final RenderSignalBase.AspectState aspectState = RenderSignalBase.getAspectState(blockPos, angle + (isBackSide ? 180 : 0) + 90);
			detectedColors.addAll(aspectState == null ? new IntAVLTreeSet() : aspectState.detectedColors);
		}

		checkBoxAcceptRedstone = CheckboxWidget.builder(Text.literal(TranslationProvider.GUI_MTR_ACCEPT_REDSTONE.getString() + " (BETA)"), textRenderer).checked(blockEntity.getAcceptRedstone()).callback((checkboxWidget, checked) -> toggleRedstoneCheckboxes1()).build();
		checkBoxOutputRedstone = CheckboxWidget.builder(Text.literal(TranslationProvider.GUI_MTR_OUTPUT_REDSTONE.getString() + " (BETA)"), textRenderer).checked(blockEntity.getOutputRedstone()).callback((checkboxWidget, checked) -> toggleRedstoneCheckboxes2()).build();

		checkBoxSelectAll = CheckboxWidget.builder(TranslationProvider.GUI_MTR_SELECT_ALL.getText(), textRenderer).callback((checkboxWidget, checked) -> {
			if (checked) {
				signalColors.clear();
			} else if (signalColors.isEmpty()) {
				signalColors.add(ItemSignalModifier.COLORS[0]);
			}
			setButtons();
		}).build();

		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			final int color = ItemSignalModifier.COLORS[i];
			checkBoxes[i] = CheckboxWidget.builder(Text.empty(), textRenderer).callback((checkboxWidget, checked) -> {
				if (checked) {
					signalColors.add(color);
				} else {
					signalColors.remove(color);
				}
				setButtons();
			}).build();
		}
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(checkBoxAcceptRedstone, SQUARE_SIZE, SQUARE_SIZE, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxOutputRedstone, SQUARE_SIZE, SQUARE_SIZE * 2, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxSelectAll, SQUARE_SIZE, SQUARE_SIZE * 3, width - SQUARE_SIZE * 2);
		iterateGrid((x, y, index) -> {
			IDrawing.setPositionAndWidth(checkBoxes[index], SQUARE_SIZE + x * SQUARE_SIZE * 2, SQUARE_SIZE * 4 + y * SQUARE_SIZE, SQUARE_SIZE * 2);
			addDrawableChild(checkBoxes[index]);
		});

		addDrawableChild(checkBoxAcceptRedstone);
		addDrawableChild(checkBoxOutputRedstone);
		addDrawableChild(checkBoxSelectAll);
		setButtons();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		iterateGrid((x, y, index) -> {
			final int color = ItemSignalModifier.COLORS[index];
			context.fill(SQUARE_SIZE * 2 + x * SQUARE_SIZE * 2, SQUARE_SIZE * 4 + y * SQUARE_SIZE, SQUARE_SIZE * 2 + x * SQUARE_SIZE * 2 + SQUARE_SIZE / (detectedColors.contains(color) ? 1 : 8), SQUARE_SIZE * 5 + y * SQUARE_SIZE, color | ARGB_BLACK);
		});
	}

	@Override
	public void close() {
		RegistryClient.sendPacketToServer(new PacketUpdateSignalConfig(blockPos, checkBoxAcceptRedstone.isChecked(), checkBoxOutputRedstone.isChecked(), signalColors, isBackSide));
		super.close();
	}

	private void setButtons() {
		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			IGui.setChecked(checkBoxes[i], signalColors.contains(ItemSignalModifier.COLORS[i]));
		}
		IGui.setChecked(checkBoxSelectAll, signalColors.isEmpty());
	}

	private void toggleRedstoneCheckboxes1() {
		if (checkBoxAcceptRedstone.isChecked()) {
			IGui.setChecked(checkBoxOutputRedstone, false);
		}
	}

	private void toggleRedstoneCheckboxes2() {
		if (checkBoxOutputRedstone.isChecked()) {
			IGui.setChecked(checkBoxAcceptRedstone, false);
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
