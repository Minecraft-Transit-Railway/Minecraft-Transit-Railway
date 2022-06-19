package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameSignBase;
import mtr.block.CustomContentBlockEntity;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderStationNameSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Custom content to display for <b>Station Name Sign Block</b>.
 * @see BlockStationNameSignBase
 * @see RenderStationNameSign
 */

public class CustomContentScreen extends ScreenMapper implements IGui, IPacket
{
    protected final BlockPos pos;
    protected final WidgetBetterTextField textField = new WidgetBetterTextField(null);
    protected String content;

    public CustomContentScreen(BlockPos pos) {
        super(new TextComponent(""));
        this.pos = pos;

        minecraft = Minecraft.getInstance();
        final ClientLevel world = minecraft.level;
        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CustomContentBlockEntity)
                content = ((CustomContentBlockEntity)entity).content;
        } else {
            content = "";
        }
        textField.setValue(content);
    }

    @Override
    protected void init() {
        super.init();
        IDrawing.setPositionAndWidth(textField, SQUARE_SIZE, SQUARE_SIZE, width);
        addDrawableChild(textField);
        textField.setValue(textField.getValue());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            font.draw(matrices, new TranslatableComponent("gui.mtr.custom_content"), SQUARE_SIZE, TEXT_PADDING, ARGB_WHITE);
            textField.setWidth((int)(width /1.1));
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Override
    public void onClose() {
        PacketTrainDataGuiClient.sendCustomContentC2S(pos, textField.getValue());
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
