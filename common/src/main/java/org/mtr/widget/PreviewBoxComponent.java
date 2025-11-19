package org.mtr.widget;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.mtr.client.IDrawing;
import org.mtr.core.tool.Utilities;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.Consumer;

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
	private Framebuffer framebuffer;

	private final Consumer<MatrixStack> onDraw;

	private static final int PAN_MULTIPLIER = 32;
	private static final int ROTATION_MULTIPLIER = 1;

	public PreviewBoxComponent(boolean allowPan, boolean allowRotation, boolean allowZoom, Consumer<MatrixStack> onDraw) {
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
			ImageComponentBase.drawTexture(framebuffer::getColorAttachment, vertexConsumer -> drawTexturedQuad(matrixStack, vertexConsumer, getLeft() + 1, getTop() + 1, getRight() - 1, getBottom() - 1, 0, 1, 1, 0));
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
		final Framebuffer oldFrameBuffer = MinecraftClient.getInstance().getFramebuffer();
		final Matrix4f oldMatrix4f = RenderSystem.getProjectionMatrix();
		final ProjectionType oldProjectionType = RenderSystem.getProjectionType();

		final double scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();
		final int width = (int) Math.round((getWidth() - 2) * scaleFactor);
		final int height = (int) Math.round((getHeight() - 2) * scaleFactor);

		if (framebuffer == null || framebuffer.textureWidth != width || framebuffer.textureHeight != height) {
			if (framebuffer != null) {
				framebuffer.delete();
			}
			framebuffer = new SimpleFramebuffer(width, height, true);
			framebuffer.setClearColor(0, 0, 0, 1);
		}

		RenderSystem.viewport(0, 0, framebuffer.textureWidth, framebuffer.textureHeight);
		framebuffer.beginWrite(true);
		RenderSystem.clearColor(0, 0, 0, 1);
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setProjectionMatrix(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.textureWidth / framebuffer.textureHeight, 0.01F, 1000), ProjectionType.PERSPECTIVE);

		final MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(panX, -panY, 10990 + zoom); // TODO figure out why is this Z offset needed?
		IDrawing.rotateXDegrees(matrixStack, rotationY);
		IDrawing.rotateYDegrees(matrixStack, rotationX);
		onDraw.accept(matrixStack);

		RenderSystem.disableDepthTest();
		framebuffer.endWrite();
		RenderSystem.viewport(0, 0, oldFrameBuffer.textureWidth, oldFrameBuffer.textureHeight);
		oldFrameBuffer.beginWrite(true);
		RenderSystem.setProjectionMatrix(oldMatrix4f, oldProjectionType);
	}

	private static float getPlayerYaw() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		return clientPlayerEntity == null ? 0 : clientPlayerEntity.getYaw() + 180;
	}

	private static float getPlayerPitch() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		return clientPlayerEntity == null ? 0 : clientPlayerEntity.getPitch();
	}
}
