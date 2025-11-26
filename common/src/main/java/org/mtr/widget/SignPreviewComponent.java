package org.mtr.widget;

import gg.essential.universal.UMatrixStack;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import kotlin.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.mtr.core.tool.Utilities;
import org.mtr.registry.UConverters;
import org.mtr.resource.SignResource;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.ReleasedDynamicTextureRegistry;

import java.awt.*;
import java.util.function.IntConsumer;

public final class SignPreviewComponent extends SlotBackgroundComponent {

	private int editingIndex;
	private int hoverEditIndex;
	private int hoverDeleteIndex;

	private final LongAVLTreeSet[] selectedIds;
	private final String[] signIds;
	private final GuiAnimation guiAnimation = new GuiAnimation();

	private static final int SPRITE_SIZE = 18;
	private static final int ARROW_OFFSET = 14;
	private static final int ANIMATION_DURATION = 200;

	public SignPreviewComponent(LongAVLTreeSet[] selectedIds, String[] signIds) {
		this.selectedIds = selectedIds;
		this.signIds = signIds;

		onMouseClickConsumer(clickEvent -> {
			if (hoverDeleteIndex >= 0 && hoverDeleteIndex < this.signIds.length) {
				this.signIds[hoverDeleteIndex] = null;
			}
		});
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		super.draw(matrixStack);
		guiAnimation.tick();
		final float left = getLeft() + 1;
		final float top = getTop() + 1;
		final float right = getRight() - 1;
		final float bottom = getBottom() - 1;
		final float signSize = bottom - top;

		drawTexture(ReleasedDynamicTextureRegistry.ARROW_DOWN_TEXTURE.get(), vertexConsumer -> {
			final double x = left + guiAnimation.getCurrentValue() * signSize + (signSize - SPRITE_SIZE) / 2;
			final double y = top - ARROW_OFFSET;
			drawTexturedQuad(matrixStack, vertexConsumer, (float) x, (float) y, (float) x + SPRITE_SIZE, (float) y + SPRITE_SIZE, 0, 0, 1, 1);
		});

		matrixStack.push();
		matrixStack.translate(getLeft() + 1, getTop() + 1, 0);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		SignResource.render(UConverters.convert(matrixStack), minecraftClient.getBufferBuilders().getEntityVertexConsumers(), selectedIds, signIds, getHeight() - 2, 0);
		minecraftClient.getBufferBuilders().getEntityVertexConsumers().draw();
		matrixStack.pop();

		final Pair<Float, Float> mousePosition = getMousePosition();
		final float mouseX = mousePosition.getFirst();
		final float mouseY = mousePosition.getSecond();
		hoverEditIndex = -1;
		hoverDeleteIndex = -1;

		if (mouseX >= left && mouseX < right && mouseY >= top && mouseY < bottom) {
			final int signIndex = (int) Math.floor((mouseX - left) / signSize);
			final float x1 = left + signIndex * signSize;
			final float x2 = left + (signIndex + 1) * signSize;
			final Color color;
			final boolean drawCross;

			if (Screen.hasShiftDown()) {
				if (signIds[signIndex] != null) {
					hoverDeleteIndex = signIndex;
					color = new Color(0xFF, 0, 0, 0x7F);
					drawCross = true;
				} else {
					color = null;
					drawCross = false;
				}
			} else {
				if (signIndex == editingIndex) {
					color = null;
				} else {
					color = new Color(0xFF, 0xFF, 0xFF, 0x7F);
					hoverEditIndex = signIndex;
				}
				drawCross = false;
			}

			if (color != null) {
				drawRectangle(vertexConsumer -> {
					vertexConsumer.pos(matrixStack, x1, bottom, 0).color(color).endVertex();
					vertexConsumer.pos(matrixStack, x2, bottom, 0).color(color).endVertex();
					vertexConsumer.pos(matrixStack, x2, top, 0).color(color).endVertex();
					vertexConsumer.pos(matrixStack, x1, top, 0).color(color).endVertex();
				}, true);
			}

			if (drawCross) {
				drawTexture(ReleasedDynamicTextureRegistry.CROSS_TEXTURE.get(), vertexConsumer -> {
					final float centerX = (float) Utilities.getAverage(x1, x2);
					final float centerY = (float) Utilities.getAverage(top, bottom);
					drawTexturedQuad(
							matrixStack, vertexConsumer,
							Math.max(centerX - SPRITE_SIZE / 2F, x1),
							Math.max(centerY - SPRITE_SIZE / 2F, top),
							Math.min(centerX + SPRITE_SIZE / 2F, x2),
							Math.min(centerY + SPRITE_SIZE / 2F, bottom),
							0, 0, 1, 1
					);
				});
			}
		}
	}

	public void onEdit(IntConsumer callback) {
		onMouseClickConsumer(clickEvent -> {
			if (hoverEditIndex >= 0 && hoverEditIndex < signIds.length) {
				callback.accept(hoverEditIndex);
				editingIndex = hoverEditIndex;
				guiAnimation.animate(editingIndex, ANIMATION_DURATION);
			}
		});
	}
}
