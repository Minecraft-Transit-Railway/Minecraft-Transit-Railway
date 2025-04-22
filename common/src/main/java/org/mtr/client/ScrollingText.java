package org.mtr.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.MTRClient;
import org.mtr.data.IGui;

import javax.annotation.Nullable;

public class ScrollingText {

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

	@Nullable
	public Identifier getTextureId() {
		return dynamicResource.identifier;
	}

	public void scrollText(MatrixStack matrixStack, VertexConsumer vertexConsumer) {
		if (dynamicResource != null) {
			final int pixelScale = isFullPixel ? 1 : RouteMapGenerator.PIXEL_SCALE;
			final double scale = availableHeight / dynamicResource.height;
			final int widthSteps = (int) Math.floor(availableWidth / scale / pixelScale);
			final int imageSteps = dynamicResource.width / pixelScale;
			final int totalSteps = widthSteps + imageSteps;
			final int step = Math.round((MTRClient.getGameTick() - ticksOffset) * scrollSpeed) % totalSteps;
			final double width = Math.min(Math.min(availableWidth, dynamicResource.width * scale), Math.min(step * pixelScale * scale, (totalSteps - step) * pixelScale * scale));
			final float x1 = (float) (Math.max(widthSteps - step, 0) * scale * pixelScale);
			final float x2 = x1 + (float) width;
			final float u1 = Math.max((float) (step - widthSteps) / imageSteps, 0);
			final float u2 = Math.min((float) step / imageSteps, 1);
			vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x1, 0, 0).color(IGui.ARGB_WHITE).texture(u1, 0).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
			vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x1, (float) availableHeight, 0).color(IGui.ARGB_WHITE).texture(u1, 1).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
			vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x2, (float) availableHeight, 0).color(IGui.ARGB_WHITE).texture(u2, 1).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
			vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), x2, 0, 0).color(IGui.ARGB_WHITE).texture(u2, 0).overlay(OverlayTexture.DEFAULT_UV).light(IGui.DEFAULT_LIGHT).normal(0, 1, 0);
		}
	}
}
