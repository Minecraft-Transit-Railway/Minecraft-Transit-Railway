package org.mtr.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.mtr.MTRClient;
import org.mtr.data.IGui;

public class ScrollingText {

	private float ticksOffset;
	private DynamicTextureCache.@Nullable DynamicResource dynamicResource;

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

	public void changeImage(DynamicTextureCache.@Nullable DynamicResource dynamicResource) {
		if (this.dynamicResource != dynamicResource) {
			this.dynamicResource = dynamicResource;
			ticksOffset = MTRClient.getGameTick();
		}
	}

	@Nullable
	public ResourceLocation getTextureId() {
		return dynamicResource == null ? null : dynamicResource.identifier;
	}

	public void scrollText(PoseStack matrixStack, VertexConsumer vertexConsumer) {
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
			vertexConsumer.addVertex(matrixStack.last().pose(), x1, 0, 0).setColor(IGui.ARGB_WHITE).setUv(u1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(IGui.DEFAULT_LIGHT).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x1, (float) availableHeight, 0).setColor(IGui.ARGB_WHITE).setUv(u1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(IGui.DEFAULT_LIGHT).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x2, (float) availableHeight, 0).setColor(IGui.ARGB_WHITE).setUv(u2, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(IGui.DEFAULT_LIGHT).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x2, 0, 0).setColor(IGui.ARGB_WHITE).setUv(u2, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(IGui.DEFAULT_LIGHT).setNormal(0, 1, 0);
		}
	}
}
