package org.mtr.mod.screen;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.PIDSLayoutData;
import org.mtr.mod.packet.PacketUpdatePIDSLayout;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.resource.PIDSData;

import java.util.List;

public class PIDSLayoutEditScreen extends ScreenExtension implements IGui {
    private String rawData;
    private PIDSData data = null;
    private boolean invalidJSON = false;
    private final TextFieldWidgetExtension dataInput;
    private final ButtonWidgetExtension confirmButton;
    private static final int MAX_ID_LENGTH = 256;

    public PIDSLayoutEditScreen(String rawData) {
        this.rawData = rawData;
        this.dataInput = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_ID_LENGTH, TextCase.LOWER, null, "");
        this.confirmButton = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.confirm"), button -> {
            if (invalidJSON || data == null) return;
            InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdatePIDSLayout(data.getID(), this.dataInput.getText2()));
            PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata = new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(data.getID(), data.getName(), data.getDescription(), data.getAuthor(), data.getModules().size(), new PIDSRenderController(rawData).arrivals);
            // Add the new metadata locally
            InitClient.pidsLayoutCache.updateMetadata(metadata);
            // Create a new dashboard screen so that all of the new data is displayed
            onClose2();
        });
        // validate JSON
        try {
            data = new PIDSData(new JsonReader(JsonParser.parseString(rawData)));
            invalidJSON = false;
            this.confirmButton.setVisibleMapped(true);
        } catch (Exception e) {
            data = null;
            invalidJSON = true;
            this.confirmButton.setVisibleMapped(false);
        }
    }

    @Override
    protected void init2() {
        super.init2();

        IDrawing.setPositionAndWidth(dataInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
        dataInput.setText2(rawData);
        addChild(new ClickableWidget(dataInput));

        IDrawing.setPositionAndWidth(confirmButton, SQUARE_SIZE, height - SQUARE_SIZE * 2, PANEL_WIDTH);
        confirmButton.setMessage2(new Text(TextHelper.translatable("gui.mtr.save_layout").data));
        addChild(new ClickableWidget(confirmButton));
    }

    @Override
    public void tick2() {
        super.tick2();
        dataInput.tick3();
        // check if text input has changed
        if (dataInput.getText2().equals(rawData)) {
            return;
        }
        rawData = dataInput.getText2();
        // validate JSON
        try {
            data = new PIDSData(new JsonReader(JsonParser.parseString(rawData)));
            invalidJSON = false;
            this.confirmButton.setVisibleMapped(true);
        } catch (Exception e) {
            data = null;
            invalidJSON = true;
            this.confirmButton.setVisibleMapped(false);
        }
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        graphicsHolder.drawText(TextHelper.translatable("gui.mtr.paste_json"), SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        if (invalidJSON || data == null) {
            graphicsHolder.drawText(TextHelper.translatable("gui.mtr.invalid_json").formatted(TextFormatting.RED), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        } else {
            PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata metadata = new PIDSLayoutData.PIDSLayoutEntry.PIDSLayoutMetadata(data.getID(), data.getName(), data.getDescription(), data.getAuthor(), data.getModules().size(), new PIDSRenderController(rawData).arrivals);
            int y = SQUARE_SIZE * 4;
            y = drawWrappedText(graphicsHolder, TextHelper.literal(metadata.name), y, ARGB_WHITE);
            y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_author", metadata.author.isEmpty() ? TextHelper.translatable("gui.mtr.anonymous").getString() : metadata.author), y, ARGB_WHITE);
            y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_arrivals", metadata.arrivalCount), y, ARGB_WHITE);
            y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.layout_modules", metadata.moduleCount), y, ARGB_WHITE);
            y = drawWrappedText(graphicsHolder, TextHelper.literal(metadata.description), y, ARGB_LIGHT_GRAY);
            // check to see if id already exists
            if (data != null && InitClient.pidsLayoutCache.getMetadata().stream().anyMatch(m -> m.id.equals(data.getID()))) {
                graphicsHolder.drawText(TextHelper.translatable("gui.mtr.id_already_exists").formatted(TextFormatting.YELLOW), SQUARE_SIZE, height - SQUARE_SIZE * 4, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
            }
        }
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
        final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH));
        int newY = y;
        for (final OrderedText formattedCharSequence : splitText) {
            final int nextY = newY + TEXT_HEIGHT + 2;
            if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
                graphicsHolder.drawText("...", SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
                return height;
            } else {
                graphicsHolder.drawText(formattedCharSequence, SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
            }
            newY = nextY;
        }
        return newY + TEXT_PADDING;
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void onClose2() {
        MinecraftClient.getInstance().openScreen(new Screen(new PIDSDashboardScreen()));
    }
}
