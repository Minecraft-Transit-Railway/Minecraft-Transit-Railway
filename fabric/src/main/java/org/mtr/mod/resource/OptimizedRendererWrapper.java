package org.mtr.mod.resource;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.render.tool.GlStateTracker;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class OptimizedRendererWrapper implements IGui {

	@Nullable
	private final OptimizedRenderer optimizedRenderer;
	private boolean shadersReady;
	private int reloadDepth;

	public OptimizedRendererWrapper() {
		this.optimizedRenderer = OptimizedRenderer.hasOptimizedRendering() ? new OptimizedRenderer() : null;
	}

	public void beginReload() {
		if (optimizedRenderer != null) {
			if (reloadDepth == 0) {
				optimizedRenderer.beginReload();
				shadersReady = true;
			}
			reloadDepth++;
		}
	}

	public void finishReload() {
		if (optimizedRenderer != null && reloadDepth > 0) {
			reloadDepth--;
			if (reloadDepth == 0) {
				optimizedRenderer.finishReload();
			}
		}
	}

	public void queue(OptimizedModelWrapper optimizedModel, GraphicsHolder graphicsHolder, int light) {
		if (optimizedRenderer != null && optimizedModel.optimizedModel != null) {
			optimizedRenderer.queue(optimizedModel.optimizedModel, graphicsHolder, ARGB_WHITE, light);
		}
	}

	public void queue(OptimizedModel optimizedModel, GraphicsHolder graphicsHolder, int color, int light) {
		if (optimizedRenderer != null && optimizedModel != null) {
			optimizedRenderer.queue(optimizedModel, graphicsHolder, color, light);
		}
	}

	@Nullable
	public <T> T upload(Supplier<T> uploader) {
		if (optimizedRenderer == null) {
			return null;
		}
		if (reloadDepth > 0) {
			return uploader.get();
		}
		final boolean initializeShaders = !shadersReady;
		if (initializeShaders) {
			beginReload();
		} else {
			GlStateTracker.capture();
		}
		try {
			return uploader.get();
		} finally {
			if (initializeShaders) {
				finishReload();
			} else {
				GlStateTracker.restore();
			}
		}
	}

	public void markReloadRequired() {
		shadersReady = false;
	}

	public void render(boolean renderTranslucent) {
		if (optimizedRenderer != null) {
			optimizedRenderer.render(renderTranslucent);
		}
	}
}
