package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Lift;
import mtr.data.LiftClient;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public class LiftCustomizationScreen extends ScreenMapper implements IGui, IPacket {

	private final LiftClient lift;
	private final Button buttonHeightMinus;
	private final Button buttonHeightAdd;
	private final Button buttonWidthMinus;
	private final Button buttonWidthAdd;
	private final Button buttonDepthMinus;
	private final Button buttonDepthAdd;
	private final Button buttonOffsetXMinus;
	private final Button buttonOffsetXAdd;
	private final Button buttonOffsetYMinus;
	private final Button buttonOffsetYAdd;
	private final Button buttonOffsetZMinus;
	private final Button buttonOffsetZAdd;
	private final WidgetBetterCheckbox buttonIsDoubleSided;
	private final Button buttonLiftStyle;
	private final Button buttonRotateAnticlockwise;
	private final Button buttonRotateClockwise;
	private final int width1;
	private final int width2;

	private static final int MIN_DIMENSION = 2;
	private static final int MAX_DIMENSION = 16;
	private static final int MAX_OFFSET = 16;

	public LiftCustomizationScreen(LiftClient lift) {
		super(Text.literal(""));
		this.lift = lift;

		buttonHeightMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftHeight = Math.max(MIN_DIMENSION * 2, lift.liftHeight - 1);
			updateControls();
		});
		buttonHeightAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftHeight = Math.min(MAX_DIMENSION * 2, lift.liftHeight + 1);
			updateControls();
		});
		buttonWidthMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftWidth = Math.max(MIN_DIMENSION, lift.liftWidth - 1);
			updateControls();
		});
		buttonWidthAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftWidth = Math.min(MAX_DIMENSION, lift.liftWidth + 1);
			updateControls();
		});
		buttonDepthMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftDepth = Math.max(MIN_DIMENSION, lift.liftDepth - 1);
			updateControls();
		});
		buttonDepthAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftDepth = Math.min(MAX_DIMENSION, lift.liftDepth + 1);
			updateControls();
		});
		buttonOffsetXMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftOffsetX = Math.max(-MAX_OFFSET * 2, lift.liftOffsetX - 1);
			updateControls();
		});
		buttonOffsetXAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftOffsetX = Math.min(MAX_OFFSET * 2, lift.liftOffsetX + 1);
			updateControls();
		});
		buttonOffsetYMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftOffsetY = Math.max(-MAX_OFFSET, lift.liftOffsetY - 1);
			updateControls();
		});
		buttonOffsetYAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftOffsetY = Math.min(MAX_OFFSET, lift.liftOffsetY + 1);
			updateControls();
		});
		buttonOffsetZMinus = UtilitiesClient.newButton(Text.literal("-"), button -> {
			lift.liftOffsetZ = Math.max(-MAX_OFFSET * 2, lift.liftOffsetZ - 1);
			updateControls();
		});
		buttonOffsetZAdd = UtilitiesClient.newButton(Text.literal("+"), button -> {
			lift.liftOffsetZ = Math.min(MAX_OFFSET * 2, lift.liftOffsetZ + 1);
			updateControls();
		});
		final Component doubleSidedText = Text.translatable("gui.mtr.lift_is_double_sided");
		final Component rotateAnticlockwiseText = Text.translatable("gui.mtr.rotate_anticlockwise");
		final Component rotateClockwiseText = Text.translatable("gui.mtr.rotate_clockwise");
		buttonIsDoubleSided = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, doubleSidedText, checked -> lift.isDoubleSided = checked);
		buttonLiftStyle = UtilitiesClient.newButton(button -> {
			lift.liftStyle = Lift.LiftStyle.values()[(lift.liftStyle.ordinal() + 1) % Lift.LiftStyle.values().length];
			updateControls();
		});
		buttonRotateAnticlockwise = UtilitiesClient.newButton(rotateAnticlockwiseText, button -> lift.facing = lift.facing.getCounterClockWise());
		buttonRotateClockwise = UtilitiesClient.newButton(rotateClockwiseText, button -> lift.facing = lift.facing.getClockWise());

		font = Minecraft.getInstance().font;
		width1 = Math.max(Math.max(SQUARE_SIZE * 3, font.width(doubleSidedText)), Math.max(font.width(rotateAnticlockwiseText), font.width(rotateClockwiseText))) + TEXT_PADDING * 2;
		width2 = width1 + SQUARE_SIZE;
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonHeightMinus, 0, 0, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonHeightAdd, width1, 0, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonWidthMinus, 0, SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonWidthAdd, width1, SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDepthMinus, 0, SQUARE_SIZE * 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDepthAdd, width1, SQUARE_SIZE * 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetXMinus, 0, SQUARE_SIZE * 3, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetXAdd, width1, SQUARE_SIZE * 3, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetYMinus, 0, SQUARE_SIZE * 4, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetYAdd, width1, SQUARE_SIZE * 4, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetZMinus, 0, SQUARE_SIZE * 5, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetZAdd, width1, SQUARE_SIZE * 5, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonIsDoubleSided, 0, SQUARE_SIZE * 7, width2);
		IDrawing.setPositionAndWidth(buttonLiftStyle, 0, SQUARE_SIZE * 8, width2);
		IDrawing.setPositionAndWidth(buttonRotateAnticlockwise, 0, SQUARE_SIZE * 9, width2);
		IDrawing.setPositionAndWidth(buttonRotateClockwise, 0, SQUARE_SIZE * 10, width2);

		addDrawableChild(buttonHeightMinus);
		addDrawableChild(buttonHeightAdd);
		addDrawableChild(buttonWidthMinus);
		addDrawableChild(buttonWidthAdd);
		addDrawableChild(buttonDepthMinus);
		addDrawableChild(buttonDepthAdd);
		addDrawableChild(buttonOffsetXMinus);
		addDrawableChild(buttonOffsetXAdd);
		addDrawableChild(buttonOffsetYMinus);
		addDrawableChild(buttonOffsetYAdd);
		addDrawableChild(buttonOffsetZMinus);
		addDrawableChild(buttonOffsetZAdd);
		addDrawableChild(buttonIsDoubleSided);
//		addDrawableChild(buttonLiftStyle);
		addDrawableChild(buttonRotateAnticlockwise);
		addDrawableChild(buttonRotateClockwise);
		updateControls();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			Gui.fill(matrices, 0, 0, width2, height, ARGB_BACKGROUND);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredString(matrices, font, Text.translatable("tooltip.mtr.rail_action_height", lift.liftHeight / 2F), width2 / 2, TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("tooltip.mtr.rail_action_width", lift.liftWidth), width2 / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("tooltip.mtr.rail_action_depth", lift.liftDepth), width2 / 2, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.offset_x", lift.liftOffsetX / 2F), width2 / 2, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.offset_y", lift.liftOffsetY), width2 / 2, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.offset_z", lift.liftOffsetZ / 2F), width2 / 2, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		lift.setExtraData(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_LIFT, packet));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void updateControls() {
		buttonHeightMinus.active = lift.liftHeight > MIN_DIMENSION * 2;
		buttonHeightAdd.active = lift.liftHeight < MAX_DIMENSION * 2;
		buttonWidthMinus.active = lift.liftWidth > MIN_DIMENSION;
		buttonWidthAdd.active = lift.liftWidth < MAX_DIMENSION;
		buttonDepthMinus.active = lift.liftDepth > MIN_DIMENSION;
		buttonDepthAdd.active = lift.liftDepth < MAX_DIMENSION;
		buttonOffsetXMinus.active = lift.liftOffsetX > -MAX_OFFSET * 2;
		buttonOffsetXAdd.active = lift.liftOffsetX < MAX_OFFSET * 2;
		buttonOffsetYMinus.active = lift.liftOffsetY > -MAX_OFFSET;
		buttonOffsetYAdd.active = lift.liftOffsetY < MAX_OFFSET;
		buttonOffsetZMinus.active = lift.liftOffsetZ > -MAX_OFFSET * 2;
		buttonOffsetZAdd.active = lift.liftOffsetZ < MAX_OFFSET * 2;
		buttonIsDoubleSided.setChecked(lift.isDoubleSided);
		buttonLiftStyle.setMessage(Text.translatable("gui.mtr.lift_style", Text.translatable("gui.mtr.lift_style_" + lift.liftStyle.toString().toLowerCase(Locale.ENGLISH))));
	}
}
