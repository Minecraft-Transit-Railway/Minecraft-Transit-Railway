package org.mtr.client;

import net.minecraft.util.math.Direction;
import org.mtr.MTRClient;
import org.mtr.data.IGui;
import org.mtr.render.MainRenderer;
import org.mtr.render.QueuedRenderLayer;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;

public class ScrollingText implements IGui {

	private float ticksOffset;
	private DynamicTextureCache.DynamicResource dynamicResource;

	private final double availableWidth;
	private final double availableHeight;
	private final int scrollSpeed;
	private final boolean isFullPixel;

	public ScrollingText(double availableWidth, double availableHeight, int scrollSpeed, boolean isFullPixel) {
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;
		this.scrollSpeed = scrollSpeed;
		this.isFullPixel = isFullPixel;
	}

	public void changeImage(@Nullable DynamicTextureCache.DynamicResource dynamicResource) {
		if (this.dynamicResource != dynamicResource) {
			this.dynamicResource = dynamicResource;
			ticksOffset = MTRClient.getGameTick();
		}
	}

	public void scrollText(StoredMatrixTransformations storedMatrixTransformations) {
		if (dynamicResource != null) {
			final int pixelScale = isFullPixel ? 1 : RouteMapGenerator.PIXEL_SCALE;
			final double scale = availableHeight / dynamicResource.height;
			final int widthSteps = (int) Math.floor(availableWidth / scale / pixelScale);
			final int imageSteps = dynamicResource.width / pixelScale;
			final int totalSteps = widthSteps + imageSteps;
			final int step = Math.round((MTRClient.getGameTick() - ticksOffset) * scrollSpeed) % totalSteps;
			final double width = Math.min(Math.min(availableWidth, dynamicResource.width * scale), Math.min(step * pixelScale * scale, (totalSteps - step) * pixelScale * scale));
			MainRenderer.scheduleRender(dynamicResource.identifier, true, QueuedRenderLayer.LIGHT_2, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, (float) (Math.max(widthSteps - step, 0) * scale * pixelScale), 0, (float) width, (float) availableHeight, Math.max((float) (step - widthSteps) / imageSteps, 0), 0, Math.min((float) step / imageSteps, 1), 1, Direction.UP, ARGB_WHITE, DEFAULT_LIGHT);
				matrixStack.pop();
			});
		}
	}
}
