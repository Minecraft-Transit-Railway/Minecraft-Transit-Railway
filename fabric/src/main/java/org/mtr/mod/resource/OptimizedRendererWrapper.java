package org.mtr.mod.resource;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.render.obj.AtlasManager;
import org.mtr.mod.Init;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public final class OptimizedRendererWrapper implements IGui {

	@Nullable
	private final OptimizedRenderer optimizedRenderer;
	@Nullable
	private AtlasManager atlasManager;
	private boolean loggedMissingAtlasManager;

	public OptimizedRendererWrapper() {
		this.optimizedRenderer = OptimizedRenderer.hasOptimizedRendering() ? new OptimizedRenderer() : null;
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
			optimizedRenderer.queue(optimizedModel.optimizedModel, graphicsHolder, ARGB_WHITE, light);
		}
	}

	public void render(boolean renderTranslucent) {
		if (optimizedRenderer != null) {
			optimizedRenderer.render(renderTranslucent);
		}
	}

	@Nullable
	public AtlasManager getAtlasManager() {
		if (atlasManager != null || optimizedRenderer == null) {
			return atlasManager;
		}

		Class<?> currentClass = optimizedRenderer.getClass();
		while (currentClass != null) {
			for (final Field field : currentClass.getDeclaredFields()) {
				if (!AtlasManager.class.isAssignableFrom(field.getType())) {
					continue;
				}
				try {
					field.setAccessible(true);
					final Object object = field.get(optimizedRenderer);
					if (object instanceof AtlasManager) {
						atlasManager = (AtlasManager) object;
						return atlasManager;
					}
				} catch (Exception e) {
					if (!loggedMissingAtlasManager) {
						loggedMissingAtlasManager = true;
						Init.LOGGER.warn("Failed to access AtlasManager from OptimizedRenderer", e);
					}
					return null;
				}
			}
			currentClass = currentClass.getSuperclass();
		}

		if (!loggedMissingAtlasManager) {
			loggedMissingAtlasManager = true;
			Init.LOGGER.warn("AtlasManager field not found on OptimizedRenderer; GPU OBJ loading will fall back to standard rendering.");
		}
		return null;
	}
}
