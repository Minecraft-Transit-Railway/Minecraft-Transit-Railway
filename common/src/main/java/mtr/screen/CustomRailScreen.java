package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.network.chat.Component;

public class CustomRailScreen extends ScreenMapper implements IGui {

    private final int speed;
    private final boolean isOneWay;
    private final WidgetBetterTextField textFieldSpeed;
    private final WidgetBetterCheckbox buttonIsOneWay;
    private final Component speedText = Text.translatable("gui.mtr.rail_speed");
    private final Component isOneWayText = Text.translatable("gui.mtr.one_way");

    public CustomRailScreen(int speed, boolean isOneWay) {
        super(Text.literal(""));
        this.speed = speed;
        this.isOneWay = isOneWay;

        textFieldSpeed = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Integer.toString(speed), 3);
        buttonIsOneWay = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, isOneWayText, checked -> {});
    }

    @Override
    protected void init() {
        super.init();
        final int textWidth = font.width(isOneWayText) + SQUARE_SIZE + TEXT_PADDING * 2;

        IDrawing.setPositionAndWidth(textFieldSpeed, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
        textFieldSpeed.setValue(String.valueOf(speed));
        addDrawableChild(textFieldSpeed);

        IDrawing.setPositionAndWidth(buttonIsOneWay,  width - SQUARE_SIZE - textWidth + TEXT_PADDING, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING), textWidth);
        buttonIsOneWay.setChecked(isOneWay);
        addDrawableChild(buttonIsOneWay);
    }

    @Override
    public void tick() {
        if (textFieldSpeed.getValue().length() >= 1) {
            if (Integer.parseInt(textFieldSpeed.getValue()) > 300) {
                textFieldSpeed.setValue("300");
            } else if (Integer.parseInt(textFieldSpeed.getValue()) == 0) {
                textFieldSpeed.setValue("1");
            }
        }
        textFieldSpeed.tick();
    }

    @Override
    public void onClose() {
        final String textFieldValue = textFieldSpeed.getValue();
        PacketTrainDataGuiClient.sendRailCustomUpdateC2S(textFieldValue.length() >= 1  ? Integer.parseInt(textFieldValue) > 0 && Integer.parseInt(textFieldValue) <= 300 ? Integer.parseInt(textFieldValue) : speed : speed, buttonIsOneWay.selected());
        super.onClose();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            font.draw(matrices, speedText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
