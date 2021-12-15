package mtr.gui;

import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.data.IGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileUploaderScreen extends ScreenMapper implements IGui {

	private final ScreenMapper screen;
	private final Consumer<List<Path>> filesCallback;

	public FileUploaderScreen(ScreenMapper screen, Consumer<List<Path>> filesCallback) {
		super(new LiteralText(""));
		this.screen = screen;
		this.filesCallback = filesCallback;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.drag_file_to_upload"), width / 2, (height - TEXT_HEIGHT) / 2, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void filesDragged(List<Path> paths) {
		filesCallback.accept(paths);
		onClose();
	}

	@Override
	public void onClose() {
		if (client != null) {
			UtilitiesClient.setScreen(client, screen);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
