package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockLiftTrackFloor;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LiftTrackFloorScreen extends ScreenMapper implements IGui, IPacket {

	private final WidgetBetterTextField textFieldFloorNumber;
	private final WidgetBetterTextField textFieldFloorDescription;
	private final WidgetBetterCheckbox checkboxShouldDing;

	private final BlockPos pos;
	private final String initialFloorNumber;
	private final String initialFloorDescription;
	private final boolean initialShouldDing;
	private final int textWidth;
	private static final Component TEXT_FLOOR_NUMBER = Text.translatable("gui.mtr.lift_floor_number");
	private static final Component TEXT_FLOOR_DESCRIPTION = Text.translatable("gui.mtr.lift_floor_description");
	private static final int TEXT_FIELD_WIDTH = 240;

	public LiftTrackFloorScreen(BlockPos pos) {
		super(Text.literal(""));
		this.pos = pos;

		textFieldFloorNumber = new WidgetBetterTextField("1", 8);
		textFieldFloorDescription = new WidgetBetterTextField("Concourse");
		checkboxShouldDing = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.lift_should_ding"), checked -> {
		});

		final Level world = Minecraft.getInstance().level;
		if (world == null) {
			initialFloorNumber = "1";
			initialFloorDescription = "";
			initialShouldDing = false;
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
				initialFloorNumber = ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) entity).getFloorNumber();
				initialFloorDescription = ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) entity).getFloorDescription();
				initialShouldDing = ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) entity).getShouldDing();
			} else {
				initialFloorNumber = "1";
				initialFloorDescription = "";
				initialShouldDing = false;
			}
		}

		font = Minecraft.getInstance().font;
		textWidth = Math.max(font.width(TEXT_FLOOR_NUMBER), font.width(TEXT_FLOOR_DESCRIPTION));
	}

	@Override
	protected void init() {
		super.init();

		final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
		final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
		IDrawing.setPositionAndWidth(textFieldFloorNumber, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + TEXT_FIELD_PADDING / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldFloorDescription, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(checkboxShouldDing, startX, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING * 2, TEXT_FIELD_WIDTH);

		textFieldFloorNumber.setValue(initialFloorNumber);
		textFieldFloorDescription.setValue(initialFloorDescription);
		checkboxShouldDing.setChecked(initialShouldDing);

		addDrawableChild(textFieldFloorNumber);
		addDrawableChild(textFieldFloorDescription);
		addDrawableChild(checkboxShouldDing);
	}

	@Override
	public void tick() {
		textFieldFloorNumber.tick();
		textFieldFloorDescription.tick();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
			final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
			font.draw(matrices, TEXT_FLOOR_NUMBER, startX, startY + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, TEXT_FLOOR_DESCRIPTION, startX, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2F + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendLiftTrackFloorC2S(pos, textFieldFloorNumber.getValue(), textFieldFloorDescription.getValue(), checkboxShouldDing.selected());
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
