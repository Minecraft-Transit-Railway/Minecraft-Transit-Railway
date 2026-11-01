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

//? if >= 26.1 {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
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

//? if >= 26.1 {
	/*// A projection is handed to the renderer as a slice of a uniform buffer now rather than as a
	// matrix, and that buffer has to be held somewhere. One is shared by every preview box: the
	// matrix is written afresh on each pass and consumed by the draws that same pass, and previews
	// are drawn one after another on the render thread, so none of them can see another's.
	private static final ProjectionMatrixBuffer PROJECTION_MATRIX_BUFFER = new ProjectionMatrixBuffer("MTR preview");
*///? }

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
//? if >= 26.1 {
			/*// A render target carries a GPU texture rather than a name. The drawing below wants the
			// OpenGL name, which the OpenGL backend's texture still answers with, and this file already
			// speaks to that backend directly for its buffer bits.
			ImageComponentBase.drawTexture(() -> ((GlTexture) framebuffer.getColorTexture()).glId(), vertexConsumer -> drawTexturedQuad(matrixStack, vertexConsumer, getLeft() + 1, getTop() + 1, getRight() - 1, getBottom() - 1, 0, 1, 1, 0));
*///? } else {
			ImageComponentBase.drawTexture(framebuffer::getColorTextureId, vertexConsumer -> drawTexturedQuad(matrixStack, vertexConsumer, getLeft() + 1, getTop() + 1, getRight() - 1, getBottom() - 1, 0, 1, 1, 0));
//? }
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
//? if >= 26.1 {
		/*// Nothing is bound any more. Which target is drawn into is stated by overriding the output
		// textures, so that is what has to be saved and put back rather than the main target.
		final GpuBufferSlice oldProjectionMatrix = RenderSystem.getProjectionMatrixBuffer();
		final ProjectionType oldProjectionType = RenderSystem.getProjectionType();
		final GpuTextureView oldColorTextureOverride = RenderSystem.outputColorTextureOverride;
		final GpuTextureView oldDepthTextureOverride = RenderSystem.outputDepthTextureOverride;
*///? } else if >= 1.21.4 {
		final RenderTarget oldFrameBuffer = Minecraft.getInstance().getMainRenderTarget();
		final Matrix4f oldMatrix4f = RenderSystem.getProjectionMatrix();
		final ProjectionType oldProjectionType = RenderSystem.getProjectionType();
//? } else {
		/*final RenderTarget oldFrameBuffer = Minecraft.getInstance().getMainRenderTarget();
		final Matrix4f oldMatrix4f = RenderSystem.getProjectionMatrix();
		final VertexSorting oldVertexSorting = RenderSystem.getVertexSorting();
*///? }

		final double scaleFactor = Minecraft.getInstance().getWindow().getGuiScale();
		final int width = (int) Math.round((getWidth() - 2) * scaleFactor);
		final int height = (int) Math.round((getHeight() - 2) * scaleFactor);

		if (framebuffer == null || framebuffer.width != width || framebuffer.height != height) {
			if (framebuffer != null) {
				framebuffer.destroyBuffers();
			}

//? if >= 26.1 {
			/*// The clear colour is given to the clear itself now instead of being held on the target.
			framebuffer = new TextureTarget(width, height, true);
*///? } else if >= 1.21.4 {
			framebuffer = new TextureTarget(width, height, true);
			framebuffer.setClearColor(0, 0, 0, 1);
//? } else {
			/*framebuffer = new TextureTarget(width, height, true, false);
			framebuffer.setClearColor(0, 0, 0, 1);
*///? }
		}

//? if >= 26.1 {
		/*// The clear names the textures it applies to instead of following whatever is bound, and the
		// viewport comes from the size of those textures. Depth testing and depth writing are decided
		// by the pipeline each draw declares, so there is nothing left to switch on around the draw.
		//
		// The perspective is unchanged; only the way it is handed over is. It is written into the
		// shared uniform buffer, and the slice that comes back names where it landed.
		RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(framebuffer.getColorTexture(), 0xFF000000, framebuffer.getDepthTexture(), 1);
		RenderSystem.setProjectionMatrix(PROJECTION_MATRIX_BUFFER.getBuffer(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.width / framebuffer.height, 0.01F, 1000)), ProjectionType.PERSPECTIVE);
		RenderSystem.outputColorTextureOverride = framebuffer.getColorTextureView();
		RenderSystem.outputDepthTextureOverride = framebuffer.getDepthTextureView();
*///? } else if >= 1.21.4 {
		RenderSystem.viewport(0, 0, framebuffer.width, framebuffer.height);
		framebuffer.bindWrite(true);
		RenderSystem.clearColor(0, 0, 0, 1);
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setProjectionMatrix(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.width / framebuffer.height, 0.01F, 1000), ProjectionType.PERSPECTIVE);
//? } else {
		/*RenderSystem.viewport(0, 0, framebuffer.width, framebuffer.height);
		framebuffer.bindWrite(true);
		RenderSystem.clearColor(0, 0, 0, 1);
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, false);
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setProjectionMatrix(new Matrix4f().perspective((float) Math.toRadians(60), (float) framebuffer.width / framebuffer.height, 0.01F, 1000), VertexSorting.DISTANCE_TO_ORIGIN);
*///? }

		final PoseStack matrixStack = new PoseStack();
		matrixStack.translate(panX, -panY, 10990 + zoom); // TODO figure out why is this Z offset needed?
		Drawing.rotateXDegrees(matrixStack, rotationY);
		Drawing.rotateYDegrees(matrixStack, rotationX);
		onDraw.accept(matrixStack);

//? if >= 26.1 {
		/*RenderSystem.outputColorTextureOverride = oldColorTextureOverride;
		RenderSystem.outputDepthTextureOverride = oldDepthTextureOverride;
		RenderSystem.setProjectionMatrix(oldProjectionMatrix, oldProjectionType);
*///? } else if >= 1.21.4 {
		RenderSystem.disableDepthTest();
		framebuffer.unbindWrite();
		RenderSystem.viewport(0, 0, oldFrameBuffer.width, oldFrameBuffer.height);
		oldFrameBuffer.bindWrite(true);
		RenderSystem.setProjectionMatrix(oldMatrix4f, oldProjectionType);
//? } else {
		/*RenderSystem.disableDepthTest();
		framebuffer.unbindWrite();
		RenderSystem.viewport(0, 0, oldFrameBuffer.width, oldFrameBuffer.height);
		oldFrameBuffer.bindWrite(true);
		RenderSystem.setProjectionMatrix(oldMatrix4f, oldVertexSorting);
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
