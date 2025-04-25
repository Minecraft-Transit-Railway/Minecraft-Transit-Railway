package org.mtr.screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import org.mtr.MTR;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.Drawing;
import org.mtr.widget.BetterTextFieldWidget;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FontRenderingSandboxScreen extends MTRScreenBase {

	private long readTime = 0;
	private FontRenderOptions fontRenderOptions = FontRenderOptions.builder().horizontalSpace(200).verticalSpace(100).horizontalPositioning(FontRenderOptions.Alignment.CENTER).verticalPositioning(FontRenderOptions.Alignment.CENTER).build();

	private final BetterTextFieldWidget textFieldWidget = new BetterTextFieldWidget(DEFAULT_TEXT, Integer.MAX_VALUE, TextCase.DEFAULT, null, DEFAULT_TEXT);
	private final CheckboxWidget checkboxWidget;
	private final Path fontRenderOptionsPath = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/cache/mtr_font_rendering_sandbox.json");

	private static final String DEFAULT_TEXT = "Hello world!";

	public FontRenderingSandboxScreen() {
		try {
			Files.createDirectories(fontRenderOptionsPath.getParent());
			if (!Files.exists(fontRenderOptionsPath)) {
				write();
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}

		checkboxWidget = CheckboxWidget.builder(Text.literal("Use MTR Font"), MinecraftClient.getInstance().textRenderer).build();
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(textFieldWidget, IGui.TEXT_FIELD_PADDING / 2, height - IGui.SQUARE_SIZE - IGui.TEXT_FIELD_PADDING, width / 2 - IGui.TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(checkboxWidget, width / 2, height - IGui.SQUARE_SIZE - IGui.TEXT_FIELD_PADDING / 2, width / 2);
		textFieldWidget.setText(DEFAULT_TEXT);
		addDrawableChild(textFieldWidget);
		addDrawableChild(checkboxWidget);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		final int newHeight = height - IGui.SQUARE_SIZE - IGui.TEXT_FIELD_PADDING;
		context.getMatrices().push();
		context.getMatrices().translate(width / 2F, newHeight / 2F, 0);

		final int boxX = (int) fontRenderOptions.getHorizontalPositioning().getOffset(fontRenderOptions.getHorizontalSpace());
		final int boxY = (int) fontRenderOptions.getVerticalPositioning().getOffset(fontRenderOptions.getVerticalSpace());
		context.fill(boxX, boxY, boxX + (int) fontRenderOptions.getHorizontalSpace(), boxY + (int) fontRenderOptions.getVerticalSpace(), IGui.ARGB_BLACK);

		final Drawing drawing = new Drawing(context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		if (checkboxWidget.isChecked()) {
			FontGroups.renderMTR(drawing, textFieldWidget.getText(), fontRenderOptions);
		} else {
			FontGroups.renderMinecraft(drawing, textFieldWidget.getText(), fontRenderOptions);
		}

		context.getMatrices().pop();
	}

	@Override
	public void tick() {
		final long currentTime = System.currentTimeMillis();
		if (currentTime > readTime) {
			try {
				fontRenderOptions = new ObjectMapper().readValue(Files.readString(fontRenderOptionsPath, StandardCharsets.UTF_8), FontRenderOptions.class);
				readTime = currentTime + 500;
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
				write();
			}
		}
	}

	private void write() {
		try {
			Files.writeString(fontRenderOptionsPath, new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(fontRenderOptions), StandardCharsets.UTF_8);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}
}
