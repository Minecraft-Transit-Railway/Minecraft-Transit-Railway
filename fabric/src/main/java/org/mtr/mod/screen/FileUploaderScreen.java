package org.mtr.mod.screen;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileUploaderScreen extends ScreenExtension implements IGui {

	private final ScreenExtension screen;
	private final Consumer<List<Path>> filesCallback;

	public FileUploaderScreen(ScreenExtension screen, Consumer<List<Path>> filesCallback) {
		super();
		this.screen = screen;
		this.filesCallback = filesCallback;
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_DRAG_FILE_TO_UPLOAD.getMutableText(), width / 2, (height - TEXT_HEIGHT) / 2, ARGB_WHITE);
	}

	@Override
	public void filesDragged2(List<Path> paths) {
		filesCallback.accept(paths);
		onClose2();
	}

	@Override
	public void onClose2() {
		MinecraftClient.getInstance().openScreen(new Screen(screen));
	}
}
