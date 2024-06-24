package org.mtr.mod.screen;

import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketUpdateCustomRail;

public class CustomRailScreen extends ScreenExtension implements IGui {

    private final int speed;
    private final boolean isOneWay;

    private final TextFieldWidgetExtension textFieldSpeed;
    private final CheckboxWidgetExtension checkboxIsOneWay;

    public CustomRailScreen(int speed, boolean isOneWay) {
        super();
        this.speed = speed;
        this.isOneWay = isOneWay;

        textFieldSpeed = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, String.valueOf(speed), 3, TextCase.DEFAULT, "\\D", null);
        checkboxIsOneWay = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE * 2, false, checked -> {});
    }

    @Override
    protected void init2() {
        super.init2();

        IDrawing.setPositionAndWidth(textFieldSpeed, width / 2 - SQUARE_SIZE * 4 - TEXT_PADDING, SQUARE_SIZE, SQUARE_SIZE * 4 - TEXT_FIELD_PADDING);
        textFieldSpeed.setText2(String.valueOf(speed));
        textFieldSpeed.setChangedListener2(text -> {
            if (!text.isEmpty()) {
                if (Integer.parseInt(text) > 300) {
                    textFieldSpeed.setText2("300");
                }
            }
        });

        IDrawing.setPositionAndWidth(checkboxIsOneWay, width / 2 - SQUARE_SIZE * 4 - TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_HEIGHT * 2, SQUARE_SIZE * 2);
        checkboxIsOneWay.setChecked(isOneWay);

        addChild(new ClickableWidget(textFieldSpeed));
        addChild(new ClickableWidget(checkboxIsOneWay));
    }

    @Override
    public void tick2() {
        textFieldSpeed.tick2();
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        final MutableText speedText = TextHelper.translatable("gui.mtr.speed");
        graphicsHolder.drawText(speedText, TEXT_PADDING + SQUARE_SIZE * 3, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        final MutableText isOneWayText = TextHelper.translatable("gui.mtr.is_one_way");
        graphicsHolder.drawText(isOneWayText, TEXT_PADDING + SQUARE_SIZE * 3, SQUARE_SIZE * 3 + TEXT_HEIGHT * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());

        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public void onClose2() {
        int finalSpeed;
        try {
            finalSpeed = Math.max(1, Integer.parseInt(textFieldSpeed.getText2()));
            if (finalSpeed > 300) {
                finalSpeed = 300;
            }
        } catch (Exception ignored) {
            finalSpeed = speed;
        }

        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateCustomRail(finalSpeed, checkboxIsOneWay.isChecked2()));

        super.onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }
}
