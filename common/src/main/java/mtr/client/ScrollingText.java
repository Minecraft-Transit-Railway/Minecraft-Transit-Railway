package mtr.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.data.IGui;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class ScrollingText implements IGui {

	private float ticksOffset;
	private ClientCache.DynamicResource dynamicResource;
	private VertexConsumer vertexConsumer;

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

	public void changeImage(ClientCache.DynamicResource dynamicResource) {
		if (this.dynamicResource != dynamicResource) {
			this.dynamicResource = dynamicResource;
			ticksOffset = MTRClient.getGameTick();
		}
	}

	public void setVertexConsumer(MultiBufferSource vertexConsumers) {
		vertexConsumer = dynamicResource == null ? null : vertexConsumers.getBuffer(MoreRenderLayers.getLight(dynamicResource.resourceLocation, true));
	}

	public void scrollText(PoseStack matrices) {
		if (vertexConsumer != null) {
			final int pixelScale = isFullPixel ? 1 : RouteMapGenerator.PIXEL_SCALE;
			final float scale = availableHeight / dynamicResource.height;
			final int widthSteps = (int) Math.floor(availableWidth / scale / pixelScale);
			final int imageSteps = dynamicResource.width / pixelScale;
			final int totalSteps = widthSteps + imageSteps;
			final int step = Math.round((MTRClient.getGameTick() - ticksOffset) * scrollSpeed) % totalSteps;
			final float width = Math.min(Math.min(availableWidth, dynamicResource.width * scale), Math.min(step * pixelScale * scale, (totalSteps - step) * pixelScale * scale));
			IDrawing.drawTexture(matrices, vertexConsumer, Math.max(widthSteps - step, 0) * scale * pixelScale, 0, width, availableHeight, Math.max((float) (step - widthSteps) / imageSteps, 0), 0, Math.min((float) step / imageSteps, 1), 1, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
		}
	}
}
