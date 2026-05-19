package org.mtr.mod.resource;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class OptimizedRendererWrapper implements IGui {

	@Nullable
	private final OptimizedRenderer optimizedRenderer;
	private int reloadDepth;

	public OptimizedRendererWrapper() {
		this.optimizedRenderer = OptimizedRenderer.hasOptimizedRendering() ? new OptimizedRenderer() : null;
	}

	public void beginReload() {
		if (optimizedRenderer != null && reloadDepth++ == 0) {
			optimizedRenderer.beginReload();
		}
	}

	public void finishReload() {
		if (optimizedRenderer != null && reloadDepth > 0 && --reloadDepth == 0) {
			optimizedRenderer.finishReload();
		}
	}

	public void runWithProtectedState(Runnable runnable) {
		beginReload();
		try {
			runnable.run();
		} finally {
			finishReload();
		}
	}

	public <T> T runWithProtectedState(Supplier<T> supplier) {
		beginReload();
		try {
			return supplier.get();
		} finally {
			finishReload();
		}
	}

	public void queue(OptimizedModelWrapper optimizedModel, GraphicsHolder graphicsHolder, int light) {
		if (optimizedRenderer != null && optimizedModel.optimizedModel != null) {
			optimizedRenderer.queue(optimizedModel.optimizedModel, graphicsHolder, ARGB_WHITE, light);
		}
	}

	public void render(boolean renderTranslucent) {
		if (optimizedRenderer != null) {
			optimizedRenderer.render(renderTranslucent);
		}
	}
}
