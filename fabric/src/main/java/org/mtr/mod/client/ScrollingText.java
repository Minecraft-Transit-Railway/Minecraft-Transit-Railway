package org.mtr.mod.client;

import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public class ScrollingText implements IGui {

	private float ticksOffset;
	private DynamicTextureCache.DynamicResource dynamicResource;

	private final float availableWidth;
	private final float availableHeight;
	private final int scrollSpeed;
	private final boolean isFullPixel;

	public ScrollingText(float availableWidth, float availableHeight, int scrollSpeed, boolean isFullPixel) {
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;
		this.scrollSpeed = scrollSpeed;
		this.isFullPixel = isFullPixel;
	}

	public void changeImage(@Nullable DynamicTextureCache.DynamicResource dynamicResource) {
		if (this.dynamicResource != dynamicResource) {
			this.dynamicResource = dynamicResource;
			ticksOffset = InitClient.getGameTick();
		}
	}

	public void scrollText(StoredMatrixTransformations storedMatrixTransformations) {
		if (dynamicResource != null) {
			final int pixelScale = isFullPixel ? 1 : RouteMapGenerator.PIXEL_SCALE;
			final float scale = availableHeight / dynamicResource.height;
			final int widthSteps = (int) Math.floor(availableWidth / scale / pixelScale);
			final int imageSteps = dynamicResource.width / pixelScale;
			final int totalSteps = widthSteps + imageSteps;
			final int step = Math.round((InitClient.getGameTick() - ticksOffset) * scrollSpeed) % totalSteps;
			final float width = Math.min(Math.min(availableWidth, dynamicResource.width * scale), Math.min(step * pixelScale * scale, (totalSteps - step) * pixelScale * scale));
			RenderTrains.scheduleRender(dynamicResource.identifier, false, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, Math.max(widthSteps - step, 0) * scale * pixelScale, 0, width, availableHeight, Math.max((float) (step - widthSteps) / imageSteps, 0), 0, Math.min((float) step / imageSteps, 1), 1, Direction.UP, ARGB_WHITE, GraphicsHolder.getDefaultLight());
				graphicsHolder.pop();
			});
		}
	}
}
