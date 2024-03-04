package org.mtr.mod.resource;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mod.render.RenderVehicles;

import javax.annotation.Nullable;

public final class OptimizedRendererWrapper {

	@Nullable
	private final OptimizedRenderer optimizedRenderer;

	public OptimizedRendererWrapper() {
		this.optimizedRenderer = RenderVehicles.useOptimizedRendering() ? new OptimizedRenderer() : null;
	}

	public void beginReload() {
		if (optimizedRenderer != null) {
			optimizedRenderer.beginReload();
		}
	}

	public void finishReload() {
		if (optimizedRenderer != null) {
			optimizedRenderer.finishReload();
		}
	}

	public void queue(OptimizedModelWrapper optimizedModel, GraphicsHolder graphicsHolder, int light) {
		if (optimizedRenderer != null && optimizedModel.optimizedModel != null) {
			optimizedRenderer.queue(optimizedModel.optimizedModel, graphicsHolder, light);
		}
	}

	public void render(boolean renderTranslucent) {
		if (optimizedRenderer != null) {
			optimizedRenderer.render(renderTranslucent);
		}
	}
}
