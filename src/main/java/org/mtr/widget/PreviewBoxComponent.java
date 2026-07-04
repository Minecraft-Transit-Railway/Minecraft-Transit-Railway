package org.mtr.widget;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.Drawing;

import java.awt.*;
import java.util.function.Consumer;

//? if >= 1.21.4 {
import com.mojang.blaze3d.ProjectionType;
//? } else {
/*import com.mojang.blaze3d.vertex.VertexSorting;
 *///? }

public final class PreviewBoxComponent extends SlotBackgroundComponent {

	@Nullable
	private Double panClickX;
	@Nullable
	private Double panClickY;
	private double panX;
	private double panY;

	@Nullable
	private Float rotationClickX;
	@Nullable
	private Float rotationClickY;
	private float rotationY = getPlayerPitch();
	private float rotationX = getPlayerYaw();

	private float zoom = 1;

	@Nullable
	private RenderTarget framebuffer;

	private final Consumer<PoseStack> onDraw;

	private static final int PAN_MULTIPLIER = 32;
	private static final int ROTATION_MULTIPLIER = 1;

	public PreviewBoxComponent(boolean allowPan, boolean allowRotation, boolean allowZoom, Consumer<PoseStack> onDraw) {
		setBackgroundColor(Color.BLACK);
		this.onDraw = onDraw;

		onMouseClickConsumer(clickEvent -> {
			if (allowPan && (clickEvent.getMouseButton() == 1 || !allowRotation)) {
				panClickX = panX - clickEvent.getRelativeX() / PAN_MULTIPLIER;
				panClickY = panY - clickEvent.getRelativeY() / PAN_MULTIPLIER;
			}

			if (allowRotation && (clickEvent.getMouseButton() == 0 || !allowPan)) {
				rotationClickX = rotationX - clickEvent.getRelativeX() / ROTATION_MULTIPLIER;
				rotationClickY = rotationY - clickEvent.getRelativeY() / ROTATION_MULTIPLIER;
			}
		});

		onMouseDragConsumer((x, y, mouseButton) -> {
			if (panClickX != null && panClickY != null) {
				panX = x / PAN_MULTIPLIER + panClickX;
				panY = y / PAN_MULTIPLIER + panClickY;
			}

			if (rotationClickX != null && rotationClickY != null) {
				rotationX = x / ROTATION_MULTIPLIER + rotationClickX;
				rotationY = Utilities.clampSafe(y / ROTATION_MULTIPLIER + rotationClickY, -90, 90);
			}
		});

		onMouseReleaseRunnable(() -> {
			panClickX = null;
			panClickY = null;
			rotationClickX = null;
			rotationClickY = null;
		});

		onMouseScrollConsumer(mouseScrollEvent -> {
			if (allowZoom) {
				zoom += (float) mouseScrollEvent.getDelta();
			}
		});
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		super.draw(matrixStack);
		drawFrameBuffer();
		if (framebuffer != null) {
			ImageComponentBase.drawTexture(framebuffer::getColorTextureId, vertexConsumer -> drawTexturedQuad(matrixStack, vertexConsumer, getLeft() + 1, getTop() + 1, getRight() - 1, getBottom() - 1, 0, 1, 1, 0));
		}
	}

	public void updateFrom(PreviewBoxComponent previewBoxComponent) {
		panX = previewBoxComponent.panX;
		panY = previewBoxComponent.panY;
		rotationY = previewBoxComponent.rotationY;
		rotationX = previewBoxComponent.rotationX;
		zoom = previewBoxComponent.zoom;
	}

	private void drawFrameBuffer() {
		final RenderTarget oldFrameBuffer = Minecraft.getInstance().getMainRenderTarget();
		final Matrix4f oldMatrix4f = RenderSystem.getProjectionMatrix();

//? if >= 1.21.4 {
		final ProjectionType oldProjectionType = RenderSystem.getProjectionType();
//? } else {
		/*final VertexSorting oldVertexSorting = RenderSystem.getVertexSorting();
//
*///? }

		final double scaleFactor = Minecraft.getInstance().getWindow().getGuiScale();
		final int width = (int) Math.round((getWidth() - 2) * scaleFactor);
		final int height = (int) Math.round((getHeight() - 2) * scaleFactor);

		if (framebuffer == null || framebuffer.width != width || framebuffer.height != height) {
			if (framebuffer != null) {
				framebuffer.destroyBuffers();
			}

//? if >= 1.21.4 {
			framebuffer = new TextureTarget(width, height, true);
//? } else {
			/*framebuffer = new TextureTarget(width, height, true, false);
//
*///? }

			framebuffer.setClearColor(0, 0, 0, 1);
		}

		RenderSystem.viewport(0, 0, framebuffer.width, framebuffer.height);
		framebuffer.bindWrite(true);
		RenderSystem.clearColor(0, 0, 0, 1);

//? if >= 1.21.4 {
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//? } else {
		/*RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, false);
//
*///? }

		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);

//? if >= 1.21.4 {
		RenderSystem.setProjectionMatrix(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.width / framebuffer.height, 0.01F, 1000), ProjectionType.PERSPECTIVE);
//? } else {
		/*RenderSystem.setProjectionMatrix(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.width / framebuffer.height, 0.01F, 1000), VertexSorting.DISTANCE_TO_ORIGIN);
//
*///? }

		final PoseStack matrixStack = new PoseStack();
		matrixStack.translate(panX, -panY, 10990 + zoom); // TODO figure out why is this Z offset needed?
		Drawing.rotateXDegrees(matrixStack, rotationY);
		Drawing.rotateYDegrees(matrixStack, rotationX);
		onDraw.accept(matrixStack);

		RenderSystem.disableDepthTest();
		framebuffer.unbindWrite();
		RenderSystem.viewport(0, 0, oldFrameBuffer.width, oldFrameBuffer.height);
		oldFrameBuffer.bindWrite(true);

//? if >= 1.21.4 {
		RenderSystem.setProjectionMatrix(oldMatrix4f, oldProjectionType);
//? } else {
		/*RenderSystem.setProjectionMatrix(oldMatrix4f, oldVertexSorting);
//
*///? }

	}

	private static float getPlayerYaw() {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		return clientPlayerEntity == null ? 0 : clientPlayerEntity.getYRot() + 180;
	}

	private static float getPlayerPitch() {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		return clientPlayerEntity == null ? 0 : clientPlayerEntity.getXRot();
	}
}
